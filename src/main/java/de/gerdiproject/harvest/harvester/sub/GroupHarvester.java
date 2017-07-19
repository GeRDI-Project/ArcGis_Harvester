package de.gerdiproject.harvest.harvester.sub;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.gerdiproject.harvest.harvester.AbstractJsonArrayHarvester;
import de.gerdiproject.harvest.harvester.JsonConst;
import de.gerdiproject.json.IJsonArray;
import de.gerdiproject.json.IJsonObject;

public class GroupHarvester extends AbstractJsonArrayHarvester
{
    private final static String GROUP_DETAILS_URL = "%s/sharing/rest/community/groups?q=(id%%3A%s)&num=1&f=json";

    private final static String MAPS_URL = "%s/sharing/rest/search?q=%%20group%%3A%s%%20&sortField=title&sortOrder=asc&start=%d&num=100&f=json";

    private final static Pattern YEAR_PATTERN = Pattern.compile("\\d\\d\\d\\d");

    private final static String VIEW_URL = "%s/home/item.html?id=%s";
    private final static String LOGO_URL = "%s/sharing/rest/content/items/%s/info/%s";


    private final String baseUrl;
    private final String groupId;
    private final List<String> groupTags;

    /**
     * Creates a (sub-) harvester for a group of maps. Each group has a unique
     * groupId.
     *
     * @param harvestedDocuments
     *            the list in which harvested documents are stored
     * @param groupName
     *            the title of the group of maps that is to be harvested
     * @param groupId
     *            the unique ID of the group of maps that is to be harvested
     */
    public GroupHarvester(String baseUrl, String groupName, String groupId)
    {
        super(groupName, 1);

        this.baseUrl = baseUrl;
        this.groupId = groupId;
        this.groupTags = new LinkedList<>();
    }


    /**
     * Aside from the inherited functionality, a list of shared search tags that
     * relate to the map group is retrieved.
     */
    @Override
    public void init()
    {
        super.init();
        initGroupTags();
    }

    /**
     * Retrieves all maps of the specified group. Maps can only be retrieved in
     * batches of MAX_ENTRIES_PER_REQUEST. Therefore, each batch of maps must be
     * merged into a single array.
     *
     * @return
     */
    protected IJsonArray getJsonArray()
    {
        IJsonArray entries = jsonBuilder.createArray();

        int startIndex = 1;

        do {
            // request a batch of 100 maps
            String mapsUrl = String.format(MAPS_URL, baseUrl, groupId, startIndex);

            IJsonObject mapsObj = httpRequester.getRawJsonFromUrl(mapsUrl);

            // get maps array
            IJsonArray maps = mapsObj.getJsonArray(JsonConst.RESULTS);

            // add maps to the builder
            entries.addAll(maps);

            // get the startIndex of the next batch request
            startIndex = mapsObj.getInt(JsonConst.NEXT_START);
        } while (startIndex != -1);

        return entries;
    }

    /**
     * Creates a document out of a single map JSON object.
     */
    @Override
    protected List<IJsonObject> harvestJsonArrayEntry(IJsonObject map)
    {
        // retrieve document fields
        String label = getLabel(map);
        IJsonArray description = getDescription(map);
        Date lastUpdate = getLastUpdate(map);

        IJsonArray geo = createGeoJsonArray(map);
        IJsonArray years = createYears(map);
        IJsonArray tags = createMapTags(map, groupTags);
        String viewUrl = createViewUrl(map);
        String logoUrl = createLogoUrl(map);

        IJsonObject doc = searchIndexFactory.createSearchableDocument(label, lastUpdate, viewUrl, null, logoUrl,
                                                                      description, geo, years, tags);

        // return document
        List<IJsonObject> docList = new LinkedList<>();
        docList.add(doc);
        return docList;
    }

    @Override
    public List<String> getValidProperties()
    {
        return null;
    }

    /**
     * Retrieves the title of a map.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return a label outlining the content of the map
     */
    protected String getLabel(IJsonObject map)
    {
        if (map.isNonEmptyValue(JsonConst.TITLE))
            return map.getString(JsonConst.TITLE);

        return null;
    }

    /**
     * Retrieves the description of a map.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return a label outlining the content of the map
     */
    protected IJsonArray getDescription(IJsonObject map)
    {
        if (map.isNonEmptyValue(JsonConst.DESCRIPTION))
            return jsonBuilder.createArrayFromObjects(map.getString(JsonConst.DESCRIPTION));

        return null;
    }

    /**
     * Retrieves the date at which a map was last modified.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return a SQL date
     */
    protected Date getLastUpdate(IJsonObject map)
    {
        int unixTimestamp = map.getInt(JsonConst.MODIFIED);
        return new Date(unixTimestamp);
    }

    /**
     * Assembles the view URL that points to the website where a GIS map is
     * located.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return an URL String
     */
    protected String createViewUrl(IJsonObject map)
    {
        final String mapId = map.getString(JsonConst.ID);
        return String.format(VIEW_URL, baseUrl, mapId);
    }

    /**
     * Assembles the logo URL that points to a thumbnail image of a GIS map.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return an URL String
     */
    protected String createLogoUrl(IJsonObject map)
    {
        if (map.isNonEmptyValue(JsonConst.THUMBNAIL)) {
            final String thumbnailPath = map.getString(JsonConst.THUMBNAIL);
            final String mapId = map.getString(JsonConst.ID);

            return String.format(LOGO_URL, baseUrl, mapId, thumbnailPath);
        }

        return null;
    }

    /**
     * Creates a geo JSON object of a single ArcGIS map.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return a geo JSON object that describes the bounding box of the map
     *         content
     */
    protected IJsonArray createGeoJsonArray(IJsonObject map)
    {
        if (!map.isNonEmptyValue(JsonConst.EXTENT))
            return null;

        // get the two points that describe the map boundaries
        IJsonArray extent = map.getJsonArray(JsonConst.EXTENT);

        IJsonArray northWest = extent.getJsonArray(0);
        IJsonArray southEast = extent.getJsonArray(1);

        double longitudeWest = northWest.getDouble(0);
        double latitudeNorth = northWest.getDouble(1);
        double longitudeEast = southEast.getDouble(0);
        double latitudeSouth = southEast.getDouble(1);

        IJsonObject geoJson = jsonBuilder.geoBuilder().createRectangle(longitudeWest, latitudeNorth, longitudeEast, latitudeSouth);

        return jsonBuilder.createArrayFromObjects(geoJson);
    }

    /**
     * Creates a year array for a single ArcGIS map.
     *
     * @param map
     *            a JSON object containing map metadata
     * @return a JSON array of years for a map
     */
    protected IJsonArray createYears(IJsonObject map)
    {
        IJsonArray yearsArray = jsonBuilder.createArray();

        // get map tags
        IJsonArray mapTags = map.getJsonArray(JsonConst.TAGS);

        // look for years in the map tags
        for (Object mapVal : mapTags) {
            String mapTag = (String) mapVal;

            // only add tag if it is not a year
            if (YEAR_PATTERN.matcher(mapTag).matches()) {
                // convert tag to integer and add it
                yearsArray.add(Integer.parseInt(mapTag));
            }
        }

        // build and return array
        return yearsArray;
    }

    /**
     * Creates a tag array for a single ArcGIS map.
     *
     * @param map
     *            a JSON object containing map metadata
     * @param groupTags
     *            a list of tags of the map group that contains the map
     * @return a JSON array of tags for a map
     */
    protected IJsonArray createMapTags(IJsonObject map, List<String> groupTags)
    {
        IJsonArray mapTagsArray = jsonBuilder.createArray();

        // add group tags
        groupTags.forEach((String groupTag) -> mapTagsArray.add(groupTag));

        // add map tags
        IJsonArray mapTags = map.getJsonArray(JsonConst.TAGS);

        for (Object mapVal : mapTags) {
            String mapTag = (String) mapVal;

            // only add tag if it is not a year
            if (!YEAR_PATTERN.matcher(mapTag.toString()).matches())
                mapTagsArray.add(mapTag);
        }

        // add map type
        mapTagsArray.add(map.getString(JsonConst.TYPE));

        // build and return array
        return mapTagsArray;
    }

    protected void initGroupTags()
    {
        String groupDetailsUrl = String.format(GROUP_DETAILS_URL, baseUrl, groupId);

        // get detailed group info for all groups (this harvest only has one!)
        IJsonObject multiGroupDetailsObj = httpRequester.getRawJsonFromUrl(groupDetailsUrl);
        IJsonArray multiGroupDetailsArray = multiGroupDetailsObj.getJsonArray(JsonConst.RESULTS);

        // retrieve this group from the array of groups
        IJsonObject groupDetails = multiGroupDetailsArray.getJsonObject(0);

        // clear old tags
        this.groupTags.clear();

        // add group title
        this.groupTags.add(groupDetails.getString(JsonConst.TITLE));

        // add group detail tags
        IJsonArray groupDetailTags = groupDetails.getJsonArray(JsonConst.TAGS);
        groupDetailTags.forEach((Object gt) -> this.groupTags.add((String) gt));
    }
}

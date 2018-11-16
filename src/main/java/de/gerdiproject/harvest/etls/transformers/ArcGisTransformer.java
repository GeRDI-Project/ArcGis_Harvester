/*
 *  Copyright © 2018 Robin Weiss (http://www.gerdi-project.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package de.gerdiproject.harvest.etls.transformers;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.constants.ArcGisDataCiteConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.json.ArcGisUser;
import de.gerdiproject.harvest.arcgis.utils.ArcGisLinkAssembler;
import de.gerdiproject.harvest.etls.extractors.ArcGisMapVO;
import de.gerdiproject.json.datacite.Creator;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Date;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.Rights;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.abstr.AbstractDate;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.enums.NameType;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.enums.TitleType;
import de.gerdiproject.json.datacite.extension.ResearchData;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.nested.PersonName;
import de.gerdiproject.json.geo.Point;

/**
 * This {@linkplain AbstractIteratorTransformer} transforms {@linkplain ArcGisMap}s
 * to {@linkplain DataCiteJson} objects.
 *
 * @author Robin Weiss
 */
public class ArcGisTransformer extends AbstractIteratorTransformer<ArcGisMapVO, DataCiteJson>
{
    private List<Subject> groupRelatedSubjects;
    private final String baseUrl;


    /**
     * Constructor that requires an URL and a query parameter.
     *
     * @param baseUrl the ArcGis base URL
     */
    public ArcGisTransformer(String baseUrl)
    {
        super();
        this.baseUrl = baseUrl;
    }


    @Override
    protected DataCiteJson transformElement(ArcGisMapVO vo) throws TransformerException
    {
        if (this.groupRelatedSubjects == null)
            this.groupRelatedSubjects = createGroupTags(vo.getFeaturedGroups());

        final ArcGisMap map = vo.getMap();
        final DataCiteJson doc = new DataCiteJson(map.getId());

        doc.setLanguage(map.getCulture());
        doc.setPublisher(ArcGisDataCiteConstants.PUBLISHER);
        doc.setRepositoryIdentifier(ArcGisDataCiteConstants.REPOSITORY_ID);
        doc.addResearchDisciplines(ArcGisDataCiteConstants.RESEARCH_DISCIPLINES);
        doc.addSubjects(groupRelatedSubjects);
        doc.addSubjects(getSubjects(map));
        doc.addTitles(getTitles(map));
        doc.addDates(getDates(map));
        doc.addDescriptions(getDescriptions(map));
        doc.addCreators(getCreators(vo.getOwner()));
        doc.addGeoLocations(getGeoLocations(map));
        doc.addRights(getRightsList(map));
        doc.addWebLinks(getWebLinks(map));
        doc.addResearchDataList(getResearchData(map));
        doc.setResourceType(getResourceType(map));

        return doc;
    }


    /**
     * Retrieves the resource type of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return the resource type of a map
     */
    private ResourceType getResourceType(ArcGisMap map)
    {
        ResourceType resourceType = null;
        String typeName = map.getType();

        if (typeName != null)
            resourceType = new ResourceType(typeName, ResourceTypeGeneral.Model);

        return resourceType;
    }


    /**
     * Retrieves the titles of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return a list of all titles of the map
     */
    private List<Title> getTitles(ArcGisMap map)
    {
        List<Title> titles = new LinkedList<>();

        // check if a title exists
        String titleText = map.getTitle();

        if (titleText != null) {
            Title mainTitle = new Title(titleText);
            mainTitle.setLang(map.getCulture());

            titles.add(mainTitle);
        }

        // check if an alternative title exists
        String altTitleText = map.getName();

        if (altTitleText != null) {
            Title alternativeTitle = new Title(altTitleText);
            alternativeTitle.setType(TitleType.AlternativeTitle);
            alternativeTitle.setLang(map.getCulture());

            titles.add(alternativeTitle);
        }

        return titles;
    }


    /**
     * Retrieves the descriptions of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return a list of all map descriptions
     */
    private List<Description> getDescriptions(ArcGisMap map)
    {
        List<Description> descriptions = new LinkedList<>();

        // get full description
        String descriptionText = map.getDescription();

        if (descriptionText != null) {
            Description fullDescription = new Description(descriptionText, DescriptionType.Abstract);
            fullDescription.setLang(map.getCulture());
            descriptions.add(fullDescription);
        }

        // get snippet description
        String snippetText = map.getSnippet();

        if (snippetText != null) {
            Description snippetDescription = new Description(snippetText, DescriptionType.Abstract);
            snippetDescription.setLang(map.getCulture());
            descriptions.add(snippetDescription);
        }

        return descriptions;
    }


    /**
     * Retrieves relevant dates of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return a list of relevant date
     */
    private List<AbstractDate> getDates(ArcGisMap map)
    {
        List<AbstractDate> dates = new LinkedList<>();

        // add the date of the creation of the map
        dates.add(new Date(map.getCreated(), DateType.Created));

        // add the date of the last modification of the map
        dates.add(new Date(map.getModified(), DateType.Updated));

        // add dates that relate to the map data itself

        // get map tags
        List<String> mapTags = map.getTags();
        Pattern yearPattern = ArcGisConstants.YEAR_PATTERN;

        // look for years in the map tags
        Calendar cal = Calendar.getInstance();

        for (String tag : mapTags) {

            // check if the tag is a year
            if (yearPattern.matcher(tag).matches()) {
                int year = Integer.parseInt(tag);

                // convert year to timestamp
                cal.set(year, 0, 1);

                // add year to dates
                dates.add(new Date(cal.getTimeInMillis(), DateType.Collected));
            }
        }

        return dates;
    }


    /**
     * Retrieves the creator(s) of a map.
     *
     * @param owner the owner of the map
     *
     * @return a list of creators
     */
    private List<Creator> getCreators(ArcGisUser owner)
    {
        final PersonName name = new PersonName(owner.getFullName(), NameType.Personal);
        final Creator creator = new Creator(name);
        creator.setGivenName(owner.getFirstName());
        creator.setFamilyName(owner.getLastName());

        if (owner.getProvider() != null)
            creator.addAffiliations(Arrays.asList(owner.getProvider()));

        return Arrays.asList(creator);
    }


    /**
     * Retrieves a list of (licensing) rights of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return a list of rights of a map,
     * or null if the map does not provide any rights
     */
    private List<Rights> getRightsList(ArcGisMap map)
    {
        List<Rights> rightsList = null;
        String licenseInfo = map.getLicenseInfo();

        if (licenseInfo != null) {
            Rights licenseRights = new Rights(licenseInfo);

            rightsList = new LinkedList<>();
            rightsList.add(licenseRights);
        }

        return rightsList;
    }


    /**
     * Creates a {@linkplain GeoLocation} of the extent of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return a GeoLocation that includes the bounding box of the map,
     * or null if the map does not provide any geo data
     */
    private List<GeoLocation> getGeoLocations(ArcGisMap map)
    {
        // get the two points that describe the map boundaries
        List<Point> extent = map.getExtent();

        if (extent == null || extent.isEmpty())
            return null;

        Point northWest = extent.get(0);
        Point southEast = extent.get(1);

        // create box
        GeoLocation geoBox = new GeoLocation();
        geoBox.setBox(northWest.getLongitude(), southEast.getLongitude(), southEast.getLatitude(), northWest.getLongitude());

        List<GeoLocation> geoList = new LinkedList<>();
        geoList.add(geoBox);
        return geoList;
    }


    /**
     * Creates a {@linkplain Subject} list for a map.
     *
     * @param map a JSON object containing map metadata
     * @param groupTags a list of tags of the map group that contains the map
     * @return a JSON array of tags for a map
     */
    private List<Subject> getSubjects(ArcGisMap map)
    {
        List<Subject> subjects = new LinkedList<>();

        Pattern yearPattern = ArcGisConstants.YEAR_PATTERN;
        String language = map.getCulture();
        List<String> tags = map.getTags();

        // add tags
        tags.forEach((String tag) -> {
            // only add tag if it is not a year
            if (!yearPattern.matcher(tag).matches())
            {
                Subject s = new Subject(tag);
                s.setLang(language);
                subjects.add(s);
            }
        });

        // add type keywords
        tags = map.getTypeKeywords();
        tags.forEach((String tag) ->
                     subjects.add(new Subject(tag))
                    );

        // add spatial reference
        String spatialRefName = map.getSpatialReference();

        if (spatialRefName != null)
            subjects.add(new Subject(spatialRefName));

        return subjects;
    }


    /**
     * Generates a list of {@linkplain WebLink}s that are related to a specified map.
     *
     * @param map the map for which the links are being generated
     * @param baseUrl the host of the map gallery
     *
     * @return a list of weblinks that are related to the map
     */
    private List<WebLink> getWebLinks(ArcGisMap map)
    {
        final String mapId = map.getId();
        final String mapType = map.getType();
        final String mapUrl = map.getUrl();
        final String thumbnailPath = map.getThumbnail();
        final String largeThumbnailPath = map.getLargeThumbnail();
        final List<String> keywords = map.getTypeKeywords();

        // add links
        List<WebLink> webLinks = new LinkedList<>();

        webLinks.add(ArcGisDataCiteConstants.ESRI_LOGO_LINK);
        webLinks.add(ArcGisLinkAssembler.getViewLink(mapId, baseUrl));
        webLinks.add(ArcGisLinkAssembler.getThumbnailLink(mapId, thumbnailPath, largeThumbnailPath, baseUrl));
        webLinks.add(ArcGisLinkAssembler.getSceneViewerLink(mapType, mapId));
        webLinks.add(ArcGisLinkAssembler.getMapViewerLink(mapType, mapId));
        webLinks.add(ArcGisLinkAssembler.getStyleViewerLink(mapType, mapId));
        webLinks.add(ArcGisLinkAssembler.getMetadataLink(mapId, keywords));
        webLinks.add(ArcGisLinkAssembler.getApplicationViewLink(mapType, mapUrl));
        webLinks.add(ArcGisLinkAssembler.getOpenDocumentLink(mapType, mapUrl));

        // remove null links
        webLinks.removeIf((WebLink link) -> link == null);

        return webLinks;
    }


    /**
     * Generates a list of {@linkplain ResearchData} that are related to a map.
     *
     * @param map the map for which the files are being generated
     *
     * @return a list of {@linkplain ResearchData} that are related to a map
     */
    private List<ResearchData> getResearchData(ArcGisMap map)
    {
        List<ResearchData> files = new LinkedList<>();

        String mapId = map.getId();
        String mapType = map.getType();

        ResearchData arcGisDesktop = ArcGisLinkAssembler.getArcGisDesktopLink(mapType, mapId);

        if (arcGisDesktop != null)
            files.add(arcGisDesktop);

        ResearchData download = ArcGisLinkAssembler.getDownloadLink(mapType, mapId, map.getName());

        if (download != null)
            files.add(download);

        return files.isEmpty() ? null : files;
    }

    /**
     * Creates a list of {@linkplain Subject}s that are related to groups of maps.
     *
     * @param groups a list of groups of which the subjects are to be retrieved
     *
     * @return a list of {@linkplain Subject}s that are related to groups of maps
     */
    private List<Subject> createGroupTags(List<ArcGisFeaturedGroup> groups)
    {
        List<Subject> subjects = new LinkedList<>();

        // convert each tag of each group to a subject
        groups.forEach((ArcGisFeaturedGroup group) ->
                       group.getTags().forEach((String tag) ->
                                               subjects.add(new Subject(tag))
                                              )
                      );
        return subjects;
    }
}

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
import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.etls.extractors.ArcGisMapVO;
import de.gerdiproject.harvest.etls.transformers.utils.ArcGisLinkHelper;
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
import de.gerdiproject.json.datacite.extension.generic.ResearchData;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
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
    public ArcGisTransformer(final String baseUrl)
    {
        super();
        this.baseUrl = baseUrl;
    }


    @Override
    public void init(final AbstractETL<?, ?> etl)
    {
        // nothing to initialize
    }


    @Override
    protected DataCiteJson transformElement(final ArcGisMapVO vo) throws TransformerException
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
        doc.addResearchData(getResearchData(map));
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
    private ResourceType getResourceType(final ArcGisMap map)
    {
        ResourceType resourceType = null;
        final String typeName = map.getType();

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
    private List<Title> getTitles(final ArcGisMap map)
    {
        final List<Title> titles = new LinkedList<>();

        // check if a title exists
        final String titleText = map.getTitle();

        if (titleText != null) {
            final Title mainTitle = new Title(titleText);
            mainTitle.setLang(map.getCulture());

            titles.add(mainTitle);
        }

        // check if an alternative title exists
        final String altTitleText = map.getName();

        if (altTitleText != null) {
            final Title alternativeTitle = new Title(altTitleText);
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
    private List<Description> getDescriptions(final ArcGisMap map)
    {
        final List<Description> descriptions = new LinkedList<>();

        // get full description
        final String descriptionText = map.getDescription();

        if (descriptionText != null) {
            final Description fullDescription = new Description(descriptionText, DescriptionType.Abstract);
            fullDescription.setLang(map.getCulture());
            descriptions.add(fullDescription);
        }

        // get snippet description
        final String snippetText = map.getSnippet();

        if (snippetText != null) {
            final Description snippetDescription = new Description(snippetText, DescriptionType.Abstract);
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
    private List<AbstractDate> getDates(final ArcGisMap map)
    {
        final List<AbstractDate> dates = new LinkedList<>();

        // add the date of the creation of the map
        dates.add(new Date(map.getCreated(), DateType.Created));

        // add the date of the last modification of the map
        dates.add(new Date(map.getModified(), DateType.Updated));

        // add dates that relate to the map data itself

        // get map tags
        final List<String> mapTags = map.getTags();
        final Pattern yearPattern = ArcGisConstants.YEAR_PATTERN;

        // look for years in the map tags
        final Calendar cal = Calendar.getInstance();

        for (final String tag : mapTags) {

            // check if the tag is a year
            if (yearPattern.matcher(tag).matches()) {
                final int year = Integer.parseInt(tag);

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
    private List<Creator> getCreators(final ArcGisUser owner)
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
    private List<Rights> getRightsList(final ArcGisMap map)
    {
        List<Rights> rightsList = null;
        final String licenseInfo = map.getLicenseInfo();

        if (licenseInfo != null) {
            final Rights licenseRights = new Rights(licenseInfo);

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
    private List<GeoLocation> getGeoLocations(final ArcGisMap map)
    {
        // get the two points that describe the map boundaries
        final List<Point> extent = map.getExtent();

        if (extent == null || extent.isEmpty())
            return null;

        final Point northWest = extent.get(0);
        final Point southEast = extent.get(1);

        // create box
        final GeoLocation geoBox = new GeoLocation();
        geoBox.setBox(northWest.getLongitude(), southEast.getLongitude(), southEast.getLatitude(), northWest.getLongitude());

        final List<GeoLocation> geoList = new LinkedList<>();
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
    private List<Subject> getSubjects(final ArcGisMap map)
    {
        final List<Subject> subjects = new LinkedList<>();

        final Pattern yearPattern = ArcGisConstants.YEAR_PATTERN;
        final String language = map.getCulture();
        List<String> tags = map.getTags();

        // add tags
        tags.forEach((final String tag) -> {
            // only add tag if it is not a year
            if (!yearPattern.matcher(tag).matches())
            {
                final Subject s = new Subject(tag);
                s.setLang(language);
                subjects.add(s);
            }
        });

        // add type keywords
        tags = map.getTypeKeywords();
        tags.forEach((final String tag) ->
                     subjects.add(new Subject(tag))
                    );

        // add spatial reference
        final String spatialRefName = map.getSpatialReference();

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
    private List<WebLink> getWebLinks(final ArcGisMap map)
    {
        final String mapId = map.getId();
        final String mapType = map.getType();
        final String mapUrl = map.getUrl();
        final String thumbnailPath = map.getThumbnail();
        final String largeThumbnailPath = map.getLargeThumbnail();
        final List<String> keywords = map.getTypeKeywords();

        // add links
        final List<WebLink> webLinks = new LinkedList<>();

        webLinks.add(ArcGisDataCiteConstants.ESRI_LOGO_LINK);
        webLinks.add(ArcGisLinkHelper.getViewLink(mapId, baseUrl));
        webLinks.add(ArcGisLinkHelper.getThumbnailLink(mapId, thumbnailPath, largeThumbnailPath, baseUrl));
        webLinks.add(ArcGisLinkHelper.getSceneViewerLink(mapType, mapId));
        webLinks.add(ArcGisLinkHelper.getMapViewerLink(mapType, mapId));
        webLinks.add(ArcGisLinkHelper.getStyleViewerLink(mapType, mapId));
        webLinks.add(ArcGisLinkHelper.getMetadataLink(mapId, keywords));
        webLinks.add(ArcGisLinkHelper.getApplicationViewLink(mapType, mapUrl));
        webLinks.add(ArcGisLinkHelper.getOpenDocumentLink(mapType, mapUrl));

        // remove null links
        webLinks.removeIf((final WebLink link) -> link == null);

        return webLinks;
    }


    /**
     * Generates a list of {@linkplain ResearchData} that are related to a map.
     *
     * @param map the map for which the files are being generated
     *
     * @return a list of {@linkplain ResearchData} that are related to a map
     */
    private List<ResearchData> getResearchData(final ArcGisMap map)
    {
        final List<ResearchData> files = new LinkedList<>();

        final String mapId = map.getId();
        final String mapType = map.getType();

        final ResearchData arcGisDesktop = ArcGisLinkHelper.getArcGisDesktopLink(mapType, mapId);

        if (arcGisDesktop != null)
            files.add(arcGisDesktop);

        final ResearchData download = ArcGisLinkHelper.getDownloadLink(mapType, mapId, map.getName());

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
    private List<Subject> createGroupTags(final List<ArcGisFeaturedGroup> groups)
    {
        final List<Subject> subjects = new LinkedList<>();

        // convert each tag of each group to a subject
        groups.forEach((final ArcGisFeaturedGroup group) ->
                       group.getTags().forEach((final String tag) ->
                                               subjects.add(new Subject(tag))
                                              )
                      );
        return subjects;
    }


    @Override
    public void clear()
    {
        // nothing to clean up
    }
}

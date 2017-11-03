/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
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
package de.gerdiproject.harvest.arcgis.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.json.ArcGisUser;
import de.gerdiproject.json.datacite.Creator;
import de.gerdiproject.json.datacite.Date;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.Title;
import de.gerdiproject.json.datacite.enums.DateType;
import de.gerdiproject.json.datacite.enums.DescriptionType;
import de.gerdiproject.json.datacite.enums.NameType;
import de.gerdiproject.json.datacite.enums.ResourceTypeGeneral;
import de.gerdiproject.json.datacite.enums.TitleType;
import de.gerdiproject.json.datacite.nested.PersonName;
import de.gerdiproject.json.geo.Point;
import de.gerdiproject.json.datacite.GeoLocation;
import de.gerdiproject.json.datacite.ResourceType;
import de.gerdiproject.json.datacite.Rights;

/**
 * This static class provides methods for retrieving DataCite fields from
 * ArcGis server responses.
 *
 * @author Robin Weiss
 */
public class ArcGisMapParser
{
    /**
     * Retrieves the resource type of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return the resource type of a map
     */
    public static ResourceType getResourceType(ArcGisMap map)
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
    public static List<Title> getTitles(ArcGisMap map)
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
    public static List<Description> getDescriptions(ArcGisMap map)
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
    public static List<Date> getDates(ArcGisMap map)
    {
        List<Date> dates = new LinkedList<>();

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
                dates.add(new Date(cal, DateType.Collected));
            }
        }

        return dates;
    }


    /**
     * Retrieves the creator(s) of a map.
     *
     * @param map a JSON object containing map metadata
     *
     * @return a list of creators
     */
    public static List<Creator> getCreators(ArcGisMap map)
    {
        // download additional user info
        ArcGisUser owner = ArcGisDownloader.getUser(map.getOwner());

        PersonName name = new PersonName(owner.getFullName(), NameType.Personal);
        Creator creator = new Creator(name);
        creator.setGivenName(owner.getFirstName());
        creator.setFamilyName(owner.getLastName());

        if (owner.getProvider() != null)
            creator.setAffiliations(Arrays.asList(owner.getProvider()));

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
    public static List<Rights> getRightsList(ArcGisMap map)
    {
        List<Rights> rightsList = null;
        String licenseInfo = map.getLicenseInfo();

        if (licenseInfo != null) {
            Rights licenseRights = new Rights();
            licenseRights.setValue(licenseInfo);

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
    public static List<GeoLocation> getGeoLocations(ArcGisMap map)
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
    public static List<Subject> getSubjects(ArcGisMap map, List<Subject> groupTags)
    {
        List<Subject> subjects = new LinkedList<>();

        // add group tags
        subjects.addAll(groupTags);

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
     * Creates a list of {@linkplain Subject}s that are related to groups of maps.
     *
     * @param groups a list of groups of which the subjects are to be retrieved
     *
     * @return a list of {@linkplain Subject}s that are related to groups of maps
     */
    public static List<Subject> createGroupTags(List<ArcGisFeaturedGroup> groups)
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

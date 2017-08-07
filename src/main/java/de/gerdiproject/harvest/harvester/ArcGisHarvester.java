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
package de.gerdiproject.harvest.harvester;

import de.gerdiproject.harvest.harvester.sub.GroupHarvester;
import de.gerdiproject.harvest.utils.HttpRequester;
import de.gerdiproject.json.IJsonArray;
import de.gerdiproject.json.IJsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;


/**
 * A harvester for ArcGis (http://esri.maps.arcgis.com/home/groups.html).
 *
 * @author row
 */
public class ArcGisHarvester extends AbstractCompositeHarvester
{
    private final static String ARC_GIS_BASE_URL = "http://arcgis.com";
    private final static String ESRI_BASE_URL = "http://esri.maps.arcgis.com";
    private final static String OVERVIEW_URL_SUFFIX = "/sharing/rest/portals/self?culture=en&f=json";
    private final static String ARC_GIS_GROUP_DETAILS_URL = ARC_GIS_BASE_URL + "/sharing/rest/community/groups?q=%s&f=json";

    private final static List<String> VALID_PARAMS = new LinkedList<>();
    private final static String  ARC_GIS_SUFFIX = "ArcGisHarvester";
    private final static String  ESRI_SUFFIX = "EsriHarvester";

    /**
     * Simple constructor that passes on the inherited one.
     */
    public ArcGisHarvester()
    {
        super(createSubHarvesters());
    }

    /**
     * Creates an ArcGisHarvester instance by checking all map groups in ArcGIS and Esri first.
     * @return
     */
    private static List<AbstractHarvester> createSubHarvesters()
    {
        // create Esri harvesters
        List<AbstractHarvester> subHarvesters = createEsriHarvesters();

        // append ArcGis harvesters
        List<AbstractHarvester> arcGisSubHarvesters = createArcGisHarvesters();
        arcGisSubHarvesters.forEach((AbstractHarvester a) -> subHarvesters.add(a));

        return subHarvesters;
    }

    /**
     * Returns a list of sub-harvesters for harvesting all map groups in Esri.
     * @return a list of sub-harvesters for harvesting all map groups in Esri
     */
    private static List<AbstractHarvester> createEsriHarvesters()
    {
        // retrieve list of groups from ArcGis
        IJsonObject groupsObj = new HttpRequester().getRawJsonFromUrl(ESRI_BASE_URL + OVERVIEW_URL_SUFFIX);
        IJsonArray groups = groupsObj.getJsonArray(JsonConst.FEATURED_GROUPS);

        // init sub-harvester array
        List<AbstractHarvester> esriHarvesters = new LinkedList<>();

        // create sub-harvesters
        groups.forEach((Object o) -> {
            final IJsonObject groupObj = ((IJsonObject) o);
            final String groupId = groupObj.getString(JsonConst.ID);
            final String harvesterName = groupObj.getString(JsonConst.TITLE) + ESRI_SUFFIX;
            esriHarvesters.add(new GroupHarvester(ESRI_BASE_URL, harvesterName, groupId));
        });

        return esriHarvesters;
    }

    /**
     * Returns a list of sub-harvesters for harvesting all map groups in Esri's ArcGis sub-category.
     * @return a list of sub-harvesters for harvesting all map groups in Esri's ArcGis sub-category
     */
    private static List<AbstractHarvester> createArcGisHarvesters()
    {
        HttpRequester httpRequester = new HttpRequester();

        // get ArcGIS overview
        IJsonObject overviewObj = httpRequester.getRawJsonFromUrl(ARC_GIS_BASE_URL + OVERVIEW_URL_SUFFIX);

        // assemble URL for getting the gallery group object
        String galleryQuery = overviewObj.getString(JsonConst.LIVING_ATLAS_GROUP_QUERY);
        IJsonArray groups;

        try {
            String galleryDetailsUrl = String.format(ARC_GIS_GROUP_DETAILS_URL, URLEncoder.encode(galleryQuery, "UTF-8"));

            // retrieve details of gallery group
            groups = httpRequester.getRawJsonFromUrl(galleryDetailsUrl).getJsonArray(JsonConst.RESULTS);
        } catch (UnsupportedEncodingException e) {
            // this should never happen, because UTF-8 is a valid encoding
            return null;
        }

        // init sub-harvester array
        List<AbstractHarvester> arcGisHarvesters = new LinkedList<>();

        // create sub-harvesters
        groups.forEach((Object o) -> {
            final IJsonObject groupObj = ((IJsonObject) o);
            final String groupId = groupObj.getString(JsonConst.ID);
            final String harvesterName = groupObj.getString(JsonConst.TITLE) + ARC_GIS_SUFFIX;
            arcGisHarvesters.add(new GroupHarvester(ARC_GIS_BASE_URL, harvesterName, groupId));
        });

        return arcGisHarvesters;

    }


    @Override
    public List<String> getValidProperties()
    {
        return VALID_PARAMS;
    }



}

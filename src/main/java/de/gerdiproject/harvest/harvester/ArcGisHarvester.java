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
import de.gerdiproject.harvest.utils.ArcGisConstants;
import de.gerdiproject.harvest.utils.ArcGisDownloader;
import de.gerdiproject.json.arcgis.FeaturedGroup;

import java.util.LinkedList;
import java.util.List;


/**
 * A harvester for ArcGis (http://esri.maps.arcgis.com/home/groups.html).
 *
 * @author Robin Weiss
 */
public class ArcGisHarvester extends AbstractCompositeHarvester
{
    private final static List<String> VALID_PARAMS = new LinkedList<>();
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
        List<AbstractHarvester> subHarvesters = new LinkedList<>();

        // add Esri harvesters
        subHarvesters.addAll(createSubHarvesters(ArcGisConstants.ESRI_BASE_URL, ArcGisConstants.ESRI_SUFFIX));

        // add ArcGis harvesters
        subHarvesters.addAll(createSubHarvesters(ArcGisConstants.ARC_GIS_BASE_URL, ArcGisConstants.ARC_GIS_SUFFIX));

        return subHarvesters;
    }



    /**
     * Returns a list of sub-harvesters for harvesting all featured groups of an ArcGis area.
     *
     * @param baseUrl the URL of an ArcGis repository that contains featured groups
     * @param nameSuffix a name suffix for all sub-harvesters
     *
     * @return a list of sub-harvesters for harvesting all featured groups of an ArcGis area
     */
    private static List<AbstractHarvester> createSubHarvesters(String baseUrl, String nameSuffix)
    {
        // retrieve list of groups from ArcGis
        List<FeaturedGroup> groups = ArcGisDownloader.getFeaturedGroupsFromOverview(baseUrl);

        List<AbstractHarvester> arcGisHarvesters = new LinkedList<>();

        // create sub-harvesters
        groups.forEach((FeaturedGroup g) -> {
            final String groupId = g.getId();
            final String harvesterName = g.getTitle().replace(' ', '-') + nameSuffix;
            arcGisHarvesters.add(new GroupHarvester(baseUrl, harvesterName, groupId));
        });

        return arcGisHarvesters;
    }


    @Override
    public List<String> getValidProperties()
    {
        return VALID_PARAMS;
    }
}

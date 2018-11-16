/**
 * Copyright © 2017 Robin Weiss (http://www.gerdi-project.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gerdiproject.harvest;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.annotation.WebListener;

import de.gerdiproject.harvest.application.ContextListener;
import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.utils.ArcGisDownloader;
import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.etls.ArcGisETL;

/**
 * This class initializes the ArcGis harvester and all mandatory objects.
 *
 * @author Robin Weiss
 */
@WebListener
public class ArcGisContextListener extends ContextListener
{

    @Override
    protected List<? extends AbstractETL<?, ?>> createETLs()
    {
        List<AbstractETL<?, ?>> etlList = new LinkedList<>();

        // add Esri harvesters
        etlList.addAll(createETLsForURL(ArcGisConstants.ESRI_BASE_URL, ArcGisConstants.ESRI_SUFFIX));

        // add ArcGis harvesters
        etlList.addAll(createETLsForURL(ArcGisConstants.ARC_GIS_BASE_URL, ArcGisConstants.ARC_GIS_SUFFIX));

        return etlList;
    }


    /**
     * Creates a list of {@linkplain AbstractETL}s for harvesting all featured groups of an ArcGis host.
     *
     * @param baseUrl the host of an ArcGis repository that contains featured groups
     * @param nameSuffix a name suffix used to distinguish sub-harvesters
     *
     * @return a list of {@linkplain AbstractETL}s for harvesting all featured groups of an ArcGis host
     */
    private static List<AbstractETL<?, ?>> createETLsForURL(String baseUrl, String nameSuffix)
    {
        // retrieve list of groups from ArcGis
        List<ArcGisFeaturedGroup> groups = ArcGisDownloader.getFeaturedGroupsFromOverview(baseUrl);

        List<AbstractETL<?, ?>> arcGisHarvesters = new LinkedList<>();

        // create sub-harvesters
        for (ArcGisFeaturedGroup g : groups) {
            final String groupId = g.getId();
            final String harvesterName = g.getTitle().replace(' ', '-') + nameSuffix;

            arcGisHarvesters.add(new ArcGisETL(harvesterName, baseUrl, groupId));
        }

        return arcGisHarvesters;
    }
}

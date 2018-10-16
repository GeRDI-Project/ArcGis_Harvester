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
package de.gerdiproject.harvest.etl;

import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.etls.StaticIteratorETL;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * Harvests a group of maps from ArcGis.
 *
 * @author Robin Weiss
 */
public class ArcGisETL extends StaticIteratorETL<ArcGisMap, DataCiteJson>
{
    /**
     * Creates an ETL for a group of maps. Each group has a unique groupId.
     *
     * @param groupName the title of the group of maps that is to be harvested
     * @param baseUrl the host of the maps
     * @param groupId the unique ID of the group of maps that is to be harvested
     */
    public ArcGisETL(String groupName, String baseUrl, String groupId)
    {
        super(
            groupName,
            new ArcGisExtractor(baseUrl, groupId),
            new ArcGisTransformer(baseUrl, groupId)
        );
    }
}

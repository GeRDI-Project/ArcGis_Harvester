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
package de.gerdiproject.harvest.etls;

import de.gerdiproject.harvest.etls.extractors.ArcGisExtractor;
import de.gerdiproject.harvest.etls.extractors.ArcGisMapVO;
import de.gerdiproject.harvest.etls.transformers.ArcGisTransformer;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * This ETL can harvest a group of maps from ArcGis.
 *
 * @author Robin Weiss
 */
public class ArcGisETL extends StaticIteratorETL<ArcGisMapVO, DataCiteJson>
{
    /**
     * Creates an ETL for a group of maps. Each group has a unique groupId.
     *
     * @param groupName the title of the group of maps that is to be harvested
     * @param baseUrl the host of the maps
     * @param groupId the unique ID of the group of maps that is to be harvested
     */
    public ArcGisETL(final String groupName, final String baseUrl, final String groupId)
    {
        super(
            groupName,
            new ArcGisExtractor(baseUrl, groupId),
            new ArcGisTransformer(baseUrl)
        );
    }
}

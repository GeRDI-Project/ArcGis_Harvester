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
package de.gerdiproject.harvest.harvester.subHarvesters;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.constants.ArcGisDataCiteConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.json.compound.ArcGisMapsResponse;
import de.gerdiproject.harvest.arcgis.utils.ArcGisDownloader;
import de.gerdiproject.harvest.arcgis.utils.ArcGisLinkAssembler;
import de.gerdiproject.harvest.arcgis.utils.ArcGisMapParser;
import de.gerdiproject.harvest.harvester.AbstractListHarvester;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Subject;

/**
 * Harvests a group of maps from ArcGis.
 *
 * @author Robin Weiss
 */
public class ArcGisFeaturedGroupHarvester extends AbstractListHarvester<ArcGisMap>
{
    private final String baseUrl;
    private final String groupId;
    private final List<Subject> groupTags;


    /**
     * Creates a (sub-) harvester for a group of maps. Each group has a unique
     * groupId.
     *
     * @param baseUrl
     *            the host of the maps
     * @param groupName
     *            the title of the group of maps that is to be harvested
     * @param groupId
     *            the unique ID of the group of maps that is to be harvested
     */
    public ArcGisFeaturedGroupHarvester(String baseUrl, String groupName, String groupId)
    {
        super(groupName, 1);

        this.baseUrl = baseUrl;
        this.groupId = groupId;

        // get group details
        List<ArcGisFeaturedGroup> featuredGroups = ArcGisDownloader.getFeaturedGroupsByQuery(baseUrl, groupId);

        // parse generic search terms from group details
        this.groupTags = ArcGisMapParser.createGroupTags(featuredGroups);
    }


    /**
     * Retrieves all maps in the group. Maps can only be retrieved in
     * batches of 100. Therefore, each batch of maps must be merged into a single array.
     *
     * @return all maps in the group
     */
    @Override
    protected Collection<ArcGisMap> loadEntries()
    {
        Collection<ArcGisMap> entries = new LinkedList<>();

        int startIndex = 1;

        do {
            // request a batch of 100 maps
            String mapsUrl = String.format(ArcGisConstants.MAPS_URL, baseUrl, groupId, startIndex);
            ArcGisMapsResponse mapsQueryResult = httpRequester.getObjectFromUrl(mapsUrl, ArcGisMapsResponse.class);

            // get maps array
            List<ArcGisMap> maps = mapsQueryResult.getResults();

            // add maps to the builder
            entries.addAll(maps);

            // get the startIndex of the next batch request
            startIndex = mapsQueryResult.getNextStart();
        } while (startIndex != -1);

        return entries;
    }


    /**
     * Creates a document out of a single map JSON object.
     */
    @Override
    protected List<IDocument> harvestEntry(ArcGisMap map)
    {
        DataCiteJson doc = new DataCiteJson();

        doc.setLanguage(map.getCulture());
        doc.setPublisher(ArcGisDataCiteConstants.PUBLISHER);
        doc.setRepositoryIdentifier(ArcGisDataCiteConstants.REPOSITORY_ID);
        doc.setResearchDisciplines(ArcGisDataCiteConstants.RESEARCH_DISCIPLINES);
        doc.setTitles(ArcGisMapParser.getTitles(map));
        doc.setDates(ArcGisMapParser.getDates(map));
        doc.setDescriptions(ArcGisMapParser.getDescriptions(map));
        doc.setCreators(ArcGisMapParser.getCreators(map));
        doc.setGeoLocations(ArcGisMapParser.getGeoLocations(map));
        doc.setRightsList(ArcGisMapParser.getRightsList(map));
        doc.setSubjects(ArcGisMapParser.getSubjects(map, groupTags));
        doc.setWebLinks(ArcGisLinkAssembler.getWebLinks(map, baseUrl));
        doc.setResearchDataList(ArcGisLinkAssembler.getFiles(map));

        return Arrays.asList(doc);
    }
}

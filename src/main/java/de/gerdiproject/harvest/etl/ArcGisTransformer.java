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
package de.gerdiproject.harvest.etl;

import java.util.Iterator;
import java.util.List;

import de.gerdiproject.harvest.arcgis.constants.ArcGisDataCiteConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.utils.ArcGisDownloader;
import de.gerdiproject.harvest.arcgis.utils.ArcGisLinkAssembler;
import de.gerdiproject.harvest.arcgis.utils.ArcGisMapParser;
import de.gerdiproject.harvest.etls.transformers.AbstractIteratorTransformer;
import de.gerdiproject.harvest.etls.transformers.TransformerException;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Subject;

/**
 * This {@linkplain AbstractIteratorTransformer} transforms {@linkplain ArcGisMap}s
 * to {@linkplain DataCiteJson} objects.
 *
 * @author Robin Weiss
 */
public class ArcGisTransformer extends AbstractIteratorTransformer<ArcGisMap, DataCiteJson>
{
    private List<Subject> groupRelatedSubjects;
    private final String baseUrl;
    private final String groupId;


    /**
     * Constructor that requires an URL and a query parameter.
     *
     * @param baseUrl the ArcGis base URL
     * @param groupId an identifier indicating which area is harvested
     */
    public ArcGisTransformer(String baseUrl, String groupId)
    {
        super();
        this.baseUrl = baseUrl;
        this.groupId = groupId;
    }


    @Override
    public Iterator<DataCiteJson> transform(Iterator<ArcGisMap> elements) throws TransformerException
    {
        // load subjects that regard all documents of this group
        final List<ArcGisFeaturedGroup> featuredGroups = ArcGisDownloader.getFeaturedGroupsByQuery(baseUrl, groupId);
        this.groupRelatedSubjects = ArcGisMapParser.createGroupTags(featuredGroups);

        return super.transform(elements);
    }


    @Override
    protected DataCiteJson transformElement(ArcGisMap source) throws TransformerException
    {
        DataCiteJson doc = new DataCiteJson(source.getId());

        doc.setLanguage(source.getCulture());
        doc.setPublisher(ArcGisDataCiteConstants.PUBLISHER);
        doc.setRepositoryIdentifier(ArcGisDataCiteConstants.REPOSITORY_ID);
        doc.setResearchDisciplines(ArcGisDataCiteConstants.RESEARCH_DISCIPLINES);
        doc.setTitles(ArcGisMapParser.getTitles(source));
        doc.setDates(ArcGisMapParser.getDates(source));
        doc.setDescriptions(ArcGisMapParser.getDescriptions(source));
        doc.setCreators(ArcGisMapParser.getCreators(source));
        doc.setGeoLocations(ArcGisMapParser.getGeoLocations(source));
        doc.setRightsList(ArcGisMapParser.getRightsList(source));
        doc.setSubjects(ArcGisMapParser.getSubjects(source, groupRelatedSubjects));
        doc.setWebLinks(ArcGisLinkAssembler.getWebLinks(source, baseUrl));
        doc.setResearchDataList(ArcGisLinkAssembler.getFiles(source));

        return doc;
    }
}

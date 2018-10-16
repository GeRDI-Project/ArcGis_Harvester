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

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.json.compound.ArcGisMapsResponse;
import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.etls.extractors.AbstractIteratorExtractor;
import de.gerdiproject.harvest.etls.extractors.ExtractorException;
import de.gerdiproject.harvest.utils.data.HttpRequester;
import de.gerdiproject.json.GsonUtils;

/**
 * This extractor retrieves {@linkplain ArcGisMap}s from a specified URL and GroupID.
 *
 * @author Robin Weiss
 */
public class ArcGisExtractor extends AbstractIteratorExtractor<ArcGisMap>
{
    private final HttpRequester httpRequester;
    private final String baseUrl;
    private final String groupId;

    private int size;
    private String version;


    /**
     * Constructor that requires an URL and a query parameter.
     *
     * @param baseUrl the ArcGis base URL
     * @param groupId an identifier indicating which area is harvested
     */
    public ArcGisExtractor(String baseUrl, String groupId)
    {
        super();

        this.size = -1;
        this.version = null;
        this.baseUrl = baseUrl;
        this.groupId = groupId;

        this.httpRequester = new HttpRequester(
            GsonUtils.createGeoJsonGsonBuilder().create(),
            StandardCharsets.UTF_8
        );
    }


    @Override
    public void init(AbstractETL<?, ?> etl)
    {
        super.init(etl);

        final String mapsUrl = String.format(ArcGisConstants.MAPS_INFO_URL, baseUrl, groupId);
        final ArcGisMapsResponse mapsQueryResult = httpRequester.getObjectFromUrl(mapsUrl, ArcGisMapsResponse.class);
        this.size = mapsQueryResult.getTotal();
        this.version = mapsQueryResult.getQuery() + size;
    }


    @Override
    public String getUniqueVersionString()
    {
        return version;
    }


    @Override
    public int size()
    {
        return Math.min(super.size(), size);
    }


    @Override
    protected Iterator<ArcGisMap> extractAll() throws ExtractorException
    {
        return new ArcGisMapsIterator();
    }


    /**
     * This class is an iterator for retrieving {@linkplain ArcGisMap} from ArcGis.
     * The underlying implementation in the ArcGis API returns the maps in batches
     * of 100 max.
     *
     * @author Robin Weiss
     */
    private class ArcGisMapsIterator implements Iterator<ArcGisMap>
    {
        private final Queue<ArcGisMap> currentBatch = new LinkedList<>();
        private int startIndex = 1;


        @Override
        public boolean hasNext()
        {
            return !currentBatch.isEmpty() || startIndex != -1;
        }


        @Override
        public ArcGisMap next()
        {
            // request the next batch of 100 maps
            if (currentBatch.isEmpty()) {
                final String mapsUrl = String.format(ArcGisConstants.MAPS_URL, baseUrl, groupId, startIndex);
                final ArcGisMapsResponse mapsQueryResult = httpRequester.getObjectFromUrl(mapsUrl, ArcGisMapsResponse.class);

                this.currentBatch.addAll(mapsQueryResult.getResults());
                this.startIndex = mapsQueryResult.getNextStart();
            }

            return currentBatch.remove();
        }
    }
}

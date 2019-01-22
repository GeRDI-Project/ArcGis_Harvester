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
package de.gerdiproject.harvest.etls.extractors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.json.ArcGisUser;
import de.gerdiproject.harvest.arcgis.json.compound.ArcGisFeaturedGroupsResponse;
import de.gerdiproject.harvest.arcgis.json.compound.ArcGisMapsResponse;
import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.utils.data.HttpRequester;

/**
 * This extractor retrieves {@linkplain ArcGisMap}s from a specified URL and GroupID.
 *
 * @author Robin Weiss
 */
public class ArcGisExtractor extends AbstractIteratorExtractor<ArcGisMapVO>
{
    private final HttpRequester httpRequester;
    private final String baseUrl;
    private final String groupId;

    private int size;
    private String version;
    private List<ArcGisFeaturedGroup> featuredGroups;


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

        this.httpRequester = new HttpRequester();
    }


    @Override
    public void init(AbstractETL<?, ?> etl)
    {
        super.init(etl);

        final String mapsUrl = String.format(ArcGisConstants.MAPS_INFO_URL, baseUrl, groupId);
        final ArcGisMapsResponse mapsQueryResult = httpRequester.getObjectFromUrl(mapsUrl, ArcGisMapsResponse.class);
        this.size = mapsQueryResult.getTotal();
        this.version = mapsQueryResult.getQuery() + size;

        // get featured groups related to the maps
        this.featuredGroups = getFeaturedGroupsByQuery(httpRequester, baseUrl, groupId);
    }


    @Override
    public String getUniqueVersionString()
    {
        return version;
    }


    @Override
    public int size()
    {
        return size;
    }


    @Override
    protected Iterator<ArcGisMapVO> extractAll() throws ExtractorException
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
    private class ArcGisMapsIterator implements Iterator<ArcGisMapVO>
    {
        private Iterator<ArcGisMap> currentBatch;
        private int startIndex;

        /**
         * Constructor.
         */
        public ArcGisMapsIterator()
        {
            this.startIndex = 1;
            getNextBatch();
        }


        @Override
        public boolean hasNext()
        {
            return currentBatch.hasNext() || startIndex != -1;
        }


        @Override
        public ArcGisMapVO next()
        {
            // request the next batch of 100 maps
            if (!currentBatch.hasNext())
                getNextBatch();


            final ArcGisMap map = currentBatch.next();
            return new ArcGisMapVO(
                       map,
                       getUser(map),
                       featuredGroups
                   );
        }


        /**
         * Extracts a batch of {@linkplain ArcGisMap}s.
         */
        private void getNextBatch()
        {
            final String mapsUrl = String.format(ArcGisConstants.MAPS_URL, baseUrl, groupId, startIndex);
            final ArcGisMapsResponse mapsQueryResult = httpRequester.getObjectFromUrl(mapsUrl, ArcGisMapsResponse.class);

            this.currentBatch = mapsQueryResult.getResults().iterator();
            this.startIndex = mapsQueryResult.getNextStart();
        }


        /**
         * Extracts details of a map owner.
         *
         * @param map the map of which the owner is to be extracted
         *
         * @return details of the map owner
         */
        private ArcGisUser getUser(ArcGisMap map)
        {
            String url = String.format(ArcGisConstants.USER_PROFILE_URL, map.getOwner());
            return httpRequester.getObjectFromUrl(url, ArcGisUser.class);
        }
    }


    /**
     * Retrieves detailed featured groups from a query request.
     *
     * @param httpRequester the {@linkplain HttpRequester} that sends the request
     * @param baseUrl the host of the ArcGis map URL
     * @param query the groups query
     *
     * @return a list of detailed featured groups
     */
    public static List<ArcGisFeaturedGroup> getFeaturedGroupsByQuery(HttpRequester httpRequester, String baseUrl, String query)
    {
        try {
            String groupDetailsUrl = baseUrl + ArcGisConstants.GROUP_DETAILS_URL_SUFFIX;
            groupDetailsUrl = String.format(groupDetailsUrl, URLEncoder.encode(query, StandardCharsets.UTF_8.displayName()));

            // retrieve details of gallery group
            return httpRequester.getObjectFromUrl(groupDetailsUrl, ArcGisFeaturedGroupsResponse.class).getResults();

        } catch (UnsupportedEncodingException e) {
            // this should never happen, because UTF-8 is a valid encoding
            return null;
        }
    }
}
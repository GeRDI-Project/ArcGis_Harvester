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
import de.gerdiproject.harvest.arcgis.json.generic.GenericArcGisResponse;
import de.gerdiproject.harvest.etls.AbstractETL;
import de.gerdiproject.harvest.utils.data.HttpRequester;

/**
 * This extractor retrieves {@linkplain ArcGisMap}s from a specified URL and GroupID.
 *
 * @author Robin Weiss
 */
public class ArcGisExtractor extends AbstractIteratorExtractor<ArcGisMapVO>
{
    // protected fields accessed by the inner iterator class
    protected final HttpRequester httpRequester;
    protected final String baseUrl;
    protected final String groupId;
    protected List<ArcGisFeaturedGroup> featuredGroups;

    private int mapCount;
    private String version;


    /**
     * Constructor that requires an URL and a query parameter.
     *
     * @param baseUrl the ArcGis base URL
     * @param groupId an identifier indicating which area is harvested
     */
    public ArcGisExtractor(final String baseUrl, final String groupId)
    {
        super();

        this.mapCount = -1;
        this.version = null;
        this.baseUrl = baseUrl;
        this.groupId = groupId;

        this.httpRequester = new HttpRequester();
    }


    @Override
    public void init(final AbstractETL<?, ?> etl)
    {
        super.init(etl);

        final String mapsUrl = String.format(ArcGisConstants.MAPS_INFO_URL, baseUrl, groupId);
        final GenericArcGisResponse<ArcGisMap> mapsQueryResult =
            httpRequester.getObjectFromUrl(mapsUrl, ArcGisConstants.MAPS_RESPONSE_TYPE);
        this.mapCount = mapsQueryResult.getTotal();
        this.version = mapsQueryResult.getQuery() + mapCount;

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
        return mapCount;
    }


    @Override
    protected Iterator<ArcGisMapVO> extractAll() throws ExtractorException
    {
        return new ArcGisMapsIterator();
    }


    @Override
    public void clear()
    {
        // nothing to clean up
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
            downloadNextBatch();
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
                downloadNextBatch();


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
        private void downloadNextBatch()
        {
            final String mapsUrl = String.format(ArcGisConstants.MAPS_URL, baseUrl, groupId, startIndex);
            final GenericArcGisResponse<ArcGisMap> mapsQueryResult =
                httpRequester.getObjectFromUrl(mapsUrl, ArcGisConstants.MAPS_RESPONSE_TYPE);

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
        private ArcGisUser getUser(final ArcGisMap map)
        {
            final String url = String.format(ArcGisConstants.USER_PROFILE_URL, map.getOwner());
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
    public static List<ArcGisFeaturedGroup> getFeaturedGroupsByQuery(final HttpRequester httpRequester, final String baseUrl, final String query)
    {
        try {
            String groupDetailsUrl = baseUrl + ArcGisConstants.GROUP_DETAILS_URL_SUFFIX;
            groupDetailsUrl = String.format(groupDetailsUrl, URLEncoder.encode(query, StandardCharsets.UTF_8.displayName()));

            // retrieve details of gallery group
            final GenericArcGisResponse<ArcGisFeaturedGroup> response =
                httpRequester.getObjectFromUrl(groupDetailsUrl, ArcGisConstants.FEATURED_GROUPS_RESPONSE_TYPE);

            return response.getResults();

        } catch (final UnsupportedEncodingException e) {
            // this should never happen, because UTF-8 is a valid encoding
            return null;
        }
    }
}

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
package de.gerdiproject.harvest.arcgis.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisFeaturedGroup;
import de.gerdiproject.harvest.arcgis.json.ArcGisOverview;
import de.gerdiproject.harvest.arcgis.json.compound.ArcGisFeaturedGroupsResponse;
import de.gerdiproject.harvest.utils.data.HttpRequester;

/**
 * This class provides static methods for downloading raw (meta-) data from ArcGis.
 *
 * @author Robin Weiss
 */
public class ArcGisDownloader
{
    /**
     * Private constructor, because only static methods are provided.
     */
    private ArcGisDownloader()
    {

    }

    /**
     * Retrieves a list of featured groups from an ArcGis map host.
     *
     * @param baseUrl the host of the ArcGis map URL
     *
     * @return a list of featured groups
     */
    public static List<ArcGisFeaturedGroup> getFeaturedGroupsFromOverview(String baseUrl)
    {
        final HttpRequester httpRequester = new HttpRequester(new Gson(), StandardCharsets.UTF_8);

        // get overview object
        final String overviewUrl = baseUrl + ArcGisConstants.OVERVIEW_URL_SUFFIX;
        final ArcGisOverview overviewObj = httpRequester.getObjectFromUrl(overviewUrl, ArcGisOverview.class);

        List<ArcGisFeaturedGroup> featuredGroups;

        // check if the featured groups array has group IDs
        boolean hasFeaturedGroupIDs = overviewObj.getFeaturedGroups().get(0).getId() != null;

        if (hasFeaturedGroupIDs)
            featuredGroups = overviewObj.getFeaturedGroups();
        else {
            // if the featured groups are missing IDs, get them via another request
            String galleryQuery = overviewObj.getLivingAtlasGroupQuery();
            featuredGroups = getFeaturedGroupsByQuery(httpRequester, baseUrl, galleryQuery);
        }

        return featuredGroups;
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

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
package de.gerdiproject.harvest.arcgis.json;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.Value;

/**
 * The response to the most basic ArcGis request. It returns basic information about
 * <br>e.g. http://esri.maps.arcgis.com/sharing/rest/portals/self?culture=en&f=json
 *
 * @author Robin Weiss
 */
@Value
public class ArcGisOverview
{
    private final String access;
    private final boolean allSSL;
    private final JsonArray allowedRedirectUris;
    private final String analysisLayersGroupQuery;
    private final JsonArray authorizedCrossOriginDomains;
    private final String basemapGalleryGroupQuery;
    private final boolean canShareBingPublic;
    private final String colorSetsGroupQuery;
    private final boolean commentsEnabled;
    private final String culture;
    private final String customBaseUrl;
    private final JsonObject defaultBasemap;
    private final JsonObject defaultExtent;
    private final JsonObject defaultVectorBasemap;
    private final String description;
    private final boolean eueiEnabled;
    private final List<ArcGisFeaturedGroup> featuredGroups;
    private final String featuredGroupsId;
    private final String featuredItemsGroupQuery;
    private final String galleryTemplatesGroupQuery;
    private final String helpBase;
    private final JsonObject helpMap;
    private final JsonObject helperServices;
    private final String homePageFeaturedContent;
    private final int homePageFeaturedContentCount;
    private final String id;
    private final boolean isPortal;
    private final String layerTemplatesGroupQuery;
    private final String livingAtlasGroupQuery;
    private final String name;
    private final String orgEmail;
    private final String orgPhone;
    private final String orgUrl;
    private final String portalHostname;
    private final String portalMode;
    private final String portalName;
    private final JsonObject portalProperties;
    private final String portalThumbnail;
    private final String region;
    private final JsonArray rotatorPanels;
    private final boolean showHomePageDescription;
    private final String staticImagesUrl;
    private final String stylesGroupQuery;
    private final boolean supportsHostedServices;
    private final String symbolSetsGroupQuery;
    private final String templatesGroupQuery;
    private final String thumbnail;
    private final String units;
    private final String urlKey;
    private final boolean useVectorBasemaps;
    private final String vectorBasemapGalleryGroupQuery;
    private final String ipCntryCode;
    private final int httpPort;
    private final int httpsPort;
    private final boolean supportsOAuth;
    private final String currentVersion;
    private final JsonArray allowedOrigins;
}

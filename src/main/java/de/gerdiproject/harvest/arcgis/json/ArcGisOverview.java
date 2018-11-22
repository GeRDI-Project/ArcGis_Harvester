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

import lombok.Data;

/**
 * The response to the most basic ArcGis request. It returns basic information about
 * <br>e.g. http://esri.maps.arcgis.com/sharing/rest/portals/self?culture=en&f=json
 *
 * @author Robin Weiss
 */
@Data
public class ArcGisOverview
{
    private String access;
    private boolean allSSL;
    private JsonArray allowedRedirectUris;
    private String analysisLayersGroupQuery;
    private JsonArray authorizedCrossOriginDomains;
    private String basemapGalleryGroupQuery;
    private boolean canShareBingPublic;
    private String colorSetsGroupQuery;
    private boolean commentsEnabled;
    private String culture;
    private String customBaseUrl;
    private JsonObject defaultBasemap;
    private JsonObject defaultExtent;
    private JsonObject defaultVectorBasemap;
    private String description;
    private boolean eueiEnabled;
    private List<ArcGisFeaturedGroup> featuredGroups;
    private String featuredGroupsId;
    private String featuredItemsGroupQuery;
    private String galleryTemplatesGroupQuery;
    private String helpBase;
    private JsonObject helpMap;
    private JsonObject helperServices;
    private String homePageFeaturedContent;
    private int homePageFeaturedContentCount;
    private String id;
    private boolean isPortal;
    private String layerTemplatesGroupQuery;
    private String livingAtlasGroupQuery;
    private String name;
    private String orgEmail;
    private String orgPhone;
    private String orgUrl;
    private String portalHostname;
    private String portalMode;
    private String portalName;
    private JsonObject portalProperties;
    private String portalThumbnail;
    private String region;
    private JsonArray rotatorPanels;
    private boolean showHomePageDescription;
    private String staticImagesUrl;
    private String stylesGroupQuery;
    private boolean supportsHostedServices;
    private String symbolSetsGroupQuery;
    private String templatesGroupQuery;
    private String thumbnail;
    private String units;
    private String urlKey;
    private boolean useVectorBasemaps;
    private String vectorBasemapGalleryGroupQuery;
    private String ipCntryCode;
    private int httpPort;
    private int httpsPort;
    private boolean supportsOAuth;
    private String currentVersion;
    private JsonArray allowedOrigins;
}

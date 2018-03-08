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

/**
 * The response to the most basic ArcGis request. It returns basic information about
 * <br>e.g. http://esri.maps.arcgis.com/sharing/rest/portals/self?culture=en&f=json
 *
 * @author Robin Weiss
 */
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


    public String getAccess()
    {
        return access;
    }


    public void setAccess(String access)
    {
        this.access = access;
    }


    public boolean isAllSSL()
    {
        return allSSL;
    }


    public void setAllSSL(boolean allSSL)
    {
        this.allSSL = allSSL;
    }


    public JsonArray getAllowedRedirectUris()
    {
        return allowedRedirectUris;
    }


    public void setAllowedRedirectUris(JsonArray allowedRedirectUris)
    {
        this.allowedRedirectUris = allowedRedirectUris;
    }


    public String getAnalysisLayersGroupQuery()
    {
        return analysisLayersGroupQuery;
    }


    public void setAnalysisLayersGroupQuery(String analysisLayersGroupQuery)
    {
        this.analysisLayersGroupQuery = analysisLayersGroupQuery;
    }


    public JsonArray getAuthorizedCrossOriginDomains()
    {
        return authorizedCrossOriginDomains;
    }


    public void setAuthorizedCrossOriginDomains(JsonArray authorizedCrossOriginDomains)
    {
        this.authorizedCrossOriginDomains = authorizedCrossOriginDomains;
    }


    public String getBasemapGalleryGroupQuery()
    {
        return basemapGalleryGroupQuery;
    }


    public void setBasemapGalleryGroupQuery(String basemapGalleryGroupQuery)
    {
        this.basemapGalleryGroupQuery = basemapGalleryGroupQuery;
    }


    public boolean isCanShareBingPublic()
    {
        return canShareBingPublic;
    }


    public void setCanShareBingPublic(boolean canShareBingPublic)
    {
        this.canShareBingPublic = canShareBingPublic;
    }


    public String getColorSetsGroupQuery()
    {
        return colorSetsGroupQuery;
    }


    public void setColorSetsGroupQuery(String colorSetsGroupQuery)
    {
        this.colorSetsGroupQuery = colorSetsGroupQuery;
    }


    public boolean isCommentsEnabled()
    {
        return commentsEnabled;
    }


    public void setCommentsEnabled(boolean commentsEnabled)
    {
        this.commentsEnabled = commentsEnabled;
    }


    public String getCulture()
    {
        return culture;
    }


    public void setCulture(String culture)
    {
        this.culture = culture;
    }


    public String getCustomBaseUrl()
    {
        return customBaseUrl;
    }


    public void setCustomBaseUrl(String customBaseUrl)
    {
        this.customBaseUrl = customBaseUrl;
    }


    public JsonObject getDefaultBasemap()
    {
        return defaultBasemap;
    }


    public void setDefaultBasemap(JsonObject defaultBasemap)
    {
        this.defaultBasemap = defaultBasemap;
    }


    public JsonObject getDefaultExtent()
    {
        return defaultExtent;
    }


    public void setDefaultExtent(JsonObject defaultExtent)
    {
        this.defaultExtent = defaultExtent;
    }


    public JsonObject getDefaultVectorBasemap()
    {
        return defaultVectorBasemap;
    }


    public void setDefaultVectorBasemap(JsonObject defaultVectorBasemap)
    {
        this.defaultVectorBasemap = defaultVectorBasemap;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public boolean isEueiEnabled()
    {
        return eueiEnabled;
    }


    public void setEueiEnabled(boolean eueiEnabled)
    {
        this.eueiEnabled = eueiEnabled;
    }


    public List<ArcGisFeaturedGroup> getFeaturedGroups()
    {
        return featuredGroups;
    }


    public void setFeaturedGroups(List<ArcGisFeaturedGroup> featuredGroups)
    {
        this.featuredGroups = featuredGroups;
    }


    public String getFeaturedGroupsId()
    {
        return featuredGroupsId;
    }


    public void setFeaturedGroupsId(String featuredGroupsId)
    {
        this.featuredGroupsId = featuredGroupsId;
    }


    public String getFeaturedItemsGroupQuery()
    {
        return featuredItemsGroupQuery;
    }


    public void setFeaturedItemsGroupQuery(String featuredItemsGroupQuery)
    {
        this.featuredItemsGroupQuery = featuredItemsGroupQuery;
    }


    public String getGalleryTemplatesGroupQuery()
    {
        return galleryTemplatesGroupQuery;
    }


    public void setGalleryTemplatesGroupQuery(String galleryTemplatesGroupQuery)
    {
        this.galleryTemplatesGroupQuery = galleryTemplatesGroupQuery;
    }


    public String getHelpBase()
    {
        return helpBase;
    }


    public void setHelpBase(String helpBase)
    {
        this.helpBase = helpBase;
    }


    public JsonObject getHelpMap()
    {
        return helpMap;
    }


    public void setHelpMap(JsonObject helpMap)
    {
        this.helpMap = helpMap;
    }


    public JsonObject getHelperServices()
    {
        return helperServices;
    }


    public void setHelperServices(JsonObject helperServices)
    {
        this.helperServices = helperServices;
    }


    public String getHomePageFeaturedContent()
    {
        return homePageFeaturedContent;
    }


    public void setHomePageFeaturedContent(String homePageFeaturedContent)
    {
        this.homePageFeaturedContent = homePageFeaturedContent;
    }


    public int getHomePageFeaturedContentCount()
    {
        return homePageFeaturedContentCount;
    }


    public void setHomePageFeaturedContentCount(int homePageFeaturedContentCount)
    {
        this.homePageFeaturedContentCount = homePageFeaturedContentCount;
    }


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public boolean isPortal()
    {
        return isPortal;
    }


    public void setPortal(boolean isPortal)
    {
        this.isPortal = isPortal;
    }


    public String getLayerTemplatesGroupQuery()
    {
        return layerTemplatesGroupQuery;
    }


    public void setLayerTemplatesGroupQuery(String layerTemplatesGroupQuery)
    {
        this.layerTemplatesGroupQuery = layerTemplatesGroupQuery;
    }


    public String getLivingAtlasGroupQuery()
    {
        return livingAtlasGroupQuery;
    }


    public void setLivingAtlasGroupQuery(String livingAtlasGroupQuery)
    {
        this.livingAtlasGroupQuery = livingAtlasGroupQuery;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getOrgEmail()
    {
        return orgEmail;
    }


    public void setOrgEmail(String orgEmail)
    {
        this.orgEmail = orgEmail;
    }


    public String getOrgPhone()
    {
        return orgPhone;
    }


    public void setOrgPhone(String orgPhone)
    {
        this.orgPhone = orgPhone;
    }


    public String getOrgUrl()
    {
        return orgUrl;
    }


    public void setOrgUrl(String orgUrl)
    {
        this.orgUrl = orgUrl;
    }


    public String getPortalHostname()
    {
        return portalHostname;
    }


    public void setPortalHostname(String portalHostname)
    {
        this.portalHostname = portalHostname;
    }


    public String getPortalMode()
    {
        return portalMode;
    }


    public void setPortalMode(String portalMode)
    {
        this.portalMode = portalMode;
    }


    public String getPortalName()
    {
        return portalName;
    }


    public void setPortalName(String portalName)
    {
        this.portalName = portalName;
    }


    public JsonObject getPortalProperties()
    {
        return portalProperties;
    }


    public void setPortalProperties(JsonObject portalProperties)
    {
        this.portalProperties = portalProperties;
    }


    public String getPortalThumbnail()
    {
        return portalThumbnail;
    }


    public void setPortalThumbnail(String portalThumbnail)
    {
        this.portalThumbnail = portalThumbnail;
    }


    public String getRegion()
    {
        return region;
    }


    public void setRegion(String region)
    {
        this.region = region;
    }


    public JsonArray getRotatorPanels()
    {
        return rotatorPanels;
    }


    public void setRotatorPanels(JsonArray rotatorPanels)
    {
        this.rotatorPanels = rotatorPanels;
    }


    public boolean isShowHomePageDescription()
    {
        return showHomePageDescription;
    }


    public void setShowHomePageDescription(boolean showHomePageDescription)
    {
        this.showHomePageDescription = showHomePageDescription;
    }


    public String getStaticImagesUrl()
    {
        return staticImagesUrl;
    }


    public void setStaticImagesUrl(String staticImagesUrl)
    {
        this.staticImagesUrl = staticImagesUrl;
    }


    public String getStylesGroupQuery()
    {
        return stylesGroupQuery;
    }


    public void setStylesGroupQuery(String stylesGroupQuery)
    {
        this.stylesGroupQuery = stylesGroupQuery;
    }


    public boolean isSupportsHostedServices()
    {
        return supportsHostedServices;
    }


    public void setSupportsHostedServices(boolean supportsHostedServices)
    {
        this.supportsHostedServices = supportsHostedServices;
    }


    public String getSymbolSetsGroupQuery()
    {
        return symbolSetsGroupQuery;
    }


    public void setSymbolSetsGroupQuery(String symbolSetsGroupQuery)
    {
        this.symbolSetsGroupQuery = symbolSetsGroupQuery;
    }


    public String getTemplatesGroupQuery()
    {
        return templatesGroupQuery;
    }


    public void setTemplatesGroupQuery(String templatesGroupQuery)
    {
        this.templatesGroupQuery = templatesGroupQuery;
    }


    public String getThumbnail()
    {
        return thumbnail;
    }


    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }


    public String getUnits()
    {
        return units;
    }


    public void setUnits(String units)
    {
        this.units = units;
    }


    public String getUrlKey()
    {
        return urlKey;
    }


    public void setUrlKey(String urlKey)
    {
        this.urlKey = urlKey;
    }


    public boolean isUseVectorBasemaps()
    {
        return useVectorBasemaps;
    }


    public void setUseVectorBasemaps(boolean useVectorBasemaps)
    {
        this.useVectorBasemaps = useVectorBasemaps;
    }


    public String getVectorBasemapGalleryGroupQuery()
    {
        return vectorBasemapGalleryGroupQuery;
    }


    public void setVectorBasemapGalleryGroupQuery(String vectorBasemapGalleryGroupQuery)
    {
        this.vectorBasemapGalleryGroupQuery = vectorBasemapGalleryGroupQuery;
    }


    public String getIpCntryCode()
    {
        return ipCntryCode;
    }


    public void setIpCntryCode(String ipCntryCode)
    {
        this.ipCntryCode = ipCntryCode;
    }


    public int getHttpPort()
    {
        return httpPort;
    }


    public void setHttpPort(int httpPort)
    {
        this.httpPort = httpPort;
    }


    public int getHttpsPort()
    {
        return httpsPort;
    }


    public void setHttpsPort(int httpsPort)
    {
        this.httpsPort = httpsPort;
    }


    public boolean isSupportsOAuth()
    {
        return supportsOAuth;
    }


    public void setSupportsOAuth(boolean supportsOAuth)
    {
        this.supportsOAuth = supportsOAuth;
    }


    public String getCurrentVersion()
    {
        return currentVersion;
    }


    public void setCurrentVersion(String currentVersion)
    {
        this.currentVersion = currentVersion;
    }


    public JsonArray getAllowedOrigins()
    {
        return allowedOrigins;
    }


    public void setAllowedOrigins(JsonArray allowedOrigins)
    {
        this.allowedOrigins = allowedOrigins;
    }
}

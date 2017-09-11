/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
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
package de.gerdiproject.harvest.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A static collection of constants, used for assembling web links and file downloads of ArcGis maps.
 * This class is package internal due to mutable maps that do not need to be public
 *
 * @author Robin Weiss
 */
class LinkAssemblerConstants
{
    public static final String FEATURE_COLLECTION_TYPE = "Feature Collection";
    public static final String VECTOR_TILE_SERVICE_TYPE = "Vector Tile Service";
    public static final String MOBILE_APP_TYPE = "Mobile Application";
    public static final String WEB_APP_TYPE = "Web Mapping Application";
    public static final String DOCUMENT_LINK_TYPE = "Document Link";
    public static final String WEB_SCENE_TYPE = "Web Scene  ";
    public static final String WEB_MAP_TYPE = "Web Map";
    public static final String CODE_ATTACHMENT_TYPE = "Code Attachment";
    public static final String MAP_SERVICE_TYPE = "Map Service";
    public static final String IMAGE_SERVICE_TYPE = "Image Service";
    public static final String FEATURE_SERVICE_TYPE = "Feature Service";
    public static final String RULE_PACKAGE_TYPE = "Rule Package";
    public static final String LAYER_PACKAGE_TYPE = "Layer Package";
    public static final String WMS_TYPE = "WMS";

    public static final String MAP_VIEWER_NAME = "Open in Map Viewer";
    public static final String SCENE_VIEWER_NAME = "Open in Scene Viewer";
    public static final String STYLE_VIEWER_NAME = "View Style";
    public static final String ARC_GIS_DESKTOP_FILE_NAME = "ArcGis Desktop";
    public static final String METADATA_VIEWER_NAME = "View Metadata";
    public static final String APPLICATION_VIEWER_NAME = "View Application";
    public static final String DOCUMENT_VIEWER_NAME = "Open";

    public static final String METADATA_TYPE_KEYWORD = "Metadata";
    public static final String METADATA_VIEWER_URL = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/info/metadata/metadata.xml?format=default&output=html";

    public static final String ARC_GIS_DESKTOP_FILE_TYPE_WEBMAP = "pkinfo";
    public static final String ARC_GIS_DESKTOP_FILE_TYPE_MAPSERVICE = "pitem";


    public static final Map<String, String> MAP_VIEWER_URLS;
    static
    {
        MAP_VIEWER_URLS = new HashMap<>();

        final String mapServiceUrl = "http://esri.maps.arcgis.com/home/signin.html?returnUrl=http%%3A%%2F%%2Fesri.maps.arcgis.com%%2Fhome%%2Fwebmap%%2Fviewer.html%%3FuseExisting%%3D1%%26layers%%3D%s";
        MAP_VIEWER_URLS.put(MAP_SERVICE_TYPE, mapServiceUrl);
        MAP_VIEWER_URLS.put(IMAGE_SERVICE_TYPE, mapServiceUrl);

        final String featureCollectionUrl = "http://esri.maps.arcgis.com/home/webmap/viewer.html?useExisting=1&layers=%s";
        MAP_VIEWER_URLS.put(FEATURE_COLLECTION_TYPE, featureCollectionUrl);
        MAP_VIEWER_URLS.put(VECTOR_TILE_SERVICE_TYPE, featureCollectionUrl);
        MAP_VIEWER_URLS.put(FEATURE_SERVICE_TYPE, featureCollectionUrl);
        MAP_VIEWER_URLS.put(WMS_TYPE, featureCollectionUrl);

        final String webMapUrl = "http://esri.maps.arcgis.com/home/webmap/viewer.html?webmap=%s";
        MAP_VIEWER_URLS.put(WEB_MAP_TYPE, webMapUrl);
    }


    public static final Map<String, String> SCENE_VIEWER_URLS;
    static
    {
        SCENE_VIEWER_URLS = new HashMap<>();

        final String mapServiceUrl = "http://esri.maps.arcgis.com/home/webscene/viewer.html?layers=%s";
        SCENE_VIEWER_URLS.put(MAP_SERVICE_TYPE, mapServiceUrl);
        SCENE_VIEWER_URLS.put(IMAGE_SERVICE_TYPE, mapServiceUrl);
        SCENE_VIEWER_URLS.put(VECTOR_TILE_SERVICE_TYPE, mapServiceUrl);
        SCENE_VIEWER_URLS.put(FEATURE_SERVICE_TYPE, mapServiceUrl);

        final String webSceneUrl = "http://esri.maps.arcgis.com/home/webscene/viewer.html?webscene=%s";
        SCENE_VIEWER_URLS.put(WEB_SCENE_TYPE, webSceneUrl);
    }


    public static final Map<String, String> ARC_GIS_DESKTOP_URLS;
    static
    {
        ARC_GIS_DESKTOP_URLS = new HashMap<>();

        final String mapServiceUrl = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/item.pitem";
        ARC_GIS_DESKTOP_URLS.put(MAP_SERVICE_TYPE, mapServiceUrl);
        ARC_GIS_DESKTOP_URLS.put(IMAGE_SERVICE_TYPE, mapServiceUrl);
        ARC_GIS_DESKTOP_URLS.put(FEATURE_SERVICE_TYPE, mapServiceUrl);
        ARC_GIS_DESKTOP_URLS.put(WMS_TYPE, mapServiceUrl);

        final String webMapUrl = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/item.pkinfo";
        ARC_GIS_DESKTOP_URLS.put(WEB_MAP_TYPE, webMapUrl);
    }


    public static final Map<String, String> DOWNLOAD_URLS;
    static
    {
        DOWNLOAD_URLS = new HashMap<>();

        final String url = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/data";
        DOWNLOAD_URLS.put(LAYER_PACKAGE_TYPE, url);
        DOWNLOAD_URLS.put(CODE_ATTACHMENT_TYPE, url);
        DOWNLOAD_URLS.put(RULE_PACKAGE_TYPE, url);
    }


    public static final Map<String, String> STYLE_VIEW_URLS;
    static
    {
        STYLE_VIEW_URLS = new HashMap<>();

        final String url = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/resources/styles/root.json?f=pjson";
        STYLE_VIEW_URLS.put(VECTOR_TILE_SERVICE_TYPE, url);
    }


    /**
     * Private Constructor, because this is a static class.
     */
    private LinkAssemblerConstants()
    {
    }
}

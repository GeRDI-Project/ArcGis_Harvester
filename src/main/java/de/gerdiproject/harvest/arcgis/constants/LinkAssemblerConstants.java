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
package de.gerdiproject.harvest.arcgis.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A static collection of constants, used for assembling web links and file downloads of ArcGis maps.
 * This class is package internal due to mutable maps that do not need to be public
 *
 * @author Robin Weiss
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkAssemblerConstants
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
    public static final String THUMBNAIL_NAME = "Thumbnail";
    public static final String VIEW_URL_NAME = "View map on arcgis.com";

    public static final String METADATA_TYPE_KEYWORD = "Metadata";
    public static final String ARC_GIS_DESKTOP_FILE_TYPE_WEBMAP = "pkinfo";
    public static final String ARC_GIS_DESKTOP_FILE_TYPE_MAPSERVICE = "pitem";

    public static final String DOWNLOAD_URL = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/data";
    public static final String MAP_VIEWER_URL_MAP_SERVICE = "http://esri.maps.arcgis.com/home/signin.html?returnUrl=http%%3A%%2F%%2Fesri.maps.arcgis.com%%2Fhome%%2Fwebmap%%2Fviewer.html%%3FuseExisting%%3D1%%26layers%%3D%s";
    public static final String MAP_VIEWER_URL_FEATURE_SERVICE = "http://esri.maps.arcgis.com/home/webmap/viewer.html?useExisting=1&layers=%s";
    public static final String MAP_VIEWER_URL_WEB_MAP = "http://esri.maps.arcgis.com/home/webmap/viewer.html?webmap=%s";
    public static final String THUMBNAIL_URL = "%s/sharing/rest/content/items/%s/info/%s";
    public static final String STYLE_VIEWER_URL = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/resources/styles/root.json?f=pjson";
    public static final String METADATA_URL = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/info/metadata/metadata.xml?format=default&output=html";
    public static final String ARC_GIS_DESKTOP_URL_MAP_SERVICE = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/item.pitem";
    public static final String ARC_GIS_DESKTOP_URL_WEB_MAP = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/item.pkinfo";
    public static final String SCENE_VIEWER_URL_MAP_SERVICE = "http://esri.maps.arcgis.com/home/webscene/viewer.html?layers=%s";
    public static final String SCENE_VIEWER_URL_WEB_SCENE = "http://esri.maps.arcgis.com/home/webscene/viewer.html?webscene=%s";
}

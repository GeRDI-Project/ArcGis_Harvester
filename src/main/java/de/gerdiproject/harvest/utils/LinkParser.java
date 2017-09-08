package de.gerdiproject.harvest.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.gerdiproject.json.arcgis.Map;
import de.gerdiproject.json.datacite.File;
import de.gerdiproject.json.datacite.WebLink;
import de.gerdiproject.json.datacite.WebLink.WebLinkType;

public class LinkParser
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

    private static final java.util.Map<String, String> MAP_VIEWER_URLS;
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

    private static final java.util.Map<String, String> SCENE_VIEWER_URLS;
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

    private static final java.util.Map<String, String> ARC_GIS_DESKTOP_URLS;
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

    private static final java.util.Map<String, String> DOWNLOAD_URLS;
    static
    {
        DOWNLOAD_URLS = new HashMap<>();

        final String url = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/data";
        DOWNLOAD_URLS.put(LAYER_PACKAGE_TYPE, url);
        DOWNLOAD_URLS.put(CODE_ATTACHMENT_TYPE, url);
        DOWNLOAD_URLS.put(RULE_PACKAGE_TYPE, url);
    }

    private static final java.util.Map<String, String> STYLE_VIEW_URLS;
    static
    {
        STYLE_VIEW_URLS = new HashMap<>();

        final String url = "http://esri.maps.arcgis.com/sharing/rest/content/items/%s/resources/styles/root.json?f=pjson";
        STYLE_VIEW_URLS.put(VECTOR_TILE_SERVICE_TYPE, url);
    }


    public static List<WebLink> getWebLinks(Map map, String baseUrl)
    {
        final String mapId = map.getId();
        final String mapType = map.getType();
        final String mapUrl = map.getUrl();
        final String thumbnail = map.getThumbnail();
        final List<String> keywords = map.getTypeKeywords();

        // add links
        List<WebLink> webLinks = new LinkedList<>();
        webLinks.add(ArcGisConstants.ESRI_LOGO_LINK);
        webLinks.add(getViewUrl(mapId, baseUrl));
        webLinks.add(getThumbnailLink(mapId, thumbnail, baseUrl));
        webLinks.add(getLink(mapType, mapId, SCENE_VIEWER_NAME, SCENE_VIEWER_URLS));
        webLinks.add(getLink(mapType, mapId, MAP_VIEWER_NAME, MAP_VIEWER_URLS));
        webLinks.add(getLink(mapType, mapId, STYLE_VIEWER_NAME, STYLE_VIEW_URLS));
        webLinks.add(getMetaDataLink(mapId, keywords));
        webLinks.add(getApplicationViewLink(mapType, mapUrl));
        webLinks.add(getDocumentLink(mapType, mapUrl));

        // remove null links
        webLinks.removeIf((WebLink link) -> link == null);

        return webLinks;
    }

    public static List<File> getFiles(Map map)
    {
        List<File> files = new LinkedList<>();

        String id = map.getId();
        String type = map.getType();

        File arcGisDesktop = getArcGisDesktop(type, id);

        if (arcGisDesktop != null)
            files.add(arcGisDesktop);

        File download = getDownload(type, id, map.getName());

        if (download != null)
            files.add(download);

        return files.isEmpty() ? null : files;
    }

    private static WebLink getViewUrl(String mapId, String baseUrl)
    {
        String viewUrl = String.format(ArcGisConstants.VIEW_URL, baseUrl, mapId);
        WebLink viewLink = new WebLink(viewUrl);
        viewLink.setType(WebLinkType.ViewURL);
        return viewLink;
    }

    private static WebLink getThumbnailLink(String mapId, String thumbnailPath, String baseUrl)
    {
        WebLink thumbnailLink = null;

        if (thumbnailPath != null) {
            String thumbnailUrl = String.format(ArcGisConstants.THUMBNAIL_URL, baseUrl, mapId, thumbnailPath);
            thumbnailLink = new WebLink(thumbnailUrl);
            thumbnailLink.setType(WebLinkType.ThumbnailURL);
        }

        return thumbnailLink;
    }


    private static WebLink getApplicationViewLink(String mapType, String mapUrl)
    {
        WebLink link = null;

        if (mapUrl != null && (mapType.equals(MOBILE_APP_TYPE) || mapType.equals(WEB_APP_TYPE))) {
            link = new WebLink(mapUrl);
            link.setName(APPLICATION_VIEWER_NAME);
        }

        return link;
    }


    private static WebLink getDocumentLink(String mapType, String mapUrl)
    {
        WebLink link = null;

        // add metadata url
        if (mapUrl != null && mapType.equals(DOCUMENT_LINK_TYPE)) {
            link = new WebLink(mapUrl);
            link.setName(DOCUMENT_VIEWER_NAME);
        }

        return link;
    }


    private static WebLink getMetaDataLink(String mapId, List<String> keywords)
    {
        WebLink link = null;

        // add metadata url
        if (keywords.contains(METADATA_TYPE_KEYWORD)) {
            link = new WebLink(String.format(METADATA_VIEWER_URL, mapId));
            link.setName(METADATA_VIEWER_NAME);
        }

        return link;
    }


    private static WebLink getLink(String mapType, String mapId, String linkName, java.util.Map<String, String> urlMap)
    {
        WebLink link = null;
        String url = urlMap.get(mapType);

        if (url != null) {
            link = new WebLink(String.format(url, mapId));
            link.setName(linkName);
        }

        return link;
    }


    private static File getArcGisDesktop(String mapType, String mapId)
    {
        File file = null;
        String url = ARC_GIS_DESKTOP_URLS.get(mapType);

        if (url != null) {
            file = new File(String.format(url, mapId), ARC_GIS_DESKTOP_FILE_NAME);
            file.setType(ARC_GIS_DESKTOP_FILE_TYPE_MAPSERVICE);
        }

        return file;
    }


    private static File getDownload(String mapType, String mapId, String mapName)
    {
        File file = null;
        String url = DOWNLOAD_URLS.get(mapType);

        if (url != null) {
            String fileType = mapName.substring(mapName.lastIndexOf('.') + 1);

            file = new File(String.format(url, mapId), mapType);
            file.setType(fileType);
        }

        return file;
    }

}

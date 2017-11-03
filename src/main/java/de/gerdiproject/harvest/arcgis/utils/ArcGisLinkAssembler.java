/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this ResearchData except in compliance
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
package de.gerdiproject.harvest.arcgis.utils;

import java.util.LinkedList;
import java.util.List;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.constants.LinkAssemblerConstants;
import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.json.datacite.extension.ResearchData;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;

/**
 * A static class for parsing and assembling weblinks and ResearchData download links for ArcGis documents.
 *
 * @author Robin Weiss
 */
public class ArcGisLinkAssembler
{
    /**
     * Private Constructor, because this class is static.
     */
    private ArcGisLinkAssembler()
    {
    }


    /**
     * Generates a list of {@linkplain WebLink}s that are related to a specified map.
     *
     * @param map the map for which the links are being generated
     * @param baseUrl the host of the map gallery
     *
     * @return a list of weblinks that are related to the map
     */
    public static List<WebLink> getWebLinks(ArcGisMap map, String baseUrl)
    {
        final String mapId = map.getId();
        final String mapType = map.getType();
        final String mapUrl = map.getUrl();
        final String thumbnailPath = map.getThumbnail();
        final String largeThumbnailPath = map.getLargeThumbnail();
        final List<String> keywords = map.getTypeKeywords();

        // add links
        List<WebLink> webLinks = new LinkedList<>();

        webLinks.add(ArcGisConstants.ESRI_LOGO_LINK);
        webLinks.add(getViewLink(mapId, baseUrl));
        webLinks.add(getThumbnailLink(mapId, thumbnailPath, largeThumbnailPath, baseUrl));
        webLinks.add(getSceneViewerLink(mapType, mapId));
        webLinks.add(getMapViewerLink(mapType, mapId));
        webLinks.add(getStyleViewerLink(mapType, mapId));
        webLinks.add(getMetadataLink(mapId, keywords));
        webLinks.add(getApplicationViewLink(mapType, mapUrl));
        webLinks.add(getOpenDocumentLink(mapType, mapUrl));

        // remove null links
        webLinks.removeIf((WebLink link) -> link == null);

        return webLinks;
    }


    /**
     * Generates a list of {@linkplain File}s that are related to a map.
     *
     * @param map the map for which the files are being generated
     *
     * @return a list of files that are related to a map
     */
    public static List<ResearchData> getFiles(ArcGisMap map)
    {
        List<ResearchData> files = new LinkedList<>();

        String mapId = map.getId();
        String mapType = map.getType();

        ResearchData arcGisDesktop = getArcGisDesktopLink(mapType, mapId);

        if (arcGisDesktop != null)
            files.add(arcGisDesktop);

        ResearchData download = getDownloadLink(mapType, mapId, map.getName());

        if (download != null)
            files.add(download);

        return files.isEmpty() ? null : files;
    }


    /**
     * Creates a generic {@linkplain WebLink} that points to a map related page.
     *
     * @param url the linking URL
     * @param linkName the human readable name of the map
     * @param type the type of the web link
     *
     * @return a {@linkplain WebLink} that points to a map related page
     */
    private static WebLink createLink(String url, String linkName, WebLinkType type)
    {
        WebLink link = null;

        if (url != null) {
            link = new WebLink(url);
            link.setName(linkName);
            link.setType(type);
        }

        return link;
    }


    /**
     * Creates a generic {@linkplain File} that represents a map related download.
     *
     * @param url the download URL
     * @param linkName the human readable name of the download
     * @param type the type of the file
     *
     * @return a {@linkplain WebLink} that points to a map related page
     */
    private static ResearchData createFile(String url, String linkName, String type)
    {
        ResearchData file = null;

        if (url != null) {
            file = new ResearchData(url, linkName);
            file.setType(type);
        }

        return file;
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to access the ArcGis Map Viewer
     * and returns this link.
     *
     * @param mapType the type of the ArcGis map
     * @param mapId the unique identifier of the map
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getMapViewerLink(String mapType, String mapId)
    {
        String url;

        switch (mapType) {
            case LinkAssemblerConstants.MAP_SERVICE_TYPE:
            case LinkAssemblerConstants.IMAGE_SERVICE_TYPE:
                url = String.format(LinkAssemblerConstants.MAP_VIEWER_URL_MAP_SERVICE, mapId);
                break;

            case LinkAssemblerConstants.FEATURE_COLLECTION_TYPE:
            case LinkAssemblerConstants.VECTOR_TILE_SERVICE_TYPE:
            case LinkAssemblerConstants.FEATURE_SERVICE_TYPE:
            case LinkAssemblerConstants.WMS_TYPE:
                url = String.format(LinkAssemblerConstants.MAP_VIEWER_URL_FEATURE_SERVICE, mapId);
                break;

            case LinkAssemblerConstants.WEB_MAP_TYPE:
                url = String.format(LinkAssemblerConstants.MAP_VIEWER_URL_WEB_MAP, mapId);
                break;

            default:
                return null;
        }

        return createLink(url, LinkAssemblerConstants.VIEW_URL_NAME, null);
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to access the ArcGis Scene Viewer
     * and returns this link.
     *
     * @param mapType the type of the ArcGis map
     * @param mapId the unique identifier of the map
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getSceneViewerLink(String mapType, String mapId)
    {
        String url;

        switch (mapType) {
            case LinkAssemblerConstants.MAP_SERVICE_TYPE:
            case LinkAssemblerConstants.IMAGE_SERVICE_TYPE:
            case LinkAssemblerConstants.VECTOR_TILE_SERVICE_TYPE:
            case LinkAssemblerConstants.FEATURE_SERVICE_TYPE:
                url = String.format(LinkAssemblerConstants.SCENE_VIEWER_URL_MAP_SERVICE, mapId);
                break;

            case LinkAssemblerConstants.WEB_SCENE_TYPE:
                url = String.format(LinkAssemblerConstants.SCENE_VIEWER_URL_WEB_SCENE, mapId);
                break;

            default:
                return null;
        }

        return createLink(url, LinkAssemblerConstants.SCENE_VIEWER_NAME, null);
    }


    /**
     * Checks if a specified map type provides a {@linkplain File} to download the map for
     * the ArcGis Desktop application and returns this file.
     *
     * @param mapType the type of the ArcGis map
     * @param mapId the unique identifier of the map
     *
     * @return a {@linkplain File} or null if no URL is defined for the map type
     */
    private static ResearchData getArcGisDesktopLink(String mapType, String mapId)
    {
        switch (mapType) {
            case LinkAssemblerConstants.MAP_SERVICE_TYPE:
            case LinkAssemblerConstants.IMAGE_SERVICE_TYPE:
            case LinkAssemblerConstants.WMS_TYPE:
            case LinkAssemblerConstants.FEATURE_SERVICE_TYPE:
                return createFile(
                           String.format(LinkAssemblerConstants.ARC_GIS_DESKTOP_URL_MAP_SERVICE, mapId),
                           LinkAssemblerConstants.ARC_GIS_DESKTOP_FILE_NAME,
                           LinkAssemblerConstants.ARC_GIS_DESKTOP_FILE_TYPE_MAPSERVICE);

            case LinkAssemblerConstants.WEB_MAP_TYPE:
                return createFile(
                           String.format(LinkAssemblerConstants.ARC_GIS_DESKTOP_URL_WEB_MAP, mapId),
                           LinkAssemblerConstants.ARC_GIS_DESKTOP_FILE_NAME,
                           LinkAssemblerConstants.ARC_GIS_DESKTOP_FILE_TYPE_WEBMAP);

            default:
                return null;
        }
    }


    /**
     * Checks if a specified map type provides a {@linkplain File} to download the map
     * and returns this file.
     *
     * @param mapType the type of the ArcGis map
     * @param mapId the unique identifier of the map
     * @param mapName the ResearchData name of the map
     *
     * @return a {@linkplain File} or null if no URL is defined for the map type
     */
    private static ResearchData getDownloadLink(String mapType, String mapId, String mapName)
    {
        if (mapType.equals(LinkAssemblerConstants.LAYER_PACKAGE_TYPE)
            || mapType.equals(LinkAssemblerConstants.CODE_ATTACHMENT_TYPE)
            || mapType.equals(LinkAssemblerConstants.RULE_PACKAGE_TYPE)) {
            return createFile(
                       String.format(LinkAssemblerConstants.DOWNLOAD_URL, mapId),
                       mapName.substring(mapName.lastIndexOf('.') + 1),
                       mapType);
        } else
            return null;
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to view the map style
     * and returns this link.
     *
     * @param mapType the type of the ArcGis map
     * @param mapId the unique identifier of the map
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getStyleViewerLink(String mapType, String mapId)
    {
        if (mapType.equals(LinkAssemblerConstants.VECTOR_TILE_SERVICE_TYPE))
            return createLink(
                       String.format(LinkAssemblerConstants.STYLE_VIEWER_URL, mapId),
                       LinkAssemblerConstants.STYLE_VIEWER_NAME,
                       null);
        else
            return null;
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to a MetaData overview page
     * and returns this link.
     *
     * @param mapId the unique identifier of the map
     * @param mapTypeKeywords keywords describing the content of the map
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getMetadataLink(String mapId, List<String> mapTypeKeywords)
    {
        if (mapTypeKeywords.contains(LinkAssemblerConstants.METADATA_TYPE_KEYWORD))
            return createLink(
                       String.format(LinkAssemblerConstants.METADATA_URL, mapId),
                       LinkAssemblerConstants.METADATA_VIEWER_NAME,
                       null);
        else
            return null;
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to open a document
     * and returns this link.
     *
     * @param mapType the type of the ArcGis map
     * @param mapUrl the URL property of the map
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getOpenDocumentLink(String mapType, String mapUrl)
    {
        if (mapUrl != null && mapType.equals(LinkAssemblerConstants.DOCUMENT_LINK_TYPE))
            return createLink(mapUrl, LinkAssemblerConstants.DOCUMENT_VIEWER_NAME, null);
        else
            return null;
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to view the application associated with the map
     * and returns this link.
     *
     * @param mapType the type of the ArcGis map
     * @param mapUrl the URL property of the map
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getApplicationViewLink(String mapType, String mapUrl)
    {
        if (mapUrl != null && (mapType.equals(LinkAssemblerConstants.MOBILE_APP_TYPE) || mapType.equals(LinkAssemblerConstants.WEB_APP_TYPE)))
            return createLink(mapUrl, LinkAssemblerConstants.APPLICATION_VIEWER_NAME, null);
        else
            return null;
    }


    /**
     * Checks if a specified map type provides a {@linkplain WebLink} to
     * and returns this link.
     *
     * @param mapId the unique identifier of the map
     * @param thumbnailPath a part of the URL that points to a small thumbnail image
     * @param largeThumbnailPath a part of the URL that points to a large thumbnail image
     * @param baseUrl the host of the map URL
     *
     * @return a {@linkplain WebLink} or null if no URL is defined for the map type
     */
    private static WebLink getThumbnailLink(String mapId, String thumbnailPath, String largeThumbnailPath, String baseUrl)
    {
        String url = null;

        if (largeThumbnailPath != null)
            url = String.format(LinkAssemblerConstants.THUMBNAIL_URL, baseUrl, mapId, largeThumbnailPath);

        else if (thumbnailPath != null)
            url = String.format(LinkAssemblerConstants.THUMBNAIL_URL, baseUrl, mapId, thumbnailPath);


        if (url != null)
            return createLink(url, LinkAssemblerConstants.THUMBNAIL_NAME, WebLinkType.ThumbnailURL);
        else
            return null;
    }


    /**
     * Creates a {@linkplain WebLink} that points to a map details page.
     *
     * @param mapId the unique identifier of the map
     * @param baseUrl the host of the map URL
     *
     * @return a {@linkplain WebLink} that points to a map details page
     */
    private static WebLink getViewLink(String mapId, String baseUrl)
    {
        String url = String.format(ArcGisConstants.VIEW_URL, baseUrl, mapId);
        return createLink(url, LinkAssemblerConstants.VIEW_URL_NAME, WebLinkType.ViewURL);
    }
}

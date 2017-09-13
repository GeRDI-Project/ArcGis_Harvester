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

import java.util.LinkedList;
import java.util.List;

import de.gerdiproject.harvest.arcgis.constants.ArcGisConstants;
import de.gerdiproject.harvest.arcgis.constants.LinkAssemblerConstants;
import de.gerdiproject.harvest.arcgis.json.Map;
import de.gerdiproject.json.datacite.File;
import de.gerdiproject.json.datacite.WebLink;
import de.gerdiproject.json.datacite.WebLink.WebLinkType;

/**
 * A static class for parsing and assembling weblinks and file download links for ArcGis documents.
 *
 * @author Robin Weiss
 */
public class LinkAssembler
{
    /**
     * Private Constructor, because this class is static.
     */
    private LinkAssembler()
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
        webLinks.add(getLink(mapType, mapId, LinkAssemblerConstants.SCENE_VIEWER_NAME, LinkAssemblerConstants.SCENE_VIEWER_URLS));
        webLinks.add(getLink(mapType, mapId, LinkAssemblerConstants.MAP_VIEWER_NAME, LinkAssemblerConstants.MAP_VIEWER_URLS));
        webLinks.add(getLink(mapType, mapId, LinkAssemblerConstants.STYLE_VIEWER_NAME, LinkAssemblerConstants.STYLE_VIEW_URLS));
        webLinks.add(getMetaDataLink(mapId, keywords));
        webLinks.add(getApplicationViewLink(mapType, mapUrl));
        webLinks.add(getDocumentLink(mapType, mapUrl));

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



    /**
     * Creates a {@linkplain WebLink} that points to a map details page.
     *
     * @param mapId the unique identifier of the map
     * @param baseUrl the host of the map URL
     *
     * @return a {@linkplain WebLink} that points to a map details page
     */
    private static WebLink getViewUrl(String mapId, String baseUrl)
    {
        String viewUrl = String.format(ArcGisConstants.VIEW_URL, baseUrl, mapId);
        WebLink viewLink = new WebLink(viewUrl);
        viewLink.setType(WebLinkType.ViewURL);
        return viewLink;
    }


    /**
     * Creates a {@linkplain WebLink} that points to a map thumbnail image.
     *
     * @param mapId the unique identifier of the map
     * @param thumbnailPath a part of the URL that points to the thumbnail image
     * @param baseUrl the host of the map URL
     *
     * @return a {@linkplain WebLink} that points to a map thumbnail image
     */
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


    /**
     * Creates a {@linkplain WebLink} that points to a map application page.
     *
     * @param mapType the type of the map application
     * @param mapUrl the URL of the map application
     *
     * @return a {@linkplain WebLink} that points to a map application page
     */
    private static WebLink getApplicationViewLink(String mapType, String mapUrl)
    {
        WebLink link = null;

        if (mapUrl != null
            && (mapType.equals(LinkAssemblerConstants.MOBILE_APP_TYPE)
                || mapType.equals(LinkAssemblerConstants.WEB_APP_TYPE))) {
            link = new WebLink(mapUrl);
            link.setName(LinkAssemblerConstants.APPLICATION_VIEWER_NAME);
        }

        return link;
    }


    /**
     * Creates a {@linkplain WebLink} that points to a map document.
     *
     * @param mapType the type of the map
     * @param mapUrl the URL of the map document
     *
     * @return a {@linkplain WebLink} that points to a map document
     */
    private static WebLink getDocumentLink(String mapType, String mapUrl)
    {
        WebLink link = null;

        // add metadata url
        if (mapUrl != null && mapType.equals(LinkAssemblerConstants.DOCUMENT_LINK_TYPE)) {
            link = new WebLink(mapUrl);
            link.setName(LinkAssemblerConstants.DOCUMENT_VIEWER_NAME);
        }

        return link;
    }


    /**
     * Creates a {@linkplain WebLink} that points to a map metadata page.
     *
     * @param mapId the unique identifier of the map
     * @param keywords a list of type keywords that describe what the map entails
     *
     * @return a {@linkplain WebLink} that points to a map metadata page
     */
    private static WebLink getMetaDataLink(String mapId, List<String> keywords)
    {
        WebLink link = null;

        // add metadata url
        if (keywords.contains(LinkAssemblerConstants.METADATA_TYPE_KEYWORD)) {
            link = new WebLink(String.format(LinkAssemblerConstants.METADATA_VIEWER_URL, mapId));
            link.setName(LinkAssemblerConstants.METADATA_VIEWER_NAME);
        }

        return link;
    }


    /**
     * Creates a generic {@linkplain WebLink} that points to a map related page.
     *
     * @param mapType the type of the map
     * @param mapId the unique identifier of the map
     * @param linkName the human readable name of the map
     * @param urlMap a map that maps map types to corresponding URLs
     *
     * @return a {@linkplain WebLink} that points to a map related page
     */
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


    /**
     * Creates a {@linkplain File} for downloading the map for the ArcGis desktop application.
     *
     * @param mapType the type of the map
     * @param mapId the unique identifier of the map
     *
     * @return a {@linkplain File} for downloading the map for the ArcGis desktop application
     */
    private static File getArcGisDesktop(String mapType, String mapId)
    {
        File file = null;
        String url = LinkAssemblerConstants.ARC_GIS_DESKTOP_URLS.get(mapType);

        if (url != null) {
            file = new File(String.format(url, mapId), LinkAssemblerConstants.ARC_GIS_DESKTOP_FILE_NAME);
            file.setType(LinkAssemblerConstants.ARC_GIS_DESKTOP_FILE_TYPE_MAPSERVICE);
        }

        return file;
    }


    /**
     * Creates a {@linkplain File} for downloading the map.
     *
     * @param mapType the type of the map
     * @param mapId the unique identifier of the map
     * @param mapName the file name of the map
     *
     * @return a {@linkplain File} for downloading the map
     */
    private static File getDownload(String mapType, String mapId, String mapName)
    {
        File file = null;
        String url = LinkAssemblerConstants.DOWNLOAD_URLS.get(mapType);

        if (url != null) {
            String fileType = mapName.substring(mapName.lastIndexOf('.') + 1);

            file = new File(String.format(url, mapId), mapType);
            file.setType(fileType);
        }

        return file;
    }

}

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
package de.gerdiproject.harvest.etls.transformers.utils;

import java.io.File;
import java.util.List;

import de.gerdiproject.harvest.arcgis.constants.ArcGisDataCiteConstants;
import de.gerdiproject.harvest.arcgis.constants.LinkAssemblerConstants;
import de.gerdiproject.json.datacite.extension.generic.ResearchData;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.extension.generic.enums.WebLinkType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A static class for parsing and assembling ArcGis web links for ArcGis documents.
 *
 * @author Robin Weiss
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArcGisLinkHelper
{
    /**
     * Creates a generic {@linkplain WebLink} that points to a map related page.
     *
     * @param url the linking URL
     * @param linkName the human readable name of the map
     * @param type the type of the web link
     *
     * @return a {@linkplain WebLink} that points to a map related page
     */
    public static WebLink createLink(final String url, final String linkName, final WebLinkType type)
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
    public static ResearchData createFile(final String url, final String linkName, final String type)
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
    public static WebLink getMapViewerLink(final String mapType, final String mapId)
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
    public static WebLink getSceneViewerLink(final String mapType, final String mapId)
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
    public static ResearchData getArcGisDesktopLink(final String mapType, final String mapId)
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
    public static ResearchData getDownloadLink(final String mapType, final String mapId, final String mapName)
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
    public static WebLink getStyleViewerLink(final String mapType, final String mapId)
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
    public static WebLink getMetadataLink(final String mapId, final List<String> mapTypeKeywords)
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
    public static WebLink getOpenDocumentLink(final String mapType, final String mapUrl)
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
    public static WebLink getApplicationViewLink(final String mapType, final String mapUrl)
    {
        if (mapUrl == null || !mapType.equals(LinkAssemblerConstants.MOBILE_APP_TYPE) && !mapType.equals(LinkAssemblerConstants.WEB_APP_TYPE))
            return null;
        else
            return createLink(mapUrl, LinkAssemblerConstants.APPLICATION_VIEWER_NAME, null);
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
    public static WebLink getThumbnailLink(final String mapId, final String thumbnailPath, final String largeThumbnailPath, final String baseUrl)
    {
        if (largeThumbnailPath != null) { // NOPMD less confusing
            final String url = String.format(LinkAssemblerConstants.THUMBNAIL_URL, baseUrl, mapId, largeThumbnailPath);
            return createLink(url, LinkAssemblerConstants.THUMBNAIL_NAME, WebLinkType.ThumbnailURL);

        } else if (thumbnailPath != null) { // NOPMD less confusing
            final String url = String.format(LinkAssemblerConstants.THUMBNAIL_URL, baseUrl, mapId, thumbnailPath);
            return createLink(url, LinkAssemblerConstants.THUMBNAIL_NAME, WebLinkType.ThumbnailURL);
            
        } else
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
    public static WebLink getViewLink(final String mapId, final String baseUrl)
    {
        final String url = String.format(ArcGisDataCiteConstants.VIEW_URL, baseUrl, mapId);
        return createLink(url, LinkAssemblerConstants.VIEW_URL_NAME, WebLinkType.ViewURL);
    }
}

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

import java.util.regex.Pattern;


/**
 * A static collection of constants, used for harvesting ArcGis.
 *
 * @author Robin Weiss
 */
public class ArcGisConstants
{
    public static final String MAPS_URL = "%s/sharing/rest/search?q=%%20group%%3A%s%%20&sortField=title&sortOrder=asc&start=%d&num=100&f=json";
    public static final String MAPS_INFO_URL = "%s/sharing/rest/search?q=%%20group%%3A%s%%20&num=0&f=json";

    public static final String ARC_GIS_BASE_URL = "http://arcgis.com";
    public static final String ESRI_BASE_URL = "http://esri.maps.arcgis.com";
    public static final String OVERVIEW_URL_SUFFIX = "/sharing/rest/portals/self?culture=en&f=json";
    public static final String GROUP_DETAILS_URL_SUFFIX = "/sharing/rest/community/groups?q=%s&f=json";

    public static final String ARC_GIS_SUFFIX = "_ArcGisETL";
    public static final String ESRI_SUFFIX = "_EsriETL";

    public static final Pattern YEAR_PATTERN = Pattern.compile("\\d\\d\\d\\d");

    public static final String USER_PROFILE_URL = "http://www.arcgis.com/sharing/rest/community/users/%s?f=json";

    /**
     * Private Constructor, because this is a static class.
     */
    private ArcGisConstants()
    {
    }
}

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

import de.gerdiproject.json.geo.Point;
import lombok.Value;

/**
 * Information about an ArcGis map.
 *
 * @author Robin Weiss
 */
@Value
public class ArcGisMap
{
    private final String id;
    private final String owner;
    private final long created;
    private final long modified;
    private final String name;
    private final String title;
    private final String type;
    private final List<String> typeKeywords;
    private final String description;
    private final List<String> tags;
    private final String snippet;
    private final String thumbnail;
    private final List<Point> extent;
    private final JsonArray categories;
    private final String spatialReference;
    private final String accessInformation;
    private final String licenseInfo;
    private final String culture;
    private final String url;
    private final String access;
    private final int size;
    private final JsonArray appCategories;
    private final JsonArray industries;
    private final JsonArray languages;
    private final String largeThumbnail;
    private final JsonArray screenshots;
    private final boolean listed;
    private final int numComments;
    private final int numRatings;
    private final float avgRating;
    private final int numViews;
}

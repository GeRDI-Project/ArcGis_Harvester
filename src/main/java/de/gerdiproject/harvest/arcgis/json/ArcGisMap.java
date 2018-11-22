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
import lombok.Data;

/**
 * Information about an ArcGis map.
 *
 * @author Robin Weiss
 */
@Data
public class ArcGisMap
{
    private String id;
    private String owner;
    private long created;
    private long modified;
    private String name;
    private String title;
    private String type;
    private List<String> typeKeywords;
    private String description;
    private List<String> tags;
    private String snippet;
    private String thumbnail;
    private List<Point> extent;
    private JsonArray categories;
    private String spatialReference;
    private String accessInformation;
    private String licenseInfo;
    private String culture;
    private String url;
    private String access;
    private int size;
    private JsonArray appCategories;
    private JsonArray industries;
    private JsonArray languages;
    private String largeThumbnail;
    private JsonArray screenshots;
    private boolean listed;
    private int numComments;
    private int numRatings;
    private float avgRating;
    private int numViews;
}

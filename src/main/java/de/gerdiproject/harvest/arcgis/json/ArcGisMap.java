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

/**
 * Information about an ArcGis map.
 *
 * @author Robin Weiss
 */
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


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getOwner()
    {
        return owner;
    }


    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    public long getCreated()
    {
        return created;
    }


    public void setCreated(long created)
    {
        this.created = created;
    }


    public long getModified()
    {
        return modified;
    }


    public void setModified(long modified)
    {
        this.modified = modified;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public List<String> getTypeKeywords()
    {
        return typeKeywords;
    }


    public void setTypeKeywords(List<String> typeKeywords)
    {
        this.typeKeywords = typeKeywords;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public List<String> getTags()
    {
        return tags;
    }


    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    public String getSnippet()
    {
        return snippet;
    }


    public void setSnippet(String snippet)
    {
        this.snippet = snippet;
    }


    public String getThumbnail()
    {
        return thumbnail;
    }


    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }


    public List<Point> getExtent()
    {
        return extent;
    }


    public void setExtent(List<Point> extent)
    {
        this.extent = extent;
    }


    public JsonArray getCategories()
    {
        return categories;
    }


    public void setCategories(JsonArray categories)
    {
        this.categories = categories;
    }


    public String getSpatialReference()
    {
        return spatialReference;
    }


    public void setSpatialReference(String spatialReference)
    {
        this.spatialReference = spatialReference;
    }


    public String getAccessInformation()
    {
        return accessInformation;
    }


    public void setAccessInformation(String accessInformation)
    {
        this.accessInformation = accessInformation;
    }


    public String getLicenseInfo()
    {
        return licenseInfo;
    }


    public void setLicenseInfo(String licenseInfo)
    {
        this.licenseInfo = licenseInfo;
    }


    public String getCulture()
    {
        return culture;
    }


    public void setCulture(String culture)
    {
        this.culture = culture;
    }


    public String getUrl()
    {
        return url;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getAccess()
    {
        return access;
    }


    public void setAccess(String access)
    {
        this.access = access;
    }


    public int getSize()
    {
        return size;
    }


    public void setSize(int size)
    {
        this.size = size;
    }


    public JsonArray getAppCategories()
    {
        return appCategories;
    }


    public void setAppCategories(JsonArray appCategories)
    {
        this.appCategories = appCategories;
    }


    public JsonArray getIndustries()
    {
        return industries;
    }


    public void setIndustries(JsonArray industries)
    {
        this.industries = industries;
    }


    public JsonArray getLanguages()
    {
        return languages;
    }


    public void setLanguages(JsonArray languages)
    {
        this.languages = languages;
    }


    public String getLargeThumbnail()
    {
        return largeThumbnail;
    }


    public void setLargeThumbnail(String largeThumbnail)
    {
        this.largeThumbnail = largeThumbnail;
    }


    public JsonArray getScreenshots()
    {
        return screenshots;
    }


    public void setScreenshots(JsonArray screenshots)
    {
        this.screenshots = screenshots;
    }


    public boolean isListed()
    {
        return listed;
    }


    public void setListed(boolean listed)
    {
        this.listed = listed;
    }


    public int getNumComments()
    {
        return numComments;
    }


    public void setNumComments(int numComments)
    {
        this.numComments = numComments;
    }


    public int getNumRatings()
    {
        return numRatings;
    }


    public void setNumRatings(int numRatings)
    {
        this.numRatings = numRatings;
    }


    public float getAvgRating()
    {
        return avgRating;
    }


    public void setAvgRating(float avgRating)
    {
        this.avgRating = avgRating;
    }


    public int getNumViews()
    {
        return numViews;
    }


    public void setNumViews(int numViews)
    {
        this.numViews = numViews;
    }


    /**
     * Returns only the ID. The string is only used to uniquely identify the map
     * inside a list to guarantee proper functionality of the
     * {@linkplain ArcGisETL}'s initHash() function.
     */
    @Override
    public String toString()
    {
        return id;
    }
}

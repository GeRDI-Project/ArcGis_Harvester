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

/**
 * This class represents a response to an ArcGis user request.
 * <br>e.g. http://www.arcgis.com/sharing/rest/community/users/dkensok?f=json
 *
 * @author Robin Weiss
 */
public class ArcGisUser
{
    private String username;
    private String fullName;
    private String firstName;
    private String lastName;
    private String description;
    private List<String> tags;
    private String culture;
    private String region;
    private String units;
    private String thumbnail;
    private long created;
    private long modified;
    private String provider;


    public String getUsername()
    {
        return username;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public String getFullName()
    {
        return fullName;
    }


    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }


    public String getFirstName()
    {
        return firstName;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getLastName()
    {
        return lastName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
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


    public String getCulture()
    {
        return culture;
    }


    public void setCulture(String culture)
    {
        this.culture = culture;
    }


    public String getRegion()
    {
        return region;
    }


    public void setRegion(String region)
    {
        this.region = region;
    }


    public String getUnits()
    {
        return units;
    }


    public void setUnits(String units)
    {
        this.units = units;
    }


    public String getThumbnail()
    {
        return thumbnail;
    }


    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
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


    public String getProvider()
    {
        return provider;
    }


    public void setProvider(String provider)
    {
        this.provider = provider;
    }
}

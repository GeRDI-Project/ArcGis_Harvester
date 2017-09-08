package de.gerdiproject.json.arcgis;

import java.util.List;

/**
 * A featured group of an {@linkplain Overview} object.
 *
 * @author Robin Weiss
 */
public class FeaturedGroup
{
    private String title;
    private String owner;
    private String id;
    private List<String> tags;


    public List<String> getTags()
    {
        return tags;
    }


    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getOwner()
    {
        return owner;
    }


    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }
}

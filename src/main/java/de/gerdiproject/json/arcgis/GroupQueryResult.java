package de.gerdiproject.json.arcgis;

/**
 * The result to a group query request.
 *
 * <br>e.g. http://www.arcgis.com/sharing/rest/community/groups?num=10&start=0&sortField=title&sortOrder=asc&q=title%3A%22Featured%20Maps%20And%20Apps%22%20AND%20owner%3Aesri&f=json
 * @author Robin Weiss
 */
public class GroupQueryResult extends QueryResult<FeaturedGroup>
{
}

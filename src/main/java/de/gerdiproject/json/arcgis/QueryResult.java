package de.gerdiproject.json.arcgis;

import java.util.List;


/**
 * The response to a generic ArcGis query request.
 *
 * @author Robin Weiss
 */
public class QueryResult<T>
{
    private String query;
    private int total;
    private int start;
    private int num;
    private int nextStart;
    private List<T> results;


    public String getQuery()
    {
        return query;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public int getTotal()
    {
        return total;
    }


    public void setTotal(int total)
    {
        this.total = total;
    }


    public int getStart()
    {
        return start;
    }


    public void setStart(int start)
    {
        this.start = start;
    }


    public int getNum()
    {
        return num;
    }


    public void setNum(int num)
    {
        this.num = num;
    }


    public int getNextStart()
    {
        return nextStart;
    }


    public void setNextStart(int nextStart)
    {
        this.nextStart = nextStart;
    }


    public List<T> getResults()
    {
        return results;
    }


    public void setResults(List<T> results)
    {
        this.results = results;
    }
}

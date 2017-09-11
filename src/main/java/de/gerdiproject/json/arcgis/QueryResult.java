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

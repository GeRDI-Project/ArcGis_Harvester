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
package de.gerdiproject.harvest.harvester;

import de.gerdiproject.harvest.MainContext;
import de.gerdiproject.harvest.harvester.sub.GroupHarvester;
import de.gerdiproject.harvest.utils.HttpRequester;
import de.gerdiproject.json.IJsonArray;
import de.gerdiproject.json.IJsonObject;
import de.gerdiproject.json.impl.JsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;


/**
 * A harvester for ArcGis (http://esri.maps.arcgis.com/home/groups.html).
 *
 * @author row
 */
public class ArcGisHarvester extends AbstractCompositeHarvester
{
	private final static String ARC_GIS_BASE_URL = "http://arcgis.com";
	private final static String ESRI_BASE_URL = "http://esri.maps.arcgis.com";
	
	private final static String OVERVIEW_URL_SUFFIX = "/sharing/rest/portals/self?culture=en&f=json";

	private final static String ARC_GIS_GROUP_DETAILS_URL = ARC_GIS_BASE_URL +"/sharing/rest/community/groups?q=%s&f=json";
	
	private final static List<String> VALID_PARAMS = new LinkedList<>();

	/**
	 * Simple constructor that passes on the inherited one.
	 * @param harvestedDocuments a list in which harvested documents are stored
	 * @param subHarvesters an array of harvesters that can run concurrently
	 */
	public ArcGisHarvester( IJsonArray harvestedDocuments, AbstractHarvester[] subHarvesters )
	{
		super( harvestedDocuments, subHarvesters );
	}
	
	/**
	 * Creates an ArcGisHarvester instance by checking all map groups in ArcGIS and Esri first.
	 * @return
	 */
	public static ArcGisHarvester createInstance()
	{
		IJsonArray harvestedDocuments = new JsonBuilder().createArray();
		AbstractHarvester[] subHarvesters = {
				createEsriHarvesterInstance(harvestedDocuments),
				createArcGisHarvesterInstance( harvestedDocuments)
		};
		
		return new ArcGisHarvester(harvestedDocuments, subHarvesters);
	}
	
	/**
	 * Creates an ArcGisHarvester instance by checking all map groups in ArcGIS first.
	 * @return
	 */
	private static ArcGisHarvester createEsriHarvesterInstance(IJsonArray harvestedDocuments)
	{
		// retrieve list of groups from ArcGis
		IJsonObject groupsObj = new HttpRequester().getRawJsonFromUrl( ESRI_BASE_URL + OVERVIEW_URL_SUFFIX );
		IJsonArray featuredGroups = groupsObj.getJsonArray( JsonConst.FEATURED_GROUPS );
		
		// init sub-harvester array
		int groupCount = featuredGroups.size();
		AbstractHarvester[] subHarvesters = new AbstractHarvester[groupCount];
		
		// create sub-harvesters
		for (int i = 0; i < groupCount; i++)
		{
			final String groupId = featuredGroups.getJsonObject( i ).getString( JsonConst.ID );
			subHarvesters[i] = new GroupHarvester( harvestedDocuments, ESRI_BASE_URL, groupId );
		}
		
		return new ArcGisHarvester(harvestedDocuments, subHarvesters);
	}
	
	/**
	 * Creates an ArcGisHarvester instance by checking all map groups in ArcGIS first.
	 * @return
	 */
	private static ArcGisHarvester createArcGisHarvesterInstance(IJsonArray harvestedDocuments)
	{
		HttpRequester httpRequester = new HttpRequester();

		// get ArcGIS overview
		IJsonObject overviewObj = httpRequester.getRawJsonFromUrl( ARC_GIS_BASE_URL + OVERVIEW_URL_SUFFIX );
		
		// assemble URL for getting the gallery group object
		String galleryQuery = overviewObj.getString( JsonConst.LIVING_ATLAS_GROUP_QUERY );
		try
		{
			String galleryDetailsUrl = String.format( ARC_GIS_GROUP_DETAILS_URL, URLEncoder.encode( galleryQuery, "UTF-8" ) );
			// retrieve details of gallery group
			IJsonArray groups = httpRequester.getRawJsonFromUrl( galleryDetailsUrl ).getJsonArray( JsonConst.RESULTS );
			
			// init sub-harvester array
			int groupCount = groups.size();
			AbstractHarvester[] subHarvesters = new AbstractHarvester[groupCount];
			
			// parse groups
			for(int i = 0; i < groupCount; i++)
			{
				IJsonObject galleryDetails = groups.getJsonObject( i );
				
				String galleryGroupId = galleryDetails.getString( JsonConst.ID );
				
				subHarvesters[i] = new GroupHarvester( harvestedDocuments, ARC_GIS_BASE_URL, galleryGroupId );
			}
			
			return new ArcGisHarvester(harvestedDocuments, subHarvesters);
		}
		catch (UnsupportedEncodingException e)
		{
			MainContext.getLogger().log( e.toString() );
			
			return null;
		}
	}
	

	@Override
	public List<String> getValidProperties()
	{
		return VALID_PARAMS;
	}

	

}

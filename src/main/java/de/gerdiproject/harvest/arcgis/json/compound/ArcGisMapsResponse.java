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
package de.gerdiproject.harvest.arcgis.json.compound;

import de.gerdiproject.harvest.arcgis.json.ArcGisMap;
import de.gerdiproject.harvest.arcgis.json.generic.GenericArcGisResponse;

/**
 * The result to a map query request.
 * <br>e.g. http://www.arcgis.com/sharing/rest/search?q=%20group%3Ac755678be14e4a0984af36a15f5b643e%20&sortField=title&sortOrder=asc&start=1&num=100&f=json
 *
 * @author Robin Weiss
 */
public class ArcGisMapsResponse extends GenericArcGisResponse<ArcGisMap>
{

}
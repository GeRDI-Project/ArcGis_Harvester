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
package de.gerdiproject.harvest.arcgis.json.generic;

import java.util.List;

import lombok.Value;


/**
 * The response to a generic ArcGis query request.
 *
 * @author Robin Weiss
 */
@Value
public class GenericArcGisResponse<T>
{
    private final String query;
    private final int total;
    private final int start;
    private final int num;
    private final int nextStart;
    private final List<T> results;
}

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

import lombok.Value;

/**
 * This class represents a response to an ArcGis user request.
 * <br>e.g. http://www.arcgis.com/sharing/rest/community/users/dkensok?f=json
 *
 * @author Robin Weiss
 */
@Value
public class ArcGisUser
{
    private final String username;
    private final String fullName;
    private final String firstName;
    private final String lastName;
    private final String description;
    private final List<String> tags;
    private final String culture;
    private final String region;
    private final String units;
    private final String thumbnail;
    private final long created;
    private final long modified;
    private final String provider;
}

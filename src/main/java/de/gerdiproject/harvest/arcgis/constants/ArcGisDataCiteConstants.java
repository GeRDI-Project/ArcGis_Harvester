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
package de.gerdiproject.harvest.arcgis.constants;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.gerdiproject.json.datacite.extension.generic.AbstractResearch;
import de.gerdiproject.json.datacite.extension.generic.WebLink;
import de.gerdiproject.json.datacite.extension.generic.constants.ResearchDisciplineConstants;
import de.gerdiproject.json.datacite.extension.generic.enums.WebLinkType;
import de.gerdiproject.json.datacite.nested.Publisher;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * A static collection of constants, used for creating ArcGis Documents.
 *
 * @author Robin Weiss
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArcGisDataCiteConstants
{
    public static final Publisher PUBLISHER = new Publisher("Esri");
    public static final String REPOSITORY_ID = "ArcGIS";
    public static final List<AbstractResearch> RESEARCH_DISCIPLINES = createResearchDisciplines();
    public static final String VIEW_URL = "%s/home/item.html?id=%s";
    public static final WebLink ESRI_LOGO_LINK = createEsriLogoLink();


    /**
     * Creates a weblink that points to the Esri logo.
     * @return a weblink that points to the Esri logo
     */
    private static WebLink createEsriLogoLink()
    {
        final WebLink logoLink = new WebLink("https://livingatlas.arcgis.com/emu/tailcoat/images/tailcoat/logo-esri.png");
        logoLink.setType(WebLinkType.ProviderLogoURL);
        return logoLink;
    }


    /**
     * Creates a list of resarch disciplines that describe ArcGis.
     *
     * @return a list of resarch disciplines that describe ArcGis
     */
    private static List<AbstractResearch> createResearchDisciplines()
    {
        return Collections.unmodifiableList(Arrays.asList(
                                                ResearchDisciplineConstants.PHYSICAL_GEOGRAPHY,
                                                ResearchDisciplineConstants.HUMAN_GEOGRAPHY
                                            ));
    }

}

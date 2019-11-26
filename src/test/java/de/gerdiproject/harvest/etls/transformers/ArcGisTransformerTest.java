/*
 *  Copyright © 2019 Robin Weiss (http://www.gerdi-project.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
package de.gerdiproject.harvest.etls.transformers;

import java.nio.charset.StandardCharsets;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.gerdiproject.harvest.ArcGisContextListener;
import de.gerdiproject.harvest.application.ContextListener;
import de.gerdiproject.harvest.etls.AbstractIteratorETL;
import de.gerdiproject.harvest.etls.ArcGisETL;
import de.gerdiproject.harvest.etls.extractors.ArcGisMapVO;
import de.gerdiproject.harvest.utils.data.DiskIO;
import de.gerdiproject.json.GsonUtils;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * This class provides Unit Tests for the {@linkplain ArcGisTransformer}.
 *
 * @author Robin Weiss
 */
@RunWith(Parameterized.class)
public class ArcGisTransformerTest extends AbstractIteratorTransformerTest<ArcGisMapVO, DataCiteJson>
{
    private static final String INPUT_FORMAT = "input-%s.json";
    private static final String OUTPUT_FORMAT = "output-%s.json";

    private final DiskIO diskReader = new DiskIO(GsonUtils.createGerdiDocumentGsonBuilder().create(), StandardCharsets.UTF_8);
    private final String transformInput;
    private final String transformOutput;

    @Parameters(name = "map type: {0}")
    public static Object[] getParameters()
    {
        return new Object[] {
                   "mocked",
                   "documentLink",
                   "imageService",
                   "layerPackage",
                   "mobileApplication",
                   "vectorTileService",
                   "webMap",
                   "webScene"
               };
    }


    /**
     * Constructor that accepts Unit Test parameters.
     *
     * @param mapType the type of the mocked map
     */
    public ArcGisTransformerTest(final String mapType)
    {
        this.transformInput = String.format(INPUT_FORMAT, mapType);
        this.transformOutput = String.format(OUTPUT_FORMAT, mapType);
    }


    @Override
    protected AbstractIteratorETL<ArcGisMapVO, DataCiteJson> getEtl()
    {
        return new ArcGisETL("MockedGroupName", "www.mo.ck/", "MockedGroupID");
    }


    @Override
    protected AbstractIteratorTransformer<ArcGisMapVO, DataCiteJson> setUpTestObjects()
    {
        AbstractIteratorTransformer<ArcGisMapVO, DataCiteJson> le = super.setUpTestObjects();

        return le;
    }


    @Override
    protected ArcGisMapVO getMockedInput()
    {
        return diskReader.getObject(getResource(transformInput), ArcGisMapVO.class);
    }


    @Override
    protected DataCiteJson getExpectedOutput()
    {
        return diskReader.getObject(getResource(transformOutput), DataCiteJson.class);
    }


    @Override
    protected ContextListener getContextListener()
    {
        return new ArcGisContextListener();
    }
}

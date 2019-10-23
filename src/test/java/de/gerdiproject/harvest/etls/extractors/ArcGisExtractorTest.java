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
package de.gerdiproject.harvest.etls.extractors;

import java.io.File;
import java.nio.charset.StandardCharsets;

import de.gerdiproject.harvest.ArcGisContextListener;
import de.gerdiproject.harvest.application.ContextListener;
import de.gerdiproject.harvest.etls.AbstractIteratorETL;
import de.gerdiproject.harvest.etls.ArcGisETL;
import de.gerdiproject.harvest.utils.data.DiskIO;
import de.gerdiproject.json.GsonUtils;
import de.gerdiproject.json.datacite.DataCiteJson;

/**
 * This class provides Unit Tests for the {@linkplain ArcGisExtractor}.
 *
 * @author Robin Weiss
 */
public class ArcGisExtractorTest extends AbstractIteratorExtractorTest<ArcGisMapVO>
{
    private static final String MOCKED_RESPONSE_FOLDER = "mockedHttpResponses";

    private final DiskIO diskReader = new DiskIO(GsonUtils.createGerdiDocumentGsonBuilder().create(), StandardCharsets.UTF_8);


    @Override
    protected AbstractIteratorETL<ArcGisMapVO, DataCiteJson> getEtl()
    {
        return new ArcGisETL("MockedGroupName", "mo.ck/", "MockedGroupID");
    }


    @Override
    protected ContextListener getContextListener()
    {
        return new ArcGisContextListener();
    }


    @Override
    protected File getConfigFile()
    {
        return getResource("config.json");
    }


    @Override
    protected File getMockedHttpResponseFolder()
    {
        return getResource(MOCKED_RESPONSE_FOLDER);
    }


    @Override
    protected ArcGisMapVO getExpectedOutput()
    {
        return diskReader.getObject(getResource("output.json"), ArcGisMapVO.class);
    }
}

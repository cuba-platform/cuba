/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.restapi.service.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.core.global.filter.OpManager;
import com.haulmont.cuba.core.global.filter.OpManagerImpl;
import com.haulmont.restapi.service.filter.testmodel.TestEnum;
import mockit.*;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 */
public class RestFilterParserTest extends CubaClientTestCase {

    private RestFilterParser restFilterParser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mocked
    private RandomStringUtils randomStringUtils;

    @Before
    public void setUp() {
        addEntityPackage("com.haulmont.cuba");
        addEntityPackage("com.haulmont.restapi.service.filter.testmodel");
        setupInfrastructure();

        restFilterParser = new RestFilterParser();
        restFilterParser.metadata = this.metadata;
        restFilterParser.opManager = new OpManagerImpl();
    }

    @Test
    public void testParseSimpleFilter() throws Exception {
        new Expectations() {{
            RandomStringUtils.randomAlphabetic(anyInt); returns("stringParamName",
                    "intParamName", "booleanParamName");
        }};

        String data = readDataFromFile("data/restFilter1.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "({E}.stringField <> :stringParamName and " +
                "{E}.intField > :intParamName and " +
                "{E}.booleanField = :booleanParamName)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();

        assertEquals("stringValue", queryParameters.get("stringParamName"));
        assertEquals(100, queryParameters.get("intParamName"));
        assertEquals(true, queryParameters.get("booleanParamName"));
    }

    @Test
    public void testEntityCondition() throws Exception {
        new Expectations() {{
            RandomStringUtils.randomAlphabetic(anyInt); result = "paramName1";
        }};

        String data = readDataFromFile("data/restFilter2.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "({E}.linkedTestEntity.id = :paramName1)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();
        assertEquals(UUID.fromString("2de6a78f-7bef-89a7-eb5e-b725582f23af"), queryParameters.get("paramName1"));
    }

    @Test
    public void testOrGroup() throws Exception {
        new Expectations() {{
            RandomStringUtils.randomAlphabetic(anyInt); returns("stringParamName",
                    "intParamName","booleanParamName");
        }};

        String data = readDataFromFile("data/restFilter3.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "(({E}.stringField <> :stringParamName or " +
                "{E}.intField > :intParamName) and " +
                "{E}.booleanField = :booleanParamName)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();

        assertEquals("stringValue", queryParameters.get("stringParamName"));
        assertEquals(100, queryParameters.get("intParamName"));
        assertEquals(true, queryParameters.get("booleanParamName"));
    }

    @Test
    public void testEnumCondition() throws Exception {
        new Expectations() {{
            RandomStringUtils.randomAlphabetic(anyInt); result = "paramName1";
        }};

        String data = readDataFromFile("data/restFilter4.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "({E}.enumField = :paramName1)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();

        assertEquals(TestEnum.ENUM_VALUE_1, queryParameters.get("paramName1"));
    }

    @Test
    public void testInOperator() throws Exception {
        new Expectations() {{
            RandomStringUtils.randomAlphabetic(anyInt); result = "paramName1";
        }};

        String data = readDataFromFile("data/restFilter5.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "({E}.intField in :paramName1)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();

        List<Integer> param1Value = (List<Integer>) queryParameters.get("paramName1");
        assertEquals(Arrays.asList(1, 2, 3), param1Value);
    }

    @Test
    public void testStartsWithOperator() throws Exception {
        new Expectations() {{
            RandomStringUtils.randomAlphabetic(anyInt); result = "paramName1";
        }};

        String data = readDataFromFile("data/restFilter6.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "({E}.stringField like :paramName1)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();
        assertEquals("(?i)AAA%", queryParameters.get("paramName1"));
    }

    @Test
    public void testNotEmptyOperator() throws Exception {
        String data = readDataFromFile("data/restFilter7.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        RestFilterParseResult parseResult = restFilterParser.parse(data, metaClass);

        String expectedJpqlWhere = "({E}.stringField is not null)";
        assertEquals(expectedJpqlWhere, parseResult.getJpqlWhere());

        Map<String, Object> queryParameters = parseResult.getQueryParameters();
        assertEquals(0, queryParameters.size());
    }

    @Test
    public void testMissingJsonFieldInCondition() throws Exception {
        String data = readDataFromFile("data/invalidRestFilter1.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        thrown.expect(RestFilterParseException.class);
        thrown.expectMessage("Field 'operator' is not defined for filter condition");

        restFilterParser.parse(data, metaClass);
    }

    @Test
    public void testInvalidPropertyValue() throws Exception {
        String data = readDataFromFile("data/invalidRestFilter2.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        thrown.expect(RestFilterParseException.class);
        thrown.expectMessage("Cannot parse property value: string");

        restFilterParser.parse(data, metaClass);
    }

    @Test
    public void testInvalidGroupType() throws Exception {
        String data = readDataFromFile("data/invalidRestFilter3.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        thrown.expect(RestFilterParseException.class);
        thrown.expectMessage("Invalid conditions group type: INVALID_GROUP_TYPE");

        restFilterParser.parse(data, metaClass);
    }

    @Test
    public void testNonExistingMetaProperty() throws Exception {
        String data = readDataFromFile("data/invalidRestFilter4.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        thrown.expect(RestFilterParseException.class);
        thrown.expectMessage("Property for test$TestEntity not found: missingProperty");

        restFilterParser.parse(data, metaClass);
    }

    @Test
    public void testInappropriateOperatorForType() throws Exception {
        String data = readDataFromFile("data/invalidRestFilter5.json");
        MetaClass metaClass = metadata.getClass("test$TestEntity");
        thrown.expect(RestFilterParseException.class);
        thrown.expectMessage("Operator > is not available for java type java.lang.String");

        restFilterParser.parse(data, metaClass);
    }

    protected String readDataFromFile(String filePath) throws Exception {
        Path path = Paths.get(RestFilterParser.class.getResource(filePath).toURI());
        byte[] data = Files.readAllBytes(path);
        return new String(data);
    }
}

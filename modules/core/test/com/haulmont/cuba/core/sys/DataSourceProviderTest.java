/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DataSourceProviderTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Logger log = LoggerFactory.getLogger(DataSourceProviderTest.class);
    private DataSourceProvider dataSourceProvider = new DataSourceProvider();

    @Test
    void testGetUrlPrefix() {
        String urlPrefix = dataSourceProvider.getUrlPrefix(Stores.MAIN);
        Assertions.assertEquals("jdbc:hsqldb:hsql://", urlPrefix);
    }

    @Test
    void testGetUrlPrefixError() {
        Assertions.assertThrows(RuntimeException.class, () -> dataSourceProvider.getUrlPrefix("NONEXISTENT_STORE"));
    }

    @Test
    void testGetDataSourceProvider() {
        String dsProvider = dataSourceProvider.getDataSourceProvider(Stores.MAIN);
        Assertions.assertEquals("application", dsProvider);
    }

    @Test
    void testGetNonexistentDataSourceProvider() {
        String dsProvider = dataSourceProvider.getDataSourceProvider("NONEXISTENT_STORE");
        Assertions.assertNull(dsProvider);
    }

    @Test
    void testGetJdbcUrlFromParts() {
        Map<String, String> dsParameters = dataSourceProvider.getDataSourceParameters(Stores.MAIN);
        String jdbcUrl = dataSourceProvider.constructJdbcUrl(Stores.MAIN, dsParameters);
        Assertions.assertEquals("jdbc:hsqldb:hsql://localhost:9111/cubadb", jdbcUrl);
    }
}

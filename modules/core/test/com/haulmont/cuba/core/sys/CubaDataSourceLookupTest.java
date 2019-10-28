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
import com.haulmont.cuba.core.sys.jdbc.ProxyDataSource;
import com.haulmont.cuba.testsupport.TestContainer;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class CubaDataSourceLookupTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Logger log = LoggerFactory.getLogger(CubaDataSourceLookupTest.class);
    private CubaDataSourceLookup cubaDataSourceLookup = new CubaDataSourceLookup();

    @Test
    void testGetUrlPrefix() {
        String urlPrefix = cubaDataSourceLookup.getUrlPrefix(Stores.MAIN);
        Assertions.assertEquals("jdbc:hsqldb:hsql://", urlPrefix);
    }

    @Test
    void testGetUrlPrefixError() {
        Assertions.assertThrows(RuntimeException.class, () -> cubaDataSourceLookup.getUrlPrefix("NONEXISTENT_STORE"));
    }

    @Test
    void testGetDataSourceProvider() {
        String dsProvider = cubaDataSourceLookup.getDataSourceProvider(Stores.MAIN);
        Assertions.assertEquals("application", dsProvider);
    }

    @Test
    void testGetNonexistentDataSourceProvider() {
        String dsProvider = cubaDataSourceLookup.getDataSourceProvider("NONEXISTENT_STORE");
        Assertions.assertNull(dsProvider);
    }

    @Test
    void testIsHikariConfigField() {
        Assertions.assertTrue(cubaDataSourceLookup.isHikariConfigField("maximumPoolSize"));
        Assertions.assertFalse(cubaDataSourceLookup.isHikariConfigField("maximumPoolSize_FALSE"));
    }

    @Test
    void testGetJdbcUrlFromParts() {
        String jdbcUrl = cubaDataSourceLookup.getJdbcUrlFromParts("cuba.dataSource.", Stores.MAIN);
        Assertions.assertEquals("jdbc:hsqldb:hsql://localhost:9111/cubadb", jdbcUrl);
    }

    @Test
    void testGetAndCloseApplicationDataSource() {
        DataSource appDataSource = cubaDataSourceLookup.getApplicationDataSource(Stores.MAIN);
        try {
            Assertions.assertTrue(appDataSource != null && ProxyDataSource.class.isAssignableFrom(appDataSource.getClass())
                    && appDataSource.isWrapperFor(HikariDataSource.class));
        } catch (SQLException e) {
            log.error("testGetAndCloseApplicationDataSource test failed", e);
        }
        Assertions.assertTrue(cubaDataSourceLookup.closeApplicationDataSource(Stores.MAIN, appDataSource));
    }
}

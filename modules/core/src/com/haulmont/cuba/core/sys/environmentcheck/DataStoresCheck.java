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

package com.haulmont.cuba.core.sys.environmentcheck;

import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.dbupdate.DbProperties;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataStoresCheck implements EnvironmentCheck {

    private static final Logger log = LoggerFactory.getLogger(DataStoresCheck.class);

    @Override
    public List<CheckFailedResult> doCheck() {
        List<CheckFailedResult> result = new ArrayList<>();
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        DataSource dataSource;
        String mainDsJndiName = AppContext.getProperty("cuba.dataSourceJndiName");
        try {
            dataSource = lookup.getDataSource(mainDsJndiName == null ? "jdbc/CubaDS" : mainDsJndiName);
            List<CheckFailedResult> checkFailedResults = checkDataStore(Stores.MAIN, dataSource);
            if (!checkFailedResults.isEmpty()) {
                result.addAll(checkFailedResults);
            }
        } catch (DataSourceLookupFailureException e) {
            result.add(new CheckFailedResult("Can not find JNDI datasource for Data Store: Main", e));
        }

        String additionalStores = AppContext.getProperty("cuba.additionalStores");
        if (additionalStores != null && Boolean.valueOf(AppContext.getProperty("cuba.checkConnectionToAdditionalDataStoresOnStartup"))) {
            for (String storeName : additionalStores.replaceAll("\\s", "").split(",")) {
                String storeJndiName = AppContext.getProperty("cuba.dataSourceJndiName_" + storeName);
                try {
                    dataSource = lookup.getDataSource(storeJndiName == null ? "" : storeJndiName);
                    List<CheckFailedResult> checkFailedResults = checkDataStore(storeName, dataSource);
                    if (!checkFailedResults.isEmpty()) {
                        result.addAll(checkFailedResults);
                    }
                } catch (DataSourceLookupFailureException e) {
                    String beanName = AppContext.getProperty("cuba.storeImpl_" + storeName);
                    if (beanName == null) {
                        result.add(new CheckFailedResult(
                                String.format("Can not find JNDI datasource for Data Store: %s", storeName),
                                null));
                    }
                }
            }
        }
        return result;
    }

    protected List<CheckFailedResult> checkDataStore(String storeName, DataSource dataSource) {
        List<CheckFailedResult> result = new ArrayList<>();
        Connection connection = null;
        try {
            log.info("Checking connection to data store {}", storeName);

            connection = dataSource.getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();

            if (Stores.isMain(storeName) && !Boolean.TRUE.equals(Boolean.parseBoolean(AppContext.getProperty("cuba.automaticDatabaseUpdate")))) {
                DbProperties dbProperties = new DbProperties(dbMetaData.getURL());
                boolean isRequiresCatalog = DbmsSpecificFactory.getDbmsFeatures().isRequiresDbCatalogName();
                boolean isSchemaByUser = DbmsSpecificFactory.getDbmsFeatures().isSchemaByUser();
                String catalogName = isRequiresCatalog ? connection.getCatalog() : null;
                String schemaName = isSchemaByUser ?
                        dbMetaData.getUserName() : dbProperties.getCurrentSchemaProperty();
                ResultSet tables = dbMetaData.getTables(catalogName, schemaName, "%", null);
                boolean found = false;
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    if ("SEC_USER".equalsIgnoreCase(tableName)) {
                        found = true;
                    }
                }
                if (!found) {
                    result.add(new CheckFailedResult("Main Data Store checked but SEC_USER table is not found - Data Store does not look like CUBA database", null));
                }
            }
        } catch (Throwable e) {
            result.add(new CheckFailedResult(
                    String.format("Exception occurred while connecting to Data Store: %s", storeName), e));
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Throwable e) {
                result.add(new CheckFailedResult(
                        String.format("Exception occurred while closing connection to Data Store: %s", storeName),
                        e));
            }
        }
        return result;
    }
}

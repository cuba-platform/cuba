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
import com.haulmont.cuba.core.sys.DataSourceProvider;
import com.haulmont.cuba.core.sys.dbupdate.DbProperties;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DataStoresCheck implements EnvironmentCheck {

    private static final Logger log = LoggerFactory.getLogger(DataStoresCheck.class);

    private DataSourceProvider dataSourceProvider = new DataSourceProvider();

    @Override
    public List<CheckFailedResult> doCheck() {

        List<CheckFailedResult> result = new ArrayList<>(checkDataStore(Stores.MAIN, () -> dataSourceProvider.getConnection(Stores.MAIN, getDataSourceJndiName(Stores.MAIN))));

        String additionalStores = AppContext.getProperty("cuba.additionalStores");
        if (additionalStores != null && Boolean.parseBoolean(AppContext.getProperty("cuba.checkConnectionToAdditionalDataStoresOnStartup"))) {
            for (String storeName : additionalStores.replaceAll("\\s", "").split(",")) {
                result.addAll(checkDataStore(storeName, () -> dataSourceProvider.getConnection(storeName, getDataSourceJndiName(storeName))));
            }
        }
        return result;
    }

    protected List<CheckFailedResult> checkDataStore(String storeName, ConnectionSupplier connectionSupplier) {
        List<CheckFailedResult> result = new ArrayList<>();
        Connection connection = null;
        try {
            log.info("Checking connection to data store {}", Stores.storeNameToString(storeName));

            connection = connectionSupplier.get();

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
                    result.add(new CheckFailedResult("SEC_USER table not found in main data store - it doesn't look like a CUBA database", null));
                }
            }

        } catch (DataSourceLookupFailureException e) {
            if (Stores.isMain(storeName)) {
                result.add(new CheckFailedResult("Cannot find datasource for main data store", e));
            } else {
                String beanName = AppContext.getProperty("cuba.storeImpl_" + storeName);
                if (beanName == null) {
                    result.add(new CheckFailedResult(
                            String.format("Cannot find datasource for '%s' data store", storeName),
                            null));
                }
            }
        } catch (Throwable e) {
            result.add(new CheckFailedResult(
                    String.format("Exception occurred while connecting to data store %s", storeName), e));
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    //Do nothing
                }
            }
        }

        return result;
    }

    protected String getDataSourceJndiName(String storeName) {
        if (Stores.MAIN.equals(storeName)) {
            String jndiName = AppContext.getProperty("cuba.dataSourceJndiName");
            return jndiName == null ? "jdbc/CubaDS" : jndiName;
        }
        return AppContext.getProperty(String.format("cuba.dataSourceJndiName_%s", storeName));
    }

    @FunctionalInterface
    protected interface ConnectionSupplier {
        Connection get() throws SQLException;
    }
}

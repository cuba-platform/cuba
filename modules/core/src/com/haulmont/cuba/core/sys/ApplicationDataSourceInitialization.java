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

import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.jdbc.ProxyDataSource;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationDataSourceInitialization {

    protected static final String JDBC_URL = "jdbcUrl";
    protected static final String MS_SQL_2005 = "2005";
    protected static final String POSTGRES_DBMS = "postgres";
    protected static final String MSSQL_DBMS = "mssql";
    protected static final String ORACLE_DBMS = "oracle";
    protected static final String MYSQL_DBMS = "mysql";
    protected static final String HSQL_DBMS = "hsql";
    protected static final String HOST = "hostname";
    protected static final String PORT = "port";
    protected static final String DB_NAME = "dbName";
    protected static final String CONNECTION_PARAMS = "connectionParams";
    protected static final ImmutableList<String> cubaDsDefaultParams = ImmutableList.of(HOST, PORT, DB_NAME, CONNECTION_PARAMS);

    public DataSource getApplicationDataSource(String storeName) {
        if (storeName == null) {
            storeName = Stores.MAIN;
        }
        Properties hikariConfigProperties = getHikariConfigProperties(storeName);
        HikariConfig config = new HikariConfig(hikariConfigProperties);

        config.setRegisterMbeans(true);

        config.setPoolName("HikariPool-" + storeName);

        HikariDataSource ds = new HikariDataSource(config);

        return new ProxyDataSource(ds);
    }

    protected Properties getHikariConfigProperties(String storeName) {
        String cubaConfigDsPrefix = "cuba.dataSource.";
        if (!Stores.isMain(storeName)) {
            cubaConfigDsPrefix = "cuba.dataSource_" + storeName + ".";
        }

        Map<String, String> cubaDsProperties = getAllDsProperties(cubaConfigDsPrefix);
        Properties hikariConfigProperties = getHikariConfigProperties(cubaDsProperties);

        if (hikariConfigProperties.getProperty(JDBC_URL) == null) {
            hikariConfigProperties.setProperty(JDBC_URL, getJdbcUrlFromParts(cubaConfigDsPrefix, storeName));
        }
        return hikariConfigProperties;
    }

    protected Properties getHikariConfigProperties(Map<String, String> properties) {
        Properties hikariConfigProperties = new Properties();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            if (cubaDsDefaultParams.contains(property.getKey())) {
                continue;
            }
            String hikariConfigDsPrefix = "dataSource.";
            if (isHikariConfigField(property.getKey())) {
                hikariConfigDsPrefix = "";
            }
            hikariConfigProperties.put(hikariConfigDsPrefix.concat(property.getKey()), property.getValue());
        }
        return hikariConfigProperties;
    }

    protected Map<String, String> getAllDsProperties(String dsPrefix) {
        Map<String, String> allDsProperties = new HashMap<>();
        String[] propertiesNames = AppContext.getPropertyNames();
        for (String cubaPropertyName : propertiesNames) {
            if (!cubaPropertyName.startsWith(dsPrefix)) {
                continue;
            }
            String value = AppContext.getProperty(cubaPropertyName);
            if (value == null) {
                continue;
            }
            allDsProperties.put(cubaPropertyName.replace(dsPrefix, ""), value);
        }
        return allDsProperties;
    }

    protected String getJdbcUrlFromParts(String dataSourcePrefix, String storeName) {
        String urlPrefix = getUrlPrefix(storeName);
        String host = AppContext.getProperty(dataSourcePrefix + HOST);
        String port = AppContext.getProperty(dataSourcePrefix + PORT);
        String dbName = AppContext.getProperty(dataSourcePrefix + DB_NAME);
        String connectionParams = AppContext.getProperty(dataSourcePrefix + CONNECTION_PARAMS);
        if (host == null || port == null || dbName == null) {
            throw new RuntimeException(String.format("jdbcUrl parameter is not specified! Can't form jdbcUrl from parts: " +
                    "provided hostname: %s, port: %s, dbName: %s.", host, port, dbName));
        }

        String jdbcUrl = urlPrefix + host + ":" + port + "/" + dbName;
        if (MSSQL_DBMS.equals(DbmsType.getType(storeName)) && !MS_SQL_2005.equals(DbmsType.getVersion(storeName))) {
            jdbcUrl = urlPrefix + host + ":" + port + ";databaseName=" + dbName;
        }

        if (StringUtils.isBlank(connectionParams) && MYSQL_DBMS.equals(DbmsType.getType(storeName))) {
            connectionParams = "?useSSL=false&allowMultiQueries=true&serverTimezone=UTC";
        }
        if (!StringUtils.isBlank(connectionParams)) {
            jdbcUrl = jdbcUrl.concat(connectionParams);
        }

        return jdbcUrl;
    }

    protected boolean isHikariConfigField(String propertyName) {
        Method[] methods = HikariConfig.class.getMethods();
        String setterName = "set".concat(StringUtils.capitalize(propertyName));
        for (Method method : methods) {
            if (setterName.equals(method.getName()) && method.getParameterCount() == 1) {
                return true;
            }
        }
        return false;
    }

    protected String getUrlPrefix(String storeName) {
        String dbmsType = DbmsType.getType(storeName);
        if (dbmsType == null) {
            throw new RuntimeException("dbmsType should be specified for each dataSource!");
        }
        switch (dbmsType) {
            case POSTGRES_DBMS:
                return "jdbc:postgresql://";
            case MSSQL_DBMS:
                if (MS_SQL_2005.equals(DbmsType.getVersion(storeName))) {
                    return "jdbc:jtds:sqlserver://";
                }
                return "jdbc:sqlserver://";
            case ORACLE_DBMS:
                return "jdbc:oracle:thin:@//";
            case MYSQL_DBMS:
                return "jdbc:mysql://";
            case HSQL_DBMS:
                return "jdbc:hsqldb:hsql://";
            default:
                throw new RuntimeException(String.format("dbmsType '%s' is unsupported!", dbmsType));
        }
    }
}

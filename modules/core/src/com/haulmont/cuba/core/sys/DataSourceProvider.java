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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.jdbc.ProxyDataSource;
import com.haulmont.cuba.core.sys.persistence.DbmsType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class DataSourceProvider {

    protected static final String DATA_SOURCE_PROVIDER_PROPERTY_NAME = "cuba.dataSourceProvider";

    protected static final String APPLICATION = "application";
    protected static final String JNDI = "jndi";

    protected static final String MS_SQL_2005 = "2005";
    protected static final String POSTGRES_DBMS = "postgres";
    protected static final String MSSQL_DBMS = "mssql";
    protected static final String ORACLE_DBMS = "oracle";
    protected static final String MYSQL_DBMS = "mysql";
    protected static final String HSQL_DBMS = "hsql";

    protected static final String JDBC_URL = "jdbcUrl";
    protected static final String HOST = "host";
    protected static final String PORT = "port";
    protected static final String DB_NAME = "dbName";
    protected static final String CONNECTION_PARAMS = "connectionParams";

    protected static final String USER_NAME = "username";
    protected static final String PASSWORD = "password";

    protected static final String DRIVER_CLASS_NAME = "driverClassName";

    protected static final ImmutableList<String> DS_CONNECTION_PARAMS = ImmutableList.of(HOST, PORT, DB_NAME, CONNECTION_PARAMS);

    private static Logger log = LoggerFactory.getLogger(DataSourceProvider.class);

    public DataSource getDataSource(String storeName, @Nullable String jndiName) {
        String dataSourceProvider = getDataSourceProvider(storeName);

        if (isJndiDataSource(dataSourceProvider)) {
            return getJndiDataSource(jndiName);
        } else if (isApplicationDataSource(dataSourceProvider)) {
            return getApplicationDataSource(storeName);
        } else {
            throw new RuntimeException(String.format("DataSource provider '%s' is unsupported! Available: 'jndi', 'application'", dataSourceProvider));
        }
    }

    public Connection getConnection(String storeName, @Nullable String jndiName) throws SQLException {
        String dataSourceProvider = getDataSourceProvider(storeName);

        if (isJndiDataSource(dataSourceProvider)) {
            return getJndiDataSource(jndiName).getConnection();
        } else if (isApplicationDataSource(dataSourceProvider)) {
            return getApplicationConnection(storeName);
        } else {
            throw new UnsupportedOperationException(String.format("DataSource provider '%s' is unsupported! Available: 'jndi', 'application'", dataSourceProvider));
        }
    }

    public void closeDataSource(DataSource dataSource) {
        try {
            if (dataSource instanceof ProxyDataSource
                    && dataSource.isWrapperFor(HikariDataSource.class)) {
                dataSource.unwrap(HikariDataSource.class).close();
            }
        } catch (SQLException e) {
            log.debug("Error while destroy application dataSource", e);
        }
    }

    protected DataSource getJndiDataSource(String jndiName) {
        Preconditions.checkNotNull(jndiName, "Jndi name is null");
        DataSourceLookup lookup = new JndiDataSourceLookup();
        return new ProxyDataSource(lookup.getDataSource(jndiName));
    }

    protected DataSource getApplicationDataSource(String storeName) {
        String actualStoreName = storeName == null ? Stores.MAIN : storeName;

        Map<String, String> dsParameters = getDataSourceParameters(actualStoreName);

        HikariConfig config = getConnectionPoolConfig(actualStoreName, dsParameters);

        return new ProxyDataSource(new HikariDataSource(config));
    }

    protected Connection getApplicationConnection(String storeName) throws SQLException {
        String actualStoreName = storeName == null ? Stores.MAIN : storeName;

        Map<String, String> dsParameters = getDataSourceParameters(actualStoreName);

        try {
            String driverClassName = dsParameters.get(DRIVER_CLASS_NAME);

            Class.forName(driverClassName);

            return DriverManager.getConnection(dsParameters.get(JDBC_URL), dsParameters.get(USER_NAME), dsParameters.get(PASSWORD));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find driver class", e);
        }
    }

    protected HikariConfig getConnectionPoolConfig(String storeName, Map<String, String> dsParameters) {
        Map<String, String> configParameters = dsParameters.entrySet().stream()
                .filter(e -> !DS_CONNECTION_PARAMS.contains(e.getKey()))
                .collect(Collectors.toMap(e -> getConfigParameterName(e.getKey()), Map.Entry::getValue));

        HikariConfig config = new HikariConfig(MapUtils.toProperties(configParameters));
        config.setRegisterMbeans(true);
        config.setPoolName(String.format("Connection Pool-%s", Stores.storeNameToString(storeName)));

        return config;
    }

    protected Map<String, String> getDataSourceParameters(String storeName) {
        String parameterPrefix = getParameterPrefix(storeName);
        //noinspection ConstantConditions
        Map<String, String> parameters = Arrays.stream(AppContext.getPropertyNames())
                .filter(p -> p.startsWith(parameterPrefix))
                .filter(p -> Objects.nonNull(AppContext.getProperty(p)))
                .collect(Collectors.toMap(p -> p.substring(parameterPrefix.length()), AppContext::getProperty));

        if (parameters.get(JDBC_URL) == null) {
            parameters.put(JDBC_URL, constructJdbcUrl(storeName, parameters));
        }

        if (parameters.get(DRIVER_CLASS_NAME) == null) {
            parameters.put(DRIVER_CLASS_NAME, getDriverClassName(storeName));
        }

        return parameters;
    }

    protected String constructJdbcUrl(String storeName, Map<String, String> parameters) {

        String urlPrefix = getUrlPrefix(storeName);

        String host = parameters.get(HOST);
        String port = parameters.get(PORT);
        String dbName = parameters.get(DB_NAME);
        String connectionParams = parameters.get(CONNECTION_PARAMS);

        if (host == null || dbName == null) {
            throw new UnsupportedOperationException(String.format("jdbcUrl parameter is not specified! Can't form jdbcUrl from parts: " +
                    "provided hostname: %s, port: %s, dbName: %s.", host, port, dbName));
        }

        String jdbcUrl = urlPrefix + host + getPortString(port) + "/" + dbName;
        if (MSSQL_DBMS.equals(DbmsType.getType(storeName)) && !MS_SQL_2005.equals(DbmsType.getVersion(storeName))) {
            jdbcUrl = urlPrefix + host + getPortString(port) + ";databaseName=" + dbName;
        }

        if (Strings.isNullOrEmpty(connectionParams) && MYSQL_DBMS.equals(DbmsType.getType(storeName))) {
            connectionParams = "?useSSL=false&allowMultiQueries=true&serverTimezone=UTC";
        }
        if (!Strings.isNullOrEmpty(connectionParams)) {
            jdbcUrl = jdbcUrl + connectionParams;
        }

        return jdbcUrl;
    }

    protected String getPortString(String port) {
        return Strings.isNullOrEmpty(port) ? "" : ":" + port;
    }

    protected String getUrlPrefix(String storeName) {
        String dbmsType = DbmsType.getType(storeName);
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
                throw new UnsupportedOperationException(String.format("dbmsType '%s' is unsupported. " +
                        "You should either provide 'driverClassName', or specify one of supported DBMS in 'dbmsType' property", dbmsType));
        }
    }

    protected String getDriverClassName(String storeName) {
        String dbmsType = DbmsType.getType(storeName);
        switch (dbmsType) {
            case POSTGRES_DBMS:
                return "org.postgresql.Driver";
            case MSSQL_DBMS:
                if (MS_SQL_2005.equals(DbmsType.getVersion(storeName))) {
                    return "net.sourceforge.jtds.jdbc.Driver";
                }
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case ORACLE_DBMS:
                return "oracle.jdbc.OracleDriver";
            case MYSQL_DBMS:
                return "com.mysql.jdbc.Driver";
            case HSQL_DBMS:
                return "org.hsqldb.jdbc.JDBCDriver";
            default:
                throw new UnsupportedOperationException(String.format("dbmsType '%s' is unsupported. " +
                        "You should either provide 'jdbcUrl', or specify one of supported DBMS in 'dbmsType' property", dbmsType));
        }
    }

    protected String getParameterPrefix(String storeName) {
        return Stores.isMain(storeName) ? "cuba.dataSource." : String.format("cuba.dataSource_%s.", storeName);
    }

    protected String getConfigParameterName(String key) {
        String setterName = "set" + StringUtils.capitalize(key);

        if (Arrays.stream(HikariConfig.class.getMethods())
                .anyMatch(m -> setterName.equals(m.getName()) && m.getParameterCount() == 1)) {
            return key;
        } else {
            return "dataSource." + key;
        }
    }

    protected String getDataSourceProvider(String storeName) {
        if (Stores.isMain(storeName)) {
            return AppContext.getProperty(DATA_SOURCE_PROVIDER_PROPERTY_NAME);
        }
        return AppContext.getProperty(DATA_SOURCE_PROVIDER_PROPERTY_NAME + "_" + storeName);
    }

    protected boolean isApplicationDataSource(String dataSourceProvider) {
        return APPLICATION.equals(dataSourceProvider);
    }

    protected boolean isJndiDataSource(String dataSourceProvider) {
        return dataSourceProvider == null || JNDI.equals(dataSourceProvider);
    }
}

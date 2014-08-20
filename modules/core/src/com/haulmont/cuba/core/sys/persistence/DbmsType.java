/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.db.DbUtils;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * System level enum encapsulating ORM initialization differences.
 * <p/>
 * For internal use only, in application code use {@link com.haulmont.cuba.core.global.DbDialect} and
 * {@link DbTypeConverter} obtained from {@link com.haulmont.cuba.core.Persistence} bean.
 *
 *
 * @author krivopustov
 * @version $Id$
 */
public enum DbmsType {

    HSQL() {
        @Override
        public Map<String, String> getJpaParameters() {
            Map<String, String> params = new HashMap<>();
            params.put("openjpa.jdbc.DBDictionary",
                    "com.haulmont.cuba.core.sys.persistence.CubaHSQLDictionary(RequiresCastForComparisons=true,SupportsSelectForUpdate=true)");
            params.put("openjpa.jdbc.MappingDefaults",
                    "FieldStrategies='java.util.UUID=com.haulmont.cuba.core.sys.persistence.UuidStringValueHandler'");
            return params;
        }
    },

    POSTGRES() {
        @Override
        public Map<String, String> getJpaParameters() {
            Map<String, String> params = new HashMap<>();
            params.put("openjpa.jdbc.DBDictionary",
                    "com.haulmont.cuba.core.sys.persistence.CubaPostgresDictionary(RequiresCastForComparisons=true)");
            params.put("openjpa.jdbc.MappingDefaults",
                    "FieldStrategies='java.util.UUID=com.haulmont.cuba.core.sys.persistence.UuidPostgresValueHandler'");
            return params;
        }
    },

    MSSQL() {
        @Override
        public Map<String, String> getJpaParameters() {
            Map<String, String> params = new HashMap<>();
            params.put("openjpa.jdbc.DBDictionary",
                    "com.haulmont.cuba.core.sys.persistence.CubaMssqlDictionary(RequiresCastForComparisons=true)");
            params.put("openjpa.jdbc.MappingDefaults",
                    "FieldStrategies='java.util.UUID=com.haulmont.cuba.core.sys.persistence.UuidMssqlValueHandler'");
            return params;
        }
    },

    ORACLE() {
        @Override
        public Map<String, String> getJpaParameters() {
            Map<String, String> params = new HashMap<>();
            putDbSchema(params);
            params.put("openjpa.jdbc.DBDictionary",
                    "com.haulmont.cuba.core.sys.persistence.CubaOracleDictionary(RequiresCastForComparisons=true)");
            params.put("openjpa.jdbc.MappingDefaults",
                    "FieldStrategies='java.util.UUID=com.haulmont.cuba.core.sys.persistence.UuidStringValueHandler(Compact=true)," +
                            "java.lang.Boolean=com.haulmont.cuba.core.sys.persistence.BooleanCharValueHandler'");
            return params;
        }
    };

    private static void putDbSchema(Map<String, String> params) {
        String dsName = AppContext.getProperty("cuba.dataSourceJndiName");
        DataSource ds;
        try {
            InitialContext context = new InitialContext();
            ds = (DataSource) context.lookup(dsName);
        } catch (NamingException e) {
            throw new RuntimeException("Error locating datasource " + dsName, e);
        }
        Connection connection = null;
        try {
            connection = ds.getConnection();
            String userName = connection.getMetaData().getUserName();
            if (!StringUtils.isEmpty(userName)) {
                params.put("openjpa.jdbc.Schema", userName.toUpperCase());
                log.info("Set openjpa.jdbc.Schema=" + userName.toUpperCase());
            } else {
                log.warn("Unable to set openjpa.jdbc.Schema: DatabaseMetaData.getUserName() returns nothing");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to set openjpa.jdbc.Schema", e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    public abstract Map<String, String> getJpaParameters();

    protected static final Log log = LogFactory.getLog(DbmsType.class);

    public String getId() {
        return name().toLowerCase();
    }

    public static DbmsType getCurrent() {
        String id = AppContext.getProperty("cuba.dbmsType");
        if (StringUtils.isBlank(id))
            throw new IllegalStateException("cuba.dbmsType is not set");
        return fromId(id);
    }

    public static DbmsType fromId(String id) {
        for (DbmsType dbmsType : DbmsType.values()) {
            if (dbmsType.name().equalsIgnoreCase(id)) {
                return dbmsType;
            }
        }
        throw new IllegalArgumentException("Unknown DBMS type: " + id);
    }
}
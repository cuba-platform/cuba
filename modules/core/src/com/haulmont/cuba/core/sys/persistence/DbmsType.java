/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

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
                    "com.haulmont.cuba.core.sys.persistence.CubaHSQLDictionary(RequiresCastForComparisons=true)");
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
            params.put("openjpa.jdbc.DBDictionary",
                    "com.haulmont.cuba.core.sys.persistence.CubaOracleDictionary(RequiresCastForComparisons=true)");
            params.put("openjpa.jdbc.MappingDefaults",
                    "FieldStrategies='java.util.UUID=com.haulmont.cuba.core.sys.persistence.UuidStringValueHandler(Compact=true)," +
                            "java.lang.Boolean=com.haulmont.cuba.core.sys.persistence.BooleanCharValueHandler'");
            return params;
        }
    };

    public abstract Map<String, String> getJpaParameters();

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

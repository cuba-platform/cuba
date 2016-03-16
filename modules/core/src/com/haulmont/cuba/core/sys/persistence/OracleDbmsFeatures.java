/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.db.DbUtils;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 */
@SuppressWarnings("UnusedDeclaration")
public class OracleDbmsFeatures implements DbmsFeatures {

    private static Logger log = LoggerFactory.getLogger(OracleDbmsFeatures.class);

    @Override
    public Map<String, String> getJpaParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("eclipselink.target-database", "com.haulmont.cuba.core.sys.persistence.CubaOraclePlatform");
        putDbSchema(params);
        return params;
    }

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
            // todo EL: Oracle schema name
//            if (!StringUtils.isEmpty(userName)) {
//                params.put("openjpa.jdbc.Schema", userName.toUpperCase());
//                log.info("Set openjpa.jdbc.Schema=" + userName.toUpperCase());
//            } else {
//                log.warn("Unable to set openjpa.jdbc.Schema: DatabaseMetaData.getUserName() returns nothing");
//            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to set openjpa.jdbc.Schema", e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    @Override
    public String getIdColumn() {
        return "ID";
    }

    @Override
    public String getDeleteTsColumn() {
        return "DELETE_TS";
    }

    @Override
    public String getTimeStampType() {
        return "timestamp";
    }

    @Nullable
    @Override
    public String getUuidTypeClassName() {
        return null;
    }

    @Nullable
    @Override
    public String getTransactionTimeoutStatement() {
        return null;
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "unique constraint \\((.+)\\) violated";
    }

    @Override
    public boolean isNullsLastSorting() {
        return true;
    }
}

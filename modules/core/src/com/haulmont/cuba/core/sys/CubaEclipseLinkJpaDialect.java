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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 */
public class CubaEclipseLinkJpaDialect extends EclipseLinkJpaDialect {

    private static final long serialVersionUID = 7560990917358283944L;

    private static final Logger log = LoggerFactory.getLogger(CubaEclipseLinkJpaDialect.class);

    @Override
    public Object beginTransaction(EntityManager entityManager, TransactionDefinition definition) throws PersistenceException, SQLException, TransactionException {
        Object result = super.beginTransaction(entityManager, definition);

        // Read default timeout every time - may be somebody wants to change it on the fly
        int defaultTimeout = 0;
        String defaultTimeoutProp = AppContext.getProperty("cuba.defaultQueryTimeoutSec");
        if (!"0".equals(defaultTimeoutProp) && !StringUtils.isBlank(defaultTimeoutProp)) {
            try {
                defaultTimeout = Integer.parseInt(defaultTimeoutProp);
            } catch (NumberFormatException e) {
                log.error("Invalid cuba.defaultQueryTimeoutSec value", e);
            }
        }

        int timeoutMs = 0;
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT)
            timeoutMs = definition.getTimeout() * 1000;
        else if (defaultTimeout != 0)
            timeoutMs = defaultTimeout * 1000;

        if (timeoutMs != 0) {
            log.trace("Applying query timeout " + timeoutMs + "ms");
            entityManager.setProperty("javax.persistence.query.timeout", timeoutMs);

            String s = DbmsSpecificFactory.getDbmsFeatures().getTransactionTimeoutStatement();
            if (s != null) {
                Connection connection = entityManager.unwrap(Connection.class);
                try (Statement statement = connection.createStatement()) {
                    statement.execute(String.format(s, timeoutMs));
                }
            }
        }

        return result;
    }
}
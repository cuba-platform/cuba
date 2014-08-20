/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.sys.persistence.DbmsType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaOpenJpaDialect extends OpenJpaDialect {

    private static final long serialVersionUID = 7560990917358283944L;

    private static final Log log = LogFactory.getLog(CubaOpenJpaDialect.class);

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

            if (DbmsType.getCurrent() == DbmsType.POSTGRES) {
                Connection connection = (Connection) ((OpenJPAEntityManager) entityManager).getConnection();
                try (Statement statement = connection.createStatement()) {
                    statement.execute("set local statement_timeout to " + timeoutMs);
                }
            }
        }

        return result;
    }
}
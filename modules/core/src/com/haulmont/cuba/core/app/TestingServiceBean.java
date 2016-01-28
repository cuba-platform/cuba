/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.security.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Service for integration testing. Don't use it in application code!
 *
 * @author krivopustov
 * @version $Id$
 */
@Service(TestingService.NAME)
public class TestingServiceBean implements TestingService {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private Persistence persistence;

    @Override
    public String executeFor(int timeMillis) {
        log.debug("executeFor " + timeMillis  + " started");
        try {
            Thread.sleep(timeMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("executeFor " + timeMillis + " finished");
        return "Done";
    }

    @Override
    @Transactional(timeout = 2)
    public String executeUpdateSql(String sql) {
        checkTestMode();

        log.info("started: " + sql);
        EntityManager em = persistence.getEntityManager();
        Query query = em.createNativeQuery(sql);
        query.executeUpdate();
        log.info("finished: " + sql);
        return "Done";
    }

    @Override
    @Transactional(timeout = 2)
    public String executeSelectSql(String sql) {
        checkTestMode();

        log.info("started: " + sql);
        EntityManager em = persistence.getEntityManager();
        Query query = em.createNativeQuery(sql);
        query.getResultList();
        log.info("finished: " + sql);
        return "Done";
    }

    @Override
    public String execute() {
        log.debug("execute started");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Done: com.haulmont.cuba.core.app.TestingServiceBean.execute";
    }

    @Override
    public boolean primitiveParameters(boolean b, int i, long l, double d) {
        log.debug("primitiveParameters: " + b + ", " + i + ", " + l + ", " + d);
        return b;
    }

    @Override
    public String executeWithException() throws TestException {
        throw new TestException("an error");
    }

    @Override
    public void clearScheduledTasks() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createNativeQuery("delete from SYS_SCHEDULED_EXECUTION");
            query.executeUpdate();

            query = em.createNativeQuery("delete from SYS_SCHEDULED_TASK");
            query.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public Object leaveOpenTransaction() {
        checkTestMode();
        Transaction tx = persistence.createTransaction();
        persistence.getEntityManager().find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
        return tx;
    }

    @Transactional
    @Override
    public void declarativeTransaction() {
        checkTestMode();
        persistence.getEntityManager().find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

        persistence.getEntityManagerContext().setAttribute("test", "test_value");
    }

    private void checkTestMode() {
        if (!Boolean.valueOf(System.getProperty("cuba.unitTestMode")))
            throw new IllegalStateException("Not in test mode");
    }
}
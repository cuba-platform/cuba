/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service for integration testing. Don't use it in application code!
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Service(TestingService.NAME)
public class TestingServiceBean implements TestingService {

    private Log log = LogFactory.getLog(getClass());

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
        if (!Boolean.valueOf(System.getProperty("cuba.unitTestMode")))
            return "Not in test mode";

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
        if (!Boolean.valueOf(System.getProperty("cuba.unitTestMode")))
            return "Not in test mode";

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
}

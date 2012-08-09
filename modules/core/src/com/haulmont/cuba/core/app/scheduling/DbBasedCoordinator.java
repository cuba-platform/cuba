/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.ScheduledTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

/**
 * Implementation of {@link Coordinator} interface, performing synchronization of singleton schedulers on the main
 * database.
 * <p>This implementation should not be used if the database is overloaded.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Coordinator.NAME)
public class DbBasedCoordinator implements Coordinator {

    private static class ContextImpl implements Context {

        private List<ScheduledTask> tasks;
        private Transaction transaction;

        private ContextImpl(List<ScheduledTask> tasks, Transaction transaction) {
            this.tasks = tasks;
            this.transaction = transaction;
        }

        @Override
        public List<ScheduledTask> getTasks() {
            return tasks;
        }

        public Transaction getTransaction() {
            return transaction;
        }
    }

    @Inject
    private Persistence persistence;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public Context begin() {
        Transaction tx = persistence.createTransaction();
        try {
            List<ScheduledTask> tasks = getTasks();
            return new ContextImpl(tasks, tx);
        } catch (Exception e) {
            tx.end();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void end(Context context) {
        log.trace("Commit transaction thereby unlock active tasks");
        ((ContextImpl) context).transaction.commit();
        ((ContextImpl) context).transaction.end();
    }

    @Override
    public boolean isLastExecutionFinished(ScheduledTask task, long now) {
        EntityManager em = persistence.getEntityManager();
        Query query = em.createQuery(
                "select e.finishTime from sys$ScheduledExecution e where e.task.id = ?1 and e.startTime = ?2");
        query.setParameter(1, task.getId());
        query.setParameter(2, task.getLastStartTime());
        List list = query.getResultList();
        if (list.isEmpty()) {
            // Execution was not registered by some reason, so using timeout value or just return false
            boolean result = task.getTimeout() != null && task.getLastStart() + task.getTimeout() <= now;
            log.trace(task + ": finished=" + result + " because of timeout");
            return result;
        }
        Date date = (Date) list.get(0);
        if (date == null) {
            log.trace(task + ": not finished yet");
            return false;
        } else {
            log.trace(task + ": finished at " + date.getTime());
            return true;
        }
    }

    private synchronized List<ScheduledTask> getTasks() {
        log.trace("Read all active tasks from DB and lock them");
        EntityManager em = persistence.getEntityManager();
        Query query = em.createQuery("select t from sys$ScheduledTask t where t.active = true");
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        return query.getResultList();
    }
}

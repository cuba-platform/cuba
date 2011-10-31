/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.entity.ScheduledExecution;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Standard implementation of {@link Runner} interface used by {@link Scheduling} to run scheduled tasks.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Runner.NAME)
public class RunnerBean implements Runner {

    private static final int MAX_THREADS = 10;

    private Log log = LogFactory.getLog(getClass());

    private ExecutorService executorService;

    @Inject
    private SchedulingAPI scheduling;

    @Inject
    private ServerInfoAPI serverInfo;

    @Inject
    private ClusterManagerAPI clusterManager;

    @Inject
    private Persistence persistence;

    @Inject
    private TimeSource timeSource;

    @Inject
    private LoginWorker loginWorker;

    @Inject
    private UserSessionManager userSessionManager;

    private Map<String, UUID> userSessionIds = new ConcurrentHashMap<String, UUID>();

    public RunnerBean() {
        executorService = Executors.newFixedThreadPool(MAX_THREADS, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "ScheduledRunnerThread");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void runTask(ScheduledTask task, final long now, final UserSession userSession) {
        // It's better not to pass an entity instance in managed state to another thread
        final ScheduledTask taskCopy = (ScheduledTask) InstanceUtils.copy(task);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                log.debug(taskCopy + ": running");
                try {
                    setSecurityContext(taskCopy, userSession);

                    ScheduledExecution execution = registerExecutionStart(taskCopy, now);
                    scheduling.setRunning(taskCopy, true);
                    try {
                        Object result = invokeBean(taskCopy);
                        registerExecutionFinish(taskCopy, execution, result);
                    } catch (Throwable throwable) {
                        registerExecutionFinish(taskCopy, execution, throwable);
                        throw throwable;
                    } finally {
                        scheduling.setRunning(taskCopy, false);
                    }
                } catch (Throwable throwable) {
                    log.error("Error running " + taskCopy, throwable);
                }
            }
        });
    }

    private void setSecurityContext(ScheduledTask task, UserSession userSession) throws LoginException {
        if (userSession == null) {
            UUID sessionId = userSessionIds.get(task.getUserName());
            userSession = sessionId == null ? null : userSessionManager.findSession(sessionId);
            if (userSession == null) {
                userSession = loginWorker.loginSystem(task.getUserName(), task.getUserPassword());
                userSessionIds.put(task.getUserName(), userSession.getId());
            }

        }
        AppContext.setSecurityContext(new SecurityContext(userSession));
    }

    private ScheduledExecution registerExecutionStart(ScheduledTask task, long now) {
        if (!BooleanUtils.isTrue(task.getLogStart()) && !BooleanUtils.isTrue(task.getSingleton()))
            return null;

        log.trace(task + ": registering execution start");

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            ScheduledExecution execution = new ScheduledExecution();
            execution.setTask(em.getReference(ScheduledTask.class, task.getId()));
            execution.setStartTime(new Date(now));
            execution.setServer(serverInfo.getServerId());

            em.persist(execution);
            tx.commit();

            return execution;
        } finally {
            tx.end();
        }
    }

    private void registerExecutionFinish(ScheduledTask task, ScheduledExecution execution, Object result) {
        if ((!BooleanUtils.isTrue(task.getLogStart()) && !BooleanUtils.isTrue(task.getSingleton())) || execution == null)
            return;

        log.trace(task + ": registering execution finish");
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            execution = em.merge(execution);
            execution.setFinishTime(timeSource.currentTimestamp());
            if (result != null)
                execution.setResult(result.toString());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private Object invokeBean(ScheduledTask task) {
        log.trace(task + ": invoking bean");
        Object bean = AppContext.getBean(task.getBeanName());
        try {
            Method method = bean.getClass().getMethod(task.getMethodName(), new Class[0]);
            return method.invoke(bean);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

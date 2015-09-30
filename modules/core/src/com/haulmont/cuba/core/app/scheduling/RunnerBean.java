/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
import com.haulmont.cuba.core.app.scheduled.MethodParameterInfo;
import com.haulmont.cuba.core.entity.ScheduledExecution;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

/**
 * Standard implementation of {@link Runner} interface used by {@link Scheduling} to run scheduled tasks.
 *
 * @author krivopustov
 * @version $Id$
 */
@Component(Runner.NAME)
public class RunnerBean implements Runner {

    protected static final int MAX_THREADS = 10;

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected ExecutorService executorService;

    @Inject
    protected SchedulingAPI scheduling;

    @Inject
    protected ServerInfoAPI serverInfo;

    @Inject
    protected Persistence persistence;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected LoginWorker loginWorker;

    @Inject
    protected UserSessionManager userSessionManager;

    @Inject
    protected Scripting scripting;

    @Inject
    protected MiddlewareStatisticsAccumulator statisticsCounter;

    protected Map<String, UUID> userSessionIds = new ConcurrentHashMap<>();

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
                    statisticsCounter.incCubaScheduledTasksCount();
                    try {
                        Object result = executeTask(taskCopy);
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

    protected void setSecurityContext(ScheduledTask task, UserSession userSession) throws LoginException {
        if (userSession == null) {
            UUID sessionId = userSessionIds.get(task.getUserName());
            userSession = sessionId == null ? null : userSessionManager.findSession(sessionId);
            if (userSession == null) {
                userSession = loginWorker.loginSystem(task.getUserName());
                userSessionIds.put(task.getUserName(), userSession.getId());
            }
        }
        AppContext.setSecurityContext(new SecurityContext(userSession));
    }

    protected ScheduledExecution registerExecutionStart(ScheduledTask task, long now) {
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

    protected void registerExecutionFinish(ScheduledTask task, ScheduledExecution execution, Object result) {
        if ((!BooleanUtils.isTrue(task.getLogFinish()) && !BooleanUtils.isTrue(task.getSingleton())) || execution == null)
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

    protected Object executeTask(ScheduledTask task) {
        switch (task.getDefinedBy()) {
            case BEAN: {
                log.trace(task + ": invoking bean");
                Object bean = AppBeans.get(task.getBeanName());
                try {
                    List<MethodParameterInfo> methodParams = task.getMethodParameters();
                    Class[] paramTypes = new Class[methodParams.size()];
                    Object[] paramValues = new Object[methodParams.size()];

                    for (int i = 0; i < methodParams.size(); i++) {
                        paramTypes[i] = methodParams.get(i).getType();
                        paramValues[i] = methodParams.get(i).getValue();
                    }

                    Method method = bean.getClass().getMethod(task.getMethodName(), paramTypes);
                    return method.invoke(bean, paramValues);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            case CLASS: {
                try {
                    Class taskClass = scripting.loadClass(task.getClassName());
                    Callable callable = (Callable) taskClass.newInstance();
                    return callable.call();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(String.format("An error occurred while instantiating class %s.", task.getClassName()), e);
                } catch (Exception e) {
                    throw new RuntimeException(
                            String.format("An error occurred while running method call() of class %s.",
                                    task.getClassName()), e);
                }
            }

            case SCRIPT: {
                return scripting.runGroovyScript(task.getScriptName(), Collections.<String, Object>emptyMap());
            }

            default: {
                throw new IllegalStateException(
                        String.format("\"Defined by\" field has illegal value: %s. Task id: [%s].",
                                task.getDefinedBy(), task.getId()));
            }
        }
    }
}

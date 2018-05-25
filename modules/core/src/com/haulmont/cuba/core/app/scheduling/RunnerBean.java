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

package com.haulmont.cuba.core.app.scheduling;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.app.scheduled.MethodParameterInfo;
import com.haulmont.cuba.core.entity.ScheduledExecution;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.SchedulingType;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.auth.SystemUserCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Standard implementation of {@link Runner} interface used by {@link Scheduling} to run scheduled tasks.
 */
@Component(Runner.NAME)
public class RunnerBean implements Runner {

    private static final Logger log = LoggerFactory.getLogger(RunnerBean.class);

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
    protected AuthenticationManager authenticationManager;

    @Inject
    protected UserSessionsAPI userSessions;

    @Inject
    protected Scripting scripting;

    @Inject
    protected MiddlewareStatisticsAccumulator statisticsCounter;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Configuration configuration;

    protected Map<String, UUID> userSessionIds = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        int nThreads = configuration.getConfig(ServerConfig.class).getSchedulingThreadPoolSize();
        executorService = Executors.newFixedThreadPool(nThreads, new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(@Nonnull Runnable r) {
                Thread thread = new Thread(r, "ScheduledRunnerThread-" + threadNumber.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void runTask(ScheduledTask task, final long now, final @Nullable UserSession userSession) {
        runTask(task, now, false, userSession);
    }

    @Override
    public void runTaskOnce(ScheduledTask task, long now, @Nullable UserSession userSession) {
        runTask(task, now, true, userSession);
    }

    protected void runTask(ScheduledTask task, final long now, final boolean manually,
                           final @Nullable UserSession userSession) {
        // It's better not to pass an entity instance in managed state to another thread
        final ScheduledTask taskCopy = metadata.getTools().copy(task);

        executorService.submit(() -> {
            log.debug("{}: running", taskCopy);
            try {
                boolean runConcurrent = scheduling.setRunning(taskCopy, true);
                if (!runConcurrent) {
                    try {
                        setSecurityContext(taskCopy, userSession);
                        ScheduledExecution execution = registerExecutionStart(taskCopy, now);
                        statisticsCounter.incCubaScheduledTasksCount();
                        try {
                            Object result = executeTask(taskCopy);
                            registerExecutionFinish(taskCopy, execution, result, manually);
                        } catch (Throwable throwable) {
                            registerExecutionFinish(taskCopy, execution, throwable, manually);
                            throw throwable;
                        }
                    } finally {
                        scheduling.setRunning(taskCopy, false);
                        scheduling.setFinished(task);
                    }
                } else {
                    log.info("Detected concurrent task execution: {}, skip it", taskCopy);
                }
            } catch (Throwable throwable) {
                log.error("Error running {}", taskCopy, throwable);
            }
        });
    }

    protected void setSecurityContext(ScheduledTask task, @Nullable UserSession userSession) throws LoginException {
        if (userSession == null) {
            UUID sessionId = userSessionIds.get(task.getUserName());
            userSession = sessionId == null ? null : userSessions.getAndRefresh(sessionId);
            if (userSession == null) {
                userSession = authenticationManager.login(new SystemUserCredentials(task.getUserName())).getSession();
                userSessionIds.put(task.getUserName(), userSession.getId());
            }
        }
        AppContext.setSecurityContext(new SecurityContext(userSession));
    }

    protected ScheduledExecution registerExecutionStart(ScheduledTask task, long now) {
        if (!BooleanUtils.isTrue(task.getLogStart()) && !BooleanUtils.isTrue(task.getSingleton()) && task.getSchedulingType() != SchedulingType.FIXED_DELAY)
            return null;

        log.trace("{}: registering execution start", task);

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            ScheduledExecution execution = metadata.create(ScheduledExecution.class);
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

    protected void registerExecutionFinish(ScheduledTask task, ScheduledExecution execution, Object result, boolean manually) {
        if ((!BooleanUtils.isTrue(task.getLogFinish()) && !BooleanUtils.isTrue(task.getSingleton()) && task.getSchedulingType() != SchedulingType.FIXED_DELAY)
                || execution == null)
            return;

        log.trace("{}: registering execution finish", task);
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            execution = em.merge(execution);
            execution.setFinishTime(timeSource.currentTimestamp());

            StringBuilder sb = new StringBuilder();
            if (result != null) {
                sb.append(result.toString());
            }

            if (manually) {
                sb.append("\n{Executed manually}");
            }
            execution.setResult(sb.toString());

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected Object executeTask(ScheduledTask task) {
        switch (task.getDefinedBy()) {
            case BEAN: {
                log.trace("{}: invoking bean", task);
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

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

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Logging;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.global.validation.MethodParametersValidationException;
import com.haulmont.cuba.core.global.validation.MethodResultValidationException;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Intercepts invocations of the middleware services.
 * <br> Checks {@link UserSession} validity and wraps exceptions into {@link RemoteException}.
 */
public class ServiceInterceptor implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ServiceInterceptor.class);

    private UserSessionsAPI userSessions;

    private Persistence persistence;

    private MiddlewareStatisticsAccumulator statisticsAccumulator;

    boolean logInternalServiceInvocation;

    public void setUserSessions(UserSessionsAPI userSessions) {
        this.userSessions = userSessions;
    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }

    public void setStatisticsAccumulator(MiddlewareStatisticsAccumulator statisticsAccumulator) {
        this.statisticsAccumulator = statisticsAccumulator;
    }

    public void setConfiguration(Configuration configuration) {
        logInternalServiceInvocation = configuration.getConfig(ServerConfig.class).getLogInternalServiceInvocation();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        SecurityContext securityContext = AppContext.getSecurityContextNN();
        boolean internalInvocation = securityContext.incServiceInvocation() > 0;
        try {
            if (internalInvocation) {
                if (logInternalServiceInvocation) {
                    log.warn("Invoking '{}' from another service", longString(invocation.getMethod()));
                }

                Object res = invocation.proceed();

                return res;
            } else {
                boolean checkTransactionOnExit = Stores.getAdditional().isEmpty() && !persistence.isInTransaction();
                statisticsAccumulator.incMiddlewareRequestsCount();
                try {
                    // Using UserSessionsAPI directly to make sure the session's "last used" timestamp is propagated to the cluster
                    UserSession userSession = userSessions.getAndRefresh(securityContext.getSessionId(), true);
                    if (userSession == null) {
                        throw new NoUserSessionException(securityContext.getSessionId());
                    }

                    log.trace("Invoking: {}, session={}", longString(invocation.getMethod()), userSession);

                    Object res = invocation.proceed();

                    return res;
                } catch (Throwable e) {
                    logException(e, invocation);
                    // Propagate the special exception to avoid serialization errors on remote clients
                    throw new RemoteException(e);
                } finally {
                    if (checkTransactionOnExit && persistence.isInTransaction()) {
                        log.warn("Open transaction left in {}", shortString(invocation.getMethod()));
                    }
                }
            }
        } finally {
            securityContext.decServiceInvocation();
        }
    }


    protected void logException(Throwable e, MethodInvocation invocation) {
        if (e instanceof NoUserSessionException) {
            // If you don't want NoUserSessionException in log, set level higher than INFO for ServiceInterceptor logger
            log.info("Exception in {}: {}", shortString(invocation.getMethod()), e.toString());
        } else if (e instanceof MethodParametersValidationException) {
            log.info("MethodParametersValidationException in {}: {}, violations:\n{}", shortString(invocation.getMethod()), e.toString(),
                    ((MethodParametersValidationException) e).getConstraintViolations());
        } else if (e instanceof MethodResultValidationException) {
            log.error("MethodResultValidationException in {}: {}, violations:\n{}", shortString(invocation.getMethod()), e.toString(),
                    ((MethodResultValidationException) e).getConstraintViolations());
        } else {
            Logging annotation = e.getClass().getAnnotation(Logging.class);
            if (annotation == null || annotation.value() == Logging.Type.FULL) {
                log.error("Exception: ", e);
            } else if (annotation.value() == Logging.Type.BRIEF) {
                log.error("Exception in {}: {}", shortString(invocation.getMethod()), e.toString());
            }
        }
    }

    protected static String longString(Method method) {
        return methodString(method, true);
    }

    protected static String shortString(Method method) {
        return methodString(method, false);
    }

    protected static String methodString(Method method, boolean longForm) {
        StringBuilder sb = new StringBuilder();
        if (longForm) {
            appendType(sb, method.getReturnType(), false);
            sb.append(" ");
        }
        appendType(sb, method.getDeclaringClass(), longForm);
        sb.append(".");
        sb.append(method.getName());
        sb.append("(");
        Class<?>[] parametersTypes = method.getParameterTypes();
        appendTypes(sb, parametersTypes, longForm);
        sb.append(")");
        return sb.toString();
    }

    private static void appendTypes(StringBuilder sb, Class<?>[] types, boolean includeArgs) {
        if (includeArgs) {
            for (int size = types.length, i = 0; i < size; i++) {
                appendType(sb, types[i], false);
                if (i < size - 1) {
                    sb.append(",");
                }
            }
        } else if (types.length != 0) {
            sb.append("..");
        }
    }

    private static void appendType(StringBuilder sb, Class<?> type, boolean useLongTypeName) {
        if (type.isArray()) {
            appendType(sb, type.getComponentType(), useLongTypeName);
            sb.append("[]");
        } else {
            sb.append(useLongTypeName ? type.getName() : type.getSimpleName());
        }
    }
}
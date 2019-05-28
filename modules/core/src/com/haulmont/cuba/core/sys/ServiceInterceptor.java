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
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts invocations of the middleware services.
 * <br> Checks {@link UserSession} validity and wraps exceptions into {@link RemoteException}.
 */
public class ServiceInterceptor {
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

    private Object aroundInvoke(ProceedingJoinPoint ctx) throws Throwable {
        SecurityContext securityContext = AppContext.getSecurityContextNN();
        boolean internalInvocation = securityContext.incServiceInvocation() > 0;
        try {
            if (internalInvocation) {
                if (logInternalServiceInvocation) {
                    log.warn("Invoking '{}' from another service", ctx.getSignature());
                }

                Object res = ctx.proceed();

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

                    log.trace("Invoking: {}, session={}", ctx.getSignature(), userSession);

                    Object res = ctx.proceed();

                    return res;
                } catch (Throwable e) {
                    logException(e, ctx);
                    // Propagate the special exception to avoid serialization errors on remote clients
                    throw new RemoteException(e);
                } finally {
                    if (checkTransactionOnExit && persistence.isInTransaction()) {
                        log.warn("Open transaction left in {}", ctx.getSignature().toShortString());
                    }
                }
            }
        } finally {
            securityContext.decServiceInvocation();
        }
    }


    protected void logException(Throwable e, ProceedingJoinPoint ctx) {
        if (e instanceof NoUserSessionException) {
            // If you don't want NoUserSessionException in log, set level higher than INFO for ServiceInterceptor logger
            log.info("Exception in {}: {}", ctx.getSignature().toShortString(), e.toString());
        } else if (e instanceof MethodParametersValidationException) {
            log.info("MethodParametersValidationException in {}: {}, violations:\n{}", ctx.getSignature().toShortString(), e.toString(),
                    ((MethodParametersValidationException) e).getConstraintViolations());
        } else if (e instanceof MethodResultValidationException) {
            log.error("MethodResultValidationException in {}: {}, violations:\n{}", ctx.getSignature().toShortString(), e.toString(),
                    ((MethodResultValidationException) e).getConstraintViolations());
        } else {
            Logging annotation = e.getClass().getAnnotation(Logging.class);
            if (annotation == null || annotation.value() == Logging.Type.FULL) {
                log.error("Exception: ", e);
            } else if (annotation.value() == Logging.Type.BRIEF) {
                log.error("Exception in {}: {}", ctx.getSignature().toShortString(), e.toString());
            }
        }
    }
}
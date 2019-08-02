/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.core.app.execution;


import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(Executions.NAME)
public class ExecutionsImpl implements Executions {

    protected static final String EXECUTIONS_ATTR = ExecutionsImpl.class.getName() + ".executions";
    private static final Logger log = LoggerFactory.getLogger(ExecutionsImpl.class);

    @Inject
    protected TimeSource timeSource;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UserSessionsAPI userSessionsAPI;
    @Inject
    protected ClusterManagerAPI clusterManager;

    @PostConstruct
    public void init() {
        clusterManager.addListener(CancelExecutionMessage.class, new CancelExecutionClusterListener());
    }

    protected static class CancelExecutionMessage implements Serializable {
        private static final long serialVersionUID = -615644330444376219L;

        protected UUID userSessionId;
        protected String group;
        protected String key;

        public CancelExecutionMessage(UUID userSessionId, String group, String key) {
            this.userSessionId = userSessionId;
            this.group = group;
            this.key = key;
        }
    }

    public ExecutionContext startExecution(String key, String group) {
        if (ExecutionContextHolder.getCurrentContext() != null) {
            throw new IllegalStateException("Execution context already started");
        }
        log.debug("Start execution context: group={}, key={}", group, key);
        ExecutionContextImpl context = new ExecutionContextImpl(Thread.currentThread(), key, group, timeSource.currentTimestamp());

        UserSession userSession = userSessionSource.getUserSession();
        CopyOnWriteArrayList<ExecutionContextImpl> executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        if (executions == null) {
            userSession.setLocalAttributeIfAbsent(EXECUTIONS_ATTR, new CopyOnWriteArrayList<>());
            executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        }
        executions.add(context);

        ExecutionContextHolder.setCurrentContext(context);

        return context;
    }

    public void endExecution() {
        try {
            Thread thread = Thread.currentThread();
            if (thread.isInterrupted()) {
                Thread.interrupted();
            }
            ExecutionContextImpl context = (ExecutionContextImpl) ExecutionContextHolder.getCurrentContext();
            if (context == null) {
                throw new IllegalStateException("No execution context found");
            }
            log.debug("End execution context: group={}, key={}", context.getGroup(), context.getKey());

            if (userSessionSource.checkCurrentUserSession()) {
                UserSession userSession = userSessionSource.getUserSession();
                removeExecutionContextFromUserSession(userSession, context);
            } else {
                log.debug("No active user session: group={}, key={}", context.getGroup(), context.getKey());
            }
            context.setState(ExecutionContextImpl.State.COMPLETED);
        } finally {
            ExecutionContextHolder.removeContext();
        }
    }

    public void cancelExecution(UUID userSessionId, String group, String key) {
        UserSession userSession = userSessionsAPI.getAndRefresh(userSessionId, false);
        if (userSession == null) {
            log.warn("User session {} not found, execution context: group={}, key={}", userSessionId, group, key);
            throw new IllegalStateException(String.format("User session {%s} not found", userSessionId));
        }
        log.debug("Try to cancel resources for execution context: group={}, key={} and user session {}", group, key, userSessionId);
        cancelLocally(userSession, group, key);
        if (clusterManager.isStarted()) {
            log.debug("Send cancel message in cluster for execution context: group={}, key={}", group, key);
            clusterManager.send(new CancelExecutionMessage(userSessionId, group, key));
        }
    }

    @Override
    public ExecutionContext getCurrentContext() {
        return ExecutionContextHolder.getCurrentContext();
    }

    protected void cancelLocally(UserSession userSession, String group, String key) {
        List<ExecutionContext> result = findExecutionContextsByKeyAndGroup(userSession, group, key);
        for (ExecutionContext context : result) {
            if (context.getState() == ExecutionContext.State.ACTIVE) {
                ExecutionContextImpl contextImpl = (ExecutionContextImpl) context;
                contextImpl.setState(ExecutionContextImpl.State.CANCELED);
                //CAUTION: let all the code paths to check isInterrupted on Thread.currentThread()
                //CAUTION: cancel resources only after thread interruption
                if (contextImpl.getThread() != null) {
                    contextImpl.getThread().interrupt();
                }
                for (CancelableResource resource : contextImpl.getResources()) {
                    try {
                        resource.cancel();
                    } catch (Exception e) {
                        log.warn("Cancel resource error. Execution context: group={}, key={} and user session {}", group, key, userSession.getId());
                    }
                }
                contextImpl.clearResources();
                removeExecutionContextFromUserSession(userSession, context);
            }
        }
    }

    protected List<ExecutionContext> findExecutionContextsByKeyAndGroup(UserSession userSession, String group, String key) {
        List<ExecutionContext> executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        List<ExecutionContext> filtered = new ArrayList<>();
        if (executions != null) {
            executions = new ArrayList<>(executions);
            for (ExecutionContext context : executions) {
                if (Objects.equals(context.getGroup(), group) &&
                        Objects.equals(context.getKey(), key)) {
                    filtered.add(context);
                }
            }
        }
        return filtered;
    }

    protected void removeExecutionContextFromUserSession(UserSession userSession, ExecutionContext context) {
        List<ExecutionContext> executions = userSession.getLocalAttribute(EXECUTIONS_ATTR);
        if (executions != null) {
            executions.remove(context);
        }
    }

    protected class CancelExecutionClusterListener implements ClusterListener<CancelExecutionMessage> {
        @Override
        public void receive(CancelExecutionMessage message) {
            UserSession userSession = userSessionsAPI.getAndRefresh(message.userSessionId, false);
            if (userSession == null) {
                log.warn("User session {} not found, execution context: group={}, key={}", message.userSessionId,
                        message.group, message.key);
            }
            log.debug("Try to cancel resources for execution context: group={}, key={} and user session {}", message.group,
                    message.key, message.userSessionId);
            cancelLocally(userSession, message.group, message.key);
        }

        @Override
        public byte[] getState() {
            return new byte[0];
        }

        @Override
        public void setState(byte[] state) {
        }
    }
}

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

package com.haulmont.cuba.security.app;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.SessionLogEntry;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.SessionParams;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component(SessionHistoryAPI.NAME)
public class SessionHistoryBean implements SessionHistoryAPI {

    @Inject
    protected ServerConfig serverConfig;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Metadata metadata;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected ServerInfoAPI serverInfoAPI;
    @Inject
    protected ClusterManagerAPI clusterManager;
    @Inject
    protected UserSessionsAPI userSessionsAPI;
    @Inject
    protected LoginWorker loginWorker;

    private Logger log = LoggerFactory.getLogger(SessionHistoryBean.class);

    protected AtomicReference<SecurityContext> systemContext = new AtomicReference<>();

    @Override
    @Nullable
    public SessionLogEntry createSessionLogRecord(UserSession userSession, SessionAction action, Map<String, Object> params) {
        return createSessionLogRecord(userSession, action, null, params);
    }

    @Override
    public SessionLogEntry createSessionLogRecord(UserSession userSession, SessionAction action,
                                                  @Nullable UserSession substitutedSession,
                                                  @Nullable Map<String, Object> params) {
        Preconditions.checkNotNullArgument(userSession);
        Preconditions.checkNotNullArgument(action);

        if (!mayLogSession(userSession)) {
            return null;
        }

        return AppContext.withSecurityContext(getSystemContext(), () -> {
            SessionLogEntry sessionLogEntry = metadata.create(SessionLogEntry.class);
            sessionLogEntry.setSessionId(userSession.getId());
            if (substitutedSession != null) {
                sessionLogEntry.setUser(userSession.getSubstitutedUser());
                sessionLogEntry.setSubstitutedUser(userSession.getUser());
            } else {
                sessionLogEntry.setUser(userSession.getUser());
            }
            sessionLogEntry.setLastAction(action);
            sessionLogEntry.setAddress(userSession.getAddress());
            sessionLogEntry.setClientInfo(userSession.getClientInfo());
            sessionLogEntry.setStartedWhen(timeSource.currentTimestamp());
            sessionLogEntry.setServer(serverInfoAPI.getServerId());

            if (params != null) {
                if (params.containsKey(ClientType.class.getName())) {
                    sessionLogEntry.setClientType(ClientType.valueOf((String) params.get(ClientType.class.getName())));
                }
                if (params.containsKey(SessionParams.CLIENT_TYPE.getId())) {
                    sessionLogEntry.setClientType(ClientType.valueOf((String) params.get(SessionParams.CLIENT_TYPE.getId())));
                }
                sessionLogEntry.setClientInfo(userSession.getClientInfo());
                sessionLogEntry.setAddress(userSession.getAddress());
            }

            return dataManager.commit(sessionLogEntry, "sessionLogEntry-view");
        });
    }

    @Override
    public void updateSessionLogRecord(UserSession userSession, @Nullable SessionAction action) {
        Preconditions.checkNotNullArgument(userSession);
        if (!mayLogSession(userSession)) {
            return;
        }

        AppContext.withSecurityContext(getSystemContext(), () -> {
            SessionLogEntry sessionLogEntry = getLastSessionLogRecord(userSession.getId());
            if (userSession.getClientInfo() != null) {
                sessionLogEntry.setClientInfo(userSession.getClientInfo());
            }
            if (userSession.getAddress() != null) {
                sessionLogEntry.setAddress(userSession.getAddress());
            }
            if (action != null) {
                sessionLogEntry.setLastAction(action);
                if (action != SessionAction.LOGIN) {
                    sessionLogEntry.setFinishedWhen(timeSource.currentTimestamp());
                }
            }

            dataManager.commit(sessionLogEntry);
            return null;
        });

    }

    @Override
    public SessionLogEntry getLastSessionLogRecord(UUID userSessionId) {
        return AppContext.withSecurityContext(getSystemContext(), () -> {
            LoadContext<SessionLogEntry> loadContext = LoadContext.create(SessionLogEntry.class).setView(SessionLogEntry.DEFAULT_VIEW)
                    .setQuery(LoadContext.createQuery("select e from sec$SessionLogEntry e where e.sessionId = :sid order by e.startedWhen desc")
                            .setParameter("sid", userSessionId).setMaxResults(1));
            return dataManager.load(loadContext);
        });
    }

    @Override
    public List<SessionLogEntry> getAllSessionLogRecords(UUID userSessionId) {
        return AppContext.withSecurityContext(getSystemContext(), () -> {
            LoadContext<SessionLogEntry> loadContext = LoadContext.create(SessionLogEntry.class).setView(SessionLogEntry.DEFAULT_VIEW)
                    .setQuery(LoadContext.createQuery("select e from sec$SessionLogEntry e where e.sessionId = :sid order by e.startedWhen asc")
                            .setParameter("sid", userSessionId));
            return dataManager.loadList(loadContext);
        });
    }

    @Override
    public void closeDeadSessionsOnStartup() {
        if (!serverConfig.getSessionHistoryEnabled()) {
            return;
        }
        if (clusterManager.isMaster()) {
            AppContext.withSecurityContext(getSystemContext(), () -> {
                LoadContext<SessionLogEntry> lc = LoadContext.create(SessionLogEntry.class).setView(SessionLogEntry.DEFAULT_VIEW)
                        .setQuery(LoadContext.createQuery("select e from sec$SessionLogEntry e where e.finishedWhen is null"));
                List<SessionLogEntry> sessionLogEntries = dataManager.loadList(lc);
                CommitContext cc = new CommitContext();
                Set<UUID> activeSessionsIds = userSessionsAPI.getUserSessionInfo().stream()
                        .map(AbstractNotPersistentEntity::getId)
                        .collect(Collectors.toSet());
                for (SessionLogEntry entry : sessionLogEntries) {
                    if (activeSessionsIds.contains(entry.getSessionId())) {
                        continue;   // do not touch active session records
                    }
                    entry.setFinishedWhen(timeSource.currentTimestamp());
                    entry.setLastAction(SessionAction.EXPIRATION);
                    cc.addInstanceToCommit(entry);
                }
                dataManager.commit(cc);
                log.info("Dead session records have been closed");
            });
        }
    }

    @PostConstruct
    protected void initSessionHistoryCleanupListener() {
        AppContext.addListener(new AppContext.Listener() {
            @Override
            public void applicationStarted() {
                if (!serverConfig.getSessionHistoryEnabled()) {
                    return;
                }
                if (!Boolean.valueOf(AppContext.getProperty("cuba.cluster.enabled"))) {
                    closeDeadSessionsOnStartup();
                    return;
                }
                CompletableFuture.runAsync(() -> {
                    try {
                        // wait for master election for 90 seconds max
                        int triesNum = 9;
                        while (!clusterManager.isMaster() && triesNum > 0) {
                            Thread.sleep(10000);
                            triesNum--;
                        }
                    } catch (InterruptedException e) {
                        log.error("Interrupted while waiting");
                        return;
                    }
                    if (clusterManager.isMaster())
                        closeDeadSessionsOnStartup();
                    else
                        log.debug("Node is not master, no session records cleanup performed");
                });
            }

            @Override
            public void applicationStopped() {
                // do nothing
            }
        });
    }

    protected SecurityContext getSystemContext() {
        SecurityContext systemContext;
        if (this.systemContext.get() == null) {
            try {
                String systemLogin = getSystemLogin();
                UserSession systemSession = loginWorker.loginSystem(systemLogin);
                this.systemContext.compareAndSet(null, new SecurityContext(systemSession.getId()));
            } catch (LoginException e) {
                throw new RuntimeException("Could not login as system user, check the \"cuba.jmxUserLogin\" property", e);
            }
        }
        systemContext = this.systemContext.get();
        return systemContext;
    }

    protected String getSystemLogin() {
        return AppContext.getProperty("cuba.jmxUserLogin");
    }

    protected boolean mayLogSession(UserSession userSession) {
        return serverConfig.getSessionHistoryEnabled() && !userSession.isSystem();
    }
}

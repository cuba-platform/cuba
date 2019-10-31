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
import com.haulmont.cuba.core.app.ServerInfoAPI;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.SessionAction;
import com.haulmont.cuba.security.entity.SessionLogEntry;
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
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.global.LoadContext.createQuery;

@Component(UserSessionLog.NAME)
public class UserSessionLogBean implements UserSessionLog {

    @Inject
    protected GlobalConfig globalConfig;
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
    protected Authentication authentication;

    private static final Logger log = LoggerFactory.getLogger(UserSessionLogBean.class);

    @Override
    @Nullable
    public SessionLogEntry createSessionLogRecord(UserSession userSession, SessionAction action, Map<String, Object> params) {
        return createSessionLogRecord(userSession, action, null, params);
    }

    @Override
    @Nullable
    public SessionLogEntry createSessionLogRecord(UserSession userSession, SessionAction action,
                                                  @Nullable UserSession substitutedSession,
                                                  @Nullable Map<String, Object> params) {
        Preconditions.checkNotNullArgument(userSession);
        Preconditions.checkNotNullArgument(action);

        if (!shouldLogSession(userSession)) {
            return null;
        }

        return authentication.withSystemUser(() -> {
            SessionLogEntry sessionLogEntry = metadata.create(SessionLogEntry.class);
            sessionLogEntry.setSessionId(userSession.getId());
            if (substitutedSession != null) {
                sessionLogEntry.setUser(userSession.getUser());
                sessionLogEntry.setSubstitutedUser(userSession.getSubstitutedUser());
            } else {
                sessionLogEntry.setUser(userSession.getUser());
            }
            sessionLogEntry.setLastAction(action);
            sessionLogEntry.setAddress(userSession.getAddress());
            sessionLogEntry.setClientInfo(userSession.getClientInfo());
            sessionLogEntry.setStartedTs(timeSource.currentTimestamp());
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
    @Nullable
    public SessionLogEntry updateSessionLogRecord(UserSession userSession, @Nullable SessionAction action) {
        Preconditions.checkNotNullArgument(userSession);
        if (!shouldLogSession(userSession)) {
            return null;
        }

        return authentication.withSystemUser(() -> {
            SessionLogEntry sessionLogEntry = getLastSessionLogRecord(userSession.getId());
            if (sessionLogEntry != null) {
                if (userSession.getClientInfo() != null) {
                    sessionLogEntry.setClientInfo(userSession.getClientInfo());
                }
                if (userSession.getAddress() != null) {
                    sessionLogEntry.setAddress(userSession.getAddress());
                }
                if (action != null) {
                    sessionLogEntry.setLastAction(action);
                    if (action != SessionAction.LOGIN) {
                        sessionLogEntry.setFinishedTs(timeSource.currentTimestamp());
                    }
                }
                return dataManager.commit(sessionLogEntry);
            }
            return null;
        });
    }

    @Override
    public SessionLogEntry getLastSessionLogRecord(UUID userSessionId) {
        return authentication.withSystemUser(() -> {
            LoadContext<SessionLogEntry> loadContext = LoadContext.create(SessionLogEntry.class)
                    .setView(SessionLogEntry.DEFAULT_VIEW)
                    .setQuery(
                            createQuery("select e from sec$SessionLogEntry e where e.sessionId = :sid order by e.startedTs desc")
                                    .setParameter("sid", userSessionId)
                                    .setMaxResults(1)
                    );
            return dataManager.load(loadContext);
        });
    }

    @Override
    public List<SessionLogEntry> getAllSessionLogRecords(UUID userSessionId) {
        return authentication.withSystemUser(() -> {
            LoadContext<SessionLogEntry> loadContext = LoadContext.create(SessionLogEntry.class)
                    .setView(SessionLogEntry.DEFAULT_VIEW)
                    .setQuery(
                            createQuery("select e from sec$SessionLogEntry e where e.sessionId = :sid order by e.startedTs asc")
                                    .setParameter("sid", userSessionId)
                    );
            return dataManager.loadList(loadContext);
        });
    }

    /**
     * Set <code>finishedTs</code> to all sessions that were interrupted by server reboot
     */
    protected void closeDeadSessionsOnStartup() {
        if (!globalConfig.getUserSessionLogEnabled()) {
            return;
        }
        if (clusterManager.isMaster()) {
            authentication.withSystemUser(() -> {
                LoadContext<SessionLogEntry> lc = LoadContext.create(SessionLogEntry.class).setView(SessionLogEntry.DEFAULT_VIEW)
                        .setQuery(createQuery("select e from sec$SessionLogEntry e where e.finishedTs is null"));
                List<SessionLogEntry> sessionLogEntries = dataManager.loadList(lc);
                CommitContext cc = new CommitContext();
                Set<UUID> activeSessionsIds = userSessionsAPI.getUserSessionsStream()
                        .map(UserSession::getId)
                        .collect(Collectors.toSet());
                for (SessionLogEntry entry : sessionLogEntries) {
                    if (activeSessionsIds.contains(entry.getSessionId())) {
                        continue;   // do not touch active session records
                    }
                    entry.setFinishedTs(timeSource.currentTimestamp());
                    entry.setLastAction(SessionAction.EXPIRATION);
                    cc.addInstanceToCommit(entry);
                }
                try {
                    dataManager.commit(cc);
                    log.info("Dead session records have been closed");
                } catch (Exception e) {
                    log.warn("Failed to close dead session records. Perhaps several CUBA applications use the same database and don't work in a cluster. Exception:\n", e);
                }
                return null;
            });
        }
    }

    @PostConstruct
    protected void initSessionHistoryCleanupListener() {
        AppContext.addListener(new AppContext.Listener() {
            @Override
            public void applicationStarted() {
                closeDeadSessionsOnStartup();
            }

            @Override
            public void applicationStopped() {
                // do nothing
            }
        });
    }

    protected boolean shouldLogSession(UserSession userSession) {
        return globalConfig.getUserSessionLogEnabled() && !userSession.isSystem();
    }
}

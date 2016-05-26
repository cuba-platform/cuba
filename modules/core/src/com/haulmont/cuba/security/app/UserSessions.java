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
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User sessions distributed cache.
 */
@Component(UserSessionsAPI.NAME)
public final class UserSessions implements UserSessionsAPI {

    static class UserSessionInfo implements Serializable {
        private static final long serialVersionUID = -4834267718111570841L;

        final UserSession session;
        final long since;
        volatile long lastUsedTs; // set to 0 when propagating removal to cluster
        volatile long lastSentTs;

        UserSessionInfo(UserSession session, long now) {
            this.session = session;
            this.since = now;
            this.lastUsedTs = now;
            this.lastSentTs = now;
        }

        @Override
        public String toString() {
            return session + ", since: " + new Date(since) + ", lastUsed: " + new Date(lastUsedTs);
        }
    }

    private Logger log = LoggerFactory.getLogger(UserSessions.class);

    private Map<UUID, UserSessionInfo> cache = new ConcurrentHashMap<>();

    private volatile int expirationTimeout = 1800;

    private volatile int sendTimeout = 10;

    private ClusterManagerAPI clusterManager;

    private UserSession NO_USER_SESSION;

    private ServerConfig serverConfig;

    @Inject
    private TimeSource timeSource;

    @Inject
    private Metadata metadata;

    public UserSessions() {
        User noUser = new User();
        noUser.setLogin("server");
        NO_USER_SESSION = new UserSession(
                UUID.fromString("a66abe96-3b9d-11e2-9db2-3860770d7eaf"), noUser,
                Collections.<Role>emptyList(), Locale.getDefault(), true) {
            @Override
            public UUID getId() {
                return AppContext.NO_USER_CONTEXT.getSessionId();
            }
        };
    }

    @Inject
    public void setConfiguration(Configuration configuration) {
        serverConfig = configuration.getConfig(ServerConfig.class);
        setExpirationTimeoutSec(serverConfig.getUserSessionExpirationTimeoutSec());
        setSendTimeoutSec(serverConfig.getUserSessionSendTimeoutSec());
    }

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        this.clusterManager.addListener(
                UserSessionInfo.class,
                new ClusterListener<UserSessionInfo>() {

                    @Override
                    public void receive(UserSessionInfo message) {
                        UUID id = message.session.getId();
                        if (message.lastUsedTs == 0) {
                            cache.remove(id);
                        } else {
                            UserSessionInfo usi = cache.get(id);
                            if (usi == null || usi.lastUsedTs < message.lastUsedTs) {
                                cache.put(id, message);
                            }
                        }
                    }

                    @Override
                    public byte[] getState() {
                        if (cache.isEmpty())
                            return new byte[0];

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeInt(cache.size());
                            for (UserSessionInfo usi : cache.values()) {
                                oos.writeObject(usi);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return bos.toByteArray();
                    }

                    @Override
                    public void setState(byte[] state) {
                        if (state == null || state.length == 0)
                            return;

                        ByteArrayInputStream bis = new ByteArrayInputStream(state);
                        try {
                            ObjectInputStream ois = new ObjectInputStream(bis);
                            int size = ois.readInt();
                            for (int i = 0; i < size; i++) {
                                UserSessionInfo usi = (UserSessionInfo) ois.readObject();
                                receive(usi);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            log.error("Error receiving state", e);
                        }
                    }
                }
        );
    }

    @Override
    public void add(UserSession session) {
        UserSessionInfo usi = new UserSessionInfo(session, timeSource.currentTimeMillis());
        cache.put(session.getId(), usi);
        if (!session.isSystem()) {
            if (serverConfig.getSyncNewUserSessionReplication())
                clusterManager.sendSync(usi);
            else
                clusterManager.send(usi);
        }
    }

    @Override
    public void remove(UserSession session) {
        UserSessionInfo usi = cache.remove(session.getId());

        if (!session.isSystem() && usi != null) {
            usi.lastUsedTs = 0;
            clusterManager.send(usi);
        }
    }

    @Override
    public UserSession get(UUID id, boolean propagate) {
        if (!AppContext.isStarted())
            return NO_USER_SESSION;

        UserSessionInfo usi = cache.get(id);
        if (usi != null) {
            long now = timeSource.currentTimeMillis();
            usi.lastUsedTs = now;
            if (propagate && !usi.session.isSystem()) {
                if (now > (usi.lastSentTs + sendTimeout * 1000)) {
                    usi.lastSentTs = now;
                    clusterManager.send(usi);
                }
            }
            return usi.session;
        }
        return null;
    }

    @Override
    public void propagate(UUID id) {
        UserSessionInfo usi = cache.get(id);
        if (usi != null) {
            usi.lastUsedTs = timeSource.currentTimestamp().getTime();
            usi.lastSentTs = timeSource.currentTimestamp().getTime();
            clusterManager.send(usi);
        }
    }

    @Override
    public int getExpirationTimeoutSec() {
        return expirationTimeout;
    }

    @Override
    public void setExpirationTimeoutSec(int value) {
        expirationTimeout = value;
    }

    @Override
    public int getSendTimeoutSec() {
        return sendTimeout;
    }

    @Override
    public void setSendTimeoutSec(int timeout) {
        this.sendTimeout = timeout;
    }

    @Override
    public Collection<UserSessionEntity> getUserSessionInfo() {
        ArrayList<UserSessionEntity> sessionInfoList = new ArrayList<>();
        for (UserSessionInfo nfo : cache.values()) {
            UserSessionEntity use = createUserSessionEntity(nfo.session, nfo.since, nfo.lastUsedTs);
            sessionInfoList.add(use);
        }
        return sessionInfoList;
    }

    private UserSessionEntity createUserSessionEntity(UserSession session, long since, long lastUsedTs) {
        UserSessionEntity use = metadata.create(UserSessionEntity.class);
        use.setId(session.getId());
        use.setLogin(session.getUser().getLogin());
        use.setUserName(session.getUser().getName());
        use.setAddress(session.getAddress());
        use.setClientInfo(session.getClientInfo());
        Date currSince = timeSource.currentTimestamp();
        currSince.setTime(since);
        use.setSince(currSince);
        Date last = timeSource.currentTimestamp();
        last.setTime(lastUsedTs);
        use.setLastUsedTs(last);
        use.setSystem(session.isSystem());
        return use;
    }

    @Override
    public void killSession(UUID id){
        UserSessionInfo usi = cache.remove(id);

        if (usi != null) {
            usi.lastUsedTs = 0;
            clusterManager.send(usi);
        }
    }

    @Override
    public void processEviction() {
        if (!AppContext.isStarted())
            return;

        log.trace("Processing eviction");
        long now = timeSource.currentTimeMillis();
        for (Iterator<UserSessionInfo> it = cache.values().iterator(); it.hasNext();) {
            UserSessionInfo usi = it.next();
            if (now > (usi.lastUsedTs + expirationTimeout * 1000)) {
                it.remove();

                usi.lastUsedTs = 0;
                clusterManager.send(usi);
            }
        }
    }
}
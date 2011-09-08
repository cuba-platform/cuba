/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 11:18:40
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.Heartbeat;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserSessions MBean implementation.
 * <p>
 * Holds and controls the current user sessions list.
 */
@ManagedBean(UserSessionsAPI.NAME)
public class UserSessions implements UserSessionsMBean, UserSessionsAPI, Heartbeat.Listener {

    private static class UserSessionInfo implements Serializable {
        private static final long serialVersionUID = -4834267718111570841L;

        private final UserSession session;
        private final long since;
        private volatile long lastUsedTs; // set to 0 when propagating removal to cluster

        private UserSessionInfo(UserSession session) {
            this.session = session;
            long now = TimeProvider.currentTimestamp().getTime();
            this.since = now;
            this.lastUsedTs = now;
        }

        public String toString() {
            return session + ", since: " + new Date(since) + ", lastUsed: " + new Date(lastUsedTs);
        }
    }

    private Log log = LogFactory.getLog(UserSessions.class);

    private Map<UUID, UserSessionInfo> cache = new ConcurrentHashMap<UUID, UserSessionInfo>();

    private volatile int expirationTimeout = 1800;

    private ClusterManagerAPI clusterManager;

    public UserSessions() {
    }

    @Inject
    public void setConfigProvider(Configuration configuration) {
        ServerConfig config = configuration.getConfig(ServerConfig.class);
        setExpirationTimeoutSec(config.getUserSessionExpirationTimeoutSec());
    }

    @Inject
    public void setHeartbeat(Heartbeat heartbeat) {
        heartbeat.addListener(this, 10);
    }

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        this.clusterManager.addListener(
                UserSessionInfo.class,
                new ClusterListener<UserSessionInfo>() {

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
                        } catch (IOException e) {
                            log.error("Error receiving state", e);
                        } catch (ClassNotFoundException e) {
                            log.error("Error receiving state", e);
                        }
                    }
                }
        );
    }

    public void add(UserSession session) {
        UserSessionInfo usi = new UserSessionInfo(session);
        cache.put(session.getId(), usi);
        if (!session.isSystem())
            clusterManager.send(usi);
    }

    public void remove(UserSession session) {
        UserSessionInfo usi = cache.remove(session.getId());

        if (!session.isSystem() && usi != null) {
            usi.lastUsedTs = 0;
            clusterManager.send(usi);
        }
    }

    public UserSession get(UUID id) {
        UserSessionInfo info = cache.get(id);
        if (info != null) {
            info.lastUsedTs = TimeProvider.currentTimestamp().getTime();
            return info.session;
        }
        return null;
    }

    public UserSessionsAPI getAPI() {
        return this;
    }

    public int getExpirationTimeoutSec() {
        return expirationTimeout;
    }

    public void setExpirationTimeoutSec(int value) {
        expirationTimeout = value;
    }

    public int getCount() {
        return cache.size();
    }

    public String printSessions() {
        StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators(cache.values(), "\n");
        return sb.toString();
    }

    public Collection<UserSessionEntity> getUserSessionInfo() {
        ArrayList<UserSessionEntity> sessionInfoList = new ArrayList<UserSessionEntity>();
        for (UserSessionInfo nfo : cache.values()) {
            UserSessionEntity use = new UserSessionEntity();
            use.setId(nfo.session.getId());
            use.setLogin(nfo.session.getUser().getLogin());
            use.setUserName(nfo.session.getUser().getName());
            use.setAddress(nfo.session.getAddress());
            use.setClientInfo(nfo.session.getClientInfo());
            Date since = TimeProvider.currentTimestamp();
            since.setTime(nfo.since);
            use.setSince(since);
            Date last = TimeProvider.currentTimestamp();
            last.setTime(nfo.lastUsedTs);
            use.setLastUsedTs(last);
            use.setSystem(nfo.session.isSystem());
            sessionInfoList.add(use);
        }
        return sessionInfoList;
    }

    public void killSession(UUID id){
        cache.remove(id);
    }

    public void processEviction() {
        log.trace("Processing eviction");
        long now = TimeProvider.currentTimestamp().getTime();
        for (Iterator<UserSessionInfo> it = cache.values().iterator(); it.hasNext();) {
            UserSessionInfo usi = it.next();
            if (now > (usi.lastUsedTs + expirationTimeout * 1000)) {
                it.remove();

                usi.lastUsedTs = 0;
                clusterManager.send(usi);
            }
        }
    }

    public void beat() {
        processEviction();
    }
}

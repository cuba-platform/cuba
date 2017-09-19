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

package com.haulmont.cuba.security.idp;

import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UuidSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.IdpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.concurrent.GuardedBy;
import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Component(IdpSessionStore.NAME)
public class IdpSessionStoreBean implements IdpSessionStore {
    private final Logger log = LoggerFactory.getLogger(IdpSessionStoreBean.class);

    @GuardedBy("lock")
    protected Map<String, IdpSessionRecord> sessions = new HashMap<>();
    @GuardedBy("lock")
    protected Map<String, IdpSessionTicketRecord> sessionTickets = new HashMap<>();

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected ClusterManagerAPI clusterManager;

    protected volatile int sendTimeoutSec = 10;

    @Inject
    protected UuidSource uuidSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;

        this.clusterManager.addListener(IdpSessionRecord.class,
                new ClusterListener<IdpSessionRecord>() {
                    @Override
                    public void receive(IdpSessionRecord message) {
                        receivedSessionFromCluster(message);
                    }

                    @Override
                    public byte[] getState() {
                        if (sessions.isEmpty()) {
                            return new byte[0];
                        }

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                        lock.readLock().lock();
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeInt(sessions.size());
                            for (IdpSessionRecord sessionRecord : sessions.values()) {
                                oos.writeObject(sessionRecord);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Unable to serialize IdpSessions for cluster state", e);
                        } finally {
                            lock.readLock().unlock();
                        }

                        return bos.toByteArray();
                    }

                    @Override
                    public void setState(byte[] state) {
                        if (state == null || state.length == 0) {
                            return;
                        }

                        ByteArrayInputStream bis = new ByteArrayInputStream(state);
                        try {
                            ObjectInputStream ois = new ObjectInputStream(bis);
                            int size = ois.readInt();
                            for (int i = 0; i < size; i++) {
                                IdpSessionRecord sessionRecord = (IdpSessionRecord) ois.readObject();
                                receive(sessionRecord);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            log.error("Error receiving state", e);
                        }
                    }
                });

        this.clusterManager.addListener(IdpSessionTicketRecord.class,
                new ClusterListener<IdpSessionTicketRecord>() {
                    @Override
                    public void receive(IdpSessionTicketRecord message) {
                        receivedTicketFromCluster(message);
                    }

                    @Override
                    public byte[] getState() {
                        if (sessionTickets.isEmpty()) {
                            return new byte[0];
                        }

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();

                        lock.readLock().lock();
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeInt(sessionTickets.size());
                            for (IdpSessionTicketRecord ticketRecord : sessionTickets.values()) {
                                oos.writeObject(ticketRecord);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Unable to serialize IdpSessions for cluster state", e);
                        } finally {
                            lock.readLock().unlock();
                        }

                        return bos.toByteArray();
                    }

                    @Override
                    public void setState(byte[] state) {
                        if (state == null || state.length == 0) {
                            return;
                        }

                        ByteArrayInputStream bis = new ByteArrayInputStream(state);
                        try {
                            ObjectInputStream ois = new ObjectInputStream(bis);
                            int size = ois.readInt();
                            for (int i = 0; i < size; i++) {
                                IdpSessionTicketRecord ticketRecord = (IdpSessionTicketRecord) ois.readObject();
                                receive(ticketRecord);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            log.error("Error receiving state", e);
                        }
                    }
                });

        this.clusterManager.addListener(NewSessionClusterMessage.class,
                new ClusterListenerAdapter<NewSessionClusterMessage>() {
                    @Override
                    public void receive(NewSessionClusterMessage message) {
                        receivedSessionFromCluster(message.getSession());
                        receivedTicketFromCluster(message.getTicket());
                    }
                });
    }

    protected void receivedSessionFromCluster(IdpSessionRecord message) {
        String id = message.session.getId();

        lock.writeLock().lock();
        try {
            if (message.lastUsedTs == 0) {
                log.debug("Removing session due to cluster message: {}", message);
                sessions.remove(id);
            } else {
                IdpSessionRecord sessionRecord = sessions.get(id);
                if (sessionRecord == null || sessionRecord.lastUsedTs < message.lastUsedTs) {
                    sessions.put(id, message);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void receivedTicketFromCluster(IdpSessionTicketRecord message) {
        String id = message.getId();

        lock.writeLock().lock();
        try {
            if (!message.isActive()) {
                log.debug("Removing ticket due to cluster message: {}", message);
                sessionTickets.remove(id);
            } else {
                sessionTickets.putIfAbsent(id, message);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String putSession(IdpSession session) {
        String serviceProviderTicket;
        lock.writeLock().lock();

        IdpSessionRecord sessionRecord;
        IdpSessionTicketRecord ticketRecord;

        try {
            sessionRecord = new IdpSessionRecord(session, timeSource.currentTimeMillis());
            sessions.put(session.getId(), sessionRecord);

            serviceProviderTicket = uuidSource.createUuid().toString().replace("-", "");
            ticketRecord = new IdpSessionTicketRecord(serviceProviderTicket, session.getId(), timeSource.currentTimeMillis());
            sessionTickets.put(serviceProviderTicket, ticketRecord);
        } finally {
            lock.writeLock().unlock();
        }

        clusterManager.sendSync(new NewSessionClusterMessage(sessionRecord, ticketRecord));

        return serviceProviderTicket;
    }

    @Override
    public boolean removeSession(String sessionId) {
        lock.writeLock().lock();
        IdpSessionRecord removedRecord;
        try {
            removedRecord = sessions.remove(sessionId);
            // tickets will be removed on expiration
        } finally {
            lock.writeLock().unlock();
        }

        if (removedRecord == null) {
            return false;
        }

        removedRecord.setLastUsedTs(0);

        clusterManager.sendSync(removedRecord);

        return true;
    }

    @Override
    public IdpSession activateSessionTicket(String serviceProviderTicket) {
        IdpSession idpSession;

        IdpSessionTicketRecord ticketRecord;

        lock.writeLock().lock();
        try {
            ticketRecord = sessionTickets.remove(serviceProviderTicket);
            if (ticketRecord == null) {
                return null;
            }

            String sessionId = ticketRecord.getSessionId();
            IdpSessionRecord sessionInfo = sessions.get(sessionId);
            idpSession = sessionInfo != null ? sessionInfo.getSession() : null;
        } finally {
            lock.writeLock().unlock();
        }

        if (idpSession != null) {
            ticketRecord.setActive(false);

            clusterManager.sendSync(ticketRecord);
        }

        return idpSession;
    }

    @Override
    public String createServiceProviderTicket(String sessionId) {
        lock.readLock().lock();

        IdpSession session;
        try {
            IdpSessionRecord sessionInfo = sessions.get(sessionId);
            session = sessionInfo != null ? sessionInfo.getSession() : null;
        } finally {
            lock.readLock().unlock();
        }

        if (session == null) {
            return null;
        }

        String serviceProviderTicket = uuidSource.createUuid().toString().replace("-", "");

        IdpSessionTicketRecord ticketRecord;

        lock.writeLock().lock();
        try {
            ticketRecord = new IdpSessionTicketRecord(serviceProviderTicket, sessionId, timeSource.currentTimeMillis());
            sessionTickets.put(serviceProviderTicket, ticketRecord);
        } finally {
            lock.writeLock().unlock();
        }

        clusterManager.sendSync(ticketRecord);

        return serviceProviderTicket;
    }

    @Override
    public List<IdpSessionInfo> getSessions() {
        lock.readLock().lock();

        List<IdpSessionInfo> sessionInfos;
        try {
            sessionInfos = new ArrayList<>(
                    sessions.values().stream()
                            .map(this::toSessionInfo)
                            .collect(Collectors.toList()));
        } finally {
            lock.readLock().unlock();
        }

        return sessionInfos;
    }

    @Override
    public Map<String, IdpSessionTicketInfo> getTickets() {
        Map<String, IdpSessionTicketInfo> tickets = new HashMap<>();
        lock.readLock().lock();
        try {
            for (Map.Entry<String, IdpSessionTicketRecord> entry : sessionTickets.entrySet()) {
                tickets.put(entry.getKey(), toTicketInfo(entry.getValue()));
            }
        } finally {
            lock.readLock().unlock();
        }

        return tickets;
    }

    @Override
    public List<IdpSessionInfo> processEviction(int sessionExpirationTimeoutSec, int ticketExpirationTimeoutSec) {
        if (!AppContext.isStarted()) {
            return Collections.emptyList();
        }

        log.trace("Processing eviction");

        List<IdpSessionInfo> expiredSessions = new ArrayList<>();
        List<IdpSessionRecord> expiredSessionRecords = new ArrayList<>();
        List<IdpSessionTicketRecord> expiredTicketRecords = new ArrayList<>();

        lock.writeLock().lock();
        try {
            long now = timeSource.currentTimeMillis();
            for (Iterator<IdpSessionRecord> it = sessions.values().iterator(); it.hasNext(); ) {
                IdpSessionRecord sessionRecord = it.next();
                if (now > (sessionRecord.getLastUsedTs() + sessionExpirationTimeoutSec * 1000)) {
                    log.debug("Removing session due to timeout: {}", sessionRecord);

                    it.remove();

                    expiredSessionRecords.add(sessionRecord);

                    expiredSessions.add(toSessionInfo(sessionRecord));
                }
            }

            for (Iterator<IdpSessionTicketRecord> it = sessionTickets.values().iterator(); it.hasNext(); ) {
                IdpSessionTicketRecord sessionTicketRecord = it.next();

                if (now > (sessionTicketRecord.getSince() + ticketExpirationTimeoutSec * 1000)) {
                    log.debug("Removing ticket due to timeout: {}", sessionTicketRecord);

                    it.remove();

                    expiredTicketRecords.add(sessionTicketRecord);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }

        for (IdpSessionRecord expiredSession : expiredSessionRecords) {
            expiredSession.setLastSentTs(0);

            clusterManager.send(expiredSession);
        }

        for (IdpSessionTicketRecord expiredTicket : expiredTicketRecords) {
            expiredTicket.setActive(false);

            clusterManager.send(expiredTicket);
        }

        return expiredSessions;
    }

    @Override
    public void propagate(String sessionId) {
        lock.readLock().lock();

        IdpSessionRecord sessionRecord;
        try {
            sessionRecord = sessions.get(sessionId);
        } finally {
            lock.readLock().unlock();
        }

        if (sessionRecord != null) {
            long now = timeSource.currentTimeMillis();
            sessionRecord.setLastUsedTs(now);
            sessionRecord.setLastSentTs(now);
            clusterManager.sendSync(sessionRecord);
        }
    }

    @Override
    public IdpSession getSession(String sessionId) {
        IdpSession session;
        IdpSessionRecord sessionRecord;

        lock.readLock().lock();
        try {
            sessionRecord = sessions.get(sessionId);
            session = sessionRecord != null ? sessionRecord.getSession() : null;
        } finally {
            lock.readLock().unlock();
        }

        if (sessionRecord != null) {
            long now = timeSource.currentTimeMillis();

            sessionRecord.setLastUsedTs(now);

            if (now > sessionRecord.getLastSentTs() + sendTimeoutSec * 1000) {
                sessionRecord.setLastSentTs(now);
                clusterManager.send(sessionRecord);
            }
        }

        return session;
    }

    @Override
    public IdpSessionInfo getSessionInfo(String sessionId) {
        lock.readLock().lock();

        IdpSessionRecord sessionRecord;
        try {
            sessionRecord = sessions.get(sessionId);
        } finally {
            lock.readLock().unlock();
        }
        return toSessionInfo(sessionRecord);
    }

    public int getSendTimeoutSec() {
        return sendTimeoutSec;
    }

    public void setSendTimeoutSec(int sendTimeoutSec) {
        this.sendTimeoutSec = sendTimeoutSec;
    }

    protected IdpSessionTicketInfo toTicketInfo(IdpSessionTicketRecord ticketRecord) {
        return new IdpSessionTicketInfo(ticketRecord.getSessionId(), ticketRecord.getSince());
    }

    protected IdpSessionInfo toSessionInfo(IdpSessionRecord record) {
        if (record == null) {
            return null;
        }

        Date sinceTs = timeSource.currentTimestamp();
        sinceTs.setTime(record.getSince());

        Date lastUsedTs = timeSource.currentTimestamp();
        lastUsedTs.setTime(record.getLastUsedTs());

        IdpSession session = record.getSession();
        return new IdpSessionInfo(
                session.getId(),
                session.getLogin(),
                session.getEmail(),
                session.getLocale(),
                sinceTs,
                lastUsedTs);
    }

    protected static class IdpSessionTicketRecord implements Serializable {
        private final String id;
        private final String sessionId;
        private final long since;
        private volatile boolean active = true; // set to false when propagating removal to cluster

        public IdpSessionTicketRecord(String id, String sessionId, long since) {
            this.id = id;
            this.sessionId = sessionId;
            this.since = since;
        }

        public String getId() {
            return id;
        }

        public String getSessionId() {
            return sessionId;
        }

        public long getSince() {
            return since;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "IdpSessionTicketRecord{" +
                    "id='" + id + '\'' +
                    ", sessionId='" + sessionId + '\'' +
                    ", since=" + since +
                    ", active=" + active +
                    '}';
        }
    }

    protected static class IdpSessionRecord implements Serializable {
        protected final IdpSession session;
        protected final long since;
        protected volatile long lastUsedTs; // set to 0 when propagating removal to cluster
        protected volatile long lastSentTs;

        public IdpSessionRecord(IdpSession session, long since) {
            this.session = session;
            this.since = since;
            this.lastSentTs = since;
            this.lastUsedTs = since;
        }

        public IdpSession getSession() {
            return session;
        }

        public long getSince() {
            return since;
        }

        public long getLastUsedTs() {
            return lastUsedTs;
        }

        public void setLastUsedTs(long lastUsedTs) {
            this.lastUsedTs = lastUsedTs;
        }

        public long getLastSentTs() {
            return lastSentTs;
        }

        public void setLastSentTs(long lastSentTs) {
            this.lastSentTs = lastSentTs;
        }

        @Override
        public String toString() {
            return "IdpSessionRecord{" +
                    "session=" + session +
                    ", lastUsedTs=" + new Date(lastUsedTs) +
                    ", lastSentTs=" + new Date(lastSentTs) +
                    '}';
        }
    }

    protected static class NewSessionClusterMessage implements Serializable {
        private final IdpSessionRecord session;
        private final IdpSessionTicketRecord ticket;

        public NewSessionClusterMessage(IdpSessionRecord session, IdpSessionTicketRecord ticket) {
            this.session = session;
            this.ticket = ticket;
        }

        public IdpSessionRecord getSession() {
            return session;
        }

        public IdpSessionTicketRecord getTicket() {
            return ticket;
        }

        @Override
        public String toString() {
            return "NewSessionClusterMessage{" +
                    "session=" + session +
                    ", ticket=" + ticket +
                    '}';
        }
    }
}
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
 */

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.RestApiToken;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.auth.AuthenticationManager;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component(ServerTokenStore.NAME)
public class ServerTokenStoreImpl implements ServerTokenStore {

    @Inject
    protected AuthenticationManager authenticationManager;

    @Inject
    protected UserSessionManager userSessionManager;

    @Inject
    protected ClusterManagerAPI clusterManagerAPI;

    @Inject
    protected ServerConfig serverConfig;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected TimeSource timeSource;

    protected Logger log = LoggerFactory.getLogger(ServerTokenStoreImpl.class);

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    private ConcurrentHashMap<String, byte[]> tokenValueToAccessTokenStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, byte[]> tokenValueToAuthenticationStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, byte[]> authenticationToAccessTokenStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, UUID> tokenValueToSessionIdStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> tokenValueToAuthenticationKeyStore = new ConcurrentHashMap<>();

    private final DelayQueue<TokenExpiry> expiryQueue = new DelayQueue<>();

    @PostConstruct
    public void init() {
        initClusterListeners();
    }

    protected void initClusterListeners() {
        clusterManagerAPI.addListener(TokenStoreAddTokenMsg.class, new ClusterListener<TokenStoreAddTokenMsg>() {
            @Override
            public void receive(TokenStoreAddTokenMsg message) {
                storeAccessTokenToMemory(message.getTokenValue(),
                        message.getAccessTokenBytes(),
                        message.getAuthenticationKey(),
                        message.getAuthenticationBytes(),
                        message.getTokenExpiry());
            }

            @Override
            public byte[] getState() {
                if (tokenValueToAccessTokenStore.isEmpty() && tokenValueToAuthenticationStore.isEmpty() && authenticationToAccessTokenStore.isEmpty()
                        && tokenValueToSessionIdStore.isEmpty() && tokenValueToAuthenticationKeyStore.isEmpty()) {
                    return new byte[0];
                }

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                lock.readLock().lock();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(tokenValueToAccessTokenStore);
                    oos.writeObject(tokenValueToAuthenticationStore);
                    oos.writeObject(authenticationToAccessTokenStore);
                    oos.writeObject(tokenValueToSessionIdStore);
                    oos.writeObject(tokenValueToAuthenticationKeyStore);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to serialize ServerTokenStore fields for cluster state", e);
                } finally {
                    lock.readLock().unlock();
                }

                return bos.toByteArray();
            }

            @SuppressWarnings("unchecked")
            @Override
            public void setState(byte[] state) {
                if (state == null || state.length == 0) {
                    return;
                }

                ByteArrayInputStream bis = new ByteArrayInputStream(state);
                lock.writeLock().lock();
                try {
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    tokenValueToAccessTokenStore = (ConcurrentHashMap<String, byte[]>) ois.readObject();
                    tokenValueToAuthenticationStore = (ConcurrentHashMap<String, byte[]>) ois.readObject();
                    authenticationToAccessTokenStore = (ConcurrentHashMap<String, byte[]>) ois.readObject();
                    tokenValueToSessionIdStore = (ConcurrentHashMap<String, UUID>) ois.readObject();
                    tokenValueToAuthenticationKeyStore = (ConcurrentHashMap<String, String>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Error receiving state", e);
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });

        clusterManagerAPI.addListener(TokenStorePutSessionMsg.class, new ClusterListenerAdapter<TokenStorePutSessionMsg>() {
            @Override
            public void receive(TokenStorePutSessionMsg message) {
                _putSessionId(message.getTokenValue(), message.getSessionId());
            }
        });

        clusterManagerAPI.addListener(TokenStoreRemoveTokenMsg.class, new ClusterListenerAdapter<TokenStoreRemoveTokenMsg>() {
            @Override
            public void receive(TokenStoreRemoveTokenMsg message) {
                removeAccessTokenFromMemory(message.getTokenValue());
            }
        });
    }

    @Override
    public byte[] getAccessTokenByAuthentication(String authenticationKey) {
        byte[] accessTokenBytes;
        accessTokenBytes = getAccessTokenByAuthenticationFromMemory(authenticationKey);
        if (accessTokenBytes == null && serverConfig.getRestStoreTokensInDb()) {
            RestApiToken restApiToken = getRestApiTokenByAuthenticationKeyFromDatabase(authenticationKey);
            if (restApiToken != null) {
                accessTokenBytes = restApiToken.getAccessTokenBytes();
                restoreInMemoryTokenData(restApiToken);
            }
        }
        return accessTokenBytes;
    }

    protected byte[] getAccessTokenByAuthenticationFromMemory(String authenticationKey) {
        return authenticationToAccessTokenStore.get(authenticationKey);
    }

    @Override
    public void storeAccessToken(String tokenValue,
                                 byte[] accessTokenBytes,
                                 String authenticationKey,
                                 byte[] authenticationBytes,
                                 Date tokenExpiry) {
        storeAccessTokenToMemory(tokenValue, accessTokenBytes, authenticationKey, authenticationBytes, tokenExpiry);
        if (serverConfig.getRestStoreTokensInDb()) {
            removeAccessTokenFromDatabase(tokenValue);
            storeAccessTokenToDatabase(tokenValue, accessTokenBytes, authenticationKey, authenticationBytes, tokenExpiry);
        }
        clusterManagerAPI.send(new TokenStoreAddTokenMsg(tokenValue, accessTokenBytes, authenticationKey, authenticationBytes, tokenExpiry));
    }

    protected void storeAccessTokenToMemory(String tokenValue,
                                            byte[] accessTokenBytes,
                                            String authenticationKey,
                                            byte[] authenticationBytes,
                                            Date tokenExpiry) {
        lock.writeLock().lock();
        try {
            tokenValueToAccessTokenStore.put(tokenValue, accessTokenBytes);
            authenticationToAccessTokenStore.put(authenticationKey, accessTokenBytes);
            tokenValueToAuthenticationStore.put(tokenValue, authenticationBytes);
            tokenValueToAuthenticationKeyStore.put(tokenValue, authenticationKey);
        } finally {
            lock.writeLock().unlock();
        }

        if (tokenExpiry != null) {
            TokenExpiry expiry = new TokenExpiry(tokenValue, tokenExpiry);
            this.expiryQueue.put(expiry);
        }
    }

    protected void storeAccessTokenToDatabase(String tokenValue,
                                              byte[] accessTokenBytes,
                                              String authenticationKey,
                                              byte[] authenticationBytes,
                                              Date tokenExpiry) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            RestApiToken restApiToken = metadata.create(RestApiToken.class);
            restApiToken.setAccessTokenValue(tokenValue);
            restApiToken.setAccessTokenBytes(accessTokenBytes);
            restApiToken.setAuthenticationKey(authenticationKey);
            restApiToken.setAuthenticationBytes(authenticationBytes);
            restApiToken.setExpiry(tokenExpiry);
            em.persist(restApiToken);
            tx.commit();
        }
    }

    @Override
    public byte[] getAccessTokenByTokenValue(String accessTokenValue) {
        byte[] accessTokenBytes;
        accessTokenBytes = getAccessTokenByTokenValueFromMemory(accessTokenValue);
        if (accessTokenBytes == null && serverConfig.getRestStoreTokensInDb()) {
            RestApiToken restApiToken = getRestApiTokenByTokenValueFromDatabase(accessTokenValue);
            if (restApiToken != null) {
                accessTokenBytes = restApiToken.getAccessTokenBytes();
                restoreInMemoryTokenData(restApiToken);
            }
        }
        return accessTokenBytes;
    }

    protected byte[] getAccessTokenByTokenValueFromMemory(String tokenValue) {
        return tokenValueToAccessTokenStore.get(tokenValue);
    }

    @Override
    public byte[] getAuthenticationByTokenValue(String tokenValue) {
        byte[] authenticationBytes;
        authenticationBytes = getAuthenticationByTokenValueFromMemory(tokenValue);
        if (authenticationBytes == null && serverConfig.getRestStoreTokensInDb()) {
            RestApiToken restApiToken = getRestApiTokenByTokenValueFromDatabase(tokenValue);
            if (restApiToken != null) {
                authenticationBytes = restApiToken.getAuthenticationBytes();
                restoreInMemoryTokenData(restApiToken);
            }
        }
        return authenticationBytes;
    }

    protected byte[] getAuthenticationByTokenValueFromMemory(String tokenValue) {
        return tokenValueToAuthenticationStore.get(tokenValue);
    }

    @Nullable
    protected RestApiToken getRestApiTokenByTokenValueFromDatabase(String accessTokenValue) {
        RestApiToken restApiToken;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            restApiToken = em.createQuery("select e from sys$RestApiToken e where e.accessTokenValue = :accessTokenValue", RestApiToken.class)
                    .setParameter("accessTokenValue", accessTokenValue)
                    .setViewName(View.LOCAL)
                    .getFirstResult();
            tx.commit();
            return restApiToken;
        }
    }

    @Nullable
    protected RestApiToken getRestApiTokenByAuthenticationKeyFromDatabase(String authenticationKey) {
        RestApiToken restApiToken;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            restApiToken = em.createQuery("select e from sys$RestApiToken e where e.authenticationKey = :authenticationKey", RestApiToken.class)
                    .setParameter("authenticationKey", authenticationKey)
                    .setViewName(View.LOCAL)
                    .getFirstResult();
            tx.commit();
            return restApiToken;
        }
    }

    /**
     * Method fills in-memory maps from the {@link RestApiToken} object got from the database
     */
    protected void restoreInMemoryTokenData(RestApiToken restApiToken) {
        lock.writeLock().lock();
        try {
            tokenValueToAccessTokenStore.put(restApiToken.getAccessTokenValue(), restApiToken.getAccessTokenBytes());
            authenticationToAccessTokenStore.put(restApiToken.getAuthenticationKey(), restApiToken.getAccessTokenBytes());
            tokenValueToAuthenticationStore.put(restApiToken.getAccessTokenValue(), restApiToken.getAuthenticationBytes());
            tokenValueToAuthenticationKeyStore.put(restApiToken.getAccessTokenValue(), restApiToken.getAuthenticationKey());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public UUID getSessionIdByTokenValue(String tokenValue) {
        return tokenValueToSessionIdStore.get(tokenValue);
    }

    @Override
    public UUID putSessionId(String tokenValue, UUID sessionId) {
        UUID uuid = _putSessionId(tokenValue, sessionId);
        clusterManagerAPI.send(new TokenStorePutSessionMsg(tokenValue, sessionId));
        return uuid;
    }

    protected UUID _putSessionId(String tokenValue, UUID sessionId) {
        lock.writeLock().lock();
        try {
            return tokenValueToSessionIdStore.put(tokenValue, sessionId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeAccessToken(String tokenValue) {
        removeAccessTokenFromMemory(tokenValue);
        if (serverConfig.getRestStoreTokensInDb()) {
            removeAccessTokenFromDatabase(tokenValue);
        }
        clusterManagerAPI.send(new TokenStoreRemoveTokenMsg(tokenValue));
    }

    protected void removeAccessTokenFromMemory(String tokenValue) {
        UUID sessionId;
        lock.writeLock().lock();
        try {
            tokenValueToAccessTokenStore.remove(tokenValue);
            tokenValueToAuthenticationStore.remove(tokenValue);
            String authenticationKey = tokenValueToAuthenticationKeyStore.remove(tokenValue);
            authenticationToAccessTokenStore.remove(authenticationKey);
            sessionId = tokenValueToSessionIdStore.remove(tokenValue);
        } finally {
            lock.writeLock().unlock();
        }
        if (sessionId != null) {
            try {
                UserSession session = userSessionManager.findSession(sessionId);
                if (session != null) {
                    AppContext.setSecurityContext(new SecurityContext(session));
                    try {
                        authenticationManager.logout();
                    } finally {
                        AppContext.setSecurityContext(null);
                    }
                }
            } catch (NoUserSessionException ignored) {
            }
        }
    }

    protected void removeAccessTokenFromDatabase(String accessTokenValue) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            em.createQuery("delete from sys$RestApiToken t where t.accessTokenValue = :accessTokenValue")
                    .setParameter("accessTokenValue", accessTokenValue)
                    .executeUpdate();
            tx.commit();
        }
    }

    @Override
    public void deleteExpiredTokens() {
        deleteExpiredTokensInMemory();
        if (serverConfig.getRestStoreTokensInDb() && clusterManagerAPI.isMaster()) {
            deleteExpiredTokensInDatabase();
        }
    }

    protected void deleteExpiredTokensInMemory() {
        TokenExpiry expiry = expiryQueue.poll();
        while (expiry != null) {
            removeAccessToken(expiry.getValue());
            expiry = expiryQueue.poll();
        }
    }

    protected void deleteExpiredTokensInDatabase() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            em.createQuery("delete from sys$RestApiToken t where t.expiry < CURRENT_TIMESTAMP")
                    .executeUpdate();
            tx.commit();
        }
    }

    protected static class TokenExpiry implements Delayed {

        private final long expiry;

        private final String value;

        public TokenExpiry(String value, Date date) {
            this.value = value;
            this.expiry = date.getTime();
        }

        @Override
        public int compareTo(Delayed other) {
            if (this == other) {
                return 0;
            }
            long diff = getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
            return (diff == 0 ? 0 : ((diff < 0) ? -1 : 1));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return expiry - System.currentTimeMillis();
        }

        public String getValue() {
            return value;
        }
    }
}
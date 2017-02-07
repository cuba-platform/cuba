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

import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 */
@Component(ServerTokenStore.NAME)
public class ServerTokenStoreImpl implements ServerTokenStore {

    @Inject
    protected UserSessionManager userSessionManager;

    @Inject
    protected ClusterManagerAPI clusterManagerAPI;

    protected Logger log = LoggerFactory.getLogger(ServerTokenStoreImpl.class);

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    private ConcurrentHashMap<String, byte[]> tokenValueToAccessTokenStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, byte[]> tokenValueToAuthenticationStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, byte[]> authenticationToAccessTokenStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, UUID> tokenValueToSessionIdStore = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> tokenValueToAuthenticationKeyStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        initClusterListeners();
    }

    protected void initClusterListeners() {
        clusterManagerAPI.addListener(TokenStoreAddTokenMsg.class, new ClusterListener<TokenStoreAddTokenMsg>() {
            @Override
            public void receive(TokenStoreAddTokenMsg message) {
                _storeAccessToken(message.getTokenValue(),
                        message.getAccessTokenBytes(),
                        message.getAuthenticationKey(),
                        message.getAuthenticationBytes());
            }

            @Override
            public byte[] getState() {
                if (tokenValueToAccessTokenStore.isEmpty() && tokenValueToAccessTokenStore.isEmpty() & authenticationToAccessTokenStore.isEmpty()
                        & tokenValueToSessionIdStore.isEmpty() & tokenValueToAuthenticationKeyStore.isEmpty()) {
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

            @Override
            public void setState(byte[] state) {
                if (state == null || state.length == 0) {
                    return;
                }

                ByteArrayInputStream bis = new ByteArrayInputStream(state);
                try {
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    tokenValueToAccessTokenStore = (ConcurrentHashMap<String, byte[]>) ois.readObject();
                    tokenValueToAuthenticationStore = (ConcurrentHashMap<String, byte[]>) ois.readObject();
                    authenticationToAccessTokenStore = (ConcurrentHashMap<String, byte[]>) ois.readObject();
                    tokenValueToSessionIdStore = (ConcurrentHashMap<String, UUID>) ois.readObject();
                    tokenValueToAuthenticationKeyStore = (ConcurrentHashMap<String, String>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Error receiving state", e);
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
                _removeAccessToken(message.getTokenValue());
            }
        });
    }

    @Override
    public byte[] getAccessTokenByAuthentication(String authenticationKey) {
        return authenticationToAccessTokenStore.get(authenticationKey);
    }

    @Override
    public void storeAccessToken(String tokenValue, byte[] accessTokenBytes, String authenticationKey, byte[] authenticationBytes) {
        _storeAccessToken(tokenValue, accessTokenBytes, authenticationKey, authenticationBytes);
        clusterManagerAPI.send(new TokenStoreAddTokenMsg(tokenValue, accessTokenBytes, authenticationKey, authenticationBytes));
    }

    protected void _storeAccessToken(String tokenValue, byte[] accessTokenBytes, String authenticationKey, byte[] authenticationBytes) {
        tokenValueToAccessTokenStore.put(tokenValue, accessTokenBytes);
        authenticationToAccessTokenStore.put(authenticationKey, accessTokenBytes);
        tokenValueToAuthenticationStore.put(tokenValue, authenticationBytes);
        tokenValueToAuthenticationKeyStore.put(tokenValue, authenticationKey);
    }

    @Override
    public byte[] getAccessTokenByTokenValue(String tokenValue) {
        return tokenValueToAccessTokenStore.get(tokenValue);
    }

    @Override
    public byte[] getAuthenticationByTokenValue(String tokenValue) {
        return tokenValueToAuthenticationStore.get(tokenValue);
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
        return tokenValueToSessionIdStore.put(tokenValue, sessionId);
    }

    @Override
    public void removeAccessToken(String tokenValue) {
        _removeAccessToken(tokenValue);
        clusterManagerAPI.send(new TokenStoreRemoveTokenMsg(tokenValue));
    }

    protected void _removeAccessToken(String tokenValue) {
        tokenValueToAccessTokenStore.remove(tokenValue);
        tokenValueToAuthenticationStore.remove(tokenValue);
        String authenticationKey = tokenValueToAuthenticationKeyStore.remove(tokenValue);
        authenticationToAccessTokenStore.remove(authenticationKey);
        UUID sessionId = tokenValueToSessionIdStore.remove(tokenValue);
        if (sessionId != null) {
            try {
                UserSession session = userSessionManager.getSession(sessionId);
                userSessionManager.removeSession(session);
            } catch (NoUserSessionException ignored) {}
        }
    }
}

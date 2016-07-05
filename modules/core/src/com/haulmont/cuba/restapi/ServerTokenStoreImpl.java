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

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
@Component(ServerTokenStore.NAME)
public class ServerTokenStoreImpl implements ServerTokenStore {

    @Inject
    protected UserSessionManager userSessionManager;

    private final ConcurrentHashMap<String, byte[]> tokenValueToAccessTokenStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, byte[]> tokenValueToAuthenticationStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, byte[]> authenticationToAccessTokenStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UUID> tokenValueToSessionIdStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> tokenValueToAuthenticationKeyStore = new ConcurrentHashMap<>();

    @Override
    public byte[] getAccessTokenByAuthentication(String authenticationKey) {
        return authenticationToAccessTokenStore.get(authenticationKey);
    }

    @Override
    public void storeAccessToken(String tokenValue, byte[] accessTokenBytes, String authenticationKey, byte[] authenticationBytes) {
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
        return tokenValueToSessionIdStore.put(tokenValue, sessionId);
    }

    @Override
    public void removeAccessToken(String tokenValue) {
        tokenValueToAccessTokenStore.remove(tokenValue);
        tokenValueToAuthenticationStore.remove(tokenValue);
        String authenticationKey = tokenValueToAuthenticationKeyStore.remove(tokenValue);
        authenticationToAccessTokenStore.remove(authenticationKey);
        UUID sessionId = tokenValueToSessionIdStore.remove(tokenValue);
        UserSession session = userSessionManager.getSession(sessionId);
        if (session != null)
            userSessionManager.removeSession(session);
    }
}

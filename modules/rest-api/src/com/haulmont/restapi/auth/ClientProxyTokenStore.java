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

package com.haulmont.restapi.auth;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.restapi.ServerTokenStore;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.restapi.config.RestApiConfig;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * A token store that redirects request from the client to to the {@link ServerTokenStore} located at the middleware.
 */
public class ClientProxyTokenStore implements TokenStore {

    @Inject
    protected ServerTokenStore serverTokenStore;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected RestApiConfig restApiConfig;

    @Inject
    protected LoginService loginService;

    protected AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return  readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        byte[] authenticationBytes = serverTokenStore.getAuthenticationByTokenValue(token);
        OAuth2Authentication authentication = authenticationBytes != null ? deserializeAuthentication(authenticationBytes) : null;
        if (authentication != null) {
            processSession(authentication, token);
        }
        return authentication;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String authenticationKey = authenticationKeyGenerator.extractKey(authentication);
        serverTokenStore.storeAccessToken(token.getValue(),
                serializeAccessToken(token),
                authenticationKey,
                serializeAuthentication(authentication));
        processSession(authentication, token.getValue());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] accessTokenBytes = serverTokenStore.getAccessTokenByTokenValue(tokenValue);
        return accessTokenBytes != null ? deserializeAccessToken(accessTokenBytes) : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        serverTokenStore.removeAccessToken(token.getValue());
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = authenticationKeyGenerator.extractKey(authentication);
        byte[] accessTokenBytes = serverTokenStore.getAccessTokenByAuthentication(key);
        return accessTokenBytes != null ? deserializeAccessToken(accessTokenBytes) : null;
    }

    /**
     * Tries to find the session associated with the given {@code authentication}. If the session id is in the store and
     * exists then it is set to the {@link SecurityContext}. If the session id is not in the store or the session with
     * the id doesn't exist in the middleware, then the trusted login attempt is performed.
     */
    protected void processSession(OAuth2Authentication authentication, String tokenValue) {
        UUID sessionId = serverTokenStore.getSessionIdByTokenValue(tokenValue);

        if (sessionId == null) {
            Map<String, String> userAuthenticationDetails = (Map<String, String>) authentication.getUserAuthentication().getDetails();
            //sessionId parameter was put in the CubaUserAuthenticationProvider
            String sessionIdStr = userAuthenticationDetails.get("sessionId");
            if (!Strings.isNullOrEmpty(sessionIdStr)) {
                sessionId = UUID.fromString(sessionIdStr);
            }
        }

        UserSession session = null;
        if (sessionId != null) {
            session = loginService.getSession(sessionId);
        }

        if (session == null) {
            Map<String, String> userAuthenticationDetails = (Map<String, String>) authentication.getUserAuthentication().getDetails();
            String username = userAuthenticationDetails.get("username");
            try {
                session = loginService.loginTrusted(username, restApiConfig.getTrustedClientPassword(),
                        getDefaultLocale());
            } catch (LoginException e) {
                throw new OAuth2Exception("Cannot login to the middleware");
            }
        }

        if (session != null) {
            serverTokenStore.putSessionId(tokenValue, session.getId());
            AppContext.setSecurityContext(new SecurityContext(session));
        }
    }

    private Locale getDefaultLocale() {
        return globalConfig.getAvailableLocales().values().iterator().next();
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        throw new UnsupportedOperationException();
    }
}

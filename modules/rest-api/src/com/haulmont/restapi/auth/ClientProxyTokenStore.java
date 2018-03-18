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
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.restapi.RestUserSessionInfo;
import com.haulmont.cuba.restapi.ServerTokenStore;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.restapi.common.RestAuthUtils;
import com.haulmont.restapi.config.RestApiConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * A token store that redirects request from the client to the {@link ServerTokenStore} located at the middleware.
 */
public class ClientProxyTokenStore implements TokenStore {

    private static final Logger log = LoggerFactory.getLogger(ClientProxyTokenStore.class);

    @Inject
    protected ServerTokenStore serverTokenStore;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected RestApiConfig restApiConfig;

    @Inject
    protected AuthenticationService authenticationService;

    @Inject
    protected TrustedClientService trustedClientService;

    protected AuthenticationKeyGenerator authenticationKeyGenerator;

    @Inject
    protected RestAuthUtils restAuthUtils;

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
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
        String userLogin = authentication.getName();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Locale locale = restAuthUtils.extractLocaleFromRequestHeader(request);
        String refreshTokenValue = token.getRefreshToken() != null ? token.getRefreshToken().getValue() : null;
        serverTokenStore.storeAccessToken(token.getValue(),
                serializeAccessToken(token),
                authenticationKey,
                serializeAuthentication(authentication),
                token.getExpiration(),
                userLogin,
                locale,
                refreshTokenValue);
        processSession(authentication, token.getValue());
        log.info("REST API access token stored: [{}] {}", authentication.getPrincipal(), token.getValue()) ;
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
        RestUserSessionInfo sessionInfo = serverTokenStore.getSessionInfoByTokenValue(tokenValue);
        UUID sessionId = sessionInfo != null ? sessionInfo.getId() : null;
        if (sessionId == null) {
            @SuppressWarnings("unchecked")
            Map<String, String> userAuthenticationDetails =
                    (Map<String, String>) authentication.getUserAuthentication().getDetails();
            //sessionId parameter was put in the CubaUserAuthenticationProvider
            String sessionIdStr = userAuthenticationDetails.get("sessionId");
            if (!Strings.isNullOrEmpty(sessionIdStr)) {
                sessionId = UUID.fromString(sessionIdStr);
            }
        }

        UserSession session = null;
        if (sessionId != null) {
            try {
                session = trustedClientService.findSession(restApiConfig.getTrustedClientPassword(), sessionId);
            } catch (LoginException e) {
                throw new RuntimeException("Unable to login with trusted client password");
            }
        }

        if (session == null) {
            @SuppressWarnings("unchecked")
            Map<String, String> userAuthenticationDetails =
                    (Map<String, String>) authentication.getUserAuthentication().getDetails();
            String username = userAuthenticationDetails.get("username");

            if (Strings.isNullOrEmpty(username)) {
                throw new IllegalStateException("Empty username extracted from user authentication details");
            }

            Locale locale = sessionInfo != null ?
                    sessionInfo.getLocale() : null;

            TrustedClientCredentials credentials = new TrustedClientCredentials(username,
                    restApiConfig.getTrustedClientPassword(), locale);
            credentials.setClientType(ClientType.REST_API);
            
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                credentials.setIpAddress(request.getRemoteAddr());
                credentials.setClientInfo(makeClientInfo(request.getHeader(HttpHeaders.USER_AGENT)));
            } else {
                credentials.setClientInfo(makeClientInfo(""));
            }

            //if locale was not determined then use the user locale
            if (locale == null) {
                credentials.setOverrideLocale(false);
            }

            try {
                session = authenticationService.login(credentials).getSession();
            } catch (LoginException e) {
                throw new OAuth2Exception("Cannot login to the middleware", e);
            }

            log.debug("New session created for token '{}' since the original session has been expired", tokenValue);
        }

        if (session != null) {
            serverTokenStore.putSessionInfo(tokenValue, new RestUserSessionInfo(session));
            AppContext.setSecurityContext(new SecurityContext(session));
        }
    }

    protected String makeClientInfo(String userAgent) {
        //noinspection UnnecessaryLocalVariable
        String serverInfo = String.format("REST API (%s:%s/%s) %s",
                globalConfig.getWebHostName(),
                globalConfig.getWebPort(),
                globalConfig.getWebContextName(),
                StringUtils.trimToEmpty(userAgent));

        return serverInfo;
    }

    protected Locale getDefaultLocale() {
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
        Date tokenExpiry = refreshToken instanceof ExpiringOAuth2RefreshToken ?
                ((ExpiringOAuth2RefreshToken) refreshToken).getExpiration() :
                null;
        String userLogin = authentication.getName();
        serverTokenStore.storeRefreshToken(refreshToken.getValue(),
                SerializationUtils.serialize(refreshToken),
                SerializationUtils.serialize(authentication),
                tokenExpiry,
                userLogin);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        byte[] refreshTokenBytes = serverTokenStore.getRefreshTokenByTokenValue(tokenValue);
        return refreshTokenBytes != null ? SerializationUtils.deserialize(refreshTokenBytes) : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        byte[] authenticationBytes = serverTokenStore.getAuthenticationByRefreshTokenValue(token.getValue());
        return authenticationBytes != null ? SerializationUtils.deserialize(authenticationBytes) : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        serverTokenStore.removeRefreshToken(token.getValue());
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        serverTokenStore.removeAccessTokenUsingRefreshToken(refreshToken.getValue());
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
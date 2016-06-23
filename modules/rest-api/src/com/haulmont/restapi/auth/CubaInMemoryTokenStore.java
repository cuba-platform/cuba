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
import com.haulmont.restapi.config.RestApiConfig;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Token store is temporarily located at 'web' or 'portal' layer. It will be soon moved to the middleware.
 */
public class CubaInMemoryTokenStore extends InMemoryTokenStore {

    protected Map<OAuth2Authentication, UUID> authenticationToSessionStore = new HashMap<>();

    protected static final Logger log = LoggerFactory.getLogger(CubaInMemoryTokenStore.class);

    @Inject
    protected RestApiConfig restApiConfig;

    @Inject
    protected LoginService loginService;

    @Inject
    protected GlobalConfig globalConfig;

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        super.storeAccessToken(token, authentication);
        processSession(authentication);
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        OAuth2Authentication authentication = super.readAuthentication(token);
        if (authentication != null) {
            processSession(authentication);
        }
        return authentication;
    }

    protected void processSession(OAuth2Authentication authentication) {
        UUID sessionId = authenticationToSessionStore.get(authentication);

        if (sessionId == null) {
            Map<String, String> userAuthenticationDetails = (Map<String, String>) authentication.getUserAuthentication().getDetails();
            //sessionId parameter is put in the CubaUserAuthenticationProvider
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
                session = loginService.loginTrusted(username, restApiConfig.getTrustedClientPassword(), globalConfig.getAvailableLocales().values().iterator().next());
            } catch (LoginException e) {
                throw new OAuth2Exception("Cannot login to the middleware");
            }
        }

        if (session != null) {
            authenticationToSessionStore.put(authentication, session.getId());
            AppContext.setSecurityContext(new SecurityContext(session));
        }
    }

    //todo MG test token removal
    @Override
    public void removeAccessToken(String tokenValue) {
        OAuth2Authentication authentication = readAuthentication(tokenValue);
        if (authentication != null) {
            UUID sessionId = authenticationToSessionStore.remove(authentication);
            UserSession session = loginService.getSession(sessionId);
            if (session != null) {
                AppContext.setSecurityContext(new SecurityContext(sessionId));
                loginService.logout();
                AppContext.setSecurityContext(null);
            }
        }
        super.removeAccessToken(tokenValue);
    }
}

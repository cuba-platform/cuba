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

package com.haulmont.restapi.auth;

import com.haulmont.cuba.security.global.UserSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Bean that is used for programmatic access token generation.
 */
public interface OAuthTokenIssuer {
    /**
     * Issue token for principal.
     *
     * @param login       an existing user login
     * @param locale      locale
     * @param loginParams params that are passed to login mechanism
     * @return result with logged in user session and newly generated OAuth2 access token
     * @throws BadCredentialsException in case of user is now allowed to use REST-API or middleware
     *                                 throws {@link com.haulmont.cuba.security.global.LoginException} during login
     */
    OAuth2AccessTokenResult issueToken(String login, Locale locale, Map<String, Object> loginParams);

    /**
     * Issue token for principal.
     *
     * @param tokenRequest login and token parameters
     * @return result with logged in user session and newly generated OAuth2 access token
     * @throws BadCredentialsException in case of user is now allowed to use REST-API or middleware
     *                                 throws {@link com.haulmont.cuba.security.global.LoginException} during login
     */
    OAuth2AccessTokenResult issueToken(OAuth2AccessTokenRequest tokenRequest);

    /**
     * Result of programmatic access token generation.
     */
    class OAuth2AccessTokenResult {
        private final UserSession userSession;
        private final OAuth2AccessToken accessToken;

        public OAuth2AccessTokenResult(UserSession userSession, OAuth2AccessToken accessToken) {
            this.userSession = userSession;
            this.accessToken = accessToken;
        }

        public UserSession getUserSession() {
            return userSession;
        }

        public OAuth2AccessToken getAccessToken() {
            return accessToken;
        }
    }

    class OAuth2AccessTokenRequest {
        private String login;
        private Locale locale;
        private Map<String, Object> loginParams = Collections.emptyMap();
        private Map<String, String> tokenDetails = Collections.emptyMap();

        public OAuth2AccessTokenRequest() {
        }

        public OAuth2AccessTokenRequest(Map<String, Object> loginParams) {
            this.loginParams = loginParams;
        }

        public Map<String, Object> getLoginParams() {
            return loginParams;
        }

        public Map<String, String> getTokenDetails() {
            return tokenDetails;
        }

        public void setLoginParams(Map<String, Object> loginParams) {
            this.loginParams = loginParams;
        }

        public void setTokenDetails(Map<String, String> tokenDetails) {
            this.tokenDetails = tokenDetails;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public Locale getLocale() {
            return locale;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }
    }
}
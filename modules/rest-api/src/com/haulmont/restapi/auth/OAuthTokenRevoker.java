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

import com.haulmont.cuba.core.global.Events;
import com.haulmont.restapi.events.OAuthTokenRevokedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Bean that is used for access token revocation
 */
@Component("cuba_OauthTokenRevoker")
public class OAuthTokenRevoker {
    protected static final Logger log = LoggerFactory.getLogger(OAuthTokenRevoker.class);

    @Inject
    protected TokenStore tokenStore;
    @Inject
    protected Events events;

    @Nullable
    public OAuth2AccessToken revokeToken(String token, Authentication clientAuth) {
        log.debug("revokeToken: token = {}, clientAuth = {}", token, clientAuth);
        return revokeAccessToken(token, clientAuth, TokenRevocationInitiator.CLIENT);
    }

    @Nullable
    public OAuth2AccessToken revokeToken(String token) {
        log.debug("revokeToken: token = {} without clientAuth", token);
        return revokeAccessToken(token, null, TokenRevocationInitiator.SERVER);
    }

    @Nullable
    protected OAuth2AccessToken revokeAccessToken(String token, @Nullable Authentication clientAuth,
                                                  TokenRevocationInitiator revocationInitiator) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken != null) {
            OAuth2Authentication authToRevoke = tokenStore.readAuthentication(accessToken);

            if (revocationInitiator == TokenRevocationInitiator.CLIENT) {
                checkIfTokenIsIssuedToClient(clientAuth, authToRevoke);
            }

            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
            tokenStore.removeAccessToken(accessToken);
            log.debug("Access token removed: {}", token);

            if (events != null) {
                events.publish(new OAuthTokenRevokedEvent(accessToken, revocationInitiator));
            }

            return accessToken;
        }

        log.debug("No access token {} found in the token store", token);
        return null;
    }

    protected void checkIfTokenIsIssuedToClient(Authentication clientAuth,
                                                OAuth2Authentication authToRevoke) {
        String requestingClientId = clientAuth.getName();
        String tokenClientId = authToRevoke.getOAuth2Request().getClientId();
        if (!requestingClientId.equals(tokenClientId)) {
            log.debug("Revoke FAILED: requesting client = {}, token's client = {}", requestingClientId, tokenClientId);
            throw new InvalidGrantException("Cannot revoke tokens issued to other clients");
        }
    }
}
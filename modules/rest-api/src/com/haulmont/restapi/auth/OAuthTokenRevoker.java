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
import com.haulmont.restapi.common.RestTokenMasker;
import com.haulmont.restapi.events.OAuthTokenRevokedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
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

    @Inject
    protected RestTokenMasker tokenMasker;

    @Nullable
    public String revokeAccessToken(String token, Authentication clientAuth) {
        log.debug("revokeAccessToken: token = {}, clientAuth = {}", tokenMasker.maskToken(token), clientAuth);
        return revokeAccessToken(token, clientAuth, TokenRevocationInitiator.CLIENT);
    }

    @Nullable
    public String revokeAccessToken(String token) {
        log.debug("revokeAccessToken: token = {} without clientAuth", tokenMasker.maskToken(token));
        return revokeAccessToken(token, null, TokenRevocationInitiator.SERVER);
    }

    @Nullable
    protected String revokeAccessToken(String token, @Nullable Authentication clientAuth,
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
            log.debug("Access token removed: {}", tokenMasker.maskToken(token));

            if (events != null) {
                events.publish(new OAuthTokenRevokedEvent(accessToken, revocationInitiator));
            }

            return accessToken.getValue();
        }

        log.debug("No access token {} found in the token store", tokenMasker.maskToken(token));
        return null;
    }

    @Nullable
    public String revokeRefreshToken(String tokenValue, Authentication clientAuth) {
        OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(tokenValue);
        if (refreshToken != null) {
            OAuth2Authentication authToRevoke = tokenStore.readAuthenticationForRefreshToken(refreshToken);
            checkIfTokenIsIssuedToClient(clientAuth, authToRevoke);
            tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
            tokenStore.removeRefreshToken(refreshToken);
            log.debug("Successfully removed refresh token {} (and any associated access token).", tokenMasker.maskToken(refreshToken.getValue()));
            return refreshToken.getValue();
        }

        log.debug("No refresh token {} found in the token store.", tokenMasker.maskToken(tokenValue));
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
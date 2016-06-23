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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 */
@Component
public class OAuthTokenRevoker {
    private static final Logger log = LoggerFactory.getLogger(OAuthTokenRevoker.class);

    @Inject
    private TokenStore tokenStore;

    public boolean revokeToken(final String token, final String tokenHint, final Authentication clientAuth) {
        log.debug("revokeToken; token = {}, tokenHint = {}, clientAuth = {}", token, tokenHint, clientAuth);

        // Check the refresh_token store first. Fall back to the access token store if we don't
        // find anything. See RFC 7009, Sec 2.1: https://tools.ietf.org/html/rfc7009#section-2.1
        if (tokenHint != null && tokenHint.equals("refresh_token")) {
            return revokeRefreshToken(token, clientAuth) || revokeAccessToken(token, clientAuth);
        }

        // The user didn't hint that this is a refresh token, so it MAY be an access
        // token. If we don't find an access token... check if it's a refresh token.
        return revokeAccessToken(token, clientAuth) || revokeRefreshToken(token, clientAuth);
    }

    private boolean revokeRefreshToken(final String token, final Authentication clientAuth) {
        final OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(token);
        if (refreshToken != null) {
            log.debug("Found refresh token {}.", token);
            final OAuth2Authentication authToRevoke = tokenStore.readAuthenticationForRefreshToken(refreshToken);
            checkIfTokenIsIssuedToClient(clientAuth, authToRevoke);
            tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
            tokenStore.removeRefreshToken(refreshToken);
            log.debug("Successfully removed refresh token {} (and any associated access token).", refreshToken);
            return true;
        }

        log.debug("No refresh token {} found in the token store.", token);
        return false;
    }

    private boolean revokeAccessToken(final String token, final Authentication clientAuth) {
        final OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken != null) {
            log.debug("Found access token {}.", token);
            final OAuth2Authentication authToRevoke = tokenStore.readAuthentication(accessToken);
            checkIfTokenIsIssuedToClient(clientAuth, authToRevoke);
            if (accessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(accessToken.getRefreshToken());
            }
            tokenStore.removeAccessToken(accessToken);
            log.debug("Successfully removed access token {} (and any associated refresh token).", token);
            return true;
        }

        log.debug("No access token {} found in the token store.", token);
        return false;
    }

    private void checkIfTokenIsIssuedToClient(final Authentication clientAuth,
                                              final OAuth2Authentication authToRevoke) {
        final String requestingClientId = clientAuth.getName();
        final String tokenClientId = authToRevoke.getOAuth2Request().getClientId();
        if (!requestingClientId.equals(tokenClientId)) {
            log.debug("Revoke FAILED: requesting client = {}, token's client = {}.", requestingClientId, tokenClientId);
            throw new InvalidGrantException("Cannot revoke tokens issued to other clients.");
        }
        log.debug("OK to revoke; token is issued to client \"{}\"", requestingClientId);
    }
}

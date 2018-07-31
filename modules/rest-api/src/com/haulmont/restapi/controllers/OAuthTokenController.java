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

package com.haulmont.restapi.controllers;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.restapi.auth.OAuthTokenRevoker;
import com.haulmont.restapi.common.RestTokenMasker;
import com.haulmont.restapi.events.OAuthTokenRevokedResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.security.Principal;

/**
 * REST controller that is used for token revocation
 */
@RestController("cuba_OAuthTokenController")
public class OAuthTokenController {

    private static final Logger log = LoggerFactory.getLogger(OAuthTokenController.class);

    @Inject
    protected OAuthTokenRevoker oAuthTokenRevoker;

    @Inject
    protected RestTokenMasker tokenMasker;

    @Inject
    protected Events events;

    @PostMapping("/v2/oauth/revoke")
    public ResponseEntity revokeToken(@RequestParam("token") String token,
                                      @RequestParam(value = "token_type_hint", required = false) String tokenTypeHint,
                                      Principal principal) {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }
        log.info("POST /oauth/revoke; token = {}, token_type_hint = {}", tokenMasker.maskToken(token), tokenTypeHint);

        String revokedTokenValue = null;
        if ("refresh_token".equals(tokenTypeHint)) {
            revokedTokenValue = oAuthTokenRevoker.revokeRefreshToken(token, (Authentication) principal);
            if (revokedTokenValue == null) {
                revokedTokenValue = oAuthTokenRevoker.revokeAccessToken(token, (Authentication) principal);
            }
        }

        if (revokedTokenValue == null) {
            revokedTokenValue = oAuthTokenRevoker.revokeAccessToken(token, (Authentication) principal);
            if (revokedTokenValue == null) {
                revokedTokenValue = oAuthTokenRevoker.revokeRefreshToken(token, (Authentication) principal);
            }
        }

        if (revokedTokenValue == null) {
            log.debug("No token with value {} was revoked.", tokenMasker.maskToken(token));
        }

        if (events != null) {
            OAuthTokenRevokedResponseEvent event = new OAuthTokenRevokedResponseEvent(token, revokedTokenValue);

            events.publish(event);

            if (event.getResponseEntity() != null) {
                return event.getResponseEntity();
            }
        }

        return ResponseEntity.ok().build();
    }
}
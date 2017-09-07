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

import com.haulmont.restapi.auth.OAuthTokenRevoker;
import com.haulmont.restapi.events.OAuthTokenRevokedResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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
@RestController
public class OAuthTokenController {

    private final Logger log = LoggerFactory.getLogger(OAuthTokenController.class);

    @Inject
    protected OAuthTokenRevoker oAuthTokenRevoker;
    @Inject
    protected ApplicationEventPublisher eventPublisher;

    @PostMapping("/v2/oauth/revoke")
    public ResponseEntity revokeToken(@RequestParam("token") String token, Principal principal) {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }
        log.info("POST /oauth/revoke; token = {},", token);
        // Invalid token revocations (token does not exist) must respond with 200 code
        OAuth2AccessToken revokedToken = oAuthTokenRevoker.revokeToken(token, (Authentication) principal);
        if (revokedToken == null) {
            log.debug("No token with value {} was revoked.", token);
        }

        if (eventPublisher != null) {
            OAuthTokenRevokedResponseEvent event = new OAuthTokenRevokedResponseEvent(token, revokedToken);

            eventPublisher.publishEvent(event);

            if (event.getResponseEntity() != null) {
                return event.getResponseEntity();
            }
        }

        return ResponseEntity.ok().build();
    }
}
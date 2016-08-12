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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.security.Principal;

/**
 * REST controller that is used for token revocation
 */
@RestController
public class OAuthTokenController {

    protected static final Logger log = LoggerFactory.getLogger(OAuthTokenController.class);

    @Inject
    private OAuthTokenRevoker oAuthTokenRevoker;

    @RequestMapping(value = "/api/oauth/revoke", method = RequestMethod.POST)
    public ResponseEntity<Void> revokeToken(@RequestParam("token") String token,
                                            @RequestParam(value = "token_hint", required = false) String tokenHint,
                                            Principal principal) {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }
        log.info("POST /oauth/revoke; token = {}, tokenHint = {}", token, tokenHint);
        // Invalid token revocations (token does not exist) still respond
        // with HTTP 200. Still, log the result anyway for posterity.
        // See: https://tools.ietf.org/html/rfc7009#section-2.2
        if (!oAuthTokenRevoker.revokeToken(token, tokenHint, (Authentication) principal)) {
            log.debug("No token with value {} was revoked.", token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

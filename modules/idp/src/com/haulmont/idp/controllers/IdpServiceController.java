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

package com.haulmont.idp.controllers;

import com.haulmont.cuba.security.app.IdpService;
import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.idp.IdpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController("cuba_IdpServiceController")
@RequestMapping(value = "service", produces = "application/json; charset=UTF-8")
public class IdpServiceController {
    private final Logger log = LoggerFactory.getLogger(IdpServiceController.class);

    @Inject
    protected IdpService idpService;

    @Inject
    protected IdpConfig idpConfig;

    @RequestMapping(value = "activate", method = RequestMethod.POST)
    protected IdpSession activateServiceProviderTicket(@RequestParam("serviceProviderTicket") String serviceProviderTicket,
                                                       @RequestParam("trustedServicePassword") String trustedServicePassword,
                                                       HttpServletResponse response) {
        if (!Objects.equals(idpConfig.getTrustedServicePassword(), trustedServicePassword)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            log.warn("Incorrect trusted client password has been passed {}", trustedServicePassword);
            return null;
        }

        log.debug("Activate service provider ticket {}", serviceProviderTicket);

        IdpSession idpSession = idpService.activateServiceProviderTicket(serviceProviderTicket);

        if (idpSession == null) {
            log.debug("IDP Session not found for ticket {}", serviceProviderTicket);
            response.setStatus(HttpStatus.GONE.value());
            return null;
        }

        return idpSession;
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    protected IdpSession getSession(@RequestParam("idpSessionId") String idpSessionId,
                                    @RequestParam("trustedServicePassword") String trustedServicePassword,
                                    HttpServletResponse response) {
        if (!Objects.equals(idpConfig.getTrustedServicePassword(), trustedServicePassword)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            log.warn("Incorrect trusted client password has been passed {}", trustedServicePassword);
            return null;
        }

        log.debug("Get IDP session {}", idpSessionId);

        IdpSession idpSession = idpService.getSession(idpSessionId);
        if (idpSession == null) {
            log.debug("IDP Session not found for id {}", idpSessionId);
            response.setStatus(HttpStatus.GONE.value());
            return null;
        }

        return idpSession;
    }

    @RequestMapping(value = "ping", method = RequestMethod.POST)
    protected void pingSession(@RequestParam("idpSessionId") String idpSessionId,
                               @RequestParam("trustedServicePassword") String trustedServicePassword,
                               HttpServletResponse response) {
        if (!Objects.equals(idpConfig.getTrustedServicePassword(), trustedServicePassword)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            log.warn("Incorrect trusted client password has been passed {}", trustedServicePassword);
            return;
        }

        log.debug("Ping IDP session {}", idpSessionId);

        IdpSession idpSession = idpService.getSession(idpSessionId);
        if (idpSession == null) {
            log.debug("IDP Session not found for id {}", idpSessionId);
            response.setStatus(HttpStatus.GONE.value());
        }
    }
}
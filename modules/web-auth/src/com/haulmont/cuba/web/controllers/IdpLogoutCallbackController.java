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

package com.haulmont.cuba.web.controllers;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.idp.IdpService;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Performs logout of user session when IDP session is logged out.
 * IDP server requests this controller to logout related sessions in a service.
 */
@Controller("cuba_IdpLogoutCallbackController")
@RequestMapping("/idpc")
public class IdpLogoutCallbackController {

    private final Logger log = LoggerFactory.getLogger(IdpLogoutCallbackController.class);

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected TrustedClientService trustedClientService;

    @Inject
    protected IdpService idpService;

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public void logout(@RequestParam(name = "idpSessionId") String idpSessionId,
                       @RequestParam(name = "trustedServicePassword") String trustedServicePassword,
                       HttpServletResponse response) {

        if (!webAuthConfig.getExternalAuthentication() || Strings.isNullOrEmpty(webAuthConfig.getIdpBaseURL())) {
            log.warn("IDP options is not set, but logout callback url is requested");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        if (!Objects.equals(webAuthConfig.getIdpTrustedServicePassword(), trustedServicePassword)) {
            log.warn("Incorrect trusted service password passed from IDP");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        UserSession systemSession;
        try {
            systemSession = trustedClientService.getSystemSession(webAuthConfig.getTrustedClientPassword());
        } catch (LoginException e) {
            log.error("Unable to obtain system session", e);
            return;
        }

        log.trace("Logout user session by IDP session");

        AppContext.withSecurityContext(new SecurityContext(systemSession), () ->
                idpService.logoutUserSession(idpSessionId)
        );
    }
}
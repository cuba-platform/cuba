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

package com.haulmont.cuba.web.auth;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.idp.IdpService;
import com.haulmont.cuba.web.security.idp.BaseIdpSessionFilter;
import com.haulmont.cuba.web.security.idp.IdpSessionPingConnector;
import com.haulmont.cuba.web.security.idp.IdpSessionPrincipal;
import com.haulmont.cuba.web.sys.RequestContext;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.security.Principal;
import java.util.Locale;

import static com.haulmont.cuba.web.security.idp.IdpSessionPrincipal.IDP_SESSION_ATTRIBUTE;

/**
 * @deprecated Use "cuba.web.idp.enabled" application property instead.
 *
 * Authentication provider that supports CUBA IDP web service.
 */
@Deprecated
public class IdpAuthProvider extends BaseIdpSessionFilter implements CubaAuthProvider {
    private final Logger log = LoggerFactory.getLogger(IdpAuthProvider.class);

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected IdpSessionPingConnector idpSessionPingConnector;

    public IdpAuthProvider() {
        log.warn("IdpAuthProvider mechanism is DEPRECATED, use 'cuba.web.idp.enabled' application property instead");
    }

    @Override
    public void authenticate(String login, String password, Locale locale) throws LoginException {
        throw new UnsupportedOperationException("IdpAuthProvider does not support login using login form");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void userSessionLoggedIn(UserSession session) {
        VaadinRequest currentRequest = VaadinService.getCurrentRequest();

        if (currentRequest != null) {
            Principal principal = currentRequest.getUserPrincipal();

            if (principal instanceof IdpSessionPrincipal) {
                IdpSession idpSession = ((IdpSessionPrincipal) principal).getIdpSession();
                session.setAttribute(IdpService.IDP_USER_SESSION_ATTRIBUTE, idpSession.getId());
            }
        }
    }

    @Override
    public void pingUserSession(UserSession session) {
        String idpSessionId = session.getAttribute(IdpService.IDP_USER_SESSION_ATTRIBUTE);
        if (idpSessionId != null) {
            idpSessionPingConnector.pingIdpSessionServer(idpSessionId);
        }
    }

    @Override
    public String logout() {
        RequestContext requestContext = RequestContext.get();
        if (requestContext != null) {
            requestContext.getSession().removeAttribute(IDP_SESSION_ATTRIBUTE);
        }

        String idpBaseURL = webIdpConfig.getIdpBaseURL();
        if (Strings.isNullOrEmpty(idpBaseURL)) {
            log.error("Application property cuba.web.idp.url is not set");
            return null;
        }

        return getIdpLogoutUrl();
    }

    @Override
    public void destroy() {
    }
}
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

package com.haulmont.cuba.web.security.idp;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.idp.IdpService;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.security.ExternalUserCredentials;
import com.haulmont.cuba.web.security.events.AppLoggedOutEvent;
import com.haulmont.cuba.web.security.events.AppStartedEvent;
import com.haulmont.cuba.web.security.events.SessionHeartbeatEvent;
import com.haulmont.cuba.web.security.events.UserSessionSubstitutedEvent;
import com.haulmont.cuba.web.sys.RequestContext;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.security.Principal;
import java.util.Locale;

import static com.haulmont.cuba.web.security.idp.IdpSessionPrincipal.IDP_SESSION_ATTRIBUTE;

@ConditionalOnAppProperty(property = "cuba.web.idp.enabled", value = "true")
@ConditionalOnAppProperty(property = "cuba.web.externalAuthentication", value = "false", defaultValue = "false")
@Component("cuba_IdpLoginLifecycleManager")
public class IdpLoginLifecycleManager {

    private static final Logger log = LoggerFactory.getLogger(IdpLoginLifecycleManager.class);

    @Inject
    protected WebIdpConfig webIdpConfig;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected IdpSessionPingConnector idpSessionPingConnector;

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void onAppStarted(AppStartedEvent event) throws LoginException {
        Connection connection = event.getApp().getConnection();
        // can be already authenticated by another event listener
        if (webIdpConfig.getIdpEnabled()
                && !connection.isAuthenticated()) {
            VaadinRequest currentRequest = VaadinService.getCurrentRequest();

            if (currentRequest != null) {
                Principal principal = currentRequest.getUserPrincipal();

                if (principal instanceof IdpSessionPrincipal) {
                    IdpSession idpSession = ((IdpSessionPrincipal) principal).getIdpSession();

                    Locale locale = event.getApp().getLocale();

                    ExternalUserCredentials credentials = new ExternalUserCredentials(principal.getName(), locale);
                    credentials.setSessionAttributes(
                            ImmutableMap.of(
                                    IdpService.IDP_USER_SESSION_ATTRIBUTE,
                                    idpSession.getId()
                            ));

                    connection.login(credentials);
                }
            }
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void onSessionSubstituted(UserSessionSubstitutedEvent event) {
        if (webIdpConfig.getIdpEnabled()) {
            String idpSessionId = IdpService.IDP_USER_SESSION_ATTRIBUTE;
            if (event.getSourceSession().getAttribute(idpSessionId) != null) {
                event.getSubstitutedSession().setAttribute(IdpService.IDP_USER_SESSION_ATTRIBUTE, idpSessionId);
            }
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void onAppLoggedOut(AppLoggedOutEvent event) {
        if (webIdpConfig.getIdpEnabled()
                && event.getLoggedOutSession() != null
                && event.getLoggedOutSession().getAttribute(IdpService.IDP_USER_SESSION_ATTRIBUTE) != null
                && event.getRedirectUrl() == null) {

            RequestContext requestContext = RequestContext.get();
            if (requestContext != null) {
                requestContext.getSession().removeAttribute(IDP_SESSION_ATTRIBUTE);
            }

            String idpBaseURL = webIdpConfig.getIdpBaseURL();
            if (!Strings.isNullOrEmpty(idpBaseURL)) {
                event.setRedirectUrl(getIdpLogoutUrl());
            } else {
                log.error("Application property cuba.web.idp.url is not set");
            }
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 10)
    @EventListener
    protected void pingIdpSession(SessionHeartbeatEvent event) {
        Connection connection = event.getSource().getConnection();

        if (webIdpConfig.getIdpEnabled()
                && connection.isAuthenticated()) {
            UserSession session = connection.getSessionNN();

            String idpSessionId = session.getAttribute(IdpService.IDP_USER_SESSION_ATTRIBUTE);
            if (idpSessionId != null) {
                idpSessionPingConnector.pingIdpSessionServer(idpSessionId);
            }
        }
    }

    protected String getIdpLogoutUrl() {
        String idpBaseURL = webIdpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpRedirectUrl = idpBaseURL + "logout?sp=" +
                URLEncodeUtils.encodeUtf8(globalConfig.getWebAppUrl());

        return idpRedirectUrl;
    }
}
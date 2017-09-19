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

package com.haulmont.cuba.web.auth;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.idp.IdpService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Authentication provider that supports CUBA IDP web service.
 */
public class IdpAuthProvider implements CubaAuthProvider {
    public static final String IDP_SESSION_ATTRIBUTE = "IDP_SESSION";
    public static final String IDP_SESSION_LOCK_ATTRIBUTE = "IDP_SESSION_LOCK";
    public static final String IDP_TICKET_REQUEST_PARAM = "idp_ticket";

    private final Logger log = LoggerFactory.getLogger(IdpAuthProvider.class);

    protected Lock sessionCheckLock = new ReentrantLock();

    @Inject
    protected WebAuthConfig webAuthConfig;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected AuthenticationService authenticationService;

    @Override
    public void authenticate(String login, String password, Locale locale) throws LoginException {
        throw new UnsupportedOperationException("IdpAuthProvider does not support login using login form");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // send static files without authentication
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (StringUtils.startsWith(httpRequest.getRequestURI(), httpRequest.getContextPath() + "/VAADIN/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String idpBaseURL = webAuthConfig.getIdpBaseURL();
        if (Strings.isNullOrEmpty(idpBaseURL)) {
            log.error("Application property cuba.web.idp.url is not set");
            httpResponse.setStatus(500);
            return;
        }

        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }

        String requestUrl = httpRequest.getRequestURL().toString();
        if (StringUtils.startsWith(requestUrl, idpBaseURL)) {
            chain.doFilter(httpRequest, response);
            return;
        }

        HttpSession session = httpRequest.getSession(true);
        Lock sessionLock = (Lock) session.getAttribute(IDP_SESSION_LOCK_ATTRIBUTE);
        if (sessionLock == null) {
            sessionCheckLock.lock();
            try {
                sessionLock = (Lock) session.getAttribute(IDP_SESSION_LOCK_ATTRIBUTE);
                if (sessionLock == null) {
                    sessionLock = new ReentrantLock();
                    session.setAttribute(IDP_SESSION_LOCK_ATTRIBUTE, sessionLock);
                }
            } finally {
                sessionCheckLock.unlock();
            }
        }

        IdpSession boundIdpSession;
        sessionLock.lock();

        try {
            session.getAttribute(IDP_SESSION_LOCK_ATTRIBUTE);
        } catch (IllegalStateException e) {
            // Someone might have invalidated the session between fetching the lock and acquiring it.
            sessionLock.unlock();

            log.debug("Invalidated session {}", session.getId());
            httpResponse.sendRedirect(httpRequest.getRequestURL().toString());
            return;
        }

        try {
            if ("GET".equals(httpRequest.getMethod())
                    && httpRequest.getParameter(IDP_TICKET_REQUEST_PARAM) != null) {
                String idpTicket = httpRequest.getParameter(IDP_TICKET_REQUEST_PARAM);

                IdpSession idpSession;
                try {
                    idpSession = getIdpSession(idpTicket);
                } catch (IdpActivationException e) {
                    log.error("Unable to obtain IDP session by ticket", e);
                    httpResponse.setStatus(500);
                    return;
                }

                if (idpSession == null) {
                    log.warn("Used old IDP ticket {}, send redirect", idpTicket);
                    // used old ticket, send redirect
                    httpResponse.sendRedirect(getIdpRedirectUrl());
                    return;
                }

                session.invalidate();

                session = httpRequest.getSession(true);
                session.setAttribute(IDP_SESSION_LOCK_ATTRIBUTE, sessionLock);
                session.setAttribute(IDP_SESSION_ATTRIBUTE, idpSession);

                log.debug("IDP session {} obtained, redirect to application", idpSession);

                // redirect to application without parameters
                httpResponse.sendRedirect(httpRequest.getRequestURL().toString());
                return;
            }

            if (session.getAttribute(IDP_SESSION_ATTRIBUTE) == null) {
                if ("GET".equals(httpRequest.getMethod())
                        && !StringUtils.startsWith(httpRequest.getRequestURI(), httpRequest.getContextPath() + "/PUSH")) {
                    httpResponse.sendRedirect(getIdpRedirectUrl());
                }
                return;
            }

            boundIdpSession = (IdpSession) session.getAttribute(IDP_SESSION_ATTRIBUTE);
        } finally {
            sessionLock.unlock();
        }

        HttpServletRequest authenticatedRequest = new IdpServletRequestWrapper(httpRequest,
                new IdpSessionPrincipalImpl(boundIdpSession));

        chain.doFilter(authenticatedRequest, response);
    }

    @Nullable
    protected IdpSession getIdpSession(String idpTicket) throws IdpActivationException {
        String idpBaseURL = webAuthConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpTicketActivateUrl = idpBaseURL + "service/activate";

        HttpPost httpPost = new HttpPost(idpTicketActivateUrl);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("serviceProviderTicket", idpTicket),
                new BasicNameValuePair("trustedServicePassword", webAuthConfig.getIdpTrustedServicePassword())
        ), StandardCharsets.UTF_8);

        httpPost.setEntity(formEntity);

        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpClient client = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        String idpResponse;
        try {
            HttpResponse httpResponse = client.execute(httpPost);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 410) {
                // used old ticket
                return null;
            }

            if (statusCode != 200) {
                throw new IdpActivationException("Idp respond with status " + statusCode);
            }

            idpResponse = new BasicResponseHandler().handleResponse(httpResponse);
        } catch (IOException e) {
            throw new IdpActivationException(e);
        } finally {
            connectionManager.shutdown();
        }

        IdpSession session;
        try {
            session = new Gson().fromJson(idpResponse, IdpSession.class);
        } catch (JsonSyntaxException e) {
            throw new IdpActivationException("Unable to parse idp response", e);
        }

        return session;
    }

    protected String getIdpLogoutUrl() {
        String idpBaseURL = webAuthConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpRedirectUrl = idpBaseURL + "logout?sp=" +
                URLEncodeUtils.encodeUtf8(globalConfig.getWebAppUrl());

        return idpRedirectUrl;
    }

    protected String getIdpRedirectUrl() {
        String idpBaseURL = webAuthConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpRedirectUrl = idpBaseURL + "?sp=" +
                URLEncodeUtils.encodeUtf8(globalConfig.getWebAppUrl());

        return idpRedirectUrl;
    }

    @Override
    public void userSessionLoggedIn(UserSession session) {
        RequestContext requestContext = RequestContext.get();

        if (requestContext != null) {
            Principal principal = requestContext.getRequest().getUserPrincipal();

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
            pingIdpSessionServer(idpSessionId);
        }
    }

    protected void pingIdpSessionServer(String idpSessionId) {
        log.debug("Ping IDP session {}", idpSessionId);

        String idpBaseURL = webAuthConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpSessionPingUrl = idpBaseURL + "service/ping";

        HttpPost httpPost = new HttpPost(idpSessionPingUrl);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("idpSessionId", idpSessionId),
                new BasicNameValuePair("trustedServicePassword", webAuthConfig.getIdpTrustedServicePassword())
        ), StandardCharsets.UTF_8);

        httpPost.setEntity(formEntity);

        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpClient client = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        try {
            HttpResponse httpResponse = client.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 410) {
                // we have to logout user
                log.debug("IDP session is expired {}", idpSessionId);

                if (userSessionSource.checkCurrentUserSession()) {
                    authenticationService.logout();

                    UserSession userSession = userSessionSource.getUserSession();

                    throw new NoUserSessionException(userSession.getId());
                }
            }
            if (statusCode != 200) {
                log.warn("IDP respond status {} on session ping", statusCode);
            }
        } catch (IOException e) {
            log.warn("Unable to ping IDP {} session {}", idpSessionPingUrl, idpSessionId, e);
        } finally {
            connectionManager.shutdown();
        }
    }

    @Override
    public String logout() {
        RequestContext requestContext = RequestContext.get();
        if (requestContext != null) {
            requestContext.getSession().removeAttribute(IDP_SESSION_ATTRIBUTE);
        }

        String idpBaseURL = webAuthConfig.getIdpBaseURL();
        if (Strings.isNullOrEmpty(idpBaseURL)) {
            log.error("Application property cuba.web.idp.url is not set");
            return null;
        }

        return getIdpLogoutUrl();
    }

    @Override
    public void destroy() {
    }

    public static class IdpServletRequestWrapper extends HttpServletRequestWrapper {

        private final IdpSessionPrincipalImpl principal;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        public IdpServletRequestWrapper(HttpServletRequest request, IdpSessionPrincipalImpl principal) {
            super(request);
            this.principal = principal;
        }

        @Override
        public Principal getUserPrincipal() {
            return principal;
        }

        @Override
        public Locale getLocale() {
            if (principal.getLocale() != null) {
                return principal.getLocale();
            }

            return super.getLocale();
        }
    }

    public static class IdpSessionPrincipalImpl implements Principal, IdpSessionPrincipal {
        private final IdpSession idpSession;

        public IdpSessionPrincipalImpl(IdpSession idpSession) {
            this.idpSession = idpSession;
        }

        @Override
        public String getName() {
            return idpSession.getLogin();
        }

        @Override
        public IdpSession getIdpSession() {
            return idpSession;
        }

        @Nullable
        public Locale getLocale() {
            String locale = idpSession.getLocale();
            if (locale == null) {
                return null;
            }

            return Locale.forLanguageTag(locale);
        }
    }

    public static class IdpActivationException extends Exception {
        public IdpActivationException(String message) {
            super(message);
        }

        public IdpActivationException(Throwable cause) {
            super(cause);
        }

        public IdpActivationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public interface IdpSessionPrincipal {
        IdpSession getIdpSession();
    }
}
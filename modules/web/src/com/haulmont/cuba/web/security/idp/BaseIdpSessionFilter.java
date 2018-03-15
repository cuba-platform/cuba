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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.IdpSession;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.haulmont.cuba.core.sys.AppContext.withSecurityContext;
import static com.haulmont.cuba.web.security.idp.IdpSessionPrincipal.*;

public abstract class BaseIdpSessionFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(BaseIdpSessionFilter.class);

    protected Lock sessionCheckLock = new ReentrantLock();

    @Inject
    protected WebIdpConfig webIdpConfig;
    @Inject
    protected WebAuthConfig webAuthConfig;
    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected TrustedClientService trustedClientService;

    protected volatile String webAppUrl;

    protected String getWebAppUrl() {
        if (webAppUrl == null) {
            synchronized (this) {
                if (webAppUrl == null) {
                    UserSession systemSession;
                    try {
                        systemSession = trustedClientService.getSystemSession(webAuthConfig.getTrustedClientPassword());
                    } catch (LoginException e) {
                        throw new RuntimeException("Unable to get systemSession", e);
                    }

                    // webAppUrl can be overridden in DB, thus we need SecurityContext to obtain it from middleware
                    withSecurityContext(new SecurityContext(systemSession), () -> {
                        String webAppUrl = globalConfig.getWebAppUrl();
                        if (!webAppUrl.endsWith("/")) {
                            webAppUrl += "/";
                        }
                        this.webAppUrl = webAppUrl;
                    });
                }
            }
        }

        return this.webAppUrl;
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
        String idpBaseURL = webIdpConfig.getIdpBaseURL();
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
                    httpResponse.sendRedirect(getIdpRedirectUrl(httpRequest));
                    return;
                }

                session.invalidate();

                session = httpRequest.getSession(true);
                session.setAttribute(IDP_SESSION_LOCK_ATTRIBUTE, sessionLock);
                session.setAttribute(IDP_SESSION_ATTRIBUTE, idpSession);

                log.debug("IDP session {} obtained, redirect to application", idpSession);

                String redirectUrl;
                try {
                    redirectUrl = getRedirectUrlWithoutIdpTicket(httpRequest);
                } catch (URISyntaxException e) {
                    log.error("Unable to compose redirect URL", e);
                    httpResponse.setStatus(500);
                    return;
                }

                httpResponse.sendRedirect(redirectUrl);
                return;
            }

            if (session.getAttribute(IDP_SESSION_ATTRIBUTE) == null) {
                if ("GET".equals(httpRequest.getMethod())
                        && !StringUtils.startsWith(httpRequest.getRequestURI(), httpRequest.getContextPath() + "/PUSH")) {
                    httpResponse.sendRedirect(getIdpRedirectUrl(httpRequest));
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
        String idpBaseURL = webIdpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpTicketActivateUrl = idpBaseURL + "service/activate";

        HttpPost httpPost = new HttpPost(idpTicketActivateUrl);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("serviceProviderTicket", idpTicket),
                new BasicNameValuePair("trustedServicePassword", webIdpConfig.getIdpTrustedServicePassword())
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
        String idpBaseURL = webIdpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpRedirectUrl = idpBaseURL + "logout?sp=" +
                URLEncodeUtils.encodeUtf8(getWebAppUrl());

        return idpRedirectUrl;
    }

    protected String getIdpRedirectUrl(HttpServletRequest request) {
        String idpBaseURL = webIdpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }

        String idpRedirectUrl = idpBaseURL + "?sp=" +
                URLEncodeUtils.encodeUtf8(getRequestURL(request));

        return idpRedirectUrl;
    }

    protected String getRequestURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    protected String getRedirectUrlWithoutIdpTicket(HttpServletRequest httpRequest) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(httpRequest.getRequestURL().toString());
        String queryString = httpRequest.getQueryString();
        if (!Strings.isNullOrEmpty(queryString)) {
            Arrays.stream(URLEncodeUtils.decodeUtf8(queryString).split("&"))
                    .map(s -> s.split("="))
                    .filter(kvPair -> !kvPair[0].equals(IDP_TICKET_REQUEST_PARAM))
                    .forEach(kvPair -> uriBuilder.setParameter(kvPair[0], kvPair[1]));

        }
        return uriBuilder.build().toString();
    }
}
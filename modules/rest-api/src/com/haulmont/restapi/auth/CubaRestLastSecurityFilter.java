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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.UserInvocationContext;
import com.haulmont.restapi.common.RestAuthUtils;
import com.haulmont.restapi.common.RestTokenMasker;
import com.haulmont.restapi.events.AfterRestInvocationEvent;
import com.haulmont.restapi.events.BeforeRestInvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

/**
 * The last filter in the security filters chain. It does the following:
 *
 * <ul>
 *     <li>logs all REST API methods access</li>
 *     <li>parses the request locale and sets it to the {@link UserInvocationContext}</li>
 * </ul>
 */
public class CubaRestLastSecurityFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CubaRestLastSecurityFilter.class);

    @Inject
    protected Configuration configuration;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected Events events;

    @Inject
    protected RestAuthUtils restAuthUtils;

    @Inject
    protected RestTokenMasker restTokenMasker;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logRequest(request);
        parseRequestLocale(request);

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (events != null && authentication != null) {
                BeforeRestInvocationEvent beforeInvocationEvent = new BeforeRestInvocationEvent(authentication, request, response);
                events.publish(beforeInvocationEvent);

                boolean invocationPrevented = beforeInvocationEvent.isInvocationPrevented();

                try {
                    if (!invocationPrevented) {
                        chain.doFilter(request, response);
                    } else {
                        log.debug("REST API invocation prevented by BeforeRestInvocationEvent handler");
                    }
                } finally {
                    events.publish(new AfterRestInvocationEvent(authentication, request, response, invocationPrevented));
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error("Error during REST API call", e);
            ((HttpServletResponse) response).sendError(500);
        }
    }

    /**
     * Method logs REST API method invocation
     */
    protected void logRequest(ServletRequest request) {
        if (log.isDebugEnabled()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String tokenValue = "";
                if (authentication instanceof CubaAnonymousAuthenticationToken) {
                    tokenValue = "anonymous";
                }
                if (authentication.getDetails() instanceof OAuth2AuthenticationDetails){
                    tokenValue = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
                }
                log.debug("REST API request [{}] {} {} {}",
                        restTokenMasker.maskToken(tokenValue),
                        ((HttpServletRequest) request).getMethod(),
                        getRequestURL((HttpServletRequest) request),
                        request.getRemoteAddr());
            }
        }
    }

    /**
     * Method parses the request locale and sets it to the {@link UserInvocationContext}
     */
    protected void parseRequestLocale(ServletRequest request) {
        Locale locale = restAuthUtils.extractLocaleFromRequestHeader((HttpServletRequest) request);
        if (locale != null) {
            SecurityContext securityContext = AppContext.getSecurityContext();
            if (securityContext != null) {
                UUID sessionId = securityContext.getSessionId();
                if (sessionId != null) {
                    UserInvocationContext.setRequestScopeInfo(sessionId, locale, null, null, null);
                }
            }
        }
    }

    protected String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL() +
                (!Strings.isNullOrEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "");
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
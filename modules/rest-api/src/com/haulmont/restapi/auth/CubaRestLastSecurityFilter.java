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
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.UserInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
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

    private final Logger log = LoggerFactory.getLogger(CubaRestLastSecurityFilter.class);

    @Inject
    protected Configuration configuration;
    @Inject
    protected MessageTools messageTools;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logRequest(request);
        parseRequestLocale(request);
        chain.doFilter(request, response);
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
                        tokenValue,
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
        //Take the locale value either from the 'Accept-Language' http header or take the default one
        Locale locale = null;
        if (!Strings.isNullOrEmpty(((HttpServletRequest) request).getHeader("Accept-Language"))) {
            Locale requestLocale = request.getLocale();
            Map<String, Locale> availableLocales = configuration.getConfig(GlobalConfig.class).getAvailableLocales();
            if (availableLocales.values().contains(requestLocale)) {
                locale = requestLocale;
            }
        }
        if (locale == null) {
            locale = messageTools.getDefaultLocale();
        }
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext != null) {
            UUID sessionId = securityContext.getSessionId();
            if (sessionId != null) {
                UserInvocationContext.setRequestScopeInfo(sessionId, locale, null, null, null);
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
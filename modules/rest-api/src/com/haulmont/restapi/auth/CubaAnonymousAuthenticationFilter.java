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

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.restapi.common.RestParseUtils;
import com.haulmont.restapi.config.RestApiConfig;
import com.haulmont.restapi.config.RestServicesConfiguration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This filter is used for anonymous access to CUBA REST API. If no Authorization header presents in the request and
 * if {@link RestApiConfig#getRestAnonymousEnabled()} is true, then the anonymous user session will be set to the
 * {@link SecurityContext} and the request will be authenticated. This filter must be invoked after the
 * {@link org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter}
 */
public class CubaAnonymousAuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CubaAnonymousAuthenticationFilter.class);

    @Inject
    protected RestApiConfig restApiConfig;

    @Inject
    protected RestServicesConfiguration restServicesConfiguration;

    @Inject
    protected TrustedClientService trustedClientService;

    @Inject
    protected RestParseUtils restParseUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (restApiConfig.getRestAnonymousEnabled() || isAnonymousServiceMethodInvoked(request)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserSession anonymousSession;
                try {
                    anonymousSession = trustedClientService.getAnonymousSession(restApiConfig.getTrustedClientPassword());
                } catch (LoginException e) {
                    throw new RuntimeException("Unable to obtain anonymous session for REST", e);
                }

                CubaAnonymousAuthenticationToken anonymousAuthenticationToken =
                        new CubaAnonymousAuthenticationToken("anonymous", AuthorityUtils.createAuthorityList("ROLE_CUBA_ANONYMOUS"));
                SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);
                AppContext.setSecurityContext(new SecurityContext(anonymousSession));
            } else {
                log.debug("SecurityContextHolder not populated with cuba anonymous token, as it already contained: '{}'",
                        SecurityContextHolder.getContext().getAuthentication());
            }
        } else {
            log.trace("Anonymous access for CUBA REST API is disabled");
        }
        chain.doFilter(request, response);
    }

    protected boolean isAnonymousServiceMethodInvoked(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            String pathInfo = ((HttpServletRequest) request).getPathInfo();
            if (pathInfo != null && pathInfo.startsWith("/v2/services/")) {
                String[] parts = pathInfo.split("/");
                if (parts.length != 5) return false;
                String serviceName = parts[3];
                String methodName = parts[4];
                List<String> methodParamNames;
                if ("GET".equals(((HttpServletRequest) request).getMethod())) {
                    methodParamNames = Collections.list(request.getParameterNames());
                } else if ("POST".equals(((HttpServletRequest) request).getMethod())) {
                    try {
                        String json = IOUtils.toString(request.getReader());
                        Map<String, String> paramsMap = restParseUtils.parseParamsJson(json);
                        methodParamNames = new ArrayList<>(paramsMap.keySet());
                    } catch (IOException e) {
                        log.error("Error on reading request body", e);
                        return false;
                    }
                } else {
                    return false;
                }
                RestServicesConfiguration.RestMethodInfo restMethodInfo = restServicesConfiguration.getRestMethodInfo(serviceName, methodName, methodParamNames);
                return restMethodInfo != null && restMethodInfo.isAnonymousAllowed();
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
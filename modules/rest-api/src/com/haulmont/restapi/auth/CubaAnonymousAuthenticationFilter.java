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

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * This filter is used for anonymous access to CUBA REST API. If no Authorization header presents in the request and
 * if {@link GlobalConfig#getRestAnonymousEnabled()} is true, then the anonymous user session will be set to the
 * {@link SecurityContext} and the request will be authenticated. This filter must be invoked after the
 * {@link org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter}
 */
public class CubaAnonymousAuthenticationFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(CubaAnonymousAuthenticationFilter.class);

    @Inject
    protected GlobalConfig globalConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (globalConfig.getRestAnonymousEnabled()) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UUID anonymousSessionId = globalConfig.getAnonymousSessionId();
                CubaAnonymousAuthenticationToken anonymousAuthenticationToken =
                        new CubaAnonymousAuthenticationToken("anonymous", AuthorityUtils.createAuthorityList("ROLE_CUBA_ANONYMOUS"));
                SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);
                AppContext.setSecurityContext(new SecurityContext(anonymousSessionId));
            } else {
                log.debug("SecurityContextHolder not populated with cuba anonymous token, as it already contained: '{}'",
                        SecurityContextHolder.getContext().getAuthentication());
            }
        } else {
            log.trace("Anonymous access for CUBA REST API is disabled");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
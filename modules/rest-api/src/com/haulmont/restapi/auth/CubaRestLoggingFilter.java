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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter logs all REST API methods access.
 */
public class CubaRestLoggingFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(CubaRestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
        chain.doFilter(request, response);
    }

    protected String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL() +
                (!Strings.isNullOrEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "");
    }

    @Override
    public void destroy() {}
}

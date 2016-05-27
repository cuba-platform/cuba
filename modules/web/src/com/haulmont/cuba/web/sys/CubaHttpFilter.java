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
 *
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CubaHttpFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(CubaHttpFilter.class);

    protected List<String> bypassUrls = new ArrayList<>();
    protected CubaAuthProvider authProvider;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        if (configuration.getConfig(WebAuthConfig.class).getExternalAuthentication()) {
            try {
                authProvider = AppBeans.get(CubaAuthProvider.NAME);
                authProvider.init(filterConfig);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            // Fill bypassUrls
            String urls = configuration.getConfig(WebConfig.class).getCubaHttpFilterBypassUrls();
            String[] strings = urls.split("[, ]");
            for (String string : strings) {
                if (StringUtils.isNotBlank(string)) {
                    bypassUrls.add(string);
                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String requestURI = request.getRequestURI();

        boolean filtered = false;

        if (authProvider != null) {
            // Active Directory integration
            if (!requestURI.endsWith("/")) {
                requestURI = requestURI + "/";
            }

            boolean bypass = false;
            for (String bypassUrl : bypassUrls) {
                if (requestURI.contains(bypassUrl)) {
                    log.debug("Skip AD auth for by pass url: " + bypassUrl);
                    bypass = true;
                    break;
                }
            }

            filtered = filterByAuthProvider(request, response, chain, bypass);
        }

        if (!filtered) {
            handleNotFiltered(request, response, chain);
        }
    }

    @Override
    public void destroy() {
        if (authProvider != null) {
            authProvider.destroy();
        }
    }

    protected void handleNotFiltered(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    protected boolean filterByAuthProvider(HttpServletRequest request, HttpServletResponse response,
                                           FilterChain chain, boolean byPass) throws IOException, ServletException {
        if (!byPass) {
            authProvider.doFilter(request, response, chain);
            return true;
        }
        return false;
    }
}
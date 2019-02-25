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
import com.haulmont.cuba.web.security.HttpRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CubaHttpFilter extends CompositeFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(CubaHttpFilter.class);

    protected List<String> bypassUrls = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            // Fill bypassUrls
            WebConfig webConfig = configuration.getConfig(WebConfig.class);
            bypassUrls.addAll(webConfig.getCubaHttpFilterBypassUrls());
            bypassUrls.addAll(webConfig.getExternalHttpFilterBypassUrls());

            setFilters(new ArrayList<Filter>(getHttpRequestFilterBeans()));

            super.init(filterConfig);

            log.debug("CubaHttpFilter initialized");
        } catch (RuntimeException e) {
            log.error("Error initializing CubaHttpFilter", e);

            throw e;
        }
    }

    protected List<HttpRequestFilter> getHttpRequestFilterBeans() {
        Map<String, HttpRequestFilter> beanFilters = AppBeans.getAll(HttpRequestFilter.class);
        List<HttpRequestFilter> availableFilters = new ArrayList<>(beanFilters.values());

        AnnotationAwareOrderComparator.sort(availableFilters);

        return availableFilters;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String requestURI = request.getRequestURI();
        if (!requestURI.endsWith("/")) {
            requestURI = requestURI + "/";
        }
        for (String bypassUrl : bypassUrls) {
            if (requestURI.contains(bypassUrl)) {
                log.trace("Skip URL check: '{}' contains '{}'", requestURI, bypassUrl);
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        super.doFilter(request, response, chain);
    }
}
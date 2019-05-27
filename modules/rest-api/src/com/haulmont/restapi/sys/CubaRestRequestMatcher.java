/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.restapi.sys;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.restapi.config.RestApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A RequestMatcher that handles REST requests matching {@link #REST_BASE_PATTERN} pattern.
 * <p>
 * Skips requests that are matched to bypass patterns configured in app properties.
 *
 * @see RestApiConfig#getExternalRestBypassPatterns()
 */
public class CubaRestRequestMatcher
        implements RequestMatcher, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CubaRestRequestMatcher.class);

    protected static final String REST_BASE_PATTERN = "/rest/v2";
    protected static final String MATCH_ALL_PATTERN = "/**";

    @Inject
    protected Configuration configuration;

    protected RequestMatcher matcher;

    protected List<String> bypassPatterns;

    @Override
    public boolean matches(HttpServletRequest request) {
        if (bypass(request)) {
            return false;
        }

        return matcher.matches(request);
    }

    protected boolean bypass(HttpServletRequest request) {
        String requestPath = getRequestPath(request);

        for (String pattern : bypassPatterns) {
            if (requestPath.startsWith(pattern)) {
                log.debug("Request '{}' is skipped as matched to '{}' bypass pattern",
                        requestPath, pattern);

                return true;
            }
        }

        return false;
    }

    protected String getRequestPath(HttpServletRequest request) {
        String url = request.getServletPath();

        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            url = StringUtils.hasLength(url)
                    ? url + pathInfo
                    : pathInfo;
        }

        return url;
    }

    @Override
    public void onApplicationEvent(@Nonnull ContextRefreshedEvent event) {
        matcher = new AntPathRequestMatcher(REST_BASE_PATTERN + MATCH_ALL_PATTERN);

        RestApiConfig restApiConfig = configuration.getConfig(RestApiConfig.class);

        bypassPatterns = restApiConfig.getExternalRestBypassPatterns()
                .stream()
                .map(pattern -> {
                    String formatted = pattern.startsWith("/")
                            ? pattern
                            : "/" + pattern;

                    return REST_BASE_PATTERN + formatted;
                })
                .collect(Collectors.toList());
    }
}

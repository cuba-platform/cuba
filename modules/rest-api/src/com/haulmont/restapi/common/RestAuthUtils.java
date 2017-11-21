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

package com.haulmont.restapi.common;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * REST API authentication utility class
 */
@Component("cuba_RestAuthUtils")
public class RestAuthUtils {

    @Inject
    protected Configuration configuration;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected MessageTools messageTools;

    protected Logger log = LoggerFactory.getLogger(RestAuthUtils.class);

    /**
     * Method extracts locale information from the Accept-Language header. If no such header is specified or the
     * passed locale is not among application available locales, then null is returned
     */
    @Nullable
    public Locale extractLocaleFromRequestHeader(HttpServletRequest request) {
        Locale locale = null;
        if (!Strings.isNullOrEmpty(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            Locale requestLocale = request.getLocale();

            GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
            Map<String, Locale> availableLocales = globalConfig.getAvailableLocales();
            if (availableLocales.values().contains(requestLocale)) {
                locale = requestLocale;
            } else {
                log.warn("Locale {} passed in the Accept-Language header is not supported by the application. It was ignored.");
            }
        }
        return locale;
    }
}

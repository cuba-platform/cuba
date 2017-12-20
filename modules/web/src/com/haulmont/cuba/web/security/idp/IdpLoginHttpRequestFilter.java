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

import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.web.security.HttpRequestFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

@ConditionalOnAppProperty(property = "cuba.web.idp.enabled", value = "true")
@ConditionalOnAppProperty(property = "cuba.web.externalAuthentication", value = "false", defaultValue = "false")
@Component("cuba_IdpLoginHttpRequestFilter")
public class IdpLoginHttpRequestFilter extends BaseIdpSessionFilter implements HttpRequestFilter, Ordered {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (webIdpConfig.getIdpEnabled()) {
            checkRequiredConfigProperties(webIdpConfig);
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

    protected void checkRequiredConfigProperties(WebIdpConfig webIdpConfig) {
        List<String> missingProperties = new ArrayList<>();

        if (StringUtils.isBlank(webIdpConfig.getIdpBaseURL())) {
            missingProperties.add("cuba.web.idp.baseUrl");
        }
        if (StringUtils.isBlank(webIdpConfig.getIdpTrustedServicePassword())) {
            missingProperties.add("cuba.web.idp.trustedServicePassword");
        }
        if (!missingProperties.isEmpty()) {
            throw new IllegalStateException("Please configure required application properties for IDP integration: \n" +
                    StringUtils.join(missingProperties, "\n"));
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}
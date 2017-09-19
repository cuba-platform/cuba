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

package com.haulmont.idp.sys;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.TrustedClientService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.idp.IdpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets {@link SecurityContext} with system session for IDP controllers to perform service calls to the middleware.
 */
public class IdpSecurityContextInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(IdpSecurityContextInterceptor.class);

    @Inject
    protected IdpConfig idpConfig;

    @Inject
    protected TrustedClientService trustedClientService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // filter resource requests
        if (ClassUtils.isAssignableValue(ResourceHttpRequestHandler.class, handler)) {
            return true;
        }

        UserSession systemSession;
        try {
            systemSession = trustedClientService.getSystemSession(idpConfig.getTrustedClientPassword());
            AppContext.setSecurityContext(new SecurityContext(systemSession));
        } catch (LoginException e) {
            log.error("Unable to obtain system session", e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        AppContext.setSecurityContext(null);
    }
}
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

package com.haulmont.cuba.web.security.providers;

import com.haulmont.cuba.core.sys.ConditionalOnAppProperty;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.Credentials;
import com.haulmont.cuba.security.auth.RememberMeCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.security.LoginProvider;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;

@ConditionalOnAppProperty(property = "cuba.web.rememberMeEnabled", value = "true", defaultValue = "true")
@Component("cuba_RememberMeLoginProvider")
public class RememberMeLoginProvider implements LoginProvider, Ordered {
    @Inject
    protected AuthenticationService authenticationService;
    @Inject
    protected WebConfig webConfig;

    @Nullable
    @Override
    public AuthenticationDetails login(Credentials credentials) throws LoginException {
        if (!(credentials instanceof RememberMeCredentials)) {
            throw new ClassCastException("Credentials cannot be cast to RememberMeCredentials");
        }

        return authenticationService.login(credentials);
    }

    @Override
    public boolean supports(Class<?> credentialsClass) {
        return webConfig.getRememberMeEnabled()
                && RememberMeCredentials.class.isAssignableFrom(credentialsClass);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 70;
    }
}
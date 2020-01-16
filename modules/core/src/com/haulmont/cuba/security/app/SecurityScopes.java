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

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.security.entity.SecurityScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component(SecurityScopes.NAME)
public class SecurityScopes {
    public static final String NAME = "cuba_SecurityScopes";

    @Inject
    protected ServerConfig serverConfig;
    @Inject
    protected Messages messages;

    protected Set<SecurityScope> securityScopes;

    @PostConstruct
    protected void init() {
        securityScopes = serverConfig.getSecurityScopes().stream()
                .map(SecurityScope::new)
                .collect(Collectors.toSet());
    }

    /**
     * Represents list of all available security scopes for all client types.
     */
    public Collection<SecurityScope> getAvailableSecurityScopes() {
        return Collections.unmodifiableSet(securityScopes);
    }

    /**
     * Register a new security scope
     */
    public void registerScope(SecurityScope securityScope) {
        securityScopes.add(securityScope);
    }
}

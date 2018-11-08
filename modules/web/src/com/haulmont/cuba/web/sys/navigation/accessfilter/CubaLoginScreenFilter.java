/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.sys.navigation.accessfilter;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.sys.navigation.NavigationState;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Order(NavigationFilter.LOWEST_PLATFORM_PRECEDENCE)
public class CubaLoginScreenFilter implements NavigationFilter {

    @Inject
    protected Messages messages;

    @Override
    public AccessCheckResult allowed(NavigationState fromState, NavigationState toState) {
        if (!"login".equals(toState.getRoot())) {
            return AccessCheckResult.allowed();
        }

        boolean authenticated = App.getInstance().getConnection().isAuthenticated();

        return authenticated
                ? AccessCheckResult.rejected(messages.getMainMessage("navigation.unableToGoToLogin"))
                : AccessCheckResult.allowed();
    }
}

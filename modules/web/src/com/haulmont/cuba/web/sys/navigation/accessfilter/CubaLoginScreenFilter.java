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

import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.web.WebConfig;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;

@Component
@Order(NavigationFilter.LOWEST_PLATFORM_PRECEDENCE)
public class CubaLoginScreenFilter implements NavigationFilter {

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected WebConfig webConfig;

    @Override
    public AccessCheckResult allowed(NavigationState fromState, NavigationState toState) {
        String loginWindowRoute = windowConfig.findRoute(webConfig.getLoginScreenId());

        if (!Objects.equals(loginWindowRoute, toState.getRoot())) {
            return AccessCheckResult.allowed();
        }

        boolean authenticated = App.getInstance().getConnection().isAuthenticated();

        return authenticated
                ? AccessCheckResult.rejected()
                : AccessCheckResult.allowed();
    }
}

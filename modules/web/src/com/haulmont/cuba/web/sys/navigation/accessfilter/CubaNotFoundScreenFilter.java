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
import com.haulmont.cuba.web.app.ui.navigation.notfoundwindow.NotFoundScreen;
import com.haulmont.cuba.web.sys.navigation.NavigationState;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Objects;

@Component
@Order(NavigationFilter.LOWEST_PLATFORM_PRECEDENCE - 10)
public class CubaNotFoundScreenFilter implements NavigationFilter {

    @Inject
    protected WindowConfig windowConfig;

    @Override
    public AccessCheckResult allowed(NavigationState fromState, NavigationState toState) {
        String notFoundScreenRoute = windowConfig.findRoute(NotFoundScreen.ID);

        return Objects.equals(notFoundScreenRoute, toState.getNestedRoute())
                ? AccessCheckResult.rejected()
                : AccessCheckResult.allowed();
    }
}

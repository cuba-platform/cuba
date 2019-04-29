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

package com.haulmont.cuba.web.sys.navigation.navigationhandler;

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Order(NavigationHandler.LOWEST_PLATFORM_PRECEDENCE - 40)
public class RootNavigationHandler extends AbstractNavigationHandler implements NavigationHandler {

    private static final Logger log = LoggerFactory.getLogger(RootNavigationHandler.class);

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected WebConfig webConfig;
    @Inject
    protected Security security;
    @Inject
    protected BeanLocator beanLocator;

    @Override
    public boolean doHandle(NavigationState requestedState, AppUI ui) {
        if (isEmptyState(requestedState)) {
            return false;
        }

        if (!rootChanged(requestedState, ui)) {
            return fullyHandled(requestedState);
        }

        WindowInfo windowInfo = windowConfig.findWindowInfoByRoute(requestedState.getRoot());
        if (windowInfo == null) {
            log.info("No screen found registered for route '{}'", requestedState.getRoot());
            revertNavigationState(ui);
            return true;
        }

        if (shouldRedirect(windowInfo, security, ui)) {
            redirect(requestedState, ui, beanLocator);
            return true;
        }

        if (isNotPermittedToNavigate(requestedState, windowInfo, security, ui)) {
            revertNavigationState(ui);
            return true;
        }

        ui.getScreens()
                .create(windowInfo.getId(), OpenMode.ROOT)
                .show();

        return fullyHandled(requestedState);
    }

    protected boolean fullyHandled(NavigationState requestedState) {
        return StringUtils.isEmpty(requestedState.getNestedRoute())
                && MapUtils.isEmpty(requestedState.getParams());
    }

    protected boolean rootChanged(NavigationState requestedState, AppUI ui) {
        Screen rootScreen = ui.getScreens().getOpenedScreens()
                .getRootScreenOrNull();

        if (rootScreen == null) {
            return false;
        }

        String rootRoute = ((WebWindow) rootScreen.getWindow())
                .getResolvedState()
                .getRoot();

        return !StringUtils.equals(rootRoute, requestedState.getRoot());
    }
}

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

import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.RedirectHandler;
import com.haulmont.cuba.web.sys.navigation.UrlChangeHandler;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter;
import org.apache.commons.lang3.StringUtils;

import static com.haulmont.cuba.web.sys.navigation.UrlTools.replaceState;

public abstract class AbstractNavigationHandler implements NavigationHandler {

    protected boolean isEmptyState(NavigationState requestedState) {
        return requestedState == null || requestedState == NavigationState.EMPTY;
    }

    protected void revertNavigationState(AppUI ui) {
        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();
        NavigationState currentState = ui.getHistory().getNow();

        Screen screen = urlChangeHandler.findActiveScreenByState(currentState);
        if (screen == null) {
            screen = urlChangeHandler.getActiveScreen();
        }

        replaceState(urlChangeHandler.getResolvedState(screen).asRoute());
    }

    protected boolean isRootRoute(WindowInfo windowInfo) {
        return windowInfo != null
                && windowInfo.getRouteDefinition().isRoot();
    }

    protected boolean shouldRedirect(WindowInfo windowInfo, Security security, AppUI ui) {
        if (ui.hasAuthenticatedSession()) {
            return false;
        }

        return !security.isScreenPermitted(windowInfo.getId());
    }

    protected void redirect(NavigationState navigationState, AppUI ui, BeanLocator beanLocator) {
        String loginScreenId = beanLocator.get(Configuration.class)
                .getConfig(WebConfig.class)
                .getLoginScreenId();

        Screen loginScreen = ui.getScreens().create(loginScreenId, OpenMode.ROOT);

        loginScreen.show();

        RedirectHandler redirectHandler = beanLocator.getPrototype(RedirectHandler.NAME, ui);
        redirectHandler.schedule(navigationState);

        ui.getUrlChangeHandler().setRedirectHandler(redirectHandler);
    }

    protected boolean isNotPermittedToNavigate(NavigationState requestedState, WindowInfo windowInfo,
                                               Security security, AppUI ui) {

        boolean screenPermitted = security.isScreenPermitted(windowInfo.getId());
        if (!screenPermitted) {
            revertNavigationState(ui);

            throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
        }

        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();

        NavigationFilter.AccessCheckResult navigationAllowed = urlChangeHandler.navigationAllowed(requestedState);
        if (navigationAllowed.isRejected()) {
            if (StringUtils.isNotEmpty(navigationAllowed.getMessage())) {
                urlChangeHandler.showNotification(navigationAllowed.getMessage());
            }

            revertNavigationState(ui);

            return true;
        }

        return false;
    }
}

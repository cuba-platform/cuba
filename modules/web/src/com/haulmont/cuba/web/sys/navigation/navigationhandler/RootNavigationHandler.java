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

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.app.ui.navigation.notfoundwindow.NotFoundScreen;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.sys.navigation.UrlChangeHandler;
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
public class RootNavigationHandler implements NavigationHandler {

    private static final Logger log = LoggerFactory.getLogger(RootNavigationHandler.class);

    @Inject
    protected WindowConfig windowConfig;

    @Override
    public boolean doHandle(NavigationState requestedState, AppUI ui) {
        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();

        if (urlChangeHandler.isEmptyState(requestedState)) {
            urlChangeHandler.revertNavigationState();
            return false;
        }

        if (!rootChanged(requestedState, ui)) {
            return false;
        }

        String rootRoute = requestedState.getRoot();
        WindowInfo windowInfo = windowConfig.findWindowInfoByRoute(rootRoute);

        if (windowInfo == null) {
            log.info("No registered screen found for route: '{}'", rootRoute);
            urlChangeHandler.revertNavigationState();

            handle404(rootRoute, ui);

            return true;
        }

        if (urlChangeHandler.shouldRedirect(windowInfo)) {
            urlChangeHandler.redirect(requestedState);
            return true;
        }

        if (!urlChangeHandler.isPermittedToNavigate(requestedState, windowInfo)) {
            return true;
        }

        Screen screen = ui.getScreens().create(windowInfo.getId(), OpenMode.ROOT);

        boolean hasNestedRoute = StringUtils.isNotEmpty(requestedState.getNestedRoute());
        if (!hasNestedRoute
                && MapUtils.isNotEmpty(requestedState.getParams())) {
            UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                    new UrlParamsChangedEvent(screen, requestedState.getParams()));

            ((WebWindow) screen.getWindow())
                    .setResolvedState(requestedState);
        }

        screen.show();

        return !hasNestedRoute;
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

    protected void handle404(String route, AppUI ui) {
        MapScreenOptions options = new MapScreenOptions(ParamsMap.of("requestedRoute", route));

        ui.getScreens()
                .create(NotFoundScreen.class, OpenMode.NEW_TAB, options)
                .show();
    }
}

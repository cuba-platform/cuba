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

package com.haulmont.cuba.web.navigation;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.components.DialogWindow;
import com.haulmont.cuba.gui.components.RootWindow;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.sys.RouteDefinition;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.UrlHandlingMode;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.sys.navigation.NavigationState;
import com.haulmont.cuba.web.sys.navigation.UrlTools;
import com.vaadin.server.Page;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

public class WebUrlRouting implements UrlRouting {

    protected static final int MAX_NESTED_ROUTES = 2;

    private static final Logger log = LoggerFactory.getLogger(WebUrlRouting.class);

    @Inject
    protected Events events;
    @Inject
    protected WebConfig webConfig;

    protected AppUI ui;

    public WebUrlRouting(AppUI ui) {
        this.ui = ui;
    }

    @Override
    public void pushState(Screen screen, Map<String, String> urlParams) {
        if (notSuitableUrlHandlingMode()) {
            return;
        }

        checkNotNullArgument(screen);
        checkNotNullArgument(urlParams);

        changeStateInternal(screen, urlParams, true);
    }

    @Override
    public void replaceState(Screen screen, Map<String, String> urlParams) {
        if (notSuitableUrlHandlingMode()) {
            return;
        }

        checkNotNullArgument(screen);
        checkNotNullArgument(urlParams);

        changeStateInternal(screen, urlParams, false);
    }

    protected void changeStateInternal(Screen screen, Map<String, String> urlParams, boolean pushState) {
        NavigationState oldNavState = getState();
        String newState = buildNavState(screen, urlParams);

        if (pushState && !externalNavigation(oldNavState, newState)) {
            UrlTools.pushState(newState);
        } else {
            UrlTools.replaceState(newState);
        }

        NavigationState newNavState = getState();
        ((WebWindow) screen.getWindow()).setResolvedState(newNavState);

        if (pushState) {
            ui.getHistory().forward(newNavState);
        }
    }

    @Override
    public NavigationState getState() {
        if (notSuitableUrlHandlingMode()) {
            return NavigationState.empty();
        }

        if (UrlTools.headless()) {
            log.trace("Unable to resolve navigation state in headless mode");
            return NavigationState.empty();
        }

        String uriFragment = Page.getCurrent().getUriFragment();
        return UrlTools.parseState(uriFragment);
    }

    protected String buildNavState(Screen screen, Map<String, String> urlParams) {
        StringBuilder state = new StringBuilder();

        if (screen.getWindow() instanceof RootWindow) {
            state.append(getRoute(screen));
        } else {
            Screen rootScreen = ui.getScreens().getOpenedScreens().getRootScreen();
            state.append(getRoute(rootScreen));

            String stateMark = getStateMark(screen);
            state.append('/').append(stateMark);

            String nestedRoute = buildNestedRoute(screen);
            if (nestedRoute != null && !nestedRoute.isEmpty()) {
                state.append('/').append(nestedRoute);
            }
        }

        state.append(buildParamsString(screen, urlParams));

        return state.toString();
    }

    protected String buildNestedRoute(Screen screen) {
        if (screen.getWindow() instanceof DialogWindow) {
            return buildDialogRoute(screen);
        } else {
            return buildCurrentScreenRoute();
        }
    }

    protected String buildDialogRoute(Screen dialog) {
        RouteDefinition routeDef = getRouteDef(dialog);
        String currentScreenRoute = buildCurrentScreenRoute();

        if (routeDef == null) {
            return currentScreenRoute;
        }

        String dialogRoute = routeDef.getPath();
        if (dialogRoute == null || dialogRoute.isEmpty()) {
            return currentScreenRoute;
        }

        String parentPrefix = routeDef.getParentPrefix();
        if (StringUtils.isNotEmpty(parentPrefix)
                && currentScreenRoute.endsWith(parentPrefix)) {
            dialogRoute = dialogRoute.substring(parentPrefix.length() + 1);
        }

        return StringUtils.isEmpty(currentScreenRoute)
                ? dialogRoute
                : currentScreenRoute + '/' + dialogRoute;
    }

    protected String buildCurrentScreenRoute() {
        List<Screen> screens = new ArrayList<>(ui.getScreens().getOpenedScreens().getCurrentBreadcrumbs());
        Collections.reverse(screens);

        String prevSubRoute = null;
        StringBuilder state = new StringBuilder();

        for (int i = 0; i < screens.size() && i < MAX_NESTED_ROUTES; i++) {
            String route = buildRoutePart(prevSubRoute, screens.get(i));

            if (StringUtils.isNotEmpty(state) && StringUtils.isNotEmpty(route)) {
                state.append('/');
            }
            state.append(route);

            prevSubRoute = route;
        }

        return state.toString();
    }

    protected String buildRoutePart(String prevSubRoute, Screen screen) {
        String screenRoute = getRoute(screen);

        String parentPrefix = getScreenParentPrefix(screen);
        if (StringUtils.isEmpty(parentPrefix)) {
            return screenRoute;
        }

        if (Objects.equals(prevSubRoute, parentPrefix)) {
            return screenRoute.replace(parentPrefix + "/", "");
        } else {
            return screenRoute;
        }
    }

    protected String getScreenParentPrefix(Screen screen) {
        String parentPrefix = null;

        Route routeAnnotation = screen.getClass().getAnnotation(Route.class);
        if (routeAnnotation != null) {
            return routeAnnotation.parentPrefix();
        } else {
            RouteDefinition routeDef = getScreenContext(screen)
                    .getWindowInfo()
                    .getRouteDefinition();
            if (routeDef != null) {
                parentPrefix = routeDef.getParentPrefix();
            }
        }

        return parentPrefix;
    }

    protected String buildParamsString(Screen screen, Map<String, String> urlParams) {
        String route = getRoute(screen);
        if (StringUtils.isEmpty(route) && (MapUtils.isNotEmpty(urlParams) || isEditor(screen))) {
            log.info("There's no route for screen \"{}\". URL params will be ignored", screen.getId());
            return "";
        }

        Map<String, String> params = new LinkedHashMap<>();

        if (isEditor(screen)) {
            Object entityId = ((EditorScreen) screen).getEditedEntity().getId();
            String base64Id = UrlTools.serializeId(entityId);

            params.put("id", base64Id);
        }

        params.putAll(urlParams != null ? urlParams : Collections.emptyMap());

        String paramsString = params.entrySet()
                .stream()
                .map(param -> String.format("%s=%s", param.getKey(), param.getValue()))
                .collect(Collectors.joining("&"));

        return !paramsString.isEmpty() ? "?" + paramsString : "";
    }

    protected boolean isEditor(Screen screen) {
        return screen instanceof EditorScreen;
    }

    protected String getRoute(Screen screen) {
        RouteDefinition routeDef = getRouteDef(screen);
        String route = routeDef != null ? routeDef.getPath() : null;

        return route == null || route.isEmpty() ? "" : route;
    }

    protected RouteDefinition getRouteDef(Screen screen) {
        return screen == null
                ? null
                : getScreenContext(screen).getWindowInfo().getRouteDefinition();
    }

    protected String getStateMark(Screen screen) {
        return String.valueOf(((WebWindow) screen.getWindow()).getUrlStateMark());
    }

    protected boolean externalNavigation(NavigationState requestedState, String newRoute) {
        if (requestedState == null) {
            return false;
        }
        NavigationState newNavigationState = UrlTools.parseState(newRoute);
        return !ui.getHistory().has(requestedState)
                && Objects.equals(requestedState.getRoot(), newNavigationState.getRoot())
                && Objects.equals(requestedState.getNestedRoute(), newNavigationState.getNestedRoute())
                && Objects.equals(requestedState.getParamsString(), newNavigationState.getParamsString());
    }

    protected boolean notSuitableUrlHandlingMode() {
        if (UrlHandlingMode.URL_ROUTES != webConfig.getUrlHandlingMode()) {
            log.debug("UrlRouting bean invocations are ignored for {} URL handling mode", webConfig.getUrlHandlingMode());
            return true;
        }
        return false;
    }
}

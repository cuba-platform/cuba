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
        return UrlTools.parseState(Page.getCurrent().getUriFragment());
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
        RouteDefinition route = getRouteDef(dialog);
        if (route == null) {
            return buildCurrentScreenRoute();
        }

        String dialogRoute = route.getPath();
        if (route.getParent() == null) {
            return dialogRoute;
        }

        Screen currentScreen = getCurrentScreen();
        boolean openedInContext = currentScreen.getClass() == route.getParent();
        if (!openedInContext) {
            throw new IllegalStateException("Dialog is opened outside of its context");
        }

        String contextRoute = buildCurrentScreenRoute();
        return StringUtils.isNotEmpty(dialogRoute) ? contextRoute + "/" + dialogRoute
                : contextRoute;
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

        Route routeAnnotation = screen.getClass().getAnnotation(Route.class);
        if (routeAnnotation == null) {
            return screenRoute;
        }

        Class<? extends Screen> parentPrefixClass = routeAnnotation.parentPrefix();
        if (Screen.class == parentPrefixClass) {
            return screenRoute;
        }

        Route parentPrefixClassRoute = parentPrefixClass.getAnnotation(Route.class);
        if (parentPrefixClassRoute == null) {
            log.info("\"{}\" screen is specified as parent prefix but it has no route");
            return screenRoute;
        }

        String parentRoute = !parentPrefixClassRoute.value().isEmpty()
                ? parentPrefixClassRoute.value()
                : parentPrefixClassRoute.path();

        if (parentRoute.isEmpty()) {
            return screenRoute;
        }

        if (Objects.equals(prevSubRoute, parentRoute)) {
            return screenRoute.replace(parentRoute + "/", "");
        } else {
            return screenRoute;
        }
    }

    protected String buildParamsString(Screen screen, Map<String, String> urlParams) {
        String route = getRoute(screen);
        if (StringUtils.isEmpty(route) && MapUtils.isNotEmpty(urlParams)) {
            log.info("There's no route for screen {}. Ignore URL params");
            return "";
        }

        Map<String, String> params = new LinkedHashMap<>();

        if (screen instanceof EditorScreen) {
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

    protected String getRoute(Screen screen) {
        RouteDefinition routeDef = getRouteDef(screen);
        String route = routeDef != null ? routeDef.getPath() : null;

        return route == null || route.isEmpty() ? "" : route;
    }

    protected RouteDefinition getRouteDef(Screen screen) {
        return screen == null ? null
                : getScreenContext(screen).getWindowInfo().getRouteDefinition();
    }

    protected Screen getCurrentScreen() {
        Iterator<Screen> screens = ui.getScreens().getOpenedScreens().getCurrentBreadcrumbs()
                .iterator();
        return screens.hasNext() ? screens.next() : null;
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

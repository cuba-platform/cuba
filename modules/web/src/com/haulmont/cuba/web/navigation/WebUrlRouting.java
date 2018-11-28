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

        if (isNotAttachedToUi(screen)) {
            log.info("Pushing state for screen not attached to UI is not permitted");
            return;
        }

        updateState(screen, urlParams, true);
    }

    @Override
    public void replaceState(Screen screen, Map<String, String> urlParams) {
        if (notSuitableUrlHandlingMode()) {
            return;
        }

        checkNotNullArgument(screen);
        checkNotNullArgument(urlParams);

        if (isNotAttachedToUi(screen)) {
            log.info("Replacing state for screen not attached to UI is not permitted");
            return;
        }

        updateState(screen, urlParams, false);
    }

    protected void updateState(Screen screen, Map<String, String> urlParams, boolean pushState) {
        NavigationState oldNavState = getState();
        NavigationState newState = buildNavState(screen, urlParams);

        // do not push copy-pasted requested state to avoid double state pushing into browser history
        if (!pushState || externalNavigation(oldNavState, newState)) {
            UrlTools.replaceState(newState.asRoute());
        } else {
            UrlTools.pushState(newState.asRoute());
        }

        ((WebWindow) screen.getWindow()).setResolvedState(newState);

        if (pushState) {
            ui.getHistory().forward(newState);
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

        String uriFragment = Page.getCurrent().getLocation().getRawFragment();
        return UrlTools.parseState(uriFragment);
    }

    protected NavigationState buildNavState(Screen screen, Map<String, String> urlParams) {
        NavigationState state;

        if (screen.getWindow() instanceof RootWindow) {
            state = new NavigationState(getRoute(screen), "", "", urlParams);
        } else {
            String rootRoute = getRoute(ui.getScreens().getOpenedScreens().getRootScreen());
            String stateMark = getStateMark(screen);
            String nestedRoute = buildNestedRoute(screen);
            Map<String, String> params = processParams(screen, urlParams);

            NavigationState currentState = ui.getHistory().getNow();
            if (Objects.equals(nestedRoute, currentState.getNestedRoute())) {
                // change only the state mark if the nesting limit has been reached
                return new NavigationState(rootRoute, stateMark, nestedRoute, currentState.getParams());
            }

            state = new NavigationState(rootRoute, stateMark, nestedRoute, params);
        }

        return state;
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
            return nullToEmpty(screenRoute);
        }

        if (Objects.equals(prevSubRoute, parentPrefix)) {
            return nullToEmpty(screenRoute.replace(parentPrefix + "/", ""));
        } else {
            return nullToEmpty(screenRoute);
        }
    }

    protected String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    protected String getScreenParentPrefix(Screen screen) {
        String parentPrefix = null;

        Route routeAnnotation = screen.getClass().getAnnotation(Route.class);
        if (routeAnnotation != null) {
            parentPrefix = routeAnnotation.parentPrefix();
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

    protected Map<String, String> processParams(Screen screen, Map<String, String> urlParams) {
        String route = getRoute(screen);

        if (StringUtils.isEmpty(route)
                && (isEditor(screen) || MapUtils.isNotEmpty(urlParams))) {
            log.debug("There's no route for screen \"{}\". URL params will be ignored", screen.getId());
            return Collections.emptyMap();
        }

        Map<String, String> params = new LinkedHashMap<>();

        if (isEditor(screen)) {
            Object entityId = ((EditorScreen) screen).getEditedEntity().getId();
            String base64Id = UrlTools.serializeId(entityId);

            params.put("id", base64Id);
        }

        params.putAll(urlParams != null
                ? urlParams
                : Collections.emptyMap());

        return params;
    }

    protected boolean isEditor(Screen screen) {
        return screen instanceof EditorScreen;
    }

    protected String getRoute(Screen screen) {
        RouteDefinition routeDef = getRouteDef(screen);
        return routeDef == null
                ? null
                : routeDef.getPath();
    }

    protected RouteDefinition getRouteDef(Screen screen) {
        return screen == null
                ? null
                : getScreenContext(screen).getWindowInfo().getRouteDefinition();
    }

    protected String getStateMark(Screen screen) {
        WebWindow window = (WebWindow) screen.getWindow();
        return String.valueOf(window.getUrlStateMark());
    }

    protected boolean externalNavigation(NavigationState requestedState, NavigationState newNavigationState) {
        if (requestedState == null) {
            return false;
        }

        boolean notInHistory = !ui.getHistory().has(requestedState);

        boolean sameRoot = Objects.equals(requestedState.getRoot(), newNavigationState.getRoot());
        boolean sameNestedRoute = Objects.equals(requestedState.getNestedRoute(), newNavigationState.getNestedRoute());
        boolean sameParams = Objects.equals(requestedState.getParamsString(), newNavigationState.getParamsString());

        return notInHistory && sameRoot && sameNestedRoute && sameParams;
    }

    protected boolean notSuitableUrlHandlingMode() {
        if (UrlHandlingMode.URL_ROUTES != webConfig.getUrlHandlingMode()) {
            log.debug("UrlRouting bean invocations are ignored for {} URL handling mode", webConfig.getUrlHandlingMode());
            return true;
        }
        return false;
    }

    protected boolean isNotAttachedToUi(Screen screen) {
        boolean notAttached;

        if (screen.getWindow() instanceof RootWindow) {
            Screen rootScreen = ui.getScreens().getOpenedScreens()
                    .getRootScreenOrNull();
            notAttached = rootScreen == null || rootScreen != screen;
        } else if (screen.getWindow() instanceof DialogWindow) {
            notAttached = !ui.getScreens().getOpenedScreens()
                    .getDialogScreens()
                    .contains(screen);
        } else {
            notAttached = !ui.getScreens().getOpenedScreens()
                    .getActiveScreens()
                    .contains(screen);
        }

        return notAttached;
    }
}

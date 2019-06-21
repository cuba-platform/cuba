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

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.MapScreenOptions;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.app.ui.navigation.notfoundwindow.NotFoundScreen;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.sys.navigation.EditorTypeExtractor;
import com.haulmont.cuba.web.sys.navigation.UrlChangeHandler;
import com.haulmont.cuba.web.sys.navigation.UrlIdSerializer;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Order(NavigationHandler.LOWEST_PLATFORM_PRECEDENCE - 30)
public class ScreenNavigationHandler implements NavigationHandler {

    private static final Logger log = LoggerFactory.getLogger(ScreenNavigationHandler.class);

    protected static final int MAX_SUB_ROUTES = 2;

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected Security security;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Metadata metadata;

    @Override
    public boolean doHandle(NavigationState requestedState, AppUI ui) {
        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();

        if (urlChangeHandler.isEmptyState(requestedState)
                || !isScreenChanged(requestedState, ui)) {
            return false;
        }

        String requestedRoute = requestedState.getNestedRoute();
        if (StringUtils.isEmpty(requestedRoute)) {
            log.info("Unable to handle state with empty route '{}'", requestedState);
            urlChangeHandler.revertNavigationState();

            return true;
        }

        String[] routeParts = {requestedRoute};
        if (windowConfig.findWindowInfoByRoute(requestedRoute) == null) {
            routeParts = requestedRoute.split("/");
        }

        if (routeParts.length > MAX_SUB_ROUTES) {
            log.info("Unable to perform navigation to requested state '{}'. Only {} sub routes are supported",
                    requestedRoute, MAX_SUB_ROUTES);
            urlChangeHandler.revertNavigationState();

            return true;
        }

        List<Pair<String, WindowInfo>> routeWindowInfos = Arrays.stream(routeParts)
                .map(subRoute -> new Pair<>(subRoute, windowConfig.findWindowInfoByRoute(subRoute)))
                .collect(Collectors.toList());

        for (Pair<String, WindowInfo> entry : routeWindowInfos) {
            WindowInfo routeWindowInfo = entry.getSecond();
            if (routeWindowInfo == null) {
                log.info("No registered screen found for route: '{}'", entry.getFirst());
                urlChangeHandler.revertNavigationState();

                handle404(entry.getFirst(), ui);

                return true;
            }

            if (urlChangeHandler.shouldRedirect(routeWindowInfo)) {
                urlChangeHandler.redirect(requestedState);
                return true;
            }

            if (urlChangeHandler.isRootRoute(routeWindowInfo)) {
                log.info("Unable navigate to '{}' as nested screen", routeWindowInfo.getId());
                urlChangeHandler.revertNavigationState();

                return true;
            }
        }

        return navigate(requestedState, ui, routeWindowInfos);
    }

    protected boolean navigate(NavigationState requestedState, AppUI ui, List<Pair<String, WindowInfo>> routeWindowInfos) {
        int subRouteIdx = 0;
        NavigationState currentState = ui.getHistory().getNow();

        for (Pair<String, WindowInfo> entry : routeWindowInfos) {
            String subRoute = entry.getFirst();

            if (skipSubRoute(requestedState, subRouteIdx, currentState, subRoute)) {
                subRouteIdx++;
                continue;
            }

            WindowInfo windowInfo = entry.getSecond();

            openScreen(requestedState, subRoute, windowInfo, ui);

            subRouteIdx++;
        }

        return true;
    }

    protected boolean fullyHandled(AppUI ui, NavigationState requestedState) {
        Map<String, String> requestedParams = MapUtils.isNotEmpty(requestedState.getParams())
                ? requestedState.getParams()
                : Collections.emptyMap();

        Screen screen = ui.getUrlChangeHandler().findActiveScreenByState(requestedState);
        if (screen == null) {
            return MapUtils.isEmpty(requestedParams);
        }

        Map<String, String> resolvedParams = ((WebWindow) screen.getWindow()).getResolvedState().getParams();

        return requestedParams.equals(resolvedParams);
    }

    protected void handle404(String route, AppUI ui) {
        MapScreenOptions options = new MapScreenOptions(ParamsMap.of("requestedRoute", route));

        ui.getScreens()
                .create(NotFoundScreen.class, OpenMode.NEW_TAB, options)
                .show();
    }

    protected boolean isScreenChanged(NavigationState requestedState, AppUI ui) {
        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();

        if (urlChangeHandler.isEmptyState(requestedState)
                || urlChangeHandler.isRootState(requestedState)) {
            return false;
        }

        Screen currentScreen = urlChangeHandler.findActiveScreenByState(ui.getHistory().getNow());

        if (currentScreen == null) {
            Iterator<Screen> screensIterator = ui.getScreens()
                    .getOpenedScreens().getCurrentBreadcrumbs().iterator();
            currentScreen = screensIterator.hasNext()
                    ? screensIterator.next()
                    : null;
        }

        if (currentScreen == null) {
            return true;
        }

        NavigationState currentState = urlChangeHandler.getResolvedState(currentScreen);
        return !Objects.equals(currentState.getStateMark(), requestedState.getStateMark())
                || !Objects.equals(currentState.getNestedRoute(), requestedState.getNestedRoute());
    }

    protected boolean skipSubRoute(NavigationState requestedState, int subRouteIdx, NavigationState currentState,
                                   String screenRoute) {
        if (!requestedState.asRoute().startsWith(currentState.asRoute() + '/')) {
            return false;
        }

        String[] currentRouteParts = currentState.getNestedRoute()
                .split("/");
        return subRouteIdx < currentRouteParts.length
                && currentRouteParts[subRouteIdx].equals(screenRoute);
    }

    protected void openScreen(NavigationState requestedState, String screenRoute, WindowInfo windowInfo, AppUI ui) {
        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();

        if (!urlChangeHandler.isPermittedToNavigate(requestedState, windowInfo)) {
            return;
        }

        Screen screen = createScreen(requestedState, screenRoute, windowInfo, ui);

        if (screen == null) {
            log.info("Unable to open screen '{}' for requested route '{}'", windowInfo.getId(),
                    requestedState.getNestedRoute());

            urlChangeHandler.revertNavigationState();
            return;
        }

        if (StringUtils.isNotEmpty(screenRoute)
                && requestedState.getNestedRoute().endsWith(screenRoute)) {
            Map<String, String> params = requestedState.getParams();
            if (MapUtils.isNotEmpty(params)) {
                UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                        new UrlParamsChangedEvent(screen, params));
            }

            ((WebWindow) screen.getWindow())
                    .setResolvedState(requestedState);
        }

        screen.show();
    }

    protected Screen createScreen(NavigationState requestedState, String screenRoute, WindowInfo windowInfo, AppUI ui) {
        Screen screen;

        if (isEditor(windowInfo)) {
            screen = createEditor(windowInfo, screenRoute, requestedState, ui);
        } else {
            OpenMode openMode = getScreenOpenMode(requestedState.getNestedRoute(), screenRoute, ui);
            screen = ui.getScreens().create(windowInfo.getId(), openMode);
        }

        return screen;
    }

    protected Screen createEditor(WindowInfo windowInfo, String screenRoute, NavigationState requestedState, AppUI ui) {
        Map<String, Object> options = createEditorScreenOptions(windowInfo, requestedState, ui);

        if (MapUtils.isEmpty(options)) {
            log.info("Unable to load entity for editor: '{}'. " +
                    "Subscribe for 'UrlParamsChangedEvent' to obtain its serialized id", windowInfo.getId());
        }

        Screen editor;
        OpenMode openMode = getScreenOpenMode(requestedState.getNestedRoute(), screenRoute, ui);

        if (isLegacyScreen(windowInfo.getControllerClass())) {
            editor = ui.getScreens().create(windowInfo.getId(), openMode, new MapScreenOptions(options));
        } else {
            editor = ui.getScreens().create(windowInfo.getId(), openMode);
        }

        if (MapUtils.isNotEmpty(options)) {
            Entity entity = (Entity) options.get(WindowParams.ITEM.name());
            //noinspection unchecked
            ((EditorScreen<Entity>) editor).setEntityToEdit(entity);
        }

        return editor;
    }

    protected OpenMode getScreenOpenMode(String requestedRoute, String screenRoute, AppUI ui) {
        if (StringUtils.isEmpty(screenRoute)) {
            return OpenMode.NEW_TAB;
        }

        String currentRoute = ui.getHistory()
                .getNow()
                .getNestedRoute();

        return requestedRoute.startsWith(currentRoute + '/')
                ? OpenMode.THIS_TAB
                : OpenMode.NEW_TAB;
    }

    @Nullable
    protected Map<String, Object> createEditorScreenOptions(WindowInfo windowInfo, NavigationState requestedState, AppUI ui) {
        UrlChangeHandler urlChangeHandler = ui.getUrlChangeHandler();

        String idParam = MapUtils.isNotEmpty(requestedState.getParams())
                ? requestedState.getParams().get("id")
                : null;

        if (StringUtils.isEmpty(idParam)) {
            return null;
        }

        Class<? extends Entity> entityClass = EditorTypeExtractor.extractEntityClass(windowInfo);
        if (entityClass == null) {
            return null;
        }

        MetaClass metaClass = metadata.getClassNN(entityClass);

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            urlChangeHandler.revertNavigationState();
            throw new AccessDeniedException(PermissionType.ENTITY_OP, EntityOp.READ, entityClass.getSimpleName());
        }

        Class<?> idType = metaClass.getPropertyNN("id")
                .getJavaType();
        Object id = UrlIdSerializer.deserializeId(idType, idParam);

        LoadContext<?> ctx = new LoadContext(metaClass);
        ctx.setId(id);
        ctx.setView(View.MINIMAL);

        Entity entity = dataManager.load(ctx);
        if (entity == null) {
            urlChangeHandler.revertNavigationState();
            throw new EntityAccessException(metaClass, id);
        }

        return ParamsMap.of(WindowParams.ITEM.name(), entity);
    }

    protected boolean isEditor(WindowInfo windowInfo) {
        return EditorScreen.class.isAssignableFrom(windowInfo.getControllerClass());
    }

    protected boolean isLegacyScreen(Class<? extends FrameOwner> controllerClass) {
        return LegacyFrame.class.isAssignableFrom(controllerClass);
    }
}

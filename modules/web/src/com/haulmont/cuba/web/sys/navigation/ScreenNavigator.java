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

package com.haulmont.cuba.web.sys.navigation;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.app.ui.navigation.notfoundwindow.NotFoundScreen;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Component(ScreenNavigator.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ScreenNavigator {

    public static final String NAME = "cuba_ScreenNavigator";

    private static final Logger log = LoggerFactory.getLogger(ScreenNavigator.class);

    protected UrlChangeHandler owner;
    protected AppUI ui;

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected Metadata metadata;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Security security;

    public ScreenNavigator(UrlChangeHandler owner, AppUI ui) {
        this.owner = owner;
        this.ui = ui;
    }

    /**
     * INTERNAL
     */
    protected ScreenNavigator() {
    }

    public void handleScreenNavigation(NavigationState requestedState) {
        if (handleRootChange(requestedState)) {
            return;
        }
        if (handleScreenChange(requestedState)) {
            return;
        }
        if (handleParamsChange(requestedState)) {
            return;
        }
        if (handleCurrentRootNavigation(requestedState)) {
            return;
        }

        log.debug("Unable to handle screen navigation for requested state: {}", requestedState);
        owner.revertNavigationState();
    }

    protected boolean handleCurrentRootNavigation(NavigationState requestedState) {
        if (!currentRootNavigated(requestedState)) {
            return false;
        }

        for (Screens.WindowStack windowStack : owner.getOpenedScreens().getWorkAreaStacks()) {
            boolean closed = owner.closeWindowStack(windowStack);
            if (!closed) {
                owner.revertNavigationState();
                return true;
            }
        }

        return true;
    }

    protected boolean currentRootNavigated(NavigationState requestedState) {
        return !owner.isRootState(ui.getHistory().getNow())
                && owner.isRootState(requestedState);
    }

    protected boolean handleRootChange(NavigationState requestedState) {
        if (!rootChanged(requestedState)
                || NavigationState.EMPTY == requestedState) {
            return false;
        }

        NavigationFilter.AccessCheckResult result = owner.navigationAllowed(requestedState);
        if (result.isRejected()) {
            if (StringUtils.isNotEmpty(result.getMessage())) {
                owner.showNotification(result.getMessage());
            }
            owner.revertNavigationState();
            return true;
        }

        log.debug("Navigation between root screens is not supported");
        owner.revertNavigationState();

        return true;
    }

    protected boolean rootChanged(NavigationState requestedState) {
        Screen rootScreen = owner.getOpenedScreens().getRootScreenOrNull();
        String currentRootRoute = owner.getResolvedState(rootScreen).getRoot();

        return StringUtils.isNoneEmpty(currentRootRoute)
                && !Objects.equals(currentRootRoute, requestedState.getRoot());
    }

    protected boolean handleScreenChange(NavigationState requestedState) {
        if (!screenChanged(requestedState)) {
            return false;
        }

        String requestedRoute = requestedState.getNestedRoute();
        if (requestedRoute == null || requestedRoute.isEmpty()) {
            log.debug("Unable to handle state with empty route '{}'", requestedState);
            owner.revertNavigationState();
            return true;
        }

        String[] routeParts = {requestedRoute};
        if (windowConfig.findWindowInfoByRoute(requestedRoute) == null) {
            routeParts = requestedRoute.split("/");
        }

        if (routeParts.length > 2) {
            log.info("Unable to perform navigation to requested state '{}'. " +
                    "Only two nested routes navigation is supported", requestedRoute);
            owner.revertNavigationState();
            return true;
        }

        List<Pair<String, WindowInfo>> routeWindowInfos = new ArrayList<>(routeParts.length);
        for (String routePart : routeParts) {
            routeWindowInfos.add(new Pair<>(routePart, windowConfig.findWindowInfoByRoute(routePart)));
        }

        for (Pair<String, WindowInfo> entry : routeWindowInfos) {
            if (entry.getSecond() == null) {
                log.info("No registered screen found for route: '{}'", entry.getFirst());

                owner.revertNavigationState();
                handle404(entry.getFirst());

                return true;
            }

            if (isRootRoute(entry.getSecond())) {
                log.info("Unable navigate to '{}' as nested screen", entry.getSecond().getId());
                owner.revertNavigationState();

                return true;
            }
        }

        int subRouteIdx = 0;
        NavigationState currentState = ui.getHistory().getNow();
        String[] currentRouteParts = currentState.getNestedRoute()
                .split("/");

        for (Pair<String, WindowInfo> entry : routeWindowInfos) {
            if (skipNavigation(requestedState, subRouteIdx, currentState, currentRouteParts, entry.getFirst())) {
                subRouteIdx++;

                continue;
            }

            openScreen(requestedState, entry.getFirst(), entry.getSecond());

            subRouteIdx++;
        }

        return true;
    }

    protected boolean skipNavigation(NavigationState requestedState, int i, NavigationState currentState,
                                     String[] currentRouteParts, String screenRoute) {
        if (!requestedState.asRoute().startsWith(currentState.asRoute() + '/')) {
            return false;
        }

        return i < currentRouteParts.length
                && currentRouteParts[i].equals(screenRoute);
    }

    protected boolean screenChanged(NavigationState requestedState) {
        if (NavigationState.EMPTY == requestedState
                || owner.isRootState(requestedState)) {
            return false;
        }

        Screen currentScreen = owner.findActiveScreenByState(ui.getHistory().getNow());

        if (currentScreen == null) {
            Iterator<Screen> screensIterator = owner.getOpenedScreens().getCurrentBreadcrumbs().iterator();
            currentScreen = screensIterator.hasNext()
                    ? screensIterator.next()
                    : null;
        }

        if (currentScreen == null) {
            return true;
        }

        NavigationState currentState = owner.getResolvedState(currentScreen);
        return !Objects.equals(currentState.getStateMark(), requestedState.getStateMark())
                || !Objects.equals(currentState.getNestedRoute(), requestedState.getNestedRoute());
    }

    protected void openScreen(NavigationState requestedState, String screenRoute, WindowInfo windowInfo) {
        if (isNotPermittedToNavigate(requestedState, windowInfo)) {
            return;
        }

        Screen screen = createScreen(requestedState, screenRoute, windowInfo);

        if (screen == null) {
            log.debug("Unable to open screen '{}' for requested route '{}'", windowInfo.getId(),
                    requestedState.getNestedRoute());

            owner.revertNavigationState();
            return;
        }

        if (StringUtils.isNotEmpty(screenRoute)
                && requestedState.getNestedRoute().endsWith(screenRoute)) {

            if (MapUtils.isNotEmpty(requestedState.getParams())) {
                UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                        new UrlParamsChangedEvent(screen, requestedState.getParams()));
            }

            ((WebWindow) screen.getWindow())
                    .setResolvedState(requestedState);
        }

        screen.show();
    }

    protected boolean isNotPermittedToNavigate(NavigationState requestedState, WindowInfo windowInfo) {
        boolean screenPermitted = security.isScreenPermitted(windowInfo.getId());
        if (!screenPermitted) {
            owner.revertNavigationState();
            throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
        }

        NavigationFilter.AccessCheckResult accessCheckResult = owner.navigationAllowed(requestedState);
        if (accessCheckResult.isRejected()) {
            if (StringUtils.isNotEmpty(accessCheckResult.getMessage())) {
                owner.showNotification(accessCheckResult.getMessage());
            }
            owner.revertNavigationState();

            return true;
        }
        return false;
    }

    protected Screen createScreen(NavigationState requestedState, String screenRoute, WindowInfo windowInfo) {
        Screen screen;

        if (isEditor(windowInfo)) {
            screen = createEditor(windowInfo, screenRoute, requestedState);
        } else {
            OpenMode openMode = getScreenOpenMode(requestedState.getNestedRoute(), screenRoute);
            screen = ui.getScreens().create(windowInfo.getId(), openMode);
        }

        return screen;
    }

    protected OpenMode getScreenOpenMode(String requestedRoute, String screenRoute) {
        if (StringUtils.isEmpty(screenRoute)) {
            return OpenMode.NEW_TAB;
        }

        String currentRoute = ui.getHistory().getNow().getNestedRoute();

        return requestedRoute.startsWith(currentRoute + '/')
                ? OpenMode.THIS_TAB
                : OpenMode.NEW_TAB;
    }

    protected Screen createEditor(WindowInfo windowInfo, String screenRoute, NavigationState requestedState) {
        Map<String, Object> options = createEditorScreenOptions(windowInfo, requestedState);

        if (MapUtils.isEmpty(options)) {
            log.info("Unable to load entity for editor: '{}'. " +
                    "Subscribe for 'UrlParamsChangedEvent' to obtain its serialized id", windowInfo.getId());
        }

        Screen editor;
        OpenMode openMode = getScreenOpenMode(requestedState.getNestedRoute(), screenRoute);

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

    @Nullable
    protected Map<String, Object> createEditorScreenOptions(WindowInfo windowInfo, NavigationState requestedState) {
        String idParam = MapUtils.isNotEmpty(requestedState.getParams())
                ? requestedState.getParams().get("id")
                : null;

        if (StringUtils.isEmpty(idParam)) {
            return null;
        }

        Class<? extends Entity> entityClass = extractEntityClass(windowInfo);
        if (entityClass == null) {
            return null;
        }

        MetaClass metaClass = metadata.getClassNN(entityClass);

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            owner.revertNavigationState();
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
            owner.revertNavigationState();
            throw new EntityAccessException(metaClass, id);
        }

        return ParamsMap.of(WindowParams.ITEM.name(), entity);
    }

    @Nullable
    protected Class<? extends Entity> extractEntityClass(WindowInfo windowInfo) {
        Class controllerClass = windowInfo.getControllerClass();

        Class<? extends Entity> entityClass = extractEntityTypeByInterface(controllerClass);
        if (entityClass == null) {
            entityClass = extractEntityTypeByClass(controllerClass);
        }

        return entityClass;
    }

    @Nullable
    protected Class<? extends Entity> extractEntityTypeByInterface(Class controllerClass) {
        while (controllerClass != null
                && !Arrays.asList(controllerClass.getInterfaces()).contains(EditorScreen.class)) {
            controllerClass = controllerClass.getSuperclass();
        }

        if (controllerClass == null) {
            return null;
        }

        Class<? extends Entity> entityClass = null;

        for (Type genericInterface : controllerClass.getGenericInterfaces()) {
            if (!(genericInterface instanceof ParameterizedType)) {
                continue;
            }

            ParameterizedType paramType = (ParameterizedType) genericInterface;
            String typeName = paramType.getRawType().getTypeName();

            if (!EditorScreen.class.getName().equals(typeName)) {
                continue;
            }

            if (paramType.getActualTypeArguments().length > 0) {
                Type typeArg = paramType.getActualTypeArguments()[0];

                if (typeArg instanceof Class
                        && Entity.class.isAssignableFrom((Class<?>) typeArg)) {
                    //noinspection unchecked
                    entityClass = (Class<? extends Entity>) typeArg;

                    break;
                }
            }
        }

        return entityClass;
    }

    @Nullable
    protected Class<? extends Entity> extractEntityTypeByClass(Class controllerClass) {
        while (controllerClass != null
                && !isAbstractEditor(controllerClass.getSuperclass())
                && !isStandardEditor(controllerClass.getSuperclass())) {
            controllerClass = controllerClass.getSuperclass();
        }

        if (controllerClass == null
                || (!isAbstractEditor(controllerClass.getSuperclass())
                && !isStandardEditor(controllerClass.getSuperclass()))) {
            return null;
        }

        if (!(controllerClass.getGenericSuperclass() instanceof ParameterizedType)) {
            return null;
        }

        Class<? extends Entity> entityClass = null;

        ParameterizedType paramType = (ParameterizedType) controllerClass.getGenericSuperclass();
        Type typeArg = paramType.getActualTypeArguments()[0];

        if (typeArg instanceof Class
                && Entity.class.isAssignableFrom((Class<?>) typeArg)) {
            //noinspection unchecked
            entityClass = (Class<? extends Entity>) typeArg;
        }

        return entityClass;
    }

    protected boolean handleParamsChange(NavigationState requestedState) {
        if (!paramsChanged(requestedState)) {
            return false;
        }

        Screen screen = owner.findActiveScreenByState(requestedState);
        if (screen == null) {
            log.debug("Unable to find info corresponding to state: {}", requestedState);

            return true;
        }

        Map<String, String> params = requestedState.getParams();
        if (params == null) {
            params = Collections.emptyMap();
        }

        UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                new UrlParamsChangedEvent(screen, params));

        return true;
    }

    protected void handle404(String route) {
        Map<String, Object> params = ParamsMap.of("requestedRoute", route);
        MapScreenOptions options = new MapScreenOptions(params);

        ui.getScreens().create(NotFoundScreen.class, OpenMode.NEW_TAB, options)
                .show();
    }

    protected boolean navigationIsNotPermitted(WindowInfo windowInfo) {
        WindowInfo loginWindowInfo = windowConfig.getWindowInfo("loginWindow");
        WindowInfo mainWindowInfo = windowConfig.getWindowInfo("mainWindow");

        return loginWindowInfo.equals(windowInfo)
                || mainWindowInfo.equals(windowInfo);
    }

    protected boolean paramsChanged(NavigationState requestedState) {
        String currentParams = owner.getResolvedState(owner.getActiveScreen())
                .getParamsString();

        return !Objects.equals(currentParams, requestedState.getParamsString());
    }

    protected boolean isEditor(WindowInfo windowInfo) {
        return EditorScreen.class.isAssignableFrom(windowInfo.getControllerClass());
    }

    protected boolean isAbstractEditor(Class controllerClass) {
        return AbstractEditor.class == controllerClass;
    }

    protected boolean isStandardEditor(Class controllerClass) {
        return StandardEditor.class == controllerClass;
    }

    protected boolean isLegacyScreen(Class<? extends FrameOwner> controllerClass) {
        return LegacyFrame.class.isAssignableFrom(controllerClass);
    }

    protected boolean isRootRoute(WindowInfo windowInfo) {
        return windowInfo != null && windowInfo.getRouteDefinition().isRoot();
    }
}

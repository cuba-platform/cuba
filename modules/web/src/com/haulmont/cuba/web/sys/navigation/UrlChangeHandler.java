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

package com.haulmont.cuba.web.sys.navigation;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.CloseOriginType;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.ui.navigation.notfoundwindow.NotFoundScreen;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.haulmont.cuba.web.gui.UrlHandlingMode;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.sys.RedirectHandler;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter.AccessCheckResult;
import com.vaadin.server.Page;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.haulmont.cuba.web.sys.navigation.UrlTools.pushState;
import static com.haulmont.cuba.web.sys.navigation.UrlTools.replaceState;

public class UrlChangeHandler {

    private static final Logger log = LoggerFactory.getLogger(UrlChangeHandler.class);

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected Metadata metadata;

    @Inject
    protected Messages messages;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected List<NavigationFilter> accessFilters;

    @Inject
    protected BeanLocator beanLocator;

    @Inject
    protected Security security;

    protected AppUI ui;

    public UrlChangeHandler(AppUI ui) {
        this.ui = ui;
    }

    public void handleUrlChange(Page.PopStateEvent event) {
        if (notSuitableUrlHandlingMode()) {
            log.debug("UrlChangeHandler is disabled for {} URL handling mode", webConfig.getUrlHandlingMode());
            return;
        }

        int hashIdx = event.getUri().indexOf("#");
        NavigationState requestedState = hashIdx < 0
                ? NavigationState.EMPTY
                : UrlTools.parseState(event.getUri().substring(hashIdx + 1));

        if (requestedState == null) {
            log.debug("Unable to handle requested state: \"{}\"", Page.getCurrent().getUriFragment());
            reloadApp();
            return;
        }

        if (!App.getInstance().getConnection().isAuthenticated()) {
            handleNoAuthNavigation(requestedState);
            return;
        }

        __handleUrlChange(requestedState);
    }

    protected void __handleUrlChange(NavigationState requestedState) {
        boolean historyNavHandled = handleHistoryNavigation(requestedState);
        if (!historyNavHandled) {
            handleScreenNavigation(requestedState);
        }
    }

    protected boolean handleHistoryNavigation(NavigationState requestedState) {
        boolean backward = getHistory().searchBackward(requestedState)
                || (getHistory().searchBackward(requestedState) && findActiveScreenByState(requestedState) != null);
        boolean forward = getHistory().searchForward(requestedState);

        if (backward) {
            handleHistoryBackward(requestedState);
        } else if (forward) {
            handleHistoryForward();
        }

        return backward || forward;
    }

    protected void handleHistoryBackward(NavigationState requestedState) {
        AccessCheckResult accessCheckResult = navigationAllowed(requestedState);
        if (accessCheckResult.isRejected()) {
            showNotification(accessCheckResult.getMessage());
            revertNavigationState();
            return;
        }

        Screen prevScreen = findScreenByState(requestedState);
        if (prevScreen == null && StringUtils.isNotEmpty(requestedState.getStateMark())) {
            revertNavigationState();
            return;
        }

        if (rootState(requestedState)) {
            handleCurrentRootNavigation(requestedState);
        }

        Screen lastOpenedScreen = findActiveScreenByState(getHistory().getNow());
        if (lastOpenedScreen != null) {
            OperationResult screenCloseResult = lastOpenedScreen
                    .getWindow().getFrameOwner()
                    .close(FrameOwner.WINDOW_CLOSE_ACTION)
                    .then(() -> proceedHistoryBackward(requestedState));

            if (OperationResult.Status.FAIL == screenCloseResult.getStatus()
                    || OperationResult.Status.UNKNOWN == screenCloseResult.getStatus()) {
                revertNavigationState();
            }
        } else {
            proceedHistoryBackward(requestedState);
        }
    }

    protected void proceedHistoryBackward(NavigationState requestedState) {
        getHistory().backward();
        selectScreen(findActiveScreenByState(requestedState));
        replaceState(requestedState.asRoute());
    }

    protected void handleHistoryForward() {
        Screen currentScreen = findActiveScreenByState(getHistory().getNow());
        if (currentScreen == null) {
            currentScreen = getAnyCurrentScreen();
        }

        String route = getResolvedState(currentScreen).asRoute();

        pushState(route);
    }

    protected void handleScreenNavigation(NavigationState requestedState) {
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
        revertNavigationState();
    }

    protected boolean handleCurrentRootNavigation(NavigationState requestedState) {
        if (!currentRootNavigated(requestedState)) {
            return false;
        }

        for (Screens.WindowStack windowStack : getOpenedScreens().getWorkAreaStacks()) {
            boolean closed = closeWindowStack(windowStack);
            if (!closed) {
                revertNavigationState();
                return false;
            }
        }

        return true;
    }

    protected boolean currentRootNavigated(NavigationState requestedState) {
        return !rootState(getHistory().getNow()) && rootState(requestedState);
    }

    protected boolean handleRootChange(NavigationState requestedState) {
        if (!rootChanged(requestedState)
                || NavigationState.EMPTY == requestedState) {
            return false;
        }

        AccessCheckResult result = navigationAllowed(requestedState);
        if (result.isRejected()) {
            showNotification(result.getMessage());
            revertNavigationState();
            return true;
        }

        log.debug("Navigation between root screens is not supported");
        revertNavigationState();

        return true;
    }

    protected boolean rootChanged(NavigationState requestedState) {
        Screen rootScreen = getOpenedScreens().getRootScreenOrNull();
        String currentRootRoute = getResolvedState(rootScreen).getRoot();

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
            revertNavigationState();
            return true;
        }

        WindowInfo windowInfo = windowConfig.findWindowInfoByRoute(requestedRoute);
        if (windowInfo != null) {
            return openScreen(requestedState, windowInfo);
        }

        String[] routeParts = requestedRoute.split("/");
        if (routeParts.length > 2) {
            log.debug("Unable to perform navigation to requested state '{}'. " +
                    "Only two nested routes navigation is supported", requestedRoute);
            revertNavigationState();
            return true;
        }

        for (String subRoute : routeParts) {
            windowInfo = windowConfig.findWindowInfoByRoute(subRoute);
            if (windowInfo == null) {
                handle404(subRoute);
                return true;
            }

            openScreen(requestedState, windowInfo);
        }

        return true;
    }

    protected boolean screenChanged(NavigationState requestedState) {
        if (NavigationState.EMPTY == requestedState) {
            return false;
        }

        Screen currentScreen = findActiveScreenByState(getHistory().getNow());

        if (currentScreen == null) {
            Iterator<Screen> screensIterator = getOpenedScreens().getCurrentBreadcrumbs().iterator();
            currentScreen = screensIterator.hasNext()
                    ? screensIterator.next()
                    : null;
        }

        if (currentScreen == null) {
            return true;
        }

        NavigationState currentState = getResolvedState(currentScreen);
        return !Objects.equals(currentState.getStateMark(), requestedState.getStateMark())
                || !Objects.equals(currentState.getNestedRoute(), requestedState.getNestedRoute());
    }

    protected boolean openScreen(NavigationState requestedState, WindowInfo windowInfo) {
        boolean screenPermitted = security.isScreenPermitted(windowInfo.getId());
        if (!screenPermitted) {
            revertNavigationState();
            throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
        }

        AccessCheckResult accessCheckResult = navigationAllowed(requestedState);
        if (accessCheckResult.isRejected()) {
            revertNavigationState();

            String msg = accessCheckResult.getMessage();
            if (msg != null && !msg.isEmpty()) {
                showNotification(msg);
            }

            return true;
        }

        Screen screen;

        if (isEditor(windowInfo)) {
            screen = createEditor(windowInfo, requestedState);
            if (screen == null) {
                log.debug("Unable to open screen '{}' for requested route '{}'",
                        windowInfo, requestedState.getNestedRoute());
                revertNavigationState();
                return true;
            }
        } else {
            OpenMode openMode = getScreenOpenMode(requestedState.getNestedRoute(), windowInfo);
            screen = getScreens().create(windowInfo.getId(), openMode);
        }

        String screenRoute = windowConfig.findRoute(windowInfo.getId());

        if (StringUtils.isNotEmpty(screenRoute)
                && requestedState.getNestedRoute().endsWith(screenRoute)) {

            if (MapUtils.isNotEmpty(requestedState.getParams())) {
                UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                        new UrlParamsChangedEvent(screen, requestedState.getParams()));
            }

            ((WebWindow) screen.getWindow()).setResolvedState(requestedState);
        }

        screen.show();

        return true;
    }

    protected OpenMode getScreenOpenMode(String route, WindowInfo windowInfo) {
        String screenRoute = windowConfig.findRoute(windowInfo.getId());

        //noinspection ConstantConditions
        return route.startsWith(screenRoute)
                ? OpenMode.NEW_TAB
                : OpenMode.THIS_TAB;
    }

    protected boolean isEditor(WindowInfo windowInfo) {
        return EditorScreen.class.isAssignableFrom(windowInfo.getControllerClass());
    }

    protected Screen createEditor(WindowInfo windowInfo, NavigationState requestedState) {
        Map<String, Object> screenOptions = createEditorScreenOptions(windowInfo, requestedState);
        if (screenOptions.isEmpty()) {
            return null;
        }

        Screen editor;
        OpenMode openMode = getScreenOpenMode(requestedState.getNestedRoute(), windowInfo);

        if (LegacyFrame.class.isAssignableFrom(windowInfo.getControllerClass())) {
            editor = getScreens().create(windowInfo.getId(), openMode, new MapScreenOptions(screenOptions));
        } else {
            editor = getScreens().create(windowInfo.getId(), openMode);
        }

        Entity entity = (Entity) screenOptions.get(WindowParams.ITEM.name());
        //noinspection unchecked
        ((EditorScreen<Entity>) editor).setEntityToEdit(entity);

        return editor;
    }

    protected Map<String, Object> createEditorScreenOptions(WindowInfo windowInfo, NavigationState requestedState) {
        Type screenSuperclass = windowInfo.getControllerClass().getGenericSuperclass();

        if (!(screenSuperclass instanceof ParameterizedType)) {
            return Collections.emptyMap();
        }

        String idParam = requestedState.getParams().get("id");
        if (StringUtils.isEmpty(idParam)) {
            return Collections.emptyMap();
        }

        ParameterizedType parameterizedEditor = (ParameterizedType) screenSuperclass;
        //noinspection unchecked
        Class<? extends Entity> entityClass = (Class<? extends Entity>) parameterizedEditor.getActualTypeArguments()[0];
        MetaClass metaClass = metadata.getClassNN(entityClass);

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            revertNavigationState();
            throw new AccessDeniedException(PermissionType.ENTITY_OP, EntityOp.READ, entityClass.getSimpleName());
        }

        Class<?> idType = metaClass.getPropertyNN("id").getJavaType();
        Object id = UrlIdSerializer.deserializeId(idType, idParam);

        LoadContext<?> ctx = new LoadContext(metaClass);
        ctx.setId(id);
        ctx.setView(View.MINIMAL);

        Entity entity = dataManager.load(ctx);
        if (entity == null) {
            return Collections.emptyMap();
        }

        return ParamsMap.of(WindowParams.ITEM.name(), entity);
    }

    protected boolean handleParamsChange(NavigationState requestedState) {
        if (!paramsChanged(requestedState)) {
            return false;
        }

        Screen screen = findActiveScreenByState(requestedState);
        UiControllerUtils.fireEvent(screen, UrlParamsChangedEvent.class,
                new UrlParamsChangedEvent(screen, requestedState.getParams()));

        return true;
    }

    protected boolean paramsChanged(NavigationState requestedState) {
        String currentParams = getResolvedState(getAnyCurrentScreen()).getParamsString();
        return !Objects.equals(currentParams, requestedState.getParamsString());
    }

    protected void reloadApp() {
        String url = ControllerUtils.getLocationWithoutParams() + "?restartApp";
        ui.getPage().open(url, "_self");
    }

    protected Screen getAnyCurrentScreen() {
        Iterator<Screen> dialogsIterator = getOpenedScreens().getDialogScreens().iterator();
        if (dialogsIterator.hasNext()) {
            return dialogsIterator.next();
        }

        Iterator<Screen> screensIterator = getOpenedScreens().getCurrentBreadcrumbs().iterator();
        if (screensIterator.hasNext()) {
            return screensIterator.next();
        }

        return getOpenedScreens().getRootScreenOrNull();
    }

    protected boolean rootState(NavigationState requestedState) {
        return StringUtils.isEmpty(requestedState.getStateMark()) && StringUtils.isEmpty(requestedState.getNestedRoute());
    }

    protected String getStateMark(Screen screen) {
        return String.valueOf(((WebWindow) screen.getWindow()).getUrlStateMark());
    }

    protected Screen findActiveScreenByState(NavigationState requestedState) {
        return findScreenByState(getOpenedScreens().getActiveScreens(), requestedState);
    }

    protected Screen findScreenByState(NavigationState requestedState) {
        return findScreenByState(getOpenedScreens().getAll(), requestedState);
    }

    protected Screen findScreenByState(Collection<Screen> screens, NavigationState requestedState) {
        return screens.stream()
                .filter(s -> Objects.equals(requestedState.getStateMark(), getStateMark(s)))
                .findFirst().orElse(null);
    }

    protected void selectScreen(Screen screen) {
        if (screen == null) {
            return;
        }
        for (Screens.WindowStack windowStack : getOpenedScreens().getWorkAreaStacks()) {
            Iterator<Screen> breadCrumbs = windowStack.getBreadcrumbs().iterator();
            if (breadCrumbs.hasNext() && breadCrumbs.next() == screen) {
                windowStack.select();
                return;
            }
        }
    }

    protected void showNotification(String msg) {
        ui.getNotifications().create(NotificationType.TRAY)
                .withCaption(msg)
                .show();
    }

    protected void revertNavigationState() {
        Screen screen = findActiveScreenByState(getHistory().getNow());
        if (screen == null) {
            screen = getAnyCurrentScreen();
        }
        pushState(getResolvedState(screen).asRoute());
    }

    protected void handleNoAuthNavigation(NavigationState requestedState) {
        if (Objects.equals(getHistory().getNow(), requestedState)) {
            return;
        }

        String nestedRoute = requestedState.getNestedRoute();
        if (StringUtils.isNotEmpty(nestedRoute)) {
            RedirectHandler redirectHandler = beanLocator.getPrototype(RedirectHandler.NAME, ui);
            redirectHandler.schedule(requestedState);
            App.getInstance().setRedirectHandler(redirectHandler);
        } else if (rootState(requestedState)) {
            Screen rootScreen = getOpenedScreens().getRootScreenOrNull();
            if (rootScreen != null) {
                pushState(getResolvedState(rootScreen).asRoute());
            }
        }

        showNotification(messages.getMainMessage("navigation.shouldLogInFirst"));
    }

    protected boolean notSuitableUrlHandlingMode() {
        return UrlHandlingMode.URL_ROUTES != webConfig.getUrlHandlingMode();
    }

    protected AccessCheckResult navigationAllowed(NavigationState requestedState) {
        for (NavigationFilter filter : accessFilters) {
            AccessCheckResult result = filter.allowed(getHistory().getNow(), requestedState);
            if (result.isRejected()) {
                return result;
            }
        }
        return AccessCheckResult.allowed();
    }

    protected Screens getScreens() {
        return ui.getScreens();
    }

    protected Screens.OpenedScreens getOpenedScreens() {
        return getScreens().getOpenedScreens();
    }

    protected History getHistory() {
        return ui.getHistory();
    }

    // Copied from WebAppWorkArea
    protected boolean closeWindowStack(Screens.WindowStack windowStack) {
        boolean closed = true;

        for (Screen screen : windowStack.getBreadcrumbs()) {
            if (isWindowClosePrevented(screen.getWindow(), CloseOriginType.CLOSE_BUTTON)) {
                closed = false;

                windowStack.select();

                break;
            }

            OperationResult closeResult = screen.close(FrameOwner.WINDOW_CLOSE_ACTION);
            if (closeResult.getStatus() != OperationResult.Status.SUCCESS) {
                closed = false;

                windowStack.select();

                break;
            }
        }
        return closed;
    }

    // Copied from WebAppWorkArea
    protected boolean isWindowClosePrevented(Window window, Window.CloseOrigin closeOrigin) {
        Window.BeforeCloseEvent event = new Window.BeforeCloseEvent(window, closeOrigin);
        ((WebWindow) window).fireBeforeClose(event);

        return event.isClosePrevented();
    }

    protected NavigationState getResolvedState(Screen screen) {
        return screen != null
                ? ((WebWindow) screen.getWindow()).getResolvedState()
                : NavigationState.EMPTY;
    }

    protected void handle404(String route) {
        Map<String, Object> params = ParamsMap.of("requestedRoute", route);
        MapScreenOptions options = new MapScreenOptions(params);

        getScreens().create(NotFoundScreen.class, OpenMode.NEW_TAB, options)
                .show();
    }

    /**
     * INTERNAL.
     * Used by {@link RedirectHandler}.
     *
     * @param requestedState new navigation requestedState
     */
    public void handleUrlChangeInternal(NavigationState requestedState) {
        if (notSuitableUrlHandlingMode()) {
            log.debug("UrlChangeHandler is disabled for {} URL handling mode", webConfig.getUrlHandlingMode());
            return;
        }
        __handleUrlChange(requestedState);
    }
}

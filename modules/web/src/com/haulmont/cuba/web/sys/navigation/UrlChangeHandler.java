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
import com.haulmont.cuba.core.global.AccessDeniedException;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.CloseOriginType;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
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
import com.haulmont.cuba.web.navigation.UrlParamsChangedEvent;
import com.haulmont.cuba.web.sys.RedirectHandler;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter;
import com.haulmont.cuba.web.sys.navigation.accessfilter.NavigationFilter.AccessCheckResult;
import com.vaadin.server.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public void handleUriChange(@SuppressWarnings("unused") Page.PopStateEvent event) {
        if (notSuitableUrlHandlingMode()) {
            return;
        }

        NavigationState requestedState = ui.getUrlRouting().getState();
        if (requestedState == null) {
            log.debug("Unable to handle requested state: \"{}\"", Page.getCurrent().getUriFragment());
            reloadApp();
            return;
        }

        if (!App.getInstance().getConnection().isAuthenticated()) {
            handleNoAuthNavigation(requestedState);
            return;
        }

        handleUriChange(requestedState);
    }

    protected void handleUriChange(NavigationState requestedState) {
        if (historyNavigation(requestedState)) {
            handleHistoryNavigation(requestedState);
        } else {
            handleScreenNavigation(requestedState);
        }
    }

    protected boolean historyNavigation(NavigationState requestedState) {
        return Objects.equals(requestedState, getHistory().getPrevious()) || Objects.equals(requestedState, getHistory().getNext());
    }

    protected void handleHistoryNavigation(NavigationState requestedState) {
        if (Objects.equals(requestedState, getHistory().getPrevious())) {
            handleHistoryBackward();
        } else {
            handleHistoryForward();
        }
    }

    protected void handleHistoryBackward() {
        NavigationState prevState = getHistory().getPrevious();
        AccessCheckResult accessCheckResult = navigationAllowed(prevState);
        if (!accessCheckResult.isAllowed()) {
            showNotification(accessCheckResult.getMessage());
            revertNavigationState();
            return;
        }

        Screen prevScreen = findScreenByState(prevState);
        //noinspection ConstantConditions
        if (prevScreen == null && StringUtils.isNotEmpty(prevState.getStateMark())) {
            revertNavigationState();
            showNotification(messages.getMainMessage("navigation.unableToGoBackward"));
            return;
        }

        Screen lastOpenedScreen = findActiveScreenByState(getHistory().getNow());
        if (lastOpenedScreen != null) {
            OperationResult screenCloseResult = lastOpenedScreen
                    .getWindow().getFrameOwner()
                    .close(FrameOwner.WINDOW_CLOSE_ACTION)
                    .then(this::proceedHistoryBackward);

            if (OperationResult.Status.FAIL == screenCloseResult.getStatus()
                    || OperationResult.Status.UNKNOWN == screenCloseResult.getStatus()) {
                revertNavigationState();
            }
        } else {
            proceedHistoryBackward();
        }
    }

    protected void proceedHistoryBackward() {
        NavigationState prevState = getHistory().backward();
        selectScreen(findActiveScreenByState(prevState));
        //noinspection ConstantConditions
        UrlTools.replaceState(prevState.asRoute());
    }

    protected void handleHistoryForward() {
        Screen currentScreen = findActiveScreenByState(getHistory().getNow());
        if (currentScreen == null) {
            currentScreen = getAnyCurrentScreen();
        }

        String route = getResolvedState(currentScreen).asRoute();

        UrlTools.pushState(route);
        showNotification(messages.getMainMessage("navigation.unableToGoForward"));
    }

    protected void handleScreenNavigation(NavigationState requestedState) {
        if (handleRootChange(requestedState)) {
            return;
        }
        if (handleScreenChange(requestedState)) {
            return;
        }
        if (handleParamsChanged(requestedState)) {
            return;
        }
        if (handleCurrentRootNavigated(requestedState)) {
            return;
        }
        revertNavigationState();
        log.info("Unable to handle screen navigation for requested state: {}", requestedState);
    }

    protected boolean handleCurrentRootNavigated(NavigationState requestedState) {
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
        if (!rootChanged(requestedState)) {
            return false;
        }

        AccessCheckResult result = navigationAllowed(requestedState);
        if (!result.isAllowed()) {
            showNotification(result.getMessage());
            revertNavigationState();
            return true;
        }

        showNotification(messages.getMainMessage("navigation.rootChangeIsNotSupported"));
        revertNavigationState();

        return true;
    }

    protected boolean rootChanged(NavigationState requestedState) {
        Screen rootScreen = getOpenedScreens().getRootScreenOrNull();
        if (rootScreen == null) {
            return false;
        }
        return !Objects.equals(getResolvedState(rootScreen).getRoot(), requestedState.getRoot());
    }

    protected boolean handleScreenChange(NavigationState requestedState) {
        if (!screenChanged(requestedState)) {
            return false;
        }

        // TODO: handle few opened screens
        WindowInfo windowInfo = windowConfig.findWindowInfoByRoute(requestedState.getNestedRoute());

        if (windowInfo == null) {
            handle404(requestedState);
            return false;
        }

        boolean screenPermitted = security.isScreenPermitted(windowInfo.getId());
        if (!screenPermitted) {
            revertNavigationState();
            throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
        }

        Screen screen;

        if (isEditor(windowInfo)) {
            screen = createEditor(windowInfo, requestedState);
            if (screen == null) {
                revertNavigationState();
                showNotification(messages.getMainMessage("navigation.failedToOpenEditor"));
                return true;
            }
        } else {
            screen = getScreens().create(windowInfo.getId(), OpenMode.NEW_TAB);
        }

        getScreens().show(screen);

        return true;
    }

    protected boolean screenChanged(NavigationState requestedState) {
        Screen currentScreen = findActiveScreenByState(getHistory().getNow());
        if (currentScreen == null) {
            Iterator<Screen> screensIterator = getOpenedScreens().getCurrentBreadcrumbs().iterator();
            currentScreen = screensIterator.hasNext() ? screensIterator.next() : null;
        }
        if (currentScreen == null) {
            return true;
        }

        NavigationState currentState = getResolvedState(currentScreen);
        return !Objects.equals(currentState.getStateMark(), requestedState.getStateMark())
                || !Objects.equals(currentState.getNestedRoute(), requestedState.getNestedRoute());
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
        if (LegacyFrame.class.isAssignableFrom(windowInfo.getControllerClass())) {
            editor = getScreens().create(windowInfo.getId(), OpenMode.NEW_TAB, new MapScreenOptions(screenOptions));
        } else {
            editor = getScreens().create(windowInfo.getId(), OpenMode.NEW_TAB);
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
        Object id = UrlTools.deserializeId(idType, idParam);

        LoadContext<?> ctx = new LoadContext(metaClass);
        ctx.setId(id);
        ctx.setView(View.MINIMAL);

        Entity entity = dataManager.load(ctx);
        if (entity == null) {
            return Collections.emptyMap();
        }

        return ParamsMap.of(WindowParams.ITEM.name(), entity);
    }

    protected boolean handleParamsChanged(NavigationState requestedState) {
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

    protected boolean rootScreenHasWorkArea() {
        return getOpenedScreens().getRootScreenOrNull() instanceof Window.HasWorkArea;
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
        ui.getNotifications().create()
                .setCaption(msg)
                .setType(Notifications.NotificationType.TRAY)
                .show();
    }

    protected void revertNavigationState() {
        Screen screen = findActiveScreenByState(getHistory().getNow());
        if (screen == null) {
            screen = getAnyCurrentScreen();
        }
        UrlTools.pushState(getResolvedState(screen).asRoute());
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
        }

        showNotification(messages.getMainMessage("navigation.shouldLogInFirst"));
    }

    protected boolean notSuitableUrlHandlingMode() {
        if (UrlHandlingMode.URL_ROUTES == webConfig.getUrlHandlingMode()) {
            return false;
        }
        log.debug("UrlChangeHandler is disabled for {} URL handling mode", webConfig.getUrlHandlingMode());
        return true;
    }

    protected AccessCheckResult navigationAllowed(NavigationState requestedState) {
        for (NavigationFilter filter : accessFilters) {
            AccessCheckResult result = filter.allowed(getHistory().getNow(), requestedState);
            if (!result.isAllowed()) {
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

                // focus tab
                windowStack.select();

                break;
            }

            OperationResult closeResult = screen.close(FrameOwner.WINDOW_CLOSE_ACTION);
            if (closeResult.getStatus() != OperationResult.Status.SUCCESS) {
                closed = false;

                // focus tab
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
        return ((WebWindow) screen.getWindow()).getResolvedState();
    }

    protected void handle404(NavigationState requestedState) {
        MapScreenOptions params = new MapScreenOptions(ParamsMap.of("requestedRoute", requestedState.getNestedRoute()));
        NotFoundScreen notFoundScreen = getScreens().create(NotFoundScreen.class, OpenMode.NEW_TAB, params);

        getScreens().show(notFoundScreen);
    }

    /**
     * INTERNAL.
     * Used by {@link RedirectHandler}.
     *
     * @param requestedState new navigation requestedState
     */
    public void handleUrlChangeInternal(NavigationState requestedState) {
        if (notSuitableUrlHandlingMode()) {
            return;
        }
        handleUriChange(requestedState);
    }
}

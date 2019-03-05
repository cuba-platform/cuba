/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.Dialogs.MessageDialogBuilder;
import com.haulmont.cuba.gui.Dialogs.OptionDialogBuilder;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Disposable;
import com.haulmont.cuba.gui.components.DialogWindow.WindowMode;
import com.haulmont.cuba.gui.components.Window.HasWorkArea;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.compatibility.SelectHandlerAdapter;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea.Mode;
import com.haulmont.cuba.gui.components.sys.WindowImplementation;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.GenericDataSupplier;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.logging.ScreenLifeCycle;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import com.haulmont.cuba.gui.model.impl.ScreenDataImpl;
import com.haulmont.cuba.gui.navigation.NavigationState;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.Screen.*;
import com.haulmont.cuba.gui.screen.compatibility.*;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.sys.*;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.gui.util.UnknownOperationResult;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebDialogWindow.GuiDialogWindow;
import com.haulmont.cuba.web.gui.components.WebTabWindow;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.sys.navigation.UrlTools;
import com.haulmont.cuba.web.widgets.*;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.perf4j.StopWatch;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.logging.UIPerformanceLogger.createStopWatch;
import static com.haulmont.cuba.gui.screen.FrameOwner.WINDOW_CLOSE_ACTION;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.*;

public class WebScreens implements Screens, WindowManager {

    private static final org.slf4j.Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    @Inject
    protected BeanLocator beanLocator;

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected Security security;
    @Inject
    protected UuidSource uuidSource;
    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected ScreenXmlLoader screenXmlLoader;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UserSettingService userSettingService;
    @Inject
    protected IconResolver iconResolver;
    @Inject
    protected Messages messages;
    @Inject
    protected Icons icons;
    @Inject
    protected WindowCreationHelper windowCreationHelper;
    @Inject
    protected AttributeAccessSupport attributeAccessSupport;
    @Inject
    protected ScreenHistorySupport screenHistorySupport;

    @Inject
    protected ScreenViewsLoader screenViewsLoader;

    @Inject
    protected WebConfig webConfig;
    @Inject
    protected ClientConfig clientConfig;

    protected AppUI ui;

    protected DataSupplier defaultDataSupplier = new GenericDataSupplier();

    public WebScreens(AppUI ui) {
        this.ui = ui;
    }

    @Override
    public <T extends Screen> T create(Class<T> requiredScreenClass, LaunchMode launchMode, ScreenOptions options) {
        checkNotNullArgument(requiredScreenClass);
        checkNotNullArgument(launchMode);
        checkNotNullArgument(options);

        WindowInfo windowInfo = getScreenInfo(requiredScreenClass).resolve();

        return createScreen(windowInfo, launchMode, options);
    }

    @Override
    public Screen create(String screenId, LaunchMode launchMode, ScreenOptions options) {
        checkNotNullArgument(screenId);
        checkNotNullArgument(launchMode);
        checkNotNullArgument(options);

        // load screen class only once
        WindowInfo windowInfo = windowConfig.getWindowInfo(screenId).resolve();

        return createScreen(windowInfo, launchMode, options);
    }

    protected <T extends Screen> T createScreen(WindowInfo windowInfo, LaunchMode launchMode, ScreenOptions options) {
        if (windowInfo.getType() != WindowInfo.Type.SCREEN) {
            throw new IllegalArgumentException(
                    String.format("Unable to create screen %s with type %s", windowInfo.getId(), windowInfo.getType())
            );
        }

        Class<T> resolvedScreenClass = (Class<T>) windowInfo.getControllerClass();

        // load XML document here in order to get metadata before Window creation, e.g. forceDialog from <dialogMode>
        Element element = loadScreenXml(windowInfo, options);

        ScreenOpenDetails openDetails = prepareScreenOpenDetails(resolvedScreenClass, element, launchMode);

        checkPermissions(openDetails.getOpenMode(), windowInfo);

        StopWatch createStopWatch = createStopWatch(ScreenLifeCycle.CREATE, windowInfo.getId());

        Window window = createWindow(windowInfo, resolvedScreenClass, openDetails);

        T controller = createController(windowInfo, window, resolvedScreenClass);

        // setup screen and controller

        setWindowId(controller, windowInfo.getId());
        setFrame(controller, window);
        setScreenContext(controller,
                new ScreenContextImpl(windowInfo, options,
                        this,
                        ui.getDialogs(),
                        ui.getNotifications(),
                        ui.getFragments(),
                        ui.getUrlRouting())
        );
        setScreenData(controller, new ScreenDataImpl());

        WindowImplementation windowImpl = (WindowImplementation) window;
        windowImpl.setFrameOwner(controller);
        windowImpl.setId(controller.getId());

        createStopWatch.stop();

        // load UI from XML

        StopWatch loadStopWatch = createStopWatch(ScreenLifeCycle.LOAD, windowInfo.getId());

        ComponentLoaderContext componentLoaderContext = new ComponentLoaderContext(options);
        componentLoaderContext.setFullFrameId(windowInfo.getId());
        componentLoaderContext.setCurrentFrameId(windowInfo.getId());
        componentLoaderContext.setFrame(window);

        if (element != null) {
            loadWindowFromXml(element, windowInfo, window, controller, componentLoaderContext);
        }

        loadStopWatch.stop();

        // inject top level screen dependencies
        StopWatch injectStopWatch = createStopWatch(ScreenLifeCycle.INJECTION, windowInfo.getId());

        UiControllerDependencyInjector dependencyInjector =
                beanLocator.getPrototype(UiControllerDependencyInjector.NAME, controller, options);
        dependencyInjector.inject();

        injectStopWatch.stop();

        // perform injection in nested fragments
        componentLoaderContext.executeInjectTasks();

        // run init

        StopWatch initStopWatch = createStopWatch(ScreenLifeCycle.INIT, windowInfo.getId());

        fireEvent(controller, InitEvent.class, new InitEvent(controller, options));

        initStopWatch.stop();

        componentLoaderContext.executeInitTasks();
        componentLoaderContext.executePostInitTasks();

        fireEvent(controller, AfterInitEvent.class, new AfterInitEvent(controller, options));

        return controller;
    }

    protected ScreenOpenDetails prepareScreenOpenDetails(Class<? extends Screen> resolvedScreenClass,
                                                         @Nullable Element element,
                                                         LaunchMode requiredLaunchMode) {
        if (!(requiredLaunchMode instanceof OpenMode)) {
            throw new UnsupportedOperationException("Unsupported LaunchMode " + requiredLaunchMode);
        }

        // check if we need to change launchMode to DIALOG
        boolean forceDialog = false;
        OpenMode launchMode = (OpenMode) requiredLaunchMode;

        if (element != null && element.element("dialogMode") != null) {
            String forceDialogAttr = element.element("dialogMode").attributeValue("forceDialog");
            if (StringUtils.isNotEmpty(forceDialogAttr)
                    && Boolean.parseBoolean(forceDialogAttr)) {
                launchMode = OpenMode.DIALOG;
            }
        }

        DialogMode dialogMode = resolvedScreenClass.getAnnotation(DialogMode.class);
        if (dialogMode != null && dialogMode.forceDialog()) {
            launchMode = OpenMode.DIALOG;
        }

        if (launchMode != OpenMode.DIALOG
                && launchMode != OpenMode.ROOT) {
            if (hasModalDialogWindow()) {
                launchMode = OpenMode.DIALOG;
                forceDialog = true;
            }
        }

        if (launchMode == OpenMode.THIS_TAB) {
            WebAppWorkArea workArea = getConfiguredWorkArea();

            switch (workArea.getMode()) {
                case SINGLE:
                    if (workArea.getSingleWindowContainer().getWindowContainer() == null) {
                        launchMode = OpenMode.NEW_TAB;
                    }
                    break;

                case TABBED:
                    TabSheetBehaviour tabSheetBehaviour = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

                    if (tabSheetBehaviour.getComponentCount() == 0) {
                        launchMode = OpenMode.NEW_TAB;
                    }
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported AppWorkArea mode");
            }
        } else if (launchMode == OpenMode.NEW_WINDOW) {
            launchMode = OpenMode.NEW_TAB;
        }

        return new ScreenOpenDetails(forceDialog, launchMode);
    }

    @Nullable
    protected Element loadScreenXml(WindowInfo windowInfo, ScreenOptions options) {
        String templatePath = windowInfo.getTemplate();

        if (StringUtils.isNotEmpty(templatePath)) {
            Map<String, Object> params = Collections.emptyMap();
            if (options instanceof MapScreenOptions) {
                params = ((MapScreenOptions) options).getParams();
            }
            return screenXmlLoader.load(templatePath, windowInfo.getId(), params);
        }

        return null;
    }

    protected <T extends Screen> void loadWindowFromXml(Element element, WindowInfo windowInfo, Window window, T controller,
                                                        ComponentLoaderContext componentLoaderContext) {
        LayoutLoader layoutLoader = beanLocator.getPrototype(LayoutLoader.NAME, componentLoaderContext);
        layoutLoader.setLocale(getLocale());
        layoutLoader.setMessagesPack(getMessagePack(windowInfo.getTemplate()));

        ComponentLoader<Window> windowLoader = layoutLoader.createWindowContent(window, element, windowInfo.getId());

        if (controller instanceof LegacyFrame) {
            screenViewsLoader.deployViews(element);

            initDsContext(window, element, componentLoaderContext);

            DsContext dsContext = ((LegacyFrame) controller).getDsContext();
            if (dsContext != null) {
                dsContext.setFrameContext(window.getContext());
            }
        }

        windowLoader.loadComponent();
    }

    protected void initDsContext(Window window, Element element, ComponentLoaderContext componentLoaderContext) {
        DsContext dsContext = loadDsContext(element);
        initDatasources(window, dsContext, componentLoaderContext.getParams());

        componentLoaderContext.setDsContext(dsContext);
    }

    protected DsContext loadDsContext(Element element) {
        DataSupplier dataSupplier;

        String dataSupplierClass = element.attributeValue("dataSupplier");
        if (StringUtils.isEmpty(dataSupplierClass)) {
            dataSupplier = defaultDataSupplier;
        } else {
            Class<Object> aClass = ReflectionHelper.getClass(dataSupplierClass);
            try {
                dataSupplier = (DataSupplier) aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to create data supplier for screen", e);
            }
        }

        //noinspection UnnecessaryLocalVariable
        DsContext dsContext = new DsContextLoader(dataSupplier).loadDatasources(element.element("dsContext"), null, null);
        return dsContext;
    }

    protected void initDatasources(Window window, DsContext dsContext, @SuppressWarnings("unused") Map<String, Object> params) {
        ((LegacyFrame) window.getFrameOwner()).setDsContext(dsContext);

        for (Datasource ds : dsContext.getAll()) {
            if (Datasource.State.NOT_INITIALIZED.equals(ds.getState()) && ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }
    }

    protected String getMessagePack(String descriptorPath) {
        if (descriptorPath.contains("/")) {
            descriptorPath = StringUtils.substring(descriptorPath, 0, descriptorPath.lastIndexOf("/"));
        }

        String messagesPack = descriptorPath.replaceAll("/", ".");
        int start = messagesPack.startsWith(".") ? 1 : 0;
        messagesPack = messagesPack.substring(start);
        return messagesPack;
    }

    protected Locale getLocale() {
        return userSessionSource.getUserSession().getLocale();
    }

    @Override
    public void show(Screen screen) {
        checkNotNullArgument(screen);
        checkNotYetOpened(screen);

        StopWatch uiPermissionsWatch = createStopWatch(ScreenLifeCycle.UI_PERMISSIONS, screen.getId());

        windowCreationHelper.applyUiPermissions(screen.getWindow());

        uiPermissionsWatch.stop();

        StopWatch beforeShowWatch = createStopWatch(ScreenLifeCycle.BEFORE_SHOW, screen.getId());

        fireEvent(screen, BeforeShowEvent.class, new BeforeShowEvent(screen));

        loadDataBeforeShow(screen);

        beforeShowWatch.stop();

        LaunchMode launchMode = screen.getWindow().getContext().getLaunchMode();

        if (launchMode instanceof OpenMode) {
            OpenMode openMode = (OpenMode) launchMode;

            switch (openMode) {
                case ROOT:
                    showRootWindow(screen);
                    break;

                case THIS_TAB:
                    showThisTabWindow(screen);
                    break;

                case NEW_WINDOW:
                case NEW_TAB:
                    showNewTabWindow(screen);
                    break;

                case DIALOG:
                    showDialogWindow(screen);
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported OpenMode " + openMode);
            }
        }

        userActionsLog.trace("Screen {} {} opened", screen.getId(), screen.getClass());

        afterShowWindow(screen);

        changeUrl(screen);

        StopWatch afterShowWatch = createStopWatch(ScreenLifeCycle.AFTER_SHOW, screen.getId());

        fireEvent(screen, AfterShowEvent.class, new AfterShowEvent(screen));

        afterShowWatch.stop();
    }

    @Override
    public OperationResult showFromNavigation(Screen screen) {
        LaunchMode launchMode = screen.getWindow().getContext().getLaunchMode();

        if (launchMode == OpenMode.NEW_TAB
            || launchMode == OpenMode.NEW_WINDOW) {
            WebAppWorkArea workArea = getConfiguredWorkArea();

            if (workArea.getMode() == Mode.SINGLE) {
                Collection<Screen> currentBreadcrumbs = workArea.getCurrentBreadcrumbs();

                if (!currentBreadcrumbs.isEmpty()) {
                    Iterator<Screen> iterator = currentBreadcrumbs.iterator();
                    OperationResult result = OperationResult.success();

                    // close all
                    while (result.getStatus() == OperationResult.Status.SUCCESS
                            && iterator.hasNext()) {

                        Screen previousScreen = iterator.next();
                        result = previousScreen.close(NAVIGATION_CLOSE_ACTION);
                    }

                    if (result.getStatus() != OperationResult.Status.SUCCESS) {
                        // if unsaved changes dialog is shown, we can continue later
                        return result.compose(() -> showFromNavigation(screen));
                    }
                }
            } else {
                int maxTabCount = webConfig.getMaxTabCount();
                if (maxTabCount > 0
                        && workArea.getOpenedTabCount() + 1 > maxTabCount) {
                    ui.getNotifications()
                            .create(NotificationType.WARNING)
                            .withCaption(messages.formatMainMessage("tooManyOpenTabs.message", maxTabCount))
                            .show();

                    return OperationResult.fail();
                }

                if (!UiControllerUtils.isMultipleOpen(screen)) {
                    Screen sameScreen = getTabbedScreensStacks(workArea)
                            .filter(windowStack -> windowStack.getBreadcrumbs().size() == 1) // never close non-top active screens
                            .map(windowStack -> windowStack.getBreadcrumbs().iterator().next())
                            .filter(tabScreen -> isAlreadyOpened(screen, tabScreen))
                            .findFirst()
                            .orElse(null);

                    if (sameScreen != null) {
                        OperationResult result = sameScreen.close(NAVIGATION_CLOSE_ACTION);
                        if (result.getStatus() != OperationResult.Status.SUCCESS) {
                            // if unsaved changes dialog is shown, we can continue later
                            return result.compose(() -> showFromNavigation(screen));
                        }
                    }
                }
            }
        }

        show(screen);

        return OperationResult.success();
    }

    protected void loadDataBeforeShow(Screen screen) {
        LoadDataBeforeShow annotation = screen.getClass().getAnnotation(LoadDataBeforeShow.class);
        if (annotation != null && annotation.value()) {
            UiControllerUtils.getScreenData(screen).loadAll();
        }
    }

    protected void changeUrl(Screen screen) {
        WebWindow webWindow = (WebWindow) screen.getWindow();
        Map<String, String> params = webWindow.getResolvedState() != null
                ? webWindow.getResolvedState().getParams()
                : Collections.emptyMap();

        ui.getUrlRouting().pushState(screen, params);
    }

    protected void checkNotYetOpened(Screen screen) {
        com.vaadin.ui.Component uiComponent = screen.getWindow()
                .unwrapComposition(com.vaadin.ui.Component.class);
        if (uiComponent.isAttached()) {
            throw new IllegalStateException("Screen is already opened " + screen.getId());
        }
    }

    protected void checkOpened(Screen screen) {
        com.vaadin.ui.Component uiComponent = screen.getWindow()
                .unwrapComposition(com.vaadin.ui.Component.class);
        if (!uiComponent.isAttached()) {
            throw new IllegalStateException("Screen is not opened " + screen.getId());
        }
    }

    protected void afterShowWindow(Screen screen) {
        WindowContext windowContext = screen.getWindow().getContext();

        if (!WindowParams.DISABLE_APPLY_SETTINGS.getBool(windowContext)) {
            applySettings(screen, getSettingsImpl(screen.getId()));
        }

        if (screen instanceof LegacyFrame) {
            if (!WindowParams.DISABLE_RESUME_SUSPENDED.getBool(windowContext)) {
                DsContext dsContext = ((LegacyFrame) screen).getDsContext();
                if (dsContext != null) {
                    ((DsContextImplementation) dsContext).resumeSuspended();
                }
            }
        }

        if (screen instanceof AbstractWindow) {
            AbstractWindow abstractWindow = (AbstractWindow) screen;
            if (abstractWindow.isAttributeAccessControlEnabled()) {
                attributeAccessSupport.applyAttributeAccess(abstractWindow, false);
            }
        } else {
            DisableAttributeAccessControl annotation = screen.getClass().getAnnotation(DisableAttributeAccessControl.class);
            if (annotation == null || !annotation.value()) {
                attributeAccessSupport.applyAttributeAccess(screen, false);
            }
        }
    }

    protected Settings getSettingsImpl(String id) {
        return new SettingsImpl(id);
    }

    @Override
    public void remove(Screen screen) {
        checkNotNullArgument(screen);
        checkOpened(screen);

        WindowImplementation windowImpl = (WindowImplementation) screen.getWindow();
        if (windowImpl instanceof Disposable) {
            ((Disposable) windowImpl).dispose();
        }

        LaunchMode launchMode = windowImpl.getContext().getLaunchMode();
        if (launchMode instanceof OpenMode) {
            OpenMode openMode = (OpenMode) launchMode;

            switch (openMode) {
                case DIALOG:
                    removeDialogWindow(screen);
                    break;

                case NEW_TAB:
                case NEW_WINDOW:
                    removeNewTabWindow(screen);
                    break;

                case ROOT:
                    removeRootWindow(screen);
                    break;

                case THIS_TAB:
                    removeThisTabWindow(screen);
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported OpenMode");
            }
        }

        fireEvent(screen, AfterDetachEvent.class, new AfterDetachEvent(screen));

        afterScreenRemove(screen);
    }

    protected void afterScreenRemove(Screen screen) {
        if (screen.getWindow() instanceof RootWindow) {
            return;
        }

        Screen currentScreen = getAnyCurrentScreen();
        if (currentScreen != null) {
            NavigationState resolvedState = ((WebWindow) currentScreen.getWindow()).getResolvedState();
            if (resolvedState != null) {
                String currentScreenRoute = resolvedState.asRoute();
                UrlTools.replaceState(currentScreenRoute);
            }
        }
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

    protected void removeThisTabWindow(Screen screen) {
        TabWindow window = (TabWindow) screen.getWindow();

        com.vaadin.ui.Component windowComposition = window.unwrapComposition(com.vaadin.ui.Component.class);

        TabWindowContainer windowContainer = (TabWindowContainer) windowComposition.getParent();
        windowContainer.removeComponent(windowComposition);

        WindowBreadCrumbs breadCrumbs = windowContainer.getBreadCrumbs();

        breadCrumbs.removeWindow();

        Window currentWindow = breadCrumbs.getCurrentWindow();
        com.vaadin.ui.Component currentWindowComposition =
                currentWindow.unwrapComposition(com.vaadin.ui.Component.class);

        windowContainer.addComponent(currentWindowComposition);

        WebAppWorkArea workArea = getConfiguredWorkArea();
        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            String tabId = tabSheet.getTab(windowContainer);

            TabWindow tabWindow = (TabWindow) currentWindow;

            String formattedCaption = tabWindow.formatTabCaption();
            String formattedDescription = tabWindow.formatTabDescription();

            tabSheet.setTabCaption(tabId, formattedCaption);
            if (!Objects.equals(formattedCaption, formattedDescription)) {
                tabSheet.setTabDescription(tabId, formattedDescription);
            } else {
                tabSheet.setTabDescription(tabId, null);
            }

            tabSheet.setTabIcon(tabId, iconResolver.getIconResource(currentWindow.getIcon()));
            tabSheet.setTabClosable(tabId, currentWindow.isCloseable());

            ContentSwitchMode contentSwitchMode =
                    ContentSwitchMode.valueOf(tabWindow.getContentSwitchMode().name());
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);
        }
    }

    protected void removeRootWindow(@SuppressWarnings("unused") Screen screen) {
        ui.setTopLevelWindow(null);
    }

    protected void removeNewTabWindow(Screen screen) {
        WebTabWindow window = (WebTabWindow) screen.getWindow();

        com.vaadin.ui.Component windowComposition = window.unwrapComposition(com.vaadin.ui.Component.class);

        TabWindowContainer windowContainer = (TabWindowContainer) windowComposition.getParent();
        windowContainer.removeComponent(windowComposition);

        WebAppWorkArea workArea = getConfiguredWorkArea();

        boolean allWindowsRemoved;
        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            tabSheet.silentCloseTabAndSelectPrevious(windowContainer);
            tabSheet.removeComponent(windowContainer);

            allWindowsRemoved = tabSheet.getComponentCount() == 0;
        } else {
            Layout singleLayout = workArea.getSingleWindowContainer();
            singleLayout.removeComponent(windowContainer);

            allWindowsRemoved = true;
        }

        WindowBreadCrumbs windowBreadCrumbs = windowContainer.getBreadCrumbs();
        if (windowBreadCrumbs != null) {
            windowBreadCrumbs.setWindowNavigateHandler(null);
            windowBreadCrumbs.removeWindow();
        }

        if (allWindowsRemoved) {
            workArea.switchTo(AppWorkArea.State.INITIAL_LAYOUT);
        }
    }

    protected void removeDialogWindow(Screen screen) {
        Window window = screen.getWindow();

        CubaWindow cubaDialogWindow = window.unwrapComposition(CubaWindow.class);
        cubaDialogWindow.forceClose();
    }

    @Override
    public void removeAll() {
        List<Screen> dialogScreens =
                getDialogScreensStream().collect(Collectors.toList());

        for (Screen dialogScreen : dialogScreens) {
            remove(dialogScreen);
        }

        WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();

        if (workArea != null) {
            Collection<WindowStack> workAreaStacks = getWorkAreaStacks(workArea);

            for (WindowStack workAreaStack : workAreaStacks) {
                Collection<Screen> tabScreens = workAreaStack.getBreadcrumbs();

                for (Screen screen : tabScreens) {
                    remove(screen);
                }
            }
        }
    }

    @Override
    public boolean hasUnsavedChanges() {
        Screen rootScreen = getRootScreenOrNull();
        if (rootScreen instanceof ChangeTracker &&
                ((ChangeTracker) rootScreen).hasUnsavedChanges()) {
            return true;
        }

        Predicate<Screen> hasUnsavedChanges = screen ->
                screen instanceof ChangeTracker
                && ((ChangeTracker) screen).hasUnsavedChanges();

        return getDialogScreensStream().anyMatch(hasUnsavedChanges)
                || getOpenedWorkAreaScreensStream().anyMatch(hasUnsavedChanges);
    }

    @Override
    public OpenedScreens getOpenedScreens() {
        return new OpenedScreensImpl();
    }

    protected Stream<Screen> getOpenedWorkAreaScreensStream() {
        Screen rootScreen = getRootScreenOrNull();
        if (rootScreen == null) {
            return Stream.empty();
        }

        WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
        if (workArea == null) {
            return Stream.empty();
        }

        return workArea.getOpenedWorkAreaScreensStream();
    }

    protected Stream<Screen> getActiveWorkAreaScreensStream() {
        Screen rootScreen = getRootScreenOrNull();
        if (rootScreen == null) {
            return Stream.empty();
        }

        WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
        if (workArea == null) {
            return Stream.empty();
        }

        return workArea.getActiveWorkAreaScreensStream();
    }

    protected Stream<Screen> getDialogScreensStream() {
        Collection<com.vaadin.ui.Window> windows = ui.getWindows();
        if (windows.isEmpty()) {
            return Stream.empty();
        }

        return windows.stream()
                .filter(w -> w instanceof GuiDialogWindow)
                .map(w -> ((GuiDialogWindow) w).getDialogWindow().getFrameOwner());
    }

    protected Collection<Screen> getCurrentBreadcrumbs() {
        WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
        if (workArea == null) {
            return Collections.emptyList();
        }

        return workArea.getCurrentBreadcrumbs();
    }

    protected Screen getRootScreenOrNull() {
        RootWindow window = ui.getTopLevelWindow();
        if (window == null) {
            return null;
        }

        return window.getFrameOwner();
    }

    protected Stream<WindowStack> getTabbedScreensStacks(WebAppWorkArea workArea) {
        if (workArea.getMode() != Mode.TABBED) {
            throw new IllegalArgumentException("WorkArea mode is not TABBED");
        }

        TabSheetBehaviour tabSheetBehaviour = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

        return tabSheetBehaviour.getTabComponentsStream()
                .map(c -> ((TabWindowContainer) c))
                .map(windowContainer -> new WindowStackImpl(windowContainer, workArea.getTabbedWindowContainer()));
    }

    protected Collection<WindowStack> getWorkAreaStacks(WebAppWorkArea workArea) {
        if (workArea.getMode() == Mode.TABBED) {
            return getTabbedScreensStacks(workArea)
                    .collect(Collectors.toList());
        } else {
            TabWindowContainer windowContainer = (TabWindowContainer) workArea.getSingleWindowContainer().getWindowContainer();
            if (windowContainer != null) {
                return Collections.singleton(new WindowStackImpl(windowContainer, workArea.getSingleWindowContainer()));
            }
        }

        return Collections.emptyList();
    }

    /**
     * @return workarea instance of the root screen
     * @throws IllegalStateException if there is no root screen or root screen does not have {@link AppWorkArea}
     */
    @Nonnull
    protected WebAppWorkArea getConfiguredWorkArea() {
        RootWindow topLevelWindow = ui.getTopLevelWindow();
        if (topLevelWindow == null) {
            throw new IllegalStateException("There is no root screen opened");
        }

        Screen controller = topLevelWindow.getFrameOwner();

        if (controller instanceof HasWorkArea) {
            AppWorkArea workArea = ((HasWorkArea) controller).getWorkArea();
            if (workArea != null) {
                return (WebAppWorkArea) workArea;
            }
        }

        throw new IllegalStateException("RootWindow does not have any configured work area");
    }

    @Nullable
    protected WebAppWorkArea getConfiguredWorkAreaOrNull() {
        RootWindow topLevelWindow = ui.getTopLevelWindow();
        if (topLevelWindow == null) {
            throw new IllegalStateException("There is no root screen opened");
        }

        Screen controller = topLevelWindow.getFrameOwner();

        if (controller instanceof HasWorkArea) {
            AppWorkArea workArea = ((HasWorkArea) controller).getWorkArea();
            if (workArea != null) {
                return (WebAppWorkArea) workArea;
            }
        }

        return null;
    }

    protected <T extends Screen> T createController(WindowInfo windowInfo, Window window, Class<T> screenClass) {
        Constructor<T> constructor;
        try {
            constructor = screenClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new DevelopmentException("No accessible constructor for screen class " + screenClass);
        }

        T controller;
        try {
            controller = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unable to create instance of screen class " + screenClass);
        }

        return controller;
    }

    protected Window createWindow(@SuppressWarnings("unused") WindowInfo windowInfo,
                                  Class<? extends Screen> screenClass,
                                  ScreenOpenDetails openDetails) {
        Window window;

        OpenMode openMode = openDetails.getOpenMode();
        switch (openMode) {
            case ROOT:
                // todo should be changed
                ui.beforeTopLevelWindowInit();

                window = uiComponents.create(RootWindow.NAME);
                break;

            case THIS_TAB:
            case NEW_TAB:
                window = uiComponents.create(TabWindow.NAME);
                break;

            case DIALOG:
                DialogWindow dialogWindow = uiComponents.create(DialogWindow.NAME);

                if (openDetails.isForceDialog()) {
                    ThemeConstants theme = ui.getApp().getThemeConstants();

                    dialogWindow.setDialogWidth(theme.get("cuba.web.WebWindowManager.forciblyDialog.width"));
                    dialogWindow.setDialogHeight(theme.get("cuba.web.WebWindowManager.forciblyDialog.height"));
                    dialogWindow.setResizable(true);
                } else {
                    DialogMode dialogMode = screenClass.getAnnotation(DialogMode.class);

                    if (dialogMode != null) {
                        dialogWindow.setModal(dialogMode.modal());
                        dialogWindow.setCloseable(dialogMode.closeable());
                        dialogWindow.setResizable(dialogMode.resizable());
                        dialogWindow.setCloseOnClickOutside(dialogMode.closeOnClickOutside());

                        if (StringUtils.isNotEmpty(dialogMode.width())) {
                            dialogWindow.setDialogWidth(dialogMode.width());
                        }
                        if (StringUtils.isNotEmpty(dialogMode.height())) {
                            dialogWindow.setDialogHeight(dialogMode.height());
                        }

                        dialogWindow.setWindowMode(dialogMode.windowMode());
                    }
                }

                window = dialogWindow;

                break;

            default:
                throw new UnsupportedOperationException("Unsupported launch mode " + openMode);
        }

        WindowContextImpl windowContext = new WindowContextImpl(window, openDetails.getOpenMode());
        ((WindowImplementation) window).setContext(windowContext);

        return window;
    }

    protected void checkPermissions(LaunchMode launchMode, WindowInfo windowInfo) {
        // ROOT windows are always permitted
        if (launchMode != OpenMode.ROOT) {
            boolean permitted = security.isScreenPermitted(windowInfo.getId());
            if (!permitted) {
                throw new AccessDeniedException(PermissionType.SCREEN, windowInfo.getId());
            }
        }
    }

    protected WindowInfo getScreenInfo(Class<? extends Screen> screenClass) {
        UiController uiController = screenClass.getAnnotation(UiController.class);
        if (uiController == null) {
            throw new IllegalArgumentException("No @UiController annotation for class " + screenClass);
        }

        String screenId = UiDescriptorUtils.getInferredScreenId(uiController, screenClass);

        return windowConfig.getWindowInfo(screenId);
    }

    protected void showRootWindow(Screen screen) {
        ui.setTopLevelWindow((RootWindow) screen.getWindow());
    }

    protected boolean isWindowClosePrevented(Window window, Window.CloseOrigin closeOrigin) {
        Window.BeforeCloseEvent event = new Window.BeforeCloseEvent(window, closeOrigin);
        ((WebWindow) window).fireBeforeClose(event);

        return event.isClosePrevented();
    }

    /**
     * Checks if there are modal GUI dialog windows.
     *
     * @return true if there is at least one modal dialog window
     */
    protected boolean hasModalDialogWindow() {
        return getDialogScreensStream()
                .anyMatch(s -> ((DialogWindow) s.getWindow()).isModal());
    }

    /**
     * Checks if there are modal Vaadin dialog windows.
     *
     * @return true if there is at least one modal Vaadin dialog window
     */
    protected boolean hasModalWindow() {
        return ui.getWindows().stream()
                .anyMatch(com.vaadin.ui.Window::isModal);
    }

    /*
     * Legacy APIs and compatibility layer.
     */

    @Override
    public Collection<Window> getOpenWindows() {
        return getOpenedScreens().getAll().stream()
                .map(Screen::getWindow)
                .collect(Collectors.toList());
    }

    @Override
    public void selectWindowTab(Window window) {
        WebAppWorkArea workArea = getConfiguredWorkArea();

        Collection<WindowStack> workAreaStacks = getWorkAreaStacks(workArea);

        Screen screen = window.getFrameOwner();
        workAreaStacks.stream()
                .filter(ws -> ws.getBreadcrumbs().contains(screen))
                .findFirst()
                .ifPresent(WindowStack::select);
    }

    @SuppressWarnings({"IncorrectCreateGuiComponent", "deprecation"})
    @Override
    public Window openWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
        params = createParametersMap(windowInfo, params);
        MapScreenOptions options = new MapScreenOptions(params);

        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        show(screen);
        return screen instanceof Window ? (Window) screen : new ScreenWrapper(screen);
    }

    @SuppressWarnings({"IncorrectCreateGuiComponent", "deprecation"})
    @Override
    public Window openWindow(WindowInfo windowInfo, OpenType openType) {
        Map<String, Object> params = createParametersMap(windowInfo, Collections.emptyMap());
        MapScreenOptions options = new MapScreenOptions(params);

        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        show(screen);
        return screen instanceof Window ? (Window) screen : new ScreenWrapper(screen);
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Datasource parentDs) {
        Map<String, Object> params = createParametersMap(windowInfo,
                Collections.singletonMap(WindowParams.ITEM.name(), item)
        );
        MapScreenOptions options = new MapScreenOptions(params);

        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        EditorScreen editorScreen = (EditorScreen) screen;
        if (editorScreen instanceof AbstractEditor) {
            ((AbstractEditor) editorScreen).setParentDs(parentDs);
        }
        editorScreen.setEntityToEdit(item);
        show(screen);
        return screen instanceof Window.Editor ? (Window.Editor) screen : new ScreenEditorWrapper(screen);
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType) {
        Map<String, Object> params = createParametersMap(windowInfo,
                Collections.singletonMap(WindowParams.ITEM.name(), item)
        );

        MapScreenOptions options = new MapScreenOptions(params);

        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        EditorScreen editorScreen = (EditorScreen) screen;
        editorScreen.setEntityToEdit(item);
        show(screen);
        return screen instanceof Window.Editor ? (Window.Editor) screen : new ScreenEditorWrapper(screen);
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Map<String, Object> params) {
        Screen editor = createEditor(windowInfo, item, openType, params);

        editor.show();

        return editor instanceof Window.Editor ? (Window.Editor) editor : new ScreenEditorWrapper(editor);
    }

    // used only for legacy screens
    @Override
    public Screen createEditor(WindowInfo windowInfo, Entity item, OpenType openType, Map<String, Object> params) {
        params = createParametersMap(windowInfo, params);
        params.put(WindowParams.ITEM.name(), item);

        MapScreenOptions options = new MapScreenOptions(params);

        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        EditorScreen editorScreen = (EditorScreen) screen;

        //noinspection unchecked
        editorScreen.setEntityToEdit(item);

        return screen;
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Map<String, Object> params,
                                    Datasource parentDs) {
        params = createParametersMap(windowInfo, params);
        params.put(WindowParams.ITEM.name(), item);

        MapScreenOptions options = new MapScreenOptions(params);

        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        EditorScreen editorScreen = (EditorScreen) screen;
        if (editorScreen instanceof AbstractEditor) {
            ((AbstractEditor) editorScreen).setParentDs(parentDs);
        }
        editorScreen.setEntityToEdit(item);
        show(screen);
        return screen instanceof Window.Editor ? (Window.Editor) screen : new ScreenEditorWrapper(screen);
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType,
                                    Map<String, Object> params) {
        params = createParametersMap(windowInfo, params);

        MapScreenOptions options = new MapScreenOptions(params);
        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        ((LookupScreen) screen).setSelectHandler(new SelectHandlerAdapter(handler));

        show(screen);

        return screen instanceof Window.Lookup ? (Window.Lookup) screen : new ScreenLookupWrapper(screen);
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType) {
        Map<String, Object> params = createParametersMap(windowInfo, Collections.emptyMap());

        MapScreenOptions options = new MapScreenOptions(params);
        Screen screen = createScreen(windowInfo, openType.getOpenMode(), options);
        applyOpenTypeParameters(screen.getWindow(), openType);

        ((LookupScreen) screen).setSelectHandler(new SelectHandlerAdapter(handler));

        show(screen);

        return screen instanceof Window.Lookup ? (Window.Lookup) screen : new ScreenLookupWrapper(screen);
    }

    @Override
    public Frame openFrame(Frame parentFrame, com.haulmont.cuba.gui.components.Component parent, WindowInfo windowInfo) {
        return openFrame(parentFrame, parent, windowInfo, Collections.emptyMap());
    }

    @Override
    public Frame openFrame(Frame parentFrame, com.haulmont.cuba.gui.components.Component parent, WindowInfo windowInfo,
                           Map<String, Object> params) {
        return openFrame(parentFrame, parent, null, windowInfo, params);
    }

    @SuppressWarnings({"deprecation", "IncorrectCreateGuiComponent"})
    @Override
    public Frame openFrame(Frame parentFrame, com.haulmont.cuba.gui.components.Component parent, @Nullable String id,
                           WindowInfo windowInfo, Map<String, Object> params) {
        ScreenFragment screenFragment;

        Fragments fragments = ui.getFragments();

        if (params != null && !params.isEmpty()) {
            screenFragment = fragments.create(parentFrame.getFrameOwner(), windowInfo.getId(), new MapScreenOptions(params));
        } else {
            screenFragment = fragments.create(parentFrame.getFrameOwner(), windowInfo.getId());
        }

        if (id != null) {
            screenFragment.getFragment().setId(id);
        }

        if (parent instanceof ComponentContainer) {
            ComponentContainer container = (ComponentContainer) parent;
            for (com.haulmont.cuba.gui.components.Component c : container.getComponents()) {
                if (c instanceof com.haulmont.cuba.gui.components.Component.Disposable) {
                    com.haulmont.cuba.gui.components.Component.Disposable disposable =
                            (com.haulmont.cuba.gui.components.Component.Disposable) c;
                    if (!disposable.isDisposed()) {
                        disposable.dispose();
                    }
                }
                container.remove(c);
            }
            container.add(screenFragment.getFragment());
        }

        fragments.init(screenFragment);

        return screenFragment instanceof Frame ? (Frame) screenFragment : new ScreenFragmentWrapper(screenFragment);
    }

    @Override
    public void showNotification(String caption) {
        ui.getNotifications().create()
                .withCaption(caption)
                .show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showNotification(String caption, Frame.NotificationType type) {
        ui.getNotifications().create()
                .withCaption(caption)
                .withContentMode(Frame.NotificationType.isHTML(type) ? ContentMode.HTML : ContentMode.TEXT)
                .withType(convertNotificationType(type))
                .show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showNotification(String caption, String description, Frame.NotificationType type) {
        ui.getNotifications().create()
                .withCaption(caption)
                .withDescription(description)
                .withContentMode(Frame.NotificationType.isHTML(type) ? ContentMode.HTML : ContentMode.TEXT)
                .withType(convertNotificationType(type))
                .show();
    }

    @SuppressWarnings("deprecation")
    protected NotificationType convertNotificationType(Frame.NotificationType type) {
        switch (type) {
            case TRAY:
            case TRAY_HTML:
                return NotificationType.TRAY;

            case ERROR:
            case ERROR_HTML:
                return NotificationType.ERROR;

            case HUMANIZED:
            case HUMANIZED_HTML:
                return NotificationType.HUMANIZED;

            case WARNING:
            case WARNING_HTML:
                return NotificationType.WARNING;

            default:
                throw new UnsupportedOperationException("Unsupported notification type");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showMessageDialog(String title, String message, Frame.MessageType messageType) {
        MessageDialogBuilder builder = ui.getDialogs().createMessageDialog()
                .withCaption(title)
                .withMessage(message)
                .withType(convertMessageType(messageType.getMessageMode()))
                .withContentMode(
                        Frame.MessageMode.isHTML(messageType.getMessageMode()) ? ContentMode.HTML : ContentMode.TEXT
                );

        if (messageType.getWidth() != null) {
            SizeUnit sizeUnit = messageType.getWidthUnit() != null ? messageType.getWidthUnit() : SizeUnit.PIXELS;
            builder.withWidth(messageType.getWidth() + sizeUnit.getSymbol());
        }
        if (messageType.getModal() != null) {
            builder.withModal(messageType.getModal());
        }
        if (messageType.getCloseOnClickOutside() != null) {
            builder.withCloseOnClickOutside(messageType.getCloseOnClickOutside());
        }
        if (messageType.getMaximized() != null) {
            builder.withMaximized(messageType.getMaximized());
        }

        builder.show();
    }

    @SuppressWarnings("deprecation")
    protected Dialogs.MessageType convertMessageType(Frame.MessageMode messageMode) {
        switch (messageMode) {
            case CONFIRMATION:
            case CONFIRMATION_HTML:
                return Dialogs.MessageType.CONFIRMATION;

            case WARNING:
            case WARNING_HTML:
                return Dialogs.MessageType.WARNING;

            default:
                throw new UnsupportedOperationException("Unsupported message type");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showOptionDialog(String title, String message, Frame.MessageType messageType, Action[] actions) {
        OptionDialogBuilder builder = ui.getDialogs().createOptionDialog()
                .withCaption(title)
                .withMessage(message)
                .withType(convertMessageType(messageType.getMessageMode()))
                .withActions(actions);

        if (messageType.getWidth() != null) {
            SizeUnit sizeUnit = messageType.getWidthUnit() != null ? messageType.getWidthUnit() : SizeUnit.PIXELS;
            builder.withWidth(messageType.getWidth() + sizeUnit.getSymbol());
        }
        if (messageType.getMaximized() != null) {
            builder.withMaximized(messageType.getMaximized());
        }

        builder.show();
    }

    @Override
    public void showExceptionDialog(Throwable throwable) {
        showExceptionDialog(throwable, null, null);
    }

    @Override
    public void showExceptionDialog(Throwable throwable, @Nullable String caption, @Nullable String message) {
        ui.getDialogs().createExceptionDialog()
                .withCaption(caption)
                .withMessage(message)
                .withThrowable(throwable)
                .show();
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        ui.getWebBrowserTools().showWebPage(url, params);
    }

    /**
     * Check modifications and close all screens in all main windows.
     *
     * @param runIfOk a closure to run after all screens are closed
     */
    @Deprecated
    public void checkModificationsAndCloseAll(Runnable runIfOk) {
        checkModificationsAndCloseAll()
                .then(runIfOk);
    }

    /**
     * Check modifications and close all screens in all main windows.
     *
     * @param runIfOk     a closure to run after all screens are closed
     * @param runIfCancel a closure to run if there were modifications and a user canceled the operation
     */
    @Deprecated
    public void checkModificationsAndCloseAll(Runnable runIfOk, Runnable runIfCancel) {
        checkModificationsAndCloseAll()
                .then(runIfOk)
                .otherwise(runIfCancel);
    }

    /**
     * Check modifications and close all screens in all main windows excluding root screens.
     *
     * @return operation result
     */
    public OperationResult checkModificationsAndCloseAll() {
        if (hasUnsavedChanges()) {
            UnknownOperationResult result = new UnknownOperationResult();

            ui.getDialogs().createOptionDialog()
                    .withCaption(messages.getMainMessage("closeUnsaved.caption"))
                    .withMessage(messages.getMainMessage("discardChangesOnClose"))
                    .withActions(
                            new BaseAction("closeApplication")
                                    .withCaption(messages.getMainMessage("closeApplication"))
                                    .withIcon(icons.get(CubaIcon.DIALOG_OK))
                                    .withHandler(event -> {
                                        closeWindowsInternal();

                                        result.success();
                                    }),
                            new DialogAction(DialogAction.Type.CANCEL, Action.Status.PRIMARY)
                                    .withHandler(event -> result.fail())
                    )
                    .show();

            return result;
        } else {
            closeWindowsInternal();

            return OperationResult.success();
        }
    }

    protected void closeWindowsInternal() {
        saveScreenSettings();
        saveScreenHistory();

        ui.getApp().removeAllWindows();
    }

    public void saveScreenHistory() {
        getOpenedWorkAreaScreensStream().forEach(s ->
                screenHistorySupport.saveScreenHistory(s)
        );

        getDialogScreensStream().forEach(s ->
                screenHistorySupport.saveScreenHistory(s)
        );
    }

    public void saveScreenSettings() {
        Screen rootScreen = getOpenedScreens().getRootScreen();

        saveSettings(rootScreen);

        getOpenedWorkAreaScreensStream().forEach(UiControllerUtils::saveSettings);

        getDialogScreensStream().forEach(UiControllerUtils::saveSettings);
    }

    /**
     * Close all screens in all main windows (browser tabs).
     *
     * @deprecated Use {@link App#removeAllWindows()} instead.
     */
    @Deprecated
    public void closeAllWindows() {
        ui.getApp().removeAllWindows();
    }

    /**
     * Close all screens in the main window (browser tab) this WindowManagerImpl belongs to.
     *
     * @deprecated Use {@link #removeAll()} instead.
     */
    @Deprecated
    public void closeAll() {
        removeAll();
    }

    // used only for legacy screens
    @Deprecated
    protected Map<String, Object> createParametersMap(WindowInfo windowInfo, @Nullable Map<String, Object> params) {
        Map<String, Object> map;

        if (params != null) {
            map = new HashMap<>(params.size());
        } else {
            map = new HashMap<>();
        }

        Element element = windowInfo.getDescriptor();
        if (element != null) {
            Element paramsElement = element.element("params") != null ? element.element("params") : element;
            if (paramsElement != null) {
                List<Element> paramElements = paramsElement.elements("param");
                for (Element paramElement : paramElements) {
                    String name = paramElement.attributeValue("name");
                    String value = paramElement.attributeValue("value");
                    if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                        Boolean booleanValue = Boolean.valueOf(value);
                        map.put(name, booleanValue);
                    } else {
                        map.put(name, value);
                    }
                }
            }
        }
        if (params != null) {
            map.putAll(params);
        }

        return map;
    }

    protected void showNewTabWindow(Screen screen) {
        WebAppWorkArea workArea = getConfiguredWorkArea();
        workArea.switchTo(AppWorkArea.State.WINDOW_CONTAINER);

        // work with new window
        createNewTabLayout(screen);
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs(@SuppressWarnings("unused") Screen screen) {
        WebAppWorkArea appWorkArea = getConfiguredWorkArea();

        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs(appWorkArea.getMode());
        windowBreadCrumbs.setUI(ui);
        windowBreadCrumbs.setBeanLocator(beanLocator);

        boolean showBreadCrumbs = webConfig.getShowBreadCrumbs() || appWorkArea.getMode() == Mode.SINGLE;
        windowBreadCrumbs.setVisible(showBreadCrumbs);

        return windowBreadCrumbs;
    }

    protected void createNewTabLayout(Screen screen) {
        WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs(screen);
        breadCrumbs.setWindowNavigateHandler(this::handleWindowBreadCrumbsNavigate);
        breadCrumbs.addWindow(screen.getWindow());
        ((WebWindow) screen.getWindow()).setUrlStateMark(getConfiguredWorkArea().generateUrlStateMark());

        TabWindowContainer windowContainer = new TabWindowContainerImpl();
        windowContainer.setPrimaryStyleName("c-app-window-wrap");
        windowContainer.setSizeFull();

        windowContainer.setBreadCrumbs(breadCrumbs);
        windowContainer.addComponent(breadCrumbs);

        Window window = screen.getWindow();

        com.vaadin.ui.Component windowComposition = window.unwrapComposition(com.vaadin.ui.Component.class);
        windowContainer.addComponent(windowComposition);

        WebAppWorkArea workArea = getConfiguredWorkArea();

        if (workArea.getMode() == Mode.TABBED) {
            windowContainer.addStyleName("c-app-tabbed-window");

            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            String tabId = "tab_" + uuidSource.createUuid();

            tabSheet.addTab(windowContainer, tabId);

            if (ui.isTestMode()) {
                String id = "tab_" + window.getId();

                tabSheet.setTabTestId(tabId, ui.getTestIdManager().getTestId(id));
                tabSheet.setTabCubaId(tabId, id);
            }
            TabWindow tabWindow = (TabWindow) window;

            String windowContentSwitchMode = tabWindow.getContentSwitchMode().name();
            ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(windowContentSwitchMode);
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);

            String formattedCaption = tabWindow.formatTabCaption();
            String formattedDescription = tabWindow.formatTabDescription();

            tabSheet.setTabCaption(tabId, formattedCaption);
            if (!Objects.equals(formattedCaption, formattedDescription)) {
                tabSheet.setTabDescription(tabId, formattedDescription);
            } else {
                tabSheet.setTabDescription(tabId, null);
            }

            tabSheet.setTabIcon(tabId, iconResolver.getIconResource(window.getIcon()));
            tabSheet.setTabClosable(tabId, window.isCloseable());
            tabSheet.setTabCloseHandler(windowContainer, this::handleTabWindowClose);
            tabSheet.setSelectedTab(windowContainer);
        } else {
            windowContainer.addStyleName("c-app-single-window");

            CubaSingleModeContainer mainLayout = workArea.getSingleWindowContainer();

            if (mainLayout.getWindowContainer() != null) {
                // remove all windows from single stack

                TabWindowContainer oldWindowContainer = (TabWindowContainer) mainLayout.getWindowContainer();

                Deque<Window> windows = oldWindowContainer.getBreadCrumbs().getWindows();
                Iterator<Window> iterator = windows.descendingIterator();

                while (iterator.hasNext()) {
                    Window oldWindow = iterator.next();
                    remove(oldWindow.getFrameOwner());
                }

                // after last window closed we need to switch back to window container
                workArea.switchTo(AppWorkArea.State.WINDOW_CONTAINER);
            }

            mainLayout.setWindowContainer(windowContainer);
        }
    }

    protected void showThisTabWindow(Screen screen) {
        WebAppWorkArea workArea = getConfiguredWorkArea();
        workArea.switchTo(AppWorkArea.State.WINDOW_CONTAINER);

        TabWindowContainer windowContainer;
        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            windowContainer = (TabWindowContainer) tabSheet.getSelectedTab();
        } else {
            windowContainer = (TabWindowContainer) workArea.getSingleWindowContainer().getWindowContainer();
        }

        if (windowContainer == null || windowContainer.getBreadCrumbs() == null) {
            throw new IllegalStateException("BreadCrumbs not found");
        }

        WindowBreadCrumbs breadCrumbs = windowContainer.getBreadCrumbs();
        Window currentWindow = breadCrumbs.getCurrentWindow();

        windowContainer.removeComponent(currentWindow.unwrapComposition(Layout.class));

        Window newWindow = screen.getWindow();
        com.vaadin.ui.Component newWindowComposition = newWindow.unwrapComposition(com.vaadin.ui.Component.class);

        windowContainer.addComponent(newWindowComposition);

        breadCrumbs.addWindow(newWindow);
        ((WebWindow) newWindow).setUrlStateMark(workArea.generateUrlStateMark());

        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            String tabId = tabSheet.getTab(windowContainer);

            TabWindow tabWindow = (TabWindow) newWindow;

            String formattedCaption = tabWindow.formatTabCaption();
            String formattedDescription = tabWindow.formatTabDescription();

            tabSheet.setTabCaption(tabId, formattedCaption);
            if (!Objects.equals(formattedCaption, formattedDescription)) {
                tabSheet.setTabDescription(tabId, formattedDescription);
            } else {
                tabSheet.setTabDescription(tabId, null);
            }

            tabSheet.setTabIcon(tabId, iconResolver.getIconResource(newWindow.getIcon()));
            tabSheet.setTabClosable(tabId, newWindow.isCloseable());

            ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(tabWindow.getContentSwitchMode().name());
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);
        } else {
            windowContainer.markAsDirtyRecursive();
        }
    }

    protected void showDialogWindow(Screen screen) {
        DialogWindow window = (DialogWindow) screen.getWindow();

        WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
        if (workArea != null) {
            ((WebWindow) window).setUrlStateMark(workArea.generateUrlStateMark());
        }

        CubaWindow vWindow = window.unwrapComposition(CubaWindow.class);
        vWindow.setErrorHandler(ui);

        String cubaId = "dialog_" + window.getId();
        if (ui.isTestMode()) {
            vWindow.setCubaId(cubaId);
        }
        if (ui.isPerformanceTestMode()) {
            vWindow.setId(ui.getTestIdManager().getTestId(cubaId));
        }

        if (hasModalWindow()) {
            // force modal
            window.setModal(true);
        }

        ui.addWindow(vWindow);
    }

    protected void handleWindowBreadCrumbsNavigate(WindowBreadCrumbs breadCrumbs, Window window) {
        Runnable op = new Runnable() {
            @Override
            public void run() {
                Window currentWindow = breadCrumbs.getCurrentWindow();
                if (!currentWindow.isCloseable()) {
                    return;
                }

                if (window != currentWindow) {
                    if (!isWindowClosePrevented(currentWindow, CloseOriginType.BREADCRUMBS)) {
                        currentWindow.getFrameOwner()
                                .close(WINDOW_CLOSE_ACTION)
                                .then(this);
                    }
                }
            }
        };
        op.run();
    }

    protected void handleTabWindowClose(HasTabSheetBehaviour targetTabSheet, com.vaadin.ui.Component tabContent) {
        WindowBreadCrumbs tabBreadCrumbs = ((TabWindowContainer) tabContent).getBreadCrumbs();

        WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
        if (workArea != null && workArea.isNotCloseable(tabBreadCrumbs.getCurrentWindow())) {
            return;
        }

        Runnable closeTask = new TabCloseTask(tabBreadCrumbs);
        closeTask.run();

        // it is needed to force redraw tabSheet if it has a lot of tabs and part of them are hidden
        targetTabSheet.markAsDirty();
    }

    public class TabCloseTask implements Runnable {
        protected WindowBreadCrumbs breadCrumbs;

        public TabCloseTask(WindowBreadCrumbs breadCrumbs) {
            this.breadCrumbs = breadCrumbs;
        }

        @Override
        public void run() {
            Window windowToClose = breadCrumbs.getCurrentWindow();
            if (windowToClose != null) {
                WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
                if ((workArea == null || !workArea.isNotCloseable(breadCrumbs.getCurrentWindow()))
                        && !isWindowClosePrevented(windowToClose, CloseOriginType.CLOSE_BUTTON)) {
                    windowToClose.getFrameOwner()
                            .close(WINDOW_CLOSE_ACTION)
                            .then(new TabCloseTask(breadCrumbs));
                }
            }
        }
    }

    @Deprecated
    protected void applyOpenTypeParameters(Window window, OpenType openType) {
        if (window instanceof DialogWindow) {
            DialogWindow dialogWindow = (DialogWindow) window;

            if (openType.getCloseOnClickOutside() != null) {
                dialogWindow.setCloseOnClickOutside(openType.getCloseOnClickOutside());
            }
            if (openType.getMaximized() != null) {
                dialogWindow.setWindowMode(openType.getMaximized() ? WindowMode.MAXIMIZED : WindowMode.NORMAL);
            }
            if (openType.getModal() != null) {
                dialogWindow.setModal(openType.getModal());
            }
            if (openType.getResizable() != null) {
                dialogWindow.setResizable(openType.getResizable());
            }
            if (openType.getWidth() != null) {
                dialogWindow.setDialogWidth(openType.getWidthString());
            }
            if (openType.getHeight() != null) {
                dialogWindow.setDialogHeight(openType.getHeightString());
            }
        }

        if (openType.getCloseable() != null) {
            window.setCloseable(openType.getCloseable());
        }
    }

    // todo message type parameters: modal / resizable / sizes / etc

    /*
     *  Utility classes
     */

    /**
     * Content of each tab of AppWorkArea TabSheet.
     */
    protected static class TabWindowContainerImpl extends CssLayout implements TabWindowContainer {
        protected WindowBreadCrumbs breadCrumbs;

        @Override
        public WindowBreadCrumbs getBreadCrumbs() {
            return breadCrumbs;
        }

        @Override
        public void setBreadCrumbs(WindowBreadCrumbs breadCrumbs) {
            this.breadCrumbs = breadCrumbs;
        }
    }

    protected static class ScreenOpenDetails {
        private boolean forceDialog;
        private OpenMode openMode;

        public ScreenOpenDetails(boolean forceDialog, OpenMode openMode) {
            this.forceDialog = forceDialog;
            this.openMode = openMode;
        }

        public boolean isForceDialog() {
            return forceDialog;
        }

        public OpenMode getOpenMode() {
            return openMode;
        }
    }

    protected static class WindowStackImpl implements WindowStack {

        protected final TabWindowContainer windowContainer;
        protected final com.vaadin.ui.Component workAreaContainer;

        public WindowStackImpl(TabWindowContainer windowContainer,
                               com.vaadin.ui.Component workAreaContainer) {
            this.windowContainer = windowContainer;
            this.workAreaContainer = workAreaContainer;
        }

        @Override
        public Collection<Screen> getBreadcrumbs() {
            checkAttached();

            Deque<Window> windows = windowContainer.getBreadCrumbs().getWindows();
            Iterator<Window> windowIterator = windows.descendingIterator();

            List<Screen> screens = new ArrayList<>(windows.size());

            while (windowIterator.hasNext()) {
                Screen screen = windowIterator.next().getFrameOwner();
                screens.add(screen);
            }

            return screens;
        }

        @Override
        public boolean isSelected() {
            checkAttached();

            if (workAreaContainer instanceof CubaSingleModeContainer) {
                return ((CubaSingleModeContainer) workAreaContainer).getWindowContainer() == windowContainer;
            }

            if (workAreaContainer instanceof HasTabSheetBehaviour) {
                TabSheetBehaviour tabSheetBehaviour = ((HasTabSheetBehaviour) workAreaContainer).getTabSheetBehaviour();

                return tabSheetBehaviour.getSelectedTab() == windowContainer;
            }

            return false;
        }

        @Override
        public void select() {
            checkAttached();

            if (workAreaContainer instanceof HasTabSheetBehaviour) {
                TabSheetBehaviour tabSheetBehaviour = ((HasTabSheetBehaviour) workAreaContainer).getTabSheetBehaviour();

                tabSheetBehaviour.setSelectedTab(windowContainer);
            }
        }

        protected void checkAttached() {
            if (windowContainer.getParent() == null) {
                throw new IllegalStateException("WindowStack has been detached");
            }
        }
    }

    protected class OpenedScreensImpl implements OpenedScreens {

        @Nonnull
        @Override
        public Screen getRootScreen() {
            RootWindow window = ui.getTopLevelWindow();
            if (window == null) {
                throw new IllegalStateException("There is no root screen in UI");
            }

            return window.getFrameOwner();
        }

        @Nullable
        @Override
        public Screen getRootScreenOrNull() {
            return WebScreens.this.getRootScreenOrNull();
        }

        @Override
        public Collection<Screen> getAll() {
            List<Screen> screens = new ArrayList<>();

            getOpenedWorkAreaScreensStream()
                    .forEach(screens::add);

            getDialogScreensStream()
                    .forEach(screens::add);

            return screens;
        }

        @Override
        public Collection<Screen> getWorkAreaScreens() {
            return getOpenedWorkAreaScreensStream()
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<Screen> getActiveScreens() {
            List<Screen> screens = new ArrayList<>();

            getActiveWorkAreaScreensStream()
                    .forEach(screens::add);

            getDialogScreensStream()
                    .forEach(screens::add);

            return screens;
        }

        @Override
        public Collection<Screen> getActiveWorkAreaScreens() {
            return getActiveWorkAreaScreensStream()
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<Screen> getDialogScreens() {
            return getDialogScreensStream()
                    .collect(Collectors.toList());
        }

        @Override
        public Collection<Screen> getCurrentBreadcrumbs() {
            return WebScreens.this.getCurrentBreadcrumbs();
        }

        @Override
        public Collection<WindowStack> getWorkAreaStacks() {
            WebAppWorkArea workArea = getConfiguredWorkAreaOrNull();
            if (workArea == null) {
                return Collections.emptyList();
            }

            return WebScreens.this.getWorkAreaStacks(workArea);
        }
    }
}
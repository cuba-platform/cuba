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

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.Dialogs.MessageDialog;
import com.haulmont.cuba.gui.Dialogs.OptionDialog;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Component.Disposable;
import com.haulmont.cuba.gui.components.Window.BeforeCloseWithCloseButtonEvent;
import com.haulmont.cuba.gui.components.Window.BeforeCloseWithShortcutEvent;
import com.haulmont.cuba.gui.components.Window.HasWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea.Mode;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.components.sys.WindowImplementation;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.GenericDataSupplier;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.screen.compatibility.ScreenWrapper;
import com.haulmont.cuba.gui.screen.events.AfterInitEvent;
import com.haulmont.cuba.gui.screen.events.AfterShowEvent;
import com.haulmont.cuba.gui.screen.events.BeforeShowEvent;
import com.haulmont.cuba.gui.screen.events.InitEvent;
import com.haulmont.cuba.gui.settings.Settings;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.sys.*;
import com.haulmont.cuba.gui.util.OperationResult;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.ScreenXmlLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoaderContext;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebTabWindow;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.haulmont.cuba.web.gui.components.util.ShortcutListenerDelegate;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.walkComponents;
import static com.haulmont.cuba.gui.components.Window.CLOSE_ACTION_ID;
import static com.haulmont.cuba.gui.screen.FrameOwner.NO_OPTIONS;

@Scope(UIScope.NAME)
@Component(Screens.NAME)
public class WebScreens implements Screens, WindowManager {
    @Inject
    protected BeanLocator beanLocator;

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected Security security;
    @Inject
    protected UuidSource uuidSource;
    @Inject
    protected ComponentsFactory componentsFactory;
    @Inject
    protected ScreenXmlLoader screenXmlLoader;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected UserSettingService userSettingService;
    @Inject
    protected ScreenViewsLoader screenViewsLoader;
    @Inject
    protected IconResolver iconResolver;
    @Inject
    protected Messages messages;

    @Inject
    protected Dialogs dialogs;
    @Inject
    protected Notifications notifications;
    @Inject
    protected WebBrowserTools webBrowserTools;

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

        WindowInfo windowInfo = getScreenInfo(requiredScreenClass);

        return createScreen(windowInfo, launchMode, options);
    }

    @Override
    public Screen create(WindowInfo windowInfo, LaunchMode launchMode, ScreenOptions options) {
        checkNotNullArgument(windowInfo);
        checkNotNullArgument(launchMode);
        checkNotNullArgument(options);

        return createScreen(windowInfo, launchMode, options);
    }

    protected <T extends Screen> T createScreen(WindowInfo windowInfo, LaunchMode launchMode, ScreenOptions options) {
        if (windowInfo.getType() != WindowInfo.Type.SCREEN) {
            throw new IllegalArgumentException(
                    String.format("Unable to create screen %s with type %s", windowInfo.getId(), windowInfo.getType())
            );
        }

        checkPermissions(launchMode, windowInfo);

        // todo change launchMode
        // todo support forciblyDialog

        // todo perf4j stop watches for lifecycle

        @SuppressWarnings("unchecked")
        Class<T> resolvedScreenClass = (Class<T>) windowInfo.getScreenClass();

        Window window = createWindow(windowInfo, resolvedScreenClass, launchMode);

        ui.beforeTopLevelWindowInit();

        T controller = createController(windowInfo, window, resolvedScreenClass, launchMode);

        ScreenUtils.setWindowId(controller, windowInfo.getId());
        ScreenUtils.setWindow(controller, window);
        ScreenUtils.setScreenContext(controller,
                new ScreenContextImpl(windowInfo, options, this, dialogs, notifications)
        );
        ScreenUtils.setScreenData(controller, beanLocator.get(ScreenData.NAME));

        WindowImplementation windowImpl = (WindowImplementation) window;
        windowImpl.setFrameOwner(controller);
        windowImpl.setId(controller.getId());

        WindowContext windowContext = new WindowContextImpl(window, launchMode, options);
        ((WindowImplementation) window).setContext(windowContext);

        loadScreenXml(windowInfo, window, controller, options);

        ScreenDependencyInjector dependencyInjector =
                beanLocator.getPrototype(ScreenDependencyInjector.NAME, controller, options);
        dependencyInjector.inject();

        InitEvent initEvent = new InitEvent(controller, options);
        ScreenUtils.fireEvent(controller, InitEvent.class, initEvent);

        AfterInitEvent afterInitEvent = new AfterInitEvent(controller, options);
        ScreenUtils.fireEvent(controller, AfterInitEvent.class, afterInitEvent);

        return controller;
    }

    protected <T extends Screen> void loadScreenXml(WindowInfo windowInfo, Window window, T controller,
                                                    ScreenOptions options) {
        String templatePath = windowInfo.getTemplate();

        if (StringUtils.isNotEmpty(templatePath)) {
            // todo support relative design path

            Map<String, Object> params = Collections.emptyMap();
            if (options instanceof MapScreenOptions) {
                params = ((MapScreenOptions) options).getParams();
            }

            Element element = screenXmlLoader.load(templatePath, windowInfo.getId(), params);

            ComponentLoaderContext componentLoaderContext = new ComponentLoaderContext(params);
            componentLoaderContext.setFullFrameId(windowInfo.getId());
            componentLoaderContext.setCurrentFrameId(windowInfo.getId());
            componentLoaderContext.setFrame(window);

            ComponentLoader windowLoader = createLayoutStructure(windowInfo, window, element, componentLoaderContext);

            if (controller instanceof LegacyFrame) {
                screenViewsLoader.deployViews(element);

                initDsContext(window, element, componentLoaderContext);

                DsContext dsContext = ((LegacyFrame) controller).getDsContext();
                if (dsContext != null) {
                    dsContext.setFrameContext(window.getContext());
                }
            }

            windowLoader.loadComponent();

            if (!componentLoaderContext.getPostInitTasks().isEmpty()) {
                EventHub eventHub = ScreenUtils.getEventHub(controller);
                eventHub.subscribe(AfterInitEvent.class, event ->
                        componentLoaderContext.executePostInitTasks()
                );
            }
        }
    }

    protected  <T extends Screen> void initDsContext(Window window, Element element,
                                                     ComponentLoaderContext componentLoaderContext) {
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

    protected void initDatasources(Window window, DsContext dsContext, Map<String, Object> params) {
        ((LegacyFrame) window.getFrameOwner()).setDsContext(dsContext);

        for (Datasource ds : dsContext.getAll()) {
            if (Datasource.State.NOT_INITIALIZED.equals(ds.getState()) && ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }
    }

    protected ComponentLoader createLayoutStructure(WindowInfo windowInfo, Window window, Element rootElement,
                                                    ComponentLoader.Context context) {
        String descriptorPath = windowInfo.getTemplate();

        LayoutLoader layoutLoader = beanLocator.getPrototype(LayoutLoader.NAME, context);
        layoutLoader.setLocale(getLocale());

        if (StringUtils.isNotEmpty(descriptorPath)) {
            if (descriptorPath.contains("/")) {
                descriptorPath = StringUtils.substring(descriptorPath, 0, descriptorPath.lastIndexOf("/"));
            }

            String path = descriptorPath.replaceAll("/", ".");
            int start = path.startsWith(".") ? 1 : 0;
            path = path.substring(start);

            layoutLoader.setMessagesPack(path);
        }
        //noinspection UnnecessaryLocalVariable
        ComponentLoader windowLoader = layoutLoader.createWindowContent(window, rootElement, windowInfo.getId());
        return windowLoader;
    }

    protected Locale getLocale() {
        return userSessionSource.getUserSession().getLocale();
    }

    @Override
    public void show(Screen screen) {
        checkNotNullArgument(screen);

        checkMultiOpen(screen);

        // todo UI security

        BeforeShowEvent beforeShowEvent = new BeforeShowEvent(screen);
        ScreenUtils.fireEvent(screen, BeforeShowEvent.class, beforeShowEvent);

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

        afterShowWindow(screen);

        AfterShowEvent afterShowEvent = new AfterShowEvent(screen);
        ScreenUtils.fireEvent(screen, AfterShowEvent.class, afterShowEvent);
    }

    protected void afterShowWindow(Screen screen) {
        WindowContext windowContext = screen.getWindow().getContext();

        if (!WindowParams.DISABLE_APPLY_SETTINGS.getBool(windowContext)) {
            ScreenUtils.applySettings(screen, getSettingsImpl(screen.getId()));
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
                AttributeAccessSupport attributeAccessSupport = AppBeans.get(AttributeAccessSupport.NAME);
                attributeAccessSupport.applyAttributeAccess(abstractWindow, false);
            }
        }
    }

    protected Settings getSettingsImpl(String id) {
        return new SettingsImpl(id);
    }

    @Override
    public void remove(Screen screen) {
        checkNotNullArgument(screen);

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

        // todo remove event ?
    }

    protected void removeThisTabWindow(Screen screen) {
        WebTabWindow window = (WebTabWindow) screen.getWindow();

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

            ContentSwitchMode contentSwitchMode =
                    ContentSwitchMode.valueOf(currentWindow.getContentSwitchMode().name());
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
        // todo implement
    }

    @Override
    public boolean hasUnsavedChanges() {
        // todo
        return false;
    }

    @Override
    public Collection<Window> getOpenWindows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void selectWindowTab(Window window) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean windowExist(WindowInfo windowInfo, Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("IncorrectCreateGuiComponent")
    @Override
    public Window openWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
        Screen screen = create(windowInfo, openType.getOpenMode(), new MapScreenOptions(params));
        show(screen);
        return screen instanceof Window ? (Window) screen : new ScreenWrapper(screen);
    }

    @SuppressWarnings("IncorrectCreateGuiComponent")
    @Override
    public Window openWindow(WindowInfo windowInfo, OpenType openType) {
        Screen screen = create(windowInfo, openType.getOpenMode(), NO_OPTIONS);
        show(screen);
        return screen instanceof Window ? (Window) screen : new ScreenWrapper(screen);
    }

    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Datasource parentDs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Window.Editor openEditor(WindowInfo windowInfo, Entity item, OpenType openType, Map<String, Object> params,
                                    Datasource parentDs) {
         // todo
        return null;
    }

    @Override
    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType,
                                    Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Window.Lookup openLookup(WindowInfo windowInfo, Window.Lookup.Handler handler, OpenType openType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Frame openFrame(Frame parentFrame, com.haulmont.cuba.gui.components.Component parent, WindowInfo windowInfo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Frame openFrame(Frame parentFrame, com.haulmont.cuba.gui.components.Component parent, WindowInfo windowInfo,
                           Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Frame openFrame(Frame parentFrame, com.haulmont.cuba.gui.components.Component parent, @Nullable String id,
                           WindowInfo windowInfo, Map<String, Object> params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close(Window window) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void openDefaultScreen() {
        // todo
    }

    @Override
    public void showNotification(String caption) {
        notifications.create()
                .setCaption(caption)
                .show();
    }

    @Override
    public void showNotification(String caption, Frame.NotificationType type) {
        notifications.create()
                .setCaption(caption)
                .setContentMode(Frame.NotificationType.isHTML(type) ? ContentMode.HTML : ContentMode.TEXT)
                .setType(convertNotificationType(type))
                .show();
    }

    @Override
    public void showNotification(String caption, String description, Frame.NotificationType type) {
        notifications.create()
                .setCaption(caption)
                .setDescription(description)
                .setContentMode(Frame.NotificationType.isHTML(type) ? ContentMode.HTML : ContentMode.TEXT)
                .setType(convertNotificationType(type))
                .show();
    }

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

    @Override
    public void showMessageDialog(String title, String message, Frame.MessageType messageType) {
        MessageDialog messageDialog = dialogs.createMessageDialog()
                .setCaption(title)
                .setMessage(message)
                .setType(convertMessageType(messageType.getMessageMode()))
                .setContentMode(
                        Frame.MessageMode.isHTML(messageType.getMessageMode()) ? ContentMode.HTML : ContentMode.TEXT
                );

        if (messageType.getWidth() != null) {
            messageDialog.setWidth(messageType.getWidth() + messageType.getWidthUnit().getSymbol());
        }
        if (messageType.getModal() != null) {
            messageDialog.setModal(messageType.getModal());
        }
        if (messageType.getCloseOnClickOutside() != null) {
            messageDialog.setCloseOnClickOutside(messageType.getCloseOnClickOutside());
        }
        if (messageType.getMaximized() != null) {
            messageDialog.setMaximized(messageType.getMaximized());
        }

        messageDialog.show();
    }

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

    @Override
    public void showOptionDialog(String title, String message, Frame.MessageType messageType, Action[] actions) {
        OptionDialog optionDialog = dialogs.createOptionDialog()
                .setCaption(title)
                .setMessage(message)
                .setType(convertMessageType(messageType.getMessageMode()))
                .setActions(actions);

        if (messageType.getWidth() != null) {
            optionDialog.setWidth(messageType.getWidth() + messageType.getWidthUnit().getSymbol());
        }
        if (messageType.getMaximized() != null) {
            optionDialog.setMaximized(messageType.getMaximized());
        }

        optionDialog.show();
    }

    @Override
    public void showExceptionDialog(Throwable throwable) {
        showExceptionDialog(throwable, null, null);
    }

    @Override
    public void showExceptionDialog(Throwable throwable, @Nullable String caption, @Nullable String message) {
        dialogs.createExceptionDialog()
                .setCaption(caption)
                .setMessage(message)
                .setThrowable(throwable)
                .show();
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        webBrowserTools.showWebPage(url, params);
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
     * todo
     *
     * @return operation result
     */
    public OperationResult checkModificationsAndCloseAll() {
        throw new UnsupportedOperationException("TODO");
    }

    public void closeAllTabbedWindows() {
        closeAllTabbedWindowsExcept(null);
    }

    public void closeAllTabbedWindowsExcept(@Nullable com.vaadin.ui.ComponentContainer keepOpened) {
        throw new UnsupportedOperationException();
    }

    /**
     * Close all screens in all main windows (browser tabs).
     */
    public void closeAllWindows() {
        throw new UnsupportedOperationException(); // todo
    }

    /**
     * Close all screens in the main window (browser tab) this WindowManagerImpl belongs to.
     */
    public void closeAll() {
        throw new UnsupportedOperationException(); // todo
    }

    protected <T extends Screen> T createController(WindowInfo windowInfo, Window window,
                                                    Class<T> screenClass, LaunchMode launchMode) {
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

    protected Window createWindow(WindowInfo windowInfo, Class<? extends Screen> screenClass, LaunchMode launchMode) {
        // todo forcibly dialog support
        // todo support forceDialog defined in XML / controller

        Window window;
        if (launchMode instanceof OpenMode) {
            OpenMode openMode = (OpenMode) launchMode;
            switch (openMode) {
                case ROOT:
                    // should be changed
                    ui.beforeTopLevelWindowInit();

                    window = componentsFactory.createComponent(RootWindow.NAME);
                    break;

                case THIS_TAB:
                case NEW_TAB:
                    window = componentsFactory.createComponent(TabWindow.NAME);
                    break;

                case DIALOG:
                    window = componentsFactory.createComponent(DialogWindow.NAME);
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported launch mode");
            }
        } else {
            throw new UnsupportedOperationException("Unsupported launch mode");
        }

        return window;
    }

    protected void checkMultiOpen(Screen screen) {
        // todo check if already opened, replace buggy int hash code
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

        String screenId = ScreenDescriptorUtils.getInferredScreenId(uiController, screenClass);

        return windowConfig.getWindowInfo(screenId);
    }

    protected void showRootWindow(Screen screen) {
        if (screen instanceof MainScreen) {
            MainScreen mainScreen = (MainScreen) screen;

            // bind system UI components to AbstractMainWindow
            walkComponents(screen.getWindow(), component -> {
                if (component instanceof AppWorkArea) {
                    mainScreen.setWorkArea((AppWorkArea) component);
                } else if (component instanceof UserIndicator) {
                    mainScreen.setUserIndicator((UserIndicator) component);
                } else if (component instanceof FoldersPane) {
                    mainScreen.setFoldersPane((FoldersPane) component);
                }

                return false;
            });
        }

        ui.setTopLevelWindow((RootWindow) screen.getWindow());

        if (screen instanceof Window.HasWorkArea) {
            AppWorkArea workArea = ((Window.HasWorkArea) screen).getWorkArea();
            if (workArea != null) {
                workArea.addStateChangeListener(new AppWorkArea.StateChangeListener() {
                    @Override
                    public void stateChanged(AppWorkArea.State newState) {
                        if (newState == AppWorkArea.State.WINDOW_CONTAINER) {
                            initTabShortcuts();

                            // listener used only once
                            getConfiguredWorkArea().removeStateChangeListener(this);
                        }
                    }
                });
            }
        }
    }

    protected void initTabShortcuts() {
        RootWindow topLevelWindow = ui.getTopLevelWindow();
        CubaOrderedActionsLayout actionsLayout = topLevelWindow.unwrap(CubaOrderedActionsLayout.class);

        if (getConfiguredWorkArea().getMode() == Mode.TABBED) {
            actionsLayout.addShortcutListener(createNextWindowTabShortcut(topLevelWindow));
            actionsLayout.addShortcutListener(createPreviousWindowTabShortcut(topLevelWindow));
        }
        actionsLayout.addShortcutListener(createCloseShortcut(topLevelWindow));
    }

    protected ShortcutListener createCloseShortcut(RootWindow topLevelWindow) {
        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination combination = KeyCombination.create(closeShortcut);

        return new ShortcutListenerDelegate("onClose", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers()))
                .withHandler((sender, target) ->
                        closeWindowByShortcut(topLevelWindow)
                );
    }

    protected ShortcutListener createNextWindowTabShortcut(RootWindow topLevelWindow) {
        String nextTabShortcut = clientConfig.getNextTabShortcut();
        KeyCombination combination = KeyCombination.create(nextTabShortcut);

        return new ShortcutListenerDelegate(
                "onNextTab", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())
        ).withHandler((sender, target) -> {
            WebAppWorkArea workArea = getConfiguredWorkArea();
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            if (tabSheet != null
                    && !hasModalWindow()
                    && tabSheet.getComponentCount() > 1) {
                com.vaadin.ui.Component selectedTabComponent = tabSheet.getSelectedTab();
                String tabId = tabSheet.getTab(selectedTabComponent);
                int tabPosition = tabSheet.getTabPosition(tabId);
                int newTabPosition = (tabPosition + 1) % tabSheet.getComponentCount();

                String newTabId = tabSheet.getTab(newTabPosition);
                tabSheet.setSelectedTab(newTabId);

                moveFocus(tabSheet, newTabId);
            }
        });
    }

    protected ShortcutListener createPreviousWindowTabShortcut(RootWindow topLevelWindow) {
        String previousTabShortcut = clientConfig.getPreviousTabShortcut();
        KeyCombination combination = KeyCombination.create(previousTabShortcut);

        return new ShortcutListenerDelegate("onPreviousTab", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())
        ).withHandler((sender, target) -> {
            WebAppWorkArea workArea = getConfiguredWorkArea();
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

            if (tabSheet != null
                    && !hasModalWindow()
                    && tabSheet.getComponentCount() > 1) {
                com.vaadin.ui.Component selectedTabComponent = tabSheet.getSelectedTab();
                String selectedTabId = tabSheet.getTab(selectedTabComponent);
                int tabPosition = tabSheet.getTabPosition(selectedTabId);
                int newTabPosition = (tabSheet.getComponentCount() + tabPosition - 1) % tabSheet.getComponentCount();

                String newTabId = tabSheet.getTab(newTabPosition);
                tabSheet.setSelectedTab(newTabId);

                moveFocus(tabSheet, newTabId);
            }
        });
    }

    protected void closeWindowByShortcut(RootWindow topLevelWindow) {
        WebAppWorkArea workArea = getConfiguredWorkArea();
        if (workArea.getState() != AppWorkArea.State.WINDOW_CONTAINER) {
            return;
        }

        if (workArea.getMode() == Mode.TABBED) {
            TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
            if (tabSheet != null) {
                TabWindowContainer layout = (TabWindowContainer) tabSheet.getSelectedTab();
                if (layout != null) {
                    tabSheet.focus();

                    WindowBreadCrumbs breadCrumbs = layout.getBreadCrumbs();

                    if (!canWindowBeClosed(breadCrumbs.getCurrentWindow())) {
                        return;
                    }

                    if (isCloseWithShortcutPrevented(breadCrumbs.getCurrentWindow())) {
                        return;
                    }

                    if (breadCrumbs.getWindows().isEmpty()) {
                        com.vaadin.ui.Component previousTab = tabSheet.getPreviousTab(layout);
                        if (previousTab != null) {
                            breadCrumbs.getCurrentWindow().closeAndRun(Window.CLOSE_ACTION_ID, () ->
                                    tabSheet.setSelectedTab(previousTab)
                            );
                        } else {
                            breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                        }
                    } else {
                        breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                    }
                }
            }
        } else {
            Iterator<WindowBreadCrumbs> it = getTabs(workArea).iterator();
            if (it.hasNext()) {
                Window currentWindow = it.next().getCurrentWindow();
                if (!isCloseWithShortcutPrevented(currentWindow)) {
                    ui.focus();
                    currentWindow.close(Window.CLOSE_ACTION_ID);
                }
            }
        }
    }

    protected List<WindowBreadCrumbs> getTabs(WebAppWorkArea workArea) {
        TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

        List<WindowBreadCrumbs> allBreadCrumbs = new ArrayList<>();
        for (int i = 0; i < tabSheet.getComponentCount(); i++) {
            String tabId = tabSheet.getTab(i);

            TabWindowContainer tabComponent = (TabWindowContainer) tabSheet.getTabComponent(tabId);
            allBreadCrumbs.add(tabComponent.getBreadCrumbs());
        }
        return allBreadCrumbs;
    }

    protected void moveFocus(TabSheetBehaviour tabSheet, String tabId) {
        TabWindowContainer windowContainer = (TabWindowContainer) tabSheet.getTabComponent(tabId);
        Window window = windowContainer.getBreadCrumbs().getCurrentWindow();

        if (window != null) {
            boolean focused = false;
            String focusComponentId = window.getFocusComponent();
            if (focusComponentId != null) {
                com.haulmont.cuba.gui.components.Component focusComponent = window.getComponent(focusComponentId);
                if (focusComponent instanceof com.haulmont.cuba.gui.components.Component.Focusable
                        && focusComponent.isEnabledRecursive()
                        && focusComponent.isVisibleRecursive()) {
                    ((com.haulmont.cuba.gui.components.Component.Focusable) focusComponent).focus();
                    focused = true;
                }
            }

            if (!focused && window instanceof Window.Wrapper) {
                Window.Wrapper wrapper = (Window.Wrapper) window;
                focused = ((WebWindow) wrapper.getWrappedWindow()).findAndFocusChildComponent();
                if (!focused) {
                    tabSheet.focus();
                }
            }
        }
    }

    protected void showNewTabWindow(Screen screen) {
        WebAppWorkArea workArea = getConfiguredWorkArea();
        workArea.switchTo(AppWorkArea.State.WINDOW_CONTAINER);

        // close previous windows
        if (workArea.getMode() == Mode.SINGLE) {
            VerticalLayout mainLayout = workArea.getSingleWindowContainer();
            if (mainLayout.getComponentCount() > 0) {
                TabWindowContainer oldLayout = (TabWindowContainer) mainLayout.getComponent(0);
                WindowBreadCrumbs oldBreadCrumbs = oldLayout.getBreadCrumbs();
                if (oldBreadCrumbs != null) {
                    Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                    oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, () -> {
                        // todo implement
//                            showWindow(window, caption, message, WindowManager.OpenType.NEW_TAB, false)
                    });
                    return;
                }
            }
        } else {
            /* todo
            Integer hashCode = getWindowHashCode(window);
            com.vaadin.ui.ComponentContainer tab = null;
            if (hashCode != null && !multipleOpen) {
                tab = findTab(hashCode);
            }

            com.vaadin.ui.ComponentContainer oldLayout = tab;
            final WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);

            if (oldBreadCrumbs != null
                    && windowOpenMode.containsKey(oldBreadCrumbs.getCurrentWindow().getFrame())
                    && !multipleOpen) {
                Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                selectWindowTab(((Window.Wrapper) oldBreadCrumbs.getCurrentWindow()).getWrappedWindow());

                int tabPosition = -1;
                final TabSheetBehaviour tabSheet = workArea.getTabbedWindowContainer().getTabSheetBehaviour();
                String tabId = tabSheet.getTab(tab);
                if (tabId != null) {
                    tabPosition = tabSheet.getTabPosition(tabId);
                }

                final int finalTabPosition = tabPosition;
                oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, () -> {
                    showWindow(window, caption, message, WindowManager.OpenType.NEW_TAB, false);

                    Window wrappedWindow = window;
                    if (window instanceof Window.Wrapper) {
                        wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
                    }

                    if (finalTabPosition >= 0 && finalTabPosition < tabSheet.getComponentCount() - 1) {
                        moveWindowTab(workArea, wrappedWindow, finalTabPosition);
                    }
                });
                return;
            }
            */
        }

        // work with new window
        createNewTabLayout(screen);
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs(@SuppressWarnings("unused") Screen screen) {
        WebAppWorkArea appWorkArea = getConfiguredWorkArea();

        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs(appWorkArea.getMode());
        windowBreadCrumbs.setBeanLocator(beanLocator);
        windowBreadCrumbs.afterPropertiesSet();

        boolean showBreadCrumbs = webConfig.getShowBreadCrumbs() || appWorkArea.getMode() == Mode.SINGLE;
        windowBreadCrumbs.setVisible(showBreadCrumbs);

        return windowBreadCrumbs;
    }

    protected void createNewTabLayout(Screen screen) {
        WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs(screen);
        breadCrumbs.setWindowNavigateHandler(this::handleWindowBreadCrumbsNavigate);
        breadCrumbs.addWindow(screen.getWindow());

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

            String tabId;

            ScreenContext screenContext = ScreenUtils.getScreenContext(screen);

            ScreenOptions options = screenContext.getScreenOptions();
            WindowInfo windowInfo = screenContext.getWindowInfo();

            com.vaadin.ui.ComponentContainer tab = findSameWindowTab(window, options);

            if (tab != null && !windowInfo.getMultipleOpen()) {
                tabSheet.replaceComponent(tab, windowContainer);
                tabSheet.removeComponent(tab);
                tabId = tabSheet.getTab(windowContainer);
            } else {
                tabId = "tab_" + uuidSource.createUuid();

                tabSheet.addTab(windowContainer, tabId);

                if (ui.isTestMode()) {
                    String id = "tab_" + window.getId();

                    tabSheet.setTabTestId(tabId, ui.getTestIdManager().getTestId(id));
                    tabSheet.setTabCubaId(tabId, id);
                }
            }
            String windowContentSwitchMode = window.getContentSwitchMode().name();
            ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(windowContentSwitchMode);
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);

            TabWindow tabWindow = (TabWindow) window;
            String formattedCaption = tabWindow.formatTabCaption();
            String formattedDescription = tabWindow.formatTabDescription();

            tabSheet.setTabCaption(tabId, formattedCaption);
            if (!Objects.equals(formattedCaption, formattedDescription)) {
                tabSheet.setTabDescription(tabId, formattedDescription);
            } else {
                tabSheet.setTabDescription(tabId, null);
            }

            tabSheet.setTabIcon(tabId, iconResolver.getIconResource(window.getIcon()));
            tabSheet.setTabClosable(tabId, true);
            tabSheet.setTabCloseHandler(windowContainer, this::handleTabWindowClose);
            tabSheet.setSelectedTab(windowContainer);
        } else {
            windowContainer.addStyleName("c-app-single-window");

            Layout mainLayout = workArea.getSingleWindowContainer();
            mainLayout.removeAllComponents();
            mainLayout.addComponent(windowContainer);
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
            windowContainer = (TabWindowContainer) workArea.getSingleWindowContainer().getComponent(0);
        }

        WindowBreadCrumbs breadCrumbs = windowContainer.getBreadCrumbs();
        if (breadCrumbs == null) {
            throw new IllegalStateException("BreadCrumbs not found");
        }

        Window currentWindow = breadCrumbs.getCurrentWindow();

        windowContainer.removeComponent(currentWindow.unwrapComposition(com.vaadin.ui.Layout.class));

        Window newWindow = screen.getWindow();
        com.vaadin.ui.Component newWindowComposition = newWindow.unwrapComposition(com.vaadin.ui.Component.class);

        windowContainer.addComponent(newWindowComposition);

        breadCrumbs.addWindow(newWindow);

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

            ContentSwitchMode contentSwitchMode = ContentSwitchMode.valueOf(newWindow.getContentSwitchMode().name());
            tabSheet.setContentSwitchMode(tabId, contentSwitchMode);
        } else {
            windowContainer.markAsDirtyRecursive();
        }
    }

    protected void showDialogWindow(Screen screen) {
        DialogWindow window = (DialogWindow) screen.getWindow();

        CubaWindow vWindow = window.unwrapComposition(CubaWindow.class);
        vWindow.setErrorHandler(ui);
        vWindow.addContextActionHandler(new DialogWindowActionHandler(window));

        if (ui.isTestMode()) {
            String cubaId = "dialog_" + window.getId();

            vWindow.setCubaId(cubaId);
            vWindow.setId(ui.getTestIdManager().getTestId(cubaId));
        }

        vWindow.addPreCloseListener(event -> {
            event.setPreventClose(true);
            if (!isCloseWithCloseButtonPrevented(window)) {
                // user has clicked on X
                window.close(Window.CLOSE_ACTION_ID);
            }
        });

        setupDialogShortcuts(window);

        if (hasModalWindow()) {
            window.setModal(true);
        }

        // todo forciblyDialog

        // todo default size

        ui.addWindow(vWindow);
    }

    protected void setupDialogShortcuts(Window window) {
        CubaWindow vWindow = window.unwrapComposition(CubaWindow.class);
        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        ShortcutListenerDelegate exitAction = new ShortcutListenerDelegate(
                "closeShortcutAction",
                closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())
        );

        exitAction.withHandler((sender, target) -> {
            // todo forciblyDialog ?
            if (vWindow.isClosable()) {
                if (isCloseWithShortcutPrevented(window)) {
                    return;
                }
                window.close(Window.CLOSE_ACTION_ID);
            }
        });

        vWindow.addActionHandler(new com.vaadin.event.Action.Handler() {
            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new ShortcutAction[]{exitAction};
            }

            @Override
            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                if (action == exitAction) {
                    exitAction.handleAction(sender, target);
                }
            }
        });
    }

    protected WebAppWorkArea getConfiguredWorkArea() {
        RootWindow topLevelWindow = ui.getTopLevelWindow();

        Screen controller = topLevelWindow.getFrameOwner();

        if (controller instanceof HasWorkArea) {
            AppWorkArea workArea = ((HasWorkArea) controller).getWorkArea();
            if (workArea != null) {
                return (WebAppWorkArea) workArea;
            }
        }

        throw new IllegalStateException("RootWindow does not have any configured work area");
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
                    if (!isCloseWithCloseButtonPrevented(currentWindow)) {
                        // todo call controller instead
                        currentWindow.closeAndRun(CLOSE_ACTION_ID, this);
                    }
                }
            }
        };
        op.run();
    }

    protected void handleTabWindowClose(HasTabSheetBehaviour targetTabSheet, com.vaadin.ui.Component tabContent) {
        WindowBreadCrumbs tabBreadCrumbs = ((TabWindowContainer) tabContent).getBreadCrumbs();

        if (!canWindowBeClosed(tabBreadCrumbs.getCurrentWindow())) {
            return;
        }

        Runnable closeTask = new TabCloseTask(tabBreadCrumbs);
        closeTask.run();

        // it is needed to force redraw tabsheet if it has a lot of tabs and part of them are hidden
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
                if (!isCloseWithCloseButtonPrevented(windowToClose)) {
                    // todo call controller method
                    windowToClose.closeAndRun(CLOSE_ACTION_ID, new TabCloseTask(breadCrumbs));
                }
            }
        }
    }

    // todo provide single BeforeClose event, move to screen
    protected boolean isCloseWithShortcutPrevented(Window window) {
        BeforeCloseWithShortcutEvent event = new BeforeCloseWithShortcutEvent(window);
        ((WebWindow) window).fireBeforeCloseWithShortcut(event);
        return event.isClosePrevented();
    }

    // todo provide single BeforeClose event, move to screen
    protected boolean isCloseWithCloseButtonPrevented(Window window) {
        BeforeCloseWithCloseButtonEvent event = new BeforeCloseWithCloseButtonEvent(window);
        ((WebWindow) window).fireBeforeCloseWithCloseButton(event);
        return event.isClosePrevented();
    }

    protected boolean canWindowBeClosed(Window window) {
        if (!window.isCloseable()) {
            return false;
        }

        if (webConfig.getDefaultScreenCanBeClosed()) {
            return true;
        }

        String defaultScreenId = webConfig.getDefaultScreenId();

        if (webConfig.getUserCanChooseDefaultScreen()) {
            String userDefaultScreen = userSettingService.loadSetting(ClientType.WEB, "userDefaultScreen");
            defaultScreenId = StringUtils.isEmpty(userDefaultScreen) ? defaultScreenId : userDefaultScreen;
        }

        return !window.getId().equals(defaultScreenId);
    }

    protected com.vaadin.ui.ComponentContainer findSameWindowTab(Window window, ScreenOptions options) {
        WebAppWorkArea workArea = getConfiguredWorkArea();

        TabSheetBehaviour tabSheetBehaviour = workArea.getTabbedWindowContainer().getTabSheetBehaviour();

        Iterator<com.vaadin.ui.Component> componentIterator = tabSheetBehaviour.getTabComponents();
        while (componentIterator.hasNext()) {
            TabWindowContainer component = (TabWindowContainer) componentIterator.next();
            Window currentWindow = component.getBreadCrumbs().getCurrentWindow();

//            todo include options hash into Window instance
//            if (hashCode.equals(getWindowHashCode(currentWindow))) {
//                return entry.getKey();
//            }
        }
        return null;
    }

    protected boolean hasModalWindow() {
        return ui.getWindows().stream()
                .anyMatch(com.vaadin.ui.Window::isModal);
    }

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

    protected class DialogWindowActionHandler implements com.vaadin.event.Action.Handler {

        protected Window window;
        protected com.vaadin.event.Action saveSettingsAction;
        protected com.vaadin.event.Action restoreToDefaultsAction;

        protected com.vaadin.event.Action analyzeAction;

        protected boolean initialized = false;

        public DialogWindowActionHandler(Window window) {
            this.window = window;
        }

        @Override
        public com.vaadin.event.Action[] getActions(Object target, Object sender) {
            if (!initialized) {
                saveSettingsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.saveSettings"));
                restoreToDefaultsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.restoreToDefaults"));
                analyzeAction = new com.vaadin.event.Action(messages.getMainMessage("actions.analyzeLayout"));

                initialized = true;
            }

            List<com.vaadin.event.Action> actions = new ArrayList<>(3);

            if (clientConfig.getManualScreenSettingsSaving()) {
                actions.add(saveSettingsAction);
                actions.add(restoreToDefaultsAction);
            }
            if (clientConfig.getLayoutAnalyzerEnabled()) {
                actions.add(analyzeAction);
            }

            return actions.toArray(new com.vaadin.event.Action[0]);
        }

        @Override
        public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
            if (initialized) {
                if (saveSettingsAction == action) {
                    Screen screen = window.getFrameOwner();
                    ScreenUtils.saveSettings(screen);
                } else if (restoreToDefaultsAction == action) {
                    Screen screen = window.getFrameOwner();
                    ScreenUtils.deleteSettings(screen);
                } else if (analyzeAction == action) {
                    LayoutAnalyzer analyzer = new LayoutAnalyzer();
                    List<LayoutTip> tipsList = analyzer.analyze(window);

                    if (tipsList.isEmpty()) {
                        showNotification("No layout problems found", Frame.NotificationType.HUMANIZED);
                    } else {
                        WindowInfo windowInfo = windowConfig.getWindowInfo("layoutAnalyzer");
                        openWindow(windowInfo, OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                    }
                }
            }
        }
    }
}
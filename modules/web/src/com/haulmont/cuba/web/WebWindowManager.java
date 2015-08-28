/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.mainwindow.WebAppWorkArea;
import com.haulmont.cuba.web.sys.WindowBreadCrumbs;
import com.haulmont.cuba.web.toolkit.ui.CubaLabel;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.haulmont.cuba.web.toolkit.ui.CubaWindow;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.cuba.gui.components.Component.AUTO_SIZE;
import static com.haulmont.cuba.gui.components.Frame.MessageType;
import static com.haulmont.cuba.gui.components.Frame.NotificationType;
import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.convertNotificationType;
import static com.vaadin.server.Sizeable.Unit;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebWindowManager extends WindowManager {

    public static final int HUMANIZED_NOTIFICATION_DELAY_MSEC = 3000;
    public static final int WARNING_NOTIFICATION_DELAY_MSEC = -1;

    private static final Logger log = LoggerFactory.getLogger(WebWindowManager.class);

    protected App app;
    protected AppUI ui;
    protected AppWindow appWindow;

    protected final WebConfig webConfig;
    protected final ClientConfig clientConfig;

    protected final Map<ComponentContainer, WindowBreadCrumbs> tabs = new HashMap<>();
    protected final Map<WindowBreadCrumbs, Stack<Map.Entry<Window, Integer>>> stacks = new HashMap<>();
    protected final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<>();
    protected final Map<Window, Integer> windows = new HashMap<>();

    protected boolean disableSavingScreenHistory;
    protected ScreenHistorySupport screenHistorySupport;

    public WebWindowManager(final App app, AppWindow appWindow) {
        this.ui = app.getAppUI();
        this.app = app;
        this.appWindow = appWindow;

        Configuration configuration = AppBeans.get(Configuration.NAME);
        webConfig = configuration.getConfig(WebConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);

        screenHistorySupport = new ScreenHistorySupport();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public App getApp() {
        return app;
    }

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<>(windowOpenMode.keySet());
    }

    @Override
    public void selectWindowTab(Window window) {
        WindowOpenMode openMode = windowOpenMode.get(window);
        if (openMode != null) {
            OpenType openType = openMode.getOpenType();
            if (openType == OpenType.NEW_TAB || openType == OpenType.THIS_TAB) {
                // show in tabsheet
                Layout layout = (Layout) openMode.getData();
                TabSheet webTabsheet = getConfiguredWorkArea().getTabbedWindowContainer();
                webTabsheet.setSelectedTab(layout);
            }
        }
    }

    @Override
    public void setWindowCaption(Window window, String caption, String description) {
        Window webWindow = window;
        if (window instanceof Window.Wrapper) {
            webWindow = ((Window.Wrapper) window).getWrappedWindow();
        }
        window.setCaption(caption);
        window.setDebugId(description);

        WindowOpenMode openMode = windowOpenMode.get(webWindow);
        String formattedCaption;
        if (openMode != null
                && (openMode.getOpenType() == OpenType.NEW_TAB || openMode.getOpenType() == OpenType.THIS_TAB)) {
            formattedCaption = formatTabCaption(caption, description);
        } else {
            formattedCaption = formatTabDescription(caption, description);
        }

        if (openMode != null) {
            if (openMode.getOpenType() == OpenType.DIALOG) {
                com.vaadin.ui.Window dialog = (com.vaadin.ui.Window) openMode.getData();
                dialog.setCaption(formattedCaption);
            } else {
                if (getConfiguredWorkArea().getMode() == AppWorkArea.Mode.SINGLE) {
                    return;
                }

                com.vaadin.ui.Component tabContent = (Component) openMode.getData();
                if (tabContent == null) {
                    return;
                }

                TabSheet tabSheet = getConfiguredWorkArea().getTabbedWindowContainer();
                TabSheet.Tab tab = tabSheet.getTab(tabContent);
                if (tab == null) {
                    return;
                }

                tab.setCaption(formattedCaption);

                String formattedDescription = formatTabDescription(caption, description);
                if (!StringUtils.equals(formattedDescription, formattedCaption)) {
                    tab.setDescription(formattedDescription);
                } else {
                    tab.setDescription(null);
                }
            }
        }
    }

    protected static class WindowOpenMode {
        protected Window window;
        protected OpenType openType;
        protected Object data;

        public WindowOpenMode(Window window, OpenType openType) {
            this.window = window;
            this.openType = openType;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Window getWindow() {
            return window;
        }

        public OpenType getOpenType() {
            return openType;
        }
    }

    protected ComponentContainer findTab(Integer hashCode) {
        Set<Map.Entry<ComponentContainer, WindowBreadCrumbs>> set = tabs.entrySet();
        for (Map.Entry<ComponentContainer, WindowBreadCrumbs> entry : set) {
            Window currentWindow = entry.getValue().getCurrentWindow();
            if (hashCode.equals(getWindowHashCode(currentWindow))) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected Stack<Map.Entry<Window, Integer>> getStack(WindowBreadCrumbs breadCrumbs) {
        return stacks.get(breadCrumbs);
    }

    protected boolean hasModalWindow() {
        for (Map.Entry<Window, WindowOpenMode> openMode : windowOpenMode.entrySet()) {
            if (OpenType.DIALOG.equals(openMode.getValue().getOpenType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void showWindow(final Window window, final String caption, OpenType type, boolean multipleOpen) {
        showWindow(window, caption, null, type, multipleOpen);
    }

    @Override
    protected void showWindow(final Window window, final String caption, final String description, OpenType type,
                              final boolean multipleOpen) {
        boolean forciblyDialog = false;
        if (type != OpenType.DIALOG && hasModalWindow()) {
            type = OpenType.DIALOG;
            forciblyDialog = true;
        }

        if (type == OpenType.THIS_TAB && tabs.size() == 0) {
            type = OpenType.NEW_TAB;
        }

        final WindowOpenMode openMode = new WindowOpenMode(window, type);
        Component component;

        window.setCaption(caption);
        window.setDescription(description);

        switch (type) {
            case NEW_TAB:
            case NEW_WINDOW:
                final WebAppWorkArea workArea = getConfiguredWorkArea();
                workArea.switchTo(AppWorkArea.State.WINDOW_CONTAINER);

                if (workArea.getMode() == AppWorkArea.Mode.SINGLE) {
                    VerticalLayout mainLayout = workArea.getSingleWindowContainer();
                    if (mainLayout.iterator().hasNext()) {
                        ComponentContainer oldLayout = (ComponentContainer) mainLayout.iterator().next();
                        WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);
                        if (oldBreadCrumbs != null) {
                            Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                            oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, new Runnable() {
                                @Override
                                public void run() {
                                    showWindow(window, caption, description, OpenType.NEW_TAB, false);
                                }
                            });
                            return;
                        }
                    }
                } else {
                    final Integer hashCode = getWindowHashCode(window);
                    ComponentContainer tab = null;
                    if (hashCode != null && !multipleOpen) {
                        tab = findTab(hashCode);
                    }
                    ComponentContainer oldLayout = tab;
                    final WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);

                    if (oldBreadCrumbs != null
                            && windowOpenMode.containsKey(oldBreadCrumbs.getCurrentWindow().<Window>getFrame())
                            && !multipleOpen) {
                        Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                        selectWindowTab(((Window.Wrapper) oldBreadCrumbs.getCurrentWindow()).getWrappedWindow());

                        int tabPosition = -1;
                        final TabSheet tabSheet = workArea.getTabbedWindowContainer();
                        TabSheet.Tab oldWindowTab = tabSheet.getTab(tab);
                        if (oldWindowTab != null) {
                            tabPosition = tabSheet.getTabPosition(oldWindowTab);
                        }

                        final int finalTabPosition = tabPosition;
                        oldWindow.closeAndRun(MAIN_MENU_ACTION_ID, new Runnable() {
                            @Override
                            public void run() {
                                showWindow(window, caption, description, OpenType.NEW_TAB, false);

                                Window wrappedWindow = window;
                                if (window instanceof Window.Wrapper) {
                                    wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
                                }

                                if (finalTabPosition >= 0 && finalTabPosition < tabSheet.getComponentCount() - 1) {
                                    moveWindowTab(workArea, wrappedWindow, finalTabPosition);
                                }
                            }
                        });
                        return;
                    }
                }
                component = showWindowNewTab(window, multipleOpen);
                break;

            case THIS_TAB:
                getConfiguredWorkArea().switchTo(AppWorkArea.State.WINDOW_CONTAINER);

                component = showWindowThisTab(window, caption, description);
                break;

            case DIALOG:
                component = showWindowDialog(window, forciblyDialog);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        openMode.setData(component);

        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            windowOpenMode.put(wrappedWindow, openMode);
        } else {
            windowOpenMode.put(window, openMode);
        }

        afterShowWindow(window);
    }

    /**
     * @param workArea Work area
     * @param window   Window implementation (WebWindow)
     * @param position new tab position
     */
    protected void moveWindowTab(WebAppWorkArea workArea, Window window, int position) {
        // move tab to
        CubaTabSheet tabSheet = workArea.getTabbedWindowContainer();

        if (position >= 0 && position < tabSheet.getComponentCount()) {
            WindowOpenMode openMode = windowOpenMode.get(window);
            if (openMode != null) {
                OpenType openType = openMode.getOpenType();
                if (openType == OpenType.NEW_TAB || openType == OpenType.THIS_TAB) {
                    // show in tabsheet
                    Layout layout = (Layout) openMode.getData();

                    tabSheet.moveTab(layout, position);
                    tabSheet.setSelectedTab(layout);
                }
            }
        }
    }

    protected void moveFocus(TabSheet tabSheet, TabSheet.Tab tab) {
        //noinspection SuspiciousMethodCalls
        Window window = tabs.get(tab.getComponent()).getCurrentWindow();

        if (window != null) {
            boolean focused = false;
            String focusComponentId = window.getFocusComponent();
            if (focusComponentId != null) {
                com.haulmont.cuba.gui.components.Component focusComponent = window.getComponent(focusComponentId);
                if (focusComponent != null && focusComponent.isEnabled() && focusComponent.isVisible()) {
                    focusComponent.requestFocus();
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

    protected Component showWindowNewTab(final Window window, final boolean multipleOpen) {
        getDialogParams().reset();

        final WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs();
        breadCrumbs.addListener(
                new WindowBreadCrumbs.Listener() {
                    @Override
                    public void windowClick(final Window window) {
                        Runnable op = new Runnable() {
                            @Override
                            public void run() {
                                Window currentWindow = breadCrumbs.getCurrentWindow();

                                if (currentWindow != null && window != currentWindow) {
                                    currentWindow.closeAndRun(Window.CLOSE_ACTION_ID, this);
                                }
                            }
                        };
                        op.run();
                    }
                }
        );
        breadCrumbs.addWindow(window);

        //noinspection UnnecessaryLocalVariable
        final Layout layout = createNewTabLayout(window, multipleOpen, breadCrumbs);

        return layout;
    }

    protected Layout createNewTabLayout(final Window window, final boolean multipleOpen, WindowBreadCrumbs breadCrumbs,
                                        Component... additionalComponents) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("cuba-app-tabbed-window");
        layout.setSizeFull();

        layout.addComponent(breadCrumbs);
        if (additionalComponents != null) {
            for (final Component c : additionalComponents) {
                layout.addComponent(c);
            }
        }

        final Component component = WebComponentsHelper.getComposition(window);
        component.setSizeFull();
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        WebAppWorkArea workArea = getConfiguredWorkArea();

        if (workArea.getMode() == AppWorkArea.Mode.TABBED) {
            CubaTabSheet tabSheet = workArea.getTabbedWindowContainer();

            layout.setMargin(true);
            TabSheet.Tab newTab;
            Integer hashCode = getWindowHashCode(window);
            ComponentContainer tab = null;
            if (hashCode != null) {
                tab = findTab(hashCode);
            }
            if (tab != null && !multipleOpen) {
                tabSheet.replaceComponent(tab, layout);
                tabSheet.removeComponent(tab);
                tabs.put(layout, breadCrumbs);
                newTab = tabSheet.getTab(layout);
            } else {
                tabs.put(layout, breadCrumbs);

                newTab = tabSheet.addTab(layout);

                if (ui.isTestMode()) {
                    tabSheet.setTestId(newTab, ui.getTestIdManager().getTestId("tab_" + window.getId()));
                    tabSheet.setCubaId(newTab, "tab_" + window.getId());
                }
            }
            String formattedCaption = formatTabCaption(window.getCaption(), window.getDescription());
            newTab.setCaption(formattedCaption);
            String formattedDescription = formatTabDescription(window.getCaption(), window.getDescription());
            if (!StringUtils.equals(formattedCaption, formattedDescription)) {
                newTab.setDescription(formattedDescription);
            } else {
                newTab.setDescription(null);
            }

            newTab.setClosable(true);
            tabSheet.setTabCloseHandler(layout,
                    new CubaTabSheet.TabCloseHandler() {
                        @Override
                        public void onClose(TabSheet tabSheet, Component tabContent) {
                            //noinspection SuspiciousMethodCalls
                            WindowBreadCrumbs breadCrumbs = tabs.get(tabContent);
                            Runnable closeTask = new TabCloseTask(breadCrumbs);
                            closeTask.run();
                        }
                    });
            tabSheet.setSelectedTab(layout);
        } else {
            tabs.put(layout, breadCrumbs);
            layout.addStyleName("cuba-app-single-window");
            layout.setMargin(true);

            VerticalLayout mainLayout = workArea.getSingleWindowContainer();
            mainLayout.removeAllComponents();
            mainLayout.addComponent(layout);
        }

        return layout;
    }

    public class TabCloseTask implements Runnable {
        private final WindowBreadCrumbs breadCrumbs;

        public TabCloseTask(WindowBreadCrumbs breadCrumbs) {
            this.breadCrumbs = breadCrumbs;
        }

        @Override
        public void run() {
            Window windowToClose = breadCrumbs.getCurrentWindow();
            if (windowToClose != null) {
                windowToClose.closeAndRun(Window.CLOSE_ACTION_ID, new TabCloseTask(breadCrumbs));
            }
        }
    }

    protected String formatTabCaption(final String caption, final String description) {
        String s = formatTabDescription(caption, description);
        int maxLength = webConfig.getMainTabCaptionLength();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        } else {
            return s;
        }
    }

    protected String formatTabDescription(final String caption, final String description) {
        if (!StringUtils.isEmpty(description)) {
            return String.format("%s: %s", caption, description);
        } else {
            return caption;
        }
    }

    protected Component showWindowThisTab(final Window window, final String caption, final String description) {
        getDialogParams().reset();

        WebAppWorkArea workArea = getConfiguredWorkArea();

        VerticalLayout layout;
        if (workArea.getMode() == AppWorkArea.Mode.TABBED) {
            TabSheet tabSheet = workArea.getTabbedWindowContainer();
            layout = (VerticalLayout) tabSheet.getSelectedTab();
        } else {
            layout = (VerticalLayout) workArea.getSingleWindowContainer().getComponent(0);
        }

        final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
        if (breadCrumbs == null) {
            throw new IllegalStateException("BreadCrumbs not found");
        }

        final Window currentWindow = breadCrumbs.getCurrentWindow();

        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        boolean pushed = false;
        for (Map.Entry<Window, Integer> entry : set) {
            if (entry.getKey().equals(currentWindow)) {
                windows.remove(currentWindow);
                getStack(breadCrumbs).push(entry);
                pushed = true;
                break;
            }
        }
        if (!pushed) {
            getStack(breadCrumbs).push(new AbstractMap.SimpleEntry<Window, Integer>(currentWindow, null));
        }

        removeFromWindowMap(currentWindow);
        layout.removeComponent(WebComponentsHelper.getComposition(currentWindow));

        final Component component = WebComponentsHelper.getComposition(window);
        component.setSizeFull();
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        breadCrumbs.addWindow(window);

        if (workArea.getMode() == AppWorkArea.Mode.TABBED) {
            TabSheet tabSheet = workArea.getTabbedWindowContainer();
            TabSheet.Tab tab = tabSheet.getTab(layout);
            String formattedCaption = formatTabCaption(caption, description);
            tab.setCaption(formattedCaption);
            String formattedDescription = formatTabDescription(caption, description);

            if (!StringUtils.equals(formattedCaption, formattedDescription)) {
                tab.setDescription(formattedDescription);
            } else {
                tab.setDescription(null);
            }
        } else {
            layout.markAsDirtyRecursive();
        }

        return layout;
    }

    protected WebAppWorkArea getConfiguredWorkArea() {
        AppWorkArea workArea = appWindow.getMainWindow().getWorkArea();
        if (workArea == null) {
            throw new IllegalStateException("Application does not have any configured work area");
        }
        return (WebAppWorkArea) workArea;
    }

    protected Component showWindowDialog(final Window window, boolean forciblyDialog) {
        final CubaWindow vWindow = createDialogWindow(window);
        vWindow.setStyleName("cuba-app-dialog-window");
        if (ui.isTestMode()) {
            vWindow.setCubaId("dialog_" + window.getId());
            vWindow.setId(ui.getTestIdManager().getTestId("dialog_" + window.getId()));
        }

        Layout layout = (Layout) WebComponentsHelper.getComposition(window);
        vWindow.setContent(layout);

        vWindow.addPreCloseListener(new CubaWindow.PreCloseListener() {
            @Override
            public void beforeWindowClose(CubaWindow.PreCloseEvent event) {
                event.setPreventClose(true);

                window.close(Window.CLOSE_ACTION_ID);
            }
        });

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        com.vaadin.event.ShortcutAction exitAction = new com.vaadin.event.ShortcutAction(
                "closeShortcutAction",
                closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())
        );

        Map<com.vaadin.event.Action, Runnable> actions = new HashMap<>();
        actions.put(exitAction, new Runnable() {
            @Override
            public void run() {
                window.close(Window.CLOSE_ACTION_ID);
            }
        });

        WebComponentsHelper.setActions(vWindow, actions);

        final DialogParams dialogParams = getDialogParams();
        boolean dialogParamsSizeUndefined = dialogParams.getHeight() == null && dialogParams.getWidth() == null;

        ThemeConstants theme = app.getThemeConstants();

        if (forciblyDialog && dialogParamsSizeUndefined) {
            layout.setHeight(100, Unit.PERCENTAGE);

            vWindow.setWidth(theme.getInt("cuba.web.WebWindowManager.forciblyDialog.width"), Unit.PIXELS);
            vWindow.setHeight(theme.getInt("cuba.web.WebWindowManager.forciblyDialog.height"), Unit.PIXELS);

            // resizable by default, but may be overridden in dialog params
            vWindow.setResizable(BooleanUtils.isNotFalse(dialogParams.getResizable()));

            window.setHeight("100%");
        } else {
            if (dialogParams.getWidth() == null) {
                vWindow.setWidth(theme.getInt("cuba.web.WebWindowManager.dialog.width"), Unit.PIXELS);
            } else if (dialogParams.getWidth() == DialogParams.AUTO_SIZE_PX) {
                vWindow.setWidthUndefined();
                layout.setWidthUndefined();
                window.setWidth(AUTO_SIZE);
            } else {
                vWindow.setWidth(dialogParams.getWidth().floatValue(), Unit.PIXELS);
            }

            if (dialogParams.getHeight() != null && dialogParams.getHeight() != DialogParams.AUTO_SIZE_PX) {
                vWindow.setHeight(dialogParams.getHeight().floatValue(), Unit.PIXELS);
                layout.setHeight("100%");
                window.setHeight("100%");
            } else {
                window.setHeight(AUTO_SIZE);
            }

            // non resizable by default
            vWindow.setResizable(BooleanUtils.isTrue(dialogParams.getResizable()));
        }

        if (dialogParams.getCloseable() != null) {
            vWindow.setClosable(dialogParams.getCloseable());
        }

        boolean modal = true;
        if (!hasModalWindow() && dialogParams.getModal() != null) {
            modal = dialogParams.getModal();
        }
        vWindow.setModal(modal);

        dialogParams.reset();

        ui.addWindow(vWindow);
        vWindow.center();

        return vWindow;
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs() {
        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs(getConfiguredWorkArea());
        windowBreadCrumbs.setVisible(webConfig.getShowBreadCrumbs());
        stacks.put(windowBreadCrumbs, new Stack<Map.Entry<Window, Integer>>());
        return windowBreadCrumbs;
    }

    protected CubaWindow createDialogWindow(Window window) {
        CubaWindow dialogWindow = new CubaWindow(window.getCaption());
        dialogWindow.setErrorHandler(ui);
        dialogWindow.addContextActionHandler(new DialogWindowActionHandler(window));
        return dialogWindow;
    }

    @Override
    public void close(Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        final WindowOpenMode openMode = windowOpenMode.get(window);
        if (openMode == null) {
            log.warn("Problem closing window " + window + " : WindowOpenMode not found");
            return;
        }
        disableSavingScreenHistory = false;
        closeWindow(window, openMode);
        windowOpenMode.remove(window);
        removeFromWindowMap(openMode.getWindow());
    }

    /**
     * Check modifications and close all screens in all main windows.
     * @param runIfOk   a closure to run after all screens are closed
     */
    public void checkModificationsAndCloseAll(Runnable runIfOk) {
        checkModificationsAndCloseAll(runIfOk, null);
    }

    /**
     * Check modifications and close all screens in all main windows.
     * @param runIfOk       a closure to run after all screens are closed
     * @param runIfCancel   a closure to run if there were modifications and a user canceled the operation
     */
    public void checkModificationsAndCloseAll(final Runnable runIfOk, final @Nullable Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            if (!disableSavingScreenHistory) {
                screenHistorySupport.saveScreenHistory(window, windowOpenMode.get(window).getOpenType());
            }

            if (window instanceof WrappedWindow && ((WrappedWindow) window).getWrapper() != null) {
                ((WrappedWindow) window).getWrapper().saveSettings();
            } else {
                window.saveSettings();
            }

            if (window.getDsContext() != null && window.getDsContext().isModified()) {
                modified = true;
            }
        }
        disableSavingScreenHistory = true;
        if (modified) {
            showOptionDialog(
                    messages.getMessage(WebWindow.class, "closeUnsaved.caption"),
                    messages.getMessage(WebWindow.class, "discardChangesOnClose"),
                    MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(messages.getMessage(WebWindow.class, "closeApplication")) {

                                {
                                    ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
                                    icon = thCM.getThemeValue("actions.dialog.Ok.icon");
                                }

                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    closeAllWindows();
                                    runIfOk.run();
                                }
                            },
                            new DialogAction(Type.CANCEL, Status.PRIMARY) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfCancel != null) {
                                        runIfCancel.run();
                                    }
                                }
                            }
                    }
            );
        } else {
            closeAllWindows();
            runIfOk.run();
        }
    }

    /**
     * Close all screens in all main windows (browser tabs).
     */
    public void closeAllWindows() {
        app.cleanupBackgroundTasks();
        app.closeAllWindows();
    }

    /**
     * Close all screens in the main window (browser tab) this WindowManager belongs to.
     */
    public void closeAll() {
        List<Map.Entry<Window, WindowOpenMode>> entries = new ArrayList<>(windowOpenMode.entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            WebWindow window = (WebWindow) entries.get(i).getKey();
            window.stopTimers();

            if (window instanceof WebWindow.Editor) {
                ((WebWindow.Editor) window).releaseLock();
            }
            closeWindow(window, entries.get(i).getValue());
        }
        disableSavingScreenHistory = false;
        windowOpenMode.clear();
        windows.clear();
    }

    protected void closeWindow(Window window, WindowOpenMode openMode) {
        if (!disableSavingScreenHistory) {
            screenHistorySupport.saveScreenHistory(window, openMode.getOpenType());
        }

        WebWindow webWindow = (WebWindow) window;
        webWindow.stopTimers();

        switch (openMode.openType) {
            case DIALOG: {
                final CubaWindow cubaDialogWindow = (CubaWindow) openMode.getData();
                cubaDialogWindow.dispose();
                fireListeners(window, tabs.size() != 0);
                break;
            }
            case NEW_TAB: {
                final Layout layout = (Layout) openMode.getData();
                layout.removeComponent(WebComponentsHelper.getComposition(window));

                WebAppWorkArea workArea = getConfiguredWorkArea();

                if (workArea.getMode() == AppWorkArea.Mode.TABBED) {
                    CubaTabSheet tabSheet = workArea.getTabbedWindowContainer();
                    tabSheet.silentCloseTabAndSelectPrevious(layout);
                    tabSheet.removeComponent(layout);
                } else {
                    VerticalLayout singleLayout = workArea.getSingleWindowContainer();
                    singleLayout.removeComponent(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = tabs.get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                tabs.remove(layout);
                stacks.remove(windowBreadCrumbs);
                fireListeners(window, !tabs.isEmpty());
                if (tabs.isEmpty() && app.getConnection().isConnected()) {
                    workArea.switchTo(AppWorkArea.State.INITIAL_LAYOUT);
                }
                break;
            }
            case THIS_TAB: {
                final VerticalLayout layout = (VerticalLayout) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                if (breadCrumbs == null) {
                    throw new IllegalStateException("Unable to close screen: breadCrumbs not found");
                }

                breadCrumbs.removeWindow();
                Window currentWindow = breadCrumbs.getCurrentWindow();
                if (!getStack(breadCrumbs).empty()) {
                    Map.Entry<Window, Integer> entry = getStack(breadCrumbs).pop();
                    putToWindowMap(entry.getKey(), entry.getValue());
                }
                final Component component = WebComponentsHelper.getComposition(currentWindow);
                component.setSizeFull();

                WebAppWorkArea workArea = getConfiguredWorkArea();

                layout.removeComponent(WebComponentsHelper.getComposition(window));
                if (app.getConnection().isConnected()) {
                    layout.addComponent(component);
                    layout.setExpandRatio(component, 1);

                    if (workArea.getMode() == AppWorkArea.Mode.TABBED) {
                        TabSheet tabSheet = workArea.getTabbedWindowContainer();
                        TabSheet.Tab tab = tabSheet.getTab(layout);
                        String formattedCaption = formatTabCaption(currentWindow.getCaption(), currentWindow.getDescription());
                        tab.setCaption(formattedCaption);
                        String formattedDescription = formatTabDescription(currentWindow.getCaption(), currentWindow.getDescription());

                        if (!StringUtils.equals(formattedCaption, formattedDescription)) {
                            tab.setDescription(formattedDescription);
                        } else {
                            tab.setDescription(null);
                        }
                    }
                }
                fireListeners(window, !tabs.isEmpty());
                if (tabs.isEmpty() && app.getConnection().isConnected()) {
                    workArea.switchTo(AppWorkArea.State.INITIAL_LAYOUT);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public void showFrame(com.haulmont.cuba.gui.components.Component parent, Frame frame) {
        if (parent instanceof com.haulmont.cuba.gui.components.Component.Container) {
            com.haulmont.cuba.gui.components.Component.Container container =
                    (com.haulmont.cuba.gui.components.Component.Container) parent;
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
            container.add(frame);
        } else {
            throw new IllegalStateException(
                    "Parent component must be com.haulmont.cuba.gui.components.Component.Container"
            );
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showNotification(String caption, Frame.NotificationType type) {
        showNotification(caption, null, type);
    }

    @Override
    public void showNotification(String caption, String description, Frame.NotificationType type) {
        Notification notification = new Notification(caption, description, convertNotificationType(type));
        notification.setHtmlContentAllowed(NotificationType.isHTML(type));
        setNotificationDelayMsec(notification, type);
        notification.show(Page.getCurrent());
    }

    protected void setNotificationDelayMsec(Notification notification, Frame.NotificationType type) {
        switch (type) {
            case HUMANIZED:
            case HUMANIZED_HTML:
                notification.setDelayMsec(HUMANIZED_NOTIFICATION_DELAY_MSEC);
                break;
            case WARNING:
            case WARNING_HTML:
                notification.setDelayMsec(WARNING_NOTIFICATION_DELAY_MSEC);
                break;
        }
    }

    @Override
    public void showMessageDialog(String title, String message, MessageType messageType) {
        final com.vaadin.ui.Window vWindow = new CubaWindow(title);

        if (ui.isTestMode()) {
            vWindow.setCubaId("messageDialog");
            vWindow.setId(ui.getTestIdManager().getTestId("messageDialog"));
        }

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        vWindow.addAction(new ShortcutListener("Esc", closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())) {
            @Override
            public void handleAction(Object sender, Object target) {
                vWindow.close();
            }
        });

        vWindow.addAction(new ShortcutListener("Enter", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                vWindow.close();
            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("cuba-app-message-dialog");
        vWindow.setContent(layout);

        Label messageLab = new CubaLabel();
        messageLab.setValue(message);
        if (MessageType.isHTML(messageType)) {
            messageLab.setContentMode(ContentMode.HTML);
        } else {
            messageLab.setContentMode(ContentMode.TEXT);
        }
        layout.addComponent(messageLab);

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.setSpacing(true);

        DialogAction action = new DialogAction(Type.OK);
        Button button = WebComponentsHelper.createButton();

        button.setCaption(action.getCaption());
        button.setIcon(WebComponentsHelper.getIcon(action.getIcon()));
        button.addStyleName(WebButton.ICON_STYLE);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                vWindow.close();
            }
        });

        buttonsContainer.addComponent(button);
        button.focus();

        layout.addComponent(buttonsContainer);

        messageLab.setWidth("100%");
        layout.setComponentAlignment(buttonsContainer, com.vaadin.ui.Alignment.BOTTOM_RIGHT);

        float width;
        DialogParams dialogParams = getDialogParams();
        if (dialogParams.getWidth() != null) {
            width = dialogParams.getWidth().floatValue();
        } else {
            width = app.getThemeConstants().getInt("cuba.web.WebWindowManager.messageDialog.width");
        }

        vWindow.setWidth(width, Unit.PIXELS);
        vWindow.setResizable(false);

        boolean modal = true;
        if (!hasModalWindow() && dialogParams.getModal() != null) {
            modal = dialogParams.getModal();
        }
        vWindow.setModal(modal);

        dialogParams.reset();

        ui.addWindow(vWindow);
        vWindow.center();
        vWindow.focus();
    }

    @Override
    public void showOptionDialog(String title, String message, MessageType messageType, Action[] actions) {
        final com.vaadin.ui.Window window = new CubaWindow(title);

        if (ui.isTestMode()) {
            window.setCubaId("optionDialog");
            window.setId(ui.getTestIdManager().getTestId("optionDialog"));
        }
        window.setClosable(false);

        Label messageLab = new CubaLabel();
        messageLab.setValue(message);
        if (MessageType.isHTML(messageType)) {
            messageLab.setContentMode(ContentMode.HTML);
        } else {
            messageLab.setContentMode(ContentMode.TEXT);
        }

        float width;
        if (getDialogParams().getWidth() != null) {
            width = getDialogParams().getWidth().floatValue();
        } else {
            width = app.getThemeConstants().getInt("cuba.web.WebWindowManager.optionDialog.width");
        }
        getDialogParams().reset();

        window.setWidth(width, Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);

        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("cuba-app-option-dialog");
        layout.setSpacing(true);
        window.setContent(layout);

        HorizontalLayout actionsBar = new HorizontalLayout();
        actionsBar.setHeight(-1, Unit.PIXELS);

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.setSpacing(true);

        for (final Action action : actions) {
            final Button button = WebComponentsHelper.createButton();
            button.setCaption(action.getCaption());
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    action.actionPerform(null);
                    ui.removeWindow(window);
                }
            });

            if (action instanceof DialogAction) {
                switch (((DialogAction) action).getType()) {
                    case OK:
                    case YES:
                        WebComponentsHelper.setClickShortcut(button, clientConfig.getCommitShortcut());
                        break;
                    case NO:
                    case CANCEL:
                    case CLOSE:
                        WebComponentsHelper.setClickShortcut(button, clientConfig.getCloseShortcut());
                        break;
                }
            }

            if (StringUtils.isNotEmpty(action.getIcon())) {
                button.setIcon(WebComponentsHelper.getIcon(action.getIcon()));
                button.addStyleName(WebButton.ICON_STYLE);
            }

            if (action instanceof AbstractAction && ((AbstractAction)action).isPrimary()) {
                button.addStyleName("primary");
                button.focus();
            }

            if (ui.isTestMode()) {
                button.setCubaId("optionDialog_" + action.getId());
                button.setId(ui.getTestIdManager().getTestId("optionDialog_" + action.getId()));
            }

            buttonsContainer.addComponent(button);
        }
        if (buttonsContainer.getComponentCount() == 1) {
            ((Button) buttonsContainer.getComponent(0)).focus();
        }

        actionsBar.addComponent(buttonsContainer);

        layout.addComponent(messageLab);
        layout.addComponent(actionsBar);

        messageLab.setSizeFull();
        layout.setExpandRatio(messageLab, 1);
        layout.setComponentAlignment(actionsBar, com.vaadin.ui.Alignment.BOTTOM_RIGHT);

        ui.addWindow(window);
        window.center();
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        String target = null;
        Integer width = null;
        Integer height = null;
        String border = "DEFAULT";
        Boolean tryToOpenAsPopup = null;
        if (params != null) {
            target = (String) params.get("target");
            width = (Integer) params.get("width");
            height = (Integer) params.get("height");
            border = (String) params.get("border");
            tryToOpenAsPopup = (Boolean) params.get("tryToOpenAsPopup");
        }
        if (target == null) {
            target = "_blank";
        }
        if (width != null && height != null && border != null) {
            ui.getPage().open(url, target, width, height, BorderStyle.valueOf(border));
        } else if (tryToOpenAsPopup != null) {
            ui.getPage().open(url, target, tryToOpenAsPopup);
        } else {
            ui.getPage().open(url, target, false);
        }
    }

    @Override
    public void initDebugIds(final Frame frame) {
        if (ui.isTestMode()) {
            com.haulmont.cuba.gui.ComponentsHelper.walkComponents(frame, new ComponentVisitor() {
                @Override
                public void visit(com.haulmont.cuba.gui.components.Component component, String name) {
                    if (component.getDebugId() == null) {
                        Frame componentFrame = null;
                        if (component instanceof com.haulmont.cuba.gui.components.Component.BelongToFrame) {
                            componentFrame = ((com.haulmont.cuba.gui.components.Component.BelongToFrame) component).getFrame();
                        }
                        if (componentFrame == null) {
                            log.warn("Frame for component " + component.getClass() + " is not assigned");
                        } else {
                            if (component instanceof WebAbstractComponent) {
                                WebAbstractComponent webComponent = (WebAbstractComponent) component;
                                webComponent.assignAutoDebugId();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void putToWindowMap(Window window, Integer hashCode) {
        if (window != null) {
            windows.put(window, hashCode);
        }
    }

    protected void removeFromWindowMap(Window window) {
        windows.remove(window);
    }

    protected Integer getWindowHashCode(Window window) {
        return windows.get(window);
    }

    @Override
    protected Window getWindow(Integer hashCode) {
        AppWorkArea workArea = appWindow.getMainWindow().getWorkArea();
        if (workArea == null || workArea.getMode() == AppWorkArea.Mode.SINGLE) {
            return null;
        }

        for (Map.Entry<Window, Integer> entry : windows.entrySet()) {
            if (hashCode.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void checkCanOpenWindow(WindowInfo windowInfo, WindowManager.OpenType openType, Map<String, Object> params) {
        if (WindowManager.OpenType.NEW_TAB.equals(openType)) {
            //noinspection StatementWithEmptyBody
            if (!windowInfo.getMultipleOpen() && getWindow(getHash(windowInfo, params)) != null) {
                //window is already open
            } else {
                int maxCount = webConfig.getMaxTabCount();
                if (maxCount > 0 && maxCount <= tabs.size()) {
                    new Notification(
                            messages.formatMessage(AppConfig.getMessagesPack(), "tooManyOpenTabs.message", maxCount),
                            Notification.Type.WARNING_MESSAGE
                    ).show(Page.getCurrent());

                    throw new SilentException();
                }
            }
        }
    }

    protected void initMainWindow(WindowInfo windowInfo) {
        String template = windowInfo.getTemplate();

        final Window mainWindow;
        Map<String, Object> params = Collections.emptyMap();
        if (template != null) {
            //noinspection unchecked
            mainWindow = createWindow(windowInfo, OpenType.NEW_TAB, params, LayoutLoaderConfig.getWindowLoaders());
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null) {
                //noinspection unchecked
                mainWindow = createWindowByScreenClass(windowInfo, params);
            } else {
                throw new DevelopmentException("Unable to load app window");
            }
        }

        // detect work area
        WebWindow windowImpl = (WebWindow) ((Window.Wrapper) mainWindow).getWrappedWindow();

        // bind system UI components to AbstractMainWindow
        ComponentsHelper.walkComponents(windowImpl, new ComponentFinder() {
            @Override
            public boolean visit(com.haulmont.cuba.gui.components.Component component) {
                if (component instanceof AppWorkArea) {
                    ((AbstractMainWindow) mainWindow).setWorkArea((AppWorkArea) component);
                } else if (component instanceof UserIndicator) {
                    ((AbstractMainWindow) mainWindow).setUserIndicator((UserIndicator) component);
                } else if (component instanceof FoldersPane) {
                    ((AbstractMainWindow) mainWindow).setFoldersPane((FoldersPane) component);
                }

                return false;
            }
        });

        // attach main window to UI
        Component windowLayout = windowImpl.getComposition();
        appWindow.addComponent(windowLayout, 0);
        appWindow.setExpandRatio(windowLayout, 1);

        appWindow.setMainWindow((Window.MainWindow) mainWindow);

        // load menu
        ComponentsHelper.walkComponents(windowImpl, new ComponentFinder() {
            @Override
            public boolean visit(com.haulmont.cuba.gui.components.Component component) {
                if (component instanceof AppMenu) {
                    ((AppMenu) component).loadMenu();
                    return true;
                }

                return false;
            }
        });

        AppWorkArea workArea = appWindow.getMainWindow().getWorkArea();
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

        afterShowWindow(mainWindow);
    }

    protected void initTabShortcuts() {
        if (getConfiguredWorkArea().getMode() == AppWorkArea.Mode.TABBED) {
            appWindow.addShortcutListener(createNextWindowTabShortcut());
            appWindow.addShortcutListener(createPreviousWindowTabShortcut());
        }
        appWindow.addShortcutListener(createCloseShortcut());
    }

    protected boolean hasDialogWindows() {
        return !ui.getWindows().isEmpty();
    }

    public ShortcutListener createCloseShortcut() {
        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination combination = KeyCombination.create(closeShortcut);

        return new ShortcutListener("onClose", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())) {
            @Override
            public void handleAction(Object sender, Object target) {
                WebAppWorkArea workArea = getConfiguredWorkArea();
                if (workArea.getState() != AppWorkArea.State.WINDOW_CONTAINER) {
                    return;
                }

                if (AppWorkArea.Mode.TABBED == workArea.getMode()) {
                    final CubaTabSheet tabSheet = workArea.getTabbedWindowContainer();
                    if (tabSheet != null) {
                        VerticalLayout layout = (VerticalLayout) tabSheet.getSelectedTab();
                        if (layout != null) {
                            tabSheet.focus();

                            WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                            if (stacks.get(breadCrumbs).empty()) {
                                final Component previousTab = tabSheet.getPreviousTab(layout);
                                if (previousTab != null) {
                                    breadCrumbs.getCurrentWindow().closeAndRun(Window.CLOSE_ACTION_ID, new Runnable() {
                                        @Override
                                        public void run() {
                                            tabSheet.setSelectedTab(previousTab);
                                        }
                                    });
                                } else {
                                    breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                                }
                            } else {
                                breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                            }
                        }
                    }
                } else {
                    ui.focus();

                    Iterator<WindowBreadCrumbs> it = tabs.values().iterator();
                    if (it.hasNext()) {
                        it.next().getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                    }
                }
            }
        };
    }

    public ShortcutListener createNextWindowTabShortcut() {
        String nextTabShortcut = clientConfig.getNextTabShortcut();
        KeyCombination combination = KeyCombination.create(nextTabShortcut);

        return new ShortcutListener("onNextTab", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())) {
            @Override
            public void handleAction(Object sender, Object target) {
                TabSheet tabSheet = getConfiguredWorkArea().getTabbedWindowContainer();

                if (tabSheet != null && !hasDialogWindows() && tabSheet.getComponentCount() > 1) {
                    Component selectedTabComponent = tabSheet.getSelectedTab();
                    TabSheet.Tab selectedTab = tabSheet.getTab(selectedTabComponent);
                    int tabPosition = tabSheet.getTabPosition(selectedTab);
                    int newTabPosition = (tabPosition + 1) % tabSheet.getComponentCount();

                    TabSheet.Tab newTab = tabSheet.getTab(newTabPosition);
                    tabSheet.setSelectedTab(newTab);

                    moveFocus(tabSheet, newTab);
                }
            }
        };
    }

    public ShortcutListener createPreviousWindowTabShortcut() {
        String previousTabShortcut = clientConfig.getPreviousTabShortcut();
        KeyCombination combination = KeyCombination.create(previousTabShortcut);

        return new ShortcutListener("onPreviousTab", combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())) {
            @Override
            public void handleAction(Object sender, Object target) {
                TabSheet tabSheet = getConfiguredWorkArea().getTabbedWindowContainer();

                if (tabSheet != null && !hasDialogWindows() && tabSheet.getComponentCount() > 1) {
                    Component selectedTabComponent = tabSheet.getSelectedTab();
                    TabSheet.Tab selectedTab = tabSheet.getTab(selectedTabComponent);
                    int tabPosition = tabSheet.getTabPosition(selectedTab);
                    int newTabPosition = (tabSheet.getComponentCount() + tabPosition - 1) % tabSheet.getComponentCount();

                    TabSheet.Tab newTab = tabSheet.getTab(newTabPosition);
                    tabSheet.setSelectedTab(newTab);

                    moveFocus(tabSheet, newTab);
                }
            }
        };
    }

    protected static class DialogWindowActionHandler implements com.vaadin.event.Action.Handler {

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
                Messages messages = AppBeans.get(Messages.NAME);

                saveSettingsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.saveSettings"));
                restoreToDefaultsAction = new com.vaadin.event.Action(messages.getMainMessage("actions.restoreToDefaults"));
                analyzeAction = new com.vaadin.event.Action(messages.getMainMessage("actions.analyzeLayout"));

                initialized = true;
            }

            List<com.vaadin.event.Action> actions = new ArrayList<>(3);

            Configuration configuration = AppBeans.get(Configuration.NAME);
            ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
            if (clientConfig.getManualScreenSettingsSaving()) {
                actions.add(saveSettingsAction);
                actions.add(restoreToDefaultsAction);
            }
            if (clientConfig.getLayoutAnalyzerEnabled()) {
                actions.add(analyzeAction);
            }

            return actions.toArray(new com.vaadin.event.Action[actions.size()]);
        }

        @Override
        public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
            if (initialized) {
                if (saveSettingsAction == action) {
                    window.saveSettings();
                } else if (restoreToDefaultsAction == action) {
                    window.deleteSettings();
                } else if (analyzeAction == action) {
                    LayoutAnalyzer analyzer = new LayoutAnalyzer();
                    List<LayoutTip> tipsList = analyzer.analyze(window);

                    if (tipsList.isEmpty()) {
                        window.showNotification("No layout problems found", NotificationType.HUMANIZED);
                    } else {
                        window.openWindow("layoutAnalyzer", OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                    }
                }
            }
        }
    }
}
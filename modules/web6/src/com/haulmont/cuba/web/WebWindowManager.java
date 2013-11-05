/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.sys.WindowBreadCrumbs;
import com.haulmont.cuba.web.toolkit.ui.ActionsTabSheet;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebWindowManager extends WindowManager {

    protected final Map<Layout, WindowBreadCrumbs> tabs = new HashMap<>();
    protected final Map<WindowBreadCrumbs, Stack<Map.Entry<Window, Integer>>> stacks = new HashMap<>();
    protected final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<>();
    protected final Map<Window, Integer> windows = new HashMap<>();
    protected final Map<Layout, WindowBreadCrumbs> fakeTabs = new HashMap<>();

    private static Log log = LogFactory.getLog(WebWindowManager.class);

    protected App app;
    protected AppWindow appWindow;

    protected Map<String, Integer> debugIds = new HashMap<>();

    protected boolean disableSavingScreenHistory;
    protected ScreenHistorySupport screenHistorySupport;

    protected final WebConfig webConfig;
    protected final ClientConfig clientConfig;

    protected Messages messages;

    public WebWindowManager(final App app, AppWindow appWindow) {
        this.app = app;
        this.appWindow = appWindow;

        Configuration configuration = AppBeans.get(Configuration.class);
        webConfig = configuration.getConfig(WebConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);

        messages = AppBeans.get(Messages.class);

        screenHistorySupport = new ScreenHistorySupport();
    }

    protected Map<Layout, WindowBreadCrumbs> getTabs() {
        return tabs;
    }

    protected Map<Layout, WindowBreadCrumbs> getFakeTabs() {
        return fakeTabs;
    }

    private Map<Window, WindowOpenMode> getWindowOpenMode() {
        return windowOpenMode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<>(getWindowOpenMode().keySet());
    }

    protected static class WindowOpenMode {

        protected Window window;
        protected OpenType openType;
        protected Object data;
        protected com.vaadin.ui.Window vaadinWindow;

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

        public com.vaadin.ui.Window getVaadinWindow() {
            return vaadinWindow;
        }

        public void setVaadinWindow(com.vaadin.ui.Window vaadinWindow) {
            this.vaadinWindow = vaadinWindow;
        }
    }

    protected Layout findTab(Integer hashCode) {
        Set<Map.Entry<Layout, WindowBreadCrumbs>> set = getFakeTabs().entrySet();
        for (Map.Entry<Layout, WindowBreadCrumbs> entry : set) {
            Window currentWindow = entry.getValue().getCurrentWindow();
            if (hashCode.equals(getWindowHashCode(currentWindow))) {
                return entry.getKey();
            }
        }
        set = getTabs().entrySet();
        for (Map.Entry<Layout, WindowBreadCrumbs> entry : set) {
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
        Set<Map.Entry<Window, WindowOpenMode>> windowOpenMode = this.windowOpenMode.entrySet();
        for (Map.Entry<Window, WindowOpenMode> openMode : windowOpenMode) {
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
    protected void showWindow(final Window window, final String caption, final String description, OpenType type, final boolean multipleOpen) {
        AppWindow appWindow = app.getAppWindow();
        boolean forciblyDialog = false;
        if (type != OpenType.DIALOG && hasModalWindow()) {
            type = OpenType.DIALOG;
            forciblyDialog = true;
        }

        if (type == OpenType.THIS_TAB && getTabs().size() == 0) {
            type = OpenType.NEW_TAB;
        }

        final WindowOpenMode openMode = new WindowOpenMode(window, type);
        Component component;

        window.setCaption(caption);
        window.setDescription(description);

        switch (type) {
            case NEW_TAB:
            case NEW_WINDOW:
                appWindow.closeStartupScreen();
                if (AppWindow.Mode.SINGLE.equals(appWindow.getMode())) {

                    VerticalLayout mainLayout = appWindow.getMainLayout();
                    if (mainLayout.getComponentIterator().hasNext()) {
                        Layout oldLayout = (Layout) mainLayout.getComponentIterator().next();
                        WindowBreadCrumbs oldBreadCrumbs = getTabs().get(oldLayout);
                        if (oldBreadCrumbs != null) {
                            Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                            oldWindow.closeAndRun("mainMenu", new Runnable() {
                                @Override
                                public void run() {
                                    showWindow(window, caption, OpenType.NEW_TAB, false);
                                }
                            });
                            return;
                        }
                    }
                } else {
                    final Integer hashCode = getWindowHashCode(window);
                    Layout tab = null;
                    if (hashCode != null && !multipleOpen) {
                        tab = findTab(hashCode);
                    }
                    Layout oldLayout = tab;
                    final WindowBreadCrumbs oldBreadCrumbs = getTabs().get(oldLayout);

                    if (oldBreadCrumbs != null &&
                            windowOpenMode.containsKey(oldBreadCrumbs.getCurrentWindow().<IFrame>getFrame()) &&
                            !multipleOpen) {
                        final Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                        Layout l = new VerticalLayout();
                        appWindow.getTabSheet().replaceComponent(tab, l);
                        fakeTabs.put(l, oldBreadCrumbs);
                        oldWindow.closeAndRun("mainMenu", new Runnable() {
                            @Override
                            public void run() {
                                putToWindowMap(oldWindow, hashCode);
                                oldBreadCrumbs.addWindow(oldWindow);
                                showWindow(window, caption, description, OpenType.NEW_TAB, multipleOpen);
                            }
                        });
                        return;
                    }
                }
                component = showWindowNewTab(window, multipleOpen, caption, description, appWindow);
                break;

            case THIS_TAB:
                appWindow.closeStartupScreen();
                component = showWindowThisTab(window, caption, description, appWindow);
                break;

            case DIALOG:
                component = showWindowDialog(window, caption, description, appWindow, forciblyDialog);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        openMode.setData(component);

        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            getWindowOpenMode().put(wrappedWindow, openMode);
        } else {
            getWindowOpenMode().put(window, openMode);
        }

        afterShowWindow(window);
    }

    protected Component showWindowNewTab(final Window window, final boolean multipleOpen, final String caption,
                                         final String description, AppWindow appWindow) {
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
        final Layout layout = createNewTabLayout(window, multipleOpen, caption, description, appWindow, breadCrumbs);

        return layout;
    }

    protected Layout createNewTabLayout(final Window window, final boolean multipleOpen, final String caption,
                                        final String description, AppWindow appWindow, Component... components) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        if (components != null) {
            for (final Component c : components) {
                layout.addComponent(c);
            }
        }

        final Component component = WebComponentsHelper.getComposition(window);
        component.setSizeFull();
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            layout.setMargin(true);
            TabSheet.Tab newTab;
            Integer hashCode = getWindowHashCode(window);
            Layout tab = null;
            if (hashCode != null) {
                tab = findTab(hashCode);
            }
            if (tab != null && !multipleOpen) {
                tabSheet.replaceComponent(tab, layout);
                tabSheet.removeComponent(tab);
                getTabs().put(layout, (WindowBreadCrumbs) components[0]);
                removeFromWindowMap(getFakeTabs().get(tab).getCurrentWindow());
                getFakeTabs().remove(tab);
                newTab = tabSheet.getTab(layout);
            } else {
                newTab = tabSheet.addTab(layout);
                getTabs().put(layout, (WindowBreadCrumbs) components[0]);
            }
            newTab.setCaption(formatTabCaption(caption, description));
            //newTab.setDescription(formatTabDescription(caption, description));
            if (tabSheet instanceof AppWindow.AppTabSheet) {
                newTab.setClosable(true);
                ((AppWindow.AppTabSheet) tabSheet).setTabCloseHandler(
                        layout,
                        new AppWindow.AppTabSheet.TabCloseHandler() {
                            @Override
                            public void onClose(TabSheet tabSheet, Component tabContent) {
                                WindowBreadCrumbs breadCrumbs = getTabs().get(tabContent);
                                Runnable closeTask = new TabCloseTask(breadCrumbs);
                                closeTask.run();
                            }
                        });
            }
            tabSheet.setSelectedTab(layout);
        } else {
            getTabs().put(layout, (WindowBreadCrumbs) components[0]);
            layout.addStyleName("single");
            layout.addStyleName("cuba-app-work-area-single-window");
            layout.setMargin(true);
            layout.setWidth("99.9%");
            layout.setHeight("99.85%");
            VerticalLayout mainLayout = appWindow.getMainLayout();
            mainLayout.removeAllComponents();
            mainLayout.addComponent(layout);
        }

        return layout;
    }

    public com.vaadin.ui.Window.CloseShortcut createCloseShortcut() {
        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination combination = KeyCombination.create(closeShortcut);

        return new com.vaadin.ui.Window.CloseShortcut(app.getCurrentWindow(), combination.getKey().getCode(),
                KeyCombination.Modifier.codes(combination.getModifiers())) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (AppWindow.Mode.TABBED == appWindow.getMode()) {
                    TabSheet tabSheet = appWindow.getTabSheet();
                    if (tabSheet != null) {
                        VerticalLayout layout = (VerticalLayout) tabSheet.getSelectedTab();
                        if (layout != null) {
                            WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                            if (stacks.get(breadCrumbs).empty()) {
                                ((AppWindow.AppTabSheet) tabSheet).closeTabAndSelectPrevious(layout);
                            } else {
                                breadCrumbs.getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                            }
                        }
                    }
                } else {
                    Iterator<WindowBreadCrumbs> it = tabs.values().iterator();
                    if (it.hasNext()) {
                        it.next().getCurrentWindow().close(Window.CLOSE_ACTION_ID);
                    }
                }
            }
        };
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

    public void setCurrentWindowCaption(Window window, String caption, String description) {
        TabSheet tabSheet = app.getAppWindow().getTabSheet();
        if (tabSheet == null) {
            return; // for SINGLE tabbing mode
        }

        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }
        WindowOpenMode openMode = getWindowOpenMode().get(window);
        if (openMode == null || OpenType.DIALOG.equals(openMode.getOpenType())) {
            return;
        }

        com.vaadin.ui.Component tabContent = tabSheet.getSelectedTab();
        if (tabContent == null) {
            return;
        }

        TabSheet.Tab tab = tabSheet.getTab(tabContent);
        if (tab == null) {
            return;
        }

        tab.setCaption(formatTabCaption(caption, description));
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

    protected Component showWindowThisTab(final Window window, final String caption, final String description, AppWindow appWindow) {
        VerticalLayout layout;

        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            layout = (VerticalLayout) tabSheet.getSelectedTab();
        } else {
            layout = (VerticalLayout) appWindow.getMainLayout().getComponentIterator().next();
        }

        final WindowBreadCrumbs breadCrumbs = getTabs().get(layout);
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

        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            TabSheet.Tab tab = tabSheet.getTab(layout);
            tab.setCaption(formatTabCaption(caption, description));
        } else {
            appWindow.getMainLayout().requestRepaintAll();
        }

        return layout;
    }

    protected Component showWindowDialog(final Window window, final String caption, final String description,
                                         AppWindow appWindow, boolean forciblyDialog) {
        removeWindowsWithName(window.getId());

        final com.vaadin.ui.Window win = createDialogWindow(window);
        win.setName(window.getId());
        setDebugId(win, window.getId());

        Layout layout = (Layout) WebComponentsHelper.getComposition(window);

        // surrond window layout with outer layout to prevent double painting
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.addComponent(layout);
        outerLayout.setExpandRatio(layout, 1);

        win.setContent(outerLayout);

        win.addListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                window.close(Window.CLOSE_ACTION_ID, true);
            }
        });

        com.vaadin.event.ShortcutAction exitAction =
                new com.vaadin.event.ShortcutAction(
                        "escapeAction",
                        com.vaadin.event.ShortcutAction.KeyCode.ESCAPE,
                        null);
        Map<com.vaadin.event.Action, Runnable> actions = new HashMap<>();
        actions.put(exitAction, new Runnable() {
            @Override
            public void run() {
                window.close(Window.CLOSE_ACTION_ID, true);
            }
        });

        WebComponentsHelper.setActions(win, actions);

        final DialogParams dialogParams = getDialogParams();
        boolean dialogParamsIsNull = dialogParams.getHeight() == null && dialogParams.getWidth() == null &&
                dialogParams.getResizable() == null;

        if (forciblyDialog && dialogParamsIsNull) {
            outerLayout.setHeight(100, Sizeable.UNITS_PERCENTAGE);
            win.setWidth(800, Sizeable.UNITS_PIXELS);
            win.setHeight(500, Sizeable.UNITS_PIXELS);
            win.setResizable(true);
            window.setHeight("100%");
        } else {
            if (dialogParams.getWidth() != null) {
                win.setWidth(dialogParams.getWidth().floatValue(), Sizeable.UNITS_PIXELS);
            } else {
                win.setWidth(600, Sizeable.UNITS_PIXELS);
            }

            if (dialogParams.getHeight() != null) {
                win.setHeight(dialogParams.getHeight().floatValue(), Sizeable.UNITS_PIXELS);
                win.getContent().setHeight("100%");
            }

            if (dialogParams.getCloseable() != null) {
                win.setClosable(dialogParams.getCloseable());
            }

            win.setResizable(BooleanUtils.isTrue(dialogParams.getResizable()));

            dialogParams.reset();
        }
        win.setModal(true);

        App.getInstance().getAppWindow().addWindow(win);
        win.center();

        return win;
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs() {
        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs();
        stacks.put(windowBreadCrumbs, new Stack<Map.Entry<Window, Integer>>());
        return windowBreadCrumbs;
    }

    protected com.vaadin.ui.Window createDialogWindow(Window window) {
        return new com.vaadin.ui.Window(window.getCaption());
    }

    @Override
    public void close(Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        final WindowOpenMode openMode = getWindowOpenMode().get(window);
        if (openMode == null) {
            log.warn("Problem closing window " + window + " : WindowOpenMode not found");
            return;
        }
        disableSavingScreenHistory = false;
        closeWindow(window, openMode);
        getWindowOpenMode().remove(window);
        removeFromWindowMap(openMode.getWindow());
    }

    public void checkModificationsAndCloseAll(final Runnable runIfOk, final @Nullable Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            if (!disableSavingScreenHistory) {
                screenHistorySupport.saveScreenHistory(window, getWindowOpenMode().get(window).getOpenType());
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
                    messages.getMessage(WebWindow.class, "closeUnsaved"),
                    IFrame.MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(messages.getMessage(WebWindow.class, "actions.Yes")) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfOk != null) {
                                        runIfOk.run();
                                    }
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/ok.png";
                                }
                            },
                            new AbstractAction(messages.getMessage(WebWindow.class, "actions.No")) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfCancel != null) {
                                        runIfCancel.run();
                                    }
                                }

                                @Override
                                public String getIcon() {
                                    return "icons/cancel.png";
                                }
                            }
                    }
            );
        } else {
            runIfOk.run();
        }
    }

    public void closeAll() {
        List<Map.Entry<Window, WindowOpenMode>> entries = new ArrayList<>(getWindowOpenMode().entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            Window window = entries.get(i).getKey();
            if (window instanceof WebWindow.Editor) {
                ((WebWindow.Editor) window).releaseLock();
            }
            closeWindow(window, entries.get(i).getValue());
        }
        disableSavingScreenHistory = false;
        getWindowOpenMode().clear();
        windows.clear();
    }

    public static void removeCloseListeners(com.vaadin.ui.Window win) {
        Collection listeners = win.getListeners(com.vaadin.ui.Window.CloseEvent.class);
        for (Object listener : listeners) {
            win.removeListener((com.vaadin.ui.Window.CloseListener) listener);
        }
    }

    private void closeWindow(Window window, WindowOpenMode openMode) {
        AppWindow appWindow = app.getAppWindow();

        if (!disableSavingScreenHistory) {
            screenHistorySupport.saveScreenHistory(window, openMode.getOpenType());
        }

        switch (openMode.openType) {
            case DIALOG: {
                final com.vaadin.ui.Window win = (com.vaadin.ui.Window) openMode.getData();
                App.getInstance().getAppWindow().removeWindow(win);
                fireListeners(window, getTabs().size() != 0);
                break;
            }
            case NEW_TAB: {
                final Layout layout = (Layout) openMode.getData();
                layout.removeComponent(WebComponentsHelper.getComposition(window));

                ActionsTabSheet webTabsheet = (ActionsTabSheet) appWindow.getTabSheet();

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    webTabsheet.silentCloseTabAndSelectPrevious(layout);
                    webTabsheet.removeComponent(layout);
                } else {
                    appWindow.getMainLayout().removeComponent(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = getTabs().get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                getTabs().remove(layout);
                stacks.remove(windowBreadCrumbs);
                fireListeners(window, getTabs().size() != 0);
                if (tabs.isEmpty()) {
                    appWindow.showStartupScreen();
                }
                break;
            }
            case THIS_TAB: {
                final VerticalLayout layout = (VerticalLayout) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = getTabs().get(layout);
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

                layout.removeComponent(WebComponentsHelper.getComposition(window));
                layout.addComponent(component);
                layout.setExpandRatio(component, 1);

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    TabSheet tabSheet = app.getAppWindow().getTabSheet();
                    TabSheet.Tab tab = tabSheet.getTab(layout);
                    tab.setCaption(formatTabCaption(currentWindow.getCaption(), currentWindow.getDescription()));
                }
                fireListeners(window, getTabs().size() != 0);
                if (tabs.isEmpty()) {
                    appWindow.showStartupScreen();
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public void showFrame(com.haulmont.cuba.gui.components.Component parent, IFrame frame) {
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
    public void showNotification(String caption, IFrame.NotificationType type) {
        app.getAppWindow().showNotification(caption, WebComponentsHelper.convertNotificationType(type));
    }

    @Override
    public void showNotification(String caption, String description, IFrame.NotificationType type) {
        com.vaadin.ui.Window.Notification notify =
                new com.vaadin.ui.Window.Notification(caption, description, WebComponentsHelper.convertNotificationType(type));
        if (type.equals(IFrame.NotificationType.HUMANIZED)) {
            notify.setDelayMsec(3000);
        }
        app.getAppWindow().showNotification(notify);
    }

    @Override
    public void showMessageDialog(
            String title,
            String message,
            IFrame.MessageType messageType
    ) {
        removeWindowsWithName("cuba-message-dialog");

        final com.vaadin.ui.Window window = new com.vaadin.ui.Window(title);
        window.setName("cuba-message-dialog");
        setDebugId(window, "cuba-message-dialog");

        window.addListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                App.getInstance().getAppWindow().removeWindow(window);
            }
        });

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        window.setContent(layout);

        Label desc = new Label(message, Label.CONTENT_XHTML);
        layout.addComponent(desc);

        float width;
        if (getDialogParams().getWidth() != null) {
            width = getDialogParams().getWidth().floatValue();
        } else {
            width = 400;
        }
        getDialogParams().reset();

        window.setWidth(width, Sizeable.UNITS_PIXELS);
        window.setResizable(false);
        window.setModal(true);

        App.getInstance().getAppWindow().addWindow(window);
        window.center();
    }

    @Override
    public void showOptionDialog(
            String title,
            String message,
            IFrame.MessageType messageType,
            Action[] actions
    ) {
        removeWindowsWithName("cuba-option-dialog");

        final com.vaadin.ui.Window window = new com.vaadin.ui.Window(title);
        window.setName("cuba-option-dialog");
        setDebugId(window, "cuba-option-dialog");
        window.setClosable(false);

        window.addListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                app.getAppWindow().removeWindow(window);
            }
        });

        Label messageBox = new Label(message, Label.CONTENT_XHTML);

        float width;
        if (getDialogParams().getWidth() != null) {
            width = getDialogParams().getWidth().floatValue();
        } else {
            width = 400;
        }
        getDialogParams().reset();

        window.setWidth(width, Sizeable.UNITS_PIXELS);
        window.setResizable(false);
        window.setModal(true);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        window.setContent(layout);

        HorizontalLayout actionsBar = new HorizontalLayout();
        actionsBar.setHeight(-1, Sizeable.UNITS_PIXELS);

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.setSpacing(true);

        for (final Action action : actions) {
            final Button button = WebComponentsHelper.createButton();
            button.setCaption(action.getCaption());
            button.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    action.actionPerform(null);
                    AppWindow appWindow = app.getAppWindow();
                    if (appWindow != null) // possible appWindow is null after logout
                    {
                        appWindow.removeWindow(window);
                    }
                }
            });

            if (action instanceof DialogAction) {
                switch (((DialogAction) action).getType()) {
                    case OK:
                    case YES:
                        button.setClickShortcut(KeyCombination.Key.ENTER.getCode(), KeyCombination.Modifier.CTRL.getCode());
                        break;
                    case NO:
                    case CANCEL:
                    case CLOSE:
                        button.setClickShortcut(KeyCombination.Key.ESCAPE.getCode());
                        break;
                }
            }

            if (action.getIcon() != null) {
                button.setIcon(new ThemeResource(action.getIcon()));
                button.addStyleName(WebButton.ICON_STYLE);
            }
            setDebugId(button, action.getId());
            buttonsContainer.addComponent(button);
        }
        if (buttonsContainer.getComponentCount() > 0) {
            ((Button) buttonsContainer.getComponent(0)).focus();
        }

        actionsBar.addComponent(buttonsContainer);

        layout.addComponent(messageBox);
        layout.addComponent(actionsBar);

        messageBox.setSizeFull();
        layout.setExpandRatio(messageBox, 1);
        layout.setComponentAlignment(actionsBar, com.vaadin.ui.Alignment.BOTTOM_RIGHT);

        App.getInstance().getAppWindow().addWindow(window);
        window.center();
    }

    @Override
    public void showWebPage(String url, @Nullable Map<String, Object> params) {
        //todo artamonov rewrite generated body
    }

    private void removeWindowsWithName(String name) {
        final com.vaadin.ui.Window mainWindow = app.getAppWindow();

        for (com.vaadin.ui.Window childWindow : new ArrayList<>(mainWindow.getChildWindows())) {
            if (name.equals(childWindow.getName())) {
                String msg = new StrBuilder("Another " + name + " window exists, removing it\n")
                        //.appendWithSeparators(Thread.currentThread().getStackTrace(), "\n")
                        .toString();
                log.warn(msg);
                mainWindow.removeWindow(childWindow);
                Set<Map.Entry<Window, WindowOpenMode>> openModeSet = getWindowOpenMode().entrySet();
                for (Map.Entry<Window, WindowOpenMode> entry : openModeSet) {
                    WindowOpenMode openMode = entry.getValue();
                    if (ObjectUtils.equals(openMode.data, childWindow)) {
                        getWindowOpenMode().remove(entry.getKey());
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void initDebugIds(final Window window) {
        if (app.isTestModeRequest()) {
            com.haulmont.cuba.gui.ComponentsHelper.walkComponents(window, new ComponentVisitor() {
                @Override
                public void visit(com.haulmont.cuba.gui.components.Component component, String name) {
                    final String id = window.getId() + "." + name;
                    if (webConfig.getAllowIdSuffix()) {
                        component.setDebugId(generateDebugId(id));
                    } else {
                        if (component.getId() != null) {
                            component.setDebugId(id);
                        } else {
                            component.setDebugId(generateDebugId(id));
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

    private Integer getWindowHashCode(Window window) {
        return windows.get(window);
    }

    @Override
    protected Window getWindow(Integer hashCode) {
        if (AppWindow.Mode.SINGLE.equals(app.getAppWindow().getMode())) {
            return null;
        }
        Set<Map.Entry<Window, Integer>> set = windows.entrySet();
        for (Map.Entry<Window, Integer> entry : set) {
            if (hashCode.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    protected void checkCanOpenWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
        if (OpenType.NEW_TAB.equals(openType)) {
            if (!windowInfo.getMultipleOpen() && getWindow(getHash(windowInfo, params)) != null) {
                //window already opened
            } else {
                int maxCount = webConfig.getMaxTabCount();
                if (maxCount > 0 && maxCount <= tabs.size()) {
                    app.getAppWindow().showNotification(
                            messages.formatMessage(AppConfig.getMessagesPack(), "tooManyOpenTabs.message", maxCount),
                            com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE);
                    throw new SilentException();
                }
            }
        }
    }

    public void setDebugId(Component component, String id) {
        if (app.isTestModeRequest()) {
            if (webConfig.getAllowIdSuffix()) {
                component.setDebugId(generateDebugId(id));
            } else {
                component.setDebugId(id);
            }
        }
    }

    protected String generateDebugId(String id) {
        Integer count = debugIds.get(id);
        if (count == null) {
            count = 0;
        }
        debugIds.put(id, ++count);
        return id + "." + count;
    }
}
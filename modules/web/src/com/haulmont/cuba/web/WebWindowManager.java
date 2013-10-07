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
import com.haulmont.cuba.web.toolkit.VersionedThemeResource;
import com.haulmont.cuba.web.toolkit.ui.CubaTabSheet;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebWindowManager extends WindowManager {

    public static final int HUMANIZED_NOTIFICATION_DELAY_MSEC = 3000;

    private static Log log = LogFactory.getLog(WebWindowManager.class);

    protected App app;
    protected AppWindow appWindow;

    protected final WebConfig webConfig;
    protected final ClientConfig clientConfig;

    protected final Map<ComponentContainer, WindowBreadCrumbs> tabs = new HashMap<>();
    protected final Map<WindowBreadCrumbs, Stack<Map.Entry<Window, Integer>>> stacks = new HashMap<>();
    protected final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<>();
    protected final Map<Window, Integer> windows = new HashMap<>();
    protected final Map<ComponentContainer, WindowBreadCrumbs> fakeTabs = new HashMap<>();

    protected Map<String, Integer> debugIds = new HashMap<>();

    protected boolean disableSavingScreenHistory;
    protected ScreenHistorySupport screenHistorySupport;

    public WebWindowManager(final App app, AppWindow appWindow) {
        this.app = app;
        this.appWindow = appWindow;

        Configuration configuration = AppBeans.get(Configuration.class);
        webConfig = configuration.getConfig(WebConfig.class);
        clientConfig = configuration.getConfig(ClientConfig.class);

        messages = AppBeans.get(Messages.class);

        screenHistorySupport = new ScreenHistorySupport();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<>(windowOpenMode.keySet());
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

    protected ComponentContainer findTab(Integer hashCode) {
        Set<Map.Entry<ComponentContainer, WindowBreadCrumbs>> set = fakeTabs.entrySet();
        for (Map.Entry<ComponentContainer, WindowBreadCrumbs> entry : set) {
            Window currentWindow = entry.getValue().getCurrentWindow();
            if (hashCode.equals(getWindowHashCode(currentWindow)))
                return entry.getKey();
        }
        set = tabs.entrySet();
        for (Map.Entry<ComponentContainer, WindowBreadCrumbs> entry : set) {
            Window currentWindow = entry.getValue().getCurrentWindow();
            if (hashCode.equals(getWindowHashCode(currentWindow)))
                return entry.getKey();
        }
        return null;
    }

    protected Stack<Map.Entry<Window, Integer>> getStack(WindowBreadCrumbs breadCrumbs) {
        return stacks.get(breadCrumbs);
    }

    protected boolean hasModalWindow() {
        for (Map.Entry<Window, WindowOpenMode> openMode : windowOpenMode.entrySet()) {
            if (OpenType.DIALOG.equals(openMode.getValue().getOpenType()))
                return true;
        }
        return false;
    }

    @Override
    protected void showWindow(final Window window, final String caption, OpenType type, boolean multipleOpen) {
        showWindow(window, caption, null, type, multipleOpen);
    }

    @Override
    protected void showWindow(final Window window, final String caption, final String description, OpenType type, final boolean multipleOpen) {
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
                appWindow.closeStartupScreen();
                if (AppWindow.Mode.SINGLE.equals(appWindow.getMode())) {

                    VerticalLayout mainLayout = appWindow.getMainLayout();
                    if (mainLayout.iterator().hasNext()) {
                        ComponentContainer oldLayout = (ComponentContainer) mainLayout.iterator().next();
                        WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);
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
                    ComponentContainer tab = null;
                    if (hashCode != null && !multipleOpen)
                        tab = findTab(hashCode);
                    ComponentContainer oldLayout = tab;
                    final WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);

                    if (oldBreadCrumbs != null &&
                            windowOpenMode.containsKey(oldBreadCrumbs.getCurrentWindow().<Window>getFrame()) &&
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
                component = showWindowNewTab(window, multipleOpen, caption, description);
                break;

            case THIS_TAB:
                appWindow.closeStartupScreen();
                component = showWindowThisTab(window, caption, description);
                break;

            case DIALOG:
                component = showWindowDialog(window, caption, description, forciblyDialog);
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

    public ShortcutListener createCloseShortcut() {
        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination combination = KeyCombination.create(closeShortcut);

        return new ShortcutListener("onClose", combination.getKey().getCode(),
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

    protected ComponentContainer createNewWinLayout(Window window, Component... components) {
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

        return layout;
    }

    protected Component showWindowNewTab(final Window window, final boolean multipleOpen, final String caption,
                                         final String description) {
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

        final Layout layout = createNewTabLayout(window, multipleOpen, caption, description, breadCrumbs);

        return layout;
    }

    protected Layout createNewTabLayout(final Window window, final boolean multipleOpen, final String caption,
                                        final String description, Component... components) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("cuba-app-tabbed-window");
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
            ComponentContainer tab = null;
            if (hashCode != null)
                tab = findTab(hashCode);
            if (tab != null && !multipleOpen) {
                tabSheet.replaceComponent(tab, layout);
                tabSheet.removeComponent(tab);
                tabs.put(layout, (WindowBreadCrumbs) components[0]);
                removeFromWindowMap(fakeTabs.get(tab).getCurrentWindow());
                fakeTabs.remove(tab);
                newTab = tabSheet.getTab(layout);
            } else {
                newTab = tabSheet.addTab(layout);
                tabs.put(layout, (WindowBreadCrumbs) components[0]);
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
                                WindowBreadCrumbs breadCrumbs = tabs.get(tabContent);
                                Runnable closeTask = new TabCloseTask(breadCrumbs);
                                closeTask.run();
                            }
                        });
            }
            tabSheet.setSelectedTab(layout);
        } else {
			tabs.put(layout, (WindowBreadCrumbs) components[0]);
            layout.addStyleName("cuba-app-work-area-single-window");
            layout.setMargin(true);

            VerticalLayout mainLayout = appWindow.getMainLayout();
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

    public void setCurrentWindowCaption(Window window, String caption, String description) {
        TabSheet tabSheet = appWindow.getTabSheet();
        if (tabSheet == null)
            return; // for SINGLE tabbing mode

        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }
        WindowOpenMode openMode = windowOpenMode.get(window);
        if (openMode == null || OpenType.DIALOG.equals(openMode.getOpenType()))
            return;

        com.vaadin.ui.Component tabContent = tabSheet.getSelectedTab();
        if (tabContent == null)
            return;

        TabSheet.Tab tab = tabSheet.getTab(tabContent);
        if (tab == null)
            return;

        String formattedCaption = formatTabCaption(caption, description);
        tab.setCaption(formattedCaption);
        window.setCaption(formattedCaption);
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
        VerticalLayout layout;

        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            layout = (VerticalLayout) tabSheet.getSelectedTab();
        } else {
            layout = (VerticalLayout) appWindow.getMainLayout().getComponentIterator().next();
        }

        final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
        if (breadCrumbs == null)
            throw new IllegalStateException("BreadCrumbs not found");

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
        } else
            appWindow.getMainLayout().markAsDirtyRecursive();

        return layout;
    }

    protected Component showWindowDialog(final Window window, final String caption, final String description,
                                         boolean forciblyDialog) {

        final com.vaadin.ui.Window win = createDialogWindow(window);
        win.setId(window.getId());
        setDebugId(win, window.getId());

        Layout layout = (Layout) WebComponentsHelper.getComposition(window);

        // surrond window layout with outer layout to prevent double painting
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setStyleName("cuba-app-dialog-window");
        outerLayout.addComponent(layout);
        outerLayout.setExpandRatio(layout, 1);

        win.setContent(outerLayout);

        win.addCloseListener(new com.vaadin.ui.Window.CloseListener() {
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
            outerLayout.setHeight(100, Sizeable.Unit.PERCENTAGE);
            win.setWidth(800, Sizeable.Unit.PIXELS);
            win.setHeight(500, Sizeable.Unit.PIXELS);
            win.setResizable(true);
            window.setHeight("100%");
        } else {
            if (dialogParams.getWidth() != null)
                win.setWidth(dialogParams.getWidth().floatValue(), Sizeable.Unit.PIXELS);
            else
                win.setWidth(600, Sizeable.Unit.PIXELS);

            if (dialogParams.getHeight() != null) {
                win.setHeight(dialogParams.getHeight().floatValue(), Sizeable.Unit.PIXELS);
                win.getContent().setHeight("100%");
            }

            if (dialogParams.getCloseable() != null) {
                win.setClosable(dialogParams.getCloseable());
            }

            win.setResizable(BooleanUtils.isTrue(dialogParams.getResizable()));

            dialogParams.reset();
        }
        win.setModal(true);

        appWindow.getAppUI().addWindow(win);
        win.center();

        return win;
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs() {
        WindowBreadCrumbs windowBreadCrumbs = new WindowBreadCrumbs();
        stacks.put(windowBreadCrumbs, new Stack<Map.Entry<Window, Integer>>());
        return windowBreadCrumbs;
    }

    protected com.vaadin.ui.Window createDialogWindow(Window window) {
        com.vaadin.ui.Window vWindow = new com.vaadin.ui.Window(window.getCaption());
        vWindow.setErrorHandler(app.getAppUI());
        return vWindow;
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

    public void checkModificationsAndCloseAll(final Runnable runIfOk, final @Nullable Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            if (!disableSavingScreenHistory) {
                screenHistorySupport.saveScreenHistory(window, windowOpenMode.get(window).getOpenType());
            }

            if (window instanceof WrappedWindow && ((WrappedWindow) window).getWrapper() != null)
                ((WrappedWindow) window).getWrapper().saveSettings();
            else
                window.saveSettings();

            if (window.getDsContext() != null && window.getDsContext().isModified()) {
                modified = true;
            }
        }
        disableSavingScreenHistory = true;
        if (modified) {
            showOptionDialog(
                    messages.getMessage(WebWindow.class, "closeUnsaved.caption"),
                    messages.getMessage(WebWindow.class, "discardChangesOnClose"),
                    IFrame.MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(messages.getMessage(WebWindow.class, "closeApplication")) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfOk != null)
                                        runIfOk.run();
                                }
                                @Override
                                public String getIcon() {
                                    return "icons/ok.png";
                                }
                            },
                            new AbstractAction(messages.getMessage(WebWindow.class, "actions.Cancel")) {
                                @Override
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfCancel != null)
                                        runIfCancel.run();
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
        List<Map.Entry<Window, WindowOpenMode>> entries = new ArrayList<>(windowOpenMode.entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            WebWindow window = (WebWindow) entries.get(i).getKey();
            window.stopTimers();

            if (window instanceof WebWindow.Editor) {
                ((WebWindow.Editor)window).releaseLock();
            }
            closeWindow(window, entries.get(i).getValue());
        }
        disableSavingScreenHistory = false;
        windowOpenMode.clear();
        windows.clear();
    }

    public static void removeCloseListeners(com.vaadin.ui.Window win) {
        Collection listeners = win.getListeners(com.vaadin.ui.Window.CloseEvent.class);
        for (Object listener : listeners) {
            win.removeCloseListener((com.vaadin.ui.Window.CloseListener) listener);
        }
    }

    protected void closeWindow(Window window, WindowOpenMode openMode) {
        if (!disableSavingScreenHistory) {
            screenHistorySupport.saveScreenHistory(window, openMode.getOpenType());
        }

        WebWindow webWindow = (WebWindow) window;
        webWindow.stopTimers();

        switch (openMode.openType) {
            case DIALOG: {
                final com.vaadin.ui.Window win = (com.vaadin.ui.Window) openMode.getData();
                removeCloseListeners(win);
                appWindow.getAppUI().removeWindow(win);
                fireListeners(window, tabs.size() != 0);
                break;
            }
            case NEW_TAB: {
                final Layout layout = (Layout) openMode.getData();
                layout.removeComponent(WebComponentsHelper.getComposition(window));

                CubaTabSheet webTabsheet = (CubaTabSheet) appWindow.getTabSheet();

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    webTabsheet.silentCloseTabAndSelectPrevious(layout);
                    webTabsheet.removeComponent(layout);
                } else {
                    appWindow.getMainLayout().removeComponent(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = tabs.get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                tabs.remove(layout);
                stacks.remove(windowBreadCrumbs);
                fireListeners(window, !tabs.isEmpty());
                if (tabs.isEmpty()) {
                    appWindow.showStartupScreen();
                }
                break;
            }
            case THIS_TAB: {
                final VerticalLayout layout = (VerticalLayout) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                if (breadCrumbs == null)
                    throw new IllegalStateException("Unable to close screen: breadCrumbs not found");

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
                    TabSheet tabSheet = appWindow.getTabSheet();
                    TabSheet.Tab tab = tabSheet.getTab(layout);
                    tab.setCaption(formatTabCaption(currentWindow.getCaption(), currentWindow.getDescription()));
                }
                fireListeners(window, !tabs.isEmpty());
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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showNotification(String caption, IFrame.NotificationType type) {
        Notification notification = new Notification(caption, WebComponentsHelper.convertNotificationType(type));
        if (type.equals(IFrame.NotificationType.HUMANIZED))
            notification.setDelayMsec(HUMANIZED_NOTIFICATION_DELAY_MSEC);
        notification.show(Page.getCurrent());
    }

    @Override
    public void showNotification(String caption, String description, IFrame.NotificationType type) {
        Notification notification = new Notification(caption, description, WebComponentsHelper.convertNotificationType(type));
        if (type.equals(IFrame.NotificationType.HUMANIZED))
            notification.setDelayMsec(HUMANIZED_NOTIFICATION_DELAY_MSEC);
        notification.show(Page.getCurrent());
    }

    @Override
    public void showMessageDialog(
            String title,
            String message,
            IFrame.MessageType messageType) {

        final com.vaadin.ui.Window window = new com.vaadin.ui.Window(title);
        window.setId("cuba-message-dialog");
        setDebugId(window, "cuba-message-dialog");

        window.addCloseListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                appWindow.getAppUI().removeWindow(window);
            }
        });

        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("cuba-app-message-dialog");
        layout.setMargin(true);
        window.setContent(layout);

        Label desc = new Label(message);
        desc.setContentMode(ContentMode.HTML);
        layout.addComponent(desc);

        float width;
        if (getDialogParams().getWidth() != null) {
            width = getDialogParams().getWidth().floatValue();
        } else {
            width = 400;
        }
        getDialogParams().reset();

        window.setWidth(width, Sizeable.Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);

        appWindow.getAppUI().addWindow(window);
        window.center();
    }

    @Override
    public void showOptionDialog(
            String title,
            String message,
            IFrame.MessageType messageType,
            Action[] actions) {

        final com.vaadin.ui.Window window = new com.vaadin.ui.Window(title);
        window.setId("cuba-option-dialog");
        setDebugId(window, "cuba-option-dialog");
        window.setClosable(false);

        window.addCloseListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                app.getAppUI().removeWindow(window);
            }
        });

        Label messageBox = new Label(message, ContentMode.HTML);

        float width;
        if (getDialogParams().getWidth() != null) {
            width = getDialogParams().getWidth().floatValue();
        } else {
            width = 400;
        }
        getDialogParams().reset();

        window.setWidth(width, Sizeable.Unit.PIXELS);
        window.setResizable(false);
        window.setModal(true);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        window.setContent(layout);

        HorizontalLayout actionsBar = new HorizontalLayout();
        actionsBar.setHeight(-1, Sizeable.Unit.PIXELS);

        HorizontalLayout buttonsContainer = new HorizontalLayout();
        buttonsContainer.setSpacing(true);

        for (final Action action : actions) {
            final Button button = WebComponentsHelper.createButton();
            button.setCaption(action.getCaption());
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    action.actionPerform(null);
                    app.getAppUI().removeWindow(window);
                }
            });

            if (action instanceof DialogAction) {
                switch (((DialogAction) action).getType()) {
                    case OK:
                    case YES:
                        button.setClickShortcut(KeyCombination.Key.ENTER.getCode(),
                                KeyCombination.Modifier.CTRL.getCode());
                        break;
                    case NO:
                    case CANCEL:
                    case CLOSE:
                        button.setClickShortcut(KeyCombination.Key.ESCAPE.getCode());
                        break;
                }
            }

            if (action.getIcon() != null) {
                button.setIcon(new VersionedThemeResource(action.getIcon()));
                button.addStyleName(WebButton.ICON_STYLE);
            }
            setDebugId(button, action.getId());
            buttonsContainer.addComponent(button);
        }
        if (buttonsContainer.getComponentCount() > 0)
            ((Button) buttonsContainer.getComponent(0)).focus();

        actionsBar.addComponent(buttonsContainer);

        layout.addComponent(messageBox);
        layout.addComponent(actionsBar);

        messageBox.setSizeFull();
        layout.setExpandRatio(messageBox, 1);
        layout.setComponentAlignment(actionsBar, com.vaadin.ui.Alignment.BOTTOM_RIGHT);

        appWindow.getAppUI().addWindow(window);
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
        if (target == null)
            target = "_blank";
        if (width != null && height != null && border != null) {
            appWindow.getAppUI().getPage().open(url, target, width, height, BorderStyle.valueOf(border));
        } else if (tryToOpenAsPopup != null) {
            appWindow.getAppUI().getPage().open(url, target, tryToOpenAsPopup);
        } else {
            appWindow.getAppUI().getPage().open(url, target, false);
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

    protected Integer getWindowHashCode(Window window){
       return windows.get(window);
    }

    @Override
    protected Window getWindow(Integer hashCode) {
        if (AppWindow.Mode.SINGLE.equals(app.getAppWindow().getMode()))
            return null;
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

    public void setDebugId(Component component, String id) {
        if (app.isTestModeRequest()) {
            if (webConfig.getAllowIdSuffix()) {
                component.setId(generateDebugId(id));
            } else {
                component.setId(id);
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
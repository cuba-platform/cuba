/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.12.2008 17:33:12
 *
 * $Id$
 */
package com.haulmont.cuba.web;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.ScreenHistoryEntity;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.ui.WindowBreadCrumbs;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;

public class WebWindowManager extends WindowManager {

    private static final long serialVersionUID = -1212999019760516097L;

    protected static class WindowData implements Serializable {
        private static final long serialVersionUID = -3919777239558187362L;

        protected final Map<Layout, WindowBreadCrumbs> tabs = new HashMap<Layout, WindowBreadCrumbs>();
        protected final Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<Window, WindowOpenMode>();
    }

    protected App app;

    protected List<WindowCloseListener> listeners = new ArrayList<WindowCloseListener>();

    protected List<ShowStartupLayoutListener> showStartupLayoutListeners = new ArrayList<ShowStartupLayoutListener>();

    protected List<CloseStartupLayoutListener> closeStartupLayoutListeners = new ArrayList<CloseStartupLayoutListener>();

    private Map<AppWindow, WindowData> appWindowMap = new HashMap<AppWindow, WindowData>();

    protected Map<String, Integer> debugIds = new HashMap<String, Integer>();

    private static Log log = LogFactory.getLog(WebWindowManager.class);

    private String baseModalWindowCaption = "";

    private Stack<ModalContentWithCaption> modalWindowStack = new Stack<ModalContentWithCaption>();

    private static class ModalContentWithCaption implements Serializable{
        String caption;
        ComponentContainer componentContainer;
        private static final long serialVersionUID = -1389415942614781757L;

        private ModalContentWithCaption(String caption, ComponentContainer componentContainer) {
            this.caption = caption;
            this.componentContainer = componentContainer;
        }
    }

    public WebWindowManager(final App app) {
        this.app = app;
        app.getConnection().addListener(new UserSubstitutionListener() {
            public void userSubstituted(Connection connection) {
                closeStartupScreen(app.getAppWindow());
                showStartupScreen(app.getAppWindow());
            }
        });
    }

    private WindowData getCurrentWindowData() {
        WindowData data = appWindowMap.get(app.getAppWindow());
        if (data == null) {
            data = new WindowData();
            appWindowMap.put(app.getAppWindow(), data);
        }
        return data;
    }

    protected Map<Layout, WindowBreadCrumbs> getTabs() {
        return getCurrentWindowData().tabs;
    }

    private Map<Window, WindowOpenMode> getWindowOpenMode() {
        return getCurrentWindowData().windowOpenMode;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected DataService createDefaultDataService() {
        return new GenericDataService();
    }

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList<Window>(getWindowOpenMode().keySet());
    }

    protected static class WindowOpenMode implements Serializable {

        private static final long serialVersionUID = 2475930997468013484L;

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

    public void addWindowCloseListener(WindowCloseListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeWindowCloseListener(WindowCloseListener listener) {
        listeners.remove(listener);
    }

    protected void fireListeners(Window window, boolean anyOpenWindowExist) {
        for (WindowCloseListener wcl : listeners) {
            wcl.onWindowClose(window, anyOpenWindowExist);
        }
    }

    public void addShowStartupLayoutListener(ShowStartupLayoutListener showStartupLayoutListener) {
        if (!showStartupLayoutListeners.contains(showStartupLayoutListener)) showStartupLayoutListeners.add(showStartupLayoutListener);
    }

    public void removeShowStartupLayoutListener(ShowStartupLayoutListener showStartupLayoutListener) {
        showStartupLayoutListeners.remove(showStartupLayoutListener);
    }

    public void removeAllShowStartupLayoutListeners() {
        showStartupLayoutListeners.clear();
    }

    protected void fireShowStartupLayoutListeners() {
        for (ShowStartupLayoutListener showStartupLayoutListener : showStartupLayoutListeners) {
            showStartupLayoutListener.onShowStartupLayout();
        }
    }

    public void addCloseStartupLayoutListener(CloseStartupLayoutListener closeStartupLayoutListener) {
        if (!closeStartupLayoutListeners.contains(closeStartupLayoutListener)) closeStartupLayoutListeners.add(closeStartupLayoutListener);
    }

    public void removeCloseStartupLayoutListener(CloseStartupLayoutListener closeStartupLayoutListener) {
        closeStartupLayoutListeners.remove(closeStartupLayoutListener);
    }

    public void removeAllCloseStartupLayoutListener() {
        closeStartupLayoutListeners.clear();
    }

    protected void fireCloseStartupLayoutListeners() {
        for (CloseStartupLayoutListener closeStartupLayoutListener : closeStartupLayoutListeners) {
            closeStartupLayoutListener.onCloseStartupLayout();
        }
    }

    public void showWindow(final Window window, final String caption, OpenType type) {
        showWindow(window, caption, null, type);
    }

    public void showWindow(final Window window, final String caption, final String description, OpenType type) {
        AppWindow appWindow = app.getAppWindow();
        final WindowOpenMode openMode = new WindowOpenMode(window, type);
        Component component;

        window.setCaption(caption);
        window.setDescription(description);

        if (window.getFrame() != null && (window.getFrame() instanceof Window.Editor) && !type.equals(OpenType.DIALOG)) {
            saveScreenHistory(window, caption);
        }

        switch (type) {
            case NEW_TAB:
                closeStartupScreen(appWindow);
                if (AppWindow.Mode.SINGLE.equals(appWindow.getMode())) {
                    VerticalLayout mainLayout = appWindow.getMainLayout();
                    if (mainLayout.getComponentIterator().hasNext()) {
                        Layout oldLayout = (Layout) mainLayout.getComponentIterator().next();
                        WindowBreadCrumbs oldBreadCrumbs = getTabs().get(oldLayout);
                        if (oldBreadCrumbs != null) {
                            Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                            oldWindow.closeAndRun("mainMenu", new Runnable() {
                                public void run() {
                                    showWindow(window, caption, OpenType.NEW_TAB);
                                }
                            });
                            return;
                        }
                    }
                }

                component = showWindowNewTab(window, caption, description, appWindow);
                break;

            case THIS_TAB:
                closeStartupScreen(appWindow);
                component = showWindowThisTab(window, caption, description, appWindow);
                break;

            case DIALOG:
                component = showWindowDialog(window, caption, description, appWindow);
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

        if (window.getContext() != null &&
                !BooleanUtils.isTrue((Boolean) window.getContext().getParams().get("disableApplySettings"))) {
            window.applySettings(new SettingsImpl(window.getId()));
        }

        ((DsContextImplementation) window.getDsContext()).resumeSuspended();
    }

    private void closeStartupScreen(AppWindow appWindow) {
        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            if (tabSheet == null) {
                fireCloseStartupLayoutListeners();
                appWindow.unInitStartupLayout();
                VerticalLayout mainLayout = appWindow.getMainLayout();
                tabSheet = new AppWindow.AppTabSheet();
                tabSheet.setSizeFull();
                mainLayout.addComponent(tabSheet);
                mainLayout.setExpandRatio(tabSheet, 1);
                appWindow.setTabSheet(tabSheet);
            }
        }
    }

    protected Layout createNewWinLayout(Window window, Component... components) {

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

    protected Component showWindowNewTab(final Window window, final String caption, final String description, AppWindow appWindow) {
        final WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs();
        breadCrumbs.addListener(
                new WindowBreadCrumbs.Listener() {
                    public void windowClick(final Window window) {
                        Runnable op = new Runnable() {
                            public void run() {
                                Window currentWindow = breadCrumbs.getCurrentWindow();

                                if (currentWindow != null && window != currentWindow) {
                                    currentWindow.closeAndRun("close", this);
                                }
                            }
                        };
                        op.run();
                    }
                }
        );
        breadCrumbs.addWindow(window);

        final Layout layout = createNewTabLayout(window, caption, description, appWindow, breadCrumbs);

        getTabs().put(layout, breadCrumbs);

        return layout;
    }

    protected Layout createNewTabLayout(final Window window, final String caption, final String description, AppWindow appWindow, Component... components) {
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
            TabSheet.Tab newTab = tabSheet.addTab(layout, formatTabCaption(caption, description), null);
            newTab.setDescription(formatTabDescription(caption, description));
            if (tabSheet instanceof AppWindow.AppTabSheet) {
                newTab.setClosable(true);
                ((AppWindow.AppTabSheet) tabSheet).setTabCloseHandler(
                        layout,
                        new AppWindow.AppTabSheet.TabCloseHandler() {
                            public void onClose(TabSheet tabSheet, Component tabContent) {
                                WindowBreadCrumbs breadCrumbs = getTabs().get(tabContent);
                                Runnable closeTask = new TabCloseTask(breadCrumbs);
                                closeTask.run();
                            }
                        });
            }
            tabSheet.setSelectedTab(layout);
        } else {
            layout.addStyleName("single");
            layout.setMargin(true);
            layout.setWidth("99.9%");
            layout.setHeight("99.85%");
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

        public void run() {
            Window windowToClose = breadCrumbs.getCurrentWindow();
            if (windowToClose != null) {
                windowToClose.closeAndRun("close", new TabCloseTask(breadCrumbs));
            }
        }
    }

    public void setCurrentWindowCaption(String caption, String description) {
        TabSheet tabSheet = app.getAppWindow().getTabSheet();
        if (tabSheet == null) return; // for SINGLE tabbing mode
        com.vaadin.ui.Component tabContent = tabSheet.getSelectedTab();
        if (tabContent == null) return;
        TabSheet.Tab tab = tabSheet.getTab(tabContent);
        if (tab == null) return;
        tab.setCaption(formatTabCaption(caption, description));
    }

    protected String formatTabCaption(final String caption, final String description) {
        String s = formatTabDescription(caption, description);
        int maxLength = ConfigProvider.getConfig(WebConfig.class).getMainTabCaptionLength();
        if (s.length() > maxLength) {
            return s.substring(0, maxLength) + "...";
        } else {
            return s;
        }
    }

    protected String formatTabDescription(final String caption, final String description) {
        if (!StringUtils.isEmpty(description)) {
            return String.format("%s | %s", caption, description);
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
        if (breadCrumbs == null)
            throw new IllegalStateException("BreadCrumbs not found");

        final Window currentWindow = breadCrumbs.getCurrentWindow();
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
            tab.setDescription(formatTabDescription(caption, description));
        } else {
            appWindow.getMainLayout().requestRepaintAll();
        }

        return layout;
    }

    protected Component showWindowDialog(final Window window, final String caption, final String description, AppWindow appWindow) {
        removeWindowsWithName(window.getId());

        com.vaadin.ui.Window win = null;

        com.vaadin.ui.Window mainWindow = app.getAppWindow();
        for (com.vaadin.ui.Window childWindow: mainWindow.getChildWindows()) {
            if (childWindow.isModal() && !childWindow.isClosable()) {
                win = childWindow;
                break;
            }
        }

        if (win == null) {
            win = createDialogWindow(window);
            baseModalWindowCaption = win.getCaption();
            win.setName(window.getId());
        } else {
            modalWindowStack.push(new ModalContentWithCaption(win.getCaption(), win.getContent()));
            win.setCaption(modalWindowStack.peek().caption + " - " + window.getCaption());
            final com.vaadin.ui.Window finalWin = win;
            window.addListener(new Window.CloseListener() {

                public void windowClosed(String actionId) {
                    if (!modalWindowStack.isEmpty()) {
                        ModalContentWithCaption modalContentWithCaption = modalWindowStack.pop();
                        finalWin.setContent(modalContentWithCaption.componentContainer);
                        finalWin.setCaption(modalContentWithCaption.caption);
                        finalWin.center();
                    }
                }
            });
        }

//        win.setName(window.getId());

        Layout layout = (Layout) WebComponentsHelper.getComposition(window);

        // surrond window layout with outer layout to prevent double painting
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.addComponent(layout);
        outerLayout.setExpandRatio(layout, 1);

        win.setContent(outerLayout);

        win.addListener(new com.vaadin.ui.Window.CloseListener() {
            public void windowClose(com.vaadin.ui.Window.CloseEvent e) {
                window.close("close", true);
            }
        });

        final DialogParams dialogParams = getDialogParams();
        if (dialogParams.getWidth() != null)
            win.setWidth(dialogParams.getWidth().floatValue(), Sizeable.UNITS_PIXELS);
        else
            win.setWidth(600, Sizeable.UNITS_PIXELS);

        if (dialogParams.getHeight() != null) {
            win.setHeight(dialogParams.getHeight().floatValue(), Sizeable.UNITS_PIXELS);
            win.getContent().setHeight("100%");
        }

        if (dialogParams.getResizable() != null) {
            win.setResizable(Boolean.valueOf(dialogParams.getResizable()));
        }

        dialogParams.reset();

        win.setModal(true);

        if (!app.getAppWindow().getChildWindows().contains(win)) {
            app.getAppWindow().addWindow(win);
        }
        win.center();

        return win;
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs() {
        return new WindowBreadCrumbs();
    }

    protected com.vaadin.ui.Window createDialogWindow(Window window) {
        return new com.vaadin.ui.Window(window.getCaption());
    }

    public void close(Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        final WindowOpenMode openMode = getWindowOpenMode().get(window);
        if (openMode == null) {
            log.warn("Problem closing window " + window + " : WindowOpenMode not found");
            return;
        }

        closeWindow(window, openMode);
        getWindowOpenMode().remove(window);
    }

    public void checkModificationsAndCloseAll(final Runnable runIfOk, final Runnable runIfCancel) {
        boolean modified = false;
        for (Window window : getOpenWindows()) {
            window.saveSettings();
            if (window.getDsContext() != null && window.getDsContext().isModified()) {
                modified = true;
            }
        }
        if (modified) {
            showOptionDialog(
                    MessageProvider.getMessage(WebWindow.class, "closeUnsaved.caption"),
                    MessageProvider.getMessage(WebWindow.class, "closeUnsaved"),
                    IFrame.MessageType.WARNING,
                    new Action[]{
                            new AbstractAction(MessageProvider.getMessage(WebWindow.class, "actions.Yes")) {
                                public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                    if (runIfOk != null)
                                        runIfOk.run();
                                }
                                @Override
                                public String getIcon() {
                                    return "icons/ok.png";
                                }
                            },
                            new AbstractAction(MessageProvider.getMessage(WebWindow.class, "actions.No")) {
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
        List<Map.Entry<Window, WindowOpenMode>> entries = new ArrayList(getWindowOpenMode().entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            Window window = entries.get(i).getKey();
            if (window instanceof WebWindow.Editor) {
                ((WebWindow.Editor)window).releaseLock();
            }
            closeWindow(window, entries.get(i).getValue());
        }
        getWindowOpenMode().clear();

        Collection windows = App.getInstance().getWindows();
        for (Object win : new ArrayList(windows)) {
            if (!win.equals(App.getInstance().getAppWindow())) {
                App.getInstance().removeWindow((com.vaadin.ui.Window) win);
                if (win instanceof AppWindow)
                    appWindowMap.remove(win);
            }
        }
    }

    private void closeWindow(Window window, WindowOpenMode openMode) {
        AppWindow appWindow = app.getAppWindow();
        switch (openMode.openType) {
            case DIALOG: {
                final com.vaadin.ui.Window win = (com.vaadin.ui.Window) openMode.getData();
                if (modalWindowStack.isEmpty()) {
                    App.getInstance().getAppWindow().removeWindow(win);
                    fireListeners(window, getTabs().size() != 0);
                }
                break;
            }
            case NEW_TAB: {
                final Layout layout = (Layout) openMode.getData();
                layout.removeComponent(WebComponentsHelper.getComposition(window));

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    appWindow.getTabSheet().removeComponent(layout);
                } else {
                    appWindow.getMainLayout().removeComponent(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = getTabs().get(layout);
                if (windowBreadCrumbs != null) {
                    windowBreadCrumbs.clearListeners();
                    windowBreadCrumbs.removeWindow();
                }

                getTabs().remove(layout);
                fireListeners(window, getTabs().size() != 0);
                showStartupScreen(appWindow);
                break;
            }
            case THIS_TAB: {
                final VerticalLayout layout = (VerticalLayout) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = getTabs().get(layout);
                if (breadCrumbs == null)
                    throw new IllegalStateException("Unable to close screen: breadCrumbs not found");

                breadCrumbs.removeWindow();
                Window currentWindow = breadCrumbs.getCurrentWindow();

                final Component component = WebComponentsHelper.getComposition(currentWindow);
                component.setSizeFull();

                layout.removeComponent(WebComponentsHelper.getComposition(window));
                layout.addComponent(component);
                layout.setExpandRatio(component, 1);

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    TabSheet tabSheet = app.getAppWindow().getTabSheet();
                    TabSheet.Tab tab = tabSheet.getTab(layout);
                    tab.setCaption(formatTabCaption(currentWindow.getCaption(), currentWindow.getDescription()));
                    tab.setDescription(formatTabDescription(currentWindow.getCaption(), currentWindow.getDescription()));
                }
                fireListeners(window, getTabs().size() != 0);
                showStartupScreen(appWindow);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

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

    private void showStartupScreen(AppWindow appWindow) {
        if (getTabs().size() == 0) {
            appWindow.getMainLayout().removeAllComponents();
            appWindow.setTabSheet(null);
            appWindow.initStartupLayout();
            fireShowStartupLayoutListeners();
        }
    }

    protected ComponentsFactory createComponentFactory() {
        return new WebComponentsFactory();
    }

    protected void saveScreenHistory(Window window, String caption){
        ScreenHistoryEntity screenHistoryEntity = EntityFactory.create(ScreenHistoryEntity.class);
        screenHistoryEntity.setCaption(caption);
        screenHistoryEntity.setUser(UserSessionClient.getUserSession().getCurrentOrSubstitutedUser());
        screenHistoryEntity.setUrl(makeLink(window));

        CommitContext cc = new CommitContext(Collections.singleton(screenHistoryEntity));
        ServiceLocator.getDataService().commit(cc);
    }

    protected String makeLink(Window window) {
        GlobalConfig c = ConfigProvider.getConfig(GlobalConfig.class);
        Entity entity = null;
        if (window.getFrame() instanceof WebWindow.Editor)
            entity = ((WebWindow.Editor) window.getFrame()).getItem();
        String url = "http://" + c.getWebHostName() + ":" + c.getWebPort() + "/" + c.getWebContextName() + "/open?" +
                "screen=" + window.getFrame().getId();
        if (entity != null) {
            String item = MetadataProvider.getSession().getClass(entity.getClass()).getName() + "-" + entity.getId();
            url += "&" + "item=" + item + "&" + "params=item:" + item;
        }
        Map<String, Object> params = window.getContext().getParams();
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                Object value = param.getValue();
                if (value instanceof String /*|| value instanceof Integer || value instanceof Double*/
                        || value instanceof Boolean) {
                    sb.append(",").append(param.getKey()).append(":").append(value.toString());
                }
            }
        }
        if (sb.length() > 0) {
            if (entity != null) {
                url += sb.toString();
            } else {
                url += "&params=" + sb.deleteCharAt(0).toString();
            }
        }

        return url;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void showNotification(String caption, IFrame.NotificationType type) {
        app.getAppWindow().showNotification(caption, WebComponentsHelper.convertNotificationType(type));
    }

    @Override
    public void showNotification(String caption, String description, IFrame.NotificationType type) {
        com.vaadin.ui.Window.Notification notify =
                new com.vaadin.ui.Window.Notification(caption, description, WebComponentsHelper.convertNotificationType(type));
        if(type.equals(IFrame.NotificationType.HUMANIZED))
            notify.setDelayMsec(3000);
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

        window.addListener(new com.vaadin.ui.Window.CloseListener() {
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

        window.addListener(new com.vaadin.ui.Window.CloseListener() {
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
                public void buttonClick(Button.ClickEvent event) {
                    action.actionPerform(null);
                    AppWindow appWindow = app.getAppWindow();
                    if (appWindow != null) // possible appWindow is null after logout
                        appWindow.removeWindow(window);
                }
            });
            if (action.getIcon() != null) {
                button.setIcon(new ThemeResource(action.getIcon()));
                button.addStyleName(WebButton.ICON_STYLE);
            }
            button.setDebugId(action.getId());
            buttonsContainer.addComponent(button);
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

    private void removeWindowsWithName(String name) {
        final com.vaadin.ui.Window mainWindow = app.getAppWindow();

        for (com.vaadin.ui.Window childWindow : new ArrayList<com.vaadin.ui.Window>(mainWindow.getChildWindows())) {
            if (name.equals(childWindow.getName())) {
                String msg = new StrBuilder("Another " + name + " window exists, removing it\n")
                        //.appendWithSeparators(Thread.currentThread().getStackTrace(), "\n")
                        .toString();
                log.warn(msg);
                mainWindow.removeWindow(childWindow);
            }
        }
    }

    public void reloadBreadCrumbs() {
        Layout layout;

        final AppWindow appWindow = App.getInstance().getAppWindow();
        final AppWindow.Mode viewMode = appWindow.getMode();

        if (viewMode == AppWindow.Mode.SINGLE) {
            final Layout mainLayout = appWindow.getMainLayout();
            layout = (Layout) mainLayout.getComponentIterator().next();
        } else {
            layout = (Layout) appWindow.getTabSheet().getSelectedTab();
        }

        if (layout != null) {
            WindowBreadCrumbs breadCrumbs = getTabs().get(layout);
            if (breadCrumbs != null) {
                breadCrumbs.update();
            }
        }
    }

    @Override
    protected void initDebugIds(final Window window) {
        com.haulmont.cuba.gui.ComponentsHelper.walkComponents(window, new ComponentVisitor() {
            public void visit(com.haulmont.cuba.gui.components.Component component, String name) {
                final String id = window.getId() + "." + name;
                if (ConfigProvider.getConfig(GlobalConfig.class).getAllowIdSuffix()) {
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

    @Override
    protected void checkCanOpenWindow(WindowInfo windowInfo, OpenType openType, Map<String, Object> params) {
        if (OpenType.NEW_TAB.equals(openType)) {
            int maxCount = ConfigProvider.getConfig(WebConfig.class).getMaxTabCount();
            if (maxCount > 0 && maxCount <= getCurrentWindowData().tabs.size()) {
                app.getAppWindow().showNotification(
                        MessageProvider.formatMessage(AppConfig.getInstance().getMessagesPack(),"tooManyOpenTabs.message",maxCount),
                        com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE);
                throw new SilentException();
            }
        }
    }

    public void setDebugId(Component component, String id) {
        if (ConfigProvider.getConfig(GlobalConfig.class).getAllowIdSuffix()) {
            component.setDebugId(generateDebugId(id));
        } else {
            component.setDebugId(id);
        }
    }

    private String generateDebugId(String id) {
        Integer count = debugIds.get(id);
        if (count == null) {
            count = 0;
        }
        debugIds.put(id, ++count);
        return id + "." + count;
    }

    public interface WindowCloseListener extends Serializable {
        void onWindowClose(Window window, boolean anyOpenWindowExist);
    }

    public interface ShowStartupLayoutListener extends Serializable {
        void onShowStartupLayout();
    }

    public interface CloseStartupLayoutListener extends Serializable {
        void onCloseStartupLayout();
    }

}

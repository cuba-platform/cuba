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

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.settings.SettingsImpl;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.ComponentsHelper;
import com.haulmont.cuba.web.ui.WindowBreadCrumbs;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.terminal.Sizeable;
import com.itmill.toolkit.ui.*;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class WindowManager extends com.haulmont.cuba.gui.WindowManager
{
    protected App app;

    protected Map<Layout, WindowBreadCrumbs> tabs = new HashMap<Layout, WindowBreadCrumbs>();

    protected Map<Window, WindowOpenMode> windowOpenMode = new LinkedHashMap<Window, WindowOpenMode>();

    private Log log = LogFactory.getLog(WindowManager.class);

    public WindowManager(App app) {
        this.app = app;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected DataService createDefaultDataService() {
        return new GenericDataService(false);
    }

    @Override
    public Collection<Window> getOpenWindows() {
        return new ArrayList(windowOpenMode.keySet());
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

    public void showWindow(final Window window, final String caption, OpenType type) {
        AppWindow appWindow = app.getAppWindow();
        final WindowOpenMode openMode = new WindowOpenMode(window, type);

        Component component;
        if (OpenType.NEW_TAB.equals(type)) {
            if (AppWindow.Mode.SINGLE.equals(appWindow.getMode())) {
                VerticalLayout mainLayout = appWindow.getMainLayout();
                if (mainLayout.getComponentIterator().hasNext()) {
                    Layout oldLayout = (Layout) mainLayout.getComponentIterator().next();
                    WindowBreadCrumbs oldBreadCrumbs = tabs.get(oldLayout);
                    if (oldBreadCrumbs == null)
                        throw new IllegalStateException("BreadCrumbs not found");
                    Window oldWindow = oldBreadCrumbs.getCurrentWindow();
                    com.haulmont.cuba.web.gui.Window webWindow;
                    if (oldWindow instanceof Window.Wrapper) {
                        webWindow = ((Window.Wrapper) oldWindow).getWrappedWindow();
                    } else {
                        webWindow = (com.haulmont.cuba.web.gui.Window) oldWindow;
                    }

                    webWindow.closeAndRun("mainMenu", new Runnable() {
                        public void run() {
                            showWindow(window, caption, OpenType.NEW_TAB);
                        }
                    });

                    return;
                }
            }

            component = showWindowNewTab(window, caption, appWindow);

        } else if (OpenType.THIS_TAB.equals(type)) {
            component = showWindowThisTab(window, caption, appWindow);

        } else if (OpenType.DIALOG.equals(type)) {
            component = showWindowDialog(window, caption, appWindow);

        } else {
            throw new UnsupportedOperationException();
        }

        openMode.setData(component);

        if (window instanceof Window.Wrapper) {
            Window wrappedWindow = ((Window.Wrapper) window).getWrappedWindow();
            windowOpenMode.put(wrappedWindow, openMode);
        } else {
            windowOpenMode.put(window, openMode);
        }

        window.applySettings(new SettingsImpl(window.getId(), getSettingService()));
    }

    protected Component showWindowNewTab(final Window window, final String caption, AppWindow appWindow) {
        final WindowBreadCrumbs breadCrumbs = createWindowBreadCrumbs();
        breadCrumbs.addListener(
                    new WindowBreadCrumbs.Listener()
                {
                    public void windowClick(final Window window) {
                        Runnable op = new Runnable() {
                            public void run() {
                                Window currentWindow = breadCrumbs.getCurrentWindow();

                                if (currentWindow != null && window != currentWindow) {
                                    com.haulmont.cuba.web.gui.Window webWindow;
                                    if (currentWindow instanceof Window.Wrapper) {
                                        webWindow = ((Window.Wrapper) currentWindow).getWrappedWindow();
                                    } else {
                                        webWindow = (com.haulmont.cuba.web.gui.Window) currentWindow;
                                    }
                                    webWindow.closeAndRun("close", this);
                                }
                            }
                        };
                        op.run();
                    }
                }
        );
        breadCrumbs.addWindow(window);

        final Layout layout = createNewTabLayout(window, caption, appWindow, breadCrumbs);

        tabs.put(layout, breadCrumbs);

        return layout;
    }

    protected Layout createNewTabLayout(final Window window, final String caption, AppWindow appWindow, Component... components) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        if (components != null) {
            for (final Component c : components) {
                layout.addComponent(c);
            }
        }

        final Component component = ComponentsHelper.unwrap(window);
        component.setSizeFull();
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            layout.setMargin(true);
            tabSheet.addTab(layout, caption, null);
            tabSheet.setSelectedTab(layout);
        } else {
            VerticalLayout mainLayout = appWindow.getMainLayout();
            mainLayout.removeAllComponents();
            mainLayout.addComponent(layout);
        }

        return layout;
    }

    protected Component showWindowThisTab(Window window, String caption, AppWindow appWindow) {
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
        layout.removeComponent(ComponentsHelper.unwrap(currentWindow));

        final Component component = ComponentsHelper.unwrap(window);
        component.setSizeFull();
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        breadCrumbs.addWindow(window);

        if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
            TabSheet tabSheet = appWindow.getTabSheet();
            tabSheet.setTabCaption(layout, caption);
            tabSheet.requestRepaintAll();
        } else {
            appWindow.getMainLayout().requestRepaintAll();
        }

        return layout;
    }

    protected Component showWindowDialog(final Window window, String caption, AppWindow appWindow) {
        removeWindowsWithName(window.getId());

        final com.itmill.toolkit.ui.Window win = createDialogWindow(window);
        win.setName(window.getId());

        Layout layout = (Layout) ComponentsHelper.unwrap(window);

        // surrond window layout with outer layout to prevent double painting
        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.addComponent(layout);
        outerLayout.setExpandRatio(layout, 1);

        win.setLayout(outerLayout);

        win.addListener(new com.itmill.toolkit.ui.Window.CloseListener() {
            public void windowClose(com.itmill.toolkit.ui.Window.CloseEvent e) {
                window.close("close");
            }
        });

        win.setWidth(600, Sizeable.UNITS_PIXELS);
//        win.setResizable(false);
        win.setModal(true);

        App.getInstance().getMainWindow().addWindow(win);
        win.center();

        return win;
    }

    protected WindowBreadCrumbs createWindowBreadCrumbs() {
        return new WindowBreadCrumbs();
    }

    protected com.itmill.toolkit.ui.Window createDialogWindow(Window window) {
        return new com.itmill.toolkit.ui.Window(window.getCaption());
    }

    protected Locale getLocale() {
        return App.getInstance().getLocale();
    }

    public void close(com.haulmont.cuba.gui.components.Window window) {
        if (window instanceof Window.Wrapper) {
            window = ((Window.Wrapper) window).getWrappedWindow();
        }

        final WindowOpenMode openMode = windowOpenMode.get(window);
        if (openMode == null) throw new IllegalStateException();

        boolean needRefresh = closeWindow(window, openMode);
        windowOpenMode.remove(window);

        // TODO (krivopustov) fix TabSheet repaint
        if (needRefresh)
            app.getMainWindow().open(new ExternalResource(app.getURL()));
    }

    public void closeAll() {
        boolean needRefresh = false;
        List<Map.Entry<Window,WindowOpenMode>> entries = new ArrayList(windowOpenMode.entrySet());
        for (int i = entries.size() - 1; i >= 0; i--) {
            boolean res = closeWindow(entries.get(i).getKey(), entries.get(i).getValue());
            needRefresh = needRefresh || res;
        }
        windowOpenMode.clear();
        // TODO (krivopustov) fix TabSheet repaint
        if (needRefresh)
            app.getMainWindow().open(new ExternalResource(app.getURL()));
    }

    private boolean closeWindow(Window window, WindowOpenMode openMode) {
        AppWindow appWindow = app.getAppWindow();
        switch (openMode.openType) {
            case DIALOG: {
                final com.itmill.toolkit.ui.Window win = (com.itmill.toolkit.ui.Window) openMode.getData();
                App.getInstance().getMainWindow().removeWindow(win);
                return false;
            }
            case NEW_TAB: {
                final Layout layout = (Layout) openMode.getData();
                layout.removeComponent(ComponentsHelper.unwrap(window));

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    appWindow.getTabSheet().removeComponent(layout);
                } else {
                    appWindow.getMainLayout().removeComponent(layout);
                }

                WindowBreadCrumbs windowBreadCrumbs = tabs.get(layout);
                if (windowBreadCrumbs != null)
                    windowBreadCrumbs.clearListeners();

                tabs.remove(layout);

                return AppWindow.Mode.TABBED.equals(appWindow.getMode());
            }
            case THIS_TAB: {
                final VerticalLayout layout = (VerticalLayout) openMode.getData();

                final WindowBreadCrumbs breadCrumbs = tabs.get(layout);
                if (breadCrumbs == null) throw new IllegalStateException("Unable to close screen: breadCrumbs not found");

                breadCrumbs.removeWindow();
                Window currentWindow = breadCrumbs.getCurrentWindow();

                final Component component = ComponentsHelper.unwrap(currentWindow);
                component.setSizeFull();

                layout.removeComponent(ComponentsHelper.unwrap(window));
                layout.addComponent(component);
                layout.setExpandRatio(component, 1);

                if (AppWindow.Mode.TABBED.equals(appWindow.getMode())) {
                    TabSheet tabSheet = app.getAppWindow().getTabSheet();
                    tabSheet.setTabCaption(layout, currentWindow.getCaption());
                    tabSheet.requestRepaintAll();
                }

                return false;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    protected ComponentsFactory createComponentFactory() {
        return new WebComponentsFactory();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void showMessageDialog(String title, String message, IFrame.MessageType messageType) {
        removeWindowsWithName("cuba-message-dialog");

        final com.itmill.toolkit.ui.Window window = new com.itmill.toolkit.ui.Window(title);
        window.setName("cuba-message-dialog");

        window.addListener(new com.itmill.toolkit.ui.Window.CloseListener() {
            public void windowClose(com.itmill.toolkit.ui.Window.CloseEvent e) {
                App.getInstance().getMainWindow().removeWindow(window);
            }
        });

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        window.setLayout(layout);

        Label desc = new Label(message, Label.CONTENT_XHTML);
        layout.addComponent(desc);

        window.addComponent(layout);

        window.setWidth(400, Sizeable.UNITS_PIXELS);
        window.setResizable(false);
        window.setModal(true);

        App.getInstance().getMainWindow().addWindow(window);
    }

    public void showOptionDialog(String title, String message,
                                 IFrame.MessageType messageType, Action[] actions)
    {
        removeWindowsWithName("cuba-option-dialog");

        final com.itmill.toolkit.ui.Window window = new com.itmill.toolkit.ui.Window(title);
        window.setName("cuba-option-dialog");

        window.addListener(new com.itmill.toolkit.ui.Window.CloseListener() {
            public void windowClose(com.itmill.toolkit.ui.Window.CloseEvent e) {
                app.getMainWindow().removeWindow(window);
            }
        });

        Label messageBox = new Label(message, Label.CONTENT_XHTML);

        window.setWidth(400, Sizeable.UNITS_PIXELS);
        window.setResizable(false);
        window.setModal(true);

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        window.setLayout(layout);

        HorizontalLayout actionsBar = new HorizontalLayout();
        actionsBar.setHeight(-1, Sizeable.UNITS_PIXELS);

        HorizontalLayout buttonsContainer = new HorizontalLayout();

        for (final Action action : actions) {
            buttonsContainer.addComponent(new Button(action.getCaption(), new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    action.actionPerform(null);
                    app.getMainWindow().removeWindow(window);
                }
            }));
        }

        actionsBar.addComponent(buttonsContainer);

        layout.addComponent(messageBox);
        layout.addComponent(actionsBar);

        messageBox.setSizeFull();
        layout.setExpandRatio(messageBox, 1);
        layout.setComponentAlignment(actionsBar, com.itmill.toolkit.ui.Alignment.BOTTOM_RIGHT);

        App.getInstance().getMainWindow().addWindow(window);
        window.center();
    }

    private void removeWindowsWithName(String name) {
        final com.itmill.toolkit.ui.Window mainWindow = app.getMainWindow();

        for (com.itmill.toolkit.ui.Window childWindow : new ArrayList<com.itmill.toolkit.ui.Window>(mainWindow.getChildWindows())) {
            if (name.equals(childWindow.getName())) {
                String msg = new StrBuilder("Another " + name + " window exists, removing it\n")
                        .appendWithSeparators(Thread.currentThread().getStackTrace(), "\n")
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

        if (viewMode == AppWindow.Mode.SINGLE) 
        {
            final Layout mainLayout = appWindow.getMainLayout();
            layout = (Layout) mainLayout.getComponentIterator().next();
        }
        else {
            layout = (Layout) appWindow.getTabSheet().getSelectedTab();
        }

        if (layout != null) {
            WindowBreadCrumbs breadCrumbs = tabs.get(layout);
            if (breadCrumbs != null) {
                breadCrumbs.update();
            }
        }
    }
}

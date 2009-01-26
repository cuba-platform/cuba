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

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.haulmont.cuba.web.ui.ScreenTitlePane;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.AbstractLayout;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.TabSheet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ScreenManager extends WindowManager
{
    private static class TabInfo
    {
        private ScreenTitlePane titlePane;
        private LinkedList<Window> screens = new LinkedList<Window>();

        private TabInfo(ScreenTitlePane titlePane) {
            this.titlePane = titlePane;
        }
    }

    private App app;

    private Map<AbstractLayout, TabInfo> tabs = new HashMap<AbstractLayout, TabInfo>();

    public ScreenManager(App app) {
        this.app = app;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public <T extends Window> T openWindow(String descriptor, WindowManager.OpenType openType, Map params) {
        Window window = createScreen(descriptor, params);
        showWindow(window, (String)params.get("caption"), openType);
        return (T) window;
    }

    private Window createScreen(String descriptor, Map params) {
        return createWindowFromTemplate(descriptor, params);
    }

    public <T extends Window> T openWindow(Class aclass, WindowManager.OpenType openType, Map params) {
        Window window = createScreen(aclass, params);
        showWindow(window, (String)params.get("caption"), openType);
        return (T) window;
    }

    private Window createScreen(Class aclass, Map params) {
        try {
            final Window window = (Window) aclass.newInstance();
            invokeMethod(window, "init");
            return window;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected Window showWindow(Window window, String caption, OpenType type) {
        if (OpenType.NEW_TAB.equals(type)) {
            ExpandLayout layout = new ExpandLayout();

            ScreenTitlePane titlePane = new ScreenTitlePane();
            titlePane.addCaption(caption);
            layout.addComponent(titlePane);

            final Component component = ComponentsHelper.unwrap(window);
            layout.addComponent(component);
            layout.expand(component);

            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            tabSheet.addTab(layout, caption, null);
            tabSheet.setSelectedTab(layout);

            TabInfo tabInfo = new TabInfo(titlePane);
            tabInfo.screens.add(window);
            tabs.put(layout, tabInfo);
        } else if (OpenType.THIS_TAB.equals(type)) {
            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            ExpandLayout layout = (ExpandLayout) tabSheet.getSelectedTab();

            TabInfo tabInfo = tabs.get(layout);
            if (tabInfo == null)
                throw new IllegalStateException("Current tab not found");

            layout.removeComponent(ComponentsHelper.unwrap(tabInfo.screens.getLast()));
            layout.addComponent(ComponentsHelper.unwrap(window));
            layout.expand(ComponentsHelper.unwrap(window));

            tabInfo.titlePane.addCaption(caption);
            tabInfo.screens.add(window);
        } else
            throw new UnsupportedOperationException("Opening type not supported: " + type);

        return window;
    }

    public void closeScreen() {
        TabSheet tabSheet = app.getAppWindow().getTabSheet();
        ExpandLayout layout = (ExpandLayout) tabSheet.getSelectedTab();

        TabInfo tabInfo = tabs.get(layout);
        if (tabInfo == null)
            throw new IllegalStateException("Unable to close screen: current tab not found");

        Window window = tabInfo.screens.getLast();

        final Object res = invokeMethod(window, "close");
        if (res != null && !Boolean.TRUE.equals(res)) { return; }

        tabInfo.screens.removeLast();

        layout.removeComponent(ComponentsHelper.unwrap(window));
        if (tabInfo.screens.isEmpty()) {
            tabSheet.removeComponent(layout);
            tabs.remove(layout);
            app.getMainWindow().open(new ExternalResource(app.getURL())); // TODO fix TabSheet repaint
        } else {
            tabInfo.titlePane.removeCaption();
            Window prevScreen = tabInfo.screens.getLast();
            layout.addComponent(ComponentsHelper.unwrap(prevScreen));
            layout.expand(ComponentsHelper.unwrap(prevScreen));
        }
    }

    protected ComponentsFactory createComponentFactory() {
        return new WebComponentsFactory();
    }
}

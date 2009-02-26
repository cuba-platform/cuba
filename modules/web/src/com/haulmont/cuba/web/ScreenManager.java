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
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.ComponentsHelper;
import com.haulmont.cuba.web.ui.ScreenTitlePane;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
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

    private Map<Layout, TabInfo> tabs = new HashMap<Layout, TabInfo>();

    public ScreenManager(App app) {
        this.app = app;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected DataService createDefaultDataService() {
        return new GenericDataService(false);
    }

    protected void showWindow(Window window, String caption, OpenType type) {
        if (OpenType.NEW_TAB.equals(type)) {
            VerticalLayout layout = new VerticalLayout();
            layout.setSizeFull();

            ScreenTitlePane titlePane = new ScreenTitlePane();
            titlePane.addCaption(caption);

            final Component component = ComponentsHelper.unwrap(window);
            component.setSizeFull();

            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            tabSheet.addTab(layout, caption, null);
            tabSheet.setSelectedTab(layout);

            layout.addComponent(titlePane);
            layout.addComponent(component);
            layout.setExpandRatio(component, 1);

            TabInfo tabInfo = new TabInfo(titlePane);
            tabInfo.screens.add(window);
            tabs.put(layout, tabInfo);
        } else if (OpenType.THIS_TAB.equals(type)) {
            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            VerticalLayout layout = (VerticalLayout) tabSheet.getSelectedTab();

            TabInfo tabInfo = tabs.get(layout);
            if (tabInfo == null)
                throw new IllegalStateException("Current tab not found");

            layout.removeComponent(ComponentsHelper.unwrap(tabInfo.screens.getLast()));

            final Component component = ComponentsHelper.unwrap(window);
            layout.addComponent(component);
            component.setSizeFull();
            layout.setExpandRatio(component, 1);

            tabInfo.titlePane.addCaption(caption);
            tabInfo.screens.add(window);
        } else
            throw new UnsupportedOperationException("Opening type not supported: " + type);
    }

    protected Locale getLocale() {
        return App.getInstance().getLocale();
    }

    public void closeScreen() {
        TabSheet tabSheet = app.getAppWindow().getTabSheet();
        VerticalLayout layout = (VerticalLayout) tabSheet.getSelectedTab();

        TabInfo tabInfo = tabs.get(layout);
        if (tabInfo == null)
            throw new IllegalStateException("Unable to close screen: current tab not found");

        Window window = tabInfo.screens.getLast();

        tabInfo.screens.removeLast();

        layout.removeComponent(ComponentsHelper.unwrap(window));
        if (tabInfo.screens.isEmpty()) {
            tabSheet.removeComponent(layout);
            tabs.remove(layout);
            app.getMainWindow().open(new ExternalResource(app.getURL())); // TODO fix TabSheet repaint
        } else {
            tabInfo.titlePane.removeCaption();
            Window prevScreen = tabInfo.screens.getLast();

            final Component component = ComponentsHelper.unwrap(prevScreen);
            component.setSizeFull();

            layout.addComponent(component);
            layout.setExpandRatio(component, 1);
//            layout.expand(ComponentsHelper.unwrap(prevScreen));
        }
    }

    protected ComponentsFactory createComponentFactory() {
        return new WebComponentsFactory();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void showMessageDialog(String title, String message, IFrame.MessageType messageType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Action showOptionDialog(String title, String message, IFrame.MessageType messageType, Action[] actions) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

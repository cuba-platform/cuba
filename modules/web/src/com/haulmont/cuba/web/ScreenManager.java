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

import com.haulmont.cuba.web.ui.Screen;
import com.haulmont.cuba.web.ui.ScreenContext;
import com.haulmont.cuba.web.ui.ScreenTitlePane;
import com.haulmont.cuba.web.xml.WebComponentsFactory;
import com.haulmont.cuba.gui.config.Action;
import com.haulmont.cuba.gui.xml.ComponentsLoader;
import com.haulmont.cuba.gui.xml.ComponentsLoaderConfig;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.AbstractLayout;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.TabSheet;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ScreenManager
{
    private static class TabInfo
    {
        private ScreenTitlePane titlePane;
        private LinkedList<Screen> screens = new LinkedList<Screen>();

        private TabInfo(ScreenTitlePane titlePane) {
            this.titlePane = titlePane;
        }
    }

    private App app;

    private Map<AbstractLayout, TabInfo> tabs = new HashMap<AbstractLayout, TabInfo>();

    public ScreenManager(App app) {
        this.app = app;
    }

    public Screen openScreen(ScreenOpenType type, String actionName) {
        return openScreen(type, actionName, null);
    }

    public Screen openScreen(ScreenOpenType type, String actionName, String tabCaption) {
        app.getAppLog().debug("Opening screen " + actionName);
        Action action = app.getActionsConfig().getAction(actionName);
        if (tabCaption == null)
            tabCaption = action.getCaption();

        Screen screen = createScreen(action);
        screen.setSizeFull();

        if (ScreenOpenType.NEW_TAB.equals(type)) {
            ExpandLayout layout = new ExpandLayout();

            ScreenTitlePane titlePane = new ScreenTitlePane();
            titlePane.addCaption(action.getCaption());
            layout.addComponent(titlePane);

            layout.addComponent(screen);
            layout.expand(screen);

            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            tabSheet.addTab(layout, tabCaption, null);
            tabSheet.setSelectedTab(layout);

            TabInfo tabInfo = new TabInfo(titlePane);
            tabInfo.screens.add(screen);
            tabs.put(layout, tabInfo);

            screen.init(new ScreenContext(layout, titlePane));
        }
        else if (ScreenOpenType.THIS_TAB.equals(type)) {
            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            ExpandLayout layout = (ExpandLayout) tabSheet.getSelectedTab();

            TabInfo tabInfo = tabs.get(layout);
            if (tabInfo == null)
                throw new IllegalStateException("Current tab not found");

            layout.removeComponent(tabInfo.screens.getLast());
            layout.addComponent(screen);
            layout.expand(screen);

            tabInfo.titlePane.addCaption(action.getCaption());
            tabInfo.screens.add(screen);

            screen.init(new ScreenContext(layout, tabInfo.titlePane));
        }
        else
            throw new UnsupportedOperationException("Opening type not supported: " + type);

        return screen;
    }

    public void closeScreen() {
        TabSheet tabSheet = app.getAppWindow().getTabSheet();
        ExpandLayout layout = (ExpandLayout) tabSheet.getSelectedTab();

        TabInfo tabInfo = tabs.get(layout);
        if (tabInfo == null)
            throw new IllegalStateException("Unable to close screen: current tab not found");

        Screen screen = tabInfo.screens.getLast();
        if (!screen.onClose()) {
            return;
        }
        tabInfo.screens.removeLast();

        layout.removeComponent(screen);
        if (tabInfo.screens.isEmpty()) {
            tabSheet.removeComponent(layout);
            tabs.remove(layout);
            app.getMainWindow().open(new ExternalResource(app.getURL())); // TODO fix TabSheet repaint
        }
        else {
            tabInfo.titlePane.removeCaption();
            Screen prevScreen = tabInfo.screens.getLast();
            layout.addComponent(prevScreen);
            layout.expand(prevScreen);
        }
    }

    private Screen createScreen(Action action) {
        final Element descriptor = action.getDescriptor();
        String className = descriptor.attributeValue("class");

        if (StringUtils.isBlank(className)) {
            final String template = descriptor.attributeValue("template");
            final ComponentsLoader loader = new ComponentsLoader(new WebComponentsFactory(), ComponentsLoaderConfig.getWindowLoaders());

            return (Screen) loader.loadComponent(getClass().getResource(template));
        } else {
            try {
                Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
                return (Screen) c.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}

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

import com.haulmont.cuba.gui.config.Action;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DatasourceFactoryImpl;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.web.ui.Screen;
import com.haulmont.cuba.web.ui.ScreenContext;
import com.haulmont.cuba.web.ui.ScreenTitlePane;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.AbstractLayout;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.TabSheet;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

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

    private Map<String, Document> descriptorsCache = new HashMap<String, Document>();

    protected Document parseDescriptor(String template) {
        Document document = descriptorsCache.get(template);
        if (document == null) {
            final InputStream stream = getClass().getResourceAsStream(template);

            SAXReader reader = new SAXReader();
            try {
                document = reader.read(stream);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

            final Element metadataContextElement = document.getRootElement().element("metadataContext");
            if (metadataContextElement != null) {
                final List<Element> viewsElements = metadataContextElement.elements("deployViews");
                for (Element viewsElement : viewsElements) {
                    final String resource = viewsElement.attributeValue("name");
                    MetadataProvider.getViewRepository().deployViews(getClass().getResourceAsStream(resource));
                }
            }

            descriptorsCache.put(template, document);
        }

        return document;
    }

    private Screen createScreen(Action action) {
        final Element descriptor = action.getDescriptor();
        String className = descriptor.attributeValue("class");

        if (StringUtils.isBlank(className)) {
            final String template = descriptor.attributeValue("template");

            Document document = parseDescriptor(template);
            final Element rootElement = document.getRootElement();

            final DsContextLoader dsContextLoader = new DsContextLoader(new DatasourceFactoryImpl());
            final DsContext dsContext = dsContextLoader.loadDatasources(rootElement.element("dsContext"));

            final LayoutLoader layoutLoader = new LayoutLoader(new WebComponentsFactory(), LayoutLoaderConfig.getWindowLoaders(), dsContext);
            final Screen screen = (Screen) layoutLoader.loadComponent(rootElement.element("layout"));

            return screen;
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

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

import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.Action;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Context;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceFactoryImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.web.components.ComponentsHelper;
import com.haulmont.cuba.web.ui.Screen;
import com.haulmont.cuba.web.ui.ScreenTitlePane;
import com.haulmont.cuba.web.xml.layout.WebComponentsFactory;
import com.itmill.toolkit.terminal.ExternalResource;
import com.itmill.toolkit.ui.AbstractLayout;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.TabSheet;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScreenManager
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

    public Window openScreen(ScreenOpenType type, String actionName) {
        return openScreen(type, actionName, null);
    }

    public Window openScreen(ScreenOpenType type, String actionName, String tabCaption) {
        app.getAppLog().debug("Opening screen " + actionName);
        Action action = app.getActionsConfig().getAction(actionName);
        if (tabCaption == null)
            tabCaption = action.getCaption();

        Window window = createScreen(action);

        if (ScreenOpenType.NEW_TAB.equals(type)) {
            ExpandLayout layout = new ExpandLayout();

            ScreenTitlePane titlePane = new ScreenTitlePane();
            titlePane.addCaption(action.getCaption());
            layout.addComponent(titlePane);

            final Component component = ComponentsHelper.unwrap(window);
            layout.addComponent(component);
            layout.expand(component);

            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            tabSheet.addTab(layout, tabCaption, null);
            tabSheet.setSelectedTab(layout);

            TabInfo tabInfo = new TabInfo(titlePane);
            tabInfo.screens.add(window);
            tabs.put(layout, tabInfo);

//            window.init(new ScreenContext(layout, titlePane));
        } else if (ScreenOpenType.THIS_TAB.equals(type)) {
            TabSheet tabSheet = app.getAppWindow().getTabSheet();
            ExpandLayout layout = (ExpandLayout) tabSheet.getSelectedTab();

            TabInfo tabInfo = tabs.get(layout);
            if (tabInfo == null)
                throw new IllegalStateException("Current tab not found");

            layout.removeComponent(ComponentsHelper.unwrap(tabInfo.screens.getLast()));
            layout.addComponent(ComponentsHelper.unwrap(window));
            layout.expand(ComponentsHelper.unwrap(window));

            tabInfo.titlePane.addCaption(action.getCaption());
            tabInfo.screens.add(window);

//            window.init(new ScreenContext(layout, tabInfo.titlePane));
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

    private Window createScreen(Action action) {
        final Element descriptor = action.getDescriptor();
        String className = descriptor.attributeValue("class");

        if (StringUtils.isBlank(className)) {
            return createWindowFromTemplate(descriptor);
        } else {
            try {
                Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
                final Window window = (Window) c.newInstance();
                invokeMethod(window, "init");
                return window;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected Window createWindowFromTemplate(Element descriptor) {
        final String template = descriptor.attributeValue("template");

        Document document = parseDescriptor(template);
        final Element rootElement = document.getRootElement();

        final DsContextLoader dsContextLoader = new DsContextLoader(new DatasourceFactoryImpl());
        final DsContext dsContext = dsContextLoader.loadDatasources(rootElement.element("dsContext"));

        final LayoutLoader layoutLoader = new LayoutLoader(new WebComponentsFactory(), LayoutLoaderConfig.getWindowLoaders(), dsContext);
        final Window window = (Screen) layoutLoader.loadComponent(rootElement);


        for (Datasource ds : dsContext.getAll()) {
            if (ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }

        dsContext.setContext(new Context() {
            public <T> T getValue(String property) {
                final com.haulmont.cuba.gui.components.Component component = window.getComponent(property);
                if (component instanceof com.haulmont.cuba.gui.components.Component.Field) {
                    return ((com.haulmont.cuba.gui.components.Component.Field) component).<T>getValue();
                } else {
                    return null;
                }
            }

            public void setValue(String property, Object value) {
                final com.haulmont.cuba.gui.components.Component component = window.getComponent(property);
                if (component instanceof com.haulmont.cuba.gui.components.Component.Field) {
                    ((com.haulmont.cuba.gui.components.Component.Field) component).setValue(value);
                } else {
                    throw new UnsupportedOperationException();
                }
            }

            public void addValueListener(ValueListener listener) {
            }

            public void removeValueListener(ValueListener listener) {
            }
        });

        return wrapByCustomClass(window, rootElement);
    }

    protected Window wrapByCustomClass(Window window, Element element) {
        Window res = window;
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            try {
                final Class<?> aClass = Class.forName(screenClass);
                final Constructor<?> constructor = aClass.getConstructor(Screen.class);
                res = (Window) constructor.newInstance(window);

                invokeMethod(res, "init");
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            return res;
        } else {
            return res;
        }
    }

    protected <T> T invokeMethod(Window window, String name) {
        try {
            final Method method = window.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            return (T) method.invoke(window);
        } catch (Throwable e) {
            return null;
        }
    }
}

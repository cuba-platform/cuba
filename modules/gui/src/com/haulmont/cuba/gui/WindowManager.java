/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 26.01.2009 11:14:00
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.Context;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DatasourceFactoryImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public abstract class WindowManager {
    public enum OpenType {
        NEW_TAB,
        THIS_TAB,
        DIALOG
    }

    protected abstract Window createWindow(String descriptor, Map params);
    protected abstract Window createWindow(Class aclass, Map params);

    public <T extends Window> T openWindow(String descriptor, WindowManager.OpenType openType, Map params) {
        Window window = createWindow(descriptor, params);

        String caption = getCaption(window, params);

        showWindow(window, caption, openType);
        return (T) window;
    }

    protected <T extends Window> String getCaption(Window window, Map params) {
        String caption = (String) params.get("caption");
        if (StringUtils.isEmpty(caption)) {
            final ResourceBundle resourceBundle = window.getResourceBundle();
            if (resourceBundle != null) {
                try {
                    caption = resourceBundle.getString("caption");
                } catch (MissingResourceException e) {
                    caption = null;
                }
            }
        }

        if (caption != null) {
            caption = TemplateHelper.processTemplate(caption, params);
        }

        return caption;
    }

    public <T extends Window> T openWindow(Class aclass, WindowManager.OpenType openType, Map params) {
        Window window = createWindow(aclass, params);

        String caption = getCaption(window, params);

        showWindow(window, caption, openType);
        return (T) window;
    }

    public <T extends Window> T openWindow(String descriptor, OpenType openType) {
        return (T)openWindow(descriptor, openType, Collections.emptyMap());
    }

    public <T extends Window> T openWindow(Class aclass, OpenType openType) {
        return (T)openWindow(aclass, openType, Collections.emptyMap());
    }

    protected abstract void showWindow(Window window, String caption, OpenType openType);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    protected Window createWindowFromTemplate(String template, Map params) {
        Document document = parseDescriptor(template, params);
        final Element rootElement = document.getRootElement();

        final DsContextLoader dsContextLoader = new DsContextLoader(new DatasourceFactoryImpl());
        final DsContext dsContext = dsContextLoader.loadDatasources(rootElement.element("dsContext"));

        final LayoutLoader layoutLoader = new LayoutLoader(createComponentFactory(), LayoutLoaderConfig.getWindowLoaders(), dsContext);
        layoutLoader.setLocale(getLocale());

        final Window window = (Window) layoutLoader.loadComponent(rootElement);

        for (Datasource ds : dsContext.getAll()) {
            final Object value = params.get(ds.getId());
            if (value != null) {
                ds.setItem(value);
            }

            if (ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }

        dsContext.setContext(new Context() {
            public <T> T getValue(String property) {
                final Component component = window.getComponent(property);
                if (component instanceof Component.Field) {
                    return ((Component.Field) component).<T>getValue();
                } else {
                    return null;
                }
            }

            public void setValue(String property, Object value) {
                final Component component = window.getComponent(property);
                if (component instanceof Component.Field) {
                    ((Component.Field) component).setValue(value);
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

    protected abstract Locale getLocale();
    protected abstract ComponentsFactory createComponentFactory();

    protected Window wrapByCustomClass(Window window, Element element) {
        Window res = window;
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            try {
                final Class<?> aClass = Class.forName(screenClass);
                final Constructor<?> constructor = aClass.getConstructor(Window.class);
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

    private Map<String, Document> descriptorsCache = new HashMap<String, Document>();

    protected boolean isDescriptorCacheEnabled() {
        return false;
    }

    protected Document parseDescriptor(String template, Map params) {
        Document document = descriptorsCache.get(template);
        if (document == null || !isDescriptorCacheEnabled()) {
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

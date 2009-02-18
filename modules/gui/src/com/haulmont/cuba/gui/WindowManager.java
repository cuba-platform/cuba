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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.DatasourceFactoryImpl;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.xml.data.DsContextLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.config.ScreenInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WindowManager {
    private DataService defaultDataService;

    public synchronized DataService getDefaultDataService() {
        if (defaultDataService == null) {
            defaultDataService = createDefaultDataService();
        }
        return defaultDataService;
    }

    protected abstract DataService createDefaultDataService();

    public enum OpenType {
        NEW_TAB,
        THIS_TAB,
        DIALOG
    }

    protected Window createWindow(String template, Map<String, Object> params, LayoutLoaderConfig layoutConfig) {
        StopWatch stopWatch = new Log4JStopWatch("WindowManager.createWindow");

        StopWatch parseDescriptorStopWatch = new Log4JStopWatch("WindowManager.createWindow (parseDescriptor)");
        Document document = parseDescriptor(template, params, true);
        parseDescriptorStopWatch.stop();

        final Element element = document.getRootElement();

        StopWatch deployViewsStopWatch = new Log4JStopWatch("WindowManager.createWindow (deployViews)");
        deployViews(document);
        deployViewsStopWatch.stop();

        StopWatch loadDsContextStopWatch = new Log4JStopWatch("WindowManager.createWindow (loadDsContext)");
        final DsContext dsContext = loadDsContext(element);
        loadDsContextStopWatch.stop();

        final ComponentLoaderContext componentLoaderContext = new ComponentLoaderContext(dsContext);

        StopWatch loadLayoutStopWatch = new Log4JStopWatch("WindowManager.createWindow (loadLayout)");
        final Window window = loadLayout(element, componentLoaderContext, layoutConfig);
        loadLayoutStopWatch.stop();

        componentLoaderContext.setWindow(window);
        initialize(window, dsContext, params);

        final Window wrapedWindow = wrapByCustomClass(window, element, params);
        componentLoaderContext.setWindow(wrapedWindow);
        componentLoaderContext.executeLazyTasks();
        stopWatch.stop();

        return wrapedWindow;
    }

    protected void deployViews(Document document) {
        final Element metadataContextElement = document.getRootElement().element("metadataContext");
        if (metadataContextElement != null) {
            List<Element> fileElements = metadataContextElement.elements("deployViews");
            for (Element fileElement : fileElements) {
                final String resource = fileElement.attributeValue("name");
                MetadataProvider.getViewRepository().deployViews(getClass().getResourceAsStream(resource));
            }

            List<Element> viewElements = metadataContextElement.elements("view");
            for (Element viewElement : viewElements) {
                MetadataProvider.getViewRepository().deployView(metadataContextElement, viewElement);
            }
        }
    }

    protected void initialize(final Window window, DsContext dsContext, Map<String, Object> params) {
        window.setDsContext(dsContext);

        for (Datasource ds : dsContext.getAll()) {
            if (ds instanceof DatasourceImplementation) {
                ((DatasourceImplementation) ds).initialized();
            }
        }

        dsContext.setContext(new WindowContext(window));
    }

    protected Window loadLayout(Element rootElement, ComponentLoader.Context context, LayoutLoaderConfig layoutConfig) {
        final LayoutLoader layoutLoader = new LayoutLoader(context, createComponentFactory(), layoutConfig);
        layoutLoader.setLocale(getLocale());

        final Window window = (Window) layoutLoader.loadComponent(rootElement);
        return window;
    }

    protected DsContext loadDsContext(Element rootElement) {
        final DsContextLoader dsContextLoader = new DsContextLoader(new DatasourceFactoryImpl(), getDefaultDataService());
        final DsContext dsContext = dsContextLoader.loadDatasources(rootElement.element("dsContext"));

        return dsContext;
    }

    protected Window createWindow(Class aclass, Map params) {
        try {
            final Window window = (Window) aclass.newInstance();
            try {
                invokeMethod(window, "init", params);
            } catch (NoSuchMethodException e) {
                invokeMethod(window, "init");
            }
            return window;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Window> T openWindow(ScreenInfo windowInfo, WindowManager.OpenType openType, Map<String, Object> params)
    {
        params = createParametersMap(windowInfo, params);
        String template = windowInfo.getTemplate();
        if (template != null) {
            return (T) __openWindow(template, openType, params);
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null)
                return (T) __openWindow(screenClass, openType, params);
            else
                return null;
        }
    }

    protected <T extends Window> T __openWindow(String descriptor, WindowManager.OpenType openType, Map<String, Object> params) {
        Window window = createWindow(descriptor, params, LayoutLoaderConfig.getWindowLoaders());

        String caption = loadCaption(window, params);

        showWindow(window, caption, openType);
        return (T) window;
    }

    protected <T extends Window> String loadCaption(Window window, Map<String, Object> params) {
        String caption = window.getCaption();
        if (!StringUtils.isEmpty(caption)) return caption;

        caption = (String) params.get("caption");
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
        } else {
            try {
                caption = invokeMethod(window, "getCaption");
                if (!StringUtils.isEmpty(caption)) {
                    caption = TemplateHelper.processTemplate(caption, params);
                }
            } catch (NoSuchMethodException e) {
                // Do nothing
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        window.setCaption(caption);

        return caption;
    }

    protected <T extends Window> T __openWindow(Class aclass, WindowManager.OpenType openType, Map<String, Object> params) {
        Window window = createWindow(aclass, params);

        String caption = loadCaption(window, params);

        showWindow(window, caption, openType);
        return (T) window;
    }

    public <T extends Window> T openEditor(ScreenInfo windowInfo, Object item, OpenType openType, Map<String, Object> params) {
        params = createParametersMap(windowInfo, params);
        params.put("parameter$item", item instanceof Datasource ? ((Datasource) item).getItem() : item);

        String template = windowInfo.getTemplate();
        Window window;
        if (template != null) {
            window = createWindow(template, params, LayoutLoaderConfig.getEditorLoaders());
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null)
                window = createWindow(screenClass, params);
            else
                throw new IllegalStateException("Invalid ScreenInfo: " + windowInfo);
        }
        ((Window.Editor) window).setItem(item);

        String caption = loadCaption(window, params);

        showWindow(window, caption, openType);
        return (T) window;
    }

    public <T extends Window> T openLookup(
            ScreenInfo windowInfo, Window.Lookup.Handler handler,
                OpenType openType, Map<String, Object> params)
    {
        params = createParametersMap(windowInfo, params);

        String template = windowInfo.getTemplate();
        Window window;
        if (template != null) {
            window = createWindow(template, params, LayoutLoaderConfig.getEditorLoaders());
        } else {
            Class screenClass = windowInfo.getScreenClass();
            if (screenClass != null)
                window = createWindow(screenClass, params);
            else
                throw new IllegalStateException("Invalid ScreenInfo: " + windowInfo);
        }

        final Element element = ((Component.HasXmlDescriptor) window).getXmlDescriptor();
        final String lookupComponent = element.attributeValue("lookupComponent");
        if (!StringUtils.isEmpty(lookupComponent)) {
            final Component component = window.getComponent(lookupComponent);
            ((Window.Lookup) window).setLookupComponent(component);
        }
        ((Window.Lookup) window).setLookupHandler(handler);

        String caption = loadCaption(window, params);

        showWindow(window, caption, openType);

        return (T) window;
    }

    protected Map<String, Object> createParametersMap(ScreenInfo windowInfo, Map<String, Object> params) {
        final Map<String, Object> map = new HashMap<String, Object>(params.size());

        final Element element = windowInfo.getDescriptor();
        if (element != null) {
            final Element paramsElement = element.element("params");
            if (paramsElement != null) {
                final List<Element> paramElements = paramsElement.elements("param");
                for (Element paramElement : paramElements) {
                    final String name = paramElement.attributeValue("name");
                    final String value = paramElement.attributeValue("value");

                    map.put("parameter$" + name, value);
                }
            }
        }


        for (Map.Entry<String, Object> entry : params.entrySet()) {
            map.put("parameter$" + entry.getKey(), entry.getValue());
        }

        return map;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public <T extends Window> T openEditor(ScreenInfo windowInfo, Object item, OpenType openType) {
        return (T)openEditor(windowInfo, item, openType, Collections.<String, Object>emptyMap());
    }

    public <T extends Window> T openWindow(ScreenInfo windowInfo, OpenType openType) {
        return (T)openWindow(windowInfo, openType, Collections.<String, Object>emptyMap());
    }

    public <T extends Window> T openLookup(ScreenInfo windowInfo, Window.Lookup.Handler handler, OpenType openType) {
        return (T)openLookup(windowInfo, handler, openType, Collections.<String, Object>emptyMap());
    }

    protected abstract void showWindow(Window window, String caption, OpenType openType);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract Locale getLocale();
    protected abstract ComponentsFactory createComponentFactory();

    protected Window wrapByCustomClass(Window window, Element element, Map<String, Object> params) {
        Window res = window;
        final String screenClass = element.attributeValue("class");
        if (!StringUtils.isBlank(screenClass)) {
            try {
                final Class<?> aClass = Class.forName(screenClass);
                Constructor<?> constructor;
                try {
                    constructor = aClass.getConstructor(Window.class);
                } catch (NoSuchMethodException e) {
                    constructor = aClass.getConstructor(IFrame.class);
                }

                res = (Window) constructor.newInstance(window);

                try {
                    invokeMethod(res, "init", params);
                } catch (NoSuchMethodException e) {
                    invokeMethod(res, "init");
                }
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

    private static final Pattern DS_CONTEXT_PATTERN = Pattern.compile("<dsContext>(\\p{ASCII}+)</dsContext>");

    protected Document parseDescriptor(String resourcePath, Map<String, Object> params, boolean isTemplate) {
        Document document;
        if (isTemplate) {
            final InputStream stream = getClass().getResourceAsStream(resourcePath);
            try {
                String template = IOUtils.toString(stream);
                Matcher matcher = DS_CONTEXT_PATTERN.matcher(template);
                if (matcher.find()) {
                    final String dsContext = matcher.group(1);

                    template = DS_CONTEXT_PATTERN.matcher(template).replaceFirst("");
                    template = TemplateHelper.processTemplate(template, params);

                    document = loadDocument(template);
                    final Document dsContextDocument = loadDocument("<dsContext>" + dsContext + "</dsContext>");
                    document.getRootElement().add(dsContextDocument.getRootElement());
                } else {
                    template = TemplateHelper.processTemplate(template, params);
                    document = loadDocument(template);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            document = descriptorsCache.get(resourcePath);
            if (document == null || !isDescriptorCacheEnabled()) {
                final InputStream stream = getClass().getResourceAsStream(resourcePath);

                SAXReader reader = new SAXReader();
                try {
                    document = reader.read(stream);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }

                descriptorsCache.put(resourcePath, document);
            }
        }

        return document;
    }

    protected Document loadDocument(String template) {
        Document document;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new StringReader(template));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return document;
    }

    protected <T> T invokeMethod(Window window, String name) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        final Class<? extends Window> aClass = window.getClass();
        Method method;
        try {
            method = aClass.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            method = aClass.getMethod(name);
        }
        method.setAccessible(true);

        return (T) method.invoke(window);
    }

    protected <T> T invokeMethod(Window window, String name, Object...params) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Class> paramClasses = new ArrayList<Class>();
        for (Object param : params) {
            if (param == null) throw new IllegalStateException("Null parameter");

            final Class<? extends Object> aClass = param.getClass();
            if (List.class.isAssignableFrom(aClass)) {
                paramClasses.add(List.class);
            } else if (Set.class.isAssignableFrom(aClass)) {
                paramClasses.add(Set.class);
            } else if (Map.class.isAssignableFrom(aClass)) {
                paramClasses.add(Map.class);
            } else {
                paramClasses.add(aClass);
            }
        }

        final Class<? extends Window> aClass = window.getClass();
        Method method;
        try {
            method = aClass.getDeclaredMethod(name, paramClasses.toArray(new Class<?>[]{}));
        } catch (NoSuchMethodException e) {
            method = aClass.getMethod(name, paramClasses.toArray(new Class<?>[]{}));
        }
        method.setAccessible(true);
        return (T) method.invoke(window, params);
    }

    protected static class ComponentLoaderContext implements ComponentLoader.Context {
        protected DsContext dsContext;
        protected Window window;

        protected List<ComponentLoader.LazyTask> lazyTasks = new ArrayList<ComponentLoader.LazyTask>();

        public ComponentLoaderContext(DsContext dsContext) {
            this.dsContext = dsContext;
        }

        public DsContext getDSContext() {
            return dsContext;
        }

        public Window getWindow() {
            return window;
        }

        public void setWindow(Window window) {
            this.window = window;
        }

        public void addLazyTask(ComponentLoader.LazyTask task) {
            lazyTasks.add(task);
        }

        public void executeLazyTasks() {
            for (ComponentLoader.LazyTask task : lazyTasks) {
                task.execute(this, window);
            }
        }
    }

}

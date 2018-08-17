/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.loaders.FrameLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.WindowLoader;
import org.dom4j.Element;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(LayoutLoader.NAME)
public class LayoutLoader {

    public static final String NAME = "cuba_LayoutLoader";

    protected ComponentLoader.Context context;
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    protected Locale locale;
    protected String messagesPack;

    protected BeanLocator beanLocator;

    public LayoutLoader(ComponentLoader.Context context) {
        this.context = context;
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Inject
    public void setFactory(ComponentsFactory factory) {
        this.factory = factory;
    }

    @Inject
    public void setConfig(LayoutLoaderConfig config) {
        this.config = config;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String messagesPack) {
        this.messagesPack = messagesPack;
    }

    protected ComponentLoader getLoader(Element element) {
        Class<? extends ComponentLoader> loaderClass = config.getLoader(element.getName());
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + element.getName(), context.getFullFrameId());
        }

        return initLoader(element, loaderClass);
    }

    protected WindowLoader getWindowLoader(Element element) {
        Class<? extends ComponentLoader> loaderClass = config.getWindowLoader();

        return (WindowLoader) initLoader(element, loaderClass);
    }

    protected ComponentLoader initLoader(Element element, Class<? extends ComponentLoader> loaderClass) {
        ComponentLoader loader;

        Constructor<? extends ComponentLoader> constructor;
        try {
            constructor = loaderClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new GuiDevelopmentException("Unable to get constructor for loader: " + e, context.getFullFrameId());
        }

        try {
            loader = constructor.newInstance();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new GuiDevelopmentException("Loader instantiation error: " + e, context.getFullFrameId());
        }

        loader.setBeanLocator(beanLocator);

        loader.setLocale(locale);
        loader.setMessagesPack(messagesPack);
        loader.setContext(context);
        loader.setLayoutLoaderConfig(config);
        loader.setFactory(factory);
        loader.setElement(element);

        return loader;
    }

    public Pair<ComponentLoader, Element> createFrameComponent(String resourcePath, String id,
                                                               Map<String, Object> params) {
        ScreenXmlLoader screenXmlLoader = AppBeans.get(ScreenXmlLoader.class); // todo use injection
        Element element = screenXmlLoader.load(resourcePath, id, params);

        ComponentLoader loader = getLoader(element);
        FrameLoader frameLoader = (FrameLoader) loader;
        frameLoader.setFrameId(id);

        loader.createComponent();

        return new Pair<>(loader, element);
    }

    public ComponentLoader createComponent(Element element) {
        ComponentLoader loader = getLoader(element);

        loader.createComponent();
        return loader;
    }

    public ComponentLoader createWindow(Element element, String windowId) {
        ComponentLoader loader = getLoader(element);
        ((WindowLoader) loader).setWindowId(windowId);

        loader.createComponent();
        return loader;
    }

    public ComponentLoader createWindowContent(Window window, Element element, String windowId) {
        WindowLoader windowLoader = getWindowLoader(element);

        windowLoader.setWindowId(windowId);
        windowLoader.setResultComponent(window);

        Element layout = element.element("layout");
        if (layout != null) {
            windowLoader.createContent(layout);
        }
        return windowLoader;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
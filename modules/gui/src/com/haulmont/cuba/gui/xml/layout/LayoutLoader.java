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
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.xml.layout.loaders.FrameLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.WindowLoader;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;

public class LayoutLoader {

    protected ComponentLoader.Context context;
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    protected Locale locale;
    protected String messagesPack;

    public LayoutLoader(ComponentLoader.Context context, ComponentsFactory factory, LayoutLoaderConfig config) {
        this.context = context;
        this.factory = factory;
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

        ComponentLoader loader;
        try {
            Constructor<? extends ComponentLoader> constructor = loaderClass.getConstructor();
            loader = constructor.newInstance();

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
            loader.setContext(context);
            loader.setLayoutLoaderConfig(config);
            loader.setFactory(factory);
            loader.setElement(element);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new GuiDevelopmentException("Loader instantiation error: " + e, context.getFullFrameId());
        }

        return loader;
    }

    public Pair<ComponentLoader, Element> createFrameComponent(String resourcePath, String id, Map<String, Object> params) {
        ScreenXmlLoader screenXmlLoader = AppBeans.get(ScreenXmlLoader.class);
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
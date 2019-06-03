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

import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Fragment;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.loaders.FragmentLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.WindowLoader;
import org.dom4j.Element;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Component(LayoutLoader.NAME)
public class LayoutLoader {

    public static final String NAME = "cuba_LayoutLoader";

    protected ComponentLoader.Context context;
    protected UiComponents factory;
    protected LayoutLoaderConfig config;

    protected BeanLocator beanLocator;

    public LayoutLoader(ComponentLoader.Context context) {
        this.context = context;
    }

    @Inject
    protected void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Inject
    protected void setFactory(UiComponents factory) {
        this.factory = factory;
    }

    @Inject
    protected void setConfig(LayoutLoaderConfig config) {
        this.config = config;
    }

    protected ComponentLoader getLoader(Element element) {
        Class<? extends ComponentLoader> loaderClass = config.getLoader(element.getName());
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + element.getName(), context);
        }

        return initLoader(element, loaderClass);
    }

    protected FragmentLoader getFragmentLoader(Element element) {
        Class<? extends ComponentLoader> loaderClass = config.getFragmentLoader();

        return (FragmentLoader) initLoader(element, loaderClass);
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
            throw new GuiDevelopmentException("Unable to get constructor for loader: " + e, context);
        }

        try {
            loader = constructor.newInstance();
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new GuiDevelopmentException("Loader instantiation error: " + e, context);
        }

        loader.setBeanLocator(beanLocator);

        loader.setContext(context);
        loader.setLayoutLoaderConfig(config);
        loader.setFactory(factory);
        loader.setElement(element);

        return loader;
    }

    public ComponentLoader createComponent(Element element) {
        ComponentLoader loader = getLoader(element);

        loader.createComponent();
        return loader;
    }

    public ComponentLoader<Fragment> createFragmentContent(Fragment fragment, Element rootWindowElement) {
        FragmentLoader fragmentLoader = getFragmentLoader(rootWindowElement);
        fragmentLoader.setResultComponent(fragment);

        Element layout = rootWindowElement.element("layout");
        if (layout != null) {
            fragmentLoader.createContent(layout);
        }

        return fragmentLoader;
    }

    public ComponentLoader<Window> createWindowContent(Window window, Element rootWindowElement) {
        WindowLoader windowLoader = getWindowLoader(rootWindowElement);
        windowLoader.setResultComponent(window);

        Element layout = rootWindowElement.element("layout");
        if (layout != null) {
            windowLoader.createContent(layout);
        }
        return windowLoader;
    }

    public ComponentLoader getLoader(Element element, String name) {
        Class<? extends ComponentLoader> loaderClass = config.getLoader(name);
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + name, context);
        }

        return initLoader(element, loaderClass);
    }

    public ComponentLoader getLoader(Element element, Class<? extends ComponentLoader> loaderClass) {
        return initLoader(element, loaderClass);
    }
}
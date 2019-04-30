/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.xml.layout;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component(CompositeComponentLayoutLoader.NAME)
public class CompositeComponentLayoutLoader {

    public static final String NAME = "cuba_CompositeComponentLayoutLoader";

    public static final String COMPOSITE_COMPONENT_ELEMENT_NAME = "composite";

    protected ComponentLoader.Context context;
    protected UiComponents factory;
    protected LayoutLoaderConfig config;

    protected Locale locale;
    protected String messagesPack;

    protected BeanLocator beanLocator;

    public CompositeComponentLayoutLoader(ComponentLoader.Context context) {
        this.context = context;
    }

    @Inject
    public void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Inject
    public void setFactory(UiComponents factory) {
        this.factory = factory;
    }

    @Inject
    public void setConfig(LayoutLoaderConfig config) {
        this.config = config;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getMessagesPack() {
        return messagesPack;
    }

    public void setMessagesPack(String messagesPack) {
        this.messagesPack = messagesPack;
    }

    protected ComponentLoader getLoader(Element element) {
        if (COMPOSITE_COMPONENT_ELEMENT_NAME.equals(element.getName())) {
            List<Element> elements = element.elements();
            Preconditions.checkArgument(elements.size() == 1,
                    "%s must contain a single root element", COMPOSITE_COMPONENT_ELEMENT_NAME);
            element = elements.get(0);
        }

        Class<? extends ComponentLoader> loaderClass = config.getLoader(element.getName());
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + element.getName(), context);
        }

        return initLoader(element, loaderClass);
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

        loader.setLocale(locale);
        loader.setMessagesPack(messagesPack);
        loader.setContext(context);
        loader.setLayoutLoaderConfig(config);
        loader.setFactory(factory);
        loader.setElement(element);

        return loader;
    }

    public Component createComponent(Element element) {
        ComponentLoader loader = getLoader(element);

        loader.createComponent();
        loader.loadComponent();
        return loader.getResultComponent();
    }
}

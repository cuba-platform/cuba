/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Gennady Pavlov
 * Created: 08.06.2010 14:19:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PopupButton;
import com.haulmont.cuba.gui.xml.layout.*;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Iterator;
import java.lang.reflect.Constructor;

public class PopupButtonLoader extends ButtonLoader {
    protected LayoutLoaderConfig config;
    protected ComponentsFactory factory;

    public PopupButtonLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        PopupButton component = (PopupButton) super.loadComponent(factory, element, parent);

        if (element.elementIterator().hasNext()) {
            Element popupElement = (Element) element.elementIterator().next();
            Component popupComponent = getLoader(popupElement.getName()).loadComponent(factory, popupElement, component);
            component.setPopupComponent(popupComponent);
        }

        String menuWidth = element.attributeValue("menuWidth");
        if (!StringUtils.isEmpty(menuWidth)) {
            component.setMenuWidth(menuWidth);
        }

        return component;
    }

    protected com.haulmont.cuba.gui.xml.layout.ComponentLoader getLoader(String name) throws IllegalAccessException, InstantiationException {
        Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loaderClass = config.getLoader(name);
        if (loaderClass == null) {
            throw new IllegalStateException(String.format("Unknown component '%s'", name));
        }

        com.haulmont.cuba.gui.xml.layout.ComponentLoader loader;
        try {
            final Constructor<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> constructor =
                    loaderClass.getConstructor(Context.class, LayoutLoaderConfig.class, ComponentsFactory.class);
            loader = constructor.newInstance(context, config, factory);

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (Throwable e) {
            loader = loaderClass.newInstance();
            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        }

        return loader;
    }
}

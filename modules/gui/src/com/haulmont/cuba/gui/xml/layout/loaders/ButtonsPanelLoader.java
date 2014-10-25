/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public class ButtonsPanelLoader extends ContainerLoader {
    public ButtonsPanelLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final ButtonsPanel component = factory.createComponent(element.getName());

        initComponent(component, element, factory);

        return component;
    }

    protected void initComponent(ButtonsPanel component, Element element, ComponentsFactory factory) {
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);
        loadAlign(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        if (!element.elements().isEmpty()) {
            loadSubComponents(component, element, "visible");
        } else {
            String className = element.attributeValue("providerClass");
            if (className != null) {
                final Class<ButtonsPanel.Provider> clazz = ReflectionHelper.getClass(className);

                try {
                    final Constructor<ButtonsPanel.Provider> constructor = clazz.getConstructor();
                    final ButtonsPanel.Provider instance = constructor.newInstance();
                    applyButtonsProvider(factory, component, instance);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }

            } else {
                throw new GuiDevelopmentException("'buttonsPanel' element must contain 'class' attribute or at least one 'button' element",
                        context.getFullFrameId());
            }
        }

        assignFrame(component);
    }

    protected void applyButtonsProvider(ComponentsFactory factory, ButtonsPanel panel, ButtonsPanel.Provider buttonsProvider)
            throws IllegalAccessException, InstantiationException {

        Collection<Component> buttons = buttonsProvider.getButtons();
        for (final Component button : buttons) {
            panel.add(button);
        }
    }
}
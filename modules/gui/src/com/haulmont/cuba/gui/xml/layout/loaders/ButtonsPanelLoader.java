/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;

import java.lang.reflect.Constructor;
import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public class ButtonsPanelLoader extends ContainerLoader<ButtonsPanel> {

    protected void applyButtonsProvider(ButtonsPanel panel, ButtonsPanel.Provider buttonsProvider)
            throws IllegalAccessException, InstantiationException {

        Collection<Component> buttons = buttonsProvider.getButtons();
        for (Component button : buttons) {
            panel.add(button);
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (ButtonsPanel) factory.createComponent(ButtonsPanel.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadId(resultComponent, element);
        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        if (!element.elements().isEmpty()) {
            loadSubComponents();
        } else {
            String className = element.attributeValue("providerClass");
            if (className != null) {
                Class<ButtonsPanel.Provider> clazz = ReflectionHelper.getClass(className);

                try {
                    Constructor<ButtonsPanel.Provider> constructor = clazz.getConstructor();
                    ButtonsPanel.Provider instance = constructor.newInstance();
                    applyButtonsProvider(resultComponent, instance);
                } catch (Throwable e) {
                    throw new RuntimeException("Unable to apply buttons provider", e);
                }
            } else {
                throw new GuiDevelopmentException("'buttonsPanel' element must contain 'class' attribute or at least one 'button' element",
                        context.getFullFrameId());
            }
        }
    }
}
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
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.util.Collection;

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
            if (StringUtils.isNotEmpty(className)) {
                Class<ButtonsPanel.Provider> clazz = ReflectionHelper.getClass(className);

                try {
                    Constructor<ButtonsPanel.Provider> constructor = clazz.getConstructor();
                    ButtonsPanel.Provider instance = constructor.newInstance();
                    applyButtonsProvider(resultComponent, instance);
                } catch (Throwable e) {
                    throw new RuntimeException("Unable to apply buttons provider", e);
                }
            }
        }
    }
}
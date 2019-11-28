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
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.Supplier;

public class ButtonsPanelLoader extends ContainerLoader<ButtonsPanel> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(ButtonsPanel.NAME);
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

        loadSpacing(resultComponent, element);
        loadMargin(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);
        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);

        if (!element.elements().isEmpty()) {
            loadSubComponents();
        } else {
            String className = element.attributeValue("providerClass");
            if (StringUtils.isNotEmpty(className)) {
                Class<Supplier<Collection<Component>>> clazz = ReflectionHelper.getClass(className);

                Supplier<Collection<Component>> instance;
                try {
                    Constructor<Supplier<Collection<Component>>> constructor = clazz.getConstructor();
                    instance = constructor.newInstance();
                } catch (NoSuchMethodException | InstantiationException
                        | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException("Unable to apply buttons provider", e);
                }

                applyButtonsProvider(resultComponent, instance);
            }
        }
    }

    protected void applyButtonsProvider(ButtonsPanel panel, Supplier<Collection<Component>> buttonsProvider) {
        Collection<Component> buttons = buttonsProvider.get();
        for (Component button : buttons) {
            panel.add(button);
        }
    }
}
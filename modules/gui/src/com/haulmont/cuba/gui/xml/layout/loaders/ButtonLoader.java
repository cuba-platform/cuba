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

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.xml.DeclarativeAction;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 */
public class ButtonLoader extends AbstractComponentLoader<Button> {

    protected void loadInvoke(Button component, boolean enabled, boolean visible, Element element) {
        if (!StringUtils.isBlank(element.attributeValue("action"))) {
            return;
        }

        final String methodName = element.attributeValue("invoke");
        if (StringUtils.isBlank(methodName)) {
            return;
        }

        String actionBaseId = component.getId();
        if (StringUtils.isEmpty(actionBaseId)) {
            actionBaseId = methodName;
        }

        DeclarativeAction action = new DeclarativeAction(actionBaseId + "_invoke",
                component.getCaption(), component.getDescription(), component.getIcon(),
                enabled, visible,
                methodName,
                component.getFrame()
        );
        component.setAction(action);
    }

    @Override
    public void createComponent() {
        resultComponent = (Button) factory.createComponent(Button.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        boolean enabled = loadEnable(resultComponent, element);
        boolean visible = loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadAction(resultComponent, element);
        loadIcon(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);
        loadAlign(resultComponent, element);

        loadInvoke(resultComponent, enabled, visible, element);
    }
}
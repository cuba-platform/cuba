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

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import org.apache.commons.lang.StringUtils;

/**
 */
public class LabelLoader extends AbstractDatasourceComponentLoader<Label> {
    @Override
    public void createComponent() {
        resultComponent = (Label) factory.createComponent(Label.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadDatasource(resultComponent, element);

        loadVisible(resultComponent, element);
        loadAlign(resultComponent, element);
        loadStyleName(resultComponent, element);

        String htmlEnabled = element.attributeValue("htmlEnabled");
        if (StringUtils.isNotEmpty(htmlEnabled)) {
            resultComponent.setHtmlEnabled(Boolean.parseBoolean(htmlEnabled));
        }

        String caption = element.attributeValue("value");
        if (StringUtils.isNotEmpty(caption)) {
            caption = loadResourceString(caption);
            resultComponent.setValue(caption);
        }
        
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadHeight(resultComponent, element, Component.AUTO_SIZE);

        resultComponent.setFormatter(loadFormatter(element));
    }
}
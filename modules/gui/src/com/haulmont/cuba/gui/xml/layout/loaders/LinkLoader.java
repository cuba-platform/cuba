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
import com.haulmont.cuba.gui.components.Link;
import org.apache.commons.lang.StringUtils;

public class LinkLoader extends AbstractComponentLoader<Link> {
    @Override
    public void createComponent() {
        resultComponent = (Link) factory.createComponent(Link.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadAlign(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadDescription(resultComponent, element);
        loadCaption(resultComponent, element);

        String url = element.attributeValue("url");
        if (StringUtils.isNotEmpty(url)) {
            resultComponent.setUrl(url);
        }

        String target = element.attributeValue("target");
        if (StringUtils.isNotEmpty(target)) {
            resultComponent.setTarget(target);
        }

        String icon = element.attributeValue("icon");
        if (StringUtils.isNotEmpty(icon)) {
            resultComponent.setIcon(icon);
        }

        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadHeight(resultComponent, element, Component.AUTO_SIZE);
    }
}
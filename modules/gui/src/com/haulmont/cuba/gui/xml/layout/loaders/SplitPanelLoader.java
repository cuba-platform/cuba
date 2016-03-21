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
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.components.VBoxLayout;
import org.apache.commons.lang.StringUtils;

/**
 */
public class SplitPanelLoader extends ContainerLoader<SplitPanel> {

    @Override
    public void createComponent() {
        resultComponent = (SplitPanel) factory.createComponent(SplitPanel.NAME);
        loadId(resultComponent, element);

        String orientation = element.attributeValue("orientation");
        if (StringUtils.isEmpty(orientation)) {
            resultComponent.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            resultComponent.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("horizontal".equalsIgnoreCase(orientation)) {
            resultComponent.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        }

        createSubComponents(resultComponent, element);
        if (resultComponent.getOwnComponents().size() == 1) {
            resultComponent.add(factory.createComponent(VBoxLayout.NAME));
        }
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadStyleName(resultComponent, element);

        String pos = element.attributeValue("pos");
        if (!StringUtils.isEmpty(pos)) {
            boolean reversePosition = false;
            if (Boolean.parseBoolean(element.attributeValue("reversePosition"))) {
                reversePosition = true;
            }

            if (pos.endsWith("px")) {
                resultComponent.setSplitPosition(Integer.parseInt(pos.substring(0, pos.indexOf("px"))), 0, reversePosition);
            } else {
                resultComponent.setSplitPosition(Integer.parseInt(pos), 8, reversePosition);
            }
        }

        String maxSplitPosition = element.attributeValue("maxSplitPosition");
        if (!StringUtils.isEmpty(maxSplitPosition)) {
            if (maxSplitPosition.endsWith("px")) {
                resultComponent.setMaxSplitPosition(Integer.parseInt(maxSplitPosition.substring(0, maxSplitPosition.indexOf("px"))), 0);
            } else {
                resultComponent.setMaxSplitPosition(Integer.parseInt(maxSplitPosition.substring(0, maxSplitPosition.indexOf("px"))), 8);
            }
        }

        String minSplitPosition = element.attributeValue("minSplitPosition");
        if (!StringUtils.isEmpty(minSplitPosition)) {
            if (minSplitPosition.endsWith("px")) {
                resultComponent.setMinSplitPosition(Integer.parseInt(minSplitPosition.substring(0, minSplitPosition.indexOf("px"))), 0);
            } else {
                resultComponent.setMinSplitPosition(Integer.parseInt(minSplitPosition.substring(0, minSplitPosition.indexOf("%"))), 8);
            }
        }

        String locked = element.attributeValue("locked");
        if (!StringUtils.isEmpty(locked)) {
            resultComponent.setLocked(Boolean.parseBoolean(locked));
        }

        loadHeight(resultComponent, element, Component.AUTO_SIZE);
        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadAlign(resultComponent, element);

        loadSubComponents();
    }
}
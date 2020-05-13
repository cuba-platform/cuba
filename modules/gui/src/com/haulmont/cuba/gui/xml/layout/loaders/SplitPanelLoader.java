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

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.SizeUnit;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.components.VBoxLayout;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class SplitPanelLoader extends ContainerLoader<SplitPanel> {

    @Override
    public void createComponent() {
        resultComponent = factory.create(SplitPanel.NAME);
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
            resultComponent.add(factory.create(VBoxLayout.NAME));
        }
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadStyleName(resultComponent, element);

        loadHtmlSanitizerEnabled(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadContextHelp(resultComponent, element);

        loadSettingsEnabled(resultComponent, element);

        loadSplitPosition(resultComponent, element);
        loadMaxSplitPosition(resultComponent, element);
        loadMinSplitPosition(resultComponent, element);

        loadLocked(resultComponent, element);

        loadDockable(resultComponent, element);
        loadDockMode(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);

        loadSubComponents();
    }

    protected void loadDockable(SplitPanel resultComponent, Element element) {
        String dockable = element.attributeValue("dockable");

        if (dockable == null || dockable.isEmpty()) {
            return;
        }

        boolean bDockable = Boolean.parseBoolean(dockable);
        resultComponent.setDockable(bDockable);
    }

    protected void loadDockMode(SplitPanel resultComponent, Element element) {
        String dockMode = element.attributeValue("dockMode");

        if (dockMode == null || dockMode.isEmpty()) {
            return;
        }

        SplitPanel.DockMode mode = SplitPanel.DockMode.valueOf(dockMode);

        if (resultComponent.getOrientation() == SplitPanel.ORIENTATION_VERTICAL
            && (mode == SplitPanel.DockMode.LEFT || mode == SplitPanel.DockMode.RIGHT)) {
            throw new GuiDevelopmentException("Dock mode " + mode.name() + " cannot be enabled for vertically oriented SplitPanel", context);
        } else if (resultComponent.getOrientation() == SplitPanel.ORIENTATION_HORIZONTAL
                && (mode == SplitPanel.DockMode.TOP || mode == SplitPanel.DockMode.BOTTOM)) {
            throw new GuiDevelopmentException("Dock mode " + mode.name() + " cannot be enabled for horizontally oriented SplitPanel", context);
        }

        resultComponent.setDockMode(mode);
    }

    protected void loadLocked(SplitPanel resultComponent, Element element) {
        String locked = element.attributeValue("locked");
        if (!StringUtils.isEmpty(locked)) {
            resultComponent.setLocked(Boolean.parseBoolean(locked));
        }
    }

    protected void loadMinSplitPosition(SplitPanel resultComponent, Element element) {
        String minSplitPosition = element.attributeValue("minSplitPosition");
        if (!StringUtils.isEmpty(minSplitPosition)) {
            int position;
            SizeUnit unit;

            if (minSplitPosition.endsWith("px")) {
                position = Integer.parseInt(minSplitPosition.substring(0, minSplitPosition.indexOf("px")));
                unit = SizeUnit.PIXELS;
            } else if (minSplitPosition.endsWith("%")) {
                position = Integer.parseInt(minSplitPosition.substring(0, minSplitPosition.indexOf("%")));
                unit = SizeUnit.PERCENTAGE;
            } else {
                throw new GuiDevelopmentException("Unit of minSplitPosition is not set", context);
            }

            resultComponent.setMinSplitPosition(position, unit);
        }
    }

    protected void loadMaxSplitPosition(SplitPanel resultComponent, Element element) {
        String maxSplitPosition = element.attributeValue("maxSplitPosition");
        if (!StringUtils.isEmpty(maxSplitPosition)) {
            int position;
            SizeUnit unit;

            if (maxSplitPosition.endsWith("px")) {
                position = Integer.parseInt(maxSplitPosition.substring(0, maxSplitPosition.indexOf("px")));
                unit = SizeUnit.PIXELS;
            } else if (maxSplitPosition.endsWith("%")) {
                position = Integer.parseInt(maxSplitPosition.substring(0, maxSplitPosition.indexOf("%")));
                unit = SizeUnit.PERCENTAGE;
            } else {
                throw new GuiDevelopmentException("Unit of maxSplitPosition is not set", context);
            }

            resultComponent.setMaxSplitPosition(position, unit);
        }
    }

    protected void loadSplitPosition(SplitPanel resultComponent, Element element) {
        String pos = element.attributeValue("pos");
        if (!StringUtils.isEmpty(pos)) {
            boolean reversePosition = false;
            int position;
            SizeUnit unit;

            if (Boolean.parseBoolean(element.attributeValue("reversePosition"))) {
                reversePosition = true;
            }

            if (pos.endsWith("px")) {
                position = Integer.parseInt(pos.substring(0, pos.indexOf("px")));
                unit = SizeUnit.PIXELS;
            } else {
                if (pos.endsWith("%")) {
                    position = Integer.parseInt(pos.substring(0, pos.indexOf("%")));
                } else {
                    position = Integer.parseInt(pos);
                }
                unit = SizeUnit.PERCENTAGE;
            }

            resultComponent.setSplitPosition(position, unit, reversePosition);
        }
    }
}
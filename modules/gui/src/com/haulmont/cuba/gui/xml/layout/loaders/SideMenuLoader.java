/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

public class SideMenuLoader extends AbstractComponentLoader<SideMenu> {
    @Override
    public void createComponent() {
        resultComponent = factory.create(SideMenu.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadStyleName(resultComponent, element);
        loadResponsive(resultComponent, element);
        loadCss(resultComponent, element);
        loadAlign(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadSelectOnClick(resultComponent, element);
        loadMenuConfigIfNeeded(resultComponent, element);

        loadSidePanel(resultComponent, element);
        loadSidePanelToggleButton(resultComponent, element);

        loadCollapsibleSubMenu(resultComponent, element);
    }

    protected void loadMenuConfigIfNeeded(SideMenu component, Element element) {
        String loadMenuConfig = element.attributeValue("loadMenuConfig");
        if (StringUtils.isEmpty(loadMenuConfig) || Boolean.parseBoolean(loadMenuConfig)) {
            component.loadMenuConfig();
        }
    }

    protected void loadSelectOnClick(SideMenu component, Element element) {
        String selectOnClick = element.attributeValue("selectOnClick");
        if (StringUtils.isNotEmpty(selectOnClick)) {
            component.setSelectOnClick(Boolean.parseBoolean(selectOnClick));
        }
    }

    protected void loadSidePanel(SideMenu component, Element element) {
        String sidePanelId = element.attributeValue("sidePanel");
        if (StringUtils.isNotEmpty(sidePanelId)) {
            Component sidePanel = resultComponent.getFrame().getComponent(sidePanelId);
            if (sidePanel == null) {
                throw new GuiDevelopmentException("Unable to find sidePanel component for SideMenu",
                        context.getFullFrameId(), "sidePanel", sidePanelId);
            }
            component.setSidePanel(sidePanel);
        }
    }

    protected void loadSidePanelToggleButton(SideMenu component, Element element) {
        String toggleButtonId = element.attributeValue("sidePanelToggleButton");
        if (StringUtils.isNotEmpty(toggleButtonId)) {
            Component toggleButton = resultComponent.getFrame().getComponent(toggleButtonId);
            if (!(toggleButton instanceof Button)) {
                throw new GuiDevelopmentException("Unable to find sidePanelToggleButton for SideMenu",
                        context.getFullFrameId(), "sidePanelToggleButton", toggleButtonId);
            }
            component.setSidePanelToggleButton((Button) toggleButton);
        }
    }

    protected void loadCollapsibleSubMenu(SideMenu component, Element element) {
        String singleExpandedMenu = element.attributeValue("showSingleExpandedMenu");
        if (StringUtils.isNotEmpty(singleExpandedMenu)) {
            component.setShowSingleExpandedMenu(Boolean.parseBoolean(singleExpandedMenu));
        }
    }
}
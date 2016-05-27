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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import org.dom4j.Element;

public class DesktopResizableTextArea extends DesktopTextArea implements ResizableTextArea {

    protected boolean resizable = false;
    protected boolean settingsEnabled = true;

    @Override
    public boolean isResizable() {
        return resizable;
    }

    @Override
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        //Do nothing, because desktop text area is not resizable
    }

    @Override
    public void addResizeListener(com.haulmont.cuba.gui.components.ResizeListener resizeListener) {
        //Do nothing, because desktop text area is not resizable
    }

    @Override
    public void removeResizeListener(com.haulmont.cuba.gui.components.ResizeListener resizeListener) {
        //Do nothing, because desktop text area is not resizable
    }

    @Override
    public void addResizeListener(ResizeListener resizeListener) {
        //Do nothing, because desktop text area is not resizable
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        //Do nothing, because desktop text area is not resizable
    }

    @Override
    public void applySettings(Element element) {
        //Do nothing, because desktop text area is not resizable
    }

    @Override
    public boolean saveSettings(Element element) {
        return false;
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
        //Do nothing, because desktop text area is not resizable
    }
}
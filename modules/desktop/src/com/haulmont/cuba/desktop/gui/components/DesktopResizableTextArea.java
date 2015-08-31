/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;
import org.dom4j.Element;

/**
 * @author subbotin
 * @version $Id$
 */
public class DesktopResizableTextArea extends DesktopTextArea implements ResizableTextArea {

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public void setResizable(boolean resizable) {
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
        return false;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        //Do nothing, because desktop text area is not resizable
    }
}
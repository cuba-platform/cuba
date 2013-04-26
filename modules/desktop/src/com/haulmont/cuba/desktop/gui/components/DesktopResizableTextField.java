/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextField;
import com.haulmont.cuba.gui.data.ResizeListener;

/**
 * @author subbotin
 * @version $Id$
 */
public class DesktopResizableTextField extends DesktopTextArea implements ResizableTextField {
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
}
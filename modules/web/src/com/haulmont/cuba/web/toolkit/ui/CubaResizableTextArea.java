/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaResizableTextArea extends CubaTextArea {

    protected boolean resizable = false;
    protected List<ResizeListener> listeners = new ArrayList<>();

    public static interface ResizeListener {
        public void onResize(String oldWidth, String oldHeight, String width, String height);
    }

    @Override
    protected CubaResizableTextAreaState getState() {
        return (CubaResizableTextAreaState) super.getState();
    }

    @Override
    protected CubaResizableTextAreaState getState(boolean markAsDirty) {
        return (CubaResizableTextAreaState) super.getState(markAsDirty);
    }

    public boolean isResizable() {
        return getState(false).resizable;
    }

    public void setResizable(boolean resizable) {
        getState().resizable = resizable;
    }

    public void addResizeListener(ResizeListener resizeListener) {
        if (!listeners.contains(resizeListener))
            listeners.add(resizeListener);
    }

    public void removeResizeListener(ResizeListener resizeListener) {
        listeners.remove(resizeListener);
    }
}
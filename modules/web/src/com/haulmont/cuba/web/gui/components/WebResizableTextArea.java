/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextArea;
import com.vaadin.ui.TextArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author subbotin
 * @version $Id$
 */
public class WebResizableTextArea
        extends
            WebTextArea
        implements
            ResizableTextArea, Component.Wrapper {

    protected final List<ResizeListener> resizeListeners = new ArrayList<>();
    private CubaResizableTextArea resizableTextField;

    @Override
    protected CubaResizableTextArea createTextFieldImpl() {
        resizableTextField = new CubaResizableTextArea();
        return resizableTextField;
    }

    @Override
    public boolean isResizable() {
        return resizableTextField.isResizable();
    }

    @Override
    public void setResizable(boolean resizable) {
        resizableTextField.setResizable(resizable);
    }

    @Override
    public void addResizeListener(ResizeListener resizeListener) {
        if (!resizeListeners.contains(resizeListener)) {
            resizeListeners.add(resizeListener);
        }
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        resizeListeners.remove(resizeListener);
    }

    @Override
    protected void attachListener(TextArea component) {
        super.attachListener(component);
        final ResizableTextArea textArea = this;
        ((CubaResizableTextArea) component).addResizeListener(new CubaResizableTextArea.ResizeListener() {
            @Override
            public void onResize(String oldWidth, String oldHeight, String width, String height) {
                for (ResizeListener listener : resizeListeners) {
                    listener.onResize(textArea, oldWidth, oldHeight, width, height);
                }
            }
        });
    }
}
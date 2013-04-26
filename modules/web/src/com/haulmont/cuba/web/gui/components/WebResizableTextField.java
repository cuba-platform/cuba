/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ResizableTextField;
import com.haulmont.cuba.gui.data.ResizeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author subbotin
 * @version $Id$
 */
public class WebResizableTextField
        extends
            WebTextArea
        implements
            ResizableTextField, Component.Wrapper {

    protected final List<ResizeListener> resizeListeners = new ArrayList<>();
    private com.haulmont.cuba.web.toolkit.ui.ResizableTextField resizableTextField;

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.ResizableTextField createTextFieldImpl() {
        resizableTextField = new com.haulmont.cuba.web.toolkit.ui.ResizableTextField();
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
        if (!resizeListeners.contains(resizeListener))
            resizeListeners.add(resizeListener);
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        resizeListeners.remove(resizeListener);
    }

    protected void attachListener(com.haulmont.cuba.web.toolkit.ui.ResizableTextField component) {
        super.attachListener(component);
        final ResizableTextField textField = this;
        component.addResizeListener(new com.haulmont.cuba.web.toolkit.ui.ResizableTextField.ResizeListener() {
            @Override
            public void onResize(String oldWidth, String oldHeight, String width, String height) {
                for (ResizeListener listener : resizeListeners)
                    listener.onResize(textField, oldWidth, oldHeight, width, height);
            }
        });
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength(); 
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }
}
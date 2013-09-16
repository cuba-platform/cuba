/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;
import com.haulmont.cuba.web.toolkit.ui.CubaResizableTextArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author subbotin
 * @version $Id$
 */
public class WebResizableTextArea
        extends
            WebAbstractTextArea<CubaResizableTextArea>
        implements
            ResizableTextArea {

    protected final List<ResizeListener> resizeListeners = new ArrayList<>();

    @Override
    protected CubaResizableTextArea createTextFieldImpl() {
        return new CubaResizableTextArea();
    }

    @Override
    public boolean isResizable() {
        return component.isResizable();
    }

    @Override
    public void setResizable(boolean resizable) {
        component.setResizable(resizable);
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
    protected void attachListener(CubaResizableTextArea component) {
        super.attachListener(component);
        final ResizableTextArea textArea = this;
        component.addResizeListener(new CubaResizableTextArea.ResizeListener() {
            @Override
            public void onResize(String oldWidth, String oldHeight, String width, String height) {
                for (ResizeListener listener : resizeListeners) {
                    listener.onResize(textArea, oldWidth, oldHeight, width, height);
                }
            }
        });
    }
}
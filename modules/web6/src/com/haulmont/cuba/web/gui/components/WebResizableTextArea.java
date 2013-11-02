/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.ResizeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author subbotin
 * @version $Id$
 */
public class WebResizableTextArea
        extends
            WebAbstractTextArea<com.haulmont.cuba.web.toolkit.ui.ResizableTextArea>
        implements
            ResizableTextArea {

    protected final List<ResizeListener> resizeListeners = new ArrayList<>();

    @Override
    protected com.haulmont.cuba.web.toolkit.ui.ResizableTextArea createTextFieldImpl() {
        com.haulmont.cuba.web.toolkit.ui.ResizableTextArea textArea =
                new com.haulmont.cuba.web.toolkit.ui.ResizableTextArea();
        textArea.setRows(5);
        return textArea;
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
        if (!resizeListeners.contains(resizeListener))
            resizeListeners.add(resizeListener);
    }

    @Override
    public void removeResizeListener(ResizeListener resizeListener) {
        resizeListeners.remove(resizeListener);
    }

    @Override
    protected void attachListener(com.haulmont.cuba.web.toolkit.ui.ResizableTextArea component) {
        super.attachListener(component);
        final ResizableTextArea textArea = this;
        component.addResizeListener(new com.haulmont.cuba.web.toolkit.ui.ResizableTextArea.ResizeListener() {
            @Override
            public void onResize(String oldWidth, String oldHeight, String width, String height) {
                for (ResizeListener listener : resizeListeners)
                    listener.onResize(textArea, oldWidth, oldHeight, width, height);
            }
        });
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaState;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey("width") && variables.containsKey("height")) {
            this.setWidth((String) variables.get("width"));
            this.setHeight((String) variables.get("height"));

            for (ResizeListener listener : listeners) {
                listener.onResize(null, null, (String) variables.get("width"), (String) variables.get("height"));
            }
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (getState(false).resizable &&
                getState(false).rows > 0 &&
                getState(false).columns > 0) {
            LogFactory.getLog(getClass()).warn("TextArea with fixed rows or cols can not be resizable");
            getState().resizable = false;
        }
    }

    public void addResizeListener(ResizeListener resizeListener) {
        if (!listeners.contains(resizeListener)) {
            listeners.add(resizeListener);
        }
    }

    public void removeResizeListener(ResizeListener resizeListener) {
        listeners.remove(resizeListener);
    }
}
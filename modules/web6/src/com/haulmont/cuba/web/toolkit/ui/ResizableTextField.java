/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VResizableTextField;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author subbotin
 * @version $Id$
 */
@ClientWidget(VResizableTextField.class)
public class ResizableTextField extends TextField {

    private boolean resizable = false;
    private List<ResizeListener> listeners = new ArrayList<ResizeListener>();

    public static interface ResizeListener {
        public void onResize(String oldWidth, String oldHeight, String width, String height);
    }


    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }


    public void addResizeListener(ResizeListener resizeListener) {
        if (!listeners.contains(resizeListener))
            listeners.add(resizeListener);
    }

    public void removeResizeListener(ResizeListener resizeListener) {
        listeners.remove(resizeListener);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isResizable()) {
            target.addAttribute("resizable", true);
        }
    }


    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (variables.containsKey("width") && variables.containsKey("height"))
            for (ResizeListener listener : listeners)
                listener.onResize(null, null, (String) variables.get("width"), (String) variables.get("height"));
    }
}

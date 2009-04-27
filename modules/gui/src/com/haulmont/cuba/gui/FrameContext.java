/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 18.02.2009 9:55:51
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.WindowContext;
import com.haulmont.cuba.gui.data.ValueListener;

import java.util.Map;

public class FrameContext implements WindowContext {
    private final IFrame frame;
    private Map<String, Object> params;

    public FrameContext(IFrame window, Map<String, Object> params) {
        this.frame = window;
        this.params = params;
    }

    public <T> T getParameterValue(String property) {
        return (T) params.get("parameter$" + property);
    }

    public <T> T getValue(String property) {
        final Component component = frame.getComponent(property);
        if (component instanceof Component.Field) {
            return ((Component.Field) component).<T>getValue();
        } else {
            return null;
        }
    }

    public void setValue(String property, Object value) {
        final Component component = frame.getComponent(property);
        if (component instanceof Component.Field) {
            ((Component.Field) component).setValue(value);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void addValueListener(ValueListener listener) {
    }

    public void removeValueListener(ValueListener listener) {
    }
}

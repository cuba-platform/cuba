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
import com.haulmont.cuba.gui.data.Context;
import com.haulmont.cuba.gui.data.ValueListener;

public class FrameContext implements Context {
    private final IFrame frame;

    public FrameContext(IFrame window) {
        this.frame = window;
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

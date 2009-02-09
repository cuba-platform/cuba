/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:16:37
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;

public class Label
    extends
        AbstractComponent<com.itmill.toolkit.ui.Label> 
    implements
        com.haulmont.cuba.gui.components.Label, Component.Wrapper
{

    public Label() {
        component = new com.itmill.toolkit.ui.Label();
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public <T> T getValue() {
        return (T) component.getValue();
    }

    public void setValue(Object value) {
        component.setValue(value);
    }

    public boolean isEditable() {
        return false;
    }

    public void setEditable(boolean editable) {
        // Do nothing
    }
}

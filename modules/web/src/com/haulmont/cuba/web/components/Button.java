/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 17:21:57
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;

public class Button
    extends
        AbstractComponent<com.itmill.toolkit.ui.Button>
    implements 
        com.haulmont.cuba.gui.components.Button, Component.Wrapper
{
    private boolean flexible;

    public Button() {
        component = new com.itmill.toolkit.ui.Button();
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public boolean isFlexible() {
        return flexible;
    }

    public void setFlexible(boolean flexible) {
        this.flexible = flexible;
        component.setWidth("100%");
    }
}

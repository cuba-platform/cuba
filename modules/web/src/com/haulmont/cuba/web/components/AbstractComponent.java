/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 11:48:21
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.Layout;

class AbstractComponent<T extends com.itmill.toolkit.ui.Component> implements Component, Component.Wrapper {
    private String id;
    protected T component;

    private int verticalAlIlignment = Layout.AlignmentHandler.ALIGNMENT_TOP;
    private int horizontalAlIlignment = Layout.AlignmentHandler.ALIGNMENT_LEFT;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVerticalAlIlignment() {
        return verticalAlIlignment;
    }

    public void setVerticalAlIlignment(int verticalAlIlignment) {
        this.verticalAlIlignment = verticalAlIlignment;
        final com.itmill.toolkit.ui.Component component = this.component.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this.component, horizontalAlIlignment, verticalAlIlignment);
        }
    }

    public int getHorizontalAlIlignment() {
        return horizontalAlIlignment;
    }

    public void setHorizontalAlIlignment(int horizontalAlIlignment) {
        this.horizontalAlIlignment = horizontalAlIlignment;
        final com.itmill.toolkit.ui.Component component = this.component.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this.component, horizontalAlIlignment, verticalAlIlignment);
        }
    }

    public <T> T getComponent() {
        return (T) component;
    }
}

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 11:46:42
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.itmill.toolkit.ui.OrderedLayout;
import com.haulmont.cuba.gui.components.Component;

class AbstractContainer extends OrderedLayout implements Component, Component.Container {
    private int verticalAlIlignment = AlignmentHandler.ALIGNMENT_TOP;
    private int horizontalAlIlignment = AlignmentHandler.ALIGNMENT_LEFT;

    public AbstractContainer(int orientation) {
        super(orientation);
    }

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        addComponent(itmillComponent);
        setComponentAlignment(itmillComponent, component.getHorizontalAlIlignment(), component.getVerticalAlIlignment());
    }

    public void remove(Component component) {
        removeComponent(ComponentsHelper.unwrap(component));
    }

    public int getVerticalAlIlignment() {
        return verticalAlIlignment;
    }

    public void setVerticalAlIlignment(int verticalAlIlignment) {
        this.verticalAlIlignment = verticalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof AlignmentHandler) {
            ((AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlIlignment);
        }
    }

    public int getHorizontalAlIlignment() {
        return horizontalAlIlignment;
    }

    public void setHorizontalAlIlignment(int horizontalAlIlignment) {
        this.horizontalAlIlignment = horizontalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof AlignmentHandler) {
            ((AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlIlignment);
        }
    }
}

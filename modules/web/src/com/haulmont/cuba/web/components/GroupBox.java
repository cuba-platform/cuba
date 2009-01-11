/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 17:52:31
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.Panel;

public class GroupBox extends Panel implements Component, Component.Container, Component.HasCaption {
    private int verticalAlIlignment = Layout.AlignmentHandler.ALIGNMENT_TOP;
    private int horizontalAlIlignment = Layout.AlignmentHandler.ALIGNMENT_LEFT;
    private String id;

    public GroupBox() {
        setLayout(new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL));
    }

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        final Layout layout = getLayout();
        layout.addComponent(itmillComponent);
        if (layout instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) getLayout()).setComponentAlignment(
                    itmillComponent,
                    component.getHorizontalAlIlignment(),
                    component.getVerticalAlIlignment());
        }
    }

    public void remove(Component component) {
        getLayout().removeComponent(ComponentsHelper.unwrap(component));
    }

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
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlIlignment);
        }
    }

    public int getHorizontalAlIlignment() {
        return horizontalAlIlignment;
    }

    public void setHorizontalAlIlignment(int horizontalAlIlignment) {
        this.horizontalAlIlignment = horizontalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlIlignment);
        }
    }
}

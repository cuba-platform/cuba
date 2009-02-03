/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 03.02.2009 13:11:34
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.Layout;

import java.util.HashMap;
import java.util.Map;

public class GridLayout extends com.itmill.toolkit.ui.GridLayout implements com.haulmont.cuba.gui.components.GridLayout {
    private int verticalAlignment = Layout.AlignmentHandler.ALIGNMENT_TOP;
    private int horizontalAlignment = Layout.AlignmentHandler.ALIGNMENT_LEFT;

    protected String id;
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        addComponent(itmillComponent);
        setComponentAlignment(itmillComponent, component.getHorizontalAlignment(), component.getVerticalAlignment());

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
    }

    public void add(Component component, int col, int row) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        addComponent(itmillComponent, col, row);
        setComponentAlignment(itmillComponent, component.getHorizontalAlignment(), component.getVerticalAlignment());

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
    }

    public void remove(Component component) {
        removeComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void requestFocus() {
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlIlignment) {
        this.verticalAlignment = verticalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof AlignmentHandler) {
            ((AlignmentHandler) component).setComponentAlignment(this, horizontalAlignment, verticalAlIlignment);
        }
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlIlignment) {
        this.horizontalAlignment = horizontalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof AlignmentHandler) {
            ((AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlignment);
        }
    }

}

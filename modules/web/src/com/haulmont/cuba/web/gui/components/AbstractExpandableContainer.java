/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 11:46:42
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.ExpandLayout;
import com.itmill.toolkit.ui.Layout;

import java.util.*;

import org.apache.commons.lang.StringUtils;

class AbstractExpandableContainer extends ExpandLayout implements Component, Component.Container {
    private int verticalAlIlignment = Layout.AlignmentHandler.ALIGNMENT_TOP;
    private int horizontalAlIlignment = Layout.AlignmentHandler.ALIGNMENT_LEFT;

    protected String id;
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public AbstractExpandableContainer(int orientation) {
        super(orientation);
    }

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        addComponent(itmillComponent);
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
        return verticalAlIlignment;
    }

    public void setVerticalAlignment(int verticalAlIlignment) {
        this.verticalAlIlignment = verticalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof AlignmentHandler) {
            ((AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlIlignment);
        }
    }

    public int getHorizontalAlignment() {
        return horizontalAlIlignment;
    }

    public void setHorizontalAlignment(int horizontalAlIlignment) {
        this.horizontalAlIlignment = horizontalAlIlignment;
        final com.itmill.toolkit.ui.Component component = getParent();
        if (component instanceof AlignmentHandler) {
            ((AlignmentHandler) component).setComponentAlignment(this, horizontalAlIlignment, verticalAlIlignment);
        }
    }

    public void expand(Component component, String height, String width) {
        final com.itmill.toolkit.ui.Component expandedComponent = ComponentsHelper.unwrap(component);
        expand(expandedComponent);

        if (!StringUtils.isEmpty(height)) {
            expandedComponent.setHeight(height);
        }

        if (!StringUtils.isEmpty(width)) {
            expandedComponent.setWidth(width);
        }
    }
}
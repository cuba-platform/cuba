/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 20.01.2009 17:09:40
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;

import java.util.Map;
import java.util.HashMap;

public class SplitPanel extends com.itmill.toolkit.ui.SplitPanel implements com.haulmont.cuba.gui.components.SplitPanel {
    private int verticalAlIlignment = AlignmentHandler.ALIGNMENT_TOP;
    private int horizontalAlIlignment = AlignmentHandler.ALIGNMENT_LEFT;

    protected String id;
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        addComponent(itmillComponent);

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
        setDebugId(id);
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
}

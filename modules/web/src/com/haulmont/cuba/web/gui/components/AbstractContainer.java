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
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.AbstractOrderedLayout;
import com.itmill.toolkit.ui.Layout;

import java.util.*;

class AbstractContainer extends AbstractOrderedLayout implements Component.Container {
    protected String id;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    private Alignment alignment = Alignment.TOP_LEFT;

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        addComponent(itmillComponent);
        setComponentAlignment(itmillComponent, ComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
        ownComponents.add(component);
    }

    public void remove(Component component) {
        removeComponent(ComponentsHelper.unwrap(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.<T>getComponent(this, id);
    }

    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);  
    }

    public String getId() {
        return id; 
    }

    public void setId(String id) {
        this.id = id;
//        setDebugId(id);
    }

    public float getWidth() {
        return super.getWidth();
    }

    public float getHeight() {
        return super.getHeight();
    }

    public void requestFocus() {
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.itmill.toolkit.ui.Component component = this.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, ComponentsHelper.convertAlignment(alignment));
        }
    }
}

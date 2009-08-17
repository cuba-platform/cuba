/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 17.08.2009 17:27:24
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.Layout;

import java.util.*;

public class HtmlBoxLayout extends CustomLayout implements com.haulmont.cuba.gui.components.HtmlBoxLayout {

    protected String id;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    public HtmlBoxLayout() {
        super("");
    }

    public void expand(Component component, String height, String width) {
        //do nothing
    }

    public void add(Component component) {
        final com.itmill.toolkit.ui.Component itmillComponent = ComponentsHelper.unwrap(component);

        if (component.getId() != null) {
            addComponent(itmillComponent, component.getId());
            componentByIds.put(component.getId(), component);
        } else {
            addComponent(itmillComponent);
        }

        ownComponents.add(component);
    }

    public void remove(Component component) {
        if (component.getId() != null) {
            removeComponent(component.getId());
            componentByIds.remove(component.getId());
        } else {
            removeComponent(ComponentsHelper.unwrap(component));
        }
        ownComponents.remove(component);
    }

    @SuppressWarnings("unchecked")
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
            ((Layout.AlignmentHandler) component).setComponentAlignment(this,
                    ComponentsHelper.convertAlignment(alignment));
        }
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isExpandable() {
        return expandable;
    }
}

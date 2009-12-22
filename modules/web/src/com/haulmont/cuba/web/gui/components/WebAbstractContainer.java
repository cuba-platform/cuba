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
import com.haulmont.cuba.gui.components.IFrame;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Layout;

import java.util.*;

class WebAbstractContainer extends AbstractOrderedLayout
        implements Component.Container, Component.Expandable, Component.BelongToFrame
{
    protected String id;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    private IFrame frame;

    public void add(Component component) {
        final com.vaadin.ui.Component itmillComponent = WebComponentsHelper.getComposition(component);

        addComponent(itmillComponent);
        setComponentAlignment(itmillComponent, WebComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
        }
        ownComponents.add(component);
    }

    public void remove(Component component) {
        removeComponent(WebComponentsHelper.getComposition(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return WebComponentsHelper.<T>getComponent(this, id);
    }

    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<Component> getComponents() {
        return WebComponentsHelper.getComponents(this);
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

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void requestFocus() {
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = this.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }
}

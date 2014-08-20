/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Layout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
class WebAbstractBox extends WebAbstractComponent<AbstractOrderedLayout> implements BoxLayout {

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected Map<String, Component> componentByIds = new HashMap<>();

    @Override
    public void add(Component childComponent) {
        final com.vaadin.ui.Component vaadinComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(vaadinComponent);
        component.setComponentAlignment(vaadinComponent, WebComponentsHelper.convertAlignment(childComponent.getAlignment()));

        if (childComponent.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
            if (frame != null) {
                frame.registerComponent(childComponent);
            }
        }
        ownComponents.add(childComponent);
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return ComponentsHelper.getComponent(this, id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component parentComponent = component.getParent();
        if (parentComponent instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) parentComponent).setComponentAlignment(component, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    @Override
    public void expand(Component childComponent, String height, String width) {
        final com.vaadin.ui.Component expandedComponent = WebComponentsHelper.getComposition(childComponent);
        WebComponentsHelper.expand(component, expandedComponent, height, width);
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void setMargin(boolean enable) {
        component.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        component.setMargin(new Layout.MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
    }

    @Override
    public void setSpacing(boolean enabled) {
        component.setSpacing(enabled);
    }
}
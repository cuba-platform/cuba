/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.ScrollablePanel;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class WebScrollBoxLayout extends WebAbstractComponent<ScrollablePanel> implements ScrollBoxLayout {

    protected List<Component> components = new ArrayList<>();

    protected Orientation orientation = Orientation.VERTICAL;
    protected ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;

    public WebScrollBoxLayout() {
        component = new ScrollablePanel();

        ((AbstractOrderedLayout) component.getContent()).setMargin(false);
        component.setScrollable(true);
    }

    @Override
    public void add(Component childComponent) {
        AbstractOrderedLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(component.getContent() instanceof VerticalLayout))
            newContent = new VerticalLayout();
        else if (orientation == Orientation.HORIZONTAL && !(component.getContent() instanceof HorizontalLayout))
            newContent = new HorizontalLayout();

        if (newContent != null) {
            newContent.setMargin(((AbstractOrderedLayout) component.getContent()).getMargin());
            newContent.setSpacing(((AbstractOrderedLayout) component.getContent()).isSpacing());
            component.setContent(newContent);

            applyScrollBarsPolicy(scrollBarPolicy);
        }

        component.getContent().addComponent(WebComponentsHelper.getComposition(childComponent));
        components.add(childComponent);
    }

    @Override
    public void remove(Component childComponent) {
        component.getContent().removeComponent(WebComponentsHelper.getComposition(childComponent));
        components.remove(childComponent);
    }

    @Override
    public void requestFocus() {
        if (component.getComponentIterator().hasNext()) {
            com.vaadin.ui.Component vComponent = component.getComponentIterator().next();
            if (vComponent instanceof com.vaadin.ui.Component.Focusable) {
                ((com.vaadin.ui.Component.Focusable) vComponent).focus();
            }
        }
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        for (Component component : components) {
            if (ObjectUtils.equals(component.getId(), id))
                return (T) component;
        }
        return null;
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        for (Component component : getComponents()) {
            if (ObjectUtils.equals(component.getId(), id))
                return (T) component;
        }
        return null;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(components);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (!ObjectUtils.equals(orientation, this.orientation)) {
            if (!components.isEmpty())
                throw new IllegalStateException("Unable to change scrollBox orientation after adding components to it");

            this.orientation = orientation;
        }
    }

    @Override
    public ScrollBarPolicy getScrollBarPolicy() {
        return scrollBarPolicy;
    }

    @Override
    public void setScrollBarPolicy(ScrollBarPolicy scrollBarPolicy) {
        if (this.scrollBarPolicy != scrollBarPolicy) {
            applyScrollBarsPolicy(scrollBarPolicy);
        }
        this.scrollBarPolicy = scrollBarPolicy;
    }

    private void applyScrollBarsPolicy(ScrollBarPolicy scrollBarPolicy) {
        switch (scrollBarPolicy) {
            case VERTICAL:
                component.getContent().setHeight(com.vaadin.ui.Component.SIZE_UNDEFINED, Sizeable.UNITS_PIXELS);
                component.getContent().setWidth(100, Sizeable.UNITS_PERCENTAGE);
                break;

            case HORIZONTAL:
                component.getContent().setHeight(100, Sizeable.UNITS_PERCENTAGE);
                component.getContent().setWidth(com.vaadin.ui.Component.SIZE_UNDEFINED, Sizeable.UNITS_PIXELS);
                break;

            case BOTH:
                component.getContent().setSizeUndefined();
                break;

            case NONE:
                component.getContent().setSizeFull();
                break;
        }
    }

    @Override
    public void setMargin(boolean enable) {
        ((AbstractOrderedLayout) component.getContent()).setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        ((AbstractOrderedLayout) component.getContent()).setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    @Override
    public void setSpacing(boolean enabled) {
        ((AbstractOrderedLayout) component.getContent()).setSpacing(enabled);
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalActionsLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebScrollBoxLayout extends WebAbstractComponent<Panel> implements ScrollBoxLayout {

    public static final String CUBA_SCROLLBOX_CONTENT_STYLE = "cuba-scrollbox-content";

    protected List<Component> components = new ArrayList<>();
    protected Alignment alignment = Alignment.TOP_LEFT;
    protected Orientation orientation = Orientation.VERTICAL;
    protected ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;

    protected String styleName;

    public WebScrollBoxLayout() {
        component = new Panel();
        component.setStyleName("cuba-scrollbox");

        CubaVerticalActionsLayout content = new CubaVerticalActionsLayout();
        content.setWidth("100%");
        content.setStyleName(CUBA_SCROLLBOX_CONTENT_STYLE);
        component.setContent(content);

        getContent().setMargin(false);
    }

    protected AbstractOrderedLayout getContent() {
        return (AbstractOrderedLayout) component.getContent();
    }

    @Override
    public void add(Component childComponent) {
        AbstractOrderedLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(getContent() instanceof CubaVerticalActionsLayout)) {
            newContent = new CubaVerticalActionsLayout();
            newContent.setWidth("100%");
        } else if (orientation == Orientation.HORIZONTAL && !(getContent() instanceof CubaHorizontalActionsLayout)) {
            newContent = new CubaHorizontalActionsLayout();
        }

        if (newContent != null) {
            newContent.setMargin((getContent()).getMargin());
            newContent.setSpacing((getContent()).isSpacing());
            newContent.setStyleName(CUBA_SCROLLBOX_CONTENT_STYLE);

            com.vaadin.ui.Component oldContent = component.getContent();
            newContent.setWidth(oldContent.getWidth(), oldContent.getWidthUnits());
            newContent.setHeight(oldContent.getHeight(), oldContent.getHeightUnits());

            component.setContent(newContent);

            applyScrollBarsPolicy(scrollBarPolicy);
        }

        getContent().addComponent(WebComponentsHelper.getComposition(childComponent));
        components.add(childComponent);
    }

    @Override
    public void setStyleName(String styleName) {
        if (StringUtils.isNotEmpty(this.styleName)) {
            getComposition().removeStyleName(this.styleName);
        }

        this.styleName = styleName;

        if (StringUtils.isNotEmpty(styleName)) {
            getComposition().addStyleName(styleName);
        }
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void remove(Component component) {
        getContent().removeComponent(WebComponentsHelper.getComposition(component));
        components.remove(component);
    }

    @Override
    public void requestFocus() {
        Iterator<com.vaadin.ui.Component> componentIterator = getContent().iterator();
        if (componentIterator.hasNext()) {
            com.vaadin.ui.Component childComponent = componentIterator.next();
            if (childComponent instanceof com.vaadin.ui.Component.Focusable) {
                ((com.vaadin.ui.Component.Focusable) childComponent).focus();
            }
        }
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        for (Component component : components) {
            if (ObjectUtils.equals(component.getId(), id)) {
                return (T) component;
            }
        }
        return null;
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
            if (!components.isEmpty()) {
                throw new IllegalStateException("Unable to change scrollBox orientation after adding components to it");
            }

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

    protected void applyScrollBarsPolicy(ScrollBarPolicy scrollBarPolicy) {
        switch (scrollBarPolicy) {
            case VERTICAL:
                getContent().setHeight(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
                getContent().setWidth(100, Sizeable.Unit.PERCENTAGE);
                break;

            case HORIZONTAL:
                getContent().setHeight(100, Sizeable.Unit.PERCENTAGE);
                getContent().setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
                break;

            case BOTH:
                getContent().setSizeUndefined();
                break;

            case NONE:
                getContent().setSizeFull();
                break;
        }
    }

    @Override
    public void setMargin(boolean enable) {
        getContent().setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        getContent().setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
    }

    @Override
    public void setSpacing(boolean enabled) {
        getContent().setSpacing(enabled);
    }
}
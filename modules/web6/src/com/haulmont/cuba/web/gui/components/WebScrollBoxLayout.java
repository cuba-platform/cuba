/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.ScrollablePanel;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.cuba.web.gui.components.WebComponentsHelper.convertAlignment;

/**
 * @author abramov
 * @version $Id$
 */
public class WebScrollBoxLayout extends WebAbstractComponent<ScrollablePanel> implements ScrollBoxLayout {

    protected Collection<Component> ownComponents = new LinkedHashSet<>();
    protected Map<String, Component> componentByIds = new HashMap<>();

    protected Orientation orientation = Orientation.VERTICAL;
    protected ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;

    public WebScrollBoxLayout() {
        component = new ScrollablePanel();

        ((AbstractOrderedLayout) component.getContent()).setMargin(false);
        component.setScrollable(true);
    }

    @Override
    public void add(Component childComponent) {
        add(childComponent, ownComponents.size());
    }

    @Override
    public void add(Component childComponent, int index) {
        if (childComponent.getParent() != null && childComponent.getParent() != this) {
            throw new IllegalStateException("Component already has parent");
        }

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

        AbstractOrderedLayout content = (AbstractOrderedLayout) component.getContent();

        if (ownComponents.contains(childComponent)) {
            int existingIndex = content.getComponentIndex(WebComponentsHelper.getComposition(childComponent));
            if (index > existingIndex) {
                index--;
            }

            remove(childComponent);
        }

        com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);
        content.addComponent(vComponent, index);
        content.setComponentAlignment(vComponent, convertAlignment(childComponent.getAlignment()));

        if (childComponent.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
        }

        if (frame != null) {
            if (childComponent instanceof BelongToFrame
                    && ((BelongToFrame) childComponent).getFrame() == null) {
                ((BelongToFrame) childComponent).setFrame(frame);
            } else {
                frame.registerComponent(childComponent);
            }
        }

        if (index == ownComponents.size()) {
            ownComponents.add(childComponent);
        } else {
            List<Component> componentsTempList = new ArrayList<>(ownComponents);
            componentsTempList.add(index, childComponent);

            ownComponents.clear();
            ownComponents.addAll(componentsTempList);
        }

        childComponent.setParent(this);
    }

    @Override
    public void remove(Component childComponent) {
        component.getContent().removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);

        childComponent.setParent(null);
    }

    @Override
    public void setFrame(IFrame frame) {
        super.setFrame(frame);

        if (frame != null) {
            for (Component childComponent : ownComponents) {
                if (childComponent instanceof BelongToFrame
                        && ((BelongToFrame) childComponent).getFrame() == null) {
                    ((BelongToFrame) childComponent).setFrame(frame);
                }
            }
        }
    }

    @Override
    public void requestFocus() {
    }

    @SuppressWarnings("unchecked")
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
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (!ObjectUtils.equals(orientation, this.orientation)) {
            if (!ownComponents.isEmpty())
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
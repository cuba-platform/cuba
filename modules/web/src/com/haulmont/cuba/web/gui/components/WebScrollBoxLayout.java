/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.web.toolkit.ui.ScrollablePanel;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class WebScrollBoxLayout extends ScrollablePanel implements ScrollBoxLayout {

    private static final long serialVersionUID = 2724785666636087457L;

    private String id;
    protected List<Component> components = new ArrayList<>();
    private Alignment alignment = Alignment.TOP_LEFT;
    private Orientation orientation = Orientation.VERTICAL;
    private ScrollBarPolicy scrollBarPolicy = ScrollBarPolicy.VERTICAL;

    private IFrame frame;

    public WebScrollBoxLayout() {
        ((AbstractOrderedLayout) getContent()).setMargin(false);
        setScrollable(true);
    }

    @Override
    public void add(Component component) {
        AbstractOrderedLayout newContent = null;
        if (orientation == Orientation.VERTICAL && !(getContent() instanceof VerticalLayout))
            newContent = new VerticalLayout();
        else if (orientation == Orientation.HORIZONTAL && !(getContent() instanceof HorizontalLayout))
            newContent = new HorizontalLayout();

        if (newContent != null) {
            newContent.setMargin(((AbstractOrderedLayout) getContent()).getMargin());
            newContent.setSpacing(((AbstractOrderedLayout) getContent()).isSpacing());
            setContent(newContent);

            applyScrollBarsPolicy(scrollBarPolicy);
        }

        getContent().addComponent(WebComponentsHelper.getComposition(component));
        components.add(component);
    }

    @Override
    public void remove(Component component) {
        getContent().removeComponent(WebComponentsHelper.getComposition(component));
        components.remove(component);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void requestFocus() {
        if (getComponentIterator().hasNext()) {
            com.vaadin.ui.Component component = getComponentIterator().next();
            if (component instanceof Focusable) {
                ((Focusable) component).focus();
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
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    @Override
    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    @Override
    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
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
                getContent().setHeight(SIZE_UNDEFINED, Sizeable.UNITS_PIXELS);
                getContent().setWidth(100, Sizeable.UNITS_PERCENTAGE);
                break;

            case HORIZONTAL:
                getContent().setHeight(100, Sizeable.UNITS_PERCENTAGE);
                getContent().setWidth(SIZE_UNDEFINED, Sizeable.UNITS_PIXELS);
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
        ((AbstractOrderedLayout) getContent()).setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        ((AbstractOrderedLayout) getContent()).setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    @Override
    public void setSpacing(boolean enabled) {
        ((AbstractOrderedLayout) getContent()).setSpacing(enabled);
    }
}
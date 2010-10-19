/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 20.01.2009 10:27:00
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ComponentContainer;
import org.apache.commons.lang.ObjectUtils;

import java.util.Collection;
import java.util.Collections;

public class WebAbstractPanel extends Panel
        implements com.haulmont.cuba.gui.components.Layout, Component.Container, Component.Expandable, Component.BelongToFrame
{
    private String id;
    private Component component;
    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    private IFrame frame;
    
    private static final long serialVersionUID = -1826985432510393176L;

    public void add(Component component) {
        final com.vaadin.ui.Component comp = WebComponentsHelper.unwrap(component);
        if (comp instanceof Layout) {
            setContent(((Layout) comp));
            this.component = component;
        } else {
            throw new UnsupportedOperationException("Only layout component is supported inside groupBox");
        }
    }

    public void remove(Component component) {
        if (getContent() == WebComponentsHelper.unwrap(component)) {
            setContent(null);
            this.component = null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }

    public void requestFocus() {
        if (getComponentIterator().hasNext()) {
            com.vaadin.ui.Component component = getComponentIterator().next();
            if (component instanceof Focusable) {
                ((Focusable) component).focus();
            }
        }
    }

    public <T extends Component> T getOwnComponent(String id) {
        return component != null && ObjectUtils.equals(component.getId(), id) ? (T) component : null;
    }

    public <T extends Component> T getComponent(String id) {
        final ComponentContainer layout = getContent();
        if (layout instanceof Container) {
            final com.haulmont.cuba.gui.components.Component component = ((Container) layout).getOwnComponent(id);

            if (component == null) {
                return WebComponentsHelper.<T>getComponentByIterate(layout, id);
            } else {
                return (T) component;
            }
        } else {
            return WebComponentsHelper.<T>getComponentByIterate(layout, id);
        }
    }

    public Collection<Component> getOwnComponents() {
        return component == null ? Collections.<Component>emptyList() : Collections.singletonList(component);
    }

    public Collection<Component> getComponents() {
        return WebComponentsHelper.getComponents(this);
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void expand(Component component, String height, String width) {
//        final com.vaadin.ui.Component expandedComponent = ComponentsHelper.unwrap(component);
//        if (getLayout() instanceof AbstractOrderedLayout) {
//            ComponentsHelper.expand((AbstractOrderedLayout) getLayout(), expandedComponent, height, width);
//        } else {
//            throw new UnsupportedOperationException();
//        }
    }
}

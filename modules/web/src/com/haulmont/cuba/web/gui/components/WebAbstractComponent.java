/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 11:48:21
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.vaadin.ui.Layout;
import org.dom4j.Element;

public class WebAbstractComponent<T extends com.vaadin.ui.Component>
    implements
        Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame
{
    private String id;
    protected T component;

    private Element element;
    private com.haulmont.cuba.gui.components.IFrame frame;
    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    public <A extends com.haulmont.cuba.gui.components.IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        return component.getDebugId();
    }

    public void setDebugId(String id) {
        component.setDebugId(id);
    }

    public String getStyleName() {
        return component.getStyleName();
    }

    public void setStyleName(String name) {
        component.setStyleName(name);
    }

    public boolean isEnabled() {
        return getComposition().isEnabled();
    }

    public void setEnabled(boolean enabled) {
        getComposition().setEnabled(enabled);
    }

    public boolean isVisible() {
        return getComposition().isVisible();
    }

    public void setVisible(boolean visible) {
        getComposition().setVisible(visible);
    }

    public void requestFocus() {
        if (component instanceof com.vaadin.ui.Component.Focusable) {
            ((com.vaadin.ui.Component.Focusable) component).focus();
        }
    }

    public float getHeight() {
        return component.getHeight();
    }

    public int getHeightUnits() {
        return component.getHeightUnits();
    }

    public void setHeight(String height) {
        component.setHeight(height);
    }

    public float getWidth() {
        return component.getWidth();
    }

    public int getWidthUnits() {
        return component.getWidthUnits();
    }

    public void setWidth(String width) {
        component.setWidth(width);
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = this.component.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this.component, WebComponentsHelper.convertAlignment(alignment));
        }
    }

    public <T> T getComponent() {
        return (T) component;
    }

    public com.vaadin.ui.Component getComposition() {
        return component;
    }

    public Element getXmlDescriptor() {
        return element;
    }

    public void setXmlDescriptor(Element element) {
        this.element = element;
    }
}

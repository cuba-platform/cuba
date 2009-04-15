/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.12.2008 11:48:21
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.IFrame;
import com.itmill.toolkit.ui.Layout;
import org.dom4j.Element;

class AbstractComponent<T extends com.itmill.toolkit.ui.Component>
    implements
        Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame
{
    private String id;
    protected T component;

    private Element element;
    private com.haulmont.cuba.gui.components.IFrame frame;
    private Alignment alignment = Alignment.TOP_LEFT;

    public <A extends com.haulmont.cuba.gui.components.IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
//        component.setDebugId(id);
    }

    public String getStyleName() {
        return component.getStyleName();
    }

    public void setStyleName(String name) {
        component.setStyleName(name);
    }

    public boolean isVisible() {
        return component.isVisible();
    }

    public void setVisible(boolean visible) {
        component.setVisible(visible);
    }

    public void requestFocus() {
        if (component instanceof com.itmill.toolkit.ui.Component.Focusable) {
            ((com.itmill.toolkit.ui.Component.Focusable) component).focus();
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

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.itmill.toolkit.ui.Component component = this.component.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this.component, ComponentsHelper.convertAlignment(alignment));
        }
    }

    public <T> T getComponent() {
        return (T) component;
    }

    public Element getXmlDescriptor() {
        return element;
    }

    public void setXmlDescriptor(Element element) {
        this.element = element;
    }
}

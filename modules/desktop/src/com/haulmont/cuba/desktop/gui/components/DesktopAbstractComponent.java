/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import org.dom4j.Element;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractComponent<C extends JComponent>
    implements
        Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame, Component.Expandable
{
    protected C jComponent;

    protected String id;
    protected IFrame frame;
    protected Element xmlDescriptor;
    protected boolean expandable;

    public <A extends IFrame> A getFrame() {
        return (A) frame;
    }

    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        return null;
    }

    public void setDebugId(String id) {
    }

    public boolean isEnabled() {
        return jComponent.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        jComponent.setEnabled(enabled);
    }

    public boolean isVisible() {
        return jComponent.isVisible();
    }

    public void setVisible(boolean visible) {
        jComponent.setVisible(visible);
    }

    public void requestFocus() {
        jComponent.requestFocus();
    }

    public float getHeight() {
        return jComponent.getHeight();
    }

    public int getHeightUnits() {
        return 0;
    }

    public void setHeight(String height) {
        // TODO
    }

    public float getWidth() {
        return jComponent.getWidth();
    }

    public int getWidthUnits() {
        return 0;
    }

    public void setWidth(String width) {
        // TODO
    }

    public Alignment getAlignment() {
        return null;
    }

    public void setAlignment(Alignment alignment) {
    }

    public String getStyleName() {
        return null;
    }

    public void setStyleName(String name) {
    }

    public Element getXmlDescriptor() {
        return xmlDescriptor;
    }

    public void setXmlDescriptor(Element element) {
        xmlDescriptor = element;
    }

    public <T> T getComponent() {
        return (T) jComponent;
    }

    public JComponent getComposition() {
        return jComponent;
    }
}

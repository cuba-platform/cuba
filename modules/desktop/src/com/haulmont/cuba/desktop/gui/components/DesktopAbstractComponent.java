/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractComponent<C extends JComponent>
    implements
        Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame, Component.Expandable
{
    protected C impl;

    protected String id;
    protected IFrame frame;
    protected Element xmlDescriptor;
    protected boolean expandable = true;

    protected Log log = LogFactory.getLog(getClass());

    protected C getImpl() {
        return impl;
    }

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
        return impl.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        impl.setEnabled(enabled);
    }

    public boolean isVisible() {
        return impl.isVisible();
    }

    public void setVisible(boolean visible) {
        impl.setVisible(visible);
    }

    public void requestFocus() {
        impl.requestFocus();
    }

    public float getHeight() {
        return getImpl().getHeight();
    }

    public int getHeightUnits() {
        return 0;
    }

    public void setHeight(String height) {
        Integer h;
        try {
            h = Integer.valueOf(height);
        } catch (NumberFormatException e) {
            log.trace("Unable to parse height value: " + height);
            return;
        }
        int width = (int) getImpl().getPreferredSize().getWidth();
        getImpl().setPreferredSize(new Dimension(width, h));
        getImpl().setMinimumSize(new Dimension(width, h));
    }

    public float getWidth() {
        return getImpl().getWidth();
    }

    public int getWidthUnits() {
        return 0;
    }

    public void setWidth(String width) {
        Integer w;
        try {
            w = Integer.valueOf(width);
        } catch (NumberFormatException e) {
            log.trace("Unable to parse width value: " + width);
            return;
        }
        int height = (int) getImpl().getPreferredSize().getHeight();
        getImpl().setPreferredSize(new Dimension(w, height));
        getImpl().setMinimumSize(new Dimension(w, height));
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
        return (T) impl;
    }

    public JComponent getComposition() {
        return impl;
    }
}

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Layout;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.List;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public class WebAbstractComponent<T extends com.vaadin.ui.Component>
    implements
        Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame {

    public static final List<Sizeable.Unit> UNIT_SYMBOLS = Arrays.asList(
            Sizeable.Unit.PIXELS, Sizeable.Unit.POINTS, Sizeable.Unit.PICAS,
            Sizeable.Unit.EM, Sizeable.Unit.EX, Sizeable.Unit.MM,
            Sizeable.Unit.CM, Sizeable.Unit.INCH, Sizeable.Unit.PERCENTAGE);

    private String id;
    protected T component;

    private Element element;
    private com.haulmont.cuba.gui.components.IFrame frame;
    private Alignment alignment = Alignment.TOP_LEFT;

    private boolean expandable = true;

    @Override
    public <A extends com.haulmont.cuba.gui.components.IFrame> A getFrame() {
        return (A) frame;
    }

    @Override
    public void setFrame(IFrame frame) {
        this.frame = frame;
        frame.registerComponent(this);
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
    public String getDebugId() {
        return component.getId();
    }

    @Override
    public void setDebugId(String id) {
        component.setId(id);
    }

    @Override
    public String getStyleName() {
        return component.getStyleName();
    }

    @Override
    public void setStyleName(String name) {
        component.setStyleName(name);
    }

    @Override
    public boolean isEnabled() {
        return getComposition().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getComposition().setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return getComposition().isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getComposition().setVisible(visible);
    }

    @Override
    public void requestFocus() {
        if (component instanceof com.vaadin.ui.Component.Focusable) {
            ((com.vaadin.ui.Component.Focusable) component).focus();
        }
    }

    @Override
    public float getHeight() {
        return component.getHeight();
    }

    @Override
    public int getHeightUnits() {
        return UNIT_SYMBOLS.indexOf(component.getHeightUnits());
    }

    @Override
    public void setHeight(String height) {
        component.setHeight(height);
    }

    @Override
    public float getWidth() {
        return component.getWidth();
    }

    @Override
    public int getWidthUnits() {
        return UNIT_SYMBOLS.indexOf(component.getWidthUnits());
    }

    @Override
    public void setWidth(String width) {
        component.setWidth(width);
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component component = this.component.getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this.component,
                    WebComponentsHelper.convertAlignment(alignment));
        }
    }

    @Override
    public <T> T getComponent() {
        return (T) component;
    }

    @Override
    public com.vaadin.ui.Component getComposition() {
        return component;
    }

    @Override
    public Element getXmlDescriptor() {
        return element;
    }

    @Override
    public void setXmlDescriptor(Element element) {
        this.element = element;
    }
}
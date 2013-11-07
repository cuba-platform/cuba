/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    protected String id;
    protected T component;

    protected Element element;
    protected com.haulmont.cuba.gui.components.IFrame frame;
    protected Alignment alignment = Alignment.TOP_LEFT;

    protected boolean expandable = true;

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
        return getComposition().getStyleName();
    }

    @Override
    public void setStyleName(String name) {
        getComposition().setStyleName(name);
    }

    @Override
    public boolean isEnabled() {
        return WebComponentsHelper.isComponentEnabled(getComposition());
    }

    @Override
    public void setEnabled(boolean enabled) {
        getComposition().setEnabled(enabled);
    }

    @Override
    public boolean isVisible() {
        return WebComponentsHelper.isComponentVisible(getComposition());
    }

    @Override
    public void setVisible(boolean visible) {
        getComposition().setVisible(visible);
    }

    /**
     * @return component enabled property
     */
    public boolean getComponentEnabledFlag() {
        return getComposition().isEnabled();
    }

    /**
     * @return component visible property
     */
    public boolean getComponentVisibleFlag() {
        return getComposition().isVisible();
    }

    @Override
    public void requestFocus() {
        if (component instanceof com.vaadin.ui.Component.Focusable) {
            ((com.vaadin.ui.Component.Focusable) component).focus();
        }
    }

    @Override
    public float getHeight() {
        return getComposition().getHeight();
    }

    @Override
    public int getHeightUnits() {
        return UNIT_SYMBOLS.indexOf(getComposition().getHeightUnits());
    }

    @Override
    public void setHeight(String height) {
        getComposition().setHeight(height);
    }

    @Override
    public float getWidth() {
        return getComposition().getWidth();
    }

    @Override
    public int getWidthUnits() {
        return UNIT_SYMBOLS.indexOf(getComposition().getWidthUnits());
    }

    @Override
    public void setWidth(String width) {
        getComposition().setWidth(width);
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
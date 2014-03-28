/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.App;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Layout;
import org.dom4j.Element;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public class WebAbstractComponent<T extends com.vaadin.ui.Component>
    implements
        Component, Component.Wrapper, Component.HasXmlDescriptor, Component.BelongToFrame {

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

        assignAutoDebugId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;

        if (this.component instanceof AbstractComponent && App.getInstance().isTestMode()) {
            ((AbstractComponent) this.component).setCubaId(id);
        }
    }

    public void assignAutoDebugId() {
        if (App.getInstance().isTestMode()) {
            String alternativeDebugId = getAlternativeDebugId();

            // always change cuba id, do not assign auto id for components
            if (getId() == null && component instanceof AbstractComponent) {
                ((AbstractComponent)component).setCubaId(alternativeDebugId);
            }
        }
    }

    /**
     * @return id that is suitable for auto debug id
     */
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }

        return getClass().getSimpleName();
    }

    @Override
    public String getDebugId() {
        return component.getDebugId();
    }

    @Override
    public void setDebugId(String id) {
        component.setDebugId(id);
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
        return getComposition().isEnabled();
    }

    /**
     * @return component enabled property
     */
    public boolean getComponentEnabledFlag() {
        return ((AbstractComponent)getComposition()).isComponentEnabled();
    }

    /**
     * @return component visible property
     */
    public boolean getComponentVisibleFlag() {
        return ((AbstractComponent)getComposition()).isComponentVisible();
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
        return getComposition().getHeight();
    }

    @Override
    public int getHeightUnits() {
        return getComposition().getHeightUnits();
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
        return getComposition().getWidthUnits();
    }

    @Override
    public void setWidth(String width) {
        getComposition().setWidth(width);
    }

    @Deprecated
    public boolean isExpandable() {
        return expandable;
    }

    @Deprecated
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
        final com.vaadin.ui.Component component = this.getComposition().getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(
                    this.getComposition(), WebComponentsHelper.convertAlignment(alignment));
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
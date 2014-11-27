/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Layout;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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

        assignAutoDebugId();
    }

    public void assignAutoDebugId() {
        AppUI ui = AppUI.getCurrent();
        if (ui.isTestMode()) {
            String alternativeDebugId = getAlternativeDebugId();

            // always change cuba id, do not assign auto id for components
            if (getId() == null && component != null) {
                component.setCubaId(alternativeDebugId);
            }

            if (frame == null || StringUtils.isEmpty(frame.getId()))
                return;

            String fullFrameId = ComponentsHelper.getFullFrameId(frame);
            TestIdManager testIdManager = ui.getTestIdManager();

            String candidateId = fullFrameId + "." + alternativeDebugId;
            if (getDebugId() != null) {
                String postfix = StringUtils.replace(getDebugId(), testIdManager.normalize(candidateId), "");
                if (StringUtils.isEmpty(postfix) || NumberUtils.isDigits(postfix)) {
                    // do not assign new Id
                    return;
                }
            }
            setDebugId(testIdManager.getTestId(candidateId));
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
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
        if (this.component != null && AppUI.getCurrent().isTestMode()) {
            this.component.setCubaId(id);
        }
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
        final com.vaadin.ui.Component component = this.getComposition().getParent();
        if (component instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) component).setComponentAlignment(this.getComposition(),
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
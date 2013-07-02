/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.caption;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout.CubaFieldGroupLayoutComponentSlot;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaCaptionWidget extends VCaption {
    public static final String CLASSNAME = "cuba-caption";

    protected Element toolTipIndicator;

    protected CubaFieldGroupLayoutComponentSlot fieldGroupSlot = null;

    public CubaCaptionWidget(ComponentConnector component, ApplicationConnection client) {
        super(component, client);

        initWidget();
    }

    public void initWidget() {
        addStyleName(CLASSNAME);
    }

    @Override
    public boolean updateCaption() {
        boolean updateResult = super.updateCaption();

        addStyleName(CLASSNAME);

        if (fieldGroupSlot != null)
            fieldGroupSlot.captionUpdated(this);

        return updateResult;
    }

    @Override
    protected void enableFieldDescription() {
        if (toolTipIndicator == null) {
            toolTipIndicator = DOM.createDiv();
            toolTipIndicator.setClassName("cuba-tooltip-button");

            DOM.insertChild(getElement(), toolTipIndicator, getDescriptionInsertPosition());
            // vaadin7 tooltips
        }
    }

    @Override
    protected void disableFieldDescription() {
        if (toolTipIndicator != null) {
            toolTipIndicator.removeFromParent();
            toolTipIndicator = null;
        }
    }

    @Override
    protected void enableFieldRequired() {
        // vaadin7 tooltips
    }

    @Override
    public int getRenderedWidth() {
        int renderedWidth = super.getRenderedWidth();
        if (toolTipIndicator != null) {
            renderedWidth += Util.getRequiredWidth(toolTipIndicator);
        }
        return renderedWidth;
    }

    @Override
    public Element getTextElement() {
        return super.getTextElement();
    }

    public Element getTooltipElement() {
        return toolTipIndicator;
    }

    public Element getRequiredIndicatorElement() {
        return requiredFieldIndicator;
    }

    @Override
    protected int getInsertPosition(InsertPosition element) {
        int pos = super.getInsertPosition(element);

        if (toolTipIndicator != null) {
            pos++;
        }

        return pos;
    }

    protected int getDescriptionInsertPosition() {
        return super.getInsertPosition(null);
    }

    public CubaFieldGroupLayoutComponentSlot getFieldGroupSlot() {
        return fieldGroupSlot;
    }

    public void setFieldGroupSlot(CubaFieldGroupLayoutComponentSlot fieldGroupSlot) {
        this.fieldGroupSlot = fieldGroupSlot;
    }
}
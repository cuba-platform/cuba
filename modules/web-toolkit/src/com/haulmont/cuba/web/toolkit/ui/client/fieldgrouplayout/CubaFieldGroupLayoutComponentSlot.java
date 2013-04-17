/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.ManagedLayout;
import com.vaadin.client.ui.VCheckBox;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;

/**
 * Component slot with horizontal layout for caption and component
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayoutComponentSlot extends ComponentConnectorLayoutSlot {

    private static final int REQUIRED_INDICATOR_WIDTH = 10;
    private static final int TOOLTIP_INDICATOR_WIDTH = 16;

    protected Element requiredElement = null;
    protected Element tooltipElement = null;
    protected Element rightCaption = null;

    public CubaFieldGroupLayoutComponentSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }

    @Override
    public void setCaption(VCaption caption) {
        super.setCaption(caption);

        if (caption != null) {
            // tune style, force horizontal layout
            Style style = caption.getElement().getStyle();
            style.setPosition(Style.Position.RELATIVE);
            style.setDisplay(Style.Display.INLINE_BLOCK);
            style.setFloat(Style.Float.LEFT);
            style.clearTop();
            style.clearLeft();

            if (caption instanceof CubaCaptionWidget)
                ((CubaCaptionWidget) caption).setFieldGroupSlot(this);
        }
    }

    public void captionUpdated(CubaCaptionWidget captionWidget) {
        moveIndicatorsRight(captionWidget);
    }

    protected void moveIndicatorsRight(final CubaCaptionWidget captionWidget) {
        int fakeCaptionWidth = 0;
        boolean widthChanged = false;
        if (captionWidget.getRequiredIndicatorElement() != null && requiredElement == null) {
            if (rightCaption == null) {
                rightCaption = DOM.createDiv();
                rightCaption.setClassName(VCaption.CLASSNAME);
                rightCaption.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
                getWrapperElement().insertAfter(rightCaption, getWidget().getElement());
            }

            captionWidget.getElement().removeChild(captionWidget.getRequiredIndicatorElement());

            // we clone element to disable all event listeners and prevent tootip events
            requiredElement = (Element) captionWidget.getRequiredIndicatorElement().cloneNode(true);
            rightCaption.appendChild(requiredElement);
            if (captionWidget.getTooltipElement() != null) {
                rightCaption.appendChild(captionWidget.getTooltipElement());
            }

            widthChanged = true;

            // remove old requiredIndicatorElement from DOM
            captionWidget.getRequiredIndicatorElement().removeFromParent();

        } else if (captionWidget.getRequiredIndicatorElement() == null && requiredElement != null) {
            requiredElement = null;
            widthChanged = true;
        }

        if (captionWidget.getTooltipElement() != null && tooltipElement == null) {
            if (rightCaption == null) {
                rightCaption = DOM.createDiv();
                rightCaption.setClassName(VCaption.CLASSNAME);
                getWrapperElement().insertAfter(rightCaption, getWidget().getElement());
            }
            if (!captionWidget.getTooltipElement().getParentElement().equals(rightCaption)) {
                captionWidget.getElement().removeChild(captionWidget.getTooltipElement());
            }
            if (!(getWidget() instanceof VCheckBox)) {
                tooltipElement = captionWidget.getTooltipElement();
                rightCaption.appendChild(tooltipElement);
            }
            widthChanged = true;
        } else if (captionWidget.getTooltipElement() == null && tooltipElement != null) {
            tooltipElement = null;
            widthChanged = true;
        }

        if (requiredElement != null) {
            fakeCaptionWidth += REQUIRED_INDICATOR_WIDTH;
        }
        if (tooltipElement != null) {
            fakeCaptionWidth += TOOLTIP_INDICATOR_WIDTH;
        }

        if (rightCaption != null && widthChanged)
            DOM.setStyleAttribute(rightCaption, "width", fakeCaptionWidth + "px");
    }

    @Override
    protected boolean isCaptionInline() {
        return true;
    }
}
/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CaptionHolder;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.toolkit.ui.client.tooltip.CubaTooltip;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VCheckBox;
import com.vaadin.client.ui.orderedlayout.CaptionPosition;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;

import java.util.List;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaSlot extends Slot implements CaptionHolder {

    protected static final String INDICATORS_CLASSNAME = "caption-indicators";

    private static final int REQUIRED_INDICATOR_WIDTH = 10;
    private static final int TOOLTIP_INDICATOR_WIDTH = 16;

    protected Element requiredElement = null;
    protected Element tooltipElement = null;
    protected Element rightCaption = null;

    private VCaption caption;

    public CubaSlot(VAbstractOrderedLayout layout, Widget widget) {
        super(layout, widget);
    }

    public void setCaption(VCaption caption){
        if (this.caption != null) {
            this.caption.removeFromParent();
        }
        this.caption = caption;
        if (caption != null) {
            // Physical attach.
            DOM.insertBefore(DOM.getParent(getWidget().getElement()), caption.getElement(), getWidget().getElement());
            Style style = caption.getElement().getStyle();
            style.setPosition(Style.Position.RELATIVE);
            style.clearTop();
            style.clearLeft();
            ((CubaCaptionWidget) caption).setCaptionHolder(this);
        }
    }

    public VCaption getCaption(){
       return caption;
    }

    public void captionUpdated(CubaCaptionWidget captionWidget){
        moveIndicatorsRight(captionWidget);
    }

    protected void moveIndicatorsRight(final CubaCaptionWidget captionWidget) {

        int fakeCaptionWidth = 0;
        boolean widthChanged = false;
        if (captionWidget.getRequiredIndicatorElement() != null && requiredElement == null) {
            if (rightCaption == null) {
                rightCaption = createRightCaption();
                getWidget().getElement().getParentElement().insertAfter(rightCaption, getWidget().getElement());
            }

            captionWidget.getElement().removeChild(captionWidget.getRequiredIndicatorElement());

            requiredElement = captionWidget.getRequiredIndicatorElement();
            if (tooltipElement != null && tooltipElement.getParentElement() == rightCaption) {
                //insert required indicator before tooltip
                rightCaption.insertBefore(requiredElement, tooltipElement);
            } else {
                rightCaption.appendChild(requiredElement);
            }

            widthChanged = true;

        } else if (captionWidget.getRequiredIndicatorElement() == null && requiredElement != null) {
            requiredElement = null;
            widthChanged = true;
        }

        if (captionWidget.getTooltipElement() != null && tooltipElement == null) {
            if (rightCaption == null) {
                rightCaption = createRightCaption();
                getWidget().getElement().getParentElement().insertAfter(rightCaption, getWidget().getElement());
            }

            if (!(getWidget() instanceof VCheckBox)) {
                captionWidget.getElement().removeChild(captionWidget.getTooltipElement());
                tooltipElement =  captionWidget.getTooltipElement();
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

        if (rightCaption != null && widthChanged) {
            DOM.setStyleAttribute(rightCaption, "width", fakeCaptionWidth + "px");
        }
    }

    protected Element createRightCaption() {
        Element rightCaption = DOM.createDiv();

        rightCaption.setClassName(VCaption.CLASSNAME);
        rightCaption.addClassName(INDICATORS_CLASSNAME);
        rightCaption.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        rightCaption.getStyle().setPosition(Style.Position.RELATIVE);
        rightCaption.getStyle().setRight(0, Style.Unit.PX);

        return rightCaption;
    }
}

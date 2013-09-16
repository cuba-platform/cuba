/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CaptionHolder;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VCheckBox;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;
import com.vaadin.shared.ui.AlignmentInfo;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaOrderedLayoutSlot extends Slot implements CaptionHolder {

    protected static final String INDICATORS_CLASSNAME = "caption-indicators";

    protected Element requiredElement = null;
    protected Element tooltipElement = null;
    protected Element errorIndicatorElement = null;

    protected Element rightCaption = null;

    protected VCaption caption;

    public CubaOrderedLayoutSlot(VAbstractOrderedLayout layout, Widget widget) {
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

    @Override
    public void captionUpdated(CubaCaptionWidget captionWidget) {
        moveIndicatorsRight(captionWidget);
    }

    protected void moveIndicatorsRight(final CubaCaptionWidget captionWidget) {
        // Indicators element always present in DOM tree of slot
        if (rightCaption == null) {
            rightCaption = createRightCaption();
            getWidget().getElement().getParentElement().insertAfter(rightCaption, getWidget().getElement());
        }

        // detach all indicators
        for (int i = 0; i < rightCaption.getChildCount(); i++) {
            rightCaption.getChild(i).removeFromParent();
        }

        /* now attach only necessary indicators */

        if (captionWidget.getRequiredIndicatorElement() != null) {
            captionWidget.getRequiredIndicatorElement().removeFromParent();

            if (!(getWidget() instanceof VCheckBox)) {
                requiredElement = captionWidget.getRequiredIndicatorElement();
                rightCaption.appendChild(requiredElement);
            }
        } else if (captionWidget.getRequiredIndicatorElement() == null && requiredElement != null) {
            requiredElement.removeFromParent();
            requiredElement = null;
        }

        if (captionWidget.getTooltipElement() != null) {
            captionWidget.getTooltipElement().removeFromParent();

            if (!(getWidget() instanceof VCheckBox)) {
                tooltipElement =  captionWidget.getTooltipElement();
                rightCaption.appendChild(tooltipElement);
            }
        } else if (captionWidget.getTooltipElement() == null && tooltipElement != null) {
            tooltipElement.removeFromParent();
            tooltipElement = null;
        }

        if (captionWidget.getErrorIndicatorElement() != null) {
            captionWidget.getErrorIndicatorElement().removeFromParent();

            if (!(getWidget() instanceof VCheckBox)) {
                errorIndicatorElement = captionWidget.getErrorIndicatorElement();
                rightCaption.appendChild(errorIndicatorElement);
            }
        } else if (captionWidget.getErrorIndicatorElement() == null && errorIndicatorElement != null) {
            errorIndicatorElement.removeFromParent();
            errorIndicatorElement = null;
        }

        if (!(getWidget() instanceof VCheckBox)) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    allocateSpaceForIndicators();
                }
            });
        }
    }

    protected void allocateSpaceForIndicators() {
        int widgetWidth = getWidget().getOffsetWidth();
        int indicatorsWidth = Util.getRequiredWidth(rightCaption);
        int captionWidth = getCaption().getElement().getOffsetWidth();
        if ((getAlignment().getBitMask() & AlignmentInfo.RIGHT.getBitMask()) == AlignmentInfo.RIGHT.getBitMask()) {
            getStyleElement().getStyle().setPaddingRight(indicatorsWidth, Style.Unit.PX);
        } else if (captionWidth >= widgetWidth + indicatorsWidth) {
            getStyleElement().getStyle().clearPaddingRight();
        } else {
            int requiredHorizontalSpace = indicatorsWidth;
            if (captionWidth > widgetWidth) {
                requiredHorizontalSpace -= (captionWidth - widgetWidth);
            }
            getStyleElement().getStyle().setPaddingRight(requiredHorizontalSpace, Style.Unit.PX);
        }
    }

    protected Element createRightCaption() {
        Element rightCaption = DOM.createDiv();

        rightCaption.setClassName(VCaption.CLASSNAME);
        rightCaption.addClassName(INDICATORS_CLASSNAME);
        rightCaption.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        rightCaption.getStyle().setPosition(Style.Position.ABSOLUTE);

        return rightCaption;
    }
}
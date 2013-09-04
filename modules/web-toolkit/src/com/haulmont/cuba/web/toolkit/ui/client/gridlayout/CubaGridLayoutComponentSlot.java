/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.gridlayout;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CaptionHolder;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.ManagedLayout;
import com.vaadin.client.ui.VCheckBox;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;
import com.vaadin.shared.ui.AlignmentInfo;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaGridLayoutComponentSlot extends ComponentConnectorLayoutSlot implements CaptionHolder {

    protected static final String INDICATORS_CLASSNAME = "caption-indicators";

    protected Element requiredElement = null;
    protected Element tooltipElement = null;

    protected Element rightCaption = null;

    protected boolean deferredLayout = false;

    public CubaGridLayoutComponentSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }

    @Override
    public void captionUpdated(CubaCaptionWidget captionWidget) {
        if (moveIndicatorsRight(captionWidget) && !deferredLayout) {
            deferredLayout = true;
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    getLayoutManager().forceLayout();

                    deferredLayout = false;
                }
            });
        }
    }

    @Override
    public void setCaption(VCaption caption) {
        super.setCaption(caption);

        if (caption instanceof CubaCaptionWidget) {
            if (isCaptionInline()) {
                moveIndicatorsRight((CubaCaptionWidget) caption);
            }
            ((CubaCaptionWidget) caption).setCaptionHolder(this);
        }
    }

    @Override
    public void positionHorizontally(double currentLocation, double allocatedSpace, double marginRight) {
        // CAUTION copied from VLayoutSlot.positionHorizontally(~)

        Style style = wrapper.getStyle();

        double availableWidth = allocatedSpace;

        VCaption caption = getCaption();
        Style captionStyle = caption != null ? caption.getElement().getStyle() : null;
        int captionWidth = getCaptionWidth();

        boolean captionAboveCompnent;
        if (caption == null) {
            captionAboveCompnent = false;
            if (isCaptionInline()) {
                style.clearPaddingLeft();
            }
            style.clearPaddingRight();
        } else {
            captionAboveCompnent = !caption.shouldBePlacedAfterComponent();
            if (!captionAboveCompnent) {
                availableWidth -= captionWidth;
                if (availableWidth < 0) {
                    availableWidth = 0;
                }
                captionStyle.clearLeft();
                captionStyle.setRight(0, Style.Unit.PX);
                style.setPaddingRight(captionWidth, Style.Unit.PX);
            } else {
                if (isCaptionInline()) {
                    availableWidth -= captionWidth;
                    if (availableWidth < 0) {
                        availableWidth = 0;
                    }
                    style.setPaddingLeft(captionWidth, Style.Unit.PX);
                }
                captionStyle.setLeft(0, Style.Unit.PX);
                captionStyle.clearRight();
                style.clearPaddingRight();
            }
        }

        // Take into account right indicators
        if (rightCaption != null) {
            double indicatorsWidth = Util.getRequiredWidth(rightCaption);
            availableWidth -= indicatorsWidth;
            if (availableWidth < 0) {
                availableWidth = 0;
            }
            style.setPaddingRight(indicatorsWidth, Style.Unit.PX);
        }

        if (marginRight > 0) {
            style.setMarginRight(marginRight, Style.Unit.PX);
        } else {
            style.clearMarginRight();
        }

        if (isRelativeWidth()) {
            style.setPropertyPx("width", (int) availableWidth);
        } else {
            style.clearProperty("width");
        }

        double allocatedContentWidth = 0;
        if (isRelativeWidth()) {
            String percentWidth = getWidget().getElement().getStyle()
                    .getWidth();
            double percentage = parsePercent(percentWidth);
            allocatedContentWidth = availableWidth * (percentage / 100);
            reportActualRelativeWidth(Math.round((float) allocatedContentWidth));
        }

        AlignmentInfo alignment = getAlignment();
        if (!alignment.isLeft()) {
            double usedWidth;
            if (isRelativeWidth()) {
                if (isCaptionInline()) {
                    usedWidth = allocatedContentWidth + captionWidth;
                } else {
                    usedWidth = allocatedContentWidth;
                }
            } else {
                usedWidth = getWidgetWidth();
            }
            if (alignment.isHorizontalCenter()) {
                currentLocation += (allocatedSpace - usedWidth) / 2d;
                if (captionAboveCompnent) {
                    captionStyle.setLeft(
                            Math.round(usedWidth - captionWidth) / 2, Style.Unit.PX);
                }
            } else {
                currentLocation += (allocatedSpace - usedWidth);
                if (captionAboveCompnent) {
                    captionStyle.setLeft(Math.round(usedWidth - captionWidth),
                            Style.Unit.PX);
                }
            }
        } else {
            if (captionAboveCompnent) {
                captionStyle.setLeft(0, Style.Unit.PX);
            }
        }

        style.setLeft(Math.round(currentLocation), Style.Unit.PX);
    }

    @Override
    public void positionVertically(double currentLocation, double allocatedSpace, double marginBottom) {
        // CAUTION copied from VLayoutSlot.positionVertically(~)
        Style style = wrapper.getStyle();

        double contentHeight = allocatedSpace;

        int captionHeight;
        VCaption caption = getCaption();
        if (caption == null || caption.shouldBePlacedAfterComponent() || isCaptionInline()) {
            style.clearPaddingTop();
            captionHeight = 0;
        } else {
            captionHeight = getCaptionHeight();
            contentHeight -= captionHeight;
            if (contentHeight < 0) {
                contentHeight = 0;
            }
            style.setPaddingTop(captionHeight, Style.Unit.PX);
        }

        if (marginBottom > 0) {
            style.setMarginBottom(marginBottom, Style.Unit.PX);
        } else {
            style.clearMarginBottom();
        }

        if (isRelativeHeight()) {
            style.setHeight(contentHeight, Style.Unit.PX);
        } else {
            style.clearHeight();
        }

        double allocatedContentHeight = 0;
        if (isRelativeHeight()) {
            String height = getWidget().getElement().getStyle().getHeight();
            double percentage = parsePercent(height);
            allocatedContentHeight = contentHeight * (percentage / 100);
            reportActualRelativeHeight(Math
                    .round((float) allocatedContentHeight));
        }

        AlignmentInfo alignment = getAlignment();
        if (!alignment.isTop()) {
            double usedHeight;
            if (isRelativeHeight()) {
                if (isCaptionInline()) {
                    usedHeight = allocatedContentHeight;
                } else {
                    usedHeight = captionHeight + allocatedContentHeight;
                }
            } else {
                usedHeight = getUsedHeight();
            }
            if (alignment.isVerticalCenter()) {
                currentLocation += (allocatedSpace - usedHeight) / 2d;
            } else {
                currentLocation += (allocatedSpace - usedHeight);
            }
        }

        style.setTop(currentLocation, Style.Unit.PX);
    }

    @Override
    public int getUsedWidth() {
        int widgetWidth = getWidgetWidth();
        if (getCaption() == null) {
            return widgetWidth;
        } else if (getCaption().shouldBePlacedAfterComponent() || isCaptionInline()) {
            widgetWidth += getCaptionWidth();
            if (rightCaption != null) {
                widgetWidth += Util.getRequiredWidth(rightCaption);
            }
            return widgetWidth;
        } else {
            if (rightCaption != null) {
                widgetWidth += Util.getRequiredWidth(rightCaption);
            }
            return Math.max(widgetWidth, getCaptionWidth());
        }
    }

    @Override
    public int getUsedHeight() {
        int widgetHeight = getWidgetHeight();
        if (getCaption() == null) {
            return widgetHeight;
        } else if (getCaption().shouldBePlacedAfterComponent() || isCaptionInline()) {
            return Math.max(widgetHeight, getCaptionHeight());
        } else {
            return widgetHeight + getCaptionHeight();
        }
    }

    protected boolean moveIndicatorsRight(final CubaCaptionWidget captionWidget) {
        // todo move error indicator right

        int fakeCaptionWidth = 0;
        boolean widthChanged = false;
        if (captionWidget.getRequiredIndicatorElement() != null && requiredElement == null) {
            if (rightCaption == null) {
                rightCaption = createRightCaption();
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
            requiredElement.removeFromParent();
            requiredElement = null;
            widthChanged = true;
        }

        if (captionWidget.getTooltipElement() != null && tooltipElement == null) {
            if (rightCaption == null) {
                rightCaption = createRightCaption();
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
            tooltipElement.removeFromParent();
            tooltipElement = null;
            widthChanged = true;
        }

        if (requiredElement != null) {
            fakeCaptionWidth += Util.getRequiredWidth(requiredElement);
        }
        if (tooltipElement != null) {
            fakeCaptionWidth += Util.getRequiredWidth(tooltipElement);
        }

        if (widthChanged) {
            if (rightCaption != null) {
                DOM.setStyleAttribute(rightCaption, "width", fakeCaptionWidth + "px");
            }
            return true;
        }

        return false;
    }

    protected Element createRightCaption() {
        Element rightCaption = DOM.createDiv();

        rightCaption.setClassName(VCaption.CLASSNAME);
        rightCaption.addClassName(INDICATORS_CLASSNAME);
        rightCaption.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        rightCaption.getStyle().setPosition(Style.Position.ABSOLUTE);
        return rightCaption;
    }

    protected boolean isCaptionInline() {
        return false;
    }
}
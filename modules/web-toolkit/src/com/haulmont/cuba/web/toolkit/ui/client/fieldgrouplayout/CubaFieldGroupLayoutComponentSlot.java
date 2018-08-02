/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CaptionHolder;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.toolkit.ui.client.gridlayout.CubaGridLayoutSlot;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.HasContextHelpConnector;
import com.vaadin.client.ui.ManagedLayout;
import com.vaadin.client.ui.VCheckBox;
import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.shared.ui.AlignmentInfo;

/**
 * Component slot with horizontal layout for caption and component
 */
public class CubaFieldGroupLayoutComponentSlot extends CubaGridLayoutSlot
        implements CaptionHolder, EventListener {

    protected static final String INDICATORS_CLASSNAME = "caption-indicators";

    protected Element requiredElement = null;
    protected Element contextHelpIndicatorElement = null;
    protected Element errorIndicatorElement = null;

    protected Element rightCaption = null;

    protected boolean useInlineCaption = true;

    public CubaFieldGroupLayoutComponentSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);

        // set line-height 25px for all captions exclude captions for CheckBox and Label
        getWrapperElement().addClassName("c-full-height-widget");
    }

    protected boolean isCaptionInline() {
        return useInlineCaption;
    }

    public void setCaptionInline(boolean useInlineCaption) {
        this.useInlineCaption = useInlineCaption;

        if (useInlineCaption) {
            getWrapperElement().addClassName("inline");
        } else {
            getWrapperElement().removeClassName("inline");
        }
    }

    @Override
    public void captionUpdated(CubaCaptionWidget captionWidget) {
        if (isCaptionInline()) {
            moveIndicatorsRight(captionWidget);
        }
    }

    @Override
    public void setCaption(VCaption caption) {
        if (!isCaptionInline()) {
            super.setCaption(caption);
        } else {
            if (rightCaption != null) {
                getLayoutManager().unregisterDependency(getLayout(), rightCaption);

                rightCaption.removeFromParent();
                rightCaption = null;
            }

            super.setCaption(caption);

            if (caption instanceof CubaCaptionWidget) {
                moveIndicatorsRight((CubaCaptionWidget) caption);
                ((CubaCaptionWidget) caption).setCaptionHolder(this);
            }
        }
    }

    @Override
    public void positionHorizontally(double currentLocation, double allocatedSpace, double marginRight) {
        if (!isCaptionInline()) {
            super.positionHorizontally(currentLocation, allocatedSpace, marginRight);
            return;
        }

        // CAUTION copied from VLayoutSlot.positionHorizontally(~)

        Style style = wrapper.getStyle();

        double availableWidth = allocatedSpace;

        VCaption caption = getCaption();
        Style captionStyle = caption != null ? caption.getElement().getStyle() : null;
        int captionWidth = getCaptionWidth();

        boolean clearCaptionRight = false;

        boolean captionAboveComponent;
        if (caption == null) {
            captionAboveComponent = false;
            style.clearPaddingLeft();

            clearCaptionRight = true;
        } else {
            captionAboveComponent = !caption.shouldBePlacedAfterComponent();
            if (!captionAboveComponent) {
                availableWidth -= captionWidth;
                if (availableWidth < 0) {
                    availableWidth = 0;
                }
                captionStyle.clearLeft();
                captionStyle.setRight(0, Style.Unit.PX);
                style.setPaddingRight(captionWidth, Style.Unit.PX);
            } else {
                availableWidth -= captionWidth;
                if (availableWidth < 0) {
                    availableWidth = 0;
                }
                style.setPaddingLeft(captionWidth, Style.Unit.PX);
                captionStyle.setLeft(0, Style.Unit.PX);
                captionStyle.clearRight();

                clearCaptionRight = true;
            }
        }

        // Take into account right indicators
        double indicatorsWidth = 0;
        if (rightCaption != null) {
            indicatorsWidth = WidgetUtil.getRequiredWidth(rightCaption);
            availableWidth -= indicatorsWidth;
            if (availableWidth < 0) {
                availableWidth = 0;
            }
            style.setPaddingRight(indicatorsWidth, Style.Unit.PX);
        } else if (clearCaptionRight) {
            style.clearPaddingRight();
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
                    usedWidth = allocatedContentWidth + indicatorsWidth + captionWidth;
                } else {
                    usedWidth = allocatedContentWidth + indicatorsWidth;
                }
            } else {
                usedWidth = getWidgetWidth() + indicatorsWidth;
            }
            if (alignment.isHorizontalCenter()) {
                currentLocation += (allocatedSpace - usedWidth) / 2d;
                if (captionAboveComponent) {
                    captionStyle.setLeft(
                            Math.round(usedWidth - captionWidth) / 2, Style.Unit.PX);
                }
            } else {
                currentLocation += (allocatedSpace - usedWidth);
                if (captionAboveComponent) {
                    captionStyle.setLeft(Math.round(usedWidth - captionWidth),
                            Style.Unit.PX);
                }
            }
        } else {
            if (captionAboveComponent) {
                captionStyle.setLeft(0, Style.Unit.PX);
            }
        }

        style.setLeft(Math.round(currentLocation), Style.Unit.PX);
    }

    @Override
    public void positionVertically(double currentLocation, double allocatedSpace, double marginBottom) {
        if (!isCaptionInline()) {
            super.positionVertically(currentLocation, allocatedSpace, marginBottom);
            return;
        }

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
        if (!isCaptionInline()) {
            return super.getUsedWidth();
        }

        int widgetWidth = getWidgetWidth();
        if (getCaption() == null) {
            return widgetWidth;
        } else if (getCaption().shouldBePlacedAfterComponent() || isCaptionInline()) {
            widgetWidth += getCaptionWidth();
            if (rightCaption != null) {
                widgetWidth += WidgetUtil.getRequiredWidth(rightCaption);
            }
            return widgetWidth;
        } else {
            if (rightCaption != null) {
                widgetWidth += WidgetUtil.getRequiredWidth(rightCaption);
            }
            return Math.max(widgetWidth, getCaptionWidth());
        }
    }

    @Override
    public int getUsedHeight() {
        if (!isCaptionInline()) {
            return super.getUsedHeight();
        }

        int widgetHeight = getWidgetHeight();
        if (getCaption() == null) {
            return widgetHeight;
        } else if (getCaption().shouldBePlacedAfterComponent() || isCaptionInline()) {
            return Math.max(widgetHeight, getCaptionHeight());
        } else {
            return widgetHeight + getCaptionHeight();
        }
    }

    public int getIndicatorsWidth() {
        if (rightCaption != null) {
            return WidgetUtil.getRequiredWidth(rightCaption);
        } else {
            return 0;
        }
    }

    public void setIndicatorsWidth(String width) {
        if (rightCaption != null) {
            rightCaption.getStyle().setProperty("width", width);
        }
    }

    public void resetIndicatorsWidth() {
        if (rightCaption != null) {
            rightCaption.getStyle().clearWidth();
        }
    }

    protected void moveIndicatorsRight(final CubaCaptionWidget captionWidget) {
        // Indicators element always present in DOM tree of slot
        if (rightCaption == null) {
            rightCaption = createRightCaption();
            getWrapperElement().insertAfter(rightCaption, getWidget().getElement());
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
        } else if (requiredElement != null) {
            requiredElement.removeFromParent();
            requiredElement = null;
        }

        if (captionWidget.getContextHelpIndicatorElement() != null) {
            captionWidget.getContextHelpIndicatorElement().removeFromParent();
            contextHelpIndicatorElement = captionWidget.getContextHelpIndicatorElement();
            rightCaption.appendChild(contextHelpIndicatorElement);

            DOM.sinkEvents(contextHelpIndicatorElement, Event.ONCLICK);
            DOM.setEventListener(contextHelpIndicatorElement, this);
        } else {
            if (contextHelpIndicatorElement != null) {
                contextHelpIndicatorElement.removeFromParent();
                DOM.setEventListener(contextHelpIndicatorElement, null);
                contextHelpIndicatorElement = null;
            }
        }

        if (captionWidget.getErrorIndicatorElement() != null) {
            captionWidget.getErrorIndicatorElement().removeFromParent();

            if (!(getWidget() instanceof VCheckBox)) {
                errorIndicatorElement = captionWidget.getErrorIndicatorElement();
                rightCaption.appendChild(errorIndicatorElement);
            }
        } else if (errorIndicatorElement != null) {
            errorIndicatorElement.removeFromParent();
            errorIndicatorElement = null;
        }
    }

    protected Element createRightCaption() {
        Element rightCaption = DOM.createDiv();

        getLayoutManager().registerDependency((ManagedLayout) getChild().getParent(), rightCaption);

        rightCaption.setClassName(VCaption.CLASSNAME);
        rightCaption.addClassName(INDICATORS_CLASSNAME);
        rightCaption.getStyle().setDisplay(Style.Display.INLINE_BLOCK);
        rightCaption.getStyle().setPosition(Style.Position.ABSOLUTE);

        return rightCaption;
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
            Element target = Element.as(event.getEventTarget());
            ComponentConnector componentConnector = Util.findConnectorFor(getWidget());
            if (target == contextHelpIndicatorElement
                    && componentConnector instanceof HasContextHelpConnector) {
                HasContextHelpConnector connector = (HasContextHelpConnector) componentConnector;
                if (hasContextHelpIconListeners(componentConnector.getState())) {
                    connector.contextHelpIconClick(event);
                }
            }
        }
    }

    protected boolean hasContextHelpIconListeners(SharedState state) {
        return state.registeredEventListeners != null
                && state.registeredEventListeners.contains(AbstractFieldState.CONTEXT_HELP_ICON_CLICK_EVENT);
    }
}
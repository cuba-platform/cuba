/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tooltip;

import com.google.gwt.aria.client.Id;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaWidget;
import com.vaadin.client.*;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.client.ui.checkbox.CheckBoxConnector;
import com.vaadin.client.ui.table.TableConnector;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaTooltip extends VTooltip {

    public static final String REQUIRED_INDICATOR = "v-required-field-indicator";

    public CubaTooltip() {
        tooltipEventHandler = new CubaTooltipEventHandler();
    }


    protected void showTooltip(boolean forceShow) {
        // Close current tooltip
        if (isShowing()) {
            closeNow();
        }

        // Schedule timer for showing the tooltip according to if it was
        // recently closed or not.
        int timeout = 0;
        if (!forceShow) {
            timeout = justClosed ? getQuickOpenDelay() : getOpenDelay();
        }
        showTimer.schedule(timeout);
        opening = true;
    }

    /* CAUTION copied from super class with small changes */
    protected void show(TooltipInfo info) {
        boolean hasContent = false;
        boolean isErrorMsgEmpty = info.getErrorMessage() == null || info.getErrorMessage().isEmpty();
        if (!isErrorMsgEmpty) {
            Element errorMsgTestEl = DOM.createDiv();
            DOM.setInnerHTML(errorMsgTestEl, info.getErrorMessage());
            isErrorMsgEmpty = DOM.getChild(errorMsgTestEl, 0).getInnerHTML().isEmpty();
        }
        if (!isErrorMsgEmpty) {
            em.setVisible(true);
            em.updateMessage(info.getErrorMessage());
            hasContent = true;
        } else {
            em.setVisible(false);
        }
        if (info.getTitle() != null && !"".equals(info.getTitle())) {
            DOM.setInnerHTML(description, info.getTitle());
            DOM.setStyleAttribute(description, "display", "");
            hasContent = true;
        } else {
            DOM.setInnerHTML(description, "");
            DOM.setStyleAttribute(description, "display", "none");
        }
        if (hasContent) {
            // Issue #8454: With IE7 the tooltips size is calculated based on
            // the last tooltip's position, causing problems if the last one was
            // in the right or bottom edge. For this reason the tooltip is moved
            // first to 0,0 position so that the calculation goes correctly.
            setPopupPosition(0, 0);
            setPopupPositionAndShow(new PositionCallback() {
                @Override
                public void setPosition(int offsetWidth, int offsetHeight) {

                    if (offsetWidth > getMaxWidth()) {
                        setWidth(getMaxWidth() + "px");

                        // Check new height and width with reflowed content
                        offsetWidth = getOffsetWidth();
                        offsetHeight = getOffsetHeight();
                    }

                    int x = tooltipEventMouseX + 10 + Window.getScrollLeft();
                    int y = tooltipEventMouseY + 10 + Window.getScrollTop();

                    if (x + offsetWidth + MARGIN - Window.getScrollLeft() > Window
                            .getClientWidth()) {
                        x = Window.getClientWidth() - offsetWidth - MARGIN
                                + Window.getScrollLeft();
                    }

                    if (y + offsetHeight + MARGIN - Window.getScrollTop() > Window
                            .getClientHeight()) {
                        y = tooltipEventMouseY - 5 - offsetHeight
                                + Window.getScrollTop();
                        if (y - Window.getScrollTop() < 0) {
                            // tooltip does not fit on top of the mouse either,
                            // put it at the top of the screen
                            y = Window.getScrollTop();
                        }
                    }

                    setPopupPosition(x, y);
                    sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
                }
            });
        } else {
            hide();
        }
    }

    public class CubaTooltipEventHandler extends TooltipEventHandler {

        private ComponentConnector currentConnector = null;

        private boolean isTooltipElement(Element element) {
            return (element.getClassName().equals(REQUIRED_INDICATOR)
                    || element.getClassName().equals(CubaCaptionWidget.TOOLTIP_CLASSNAME));
        }

        protected boolean resolveConnector(Element element) {

            if (isTooltipElement(element)) {
                element = element.getParentElement().cast();

                int index = DOM.getChildIndex(element.getParentElement().<Element>cast(), element);
                int indexOfComponent = index == 0 ? index + 1 : index - 1;
                element = DOM.getChild(element.getParentElement().<Element>cast(), indexOfComponent);
                //special case for ResizableTextArea.
                if (CubaResizableTextAreaWidget.TEXT_AREA_WRAPPER.equals(element.getClassName())) {
                    element = DOM.getChild(element, 0);
                }
            }

            ApplicationConnection ac = getApplicationConnection();
            ComponentConnector connector = Util.getConnectorForElement(ac,
                    RootPanel.get(), element);

            // Try to find first connector with proper tooltip info
            TooltipInfo info = null;
            while (connector != null) {

                info = connector.getTooltipInfo(element);

                if (info != null && info.hasMessage()) {
                    break;
                }

                if (!(connector.getParent() instanceof ComponentConnector)) {
                    connector = null;
                    info = null;
                    break;
                }
                connector = (ComponentConnector) connector.getParent();
            }

            if (connector != null && info != null) {
                assert connector.hasTooltip() : "getTooltipInfo for "
                        + Util.getConnectorString(connector)
                        + " returned a tooltip even though hasTooltip claims there are no tooltips for the connector.";
                currentTooltipInfo = info;
                currentConnector = connector;

                return true;
            }

            return false;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (isTooltipElement(event.getNativeEvent().getEventTarget().<Element>cast())) {
                closeNow();
                handleShowHide(event, false);
            } else {
                hideTooltip();
            }
        }

        protected void handleShowHide(DomEvent domEvent, boolean isFocused) {
            Event event = Event.as(domEvent.getNativeEvent());
            com.google.gwt.dom.client.Element element = Element.as(event
                    .getEventTarget());

            // We can ignore move event if it's handled by move or over already
            if (currentElement == element && currentIsFocused == isFocused && domEvent instanceof MouseMoveEvent) {
                return;
            }

            boolean connectorAndTooltipFound = resolveConnector((com.google.gwt.user.client.Element) element);
            if (!connectorAndTooltipFound) {
                //close tooltip only if it is from button or checkbox
                if (currentConnector instanceof ButtonConnector || currentConnector instanceof CheckBoxConnector) {
                    if (isShowing()) {
                        handleHideEvent();
                        Roles.getButtonRole()
                                .removeAriaDescribedbyProperty(element);
                    } else {
                        currentTooltipInfo = null;
                        currentConnector = null;
                    }
                }

            } else {
                updatePosition(event, isFocused);
                if ((domEvent instanceof ClickEvent && !isStandardTooltip(currentConnector))
                        || (!(domEvent instanceof ClickEvent) && isStandardTooltip(currentConnector))) {
                    if (isShowing()) {
                        replaceCurrentTooltip();
                        Roles.getTooltipRole().removeAriaDescribedbyProperty(
                                currentElement);
                    } else {
                        if (isStandardTooltip(currentConnector)) {
                            showTooltip(false);
                        } else {
                            showTooltip(true);
                        }
                    }

                    Roles.getTooltipRole().setAriaDescribedbyProperty(element,
                            Id.of(uniqueId));
                }
            }

            currentIsFocused = isFocused;
            currentElement = element;
        }

        private boolean isStandardTooltip(ComponentConnector connector) {
            return (connector instanceof ButtonConnector
                    || connector instanceof CheckBoxConnector
                    || connector instanceof TableConnector);
        }

    }
}

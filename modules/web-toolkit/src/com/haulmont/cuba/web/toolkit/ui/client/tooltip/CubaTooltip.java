/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tooltip;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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
        if (isActuallyVisible()) {
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

    public class CubaTooltipEventHandler extends TooltipEventHandler {

        private ComponentConnector currentConnector = null;

        private boolean isTooltipElement(Element element) {
            return (element.getClassName().equals(REQUIRED_INDICATOR)
                    || element.getClassName().equals(CubaCaptionWidget.TOOLTIP_CLASSNAME));
        }

        @Override
        protected TooltipInfo getTooltipFor(Element element) {
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
            //special case for ResizableTextArea
            if (CubaResizableTextAreaWidget.RESIZE_ELEMENT.equals(element.getClassName())) {
                element = DOM.getChild(element.getParentElement().<Element>cast(), 0);
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
                currentConnector = connector;

                return info;
            }

            return null;
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

        @Override
        protected void handleShowHide(DomEvent domEvent, boolean isFocused) {
            Event event = Event.as(domEvent.getNativeEvent());
            Element element = Element.as(event.getEventTarget());

            // We can ignore move event if it's handled by move or over already
            if (currentElement == element && handledByFocus) {
                return;
            }

            TooltipInfo info = getTooltipFor(element);
            if (info == null) {
                // close tooltip only if it is from button or checkbox
                if (currentConnector instanceof ButtonConnector || currentConnector instanceof CheckBoxConnector) {
                    if (isActuallyVisible()) {
                        handleHideEvent();
                    } else {
                        currentConnector = null;
                    }
                }
            } else {
                if ((domEvent instanceof ClickEvent && !isStandardTooltip(currentConnector))
                        || (!(domEvent instanceof ClickEvent) && isStandardTooltip(currentConnector))) {
                    setTooltipText(info);
                    updatePosition(event, isFocused);

                    if (isActuallyVisible()) {
                        closeNow();
                    }

                    if (isStandardTooltip(currentConnector)) {
                        showTooltip(false);
                    } else {
                        showTooltip(true);
                    }
                }
            }

            handledByFocus = isFocused;
            currentElement = element;
        }

        protected boolean isStandardTooltip(ComponentConnector connector) {
            return (connector instanceof ButtonConnector
                    || connector instanceof CheckBoxConnector
                    || connector instanceof TableConnector);
        }
    }
}
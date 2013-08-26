/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tooltip;

import com.google.gwt.aria.client.Id;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.vaadin.client.*;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.client.ui.checkbox.CheckBoxConnector;

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
                element = DOM.getChild(element.getParentElement().<Element>cast(), index - 1);
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
            return (connector instanceof ButtonConnector || connector instanceof CheckBoxConnector);
        }

    }
}

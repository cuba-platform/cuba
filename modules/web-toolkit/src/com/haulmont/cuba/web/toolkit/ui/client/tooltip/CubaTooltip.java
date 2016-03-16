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

package com.haulmont.cuba.web.toolkit.ui.client.tooltip;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea.CubaResizableTextAreaWrapperWidget;
import com.vaadin.client.*;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.client.ui.gridlayout.GridLayoutConnector;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;
import com.vaadin.client.ui.orderedlayout.Slot;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class CubaTooltip extends VTooltip {

    public static final String REQUIRED_INDICATOR = "v-required-field-indicator";

    // If required indicators are not visible we show toolip on mouse hover otherwise only by mouse click
    protected static Boolean requiredIndicatorVisible = null;

    protected List<String> excludeStyles = new ArrayList<String>();

    public CubaTooltip() {
        tooltipEventHandler = new CubaTooltipEventHandler();

        excludeStyles.add(CubaResizableTextAreaWrapperWidget.RESIZE_ELEMENT);
    }

    protected void showTooltip(boolean forceShow) {
        // Schedule timer for showing the tooltip according to if it
        // was recently closed or not.
        int timeout = 0;
        if (!forceShow) {
            timeout = justClosed ? getQuickOpenDelay() : getOpenDelay();
        }
        if (timeout == 0) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    showTooltip();
                }
            });
        } else {
            showTimer.schedule(timeout);
            opening = true;
        }
    }

    public static void checkRequiredInicatorMode() {
        requiredIndicatorVisible = null;
    }

    public class CubaTooltipEventHandler extends TooltipEventHandler {

        private ComponentConnector currentConnector = null;

        private boolean isTooltipElement(Element element) {
            return (element.getClassName().equals(REQUIRED_INDICATOR)
                    || element.getClassName().equals(CubaCaptionWidget.TOOLTIP_CLASSNAME));
        }

        protected void checkRequiredIndicatorVisible() {
            if (requiredIndicatorVisible == null) {
                Element requiredIndicatorFake = DOM.createDiv();
                requiredIndicatorFake.setClassName(REQUIRED_INDICATOR);
                requiredIndicatorFake.getStyle().setPosition(Style.Position.ABSOLUTE);

                String rootPanelId = ac.getConfiguration().getRootPanelId();
                Element rootPanel = Document.get().getElementById(rootPanelId);
                rootPanel.appendChild(requiredIndicatorFake);

                String display = new ComputedStyle(requiredIndicatorFake).getProperty("display");

                requiredIndicatorVisible = !"none".equals(display);

                rootPanel.removeChild(requiredIndicatorFake);
            }
        }

        @Override
        protected TooltipInfo getTooltipFor(Element element) {
            checkRequiredIndicatorVisible();

            if (!requiredIndicatorVisible) {
                if (excludeStyles.contains(element.getClassName())) {
                    return null;
                } else {
                    return super.getTooltipFor(element);
                }
            }

            if (isTooltipElement(element)) {
                element = element.getParentElement().cast();

                int index = DOM.getChildIndex(element.getParentElement().<Element>cast(), element);
                int indexOfComponent = index == 0 ? index + 1 : index - 1;
                element = DOM.getChild(element.getParentElement().<Element>cast(), indexOfComponent);
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

            if (connector != null) {
                assert connector.hasTooltip() : "getTooltipInfo for "
                        + Util.getConnectorString(connector)
                        + " returned a tooltip even though hasTooltip claims there are no tooltips for the connector.";
                currentConnector = connector;

                return info;
            }

            return null;
        }

        @Override
        public void onMouseDown(MouseDownEvent event) {
            checkRequiredIndicatorVisible();

            if (requiredIndicatorVisible) {
                if (isTooltipElement(event.getNativeEvent().getEventTarget().<Element>cast())) {
                    closeNow();
                    handleShowHide(event, false);
                } else {
                    hideTooltip();
                }
            }
        }

        @Override
        protected void handleShowHide(DomEvent domEvent, boolean isFocused) {
            checkRequiredIndicatorVisible();

            if (!requiredIndicatorVisible) {
                super.handleShowHide(domEvent, isFocused);
            }

            // CAUTION copied from parent with changes
            Event event = Event.as(domEvent.getNativeEvent());
            Element element = Element.as(event.getEventTarget());

            // We can ignore move event if it's handled by move or over already
            if (currentElement == element && handledByFocus) {
                return;
            }

            // If the parent (sub)component already has a tooltip open and it
            // hasn't changed, we ignore the event.
            // TooltipInfo contains a reference to the parent component that is
            // checked in it's equals-method.
            if (currentElement != null && isTooltipOpen()) {
                TooltipInfo currentTooltip = getTooltipFor(currentElement);
                TooltipInfo newTooltip = getTooltipFor(element);
                if (currentTooltip != null && currentTooltip.equals(newTooltip)) {
                    return;
                }
            }

            TooltipInfo info = getTooltipFor(element);
            if (info == null) {
                handleHideEvent();
                currentConnector = null;
            } else {
                boolean hasTooltipIndicator = hasIndicators(currentConnector);
                boolean elementIsIndicator = elementIsIndicator(element);

                if ((hasTooltipIndicator && elementIsIndicator) || (!hasTooltipIndicator)) {
                    if (closing) {
                        closeTimer.cancel();
                        closing = false;
                    }

                    if (isTooltipOpen()) {
                        closeNow();
                    }

                    setTooltipText(info);
                    updatePosition(event, isFocused);

                    if (BrowserInfo.get().isIOS()) {
                        element.focus();
                    }

                    showTooltip(domEvent instanceof MouseDownEvent && elementIsIndicator);
                }
            }

            handledByFocus = isFocused;
            currentElement = element;
        }

        protected boolean elementIsIndicator(Element relativeElement) {
            return relativeElement != null
                    && ("v-required-field-indicator".equals(relativeElement.getClassName())
                    || "cuba-tooltip-button".equals(relativeElement.getClassName()));
        }

        protected boolean hasIndicators(ComponentConnector connector) {
            if (connector == null || connector.getWidget() == null) {
                return false;
            }

            Widget parentWidget = connector.getWidget().getParent();

            if (parentWidget instanceof Slot) {
                Slot slot = (Slot) parentWidget;
                if (slot.getCaptionElement() != null) {
                    com.google.gwt.user.client.Element captionElement = slot.getCaptionElement();
                    for (int i = 0; i < captionElement.getChildCount(); i++) {
                        Node child = captionElement.getChild(i);
                        if (child instanceof Element
                                && (elementIsIndicator(((Element) child)))) {
                            return true;
                        }
                    }
                }
            } else if (connector.getParent() instanceof GridLayoutConnector) {
                GridLayoutConnector gridLayoutConnector = (GridLayoutConnector) connector.getParent();
                VGridLayout gridWidget = gridLayoutConnector.getWidget();
                VGridLayout.Cell cell = gridWidget.widgetToCell.get(connector.getWidget());

                ComponentConnectorLayoutSlot slot = cell.slot;
                if (slot != null) {
                    VCaption caption = slot.getCaption();
                    if (caption != null) {
                        com.google.gwt.user.client.Element captionElement = caption.getElement();
                        for (int i = 0; i < captionElement.getChildCount(); i++) {
                            Node child = captionElement.getChild(i);
                            if (child instanceof Element
                                    && (elementIsIndicator(((Element) child)))) {
                                return true;
                            }
                        }
                    }

                    if (caption instanceof CubaCaptionWidget) {
                        CubaCaptionWidget cubaCaptionWidget = (CubaCaptionWidget) caption;
                        if (cubaCaptionWidget.getRequiredIndicatorElement() != null
                                || cubaCaptionWidget.getTooltipElement() != null) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }
}
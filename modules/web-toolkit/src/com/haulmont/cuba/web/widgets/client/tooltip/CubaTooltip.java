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

package com.haulmont.cuba.web.widgets.client.tooltip;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.checkbox.CubaCheckBoxWidget;
import com.haulmont.cuba.web.widgets.client.resizabletextarea.CubaResizableTextAreaWrapperWidget;
import com.vaadin.client.*;

import static com.haulmont.cuba.web.widgets.client.caption.CubaCaptionWidget.CONTEXT_HELP_CLASSNAME;

public class CubaTooltip extends VTooltip {

    public static final String REQUIRED_INDICATOR = "v-required-field-indicator";
    public static final String ERROR_INDICATOR = "v-errorindicator";

    // If required indicators are not visible we show tooltip on mouse hover otherwise only by mouse click
    protected static Boolean requiredIndicatorVisible = null;

    protected Element contextHelpElement = DOM.createDiv();

    public CubaTooltip() {
        contextHelpElement.setClassName("c-tooltip-context-help");
        DOM.appendChild(getWidget().getElement(), contextHelpElement);
        tooltipEventHandler = new CubaTooltipEventHandler();
    }

    protected void showTooltip(boolean forceShow) {
        // Schedule timer for showing the tooltip according to if it
        // was recently closed or not.
        int timeout = 0;
        if (!forceShow) {
            timeout = justClosed ? getQuickOpenDelay() : getOpenDelay();
        }
        if (timeout == 0) {
            Scheduler.get().scheduleDeferred(this::showTooltip);
        } else {
            showTimer.schedule(timeout);
            opening = true;
        }
    }

    public static void checkRequiredIndicatorMode() {
        requiredIndicatorVisible = null;
    }

    @Override
    public void connectHandlersToWidget(Widget widget) {
        Profiler.enter("VTooltip.connectHandlersToWidget");
        widget.addDomHandler(tooltipEventHandler, MouseOutEvent.getType());
        widget.addDomHandler(tooltipEventHandler, MouseDownEvent.getType());
        widget.addDomHandler(tooltipEventHandler, KeyDownEvent.getType());

        if (!BrowserInfo.get().isIOS()) {
            widget.addDomHandler(tooltipEventHandler, MouseMoveEvent.getType());
            widget.addDomHandler(tooltipEventHandler, FocusEvent.getType());
            widget.addDomHandler(tooltipEventHandler, BlurEvent.getType());
        }
        Profiler.leave("VTooltip.connectHandlersToWidget");
    }

    @Override
    protected void setTooltipText(TooltipInfo info) {
        super.setTooltipText(info);

        if (info.getTitle() != null && !info.getTitle().isEmpty()) {
            description.getElement().removeAttribute("aria-hidden");
        } else {
            description.getElement().setAttribute("aria-hidden", "true");
        }

        String contextHelp = info.getContextHelp();
        if (contextHelp != null && !contextHelp.isEmpty()) {
            if (info.isContextHelpHtmlEnabled()) {
                contextHelpElement.setInnerHTML(contextHelp);
            } else {
                if (contextHelp.contains("\n")) {
                    contextHelp = WidgetUtil.escapeHTML(contextHelp).replace("\n", "<br/>");
                    contextHelpElement.setInnerHTML(contextHelp);
                } else {
                    contextHelpElement.setInnerText(contextHelp);
                }
            }
            contextHelpElement.getStyle().clearDisplay();
        } else {
            contextHelpElement.setInnerHTML("");
            contextHelpElement.getStyle().setDisplay(Style.Display.NONE);
        }
    }

    @Override
    public void hide() {
        contextHelpElement.setInnerHTML("");
        super.hide();
    }

    public class CubaTooltipEventHandler extends TooltipEventHandler {

        protected ComponentConnector currentConnector = null;

        protected boolean isTooltipElement(Element element) {
            return (isRequiredIndicator(element)
                    || isContextHelpElement(element));
        }

        protected boolean isRequiredIndicator(Element element) {
            return REQUIRED_INDICATOR.equals(element.getClassName())
                    || ERROR_INDICATOR.equals(element.getClassName());
        }

        protected boolean isContextHelpElement(Element element) {
            return CONTEXT_HELP_CLASSNAME.equals(element.getClassName());
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

        protected boolean isClassNameExcluded(String className) {
            return CubaResizableTextAreaWrapperWidget.RESIZE_ELEMENT.equals(className);
        }

        @Override
        protected TooltipInfo getTooltipFor(Element element) {
            Element originalElement = element;

            if (isClassNameExcluded(element.getClassName())) {
                return null;
            }

            if (isTooltipElement(element)) {
                element = element.getParentElement().cast();

                Widget widget = WidgetUtil.findWidget(element);
                if (!(widget instanceof CubaCheckBoxWidget)) {
                    int index = DOM.getChildIndex(element.getParentElement().cast(), element);
                    int indexOfComponent = index == 0 ? index + 1 : index - 1;
                    element = DOM.getChild(element.getParentElement().cast(), indexOfComponent);
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

            if (connector != null) {
                assert connector.hasTooltip() : "getTooltipInfo for "
                        + Util.getConnectorString(connector)
                        + " returned a tooltip even though hasTooltip claims there are no tooltips for the connector.";
                currentConnector = connector;

                return updateTooltip(info, originalElement);
            }

            return null;
        }

        protected TooltipInfo updateTooltip(TooltipInfo info, Element element) {
            if (isContextHelpElement(element)) {
                info.setTitle(null);
                info.setErrorMessage(null);
            } else {
                info.setContextHelp(null);
                checkRequiredIndicatorVisible();

                if (requiredIndicatorVisible && isRequiredIndicator(element)) {
                    info.setTitle(null);
                }
            }
            return info;
        }

        @Override
        public void onMouseDown(MouseDownEvent event) {
            Element element = event.getNativeEvent().getEventTarget().cast();
            if (isTooltipElement(element)) {
                closeNow();
                handleShowHide(event, false);
            } else {
                hideTooltip();
            }
        }

        @Override
        protected void handleShowHide(DomEvent domEvent, boolean isFocused) {
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
                boolean elementIsIndicator = elementIsIndicator(element);

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

            handledByFocus = isFocused;
            currentElement = element;
        }

        protected boolean elementIsIndicator(Element relativeElement) {
            return relativeElement != null
                    && isTooltipElement(relativeElement);
        }
    }
}
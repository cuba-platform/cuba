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

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.Util;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.HasContextHelpConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.orderedlayout.CaptionPosition;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;
import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.communication.SharedState;

import java.util.List;

public class CubaOrderedLayoutSlot extends Slot implements ClickHandler {

    public static final String CONTEXT_HELP_CLASSNAME = "c-context-help-button";
    public static final String CONTEXT_HELP_CLICKABLE_CLASSNAME = "c-context-help-button-clickable";

    protected Element contextHelpIcon;

    protected HandlerRegistration clickHandlerRegistration = null;

    public CubaOrderedLayoutSlot(VAbstractOrderedLayout layout, Widget widget) {
        super(layout, widget);
    }

    public void setCaption(String captionText, boolean contextHelpIconEnabled, Icon icon, List<String> styles,
                           String error, boolean showError, boolean required, boolean enabled, boolean captionAsHtml) {
        // CAUTION copied from super
        // Caption wrappers
        Widget widget = getWidget();
        final Element focusedElement = WidgetUtil.getFocusedElement();
        // By default focus will not be lost
        boolean focusLost = false;
        if (captionText != null || icon != null || error != null || required || contextHelpIconEnabled) {
            if (caption == null) {
                caption = DOM.createDiv();
                captionWrap = DOM.createDiv();
                captionWrap.addClassName(StyleConstants.UI_WIDGET);
                captionWrap.addClassName("v-has-caption");
                getElement().appendChild(captionWrap);
                orphan(widget);
                captionWrap.appendChild(widget.getElement());
                adopt(widget);

                // Made changes to DOM. Focus can be lost if it was in the
                // widget.
                focusLost = (focusedElement == null ? false : widget
                        .getElement().isOrHasChild(focusedElement));
            }
        } else if (caption != null) {
            orphan(widget);
            getElement().appendChild(widget.getElement());
            adopt(widget);
            captionWrap.removeFromParent();
            caption = null;
            captionWrap = null;

            // Made changes to DOM. Focus can be lost if it was in the widget.
            focusLost = (focusedElement == null ? false : widget.getElement()
                    .isOrHasChild(focusedElement));
        }

        // Caption text
        if (captionText != null) {
            if (this.captionText == null) {
                this.captionText = DOM.createSpan();
                this.captionText.addClassName("v-captiontext");

                if (caption != null) {
                    caption.appendChild(this.captionText);
                }
            }
            if (captionText.trim().equals("")) {
                this.captionText.setInnerHTML("&nbsp;");
            } else {
                if (captionAsHtml) {
                    this.captionText.setInnerHTML(captionText);
                } else {
                    this.captionText.setInnerText(captionText);
                }
            }
        } else if (this.captionText != null) {
            this.captionText.removeFromParent();
            this.captionText = null;
        }

        // Icon
        if (this.icon != null) {
            this.icon.getElement().removeFromParent();
        }
        if (icon != null) {
            if (caption != null) {
                caption.insertFirst(icon.getElement());
            }
        }
        this.icon = icon;

        // Required
        if (required) {
            if (requiredIcon == null) {
                requiredIcon = DOM.createSpan();
                // TODO decide something better (e.g. use CSS to insert the
                // character)
                requiredIcon.setInnerHTML("*");
                requiredIcon.setClassName("v-required-field-indicator");

                // The star should not be read by the screen reader, as it is
                // purely visual. Required state is set at the element level for
                // the screen reader.
                Roles.getTextboxRole().setAriaHiddenState(requiredIcon, true);
            }
            if (caption != null) {
                caption.appendChild(requiredIcon);
            }
        } else if (requiredIcon != null) {
            requiredIcon.removeFromParent();
            requiredIcon = null;
        }

        // Context Help
        // Haulmont API
        if (contextHelpIconEnabled) {
            if (contextHelpIcon == null) {
                contextHelpIcon = DOM.createSpan();
                // TODO decide something better (e.g. use CSS to insert the character)
                contextHelpIcon.setInnerHTML("?");
                contextHelpIcon.setClassName(CONTEXT_HELP_CLASSNAME);

                ComponentConnector componentConnector = Util.findConnectorFor(widget);
                if (hasContextHelpIconListeners(componentConnector.getState())) {
                    contextHelpIcon.addClassName(CONTEXT_HELP_CLICKABLE_CLASSNAME);
                }

                // The question mark should not be read by the screen reader, as it is
                // purely visual. Required state is set at the element level for
                // the screen reader.
                Roles.getTextboxRole().setAriaHiddenState(contextHelpIcon, true);
            }
            if (caption != null) {
                caption.appendChild(contextHelpIcon);

                if (clickHandlerRegistration == null) {
                    clickHandlerRegistration = addDomHandler(this, ClickEvent.getType());
                }
            }
        } else {
            if (this.contextHelpIcon != null) {
                this.contextHelpIcon.removeFromParent();
                this.contextHelpIcon = null;
            }

            if (clickHandlerRegistration != null) {
                clickHandlerRegistration.removeHandler();
                clickHandlerRegistration = null;
            }
        }

        // Error
        if (error != null && showError) {
            if (errorIcon == null) {
                errorIcon = DOM.createSpan();
                errorIcon.setClassName("v-errorindicator");
            }
            if (caption != null) {
                caption.appendChild(errorIcon);
            }
        } else if (errorIcon != null) {
            errorIcon.removeFromParent();
            errorIcon = null;
        }

        if (caption != null) {
            // Styles
            caption.setClassName("v-caption");

            if (styles != null) {
                for (String style : styles) {
                    caption.addClassName("v-caption-" + style);
                }
            }

            if (enabled) {
                caption.removeClassName("v-disabled");
            } else {
                caption.addClassName("v-disabled");
            }

            // Caption position
            if (captionText != null || icon != null) {
                setCaptionPosition(CaptionPosition.TOP);
            } else {
                setCaptionPosition(CaptionPosition.RIGHT);
            }
        }

        if (focusLost) {
            // Find out what element is currently focused.
            Element currentFocus = WidgetUtil.getFocusedElement();
            if (currentFocus != null
                    && currentFocus.equals(Document.get().getBody())) {
                // Focus has moved to BodyElement and should be moved back to
                // original location. This happened because of adding or
                // removing the captionWrap
                focusedElement.focus();
            } else if (currentFocus != focusedElement) {
                // Focus is either moved somewhere else on purpose or IE has
                // lost it. Investigate further.
                Timer focusTimer = new Timer() {

                    @Override
                    public void run() {
                        if (WidgetUtil.getFocusedElement() == null) {
                            // This should never become an infinite loop and
                            // even if it does it will be stopped once something
                            // is done with the browser.
                            schedule(25);
                        } else if (WidgetUtil.getFocusedElement().equals(
                                Document.get().getBody())) {
                            // Focus found it's way to BodyElement. Now it can
                            // be restored
                            focusedElement.focus();
                        }
                    }
                };
                if (BrowserInfo.get().isIE8()) {
                    // IE8 can't fix the focus immediately. It will fail.
                    focusTimer.schedule(25);
                } else {
                    // Newer IE versions can handle things immediately.
                    focusTimer.run();
                }
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        Element target = Element.as(event.getNativeEvent().getEventTarget());
        ComponentConnector componentConnector = Util.findConnectorFor(getWidget());

        if (target == contextHelpIcon
                && componentConnector instanceof HasContextHelpConnector) {
            HasContextHelpConnector connector = (HasContextHelpConnector) componentConnector;
            if (hasContextHelpIconListeners(componentConnector.getState())) {
                connector.contextHelpIconClick(event);
            }
        }
    }

    protected boolean hasContextHelpIconListeners(SharedState state) {
        return state.registeredEventListeners != null
                && state.registeredEventListeners.contains(AbstractFieldState.CONTEXT_HELP_ICON_CLICK_EVENT);
    }
}
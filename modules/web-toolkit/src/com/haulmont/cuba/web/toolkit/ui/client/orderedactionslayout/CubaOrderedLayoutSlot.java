/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.orderedlayout.CaptionPosition;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;

import java.util.List;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaOrderedLayoutSlot extends Slot {

    public static final String TOOLTIP_CLASSNAME = "cuba-tooltip-button";

    protected Element tooltipIcon;
    protected String descriptionText;

    public CubaOrderedLayoutSlot(VAbstractOrderedLayout layout, Widget widget) {
        super(layout, widget);
    }

    public void setCaption(String captionText, String descriptionText, Icon icon, List<String> styles,
                           String error, boolean showError, boolean required, boolean enabled, boolean captionAsHtml) {
        // CAUTION copied from super
        // Caption wrappers
        Widget widget = getWidget();
        final Element focusedElement = WidgetUtil.getFocusedElement();
        // By default focus will not be lost
        boolean focusLost = false;
        if (captionText != null || icon != null || error != null || required || descriptionText != null) {
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

        // Description
        // Haulmont API
        this.descriptionText = descriptionText;
        if (descriptionText != null) {
            if (tooltipIcon == null) {
                tooltipIcon = DOM.createSpan();
                // TODO decide something better (e.g. use CSS to insert the
                // character)
                tooltipIcon.setInnerHTML("?");
                tooltipIcon.setClassName(TOOLTIP_CLASSNAME);

                // The star should not be read by the screen reader, as it is
                // purely visual. Required state is set at the element level for
                // the screen reader.
                Roles.getTextboxRole().setAriaHiddenState(tooltipIcon, true);
            }
            if (caption != null) {
                caption.appendChild(tooltipIcon);
            }
        } else if (this.tooltipIcon != null) {
            this.tooltipIcon.removeFromParent();
            this.tooltipIcon = null;
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
}
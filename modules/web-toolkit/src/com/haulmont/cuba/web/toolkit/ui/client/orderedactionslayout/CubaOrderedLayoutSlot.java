/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.ImageIcon;
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

    public void setCaption(String captionText, String descriptionText, String iconUrl, List<String> styles, String error,
                           boolean showError, boolean required, boolean enabled) {
        // CAUTION copied from super
        // Caption wrappers
        Widget widget = getWidget();
        if (captionText != null || descriptionText != null || iconUrl != null || error != null || required) {
            if (caption == null) {
                caption = DOM.createDiv();
                captionWrap = DOM.createDiv();
                captionWrap.addClassName(StyleConstants.UI_WIDGET);
                captionWrap.addClassName("v-has-caption");
                getElement().appendChild(captionWrap);
                orphan(widget);
                captionWrap.appendChild(widget.getElement());
                adopt(widget);
            }
        } else if (caption != null) {
            orphan(widget);
            getElement().appendChild(widget.getElement());
            adopt(widget);
            captionWrap.removeFromParent();
            caption = null;
            captionWrap = null;
        }

        // Caption text
        if (captionText != null) {
            if (this.captionText == null) {
                this.captionText = DOM.createSpan();
                this.captionText.addClassName("v-captiontext");
                caption.appendChild(this.captionText);
            }
            if (captionText.trim().equals("")) {
                this.captionText.setInnerHTML("&nbsp;");
            } else {
                this.captionText.setInnerText(captionText);
            }
        } else if (this.captionText != null) {
            this.captionText.removeFromParent();
            this.captionText = null;
        }

        // Icon
        if (iconUrl != null) {
            if (icon == null) {
                icon = new ImageIcon();
                caption.insertFirst(icon.getElement());
            }
            icon.setUri(iconUrl);
        } else if (icon != null) {
            icon.getElement().removeFromParent();
            icon = null;
        }

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
            caption.appendChild(requiredIcon);
        } else if (requiredIcon != null) {
            requiredIcon.removeFromParent();
            requiredIcon = null;
        }

        // Desciption
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
            caption.appendChild(tooltipIcon);
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
            caption.appendChild(errorIcon);
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
            if (captionText != null || iconUrl != null) {
                setCaptionPosition(CaptionPosition.TOP);
            } else {
                setCaptionPosition(CaptionPosition.RIGHT);
            }
        }
    }
}
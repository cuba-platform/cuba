/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.caption;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.aria.AriaHelper;
import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.ComponentConstants;
import com.vaadin.shared.ui.ComponentStateUtil;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaCaptionWidget extends VCaption {

    public static final String CUBA_CLASSNAME = "cuba-caption";
    public static final String TOOLTIP_CLASSNAME = "cuba-tooltip-button";

    protected Element toolTipIndicator;

    protected boolean captionPlacedAfterComponentByDefault = true;

    protected CaptionHolder captionHolder = null;

    public CubaCaptionWidget(ComponentConnector component, ApplicationConnection client) {
        super(component, client);

        initWidget();
    }

    public void initWidget() {
        addStyleName(CUBA_CLASSNAME);
    }

    @Override
    public boolean updateCaption() {
        /* CAUTION copied from super class with small changes */
        boolean wasPlacedAfterComponent = placedAfterComponent;

        // Caption is placed after component unless there is some part which
        // moves it above.
        placedAfterComponent = captionPlacedAfterComponentByDefault;

        String style = CLASSNAME;
        if (ComponentStateUtil.hasStyles(owner.getState())) {
            for (String customStyle : owner.getState().styles) {
                style += " " + CLASSNAME + "-" + customStyle;
            }
        }
        if (!owner.isEnabled()) {
            style += " " + ApplicationConnection.DISABLED_CLASSNAME;
        }
        setStyleName(style);

        boolean hasIcon = owner.getState().resources
                .containsKey(ComponentConstants.ICON_RESOURCE);
        boolean showRequired = false;
        boolean showError = owner.getState().errorMessage != null;
        if (owner.getState() instanceof AbstractFieldState) {
            AbstractFieldState abstractFieldState = (AbstractFieldState) owner
                    .getState();
            showError = showError && !abstractFieldState.hideErrors;
        }
        if (owner instanceof AbstractFieldConnector) {
            showRequired = ((AbstractFieldConnector) owner).isRequired();
        }

        if (hasIcon) {
            if (icon == null) {
                icon = new Icon(client);
                icon.setWidth("0");
                icon.setHeight("0");

                DOM.insertChild(getElement(), icon.getElement(),
                        getInsertPosition(InsertPosition.ICON));
            }
            // Icon forces the caption to be above the component
            placedAfterComponent = false;

            icon.setUri(owner.getState().resources.get(
                    ComponentConstants.ICON_RESOURCE).getURL());

        } else if (icon != null) {
            // Remove existing
            DOM.removeChild(getElement(), icon.getElement());
            icon = null;
        }

        if (owner.getState().caption != null) {
            // A caption text should be shown if the attribute is set
            // If the caption is null the ATTRIBUTE_CAPTION should not be set to
            // avoid ending up here.

            if (captionText == null) {
                captionText = DOM.createDiv();
                captionText.setClassName("v-captiontext");

                DOM.insertChild(getElement(), captionText,
                        getInsertPosition(InsertPosition.CAPTION));
            }

            // Update caption text
            String c = owner.getState().caption;
            // A text forces the caption to be above the component.
            placedAfterComponent = false;
            if (c == null || c.trim().equals("")) {
                // Not sure if c even can be null. Should not.

                // This is required to ensure that the caption uses space in all
                // browsers when it is set to the empty string. If there is an
                // icon, error indicator or required indicator they will ensure
                // that space is reserved.
                if (!hasIcon && !showRequired && !showError) {
                    captionText.setInnerHTML("&nbsp;");
                }
            } else {
                DOM.setInnerText(captionText, c);
            }

        } else if (captionText != null) {
            // Remove existing
            DOM.removeChild(getElement(), captionText);
            captionText = null;
        }

        if (ComponentStateUtil.hasDescription(owner.getState())) {
            addStyleDependentName("hasdescription");
            enableFieldDescription();
        } else {
            removeStyleDependentName("hasdescription");
            disableFieldDescription();
        }

        AriaHelper.handleInputRequired(owner.getWidget(), showRequired);

        if (showRequired) {
            if (requiredFieldIndicator == null) {
                requiredFieldIndicator = DOM.createDiv();
                requiredFieldIndicator
                        .setClassName("v-required-field-indicator");
                DOM.setInnerText(requiredFieldIndicator, "*");

                DOM.insertChild(getElement(), requiredFieldIndicator,
                        getInsertPosition(InsertPosition.REQUIRED));

                // Hide the required indicator from assistive device
                Roles.getTextboxRole().setAriaHiddenState(
                        requiredFieldIndicator, true);
            }
        } else if (requiredFieldIndicator != null) {
            // Remove existing
            requiredFieldIndicator.removeFromParent();
            requiredFieldIndicator = null;
        }

        AriaHelper.handleInputInvalid(owner.getWidget(), showError);

        if (showError) {
            if (errorIndicatorElement == null) {
                errorIndicatorElement = DOM.createDiv();
                DOM.setInnerHTML(errorIndicatorElement, "&nbsp;");
                DOM.setElementProperty(errorIndicatorElement, "className",
                        "v-errorindicator");

                DOM.insertChild(getElement(), errorIndicatorElement,
                        getInsertPosition(InsertPosition.ERROR));

                // Hide error indicator from assistive devices
                Roles.getTextboxRole().setAriaHiddenState(
                        errorIndicatorElement, true);
            }
        } else if (errorIndicatorElement != null) {
            // Remove existing
            errorIndicatorElement.removeFromParent();
            errorIndicatorElement = null;
        }

        addStyleName(CLASSNAME);

        if (captionHolder != null) {
            captionHolder.captionUpdated(this);
        }
        return (wasPlacedAfterComponent != placedAfterComponent);
    }

    protected void enableFieldDescription() {
        if (toolTipIndicator == null) {
            toolTipIndicator = DOM.createDiv();
            toolTipIndicator.setClassName(TOOLTIP_CLASSNAME);

            DOM.insertChild(getElement(), toolTipIndicator, getDescriptionInsertPosition());
        }
    }

    protected void disableFieldDescription() {
        if (toolTipIndicator != null) {
            toolTipIndicator.removeFromParent();
            toolTipIndicator = null;
        }
    }

    @Override
    public int getRenderedWidth() {
        int width = 0;

        if (icon != null) {
            width += Util.getRequiredWidth(icon.getElement());
        }

        if (captionText != null) {
            width += Util.getRequiredWidth(captionText);
        }
        if (requiredFieldIndicator != null && requiredFieldIndicator.getParentElement() == getElement()) {
            width += Util.getRequiredWidth(requiredFieldIndicator);
        }
        if (errorIndicatorElement != null && errorIndicatorElement.getParentElement() == getElement()) {
            width += Util.getRequiredWidth(errorIndicatorElement);
        }
        if (toolTipIndicator != null && toolTipIndicator.getParentElement() == getElement()) {
            width += Util.getRequiredWidth(toolTipIndicator);
        }
        return width;
    }

    @Override
    public Element getTextElement() {
        return super.getTextElement();
    }

    public Element getTooltipElement() {
        return toolTipIndicator;
    }

    public Element getRequiredIndicatorElement() {
        return requiredFieldIndicator;
    }

    public Element getErrorIndicatorElement() {
        return errorIndicatorElement;
    }

    @Override
    protected int getInsertPosition(InsertPosition element) {
        int pos = super.getInsertPosition(element);

        if (toolTipIndicator != null) {
            pos++;
        }

        return pos;
    }

    protected int getDescriptionInsertPosition() {
        return super.getInsertPosition(null);
    }

    public void setCaptionHolder(CaptionHolder captionHolder) {
        this.captionHolder = captionHolder;
    }

    public boolean isCaptionPlacedAfterComponentByDefault() {
        return captionPlacedAfterComponentByDefault;
    }

    public void setCaptionPlacedAfterComponentByDefault(boolean captionPlacedAfterComponentByDefault) {
        this.captionPlacedAfterComponentByDefault = captionPlacedAfterComponentByDefault;
    }
}
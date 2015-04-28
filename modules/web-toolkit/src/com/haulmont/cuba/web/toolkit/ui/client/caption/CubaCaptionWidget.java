/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.caption;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.*;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.client.ui.ImageIcon;
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
        ComponentConnector owner = getOwner();

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
            style += " " + StyleConstants.DISABLED;
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

        if (icon != null) {
            getElement().removeChild(icon.getElement());
            icon = null;
        }
        if (hasIcon) {
            String uri = owner.getState().resources.get(
                    ComponentConstants.ICON_RESOURCE).getURL();

            icon = getOwner().getConnection().getIcon(uri);

            if (icon instanceof ImageIcon) {
                // onload will set appropriate size later
                icon.setWidth("0");
                icon.setHeight("0");
            }

            DOM.insertChild(getElement(), icon.getElement(),
                    getInsertPosition(InsertPosition.ICON));

            // Icon forces the caption to be above the component
            placedAfterComponent = false;
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
                setCaptionText(captionText, owner.getState());
            }

        } else if (captionText != null) {
            // Remove existing
            getElement().removeChild(captionText);
            captionText = null;
        }

        if (ComponentStateUtil.hasDescription(owner.getState())
                && captionText != null) {
            addStyleDependentName("hasdescription");
        } else {
            removeStyleDependentName("hasdescription");
        }

        AriaHelper.handleInputRequired(owner.getWidget(), showRequired);

        if (showRequired) {
            if (requiredFieldIndicator == null) {
                requiredFieldIndicator = DOM.createDiv();
                requiredFieldIndicator.setClassName("v-required-field-indicator");
                requiredFieldIndicator.setInnerText("*");

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

        // Description
        // Haulmont API
        if (ComponentStateUtil.hasDescription(owner.getState())) {
            addStyleDependentName("hasdescription");
            if (toolTipIndicator == null) {
                toolTipIndicator = DOM.createDiv();
                toolTipIndicator.setClassName(TOOLTIP_CLASSNAME);

                DOM.insertChild(getElement(), toolTipIndicator, getDescriptionInsertPosition());
            }
        } else {
            removeStyleDependentName("hasdescription");
            if (toolTipIndicator != null) {
                toolTipIndicator.removeFromParent();
                toolTipIndicator = null;
            }
        }

        AriaHelper.handleInputInvalid(owner.getWidget(), showError);

        if (showError) {
            if (errorIndicatorElement == null) {
                errorIndicatorElement = DOM.createDiv();

                errorIndicatorElement.setInnerHTML("&nbsp;");
                errorIndicatorElement.setClassName("v-errorindicator");

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

    @Override
    public int getRenderedWidth() {
        int width = 0;

        if (icon != null) {
            width += WidgetUtil.getRequiredWidth(icon.getElement());
        }

        if (captionText != null) {
            width += WidgetUtil.getRequiredWidth(captionText);
        }
        if (requiredFieldIndicator != null && requiredFieldIndicator.getParentElement() == getElement()) {
            width += WidgetUtil.getRequiredWidth(requiredFieldIndicator);
        }
        if (errorIndicatorElement != null && errorIndicatorElement.getParentElement() == getElement()) {
            width += WidgetUtil.getRequiredWidth(errorIndicatorElement);
        }
        if (toolTipIndicator != null && toolTipIndicator.getParentElement() == getElement()) {
            width += WidgetUtil.getRequiredWidth(toolTipIndicator);
        }
        return width;
    }

    @Override
    @SuppressWarnings("deprecation")
    public com.google.gwt.user.client.Element getTextElement() {
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

    public void setCaptionPlacedAfterComponentByDefault(boolean captionPlacedAfterComponentByDefault) {
        this.captionPlacedAfterComponentByDefault = captionPlacedAfterComponentByDefault;
    }
}
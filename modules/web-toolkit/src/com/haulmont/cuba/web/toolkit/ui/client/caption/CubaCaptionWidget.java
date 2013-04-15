/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.caption;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaCaptionWidget extends VCaption {
    public static final String CLASSNAME = "cuba-caption";

    protected Element toolTipIndicator;

    public CubaCaptionWidget(ComponentConnector component, ApplicationConnection client) {
        super(component, client);

        initWidget();
    }

    public void initWidget() {
        addStyleName(CLASSNAME);
    }

    @Override
    public boolean updateCaption() {
        boolean updateResult = super.updateCaption();

        addStyleName(CLASSNAME);

        return updateResult;
    }

    @Override
    protected void enableFieldDescription() {
        if (toolTipIndicator == null) {
            toolTipIndicator = DOM.createDiv();
            toolTipIndicator.setClassName("cuba-tooltip-button");

            DOM.insertChild(getElement(), toolTipIndicator, getDescriptionInsertPosition());
            DOM.sinkEvents(toolTipIndicator, Event.ONCLICK);
            DOM.setEventListener(toolTipIndicator, new EventListener() {
                @Override
                public void onBrowserEvent(Event event) {
                    client.getVTooltip().getTooltipEventHandler().showTooltipForElement(toolTipIndicator, event);
                }
            });
        }
    }

    @Override
    protected void disableFieldDescription() {
        if (toolTipIndicator != null) {
            toolTipIndicator.removeFromParent();
            toolTipIndicator = null;
        }
    }

    @Override
    protected void enableFieldRequired() {
        DOM.sinkEvents(requiredFieldIndicator, Event.ONCLICK);
        DOM.setEventListener(requiredFieldIndicator, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                client.getVTooltip().getTooltipEventHandler().showTooltipForElement(requiredFieldIndicator, event);
            }
        });
    }

    @Override
    protected void disableFieldRequired() {
        super.disableFieldRequired();
    }
    @Override
    public int getRenderedWidth() {
        int renderedWidth = super.getRenderedWidth();
        if (toolTipIndicator != null) {
            renderedWidth += Util.getRequiredWidth(toolTipIndicator);
        }
        return renderedWidth;
    }

    @Override
    public Element getTextElement() {
        return super.getTextElement();
    }

    public Element getErrorIndicatorElement() {
        return errorIndicatorElement;
    }

    public Element getTooltipElement() {
        return toolTipIndicator;
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
}
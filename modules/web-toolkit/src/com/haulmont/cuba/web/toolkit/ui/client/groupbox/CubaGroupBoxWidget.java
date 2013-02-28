/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VPanel;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaGroupBoxWidget extends VPanel {

    public static final String CLASSNAME = "cuba-groupbox";

    protected Element descriptionNode = DOM.createDiv();

    protected Element fieldset = DOM.createFieldSet();
    protected Element legend = DOM.createLegend();

    protected Element expander = DOM.createSpan();

    protected boolean expanded = true;

    protected boolean collapsable = false;

    protected ExpandHandler expandHandler;

    public CubaGroupBoxWidget() {
        setStyleName(CLASSNAME);

        // remove parent DOM structure
        getElement().removeChild(captionNode.getParentElement());
        getElement().removeChild(bottomDecoration);
        getElement().removeChild(contentNode);

        captionNode.getParentElement().removeChild(captionNode);

        captionNode.setClassName(CLASSNAME + "-caption");
        descriptionNode.setClassName(CLASSNAME + "-description");
        contentNode.setClassName(CLASSNAME + "-content");
        bottomDecoration.setClassName(CLASSNAME + "-deco");
        expander.setClassName(CLASSNAME + "-expander");

        setExpanded(true);

        captionNode.insertFirst(expander);

        legend.appendChild(captionNode);

        fieldset.appendChild(legend);
        fieldset.appendChild(descriptionNode);
        fieldset.appendChild(contentNode);
        fieldset.appendChild(bottomDecoration);
        getElement().appendChild(fieldset);

        DOM.sinkEvents(expander, Event.ONCLICK);

        addHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
            }
        }, ResizeEvent.getType());
    }

    public void setDescription(String text) {
        DOM.setInnerText(descriptionNode, text);
    }

    @Override
    public void setCaption(String text) {
        if (text == null || text.equals(""))
            addStyleDependentName("nocaption");
        else
            removeStyleDependentName("nocaption");

        super.setCaption(text);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        if (expanded)
            expander.addClassName("expanded");
        else
            expander.removeClassName("expanded");

        this.expanded = expanded;
    }

    public boolean isCollapsable() {
        return collapsable;
    }

    public void setCollapsable(boolean collapsable) {
        if (collapsable) {
            DOM.setStyleAttribute(expander, "display", "");
            removeStyleDependentName("nocollapsable");
        } else {
            addStyleDependentName("nocollapsable");
            DOM.setStyleAttribute(expander, "display", "none");
        }
        this.collapsable = collapsable;
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK && DOM.eventGetTarget(event) == expander) {
            setExpanded(!expanded);

            if (collapsable && expandHandler != null) {
                if (expanded)
                    expandHandler.expand();
                else
                    expandHandler.collapse();
            }
            DOM.eventCancelBubble(event, true);
        } else {
            super.onBrowserEvent(event);
        }
    }

    public interface ExpandHandler {
        void expand();

        void collapse();
    }
}
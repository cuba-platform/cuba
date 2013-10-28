/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VPanel;

/**
 * @author gorodnov
 * @version $Id$
 */
public class VGroupBox extends VPanel {

    public static final String CLASSNAME = "group-box";

    protected int contentNodeBorderPaddingsHor = -1;
    protected int contentNodeBorderPaddingsVer = -1;

    protected Element expander;

    protected boolean expanded;
    protected boolean collapsable;

    protected Element descriptionNode;

    protected Element fieldset;
    protected Element legend;

    protected boolean captionVisible = false;

    protected String mainStyleName = CLASSNAME;

    @SuppressWarnings("unused")
    public VGroupBox() {
    }

    @Override
    protected void constructDOM() {
        VConsole.log(">> Style: " + mainStyleName);

        fieldset = DOM.createFieldSet();
        legend = DOM.createLegend();
        expander = DOM.createDiv();
        descriptionNode = DOM.createDiv();

        captionNode.setClassName(mainStyleName + "-caption");
        descriptionNode.setClassName(mainStyleName + "-description");
        contentNode.setClassName(mainStyleName + "-content");
        bottomDecoration.setClassName(mainStyleName + "-deco");

        captionNode.appendChild(expander);
        captionNode.appendChild(captionText);

        legend.appendChild(captionNode);

        fieldset.appendChild(legend);
        fieldset.appendChild(descriptionNode);
        fieldset.appendChild(contentNode);
        fieldset.appendChild(bottomDecoration);
        getElement().appendChild(fieldset);

        setStyleName(mainStyleName);

        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
        DOM.sinkEvents(contentNode, Event.ONSCROLL);

        DOM.sinkEvents(expander, Event.ONCLICK);

        contentNode.getStyle().setProperty("position", "relative");
        getElement().getStyle().setProperty("overflow", "hidden");
    }

    @Override
    protected void renderContent(UIDL uidl) {
        //do nothing
    }

    @Override
    protected void renderDOM(UIDL uidl) {
        if (uidl.hasAttribute("caption")
                && !uidl.getStringAttribute("caption").equals("")) {
            setCaption(uidl.getStringAttribute("caption"));
        } else {
            setCaption("");
        }
        if (uidl.hasAttribute("description")
                && !uidl.getStringAttribute("description").equals("")) {
            setDescription(uidl.getStringAttribute("description"));
        } else {
            setDescription("");
        }
    }

    private void setDescription(String text) {
        DOM.setInnerText(descriptionNode, text);
    }

    @Override
    public void updateFromUIDL(UIDL uidl) {
        super.updateFromUIDL(uidl);

        if (!uidl.hasAttribute("caption") || uidl.getStringAttribute("caption").equals("")) {
            addStyleDependentName("nocaption");
            captionVisible = false;
        } else {
            removeStyleDependentName("nocaption");
            captionVisible = true;
        }

        if (!uidl.hasAttribute("description") || uidl.getStringAttribute("description").equals("")) {
            addStyleDependentName("nodescription");
        } else {
            removeStyleDependentName("nodescription");
        }

        collapsable = uidl.getBooleanAttribute("collapsable");
        if (collapsable) {
            DOM.setStyleAttribute(expander, "display", "");
            removeStyleDependentName("nocollapsable");
        } else {
            addStyleDependentName("nocollapsable");
            DOM.setStyleAttribute(expander, "display", "none");
        }
        if (uidl.getBooleanAttribute("expanded") != expanded) {
            toggleExpand();
        }

        //render content
        final UIDL layoutUidl = uidl.getChildUIDL(0);
        if (layoutUidl != null) {
            final Paintable newLayout = client.getPaintable(layoutUidl);
            if (newLayout != layout) {
                if (layout != null) {
                    client.unregisterPaintable(layout);
                }
                setWidget((Widget) newLayout);
                layout = newLayout;
            }
            layout.updateFromUIDL(layoutUidl, client);
        }

        detectContainerBorders();
    }

    protected void toggleExpand() {
        expanded = !expanded;
        if (expanded) {
            captionNode.addClassName("expanded");
        } else {
            captionNode.removeClassName("expanded");
        }
    }

    @Override
    protected int getCaptionPaddingHorizontal() {
        return 0;
    }

    @Override
    protected void detectContainerBorders() {
        if (isAttached()) {
            String oldWidth = DOM.getStyleAttribute(contentNode, "width");
            String oldHeight = DOM.getStyleAttribute(contentNode, "height");

            DOM.setStyleAttribute(contentNode, "overflow", "hidden");

            DOM.setStyleAttribute(contentNode, "width", "0px");
            DOM.setStyleAttribute(contentNode, "height", "0px");

            borderPaddingHorizontal = contentNodeBorderPaddingsHor = contentNode.getOffsetWidth();
            borderPaddingVertical = contentNodeBorderPaddingsVer = contentNode.getOffsetHeight();

            DOM.setStyleAttribute(contentNode, "width", "");
            DOM.setStyleAttribute(contentNode, "height", "");

            borderPaddingHorizontal += (fieldset.getOffsetWidth() - contentNode.getOffsetWidth());
            borderPaddingVertical += (fieldset.getOffsetHeight() - contentNode.getOffsetHeight());

            borderPaddingVertical = borderPaddingVertical - legend.getOffsetHeight();

            DOM.setStyleAttribute(contentNode, "width", oldWidth);
            DOM.setStyleAttribute(contentNode, "height", oldHeight);
            DOM.setStyleAttribute(contentNode, "overflow", "auto");
        }
    }

    protected int getContentNodeBorderPaddingsWidth() {
        if (isAttached()) {
            if (contentNodeBorderPaddingsHor < 0) {
                detectContainerBorders();
            }
            return contentNodeBorderPaddingsHor;
        }
        return 0;
    }

    protected int getContentNodeBorderPaddingsHeight() {
        if (isAttached()) {
            if (contentNodeBorderPaddingsVer < 0) {
                detectContainerBorders();
            }
            return contentNodeBorderPaddingsVer;
        }
        return 0;
    }

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {
        int w = 0;
        int h = 0;

        if (width != null && !width.equals("")) {
            w = contentNode.getOffsetWidth() - getContentNodeBorderPaddingsWidth();
            if (w < 0) {
                w = 0;
            }
        }

        if (height != null && !height.equals("")) {
            h = contentNode.getOffsetHeight() - getContentNodeBorderPaddingsHeight();
            if (h < 0) {
                h = 0;
            }
        }

        return new RenderSpace(w, h, true);
    }

    @Override
    public void runHacks(boolean runGeckoFix) {
        runWebkitOverflowAutoFix();
    }

    public void runWebkitOverflowAutoFix() {
        client.runDescendentsLayout(this);

        if (BrowserInfo.get().isWebkit())
            Util.runWebkitOverflowAutoFix(contentNode);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK && DOM.eventGetTarget(event) == expander) {
            toggleExpand();
            if (collapsable) {
                if (expanded) {
                    client.updateVariable(id, "expand", true, true);
                } else {
                    client.updateVariable(id, "collapse", true, true);
                }
            }
            DOM.eventCancelBubble(event, true);
        } else {
            super.onBrowserEvent(event);
        }
    }
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.VTextArea;

/**
 * @author subbotin
 * @version $Id$
 */
public class VResizableTextArea extends VTextArea {

    public static final String DIV_CLASS_NAME = "v-resizable-textarea-div";

    private boolean dragDrop = false;
    private boolean composed = false;
    private boolean resizable = false;

    private Element resizeElement = DOM.createDiv();

    private static final int MOUSE_EVENTS = Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP | Event.ONMOUSEOVER;
    private static final int RESIZE_REGION = 16;
    private static final int MINIMAL_WIDTH = 17;
    private static final int MINIMAL_HEIGHT = 17;

    private class ResizeEventListener implements EventListener {
        @Override
        public void onBrowserEvent(Event event) {
            switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEOVER:
                    handleMouseStyle(event);
                    break;
                case Event.ONMOUSEDOWN:
                    captureEvents(event);
                    break;
                case Event.ONMOUSEUP:
                    releaseCapture(event);
                    break;
                case Event.ONMOUSEMOVE:
                    handleResize(event);
                    break;
            }
        }
    }

    public VResizableTextArea() {
        DOM.setStyleAttribute(getElement(), "resize", "none");
        resizeElement.setClassName(DIV_CLASS_NAME);
    }

    private void handleMouseStyle(Event event) {
        if (isResizeRegion(event)) {
            DOM.setStyleAttribute(resizeElement, "cursor", "se-resize");
        } else {
            DOM.setStyleAttribute(resizeElement, "cursor", "default");
        }
    }

    private void captureEvents(Event event) {
        event.preventDefault();
        if (isResizeRegion(event) && DOM.eventGetButton(event) == Event.BUTTON_LEFT) {
            if (!dragDrop) {
                dragDrop = true;
                DOM.setCapture(resizeElement);
            }
        }
    }

    private void releaseCapture(Event event) {
        if (DOM.eventGetButton(event) == Event.BUTTON_LEFT && dragDrop) {
            dragDrop = false;
            DOM.releaseCapture(resizeElement);
            client.updateVariable(id, "width", getOffsetWidth() + "px", false);
            client.updateVariable(id, "height", getOffsetHeight() + "px", false);
            client.updateVariable(id, "text", getText(), false);
            client.sendPendingVariableChanges();
        }
    }

    private void handleResize(Event event) {
        if (!isResizeRegion(event)) {
            DOM.setStyleAttribute(resizeElement, "cursor", "default");
        }

        //calculate and set the new size
        if (dragDrop) {
            int mouseX = DOM.eventGetClientX(event);
            int mouseY = DOM.eventGetClientY(event);
            int absoluteLeft = getAbsoluteLeft();
            int absoluteTop = getAbsoluteTop();

            //do not allow mirror-functionality
            if (mouseY > absoluteTop + MINIMAL_HEIGHT && mouseX > absoluteLeft + MINIMAL_WIDTH) {
                int width = mouseX - absoluteLeft + 2;
                int height = mouseY - absoluteTop + 2;

                setHeight(height + "px");
                setWidth(width + "px");

                Util.notifyParentOfSizeChange(this, false);
            }
        }
    }

    private boolean isResizeRegion(Event event) {
        int mouseX = DOM.eventGetClientX(event);
        int mouseY = DOM.eventGetClientY(event);

        int regionEndY = DOM.getAbsoluteTop(resizeElement) + resizeElement.getOffsetHeight();
        int regionEndX = DOM.getAbsoluteLeft(resizeElement) + resizeElement.getOffsetWidth();

        return regionEndX - mouseX < RESIZE_REGION && regionEndY - mouseY < RESIZE_REGION;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        VConsole.log(">> TEXT AREA");

        if (uidl.hasAttribute("resizable")) {
            resizable = uidl.getBooleanAttribute("resizable");
        }

        if (uidl.hasAttribute("rows")) {
            VConsole.log(">> ROWS " + uidl.getIntAttribute("rows"));

            setRows(uidl.getIntAttribute("rows"));
        }

        if (!composed) {
            if (resizable) {
                Element parentDiv = DOM.createDiv();
                DOM.setStyleAttribute(parentDiv, "position", "relative");
                DOM.setStyleAttribute(parentDiv, "overflow", "hidden");
                DOM.setStyleAttribute(parentDiv, "display", "inline");

                getElement().getParentElement().appendChild(parentDiv);
                getElement().getParentElement().removeChild(getElement());
                parentDiv.appendChild(getElement());
                parentDiv.appendChild(resizeElement);

                DOM.sinkEvents(resizeElement, MOUSE_EVENTS);
                DOM.setEventListener(resizeElement, new ResizeEventListener());
            }
            composed = true;
        }
        super.updateFromUIDL(uidl, client);
    }
}

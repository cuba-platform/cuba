/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import  com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorMap;
import com.vaadin.client.LayoutManager;
import com.vaadin.client.ui.VTextArea;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaResizableTextAreaWidget extends VTextArea {

    public static final String TEXT_AREA_WRAPPER = "text-area-wrapper";
    public static final String RESIZE_ELEMENT = "cuba-resizabletextarea-resize-corner";

    protected boolean dragDrop = false;
    protected boolean composed = false;
    protected boolean resizable = false;

    protected Element resizeElement = DOM.createDiv();

    protected static final int MOUSE_EVENTS = Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP | Event.ONMOUSEOVER;
    protected static final int RESIZE_REGION = 16;
    protected static final int MINIMAL_WIDTH = 17;
    protected static final int MINIMAL_HEIGHT = 17;

    public CubaResizableTextAreaWidget() {
        getElement().getStyle().setProperty("resize", "none");
        sinkEvents(Event.ONKEYDOWN);
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        if (!composed) {
            if (resizable) {
                Element parentDiv = DOM.createDiv();
                Style style = parentDiv.getStyle();

                style.setProperty("position", "relative");
                style.setProperty("overflow", "hidden");
                style.setProperty("display", "inline");
                parentDiv.setClassName(TEXT_AREA_WRAPPER);

                getElement().getParentElement().replaceChild(parentDiv,getElement());
                parentDiv.appendChild(getElement());
                parentDiv.appendChild(resizeElement);

                DOM.sinkEvents(resizeElement, MOUSE_EVENTS);
                DOM.setEventListener(resizeElement, new ResizeEventListener());
            }
            composed = true;
        }
    }

    protected class ResizeEventListener implements EventListener {
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

    protected void handleMouseStyle(Event event) {
        Style resizeElementStyle = resizeElement.getStyle();
        if (isResizeRegion(event)) {
            resizeElementStyle.setCursor(Style.Cursor.SE_RESIZE);
        } else {
            resizeElementStyle.setCursor(Style.Cursor.DEFAULT);
        }
    }

    protected void captureEvents(Event event) {
        event.preventDefault();
        if (isResizeRegion(event) && event.getButton() == Event.BUTTON_LEFT) {
            if (!dragDrop) {
                dragDrop = true;
                DOM.setCapture(resizeElement);
            }
        }
    }

    protected void releaseCapture(Event event) {
        if (event.getButton() == Event.BUTTON_LEFT && dragDrop) {
            dragDrop = false;
            DOM.releaseCapture(resizeElement);
            ComponentConnector connector = ConnectorMap.get(client).getConnector(this);

            client.updateVariable(connector.getConnectorId(), "width", getOffsetWidth() + "px", false);
            client.updateVariable(connector.getConnectorId(), "height", getOffsetHeight() + "px", false);
            client.updateVariable(connector.getConnectorId(), "text", getText(), false);
            client.sendPendingVariableChanges();
        }
    }

    protected void handleResize(Event event) {
        if (!isResizeRegion(event)) {
            resizeElement.getStyle().setCursor(Style.Cursor.DEFAULT);
        }

        //calculate and set the new size
        if (dragDrop) {
            int mouseX = event.getClientX();
            int mouseY = event.getClientY();
            int absoluteLeft = getAbsoluteLeft();
            int absoluteTop = getAbsoluteTop();

            //do not allow mirror-functionality
            if (mouseY > absoluteTop + MINIMAL_HEIGHT && mouseX > absoluteLeft + MINIMAL_WIDTH) {
                int width = mouseX - absoluteLeft + 2;
                int height = mouseY - absoluteTop + 2;

                setHeight(height + "px");
                setWidth(width + "px");

                LayoutManager layoutManager = LayoutManager.get(client);
                ComponentConnector connector = ConnectorMap.get(client).getConnector(this);
                layoutManager.setNeedsMeasure(connector);
            }
        }
    }

    protected boolean isResizeRegion(Event event) {
        int mouseX = event.getClientX();
        int mouseY = event.getClientY();

        int regionEndY = resizeElement.getAbsoluteTop() + resizeElement.getOffsetHeight();
        int regionEndX = resizeElement.getAbsoluteLeft() + resizeElement.getOffsetWidth();

        return regionEndX - mouseX < RESIZE_REGION && regionEndY - mouseY < RESIZE_REGION;
    }

    @Override
    public void onBrowserEvent(Event event) {
        int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN
                && event.getKeyCode() == KeyCodes.KEY_ENTER
                && !event.getAltKey()
                && !event.getShiftKey()
                && !event.getCtrlKey()) {
            event.stopPropagation();
            return;
        }
        super.onBrowserEvent(event);
    }
}
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

package com.haulmont.cuba.web.toolkit.ui.client.resizabletextarea;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.vaadin.client.ComputedStyle;
import com.vaadin.client.ui.VCustomComponent;

public class CubaResizableTextAreaWrapperWidget extends VCustomComponent {

    public static final String RESIZE_ELEMENT = "cuba-resizabletextarea-resize-corner";

    protected boolean dragDrop = false;
    protected boolean enabled = true;

    protected Element resizeElement;

    protected ResizeHandler resizeHandler;

    protected static final int MOUSE_EVENTS = Event.ONMOUSEDOWN | Event.ONMOUSEMOVE | Event.ONMOUSEUP | Event.ONMOUSEOVER;
    protected static final int MINIMAL_WIDTH = 17;
    protected static final int MINIMAL_HEIGHT = 17;

    public boolean isResizable() {
        return resizeElement != null;
    }

    public void setResizable(boolean resizable) {
        if (isResizable() == resizable) {
            return;
        }

        if (resizable) {
            resizeElement = DOM.createDiv();
            resizeElement.setClassName(RESIZE_ELEMENT);

            getElement().appendChild(resizeElement);

            DOM.sinkEvents(resizeElement, MOUSE_EVENTS);
            DOM.setEventListener(resizeElement, new ResizeEventListener());
        } else {
            if (resizeElement != null) {
                DOM.sinkEvents(resizeElement, 0);
                DOM.setEventListener(resizeElement, null);

                resizeElement.removeFromParent();

                resizeElement = null;
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected class ResizeEventListener implements EventListener {
        @Override
        public void onBrowserEvent(Event event) {
            switch (DOM.eventGetType(event)) {
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

    protected void captureEvents(Event event) {
        event.preventDefault();
        if (isEnabled() && event.getButton() == Event.BUTTON_LEFT) {
            if (!dragDrop) {
                dragDrop = true;
                DOM.setCapture(resizeElement);

                ComputedStyle cs = new ComputedStyle(getElement());
                setWidth(cs.getProperty("width"));
                setHeight(cs.getProperty("height"));

                getTextArea().getStyle().setProperty("width", "100%");
                getTextArea().getStyle().setProperty("height", "100%");
            }
        }
    }

    protected void releaseCapture(Event event) {
        if (event.getButton() == Event.BUTTON_LEFT && dragDrop) {
            dragDrop = false;
            DOM.releaseCapture(resizeElement);

            if (resizeHandler != null) {
                ComputedStyle cs = new ComputedStyle(getElement());
                resizeHandler.sizeChanged(cs.getProperty("width"), cs.getProperty("height"));
                resizeHandler.textChanged(getText());
            }
        }
    }

    protected void handleResize(Event event) {
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

                if (resizeHandler != null) {
                    resizeHandler.handleResize();
                }
            }
        }
    }

    protected Element getTextArea() {
        return getElement().getFirstChildElement();
    }

    public String getText() {
        return getTextArea().getPropertyString("value");
    }

    public interface ResizeHandler {

        void handleResize();

        void sizeChanged(String width, String height);

        void textChanged(String text);
    }
}
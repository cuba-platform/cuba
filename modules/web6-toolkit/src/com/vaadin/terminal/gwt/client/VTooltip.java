/*
 * Copyright 2010 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.terminal.gwt.client;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.haulmont.cuba.toolkit.gwt.client.ui.Table;
import com.vaadin.terminal.gwt.client.ui.VButton;
import com.vaadin.terminal.gwt.client.ui.VCheckBox;
import com.vaadin.terminal.gwt.client.ui.VOverlay;

public class VTooltip extends VOverlay {
    private static final String CLASSNAME = "v-tooltip";
    private static final int MARGIN = 4;
    public static final int TOOLTIP_EVENTS = Event.ONCLICK | Event.ONKEYDOWN
            | Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONMOUSEMOVE
            | Event.ONCLICK;
    protected static final int MAX_WIDTH = 500;
    private static final int QUICK_OPEN_TIMEOUT = 1000;
    private static final int CLOSE_TIMEOUT = 300;
    private static final int OPEN_DELAY = 750;
    private static final int QUICK_OPEN_DELAY = 100;
    VErrorMessage em = new VErrorMessage();
    Element description = DOM.createDiv();

    private HandlerRegistration nativePreviewHandlerRegistration;

    private Paintable tooltipOwner;

    private boolean closing = false;
    private boolean opening = false;
    private ApplicationConnection ac;
    // Open next tooltip faster. Disabled after 2 sec of showTooltip-silence.
    private boolean justClosed = false;
    // If this is "additional" tooltip, this field contains the key for it
    private Object tooltipKey;

    public VTooltip(ApplicationConnection client) {
        super(false, false, true);
        ac = client;
        setStyleName(CLASSNAME);
        FlowPanel layout = new FlowPanel();
        setWidget(layout);
        layout.add(em);
        DOM.setElementProperty(description, "className", CLASSNAME + "-text");
        DOM.appendChild(layout.getElement(), description);
    }

    /**
     * Show a popup containing the information in the "info" tooltip
     *
     * @param info
     */
    private void show(TooltipInfo info) {
        boolean hasContent = false;
        if (info.getErrorUidl() != null) {
            em.setVisible(true);
            em.updateFromUIDL(info.getErrorUidl());
            hasContent = true;
        } else {
            em.setVisible(false);
        }
        if (info.getTitle() != null && !"".equals(info.getTitle())) {
            DOM.setInnerHTML(description, info.getTitle());
            DOM.setStyleAttribute(description, "display", "");
            hasContent = true;
        } else {
            DOM.setInnerHTML(description, "");
            DOM.setStyleAttribute(description, "display", "none");
        }
        if (hasContent) {
            setPopupPositionAndShow(new PositionCallback() {
                public void setPosition(int offsetWidth, int offsetHeight) {

                    if (offsetWidth > MAX_WIDTH) {
                        setWidth(MAX_WIDTH + "px");
                    }

                    offsetWidth = getOffsetWidth();

                    int x = tooltipEventMouseX + 10 + Window.getScrollLeft();
                    int y = tooltipEventMouseY + 10 + Window.getScrollTop();

                    if (x + offsetWidth + MARGIN - Window.getScrollLeft() > Window
                            .getClientWidth()) {
                        x = Window.getClientWidth() - offsetWidth - MARGIN;
                    }

                    if (y + offsetHeight + MARGIN - Window.getScrollTop() > Window
                            .getClientHeight()) {
                        y = tooltipEventMouseY - 5 - offsetHeight;
                        if (y - Window.getScrollTop() < 0) {
                            // tooltip does not fit on top of the mouse either,
                            // put it at the top of the screen
                            y = Window.getScrollTop();
                        }
                    }

                    setPopupPosition(x, y);
                    sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
                }
            });
            if (!(tooltipOwner instanceof VButton)&&!(tooltipOwner instanceof VCheckBox)&&!(tooltipOwner instanceof Table)) {
                nativePreviewHandlerRegistration = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
                    @Override
                    public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                        Event nativeEvent = Event.as(event.getNativeEvent());
                        boolean eventTargetsPopup = eventTargetsPopup(nativeEvent);


                        if (eventTargetsPopup) {
                            event.consume();
                        } else if ((event.getTypeInt() & Event.ONCLICK) == Event.ONCLICK) {
                            hideTooltip();
                        }
                    }
                });
            }
        } else {
            hide();
        }
    }

    private boolean eventTargetsPopup(NativeEvent event) {
        EventTarget target = event.getEventTarget();
        if (com.google.gwt.dom.client.Element.is(target)) {
            return getElement().isOrHasChild(com.google.gwt.dom.client.Element.as(target));
        }
        return false;
    }

    public void showTooltip(Paintable owner, Event event, Object key) {
        if (!(owner instanceof VButton)&&!(owner instanceof VCheckBox)&&!(owner instanceof Table)) {
            updatePosition(event);
            tooltipOwner = owner;
            TooltipInfo info = ac.getTooltipTitleInfo(owner, tooltipKey);
            if (null != info) {
                show(info);
            }
            return;
        }
        if (closing && tooltipOwner == owner && tooltipKey == key) {
            // return to same tooltip, cancel closing
            closeTimer.cancel();
            closing = false;
            justClosedTimer.cancel();
            justClosed = false;
            return;
        }

        if (closing) {
            closeNow();
        }

        updatePosition(event);

        if (opening) {
            showTimer.cancel();
        }
        tooltipOwner = owner;
        tooltipKey = key;

        // Schedule timer for showing the tooltip according to if it was
        // recently closed or not.
        if (justClosed) {
            showTimer.schedule(QUICK_OPEN_DELAY);
        } else {
            showTimer.schedule(OPEN_DELAY);
        }
        opening = true;
    }

    private void closeNow() {
        if (closing) {
            hide();
            tooltipOwner = null;
            setWidth("");
            closing = false;
        }
    }

    private Timer showTimer = new Timer() {
        @Override
        public void run() {
            TooltipInfo info = ac.getTooltipTitleInfo(tooltipOwner, tooltipKey);
            if (null != info) {
                show(info);
            }
            opening = false;
        }
    };

    private Timer closeTimer = new Timer() {
        @Override
        public void run() {
            closeNow();
            justClosedTimer.schedule(2000);
            justClosed = true;
        }
    };

    private Timer justClosedTimer = new Timer() {
        @Override
        public void run() {
            justClosed = false;
        }
    };

    public void hideTooltip() {
        if (!(tooltipOwner instanceof VButton)&&!(tooltipOwner instanceof VCheckBox)&&!(tooltipOwner instanceof Table)) {
            if (nativePreviewHandlerRegistration != null) {
                nativePreviewHandlerRegistration.removeHandler();
            }
            tooltipOwner = null;
            hide();
            return;
        }
        if (opening) {
            showTimer.cancel();
            opening = false;
            tooltipOwner = null;
        }
        if (!isAttached()) {
            return;
        }
        if (closing) {
            // already about to close
            return;
        }
        closeTimer.schedule(CLOSE_TIMEOUT);
        closing = true;
        justClosed = true;
        justClosedTimer.schedule(QUICK_OPEN_TIMEOUT);

    }

    private int tooltipEventMouseX;
    private int tooltipEventMouseY;

    public void updatePosition(Event event) {
        tooltipEventMouseX = DOM.eventGetClientX(event);
        tooltipEventMouseY = DOM.eventGetClientY(event);

    }

    public void handleTooltipEvent(Event event, Paintable owner, Object key) {
        final int type = DOM.eventGetType(event);
        if ((VTooltip.TOOLTIP_EVENTS & type) == type && owner != null) {
            if ((owner instanceof VButton) || (owner instanceof VCheckBox) || (owner instanceof Table)) {
                if (type == Event.ONMOUSEOVER) {
                    showTooltip(owner, event, key);
                } else if (type == Event.ONMOUSEMOVE) {
                    updatePosition(event);
                } else {
                    hideTooltip();
                }
            } else {
                if (type == Event.ONCLICK) {
                    showTooltip(owner, event, key);
                }
            }
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (!(tooltipOwner instanceof VButton)&&!(tooltipOwner instanceof VCheckBox) &&!(tooltipOwner instanceof Table)){
            return;
        }
        final int type = DOM.eventGetType(event);
        // cancel closing event if tooltip is mouseovered; the user might want
        // to scroll of cut&paste

        switch (type) {
        case Event.ONMOUSEOVER:
            closeTimer.cancel();
            closing = false;
            break;
        case Event.ONMOUSEOUT:
            hideTooltip();
            break;
        default:
            // NOP
        }
    }

}

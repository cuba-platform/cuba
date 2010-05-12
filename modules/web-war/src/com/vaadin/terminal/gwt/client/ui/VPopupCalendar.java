/*
 * Copyright 2009 IT Mill Ltd.
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

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VPopupCalendar extends VTextualDate implements Paintable, Field,
        ClickHandler, CloseHandler<PopupPanel> {

    private class CalendarToggler extends FlowPanel implements HasClickHandlers {
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }

    private final CalendarToggler calendarToggle = new CalendarToggler();

    private final VCalendarPanel calendar;

    private final VOverlay popup;
    private boolean open = false;

    public VPopupCalendar() {
        super();

        calendarToggle.setStyleName(CLASSNAME + "-wrap");
        calendarToggle.addClickHandler(this);
        add(calendarToggle);

        calendar = new VCalendarPanel(this);
        popup = new VOverlay(true, true, true);
        popup.setStyleName(VDateField.CLASSNAME + "-popup");
        popup.setWidget(calendar);
        popup.addCloseHandler(this);

        DOM.setElementProperty(calendar.getElement(), "id",
                "PID_VAADIN_POPUPCAL");

        calendarToggle.add(text);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);
        addStyleName(CLASSNAME + "-popupcalendar");
        popup.setStyleName(VDateField.CLASSNAME + "-popup "
                + VDateField.CLASSNAME + "-"
                + resolutionToString(currentResolution));
        if (date != null) {
            calendar.updateCalendar();
        }
    }

    public void onClick(ClickEvent event) {
        if (event.getNativeEvent().getEventTarget().cast() != text.getElement() && !open && !readonly && enabled) {
            open = true;
            calendar.updateCalendar();
            // clear previous values
            popup.setWidth("");
            popup.setHeight("");
            popup.setPopupPositionAndShow(new PositionCallback() {
                public void setPosition(int offsetWidth, int offsetHeight) {
                    final int w = offsetWidth;
                    final int h = offsetHeight;
                    int t = calendarToggle.getAbsoluteTop();
                    int l = calendarToggle.getAbsoluteLeft();
                    if (l + w > Window.getClientWidth()
                            + Window.getScrollLeft()) {
                        l = Window.getClientWidth() + Window.getScrollLeft()
                                - w;
                    }
                    if (t + h + calendarToggle.getOffsetHeight() + 30 > Window
                            .getClientHeight()
                            + Window.getScrollTop()) {
                        t = Window.getClientHeight() + Window.getScrollTop()
                                - h - calendarToggle.getOffsetHeight() - 30;
                        l += calendarToggle.getOffsetWidth();
                    }

                    // fix size
                    popup.setWidth(w + "px");
                    popup.setHeight(h + "px");

                    popup.setPopupPosition(l, t
                            + calendarToggle.getOffsetHeight() + 2);

                    setFocus(true);
                }
            });
        }
    }

    public void onClose(CloseEvent<PopupPanel> event) {
        if (event.getSource() == popup) {
            buildDate();
            // Sigh.
            Timer t = new Timer() {
                @Override
                public void run() {
                    open = false;
                }
            };
            t.schedule(100);
        }
    }

    /**
     * Sets focus to Calendar panel.
     *
     * @param focus
     */
    public void setFocus(boolean focus) {
        calendar.setFocus(focus);
    }

    @Override
    public void iLayout() {
        if (needLayout) {
            text.setWidth("100%");
        }
    }

}
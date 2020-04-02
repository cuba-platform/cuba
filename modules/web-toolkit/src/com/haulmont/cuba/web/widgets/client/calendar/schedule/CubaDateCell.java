/*
 * Copyright (c) 2008-2020 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.calendar.schedule;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.client.calendar.CubaCalendarWidget;
import com.vaadin.v7.client.ui.calendar.schedule.DateCell;
import com.vaadin.v7.client.ui.calendar.schedule.WeekGrid;

import java.util.Date;

public class CubaDateCell extends DateCell {

    public static final String SLOT_NUMBER_STYLENAME = "c-date-number";

    protected boolean rangeSelect = false;

    public CubaDateCell(WeekGrid parent, Date date) {
        super(parent, date);

        // add style with number for checking in events which slot was clicked
        for (int i = 0; i < slots.size(); i++) {
            Element slotElement = slots.get(i).getElement();
            slotElement.addClassName(SLOT_NUMBER_STYLENAME + "-" + i);
        }

        sinkEvents(Event.ONCLICK);
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        // consider that rangeSelect will be fired
        rangeSelect = true;

        super.onMouseUp(event);
    }

    @Override
    public void cancelRangeSelect() {
        super.cancelRangeSelect();

        // rangeSelect was cancelled
        rangeSelect = false;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        // ONCLICK is invoked after mouseDown and mouseUp events, so we need
        // to check that rangeSelect event was not fired
        if (event.getTypeInt() == Event.ONCLICK && !rangeSelect) {
            Element target = Element.as(event.getEventTarget());
            String targetSlotNumber = getSlotNumberStyle(target);

            for (DateCellSlot slot : slots) {
                String className = slot.getElement().getClassName();

                if (containsSlotNumber(className, targetSlotNumber)) {
                    CubaCalendarWidget calendar = (CubaCalendarWidget) weekgrid.getCalendar();
                    if (calendar.getDayClickListener() != null) {
                        calendar.getDayClickListener().accept(slot.getFrom());
                    }
                    break;
                }
            }
        }
    }

    protected String getSlotNumberStyle(Element element) {
        for (int i = 0; i < slots.size(); i++) {
            String className = element.getClassName();
            String slotNumberStyle = SLOT_NUMBER_STYLENAME + "-" + i;

            if (containsSlotNumber(className, slotNumberStyle)) {
                return slotNumberStyle;
            }
        }

        return null;
    }

    protected boolean containsSlotNumber(String className, String slotNumberStyle) {
        String[] classes = className.split(" ");
        for (String selector : classes) {
            if (selector.equals(slotNumberStyle)) {
                return true;
            }
        }

        return false;
    }
}

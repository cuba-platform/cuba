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
 */

package com.haulmont.cuba.gui.components.calendar;

import com.haulmont.cuba.gui.components.Calendar;

import java.util.ArrayList;
import java.util.List;

public class ListCalendarEventProvider implements CalendarEventProvider {

    protected List<CalendarEvent> eventList = new ArrayList<>();
    protected List<EventSetChangeListener> listeners;
    protected Calendar calendar;
    protected CalendarEvent.EventChangeListener eventChangeListener =
            (CalendarEvent.EventChangeListener) eventChangeEvent ->
                    fireEventSetChange();

    protected void fireEventSetChange() {
        if (listeners != null) {
            EventSetChangeEvent eventSetChangeEvent = new EventSetChangeEvent(this);

            for (EventSetChangeListener eventSetChangeListener : listeners) {
                eventSetChangeListener.eventSetChange(eventSetChangeEvent);
            }
        }
    }

    @Override
    public List<CalendarEvent> getEvents() {
        return eventList;
    }

    @Override
    public void addEvent(CalendarEvent calendarEvent) {
        calendarEvent.addEventChangeListener(eventChangeListener);
        eventList.add(calendarEvent);
        fireEventSetChange();
    }

    @Override
    public void removeEvent(CalendarEvent calendarEvent) {
        calendarEvent.removeEventChangeListener(eventChangeListener);
        eventList.remove(calendarEvent);
        fireEventSetChange();
    }

    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public void addEventSetChangeListener(EventSetChangeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeEventSetChangeListener(EventSetChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
}
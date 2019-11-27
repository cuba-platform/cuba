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

package com.haulmont.cuba.web.gui.components.calendar;

import com.haulmont.cuba.gui.components.calendar.CalendarEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class CalendarEventWrapper<V> implements com.vaadin.v7.ui.components.calendar.event.CalendarEvent,
        com.vaadin.v7.ui.components.calendar.event.CalendarEvent.EventChangeNotifier {

    protected Function<V, Date> modelToPresentationConverter;

    protected CalendarEvent<V> calendarEvent;
    protected List<EventChangeListener> eventChangeListeners;

    public CalendarEventWrapper(CalendarEvent<V> calendarEvent, Function<V, Date> modelToPresentationConverter) {
        this.calendarEvent = calendarEvent;
        this.modelToPresentationConverter = modelToPresentationConverter;

        calendarEvent.addEventChangeListener(eventChangeEvent -> fireItemChanged());
    }

    protected void fireItemChanged() {
        if (eventChangeListeners != null) {
            EventChangeEvent event = new EventChangeEvent(this);

            for (EventChangeListener listener : eventChangeListeners) {
                listener.eventChange(event);
            }
        }
    }

    @Override
    public Date getStart() {
        return modelToPresentationConverter != null
                ? modelToPresentationConverter.apply(calendarEvent.getStart())
                : (Date) calendarEvent.getStart();
    }

    @Override
    public Date getEnd() {
        return modelToPresentationConverter != null
                ? modelToPresentationConverter.apply(calendarEvent.getEnd())
                : (Date) calendarEvent.getEnd();
    }

    @Override
    public String getCaption() {
        return calendarEvent.getCaption();
    }

    @Override
    public String getDescription() {
        return calendarEvent.getDescription();
    }

    @Override
    public String getStyleName() {
        return calendarEvent.getStyleName();
    }

    @Override
    public boolean isAllDay() {
        return calendarEvent.isAllDay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CalendarEventWrapper that = (CalendarEventWrapper) o;

        return calendarEvent.equals(that.calendarEvent);
    }

    @Override
    public int hashCode() {
        return calendarEvent.hashCode();
    }

    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }

    @Override
    public void addEventChangeListener(EventChangeListener listener) {
        if (eventChangeListeners == null) {
            eventChangeListeners = new ArrayList<>();
        }

        if (!eventChangeListeners.contains(listener)) {
            eventChangeListeners.add(listener);
        }
    }

    @Override
    public void removeEventChangeListener(EventChangeListener listener) {
        if (eventChangeListeners != null) {
            eventChangeListeners.remove(listener);
        }
    }
}
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

import com.haulmont.cuba.gui.components.calendar.CalendarEventProvider;
import com.vaadin.v7.ui.components.calendar.event.CalendarEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class CalendarEventProviderWrapper<V>
        implements
            com.vaadin.v7.ui.components.calendar.event.CalendarEditableEventProvider,
            com.vaadin.v7.ui.components.calendar.event.CalendarEventProvider.EventSetChangeNotifier {

    protected CalendarEventProvider calendarEventProvider;
    protected List<CalendarEvent> itemsCache = new ArrayList<>();
    protected List<EventSetChangeListener> listeners = new ArrayList<>();

    protected Function<V, Date> modelToPresentationConverter;

    public CalendarEventProviderWrapper(CalendarEventProvider calendarEventProvider, Function<V, Date> modelToPresentationConverter) {
        this.calendarEventProvider = calendarEventProvider;
        this.modelToPresentationConverter = modelToPresentationConverter;
        calendarEventProvider.addEventSetChangeListener(changeEvent -> fireEventSetChange());
    }

    protected void fireEventSetChange() {
        itemsCache.clear();

        EventSetChangeEvent event = new EventSetChangeEvent(this);
        for (EventSetChangeListener listener : listeners) {
            listener.eventSetChange(event);
        }
    }

    public CalendarEventProvider getCalendarEventProvider() {
        return calendarEventProvider;
    }

    @Override
    public void addEvent(CalendarEvent event) {
        throw new UnsupportedOperationException("Wrapper does not support direct access");
    }

    @Override
    public void removeEvent(CalendarEvent event) {
        throw new UnsupportedOperationException("Wrapper does not support direct access");
    }

    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {
        if (itemsCache.isEmpty()) {
            for (com.haulmont.cuba.gui.components.calendar.CalendarEvent calendarEvent : calendarEventProvider.getEvents()) {
                CalendarEventWrapper calendarEventWrapper = new CalendarEventWrapper<>(calendarEvent, modelToPresentationConverter);
                itemsCache.add(calendarEventWrapper);
            }

            return itemsCache;
        } else {
            return itemsCache;
        }
    }

    @Override
    public void addEventSetChangeListener(EventSetChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeEventSetChangeListener(EventSetChangeListener listener) {
        listeners.remove(listener);
    }
}
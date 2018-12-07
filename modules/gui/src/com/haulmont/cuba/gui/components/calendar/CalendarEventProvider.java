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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.Calendar;

import java.io.Serializable;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;

public interface CalendarEventProvider {

    /**
     * Adds {@link CalendarEvent} to the event provider list.
     * <p>
     * Not supported by {@link EntityCalendarEventProvider}, use datasource for changing data items.
     *
     * @param event calendar event
     */
    void addEvent(CalendarEvent event);

    /**
     * Removes {@link CalendarEvent} from the event provider list.
     * <p>
     * Not supported by {@link EntityCalendarEventProvider}, use datasource for changing data items.
     *
     * @param event calendar event
     */
    void removeEvent(CalendarEvent event);

    /**
     * Removes all {@link CalendarEvent} in the event provider list.
     * <p>
     * Not supported by {@link EntityCalendarEventProvider}, use datasource for changing data items.
     */
    void removeAllEvents();

    void setCalendar(Calendar calendar);

    Subscription addEventSetChangeListener(Consumer<EventSetChangeEvent> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} onject instead
     */
    @Deprecated
    void removeEventSetChangeListener(Consumer<EventSetChangeEvent> listener);

    List<CalendarEvent> getEvents();

    class EventSetChangeEvent extends EventObject {

        public EventSetChangeEvent(CalendarEventProvider source) {
            super(source);
        }

        @Override
        public CalendarEventProvider getSource() {
            return (CalendarEventProvider) super.getSource();
        }

        public CalendarEventProvider getProvider() {
            return getSource();
        }
    }

    /**
     * @deprecated Use {@link Consumer} instead
     */
    @Deprecated
    interface EventSetChangeListener extends Consumer<EventSetChangeEvent> {

        @Override
        default void accept(EventSetChangeEvent event) {
            eventSetChange(event);
        }

        void eventSetChange(EventSetChangeEvent changeEvent);
    }
}
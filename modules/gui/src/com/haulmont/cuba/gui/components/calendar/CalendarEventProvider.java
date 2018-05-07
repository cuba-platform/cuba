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

import java.io.Serializable;
import java.util.List;

public interface CalendarEventProvider {

    void addEvent(CalendarEvent event);
    void removeEvent(CalendarEvent event);

    /**
     * Removes all {@link CalendarEvent} in the list event provider.
     */
    void removeAllEvents();

    void setCalendar(Calendar calendar);

    void addEventSetChangeListener(EventSetChangeListener listener);
    void removeEventSetChangeListener(EventSetChangeListener listener);

    List<CalendarEvent> getEvents();

    class EventSetChangeEvent implements Serializable {
        private CalendarEventProvider source;

        public EventSetChangeEvent(CalendarEventProvider source) {
            this.source = source;
        }

        public CalendarEventProvider getProvider() {
            return source;
        }
    }

    interface EventSetChangeListener extends Serializable {

        void eventSetChange(EventSetChangeEvent changeEvent);
    }
}
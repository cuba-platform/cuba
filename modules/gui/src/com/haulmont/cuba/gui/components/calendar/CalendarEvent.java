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

import java.io.Serializable;
import java.util.EventObject;
import java.util.function.Consumer;

public interface CalendarEvent<V> extends Serializable {
    V getStart();

    void setStart(V start);

    V getEnd();

    void setEnd(V end);

    String getCaption();
    void setCaption(String caption);

    void setDescription(String description);
    String getDescription();

    String getStyleName();
    void setStyleName(String styleName);

    boolean isAllDay();
    void setAllDay(boolean isAllDay);

    Subscription addEventChangeListener(Consumer<EventChangeEvent<V>> listener);

    /**
     * @param listener a listener to remove
     * @deprecated Use {@link Subscription} object instead
     */
    @Deprecated
    void removeEventChangeListener(Consumer<EventChangeEvent<V>> listener);

    class EventChangeEvent<V> extends EventObject {

        public EventChangeEvent(CalendarEvent source) {
            super(source);
        }

        @Override
        public CalendarEvent<V> getSource() {
            return (CalendarEvent<V>) super.getSource();
        }

        public CalendarEvent<V> getCalendarEvent() {
            return getSource();
        }
    }

    /**
     * @deprecated Use {@link Consumer} instead
     */
    @Deprecated
    interface EventChangeListener<V> extends Consumer<EventChangeEvent<V>> {

        @Override
        default void accept(EventChangeEvent<V> eventChangeEvent) {
            eventChange(eventChangeEvent);
        }

        void eventChange(EventChangeEvent<V> eventChangeEvent);
    }
}

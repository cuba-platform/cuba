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

import com.haulmont.bali.events.EventHub;
import com.haulmont.bali.events.Subscription;

import java.util.function.Consumer;

public class SimpleCalendarEvent<V> implements CalendarEvent<V> {

    protected V start;
    protected V end;
    protected String caption;
    protected String description;
    protected String styleName;
    protected boolean isAllDay;

    protected EventHub events = new EventHub();

    protected void fireDataChanged() {
        events.publish(EventChangeEvent.class, new EventChangeEvent<>(this));
    }

    @Override
    public V getStart() {
        return start;
    }

    @Override
    public void setStart(V start) {
        this.start = start;
        fireDataChanged();
    }

    @Override
    public V getEnd() {
        return end;
    }

    @Override
    public void setEnd(V end) {
        this.end = end;
        fireDataChanged();
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        fireDataChanged();
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        fireDataChanged();
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
        fireDataChanged();
    }

    @Override
    public boolean isAllDay() {
        return isAllDay;
    }

    @Override
    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
        fireDataChanged();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addEventChangeListener(Consumer<EventChangeEvent<V>> listener) {
        return events.subscribe(EventChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeEventChangeListener(Consumer<EventChangeEvent<V>> listener) {
        events.unsubscribe(EventChangeEvent.class, (Consumer) listener);
    }
}
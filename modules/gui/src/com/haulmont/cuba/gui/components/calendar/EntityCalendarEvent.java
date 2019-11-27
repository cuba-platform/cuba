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
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.calendar.EntityCalendarEventProvider;
import org.apache.commons.lang3.BooleanUtils;

import java.util.function.Consumer;

public class EntityCalendarEvent<E extends Entity, V> implements CalendarEvent<V> {

    protected final E entity;
    protected final EntityCalendarEventProvider provider;

    protected EventHub events = new EventHub();

    public EntityCalendarEvent(E entity, EntityCalendarEventProvider provider) {
        this.entity = entity;
        this.provider = provider;

        // todo bad practice, use datasource listener instead
        this.entity.addPropertyChangeListener(this::onPropertyChanged);
    }

    protected void onPropertyChanged(Instance.PropertyChangeEvent event) {
        events.publish(EventChangeEvent.class, new EventChangeEvent<>(this));
    }

    public E getEntity() {
        return entity;
    }

    @Override
    public V getStart() {
        if (provider.getStartDateProperty() != null) {
            return entity.getValue(provider.getStartDateProperty());
        } else {
            return null;
        }
    }

    @Override
    public void setStart(V start) {
        entity.setValue(provider.getStartDateProperty(), start);
    }

    @Override
    public V getEnd() {
        if (provider.getEndDateProperty() != null) {
            return entity.getValue(provider.getEndDateProperty());
        } else {
            return null;
        }
    }

    @Override
    public void setEnd(V end) {
        entity.setValue(provider.getEndDateProperty(), end);
    }

    @Override
    public String getCaption() {
        if (provider.getCaptionProperty() != null) {
            return entity.getValue(provider.getCaptionProperty());
        } else {
            return null;
        }
    }

    @Override
    public void setCaption(String caption) {
        entity.setValue(provider.getCaptionProperty(), caption);
    }

    @Override
    public void setDescription(String description) {
        entity.setValue(provider.getDescriptionProperty(), description);
    }

    @Override
    public String getDescription() {
        if (provider.getDescriptionProperty() != null) {
            return entity.getValue(provider.getDescriptionProperty());
        } else {
            return null;
        }
    }

    @Override
    public String getStyleName() {
        if (provider.getStyleNameProperty() != null) {
            return entity.getValue(provider.getStyleNameProperty());
        } else {
            return null;
        }
    }

    @Override
    public void setStyleName(String styleName) {
        entity.setValue(provider.getStyleNameProperty(), styleName);
    }

    @Override
    public boolean isAllDay() {
        if (provider.getIsAllDayProperty() != null) {
            return BooleanUtils.isTrue(entity.getValue(provider.getIsAllDayProperty()));
        } else {
            return false;
        }
    }

    @Override
    public void setAllDay(boolean isAllDay) {
        entity.setValue(provider.getIsAllDayProperty(), isAllDay);
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

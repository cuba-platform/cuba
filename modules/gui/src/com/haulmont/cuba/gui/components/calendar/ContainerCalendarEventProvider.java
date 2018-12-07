/*
 * Copyright (c) 2008-2018 Haulmont.
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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.components.data.calendar.EntityCalendarEventProvider;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionContainer.CollectionChangeEvent;
import com.haulmont.cuba.gui.model.InstanceContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ContainerCalendarEventProvider<E extends Entity>
        implements CalendarEventProvider, EntityCalendarEventProvider {

    protected List<CalendarEvent> itemsCache;

    protected CollectionContainer<E> container;

    protected Calendar calendar;

    protected String startDateProperty;
    protected String endDateProperty;
    protected String captionProperty;
    protected String descriptionProperty;
    protected String styleNameProperty;
    protected String allDayProperty;

    protected EventHub events = new EventHub();

    protected Subscription collectionChangeListener;
    protected Subscription propertyChangeListener;

    public ContainerCalendarEventProvider(CollectionContainer<E> container) {
        this.container = container;

        collectionChangeListener = this.container.addCollectionChangeListener(this::onCollectionChanged);
        propertyChangeListener = this.container.addItemPropertyChangeListener(this::onItemPropertyChanged);
    }

    protected void onCollectionChanged(CollectionChangeEvent<E> event) {
        itemsCache = null;
        events.publish(EventSetChangeEvent.class, new EventSetChangeEvent(this));
    }

    protected void onItemPropertyChanged(InstanceContainer.ItemPropertyChangeEvent<E> event) {
        if (event.getProperty().equals(startDateProperty)
                || event.getProperty().equals(endDateProperty)
                || event.getProperty().equals(captionProperty)
                || event.getProperty().equals(descriptionProperty)
                || event.getProperty().equals(styleNameProperty)
                || event.getProperty().equals(allDayProperty)) {
            itemsCache = null;
            events.publish(EventSetChangeEvent.class, new EventSetChangeEvent(this));
        }
    }

    public CollectionContainer<E> getContainer() {
        return container;
    }

    @Override
    public void addEvent(CalendarEvent event) {
        throw new UnsupportedOperationException("Use container for changing data items of " +
                "ContainerCalendarEventProvider");
    }

    @Override
    public void removeEvent(CalendarEvent event) {
        throw new UnsupportedOperationException("Use container for changing data items of " +
                "ContainerCalendarEventProvider");
    }

    @Override
    public void removeAllEvents() {
        throw new UnsupportedOperationException("Use container for changing data items of " +
                "ContainerCalendarEventProvider");
    }

    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public Subscription addEventSetChangeListener(Consumer<EventSetChangeEvent> listener) {
        return events.subscribe(EventSetChangeEvent.class, listener);
    }

    @Override
    public void removeEventSetChangeListener(Consumer<EventSetChangeEvent> listener) {
        events.unsubscribe(EventSetChangeEvent.class, listener);
    }

    @Override
    public List<CalendarEvent> getEvents() {
        if (startDateProperty == null
                || endDateProperty == null
                || captionProperty == null) {
            return Collections.emptyList();
        }

        if (itemsCache == null) {
            itemsCache = new ArrayList<>();
            for (E item : container.getItems()) {
                itemsCache.add(new EntityCalendarEvent<>(item, this));
            }
            return itemsCache;
        } else {
            return itemsCache;
        }
    }

    @Override
    public void setStartDateProperty(String startDateProperty) {
        this.startDateProperty = startDateProperty;
    }

    @Override
    public String getStartDateProperty() {
        return startDateProperty;
    }

    @Override
    public void setEndDateProperty(String endDateProperty) {
        this.endDateProperty = endDateProperty;
    }

    @Override
    public String getEndDateProperty() {
        return endDateProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setDescriptionProperty(String descriptionProperty) {
        this.descriptionProperty = descriptionProperty;
    }

    @Override
    public String getDescriptionProperty() {
        return descriptionProperty;
    }

    @Override
    public void setStyleNameProperty(String styleNameProperty) {
        this.styleNameProperty = styleNameProperty;
    }

    @Override
    public String getStyleNameProperty() {
        return styleNameProperty;
    }

    @Override
    public void setAllDayProperty(String allDayProperty) {
        this.allDayProperty = allDayProperty;
    }

    @Override
    public String getIsAllDayProperty() {
        return allDayProperty;
    }

    @Override
    public void unbind() {
        if (collectionChangeListener != null) {
            collectionChangeListener.remove();
        }

        if (propertyChangeListener != null) {
            propertyChangeListener.remove();
        }
    }
}

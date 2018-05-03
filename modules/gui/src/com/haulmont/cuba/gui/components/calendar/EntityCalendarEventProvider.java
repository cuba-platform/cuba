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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Calendar;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityCalendarEventProvider implements CalendarEventProvider, Serializable {
    protected List<CalendarEvent> itemsCache;
    protected CollectionDatasource datasource;
    protected List<EventSetChangeListener> listeners;
    protected Calendar calendar;

    protected String startDateProperty;
    protected String endDateProperty;
    protected String captionProperty;
    protected String descriptionProperty;
    protected String styleNameProperty;
    protected String allDayProperty;

    protected CollectionDatasource.CollectionChangeListener collectionChangeListener;
    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;

    public EntityCalendarEventProvider (CollectionDatasource datasource) {
        this.datasource = datasource;

        collectionChangeListener = e -> {
            itemsCache = null;
            fireDataChanged();
        };

        itemPropertyChangeListener = e -> {
            if (e.getProperty() != null) {
                if (e.getProperty().equals(startDateProperty)
                        || e.getProperty().equals(endDateProperty)
                        || e.getProperty().equals(captionProperty)
                        || e.getProperty().equals(descriptionProperty)
                        || e.getProperty().equals(styleNameProperty)
                        || e.getProperty().equals(allDayProperty)) {
                    itemsCache = null;
                    fireDataChanged();
                }
            }
        };

        datasource.addCollectionChangeListener(collectionChangeListener);
        datasource.addItemPropertyChangeListener(itemPropertyChangeListener);
    }

    protected void fireDataChanged() {
        if (listeners != null) {
            EventSetChangeEvent eventSetChangeEvent = new EventSetChangeEvent(this);

            for (EventSetChangeListener eventSetChangeListener : listeners) {
                eventSetChangeListener.eventSetChange(eventSetChangeEvent);
            }
        }
    }

    @Override
    public void addEvent(CalendarEvent event) {
        throw new UnsupportedOperationException("Use datasource for changing data items of EntityCalendarEventProvider");
    }

    @Override
    public void removeEvent(CalendarEvent event) {
        throw new UnsupportedOperationException("Use datasource for changing data items of EntityCalendarEventProvider");
    }

    @Override
    public void removeAllEvents() {
        throw new UnsupportedOperationException("Use datasource for changing data items of EntityCalendarEventProvider");
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

    @Override
    public List<CalendarEvent> getEvents() {
        if (startDateProperty == null || endDateProperty == null || captionProperty == null) {
            return new ArrayList<>();
        }

        if (itemsCache == null) {
            itemsCache = new ArrayList<>();
            for (Entity entity : (Collection<Entity>) datasource.getItems()) {
                itemsCache.add(new EntityCalendarEvent(entity, this));
            }
            return itemsCache;
        } else {
            return itemsCache;
        }
    }

    public void setStartDateProperty(String startDateProperty) {
        this.startDateProperty = startDateProperty;
    }

    public String getStartDateProperty() {
        return startDateProperty;
    }

    public void setEndDateProperty(String endDateProperty) {
        this.endDateProperty = endDateProperty;
    }

    public String getEndDateProperty() {
        return endDateProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setDescriptionProperty(String descriptionProperty) {
        this.descriptionProperty = descriptionProperty;
    }

    public String getDescriptionProperty() {
        return descriptionProperty;
    }

    public void setStyleNameProperty(String styleNameProperty) {
        this.styleNameProperty = styleNameProperty;
    }

    public String getStyleNameProperty() {
        return styleNameProperty;
    }

    public void setAllDayProperty(String allDayProperty) {
        this.allDayProperty = allDayProperty;
    }

    public String getIsAllDayProperty() {
        return allDayProperty;
    }

    public void unbind() {
        datasource.removeCollectionChangeListener(collectionChangeListener);
        datasource.removeItemPropertyChangeListener(itemPropertyChangeListener);
    }
}
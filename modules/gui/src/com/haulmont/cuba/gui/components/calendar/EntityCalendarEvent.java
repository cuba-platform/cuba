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

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityCalendarEvent implements CalendarEvent {

    protected final Entity entity;
    protected final EntityCalendarEventProvider provider;
    protected List<EventChangeListener> eventChangeListeners;

    public EntityCalendarEvent(Entity entity, EntityCalendarEventProvider provider) {
        this.entity = entity;
        this.provider = provider;

        this.entity.addPropertyChangeListener(new Instance.PropertyChangeListener() {
            @Override
            public void propertyChanged(Instance.PropertyChangeEvent e) {
                fireEventChanged();
            }
        });
    }

    protected void fireEventChanged() {
        if (eventChangeListeners != null) {
            EventChangeEvent eventChangeEvent = new EventChangeEvent(this);
            for (EventChangeListener listener : eventChangeListeners) {
                listener.eventChange(eventChangeEvent);
            }
        }
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public Date getStart() {
        if (provider.getStartDateProperty() != null) {
            return entity.getValue(provider.getStartDateProperty());
        } else {
            return null;
        }
    }

    @Override
    public void setStart(Date start) {
        entity.setValue(provider.getStartDateProperty(), start);
    }

    @Override
    public Date getEnd() {
        if (provider.getEndDateProperty() != null) {
            return entity.getValue(provider.getEndDateProperty());
        } else {
            return null;
        }
    }

    @Override
    public void setEnd(Date end) {
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

            if (eventChangeListeners.isEmpty()) {
                eventChangeListeners = null;
            }
        }
    }
}

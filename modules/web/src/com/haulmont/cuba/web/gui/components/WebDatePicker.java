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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaDatePicker;
import com.vaadin.ui.InlineDateField;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class WebDatePicker extends WebAbstractField<InlineDateField> implements DatePicker {

    protected Resolution resolution = Resolution.DAY;

    protected boolean updatingInstance;

    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected Datasource.ItemChangeListener itemChangeListener;

    public WebDatePicker() {
        this.component = new CubaDatePicker();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);

        Messages messages = AppBeans.get(Messages.NAME);
        component.setDateOutOfRangeMessage(messages.getMainMessage("datePicker.dateOutOfRangeMessage"));
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath);
            }
        };
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        Preconditions.checkNotNullArgument(resolution);

        this.resolution = resolution;
        com.vaadin.shared.ui.datefield.Resolution vResolution;
        switch (resolution) {
            case MONTH:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.MONTH;
                break;
            case YEAR:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.YEAR;
                break;
            case DAY:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.DAY;
                break;
            default:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.DAY;
                break;
        }

        component.setResolution(vResolution);
    }

    protected boolean checkRange(Date value) {
        if (value != null) {
            if (component.getRangeStart() != null && value.before(component.getRangeStart())) {
                handleDateOutOfRange(value);
                return false;
            }

            if (component.getRangeEnd() != null && value.after(component.getRangeEnd())) {
                handleDateOutOfRange(value);
                return false;
            }
        }

        return true;
    }

    protected void handleDateOutOfRange(Date value) {
        Messages messages = AppBeans.get(Messages.NAME);
        if (getFrame() != null) {
            getFrame().showNotification(messages.getMainMessage("datePicker.dateOutOfRangeMessage"),
                    Frame.NotificationType.WARNING);
        }

        component.setValue((Date) prevValue);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        MetaClass metaClass = datasource.getMetaClass();
        resolveMetaPropertyPath(metaClass, property);

        component.addValueChangeListener(event -> {
            if (updatingInstance) {
                return;
            }

            if (!checkRange(component.getValue())) {
                return;
            }

            updatingInstance = true;
            try {
                if (datasource != null && metaPropertyPath != null) {
                    if (datasource.getItem() != null) {
                        InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), component.getValue());
                    }
                }
            } finally {
                updatingInstance = false;
            }

            Object newValue = getValue();
            fireValueChanged(newValue);
        });

        itemChangeListener = e -> {
            if (updatingInstance) {
                return;
            }
            Date value = getEntityValue(e.getItem());
            setValueToFields(value);
            fireValueChanged(value);
        };
        //noinspection unchecked
        datasource.addItemChangeListener(new WeakItemChangeListener(datasource, itemChangeListener));

        itemPropertyChangeListener = e -> {
            if (updatingInstance) {
                return;
            }
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                setValueToFields((Date) e.getValue());
                fireValueChanged(e.getValue());
            }
        };
        //noinspection unchecked
        datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener));

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                setValueToFields(value);
                fireValueChanged(value);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaClass, property));
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }

        initBeanValidator();
        setDateRangeByProperty(metaProperty);
    }

    protected void setDateRangeByProperty(MetaProperty metaProperty) {
        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);

        if (metaProperty.getAnnotations().get(Past.class.getName()) != null) {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            Date currentTimestamp = timeSource.currentTimestamp();

            Calendar calendar = Calendar.getInstance(sessionSource.getLocale());
            calendar.setTime(currentTimestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

            setRangeEnd(calendar.getTime());
        } else if (metaProperty.getAnnotations().get(Future.class.getName()) != null) {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            Date currentTimestamp = timeSource.currentTimestamp();

            Calendar calendar = Calendar.getInstance(sessionSource.getLocale());
            calendar.setTime(currentTimestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            setRangeStart(calendar.getTime());
        }
    }

    protected Date convertDate() {
        final Date datePickerDate = component.getValue();
        if (datePickerDate == null) {
            return null;
        }

        if (metaProperty != null) {
            Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
            if (javaClass.equals(java.sql.Date.class)) {
                return new java.sql.Date(datePickerDate.getTime());
            } else {
                return datePickerDate;
            }
        } else {
            return datePickerDate;
        }
    }

    protected Date getEntityValue(Entity item) {
        return InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
    }

    protected void fireValueChanged(Object value) {
        Object oldValue = prevValue;

        if (!Objects.equals(oldValue, value)) {
            prevValue = value;

            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }

    protected void setValueToFields(Date value) {
        updatingInstance = true;
        try {
            component.setValueIgnoreReadOnly(value);
        } finally {
            updatingInstance = false;
        }
    }

    @Override
    public Date getRangeStart() {
        return component.getRangeStart();
    }

    @Override
    public void setRangeStart(Date rangeStart) {
        component.setRangeStart(rangeStart);
    }

    @Override
    public Date getRangeEnd() {
        return component.getRangeEnd();
    }

    @Override
    public void setRangeEnd(Date rangeEnd) {
        component.setRangeEnd(rangeEnd);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Date getValue() {
        return convertDate();
    }

    @Override
    public void setValue(Object value) {
        if (!checkRange((Date) value)) {
            return;
        }

        super.setValue(value);
    }
}
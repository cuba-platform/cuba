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
 *
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.CubaDateField;
import com.haulmont.cuba.web.toolkit.ui.CubaDateFieldWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaMaskedTextField;
import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Time;
import java.util.*;

/**
 */
public class WebDateField extends WebAbstractField<CubaDateFieldWrapper> implements DateField {

    protected Resolution resolution;

    protected boolean editable = true;

    protected boolean updatingInstance;

    protected CubaDateField dateField;
    protected WebTimeField timeField;

    protected HorizontalLayout innerLayout;

    protected String dateTimeFormat;
    protected String dateFormat;
    protected String timeFormat;

    protected TimeZone timeZone;

    protected UserSession userSession;

    protected TimeZones timeZones = AppBeans.get(TimeZones.NAME);

    public WebDateField() {
        innerLayout = new HorizontalLayout();
        innerLayout.setSpacing(true);

        dateField = new CubaDateField();
        dateField.setImmediate(true);
        dateField.setInvalidAllowed(true);

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        userSession = sessionSource.getUserSession();

        Locale locale = userSession.getLocale();
        dateField.setDateFormat(Datatypes.getFormatStringsNN(locale).getDateFormat());
        dateField.setResolution(com.vaadin.shared.ui.datefield.Resolution.DAY);

        timeField = new WebTimeField();

        CubaMaskedTextField vTimeField = (CubaMaskedTextField) timeField.getComponent();
        vTimeField.setImmediate(true);
        vTimeField.setInvalidAllowed(false);
        vTimeField.setInvalidCommitted(true);

        dateField.addValueChangeListener(createDateValueChangeListener());
        timeField.addValueChangeListener(createTimeValueChangeListener());
        setResolution(Resolution.MIN);

        component = new CubaDateFieldWrapper(this, innerLayout);
    }

    protected Property.ValueChangeListener createDateValueChangeListener() {
        return e -> {
            updateInstance();

            if (component != null) {
                // Repaint error state
                component.markAsDirty();
            }
        };
    }

    protected Component.ValueChangeListener createTimeValueChangeListener() {
        return event -> updateInstance();
    }

    public CubaDateField getDateField() {
        return dateField;
    }

    public WebTimeField getTimeField() {
        return timeField;
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
        __setResolution(resolution);
        updateLayout();
    }

    @Override
    public String getDateFormat() {
        return dateTimeFormat;
    }

    @Override
    public void setDateFormat(String dateFormat) {
        dateTimeFormat = dateFormat;
        StringBuilder date = new StringBuilder(dateFormat);
        StringBuilder time = new StringBuilder(dateFormat);
        int timeStartPos = findTimeStartPos(dateFormat);
        if (timeStartPos >= 0) {
            time.delete(0, timeStartPos);
            date.delete(timeStartPos, dateFormat.length());
            timeFormat = StringUtils.trimToEmpty(time.toString());
            timeField.setFormat(timeFormat);
            setResolution(resolution);
        } else if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            setResolution(Resolution.DAY);
        }

        this.dateFormat = StringUtils.trimToEmpty(date.toString());
        dateField.setDateFormat(this.dateFormat);
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        TimeZone prevTimeZone = this.timeZone;
        Date value = getValue();
        this.timeZone = timeZone;
        if (value != null && !ObjectUtils.equals(prevTimeZone, timeZone)) {
            Date newValue = timeZones.convert(value,
                    TimeZone.getDefault(), timeZone != null ? timeZone : TimeZone.getDefault());
            setValueToFields(newValue);
        }
    }

    public void updateLayout() {
        innerLayout.removeAllComponents();
        innerLayout.addComponent(dateField);
        innerLayout.setExpandRatio(dateField, 1.0f);
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            innerLayout.setSpacing(true);
            innerLayout.addComponent(timeField.<com.vaadin.ui.Component>getComponent());
        } else {
            innerLayout.setSpacing(false);
        }
    }

    @Override
    protected void attachListener(CubaDateFieldWrapper component) {
        // do nothing
    }

    protected int findTimeStartPos(String dateTimeFormat) {
        List<Integer> positions = new ArrayList<>();

        char[] signs = new char[]{'H', 'h', 'm', 's'};
        for (char sign : signs) {
            int pos = dateTimeFormat.indexOf(sign);
            if (pos > -1) {
                positions.add(pos);
            }
        }
        return positions.isEmpty() ? -1 : Collections.min(positions);
    }

    protected void __setResolution(Resolution resolution) {
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            timeField.setResolution(resolution);
        } else {
            dateField.setResolution(WebComponentsHelper.convertDateFieldResolution(resolution));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        return (T) constructDate();
    }

    @Override
    public void setValue(Object value) {
        setValueToFields(toUserDate((Date) value));
        updateInstance();
    }

    protected Date toUserDate(Date date) {
        return timeZone == null ? date : timeZones.convert(date, TimeZone.getDefault(), timeZone);
    }

    protected Date toServerDate(Date date) {
        return timeZone == null ? date : timeZones.convert(date, timeZone, TimeZone.getDefault());
    }

    protected void setValueToFields(Date value) {
        updatingInstance = true;
        try {
            dateField.setValueIgnoreReadOnly(value);

            timeField.setValue(value);
        } finally {
            updatingInstance = false;
        }
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            timeField.setDebugId(testIdManager.getTestId(id + "_time"));
            dateField.setId(testIdManager.getTestId(id + "_date"));
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && AppUI.getCurrent().isTestMode()) {
            timeField.setId("timepart");
            dateField.setCubaId("datepart");
        }
    }

    protected boolean isHourUsed() {
        return resolution != null && resolution.ordinal() <= Resolution.HOUR.ordinal();
    }

    protected boolean isMinUsed() {
        return resolution != null && resolution.ordinal() <= Resolution.MIN.ordinal();
    }

    protected void updateInstance() {
        if (updatingInstance) {
            return;
        }

        updatingInstance = true;
        try {
            Date value = constructDate();
            if (datasource != null && metaPropertyPath != null) {
                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                }
            }
        } finally {
            updatingInstance = false;
        }

        Object newValue = getValue();
        fireValueChanged(newValue);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        resolveMetaPropertyPath(metaClass, property);

        if (metaProperty.getRange().isDatatype()
                && metaProperty.getRange().asDatatype().getName().equals(DateTimeDatatype.NAME)
                && timeZone == null) {
            Object ignoreUserTimeZone = metaProperty.getAnnotations().get(IgnoreUserTimeZone.class.getName());
            if (!Boolean.TRUE.equals(ignoreUserTimeZone)) {
                timeZone = userSession.getTimeZone();
            }
        }

        //noinspection unchecked
        datasource.addItemChangeListener(e -> {
            if (updatingInstance) {
                return;
            }
            Date value = getEntityValue(e.getItem());
            setValueToFields(toUserDate(value));
            fireValueChanged(value);
        });

        //noinspection unchecked
        datasource.addItemPropertyChangeListener(e -> {
            if (updatingInstance) {
                return;
            }
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                setValueToFields(toUserDate((Date) e.getValue()));
                fireValueChanged(e.getValue());
            }
        });

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                setValueToFields(toUserDate(value));
                fireValueChanged(value);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }
    }

    protected Date getEntityValue(Entity item) {
        return InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
    }

    protected void fireValueChanged(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            Object oldValue = prevValue;

            prevValue = value;

            if (listeners != null && !listeners.isEmpty()) {
                ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
                for (ValueChangeListener listener : listeners) {
                    listener.valueChanged(event);
                }
            }
        }
    }

    protected Date constructDate() {
        final Date datePickerDate = dateField.getValue();
        if (datePickerDate == null) {
            return null;
        }

        Calendar c = Calendar.getInstance(userSession.getLocale());
        c.setTime(datePickerDate);
        if (timeField.getValue() == null) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
        } else {
            Calendar c2 = Calendar.getInstance(userSession.getLocale());
            c2.setTime(timeField.<Date>getValue());

            c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
            c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
            c.set(Calendar.SECOND, c2.get(Calendar.SECOND));
        }

        Date serverDate = toServerDate(c.getTime());

        if (metaProperty != null) {
            Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
            if (javaClass.equals(java.sql.Date.class)) {
                return new java.sql.Date(serverDate.getTime());
            } else if (javaClass.equals(Time.class)) {
                return new Time(serverDate.getTime());
            } else {
                return serverDate;
            }
        } else {
            return serverDate;
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (this.editable == editable) {
            return;
        }
        this.editable = editable;

        setEditableInternal(editable);
    }

    protected void setEditableInternal(boolean editable) {
        timeField.setEditable(editable);
        dateField.setReadOnly(!editable);

        component.setCompositionReadOnly(!editable);
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisible() || !isEditable() || !isEnabled()) {
            return;
        }

        if (isRequired() && dateField.getValue() == null) {
            throw new RequiredValueMissingException(component.getRequiredError(), this);
        }

        if (validators != null) {
            for (Field.Validator validator : validators) {
                validator.validate(getValue());
            }
        }
    }
}
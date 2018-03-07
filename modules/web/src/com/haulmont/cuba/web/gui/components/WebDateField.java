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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaDateField;
import com.haulmont.cuba.web.widgets.CubaMaskedTextField;
import com.vaadin.ui.Layout;
import com.vaadin.v7.data.Property;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import java.sql.Time;
import java.util.*;

public class WebDateField extends WebAbstractField<CubaDateFieldWrapper> implements DateField {

    protected Resolution resolution;

    protected boolean updatingInstance;

    protected CubaDateField dateField;
    protected WebTimeField timeField;

    protected Layout innerLayout;

    protected String dateTimeFormat;
    protected String dateFormat;
    protected String timeFormat;

    protected TimeZone timeZone;

    protected UserSession userSession;

    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected WeakItemPropertyChangeListener weakItemPropertyChangeListener;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected WeakItemChangeListener weakItemChangeListener;

    protected boolean buffered = false;
    protected boolean updateTimeFieldResolution = false;

    public WebDateField() {
        innerLayout = new com.vaadin.ui.CssLayout();
        innerLayout.setPrimaryStyleName("c-datefield-layout");

        dateField = new CubaDateField();
        dateField.setValidationVisible(false);
        dateField.setInvalidAllowed(true);

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        userSession = sessionSource.getUserSession();

        Locale locale = userSession.getLocale();
        dateField.setDateFormat(Datatypes.getFormatStringsNN(locale).getDateFormat());
        dateField.setResolution(com.vaadin.v7.shared.ui.datefield.Resolution.DAY);

        timeField = new WebTimeField();

        CubaMaskedTextField vTimeField = (CubaMaskedTextField) timeField.getComponent();
        vTimeField.setInvalidAllowed(false);
        vTimeField.setInvalidCommitted(true);

        dateField.addValueChangeListener(createDateValueChangeListener());
        timeField.addValueChangeListener(createTimeValueChangeListener());
        setResolution(Resolution.MIN);

        component = new CubaDateFieldWrapper(this, innerLayout);
    }

    protected Property.ValueChangeListener createDateValueChangeListener() {
        return e -> {
            if (!checkRange(constructDate())) {
                return;
            }

            updateInstance();

            if (component != null) {
                // Repaint error state
                component.markAsDirty();
            }
        };
    }

    protected Component.ValueChangeListener createTimeValueChangeListener() {
        return event -> {
            if (!checkRange(constructDate())) {
                return;
            }
            if (!updateTimeFieldResolution) {
                updateInstance();
            }
        };
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
    public void setRangeStart(Date value) {
        dateField.setRangeStart(value);
    }

    @Override
    public Date getRangeStart() {
        return dateField.getRangeStart();
    }

    @Override
    public void setRangeEnd(Date value) {
        dateField.setRangeEnd(value);
    }

    @Override
    public Date getRangeEnd() {
        return dateField.getRangeEnd();
    }

    protected boolean checkRange(Date value) {
        if (updatingInstance) {
            return true;
        }

        if (value != null) {
            Date rangeStart = getRangeStart();
            if (rangeStart != null && value.before(rangeStart)) {
                handleDateOutOfRange(value);
                return false;
            }

            Date rangeEnd = getRangeEnd();
            if (rangeEnd != null && value.after(rangeEnd)) {
                handleDateOutOfRange(value);
                return false;
            }
        }

        return true;
    }

    protected void handleDateOutOfRange(Date value) {
        if (getFrame() != null) {
            Messages messages = AppBeans.get(Messages.NAME);
            getFrame().showNotification(messages.getMainMessage("datePicker.dateOutOfRangeMessage"),
                    Frame.NotificationType.TRAY);
        }

        updatingInstance = true;
        try {
            dateField.setValue((Date) prevValue);
            if (prevValue == null) {
                timeField.setValue(null);
            } else {
                timeField.setValue(extractTime((Date) prevValue));
            }
        } finally {
            updatingInstance = false;
        }
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
        dateField.setTimeZone(timeZone);
        if (value != null && !Objects.equals(prevTimeZone, timeZone)) {
            setValueToFields(value);
        }
    }

    public void updateLayout() {
        innerLayout.removeAllComponents();
        innerLayout.addComponent(dateField);

        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            innerLayout.addComponent(timeField.<com.vaadin.ui.Component>getComponent());
            innerLayout.addStyleName("c-datefield-withtime");
        } else {
            innerLayout.removeStyleName("c-datefield-withtime");
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
            // while changing resolution, timeField loses its value, so we need to set it again
            updateTimeFieldResolution = true;
            Date value = dateField.getValue();
            if (value == null) {
                timeField.setValue(null);
            } else {
                timeField.setValue(extractTime(value));
            }
            updateTimeFieldResolution = false;
        } else {
            dateField.setResolution(WebComponentsHelper.convertDateFieldResolution(resolution));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Date getValue() {
        return constructDate();
    }

    @Override
    public void setValue(Object value) {
        setValueToFields((Date) value);
        updateInstance();
    }

    @Override
    public void commit() {
        if (updatingInstance) {
            return;
        }

        updatingInstance = true;
        try {
            if (datasource != null && metaPropertyPath != null) {
                Date value = constructDate();

                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                    setModified(false);
                }
            }
        } finally {
            updatingInstance = false;
        }

        Object newValue = getValue();
        fireValueChanged(newValue);
    }

    @Override
    public void discard() {
        if (datasource != null && datasource.getItem() != null) {
            Date value = getEntityValue(datasource.getItem());
            setValueToFields(value);
            fireValueChanged(value);
        }
    }

    @Override
    public boolean isBuffered() {
        return buffered;
    }

    @Override
    public void setBuffered(boolean buffered) {
        this.buffered = buffered;
    }

    @Override
    public boolean isModified() {
        return dateField.isModified();
    }

    protected void setModified(boolean modified) {
        dateField.setModified(modified);
    }

    protected void setValueToFields(Date value) {
        updatingInstance = true;
        try {
            dateField.setValueIgnoreReadOnly(value);
            if (value == null) {
                timeField.setValue(null);
            } else {
                timeField.setValue(extractTime(value));
            }
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
            if (datasource != null && metaPropertyPath != null) {
                Date value = constructDate();

                if (!isBuffered()) {
                    if (datasource.getItem() != null) {
                        InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                        setModified(false);
                    }
                } else {
                    setModified(true);
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
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null)))
            return;

        if (this.datasource != null) {
            metaProperty = null;
            metaPropertyPath = null;

            component.setPropertyDataSource(null);

            //noinspection unchecked
            this.datasource.removeItemChangeListener(weakItemChangeListener);
            weakItemChangeListener = null;

            //noinspection unchecked
            this.datasource.removeItemPropertyChangeListener(weakItemPropertyChangeListener);
            weakItemPropertyChangeListener = null;

            this.datasource = null;

            if (itemWrapper != null) {
                itemWrapper.unsubscribe();
            }

            timeZone = null;

            disableBeanValidator();
        }

        if (datasource != null) {
            //noinspection unchecked
            this.datasource = datasource;

            MetaClass metaClass = datasource.getMetaClass();
            resolveMetaPropertyPath(metaClass, property);

            if (metaProperty.getRange().isDatatype()
                    && metaProperty.getRange().asDatatype().getJavaClass().equals(Date.class)
                    && timeZone == null) {
                MetadataTools metadataTools = AppBeans.get(MetadataTools.class);
                Boolean ignoreUserTimeZone = metadataTools.getMetaAnnotationValue(metaProperty, IgnoreUserTimeZone.class);
                if (!Boolean.TRUE.equals(ignoreUserTimeZone)) {
                    timeZone = userSession.getTimeZone();
                    dateField.setTimeZone(timeZone);
                }
            }

            itemChangeListener = e -> {
                if (updatingInstance) {
                    return;
                }
                Date value = getEntityValue(e.getItem());
                setValueToFields(value);
                fireValueChanged(value);
            };
            weakItemChangeListener = new WeakItemChangeListener(datasource, itemChangeListener);
            //noinspection unchecked
            datasource.addItemChangeListener(weakItemChangeListener);

            itemPropertyChangeListener = e -> {
                if (updatingInstance) {
                    return;
                }
                if (!isBuffered() && e.getProperty().equals(metaPropertyPath.toString())) {
                    setValueToFields((Date) e.getValue());
                    fireValueChanged(e.getValue());
                }
            };

            weakItemPropertyChangeListener = new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener);
            //noinspection unchecked
            datasource.addItemPropertyChangeListener(weakItemPropertyChangeListener);

            if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
                if (property.equals(metaPropertyPath.toString())) {
                    Date value = getEntityValue(datasource.getItem());
                    setValueToFields(value);
                    fireValueChanged(value);
                }
            }

            initRequired(metaPropertyPath);
            initDateFormat(metaProperty);

            if (metaProperty.isReadOnly()) {
                setEditable(false);
            }

            initBeanValidator();
            setDateRangeByProperty(metaProperty);
        }
    }

    protected void initDateFormat(MetaProperty metaProperty) {
        TemporalType tt = null;
        if (metaProperty.getRange().asDatatype().getJavaClass().equals(java.sql.Date.class)) {
            tt = TemporalType.DATE;
        } else if (metaProperty.getAnnotations() != null) {
            tt = (TemporalType) metaProperty.getAnnotations().get(MetadataTools.TEMPORAL_ANN_NAME);
        }

        setResolution(tt == TemporalType.DATE
                ? DateField.Resolution.DAY
                : Resolution.MIN);

        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        String formatStr = messageTools.getDefaultDateFormat(tt);
        setDateFormat(formatStr);
    }

    protected void setDateRangeByProperty(MetaProperty metaProperty) {
        if (metaProperty.getAnnotations().get(Past.class.getName()) != null) {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            Date currentTimestamp = timeSource.currentTimestamp();

            Calendar calendar = Calendar.getInstance(userSession.getLocale());
            calendar.setTime(currentTimestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

            setRangeEnd(calendar.getTime());
        } else if (metaProperty.getAnnotations().get(Future.class.getName()) != null) {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            Date currentTimestamp = timeSource.currentTimestamp();

            Calendar calendar = Calendar.getInstance(userSession.getLocale());
            calendar.setTime(currentTimestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if (metaProperty.getRange().asDatatype().getJavaClass().equals(java.sql.Date.class)) {
                calendar.add(Calendar.DATE, 1);
            }

            setRangeStart(calendar.getTime());
        }
    }

    protected Date getEntityValue(Entity item) {
        return InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
    }

    protected void fireValueChanged(Object value) {
        Object oldValue = prevValue;

        if (!Objects.equals(oldValue, value)) {
            prevValue = value;

            if (hasValidationError()) {
                setValidationError(null);
            }

            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }

    protected Date constructDate() {
        final Date datePickerDate = dateField.getValue();
        if (datePickerDate == null) {
            return null;
        }

        Calendar dateCalendar = Calendar.getInstance(userSession.getLocale());
        if (timeZone != null) {
            dateCalendar.setTimeZone(timeZone);
        }
        dateCalendar.setTime(datePickerDate);
        if (timeField.getValue() == null) {
            dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            dateCalendar.set(Calendar.MINUTE, 0);
            dateCalendar.set(Calendar.SECOND, 0);
        } else {
            Calendar timeCalendar = Calendar.getInstance(userSession.getLocale());
            timeCalendar.setTime(timeField.<Date>getValue());

            dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        }

        Date resultDate = dateCalendar.getTime();

        if (metaProperty != null) {
            Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
            if (javaClass.equals(java.sql.Date.class)) {
                return new java.sql.Date(resultDate.getTime());
            } else if (javaClass.equals(Time.class)) {
                return new Time(resultDate.getTime());
            } else {
                return resultDate;
            }
        } else {
            return resultDate;
        }
    }

    protected Date extractTime(Date date) {
        Calendar dateCalendar = Calendar.getInstance(userSession.getLocale());
        if (timeZone != null) {
            dateCalendar.setTimeZone(timeZone);
        }
        dateCalendar.setTime(date);

        Calendar timeCalendar = Calendar.getInstance(userSession.getLocale());
        timeCalendar.setTimeInMillis(0);

        timeCalendar.set(Calendar.HOUR_OF_DAY, dateCalendar.get(Calendar.HOUR_OF_DAY));
        timeCalendar.set(Calendar.MINUTE, dateCalendar.get(Calendar.MINUTE));
        timeCalendar.set(Calendar.SECOND, dateCalendar.get(Calendar.SECOND));

        return timeCalendar.getTime();
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        timeField.setEditable(editable);
        dateField.setReadOnly(!editable);

        component.setCompositionReadOnly(!editable);
    }

    @Override
    public int getTabIndex() {
        return dateField.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        dateField.setTabIndex(tabIndex);
        timeField.setTabIndex(tabIndex);
    }
}
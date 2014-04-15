/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.FormatStrings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.toolkit.ui.CubaDateField;
import com.haulmont.cuba.web.toolkit.ui.CubaDateFieldWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaMaskedTextField;
import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author abramov
 * @version $Id$
 */
public class WebDateField extends WebAbstractField<CubaDateFieldWrapper> implements DateField {

    protected Resolution resolution;

    protected boolean editable = true;

    // Due to #PL-3538, #VAADIN-13124
    protected boolean enabled = true;
    protected boolean updatingInstance;

    protected CubaDateField dateField;
    protected WebTimeField timeField;

    protected HorizontalLayout composition;

    protected String dateTimeFormat;
    protected String dateFormat;
    protected String timeFormat;

    public WebDateField() {
        composition = new HorizontalLayout();

        composition.setSpacing(true);
        dateField = new CubaDateField();

        Locale userLocale = AppBeans.get(UserSessionSource.class).getLocale();
        FormatStrings formats = Datatypes.getFormatStrings(userLocale);
        if (formats != null) {
            dateField.setDateFormat(formats.getDateFormat());
        }

        dateField.setResolution(com.vaadin.shared.ui.datefield.Resolution.DAY);
        dateField.setWidth("100%");

        dateField.setImmediate(true);
        dateField.setInvalidAllowed(true);

        timeField = new WebTimeField();

        CubaMaskedTextField vTimeField = timeField.getComponent();
        vTimeField.setImmediate(true);
        vTimeField.setInvalidAllowed(false);
        vTimeField.setInvalidCommitted(true);

        dateField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                updateInstance();
            }
        });

        timeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                updateInstance();
            }
        });
        setResolution(Resolution.MIN);

        component = new CubaDateFieldWrapper(this, composition);
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

    public void updateLayout() {
        composition.removeAllComponents();
        composition.addComponent(dateField);
        composition.setExpandRatio(dateField, 1.0f);
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            composition.setSpacing(true);
            composition.addComponent(timeField.<com.vaadin.ui.Component>getComponent());
        } else {
            composition.setSpacing(false);
        }
    }

    @Override
    protected void attachListener(CubaDateFieldWrapper component) {
        // do nothing
    }

    @Override
    public void setDateFormat(String dateFormat) {
        dateTimeFormat = dateFormat;
        StringBuilder date = new StringBuilder(dateFormat);
        StringBuilder time = new StringBuilder(dateFormat);
        int timeStartPos = dateFormat.indexOf('h');
        if (timeStartPos < 0) {
            timeStartPos = dateFormat.indexOf('H');
        }
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

    protected void __setResolution(Resolution resolution) {
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            timeField.setResolution(resolution);
        } else {
            dateField.setResolution(WebComponentsHelper.convertDateFieldResolution(resolution));
        }
    }

    @Override
    public <T> T getValue() {
        return (T) constructDate();
    }

    @Override
    public void setValue(Object value) {
        if (!editable) {
            return;
        }

        updatingInstance = true;
        try {
            dateField.setValue((Date) value);
            timeField.setValue(value);
        } finally {
            updatingInstance = false;
        }

        updateInstance();
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

    protected void setValueFromDs(Object value) {
        boolean isEditable = editable;
        if (!editable) {
            setEditable(true);
        }
        updatingInstance = true;
        try {
            dateField.setValue((Date) value);
            timeField.setValue(value);
        } finally {
            updatingInstance = false;
        }
        setEditable(isEditable);
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
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        dateField.setEnabled(enabled);
        timeField.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setValueChangingListener(ValueChangingListener listener) {
    }

    @Override
    public void removeValueChangingListener() {
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance) {
                            return;
                        }
                        Date value = getEntityValue(item);
                        setValueFromDs(value);
                        fireValueChanged(value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance) {
                            return;
                        }
                        if (property.equals(metaPropertyPath.toString())) {
                            setValueFromDs(value);
                            fireValueChanged(value);
                        }
                    }
                }
        );

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                setValueFromDs(value);
                fireValueChanged(value);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }
    }

    protected Date getEntityValue(Entity item) {
        return InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
    }

    protected void fireValueChanged(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            Object oldValue = prevValue;

            prevValue = value;

            for (ValueListener listener : listeners) {
                listener.valueChanged(this, "value", oldValue, value);
            }
        }
    }

    protected Date constructDate() {
        final Date datePickerDate = dateField.getValue();
        if (datePickerDate == null) {
            return null;
        }

        Locale locale = AppBeans.get(UserSessionSource.class).getLocale();

        Calendar c = Calendar.getInstance(locale);
        c.setTime(datePickerDate);
        if (timeField.getValue() == null) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
        } else {
            Calendar c2 = Calendar.getInstance(locale);
            c2.setTime(timeField.<Date>getValue());

            c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
            c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
            c.set(Calendar.SECOND, c2.get(Calendar.SECOND));
        }

        if (metaProperty != null) {
            Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
            if (javaClass.equals(java.sql.Date.class)) {
                return new java.sql.Date(c.getTimeInMillis());
            } else if (javaClass.equals(Time.class)) {
                return new Time(c.getTimeInMillis());
            } else {
                return c.getTime();
            }
        } else {
            return c.getTime();
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
        timeField.setEditable(editable);
        dateField.setReadOnly(!editable);

        component.setCompositionReadOnly(!editable);
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisible() || !isEditable() || !isEnabled())
            return;

        if (isRequired() && dateField.getValue() == null) {
            throw new RequiredValueMissingException(component.getRequiredError(), this);
        }

        for (Field.Validator validator : validators) {
            validator.validate(getValue());
        }
    }
}
/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigBoxLayoutAdapter;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopDateField
    extends DesktopAbstractField<JPanel>
    implements DateField
{
    private Resolution resolution;
    private Datasource datasource;
    private MetaPropertyPath metaPropertyPath;
    private MetaProperty metaProperty;

    private boolean updatingInstance;
    private boolean required;
    private String requiredMessage;

    private JXDatePicker datePicker;
    private JTextField hoursField;
    private JTextField minutesField;
    private BoxLayoutAdapter adapter;
    private DocumentListener minListener;
    private DocumentListener hourListener;
    private boolean valid = true;
    private String caption;

    private Object prevValue = null;

    public DesktopDateField() {
        impl = new JPanel();
        initComponentParts();
        setResolution(Resolution.MIN);
    }

    private void initComponentParts() {
        adapter = new MigBoxLayoutAdapter(impl);
        impl.setLayout(adapter.getLayout());
        adapter.setSpacing(false);
        adapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
        adapter.setExpandLayout(true);

        datePicker = new JXDatePicker();
        String dateFormat = Datatypes.getFormatStrings(UserSessionProvider.getLocale()).getDateFormat();
        datePicker.setFormats(dateFormat);

        hoursField = new JTextField();
        hoursField.setColumns(2);

        minutesField = new JTextField();
        minutesField.setColumns(2);

        hourListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (isHourUsed()) {
                    updateInstance();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (isHourUsed()) {
                    updateInstance();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (isHourUsed()) {
                    updateInstance();
                }
            }
        };

        minListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (isMinUsed()) {
                    updateInstance();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (isMinUsed()) {
                    updateInstance();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (isMinUsed()) {
                    updateInstance();
                }
            }
        };
    }

    private void updateLayout() {
        impl.removeAll();

        impl.add(datePicker, "growx, w 100%");

        if (isHourUsed()) {
            impl.add(hoursField, "w 30px!");
        }

        if (isMinUsed()) {
            impl.add(new JLabel(":"), "aligny 50%");
            impl.add(minutesField, "w 30px!");
        }
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
        updateLayout();
    }

    @Override
    public String getDateFormat() {
        return null;
    }

    @Override
    public void setDateFormat(String dateFormat) {
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void setRequiredMessage(String msg) {
        requiredMessage = msg;
    }

    @Override
    public <T> T getValue() {
        try {
            return (T) constructDate();
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        updatePartsFromValue((Date) value);
    }

    @Override
    public void addValidator(Validator validator) {
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void validate() throws ValidationException {
        try {
            constructDate();
            super.validate();
        }
        catch (Exception e) {
            throw new ValidationException(e);
        }
    }

    @Override
    public Datasource getDatasource() {
        return null;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        metaProperty = metaPropertyPath.getMetaProperty();

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;
                        Date value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        updatePartsFromValue(value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            updatePartsFromValue((Date) value);
                        }
                    }
                }
        );

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
                updatePartsFromValue(value);
            }
        }

        datePicker.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("date".equals(evt.getPropertyName())) {
                            updateInstance();
                        }
                    }
                }
        );

        hoursField.getDocument().addDocumentListener(hourListener);
        minutesField.getDocument().addDocumentListener(minListener);

        setRequired(metaProperty.isMandatory());
    }

    private void updatePartsFromValue(Date value) {
        updatingInstance = true;
        try {
            setDateParts(value);
            valid = true;
        } finally {
            updatingInstance = false;
        }

        Object newValue = getValue();
        if (!ObjectUtils.equals(prevValue, newValue))
            fireValueChanged(prevValue, newValue);
        prevValue = newValue;
    }

    private void setDateParts(Date value) {
        datePicker.setDate(value);

        if (value != null) {
            Calendar calendar = Calendar.getInstance(UserSessionProvider.getLocale());
            calendar.setTime(value);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);

            hoursField.setText(String.format("%02d", hour));
            minutesField.setText(String.format("%02d", min));
        }
        else {
            hoursField.setText("");
            minutesField.setText("");
        }
    }

    @Override
    public boolean isEditable() {
        return datePicker.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        datePicker.setEditable(editable);
        hoursField.setEditable(editable);
        minutesField.setEditable(editable);
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    private void updateInstance() {
        if (updatingInstance)
            return;

        updatingInstance = true;
        try {
            if (datasource != null && metaPropertyPath != null) {
                Date value = constructDate();
                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                }
            }
            valid = true;
        }
        catch (RuntimeException e) {
            valid = false;
        }
        finally {
            updatingInstance = false;
        }
        if (valid) {
            Object newValue = getValue();
            if (!ObjectUtils.equals(prevValue, newValue))
                fireValueChanged(prevValue, newValue);
            prevValue = newValue;
        }
    }

    private Date constructDate() {
        final Date datePickerDate = datePicker.getDate();
        if (datePickerDate == null) {
            return null;
        }
        Calendar c = Calendar.getInstance(UserSessionProvider.getLocale());
        c.setTime(datePickerDate);

        if (isHourUsed()) {
            int hours = Integer.valueOf(hoursField.getText());
            if (hours < 0 || hours > 23) {
                throw new NumberFormatException("Invalid hours: " + hours);
            }
            c.set(Calendar.HOUR_OF_DAY, hours);
        }

        if (isMinUsed()) {
            int min = Integer.valueOf(minutesField.getText());
            if (min < 0 || min > 59) {
                throw new NumberFormatException("Invalid minutes: " + min);
            }
            c.set(Calendar.MINUTE, min);
        }

        Date time = c.getTime();
        return time;
    }

    private boolean isHourUsed() {
        return resolution != null && resolution.ordinal() <= Resolution.HOUR.ordinal();
    }

    private boolean isMinUsed() {
        return resolution != null && resolution.ordinal() <= Resolution.MIN.ordinal();
    }
}

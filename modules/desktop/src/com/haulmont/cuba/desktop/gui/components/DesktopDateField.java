/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigBoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.DatePicker.DatePicker;
import com.haulmont.cuba.desktop.sys.vcl.TimeField.TimeField;
import com.haulmont.cuba.desktop.sys.vcl.TimeField.TimeFieldDocument;
import com.haulmont.cuba.gui.components.DateField;
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
import java.text.ParseException;
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
    private static final String HOUR_FORMAT="00";
    private static final String HOUR_MINUTE_FORMAT="00:00";

    private Resolution resolution;
    private Datasource datasource;
    private MetaPropertyPath metaPropertyPath;
    private MetaProperty metaProperty;

    private boolean updatingInstance;
    private boolean required;
    private String requiredMessage;

    private JXDatePicker datePicker;
    private TimeField timeField;
    private DocumentListener timeListener;
    private boolean valid = true;
    private String caption;

    private Object prevValue = null;

    public DesktopDateField() {
        impl = new JPanel();
        initComponentParts();
        setResolution(Resolution.MIN);

        DesktopComponentsHelper.adjustDateFieldSize(impl);
    }

    private void initComponentParts() {
        BoxLayoutAdapter adapter = new MigBoxLayoutAdapter(impl);
        adapter.setSpacing(false);
        adapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
        adapter.setExpandLayout(true);
        impl.setLayout(adapter.getLayout());

        datePicker = new DatePicker();

        timeField = new TimeField(HOUR_MINUTE_FORMAT);

        timeListener = new DocumentListener() {
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

    }

    private void updateLayout() {
        impl.removeAll();

        impl.add(datePicker, "growx, w 100%");
        if (isHourUsed() && !isMinUsed()) {
            timeField.setDocument(new TimeFieldDocument(timeField, HOUR_FORMAT));
            impl.add(timeField, "w 23px!");
        } else if (isHourUsed() && isMinUsed()) {
            timeField.setDocument(new TimeFieldDocument(timeField, HOUR_MINUTE_FORMAT));
            impl.add(timeField, "w 45px!");
        }
        timeField.getDocument().addDocumentListener(timeListener);
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
        if (!ObjectUtils.equals(prevValue, value)) {
            updatePartsFromValue((Date) value);
        }
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
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
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

            timeField.setText(String.format("%02d", hour)+String.format("%02d", min));
        }
        else {
            timeField.setText("");
        }
    }

    @Override
    public boolean isEditable() {
        return datePicker.isEditable();
    }

    public boolean isEnabled() {
        return datePicker.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        datePicker.setEnabled(enabled);
        timeField.setEnabled(enabled);
    }

    @Override
    public void setEditable(boolean editable) {
        datePicker.setEditable(editable);
        timeField.setEditable(editable);
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
                    Object obj = value;
                    Datatype<Object> datatype = metaProperty.getRange().asDatatype();
                    if (!datatype.getJavaClass().equals(Date.class)) {
                        String str = Datatypes.get(Date.class).format(value);
                        try {
                            obj = datatype.parse(str);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), obj);
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
            int hours = timeField.getHours();
            c.set(Calendar.HOUR_OF_DAY, hours);
        }

        if (isMinUsed()) {
            int min = timeField.getMinutes();
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

    public JXDatePicker getDatePicker() {
        return datePicker;
    }

    public TimeField getTimeField() {
        return timeField;
    }
}

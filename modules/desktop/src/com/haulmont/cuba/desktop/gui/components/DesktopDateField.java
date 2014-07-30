/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigBoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.DatePicker.DatePicker;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.desktop.sys.vcl.FocusableComponent;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopDateField extends DesktopAbstractField<JPanel> implements DateField {

    protected Resolution resolution;
    protected Datasource datasource;
    protected MetaPropertyPath metaPropertyPath;
    protected MetaProperty metaProperty;

    protected String dateTimeFormat;
    protected String dateFormat;
    protected String timeFormat;

    protected boolean updatingInstance;

    protected JXDatePicker datePicker;
    protected DesktopTimeField timeField;
    protected boolean valid = true;
    protected String caption;

    protected Object prevValue = null;
    protected boolean editable = true;

    public DesktopDateField() {
        impl = new FocusableComposition();

        initComponentParts();
        setResolution(Resolution.MIN);

        Locale locale = AppBeans.get(UserSessionSource.class).getLocale();
        setDateFormat(Datatypes.getFormatStringsNN(locale).getDateTimeFormat());
        DesktopComponentsHelper.adjustDateFieldSize(impl);
    }

    protected void initComponentParts() {
        BoxLayoutAdapter adapter = new MigBoxLayoutAdapter(impl);
        adapter.setSpacing(false);
        adapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
        adapter.setExpandLayout(true);
        impl.setLayout(adapter.getLayout());

        datePicker = new FlushableDatePicker();

        Dimension size = new Dimension(100, DesktopComponentsHelper.FIELD_HEIGHT);
        datePicker.setPreferredSize(size);
        datePicker.setMinimumSize(size);

        timeField = new DesktopTimeField();

        timeField.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                updateInstance();
            }
        });

        datePicker.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("date".equals(evt.getPropertyName())) {
                            updateInstance();
                            updateMissingValueState();
                        }
                    }
                }
        );
    }

    protected void updateLayout() {
        impl.removeAll();
        impl.add(datePicker, "growx, w 100%");
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            impl.add(timeField.getImpl());
        }
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        _setResolution(resolution);
        updateLayout();
    }

    protected void _setResolution(Resolution resolution) {
        this.resolution = resolution;
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            timeField.setResolution(resolution);
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
        int timeStartPos = dateFormat.indexOf('h');
        if (timeStartPos < 0) {
            timeStartPos = dateFormat.indexOf('H');
        }
        if (timeStartPos >= 0) {
            time.delete(0, timeStartPos);
            timeFormat = StringUtils.trimToEmpty(time.toString());
            timeField.setFormat(timeFormat);
            _setResolution(resolution);
            date.delete(timeStartPos, dateFormat.length());
        } else if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            _setResolution(Resolution.DAY);
        }

        this.dateFormat = StringUtils.trimToEmpty(date.toString());
        datePicker.setFormats(this.dateFormat);

        updateLayout();
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void requestFocus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                datePicker.requestFocus();
            }
        });
    }

    @Override
    public <T> T getValue() {
        try {
            return (T) constructDate();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        if (!isEditable()) {
            log.debug("Set value for non editable field ignored");
            return;
        }

        if (!ObjectUtils.equals(prevValue, value)) {
            updateComponent((Date) value);
            fireChangeListeners(value);
        }
    }

    @Override
    public void validate() throws ValidationException {
        try {
            constructDate();
            super.validate();
        } catch (RequiredValueMissingException e) {
            throw e;
        } catch (Exception e) {
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
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
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

        checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        metaProperty = metaPropertyPath.getMetaProperty();

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance) {
                            return;
                        }
                        Date value = getEntityValue(item);
                        updateComponent(value);
                        fireChangeListeners(value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance) {
                            return;
                        }
                        if (property.equals(metaPropertyPath.toString())) {
                            updateComponent((Date) value);
                            fireChangeListeners(value);
                        }
                    }
                }
        );

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                updateComponent(value);
                fireChangeListeners(value);
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

    protected void updateComponent(Date value) {
        updatingInstance = true;
        try {
            setDateParts(value);
            valid = true;
        } finally {
            updatingInstance = false;
        }
        updateMissingValueState();
    }

    protected void fireChangeListeners(Object newValue) {
        if (!ObjectUtils.equals(prevValue, newValue)) {
            Object oldValue = prevValue;

            prevValue = newValue;

            fireValueChanged(oldValue, newValue);
        }
    }

    protected void setDateParts(Date value) {
        datePicker.setDate(value);
        timeField.setValueInternal(value);
    }

    @Override
    public boolean isEditable() {
        return datePicker.isEditable();
    }

    @Override
    public boolean isEnabled() {
        return datePicker.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        datePicker.setEnabled(enabled);
        timeField.setEnabled(enabled);
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        datePicker.setEditable(editable);
        timeField.setEditable(editable);
        updateMissingValueState();
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
        return datePicker.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        datePicker.getEditor().setToolTipText(description);
        timeField.setDescription(description);
        DesktopToolTipManager.getInstance().registerTooltip(datePicker.getEditor());
    }

    protected void updateInstance() {
        if (updatingInstance) {
            return;
        }

        updatingInstance = true;
        try {
            if (datasource != null && metaPropertyPath != null) {
                Date value = constructDate();

                if (ObjectUtils.equals(prevValue, value)) {
                    valid = true;
                    return;
                }

                if (datasource.getItem() != null) {
                    Object obj = value;
                    Datatype<Object> datatype = metaProperty.getRange().asDatatype();
                    if (!datatype.getJavaClass().equals(Date.class)) {
                        String str = Datatypes.getNN(Date.class).format(value);
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
        } catch (RuntimeException e) {
            valid = false;
        } finally {
            updatingInstance = false;
        }
        if (valid) {
            Object newValue = getValue();
            fireChangeListeners(newValue);
        }
    }

    protected Date constructDate() {
        final Date datePickerDate = datePicker.getDate();
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

        //noinspection UnnecessaryLocalVariable
        Date time = c.getTime();
        return time;
    }

    protected boolean isHourUsed() {
        return resolution != null && resolution.ordinal() <= Resolution.HOUR.ordinal();
    }

    protected boolean isMinUsed() {
        return resolution != null && resolution.ordinal() <= Resolution.MIN.ordinal();
    }

    public JXDatePicker getDatePicker() {
        return datePicker;
    }

    public DesktopTimeField getTimeField() {
        return timeField;
    }

    @Override
    public void updateMissingValueState() {
        boolean value = required && editable && datePicker.getEditor().getValue() == null;
        decorateMissingValue(datePicker.getEditor(), value);
        if (isHourUsed()) {
            decorateMissingValue(timeField.getImpl(), value);
            timeField.getImpl().repaint();
        }
    }

    protected void flush() {
        if (isEditable() && isEnabled()) {
            try {
                datePicker.getEditor().commitEdit();
            } catch (ParseException e) {
                return;
            }

            updateInstance();
            updateMissingValueState();
        }
    }

    public class FocusableComposition extends JPanel implements FocusableComponent, Flushable {

        @Override
        public void focus() {
            DesktopDateField.this.requestFocus();
        }

        @Override
        public void flushValue() {
            flush();
        }
    }

    public class FlushableDatePicker extends DatePicker implements Flushable {

        @Override
        public void flushValue() {
            flush();
        }
    }
}
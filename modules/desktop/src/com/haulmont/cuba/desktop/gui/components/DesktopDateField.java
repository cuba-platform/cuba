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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
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
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class DesktopDateField extends DesktopAbstractField<JPanel> implements DateField {
    protected Resolution resolution;
    protected Datasource datasource;
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

    protected TimeZone timeZone;
    protected UserSession userSession;
    protected TimeZones timeZones = AppBeans.get(TimeZones.NAME);

    public DesktopDateField() {
        impl = new FocusableComposition();

        initComponentParts();
        setResolution(Resolution.MIN);

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        userSession = sessionSource.getUserSession();
        Locale locale = userSession.getLocale();
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

        Dimension size = new Dimension(110, DesktopComponentsHelper.FIELD_HEIGHT);
        datePicker.setPreferredSize(size);
        datePicker.setMinimumSize(size);

        timeField = new DesktopTimeField();
        timeField.addValueChangeListener(e -> {
            updateInstance();
        });

        datePicker.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                updateInstance();
                updateMissingValueState();
            }
        });
    }

    protected void updateLayout() {
        impl.removeAll();
        impl.add(datePicker, "growx, w 100%");
        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            impl.add(timeField.getImpl());
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && App.getInstance().isTestMode()) {
            timeField.setId("timepart");
            datePicker.setName("datepart");
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
        int timeStartPos = findTimeStartPos(dateFormat);
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
            updateComponent(newValue);
        }
    }

    @Override
    public void requestFocus() {
        SwingUtilities.invokeLater(() -> {
            datePicker.requestFocus();
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
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (!ObjectUtils.equals(prevValue, value)) {
            Date targetDate = (Date) value;

            updateInstance(targetDate);
            updateComponent(toUserDate((Date) value));
            fireChangeListeners(value);
        }
    }

    protected Date toUserDate(Date date) {
        return timeZone == null ? date : timeZones.convert(date, TimeZone.getDefault(), timeZone);
    }

    protected Date toServerDate(Date date) {
        return timeZone == null ? date : timeZones.convert(date, timeZone, TimeZone.getDefault());
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

        resolveMetaPropertyPath(datasource.getMetaClass(), property);
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
            updateComponent(toUserDate(value));
            fireChangeListeners(value);
        });

        //noinspection unchecked
        datasource.addItemPropertyChangeListener(e -> {
            if (updatingInstance) {
                return;
            }
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                updateComponent(toUserDate((Date) e.getValue()));
                fireChangeListeners(e.getValue());
            }
        });

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                updateComponent(toUserDate(value));
                fireChangeListeners(value);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(datasource.getMetaClass(), property));
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
    public void updateEnabled() {
        super.updateEnabled();

        datePicker.setEnabled(isEnabledWithParent());
        timeField.setEnabled(isEnabledWithParent());
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
        if (!ObjectUtils.equals(this.caption, caption)) {
            this.caption = caption;

            requestContainerUpdate();
        }
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

                setValueToDs(value);
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

    protected void updateInstance(Date value) {
        if (updatingInstance) {
            return;
        }

        updatingInstance = true;
        try {
            if (datasource != null && metaPropertyPath != null) {
                setValueToDs(value);
            }
            valid = true;
        } catch (RuntimeException e) {
            valid = false;
        } finally {
            updatingInstance = false;
        }
    }

    protected void setValueToDs(Date value) {
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

    protected Date constructDate() {
        final Date datePickerDate = datePicker.getDate();
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

        //noinspection UnnecessaryLocalVariable
        Date serverDate = toServerDate(c.getTime());
        return serverDate;
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
            DesktopDateField.this.datePicker.requestFocus();
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
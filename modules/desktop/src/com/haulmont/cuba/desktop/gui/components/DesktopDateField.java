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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.layout.MigBoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.DatePicker.DatePicker;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.desktop.sys.vcl.FocusableComponent;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Frame.NotificationType;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import javax.persistence.TemporalType;
import javax.swing.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import java.awt.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class DesktopDateField extends DesktopAbstractField<JPanel> implements DateField {
    protected Messages messages;
    protected Resolution resolution;
    protected Datasource datasource;
    protected String dateTimeFormat;
    protected String dateFormat;
    protected String timeFormat;

    protected boolean updatingInstance;

    protected JXDatePicker datePicker;
    protected DesktopTimeField timeField;
    protected boolean valid = true;

    protected Object prevValue = null;

    protected TimeZone timeZone;
    protected UserSession userSession;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;
    protected Date startDate;
    protected Date endDate;

    protected boolean updateTimeFieldResolution = false;

    public DesktopDateField() {
        impl = new FocusableComposition();

        messages = AppBeans.get(Messages.NAME);
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

        Dimension size = getDefaultDimension();
        datePicker.setPreferredSize(size);
        datePicker.setMinimumSize(size);

        timeField = new DesktopTimeField();
        timeField.addValueChangeListener(e -> {
            if (!checkRange(constructDate())) {
                return;
            }

            if (!updateTimeFieldResolution) {
                updateInstance();
            }
        });

        datePicker.addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                if (!checkRange(constructDate())) {
                    return;
                }

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
            // while changing resolution, timeField loses its value, so we need to set it again
            updateTimeFieldResolution = true;
            Date value = datePicker.getDate();
            if (value == null) {
                timeField.setValue(null);
            } else {
                timeField.setValue(extractTime(value));
            }
            updateTimeFieldResolution = false;
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
        if (prevTimeZone == null && timeZone == null) {
            return;
        }
        Date value = getValue();
        this.timeZone = timeZone;
        datePicker.setTimeZone(timeZone);
        if (value != null && !Objects.equals(prevTimeZone, timeZone)) {
            updateComponent(value);
        }
    }

    @Override
    public void setRangeStart(Date value) {
        startDate = value;
    }

    @Override
    public Date getRangeStart() {
        return startDate;
    }

    @Override
    public void setRangeEnd(Date value) {
        endDate = value;
    }

    @Override
    public Date getRangeEnd() {
        return endDate;
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
            getFrame().showNotification(messages.getMainMessage("dateField.dateOutOfRangeMessage"),
                    NotificationType.TRAY);
        }

        updatingInstance = true;
        try {
            datePicker.setDate((Date) prevValue);
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
    public void requestFocus() {
        SwingUtilities.invokeLater(() -> {
            datePicker.requestFocus();
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Date getValue() {
        try {
            return constructDate();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (!Objects.equals(prevValue, value)) {
            Date targetDate = (Date) value;

            updateInstance(targetDate);
            updateComponent((Date) value);
            fireChangeListeners(value);
        }
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisibleRecursive() || !isEditableWithParent() || !isEnabledRecursive())
            return;

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
                && metaProperty.getRange().asDatatype().getJavaClass().equals(Date.class)
                && timeZone == null) {
            MetadataTools metadataTools = AppBeans.get(MetadataTools.class);
            Boolean ignoreUserTimeZone = metadataTools.getMetaAnnotationValue(metaProperty, IgnoreUserTimeZone.class);
            if (!Boolean.TRUE.equals(ignoreUserTimeZone)) {
                timeZone = userSession.getTimeZone();
            }
        }

        itemChangeListener = e -> {
            if (updatingInstance) {
                return;
            }
            Date value = getEntityValue(e.getItem());
            updateComponent(value);
            fireChangeListeners(value);
        };
        //noinspection unchecked
        datasource.addItemChangeListener(new WeakItemChangeListener(datasource, itemChangeListener));

        itemPropertyChangeListener = e -> {
            if (updatingInstance) {
                return;
            }
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                updateComponent((Date) e.getValue());
                fireChangeListeners(e.getValue());
            }
        };
        //noinspection unchecked
        datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener));

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                updateComponent(value);
                fireChangeListeners(value);
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
        if (!Objects.equals(prevValue, newValue)) {
            Object oldValue = prevValue;

            prevValue = newValue;

            fireValueChanged(oldValue, newValue);
        }
    }

    protected void setDateParts(Date value) {
        datePicker.setDate(value);
        if (value == null) {
            timeField.setValueInternal(null);
        } else {
            timeField.setValueInternal(extractTime(value));
        }
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        datePicker.setEnabled(isEnabledWithParent());
        timeField.setEnabled(isEnabledWithParent());
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        datePicker.setEditable(editable);
        timeField.setEditable(editable);
        updateMissingValueState();
    }

    @Override
    protected void setCaptionToComponent(String caption) {
        super.setCaptionToComponent(caption);

        requestContainerUpdate();
    }

    @Override
    public String getDescription() {
        return datePicker.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        if (!Objects.equals(this.getDescription(), description)) {
            datePicker.getEditor().setToolTipText(description);
            timeField.setDescription(description);
            DesktopToolTipManager.getInstance().registerTooltip(datePicker.getEditor());

            requestContainerUpdate();
        }
    }

    protected void updateInstance() {
        if (updatingInstance) {
            return;
        }

        updatingInstance = true;
        try {
            if (datasource != null && metaPropertyPath != null) {
                Date value = constructDate();
                if (Objects.equals(prevValue, value)) {
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

        //noinspection UnnecessaryLocalVariable
        return dateCalendar.getTime();
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
        boolean value = required && isEditableWithParent() && datePicker.getEditor().getValue() == null;
        decorateMissingValue(datePicker.getEditor(), value);
        if (isHourUsed()) {
            decorateMissingValue(timeField.getImpl(), value);
            timeField.getImpl().repaint();
        }
    }

    protected void flush() {
        if (isEditable() && isEnabledRecursive()) {
            try {
                datePicker.getEditor().commitEdit();
            } catch (ParseException e) {
                return;
            }

            updateInstance();
            updateMissingValueState();
        }
    }

    protected Dimension getDefaultDimension() {
        UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
        if (lafDefaults.getDimension("DateField.dimension") != null) { // take it from desktop theme
            return lafDefaults.getDimension("DateField.dimension");
        }
        return new Dimension(110, DesktopComponentsHelper.FIELD_HEIGHT);
    }

    @Override
    public void commit() {
        // do nothing
    }

    @Override
    public void discard() {
        // do nothing
    }

    @Override
    public boolean isBuffered() {
        // do nothing
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // do nothing
    }

    @Override
    public boolean isModified() {
        // do nothing
        return false;
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
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTimeField extends DesktopAbstractField<JFormattedTextField> implements TimeField {

    private boolean showSeconds;
    private String timeFormat;
    private String mask;
    private MaskFormatter formatter;
    private boolean updatingInstance;
    private Datasource datasource;
    private Object prevValue;
    private String caption;
    private DateField.Resolution resolution;
    private boolean editable = true;

    protected static final int DIGIT_WIDTH = 23;

    public DesktopTimeField() {
        UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);

        timeFormat = Datatypes.getFormatStringsNN(uss.getLocale()).getTimeFormat();
        resolution = DateField.Resolution.MIN;
        formatter = new MaskFormatter();
        formatter.setPlaceholderCharacter('_');
        impl = new FlushableFormattedTextField(formatter);
        FieldListener listener = new FieldListener();
        impl.addFocusListener(listener);
        impl.addKeyListener(listener);

        setShowSeconds(timeFormat.contains("ss"));
    }

    @Override
    public boolean getShowSeconds() {
        return showSeconds;
    }

    private Object validateRawValue(String value) throws NumberFormatException, ParseException {
        if (value.equals(StringUtils.replaceChars(mask, "#U", "__"))) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        sdf.setLenient(false);
        try {
            Date parsedValue = sdf.parse(value);

            Class targetType = null;
            if (datasource != null && metaPropertyPath != null) {
                targetType = metaPropertyPath.getRangeJavaClass();
            }

            if (targetType == java.sql.Time.class) {
                return new Time(parsedValue.getTime());
            }
            if (targetType == java.sql.Date.class) {
                log.warn("Do not use java.sql.Date with time field");
                return new java.sql.Date(parsedValue.getTime());
            }

            return parsedValue;
        } catch (ParseException e) {
            showValidationMessage();
            return prevValue;
        }
    }

    private void showValidationMessage() {
        Messages messages = AppBeans.get(Messages.NAME);
        DesktopComponentsHelper.getTopLevelFrame(this).showNotification(
                messages.getMessage(AppConfig.getMessagesPack(), "validationFail"),
                com.haulmont.cuba.gui.components.Frame.NotificationType.TRAY
        );
    }

    public void setResolution(DateField.Resolution resolution) {
        this.resolution = resolution;
        if (resolution.ordinal() <= DateField.Resolution.SEC.ordinal()) {
            setShowSeconds(true);
        } else if (resolution.ordinal() <= DateField.Resolution.MIN.ordinal()) {
            setShowSeconds(false);
        } else if (resolution.ordinal() <= DateField.Resolution.HOUR.ordinal()) {
            StringBuilder builder = new StringBuilder(timeFormat);
            if (timeFormat.contains("mm")) {
                int minutesIndex = builder.indexOf("mm");
                builder.delete(minutesIndex > 0 ? --minutesIndex : minutesIndex, minutesIndex + 3);
                timeFormat = builder.toString();
            }
            setShowSeconds(false);
        }
    }

    public DateField.Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
        if (showSeconds) {
            if (!timeFormat.contains("ss")) {
                int minutesIndex = timeFormat.indexOf("mm");
                StringBuilder builder = new StringBuilder(timeFormat);
                builder.insert(minutesIndex + 2, ":ss");
                timeFormat = builder.toString();
            }
        } else {
            if (timeFormat.contains("ss")) {
                int secondsIndex = timeFormat.indexOf("ss");
                StringBuilder builder = new StringBuilder(timeFormat);
                builder.delete(secondsIndex > 0 ? --secondsIndex : secondsIndex, secondsIndex + 3);
                timeFormat = builder.toString();
            }
        }
        updateTimeFormat();
        updateWidth();
    }

    private void updateTimeFormat() {
        mask = StringUtils.replaceChars(timeFormat, "Hhmsa", "####U");
        try {
            formatter.setMask(mask);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        impl.setValue(impl.getValue());
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

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;
                        Date value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        updateComponent(value);
                        fireChangeListeners(value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            updateComponent(value);
                            fireChangeListeners(value);
                        }
                    }
                }
        );

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
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

    @Override
    public void setFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        showSeconds = timeFormat.contains("ss");
        updateTimeFormat();
        updateWidth();
    }

    @Override
    public String getFormat() {
        return timeFormat;
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
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    public <T> T getValue() {
        try {
            return (T) new SimpleDateFormat(timeFormat).parse(impl.getText());
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void setValue(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent(value);
            fireChangeListeners(value);
        }
    }

    protected void setValueInternal(Object value) {
        boolean editable = isEditable();
        setEditable(true);

        setValue(value);

        setEditable(editable);
    }

    protected void updateComponent(Object value) {
        updatingInstance = true;
        if (value == null) {
            impl.setValue("");
            updatingInstance = false;
        } else {
            try {
                if (value instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                    impl.setValue(sdf.format(value));
                }
            } finally {
                updatingInstance = false;
            }
        }
        updateMissingValueState();
    }

    protected void updateInstance(Object value) {
        if (updatingInstance)
            return;

        if (ObjectUtils.equals(prevValue, value))
            return;

        updatingInstance = true;
        try {
            if (datasource != null && metaProperty != null && datasource.getItem() != null)
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }

    protected void updateWidth() {
        int width = isAmPmUsed() ? DIGIT_WIDTH : 0;
        if (showSeconds) {
            width = width + DIGIT_WIDTH;
        }
        int height = impl.getPreferredSize().height;

        switch (resolution) {
            case HOUR:
                impl.setMinimumSize(new Dimension(DIGIT_WIDTH + width, height));
                break;
            case MIN:
            case SEC:
                impl.setMinimumSize(new Dimension(DIGIT_WIDTH * 2 + width, height));
        }
    }

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    @Override
    public boolean isEditable() {
        return impl.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        impl.setEditable(editable);
        updateMissingValueState();
    }

    @Override
    public void updateMissingValueState() {
        Object implValue = impl.getValue();
        boolean value = required && editable
                && (implValue == null || implValue instanceof String && StringUtils.isBlank((String) implValue));
        decorateMissingValue(impl, value);
    }

    public boolean isAmPmUsed() {
        return timeFormat.contains("a");
    }

    protected class FieldListener implements FocusListener, KeyListener {
        private static final int ENTER_CODE = 10;

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            fireEvent();
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (ENTER_CODE == e.getKeyCode())
                fireEvent();
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        private void fireEvent() {
            flush();
        }
    }

    protected void flush() {
        if (isEditable() && isEnabled()) {
            Object newValue;
            try {
                newValue = validateRawValue(getImpl().getText());
            } catch (Exception e) {
                showValidationMessage();
                newValue = prevValue;
            }
            if ("".equals(newValue))
                newValue = null;

            if (!ObjectUtils.equals(prevValue, newValue))
                setValue(newValue);
            else
                updateComponent(newValue);
        }
    }

    protected class FlushableFormattedTextField extends JFormattedTextField implements Flushable {

        public FlushableFormattedTextField(MaskFormatter formatter) {
            super(formatter);
        }

        @Override
        public void flushValue() {
            flush();
        }
    }
}
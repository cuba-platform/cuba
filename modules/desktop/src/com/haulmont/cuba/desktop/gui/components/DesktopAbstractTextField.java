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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.gui.executors.impl.DesktopBackgroundWorker;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.validation.ValidationAlertHolder;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Locale;

public abstract class DesktopAbstractTextField<T extends JTextComponent> extends DesktopAbstractField<T> {

    protected Document doc;

    protected Datatype datatype;

    protected boolean updatingInstance;
    protected Object prevValue;

    protected Datasource datasource;

    protected boolean editable = true;

    protected int maxLength;
    protected boolean trimming = true;

    protected String caption;

    protected Locale locale = AppBeans.<UserSessionSource>get(UserSessionSource.NAME).getLocale();
    protected DefaultValueFormatter valueFormatter;

    protected DesktopAbstractTextField() {
        doc = createDocument();

        impl = createTextComponentImpl();

        impl.setEnabled(enabled);
        impl.setEditable(editable);
        impl.setDocument(doc);
        impl.setVisible(isVisible());

        TextFieldListener listener = createTextListener();
        impl.addKeyListener(listener);
        impl.addFocusListener(listener);
        impl.putClientProperty(getSwingPropertyId(), getId());

        updateMissingValueState();

        valueFormatter = new DefaultValueFormatter(locale);
    }

    protected Document createDocument() {
        return new TextComponentDocument();
    }

    protected TextFieldListener createTextListener() {
        return new TextFieldListener();
    }

    protected abstract T createTextComponentImpl();

    @Override
    public void updateMissingValueState() {
        boolean state = required && editable && StringUtils.isBlank(impl.getText());
        decorateMissingValue(impl, state);
        if (getComposition() instanceof JScrollPane) {
            decorateMissingValue(getComposition(), state);
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if (impl != null) {
            impl.setEditable(editable);
            updateMissingValueState();
        }
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        if (impl != null) {
            impl.setEnabled(isEnabledWithParent());
        }
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
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        if (!ObjectUtils.equals(this.getDescription(), description)) {
            impl.setToolTipText(description);
            DesktopToolTipManager.getInstance().registerTooltip(impl);

            requestContainerUpdate();
        }
    }

    @Override
    public <V> V getValue() {
        String text = getImpl().getText();
        Object rawValue = validateRawValue(text);
        if (rawValue instanceof String) {
            rawValue = StringUtils.trimToNull((String) rawValue);
        }

        //noinspection unchecked
        return (V) rawValue;
    }

    @Override
    public void setValue(Object value) {
        DesktopBackgroundWorker.checkSwingUIAccess();

        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent(value);
            fireChangeListeners(value);
        } else {
            updateComponent(prevValue);
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
        valueFormatter.setMetaProperty(metaProperty);

        //noinspection unchecked
        datasource.addItemChangeListener(e -> {
            if (updatingInstance) {
                return;
            }

            Object value = InstanceUtils.getValueEx(e.getItem(), metaPropertyPath.getPath());
            updateComponent(value);
            fireChangeListeners(value);
        });

        //noinspection unchecked
        datasource.addItemPropertyChangeListener(e -> {
            if (updatingInstance) {
                return;
            }

            if (e.getProperty().equals(metaPropertyPath.toString())) {
                updateComponent(e.getValue());
                fireChangeListeners(e.getValue());
            }
        });

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(datasource.getMetaClass(), property));
        }

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object value = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            updateComponent(value);
            fireChangeListeners();
        }

        Integer maxLength = (Integer) metaProperty.getAnnotations().get("length");
        if (maxLength != null && this instanceof TextInputField.MaxLengthLimited) {
            ((TextInputField.MaxLengthLimited)this).setMaxLength(maxLength);
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }
    }

    @Override
    protected boolean isEmpty(Object value) {
        if (value instanceof String)
            return StringUtils.isBlank((String) value);
        else
            return value == null;
    }

    protected void showValidationMessage() {
        if (ValidationAlertHolder.isListen()) {
            ValidationAlertHolder.validationFailed();
        }

        impl.requestFocus();

        Messages messages = AppBeans.get(Messages.NAME);
        DesktopComponentsHelper.getTopLevelFrame(this).showNotification(
                messages.getMessage(AppConfig.getMessagesPack(), "validationFail"),
                Frame.NotificationType.TRAY
        );
    }

    protected Object validateRawValue(String rawValue) {
        if (trimming && rawValue != null)
            rawValue = rawValue.trim();

        if ((datasource != null) && (metaPropertyPath != null)) {
            Range range = metaProperty.getRange();
            if (range.isDatatype())
                datatype = metaPropertyPath.getRange().asDatatype();
            if (range.isClass())
                return prevValue;
            if (range.isEnum()) {
                try {
                    return range.asEnumeration().parse(rawValue, locale);
                } catch (ParseException e) {
                    showValidationMessage();
                    return prevValue;
                }
            }
        }
        if (datatype != null) {
            try {
                // double conversion to verify type constraints
                // used for properly parsing BigDecimal values
                Object datatypeValue = datatype.parse(rawValue, locale);

                String formattedValue;
                if (valueFormatter.getFormatter() == null) {
                    formattedValue = datatype.format(datatypeValue, locale);
                } else {
                    formattedValue = valueFormatter.getFormatter().format(datatypeValue);
                }

                return datatype.parse(formattedValue, locale);
            } catch (ParseException ignored) {
                showValidationMessage();
                return prevValue;
            }
        }
        return rawValue;
    }

    protected void updateComponent(Object value) {
        getImpl().setText(valueFormatter.formatValue(value));
        getImpl().setCaretPosition(0);
        updateMissingValueState();
    }

    protected void fireChangeListeners() {
        fireChangeListeners(getValue());
    }

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    protected void updateInstance(Object value) {
        if (updatingInstance)
            return;

        if (ObjectUtils.equals(prevValue, value)) {
            return;
        }

        updatingInstance = true;
        try {
            if (datasource != null && metaProperty != null && datasource.getItem() != null)
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }

    protected void flush() {
        if (isEditable() && isEnabled()) {
            Object newValue = validateRawValue(getImpl().getText());
            if ("".equals(newValue))
                newValue = null;

            if (!ObjectUtils.equals(prevValue, newValue))
                setValue(newValue);
            else
                updateComponent(newValue);
        }
    }

    protected class TextFieldListener implements FocusListener, KeyListener {
        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            flush();
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (KeyEvent.VK_ENTER == e.getKeyCode() && e.getModifiers() == 0) {
                flush();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
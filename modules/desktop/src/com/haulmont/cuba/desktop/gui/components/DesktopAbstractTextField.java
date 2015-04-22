/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.validation.ValidationAlertHolder;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Locale;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class DesktopAbstractTextField<T extends JTextComponent> extends DesktopAbstractField<T> {

    protected TextComponentDocument doc;

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
        doc = new TextComponentDocument();

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
        if (!ObjectUtils.equals(prevValue, value)) {
            if (valueChangingListener != null) {
                value = fireValueChanging(prevValue, value);
            }
        }

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
        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;

                        Object value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
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

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
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
                IFrame.NotificationType.TRAY
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

                //noinspection unchecked
                String formattedValue;
                if (valueFormatter.getFormatter() == null) {
                    formattedValue = datatype.format(datatypeValue);
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
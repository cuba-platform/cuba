/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTextField extends DesktopAbstractField<JTextComponent> implements TextField {

    private TextComponentDocument doc;
    private Datasource datasource;
    private MetaProperty metaProperty;
    private MetaPropertyPath metaPropertyPath;
    private int rows;
    private int columns;
    private boolean secret;
    private Datatype datatype;
    private Formatter formatter;
    private int maxLength;
    private boolean editable = true;
    private boolean visible = true;
    private boolean enabled = true;
    private Object prevValue;
    private String caption;
    private String description;

    private Locale locale = UserSessionProvider.getLocale();

    private boolean updatingInstance;

    private JComponent composition;

    public DesktopTextField() {
        doc = new TextComponentDocument();
        doc.setMaxLength(maxLength);
    }

    protected JTextComponent getImpl() {
        if (impl == null) {
            if (rows > 1) {
                impl = new JTextArea();
                ((JTextArea) impl).setRows(rows);
                ((JTextArea) impl).setLineWrap(true);
                ((JTextArea) impl).setWrapStyleWord(true);

                int height = (int) impl.getPreferredSize().getHeight();
                impl.setMinimumSize(new Dimension(0, height));

                composition = new JScrollPane(impl);
                composition.setPreferredSize(new Dimension(150, height));
                composition.setMinimumSize(new Dimension(0, height));

                doc.putProperty("filterNewlines", false);

            } else {
                if (secret)
                    impl = new JPasswordField();
                else
                    impl = new JTextField();
                int height = (int) impl.getPreferredSize().getHeight();
                impl.setPreferredSize(new Dimension(150, height));
                composition = impl;
            }

            impl.setEditable(editable);
            impl.setDocument(doc);
            TextFieldListener listener = new TextFieldListener();
            impl.addKeyListener(listener);
            impl.addFocusListener(listener);
        }
        return impl;
    }

    public int getRows() {
        return rows;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (impl != null)
            impl.setEnabled(enabled);
        requestContainerUpdate();
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (impl != null)
            impl.setVisible(visible);
        if (composition != impl && composition != null) {
            composition.setVisible(visible);
        }
        requestContainerUpdate();
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setRows(int rows) {
        if ((this.rows <= 1 && rows > 1) || (this.rows > 1 && rows == 1))
            impl = null;
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        if (this.secret != secret)
            impl = null;
        this.secret = secret;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int value) {
        maxLength = value;
        doc.setMaxLength(value);
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }

    public <T> T getValue() {
        String text = getImpl().getText();
        return (T) validateRawValue(text);
    }

    public void setValue(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);

            updateComponent(value);

            fireChangeListeners();
        }
    }

    private void updateComponent(Object value) {
        String text;
        if (metaProperty != null)
            text = formatValue(value, metaProperty);
        else if (datatype != null)
            text = datatype.format(value, locale);
        else
            text = value == null ? "" : String.valueOf(value);
        getImpl().setText(text);
    }

    private Object validateRawValue(String rawValue) {
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
                return datatype.parse(rawValue, locale);
            } catch (ParseException ignored) {
                showValidationMessage();
                return prevValue;
            }
        }
        if (StringUtils.isEmpty(rawValue))
            return null;
        return rawValue;
    }

    private void showValidationMessage() {
        App.getInstance().showNotificationPopup(
                MessageProvider.getMessage(AppConfig.getMessagesPack(), "validationFail"),
                IFrame.NotificationType.TRAY
        );
    }

    @Override
    protected boolean isEmpty(Object value) {
        if (value instanceof String)
            return StringUtils.isBlank((String) value);
        else
            return value == null;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

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
        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object value = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            updateComponent(value);
            fireChangeListeners();
        }
    }

    private void fireChangeListeners() {
        fireChangeListeners(getValue());
    }

    private void fireChangeListeners(Object newValue) {
        if (!ObjectUtils.equals(prevValue, newValue)) {
            fireValueChanged(prevValue, newValue);
            prevValue = newValue;
        }
    }

    private void updateInstance(Object value) {
        if (updatingInstance)
            return;

        if (ObjectUtils.equals(prevValue, value))
            return;

        updatingInstance = true;
        try {
            if ((datasource != null) && (metaPropertyPath != null)) {
                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                }
            }
        } finally {
            updatingInstance = false;
        }
    }

    private String formatValue(Object value, MetaProperty metaProperty) {
        String text;
        if (value == null) {
            text = "";
        } else if (formatter == null) {
            Range range = metaProperty.getRange();
            if (range.isDatatype()) {
                text = range.asDatatype().format(value, locale);
            } else if (range.isEnum()) {
                text = value.toString();
            } else if (range.isClass()) {
                text = InstanceUtils.getInstanceName((Instance) value);
            } else
                text = value.toString();
        } else {
            text = formatter.format(value);
        }
        return text;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (impl != null)
            impl.setEditable(editable);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public <T> T getComponent() {
        return (T) getImpl();
    }

    @Override
    public JComponent getComposition() {
        getImpl();
        return composition;
    }

    protected class TextFieldListener implements FocusListener, KeyListener {
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
            Object newValue = validateRawValue(getImpl().getText());
            if ("".equals(newValue))
                newValue = null;

            if (!ObjectUtils.equals(prevValue, newValue))
                setValue(newValue);
            else
                updateComponent(newValue);
        }
    }
}

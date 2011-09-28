/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
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

    private Datatype datatype;
    private Datasource datasource;
    private MetaProperty metaProperty;
    private MetaPropertyPath metaPropertyPath;

    private int rows;
    private int columns;
    private int maxLength;

    private boolean secret;
    private boolean editable = true;
    private boolean enabled = true;

    private Object prevValue;

    private String caption;
    private String description;

    private DefaultValueFormatter valueFormatter;
    private Locale locale = UserSessionProvider.getLocale();

    private boolean updatingInstance;

    private JComponent composition;

    public DesktopTextField() {
        doc = new TextComponentDocument();
        doc.setMaxLength(maxLength);

        valueFormatter = new DefaultValueFormatter(locale);
    }

    @Override
    protected JTextComponent getImpl() {
        if (impl == null) {
            instantiateImpl();
        }
        return impl;
    }

    private void instantiateImpl() {
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
        impl.putClientProperty(getSwingPropertyId(), getId());
    }

    @Override
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
    public void setRows(int rows) {
        if ((this.rows <= 1 && rows > 1) || (this.rows > 1 && rows == 1))
            impl = null;
        this.rows = rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public boolean isSecret() {
        return secret;
    }

    @Override
    public void setSecret(boolean secret) {
        if (this.secret != secret)
            impl = null;
        this.secret = secret;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(int value) {
        maxLength = value;
        doc.setMaxLength(value);
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
        valueFormatter.setDatatype(datatype);
    }

    @Override
    public <T> T getValue() {
        String text = getImpl().getText();
        return (T) validateRawValue(text);
    }

    @Override
    public void setValue(Object value) {
       if (!ObjectUtils.equals(prevValue, value)) {
           updateInstance(value);
           updateComponent(value);
           fireChangeListeners(value);
       }
    }

    private void updateComponent(Object value) {
        getImpl().setText(valueFormatter.formatValue(value));
        getImpl().setCaretPosition(0);
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

        valueFormatter.setMetaProperty(metaProperty);

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
        }
        prevValue = newValue;
    }

    private void updateInstance(Object value) {
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
        return getImpl().getToolTipText();
    }

    public void setDescription(String description) {
        getImpl().setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    public Formatter getFormatter() {
        return valueFormatter.getFormatter();
    }

    public void setFormatter(Formatter formatter) {
        valueFormatter.setFormatter(formatter);
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
    }
}

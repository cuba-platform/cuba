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
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

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
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (impl != null)
            impl.setVisible(visible);
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
        return (T) getImpl().getText();
    }

    public void setValue(Object value) {
        setValueFromText(value == null ? "" : String.valueOf(value));
    }

    @Override
    protected boolean isEmpty(Object value) {
        return StringUtils.isBlank((String) value);
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
                        String text = formatValue(value, metaProperty);
                        setValueFromText(text);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            String text = formatValue(value, metaProperty);
                            setValueFromText(text);
                        }
                    }
                }
        );

        doc.addDocumentListener(
                new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        updateInstance();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        updateInstance();
                    }

                    public void changedUpdate(DocumentEvent e) {
                    }
                }
        );

        setRequired(metaProperty.isMandatory());
        if (datasource.getState() == Datasource.State.VALID) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            setValue(newValue);
        }
    }

    private void setValueFromText(String text) {
        updatingInstance = true;
        try {
            getImpl().setText(text);
        } finally {
            updatingInstance = false;
        }
        Object value = getValue();
        if (ObjectUtils.equals(prevValue, value)) {
            fireValueChanged(prevValue, value);
            prevValue = value;
        }
    }

    private void updateInstance() {
        if (updatingInstance || datasource == null || metaPropertyPath == null)
            return;

        updatingInstance = true;
        try {
            String text = getImpl().getText();
            Object value;
            if (metaProperty.getRange().isDatatype()) {
                try {
                    value = metaProperty.getRange().asDatatype().parse(text, UserSessionProvider.getLocale());
                } catch (ParseException e) {
                    log.warn(e);
                    return;
                }
            } else {
                value = text;
            }
            if (datasource.getItem() != null) {
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
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
                text = range.asDatatype().format(value, UserSessionProvider.getLocale());
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
        return null;
    }

    public void setDescription(String description) {
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
            final Object value = getValue();
            if (!ObjectUtils.equals(prevValue, value)) {
                fireValueChanged(prevValue, value);
                prevValue = value;
            }
        }
    }
}

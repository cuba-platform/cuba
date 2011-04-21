/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopTextField extends DesktopAbstractComponent<JTextComponent> implements TextField {

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

    private boolean updatingInstance;

    public DesktopTextField() {
        doc = new TextComponentDocument();
        doc.setMaxLength(maxLength);
    }

    protected JTextComponent getImpl() {
        if (impl == null) {
            if (rows > 1) {
                impl = new JTextArea();
            } else {
                if (secret)
                    impl = new JPasswordField();
                else
                    impl = new JTextField();
            }
            int height = (int) impl.getPreferredSize().getHeight();
            impl.setPreferredSize(new Dimension(150, height));
            impl.setEditable(editable);
            impl.setDocument(doc);
        }
        return impl;
    }

    public int getRows() {
        return rows;
    }

    @Override
    public float getHeight() {
        return getImpl().getHeight();
    }

    @Override
    public float getWidth() {
        return getImpl().getWidth();
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
    public void setHeight(String height) {
    }

    @Override
    public void setWidth(String width) {
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
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }

    public boolean isRequired() {
        return false;
    }

    public void setRequired(boolean required) {
    }

    public void setRequiredMessage(String msg) {
    }

    public <T> T getValue() {
        return (T) getImpl().getText();
    }

    public void setValue(Object value) {
        getImpl().setText((String) value);
    }

    public void addListener(ValueListener listener) {
    }

    public void removeListener(ValueListener listener) {
    }

    public void addValidator(Validator validator) {
    }

    public void removeValidator(Validator validator) {
    }

    public boolean isValid() {
        return true;
    }

    public void validate() throws ValidationException {
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;
                        Object value = InstanceUtils.getValueEx((Instance) item, metaPropertyPath.getPath());
                        String text = formatValue(value);
                        getImpl().setText(text);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            String text = formatValue(value);
                            getImpl().setText(text);
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
    }

    private void updateInstance() {
        if (updatingInstance || datasource == null || metaPropertyPath == null)
            return;

        updatingInstance = true;
        try {
            String value = getImpl().getText();
            InstanceUtils.setValueEx((Instance) datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }

    private String formatValue(Object value) {
        String text;
        if (value == null) {
            text = "";
        } else if (formatter == null) {
            if (value instanceof Instance) {
                text = InstanceUtils.getInstanceName((Instance) value);
            } else {
                text = value.toString();
            }
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
        return null;
    }

    public void setCaption(String caption) {
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
        return getImpl();
    }
}

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;

public class WebTextField
    extends
        WebAbstractField<com.haulmont.cuba.web.toolkit.ui.TextField>
    implements
        TextField, Component.Wrapper {

    private Log log = LogFactory.getLog(WebTextField.class);

    private Datatype datatype;

    public WebTextField() {
        this.component = new com.haulmont.cuba.web.toolkit.ui.TextField();
        attachListener(component);
        component.setImmediate(true);
        component.setNullRepresentation("");
    }

    public int getRows() {
        return component.getRows();
    }

    public void setRows(int rows) {
        component.setRows(rows);
    }

    public int getColumns() {
        return component.getColumns();
    }

    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    public boolean isSecret() {
        return component.isSecret();
    }

    public void setSecret(boolean secret) {
        component.setSecret(secret);
    }

    public int getMaxLength() {
        return component.getMaxLength();
    }

    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public <T> T getValue() {
        Object value = super.getValue();
        if (datasource == null && datatype != null && value instanceof String) {
            try {
                return (T) datatype.parse((String) value);
            } catch (ParseException e) {
                log.warn("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                return null;
            }
        } else {
            return (T) value;
        }
    }

    @Override
    public void setValue(Object value) {
        if (datasource == null && datatype != null && value != null) {
            String str = datatype.format(value);
            super.setValue(str);
        } else {
            super.setValue(value);
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);
        Integer len = (Integer) metaProperty.getAnnotations().get("length");
        if (len != null) {
            component.setMaxLength(len);
        }
    }
}

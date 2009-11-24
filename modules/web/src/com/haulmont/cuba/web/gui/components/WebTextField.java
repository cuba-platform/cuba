/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;

public class WebTextField
    extends
        WebAbstractField<com.haulmont.cuba.web.toolkit.ui.TextField>
    implements
        TextField, Component.Wrapper {

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

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);
        Integer len = (Integer) metaProperty.getAnnotations().get("length");
        if (len != null) {
            component.setMaxLength(len);
        }
    }
}

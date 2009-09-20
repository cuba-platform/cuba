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
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.data.Datasource;
import com.vaadin.ui.RichTextArea;

public class WebTextArea
    extends
        WebAbstractField<RichTextArea>
    implements
        TextArea, Component.Wrapper {

    public WebTextArea() {
        this.component = new RichTextArea();
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
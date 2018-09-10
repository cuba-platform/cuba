/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.vaadin.v7.ui.AbstractSelect;

import java.util.function.Function;

public class CalculatableColumnGenerator implements SystemTableColumnGenerator {
    protected Table table;

    public CalculatableColumnGenerator(Table table) {
        this.table = table;
    }

    @Override
    public com.vaadin.ui.Component generateCell(com.vaadin.v7.ui.Table source, Object itemId, Object columnId) {
        return generateCell((AbstractSelect) source, itemId, columnId);
    }

    protected com.vaadin.ui.Component generateCell(AbstractSelect source, Object itemId, Object columnId) {
        CollectionDatasource ds = table.getDatasource();
        MetaPropertyPath propertyPath = ds.getMetaClass().getPropertyPath(columnId.toString());

        PropertyWrapper propertyWrapper = (PropertyWrapper) source.getContainerProperty(itemId, propertyPath);

        Function formatter = null;
        Table.Column column = table.getColumn(columnId.toString());
        if (column != null) {
            formatter = column.getFormatter();
        }

        com.vaadin.ui.Label label = new com.vaadin.ui.Label();
        setLabelText(label, propertyWrapper.getValue(), formatter);
        label.setWidthUndefined();

        //add property change listener that will update a label value
        propertyWrapper.addListener(new CalculatablePropertyValueChangeListener(label, formatter));

        return label;
    }

    @Deprecated
    public static void setLabelText(com.vaadin.ui.Label label, Object value, Function<Object, String> formatter) {
        label.setValue(value == null
                ? "" : String.class.isInstance(value)
                ? (String) value : formatter != null
                ? formatter.apply(value) : value.toString()
        );
    }
}
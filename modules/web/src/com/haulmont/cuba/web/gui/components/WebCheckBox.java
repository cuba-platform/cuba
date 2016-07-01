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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;

import java.util.Collection;

public class WebCheckBox extends WebAbstractField<com.vaadin.ui.CheckBox> implements CheckBox {

    public WebCheckBox() {
        this.component = new CubaCheckBox();
        component.setImmediate(true);
        component.setInvalidCommitted(true);

        attachListener(component);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    @Override
                    public Object getValue() {
                        Object value = super.getValue();
                        if (value == null) {
                            Range range = propertyPath.getRange();
                            if (range.isDatatype()
                                    && range.asDatatype().equals(Datatypes.get(Boolean.class))) {
                                value = Boolean.FALSE;
                            }
                        }
                        return value;
                    }
                };
            }
        };
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            super.setValue(Boolean.FALSE);
        } else {
            super.setValue(value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean getValue() {
        return super.getValue();
    }

    @Override
    public boolean isChecked() {
        return Boolean.TRUE.equals(getValue());
    }
}
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

import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.web.widgets.CubaOptionGroup;
import com.haulmont.cuba.web.widgets.client.optiongroup.OptionGroupOrientation;
import com.vaadin.v7.data.Property;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebOptionsGroup<V> extends WebAbstractOptionsBase<CubaOptionGroup, V> implements OptionsGroup<V> {

    protected Orientation orientation = Orientation.VERTICAL;

    public WebOptionsGroup() {
        component = new CubaOptionGroup() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                if (newDataSource == null) {
                    super.setPropertyDataSource(null);
                    return;
                }

                // todo
                /*super.setPropertyDataSource(new PropertyAdapter(newDataSource) {

                    @Override
                    public Class getType() {
                        // we ourselves convert values in this property adapter
                        return Object.class;
                    }

                    @Override
                    public Object getValue() {
                        final Object o = itemProperty.getValue();

                        Object keyFromValue = getKeyFromValue(o);
                        if (metaProperty != null && keyFromValue != null) {
                            if (List.class.isAssignableFrom(metaProperty.getJavaType())) {
                                return new ArrayList((Collection) keyFromValue);
                            }
                        }
                        return keyFromValue;
                    }

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });*/
            }
        };
        attachListener(component);
        initDefaults(component);
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        checkNotNull(orientation, "Orientation must not be null");

        if (orientation != this.orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                component.setOrientation(OptionGroupOrientation.HORIZONTAL);
            } else {
                component.setOrientation(OptionGroupOrientation.VERTICAL);
            }
            this.orientation = orientation;
        }
    }

    // todo
    /*@Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);

        if (metaProperty != null) {
            if (List.class.isAssignableFrom(metaProperty.getJavaType())) {
                component.setConverter(new SetToListConverter());
            }
        }
    }*/

    @Override
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        Object value = getValue();
        return (value instanceof Collection)
                ? (Collection) value
                : Collections.singleton(value);
    }
}
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

import com.vaadin.v7.data.Property;

@SuppressWarnings("deprecation")
public class TableItemPropertyWrapper implements Property, Property.ValueChangeNotifier {
    protected boolean readOnly;
    protected TableItemWrapper itemWrapper;
    protected Object propertyId;

    public TableItemPropertyWrapper(TableItemWrapper itemWrapper, Object propertyId) {
        this.itemWrapper = itemWrapper;
        this.propertyId = propertyId;
    }

    @Override
    public Object getValue() {
        return itemWrapper.getPropertyValue(propertyId);
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException {
        if (readOnly) {
            throw new ReadOnlyException();
        }

        itemWrapper.setPropertyValue(propertyId, newValue);
    }

    @Override
    public Class getType() {
        return itemWrapper.getPropertyType(propertyId);
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        // Table uses single listener on value change for all the properties
        itemWrapper.addValueChangeListener(listener);
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        // do nothing
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
        // do nothing
    }
}
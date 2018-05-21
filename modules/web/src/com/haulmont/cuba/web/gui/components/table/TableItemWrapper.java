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

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class TableItemWrapper implements Item {
    protected Object itemId;
    protected Map<Object, TableItemPropertyWrapper> propertyWrappers = new HashMap<>();

    protected TableDataContainer tableDataContainer;

    public TableItemWrapper(TableDataContainer tableDataContainer) {
        this.tableDataContainer = tableDataContainer;
    }

    public Object getItemId() {
        return itemId;
    }

    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }

    public Map<Object, TableItemPropertyWrapper> getPropertyWrappers() {
        return propertyWrappers;
    }

    @Override
    public Property getItemProperty(Object id) {
        return propertyWrappers.get(id);
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        return propertyWrappers.keySet();
    }

    @Override
    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Class getPropertyType(Object propertyId) {
        return tableDataContainer.getTableSource().getType(propertyId);
    }

    public Object getPropertyValue(Object propertyId) {
        return tableDataContainer.getTableSource().getItemValue(itemId, propertyId);
    }

    public void setPropertyValue(Object propertyId, Object newValue) {
        tableDataContainer.getTableSource().setItemValue(itemId, propertyId, newValue);
    }

    public void addValueChangeListener(Property.ValueChangeListener propertyValueChangeListener) {
        tableDataContainer.addValueChangeListener(propertyValueChangeListener);
    }
}
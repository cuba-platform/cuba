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
package com.haulmont.cuba.web.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemWrapper implements Item, UnsubscribableDsWrapper {

    protected Map<MetaPropertyPath, PropertyWrapper> properties = new HashMap<>();

    protected Object item;
    protected MetaClass metaClass;

    public ItemWrapper(Object item, MetaClass metaClass, Collection<MetaPropertyPath> properties) {
        this.item = item;
        this.metaClass = metaClass;

        for (MetaPropertyPath property : properties) {
            this.properties.put(property, createPropertyWrapper(item, property));
        }
    }

    protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
        return new PropertyWrapper(item, propertyPath);
    }

    @Override
    public Property getItemProperty(Object id) {
        if (id instanceof MetaPropertyPath) {
            return properties.get(id);
        }
        return null;
    }

    @Override
    public Collection getItemPropertyIds() {
        return properties.keySet();
    }

    @Override
    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsubscribe() {
        item = null;
        metaClass = null;

        properties.values().forEach(PropertyWrapper::unsubscribe);
        properties.clear();
    }

    @Override
    public String toString() {
        Entity entity = getItem();
        return entity == null ? "" : entity.getInstanceName();
    }

    public Entity getItem() {
        return item instanceof Datasource ? ((Datasource) item).getItem() : (Entity) item;
    }
}
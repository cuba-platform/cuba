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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.model.CollectionPropertyContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class CollectionPropertyContainerImpl<E extends Entity>
        extends CollectionContainerImpl<E> implements CollectionPropertyContainer<E> {

    protected InstanceContainer parent;
    protected String property;

    public CollectionPropertyContainerImpl(MetaClass metaClass, InstanceContainer parent, String property) {
        super(metaClass);
        this.parent = parent;
        this.property = property;
        sorter = new CollectionPropertyContainerSorter(this);
    }

    @Override
    public InstanceContainer getParent() {
        return parent;
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public List<E> getDisconnectedItems() {
        return super.getMutableItems();
    }

    @Override
    public void setDisconnectedItems(@Nullable Collection<E> entities) {
        super.setItems(entities);
    }

    @Override
    public List<E> getMutableItems() {
        return new ObservableList<>(collection, (changeType, changes) -> {
            buildIdMap();
            clearItemIfNotExists();
            updateParent();
            fireCollectionChanged(changeType, changes);
        });
    }

    @Override
    public void setItems(@Nullable Collection<E> entities) {
        super.setItems(entities);
        Entity parentItem = parent.getItemOrNull();
        if (parentItem != null) {
            MetaProperty parentProperty = getParentProperty();
            Collection parentCollection = parentItem.getValue(parentProperty.getName());
            if (parentCollection != entities) {
                updateParentCollection(parentProperty, parentCollection, entities);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void updateParent() {
        MetaProperty parentProperty = getParentProperty();
        Collection parentCollection = parent.getItem().getValue(parentProperty.getName());
        updateParentCollection(parentProperty, parentCollection, this.collection);
    }

    protected MetaProperty getParentProperty() {
        MetaClass parentMetaClass = parent.getEntityMetaClass();
        MetaProperty parentProperty = parentMetaClass.getPropertyNN(property);
        if (!parentProperty.getRange().getCardinality().isMany()) {
            throw new IllegalStateException(String.format("Property '%s' is not a collection", property));
        }
        return parentProperty;
    }

    @SuppressWarnings("unchecked")
    private void updateParentCollection(MetaProperty metaProperty,
                                        @Nullable Collection parentCollection,
                                        @Nullable Collection<E> newCollection) {
        if (newCollection == null) {
            parent.getItem().setValue(metaProperty.getName(), null);
        } else {
            if (parentCollection == null) {
                if (List.class.isAssignableFrom(metaProperty.getJavaType())) {
                    parentCollection = new ArrayList(newCollection);
                } else {
                    parentCollection = new LinkedHashSet(newCollection);
                }
                parent.getItem().setValue(metaProperty.getName(), parentCollection);
            } else {
                parentCollection.clear();
                parentCollection.addAll(newCollection);
            }
        }
    }
}

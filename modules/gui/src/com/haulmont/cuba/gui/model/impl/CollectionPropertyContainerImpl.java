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

import com.haulmont.chile.core.model.Instance;
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

    protected InstanceContainer master;
    protected String property;

    public CollectionPropertyContainerImpl(MetaClass metaClass, InstanceContainer master, String property) {
        super(metaClass);
        this.master = master;
        this.property = property;
    }

    @Override
    public InstanceContainer getMaster() {
        return master;
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
        return new ObservableList<>(collection, idMap, (changeType, changes) -> {
            buildIdMap();
            clearItemIfNotExists();
            updateMaster();
            fireCollectionChanged(changeType, changes);
        });
    }

    @Override
    public void setItems(@Nullable Collection<E> entities) {
        super.setItems(entities);
        Entity masterItem = master.getItemOrNull();
        if (masterItem != null) {
            MetaProperty masterProperty = getMasterProperty();
            Collection masterCollection = masterItem.getValue(masterProperty.getName());
            if (masterCollection != entities) {
                updateMasterCollection(masterProperty, masterCollection, entities);
            }
        }
    }

    protected void updateMaster() {
        MetaProperty masterProperty = getMasterProperty();
        Collection masterCollection = master.getItem().getValue(masterProperty.getName());
        updateMasterCollection(masterProperty, masterCollection, this.collection);
    }

    protected MetaProperty getMasterProperty() {
        MetaClass masterMetaClass = master.getEntityMetaClass();
        MetaProperty masterProperty = masterMetaClass.getPropertyNN(property);
        if (!masterProperty.getRange().getCardinality().isMany()) {
            throw new IllegalStateException(String.format("Property '%s' is not a collection", property));
        }
        return masterProperty;
    }

    @SuppressWarnings("unchecked")
    private void updateMasterCollection(MetaProperty metaProperty,
                                        @Nullable Collection masterCollection,
                                        @Nullable Collection<E> newCollection) {
        if (newCollection == null) {
            master.getItem().setValue(metaProperty.getName(), null);
        } else {
            if (masterCollection == null) {
                if (List.class.isAssignableFrom(metaProperty.getJavaType())) {
                    masterCollection = new ArrayList(newCollection);
                } else {
                    masterCollection = new LinkedHashSet(newCollection);
                }
                master.getItem().setValue(metaProperty.getName(), masterCollection);
            } else {
                masterCollection.clear();
                masterCollection.addAll(newCollection);
                if (master instanceof ItemPropertyChangeNotifier) {
                    Instance.PropertyChangeEvent event = new Instance.PropertyChangeEvent(
                            master.getItem(),
                            metaProperty.getName(),
                            masterCollection,
                            masterCollection
                    );
                    ((ItemPropertyChangeNotifier) master).itemPropertyChanged(event);
                }
            }
        }
    }
}

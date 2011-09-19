/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 15:18:26
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class PropertyDatasourceImpl<T extends Entity>
        extends
        AbstractDatasource<T>
        implements
        Datasource<T>, DatasourceImplementation<T>, PropertyDatasource<T> {

    protected Datasource masterDs;
    protected MetaProperty metaProperty;

    public PropertyDatasourceImpl(String id, Datasource ds, String property) {
        super(id);
        this.masterDs = ds;
        metaProperty = ds.getMetaClass().getProperty(property);
        initParentDsListeners();
    }

    protected void initParentDsListeners() {
        masterDs.addListener(new DatasourceListener<Entity>() {

            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                Entity prevValue = getItem(prevItem);
                Entity newValue = getItem(item);
                reattachListeners(prevValue, newValue);
                fireItemChanged(prevValue);
            }

            public void stateChanged(Datasource ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                    dsListener.stateChanged(PropertyDatasourceImpl.this, prevState, state);
                }
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals(metaProperty.getName()) && !ObjectUtils.equals(prevValue, value)) {
                    reattachListeners((Entity) prevValue, (Entity) value);
                    fireItemChanged(prevValue);
                }
            }

            private void reattachListeners(Entity prevItem, Entity item) {
                if (prevItem != item) {
                    detachListener(prevItem);
                    attachListener(item);
                }
            }
        });
    }

    public State getState() {
        return masterDs.getState();
    }

    public T getItem() {
        final Instance item = masterDs.getItem();
        return getItem(item);
    }

    private T getItem(Instance item) {
        return item == null ? null : (T) item.getValue(metaProperty.getName());
    }

    public MetaClass getMetaClass() {
        MetaClass metaClass = metaProperty.getRange().asClass();
        Class replacedClass = MetadataProvider.getReplacedClass(metaClass);
        return replacedClass != null ? MetadataProvider.getSession().getClass(replacedClass) : metaClass;
    }

    public View getView() {
        final ViewProperty property = masterDs.getView().getProperty(metaProperty.getName());
        return property == null ? null : MetadataProvider.getViewRepository().getView(getMetaClass(), property.getView().getName());
    }

    public DsContext getDsContext() {
        return masterDs.getDsContext();
    }

    public DataService getDataService() {
        return masterDs.getDataService();
    }

    public void commit() {
        if (Datasource.CommitMode.PARENT.equals(getCommitMode())) {
            if (parentDs == null)
                throw new IllegalStateException("parentDs is null while commitMode=PARENT");

            if (parentDs instanceof CollectionDatasource) {
                for (Object item : itemToCreate) {
                    ((CollectionDatasource) parentDs).addItem((Entity) item);
                }
                for (Object item : itemToUpdate) {
                    ((CollectionDatasource) parentDs).modifyItem((Entity) item);
                }
                for (Object item : itemToDelete) {
                    ((CollectionDatasource) parentDs).removeItem((Entity) item);
                }
            } else {
                // ??? No idea what to do here
            }
            clearCommitLists();
            modified = false;
        }
    }

    public void refresh() {
    }

    public void setItem(T item) {
        if (getItem() != null) {
            InstanceUtils.copy(item, getItem());
            itemToUpdate.add(item);
        } else {
            final Instance parentItem = masterDs.getItem();
            parentItem.setValue(metaProperty.getName(), item);
        }
        setModified(true);
    }

    public void invalidate() {
    }


    public void initialized() {
    }

    public void valid() {
    }

    public void committed(Set<Entity> entities) {
        Entity parentItem = masterDs.getItem();

        T prevItem = getItem();
        T newItem = null;
        for (Entity entity : entities) {
            if (entity.equals(prevItem)) {
                newItem = prevItem;
                break;
            }
        }

        // If committed set contains previousItem
        if ((parentItem != null) && newItem != null) {
            // Value changed

            boolean isModified = masterDs.isModified();

            AbstractInstance parentInstance = (AbstractInstance) parentItem;
            parentInstance.setValue(metaProperty.getName(), newItem, false);
            detachListener(prevItem);
            attachListener(newItem);

            fireItemChanged(prevItem);

            ((DatasourceImplementation) masterDs).setModified(isModified);
        } else {
            if (parentItem != null) {
                Entity newParentItem = null;
                Entity previousParentItem = null;

                // Find previous and new parent items
                Iterator<Entity> commitIter = entities.iterator();
                while (commitIter.hasNext() && (previousParentItem == null) && (newParentItem == null)) {
                    Entity commitItem = commitIter.next();
                    if (commitItem.equals(parentItem)) {
                        previousParentItem = parentItem;
                        newParentItem = commitItem;
                    }
                }
                if (previousParentItem != null) {
                    detachListener(getItem(previousParentItem));
                }
                if (newParentItem != null) {
                    attachListener(getItem(newParentItem));
                }
            }
        }
        modified = false;
        clearCommitLists();
    }

    @Override
    public Datasource getMaster() {
        return masterDs;
    }

    @Override
    public MetaProperty getProperty() {
        return metaProperty;
    }
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * @author abramov
 * @version $Id$
 */
public class PropertyDatasourceImpl<T extends Entity>
        extends AbstractDatasource<T>
        implements Datasource<T>, DatasourceImplementation<T>, PropertyDatasource<T> {

    protected Datasource masterDs;
    protected MetaProperty metaProperty;
    protected volatile MetaClass metaClass;
    protected volatile View view;

    @Override
    public void setup(String id, Datasource masterDs, String property) {
        this.id = id;
        this.masterDs = masterDs;
        metaProperty = masterDs.getMetaClass().getProperty(property);
        initParentDsListeners();
    }

    protected void initParentDsListeners() {
        masterDs.addListener(new DatasourceListener<Entity>() {
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                Entity prevValue = getItem(prevItem);
                Entity newValue = getItem(item);
                reattachListeners(prevValue, newValue);
                fireItemChanged(prevValue);
            }

            @Override
            public void stateChanged(Datasource ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                    dsListener.stateChanged(PropertyDatasourceImpl.this, prevState, state);
                }
            }

            @Override
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

    @Override
    public State getState() {
        return masterDs.getState();
    }

    @Override
    public T getItem() {
        final Instance item = masterDs.getItem();
        return getItem(item);
    }

    private T getItem(Instance item) {
        return item == null ? null : (T) item.getValue(metaProperty.getName());
    }

    @Override
    public MetaClass getMetaClass() {
        if (metaClass == null) {
            MetaClass propertyMetaClass = metaProperty.getRange().asClass();
            metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(propertyMetaClass);
        }
        return metaClass;
    }

    @Override
    public View getView() {
        if (view == null) {
            View masterView = masterDs.getView();
            if (masterView == null)
                throw new IllegalStateException("No view for datasource " + masterDs.getId());
            ViewProperty property = masterView.getProperty(metaProperty.getName());
            if (property == null)
                return null;
            if (property.getView() == null)
                throw new IllegalStateException("Invalid view definition: " + masterView
                        + ". Property '" + property + "' must have a view");
            view = metadata.getViewRepository().findView(getMetaClass(), property.getView().getName());
            //anonymous (nameless) view
            if (view == null)
                view = property.getView();
        }
        return view;
    }

    @Override
    public DsContext getDsContext() {
        return masterDs.getDsContext();
    }

    @Override
    public DataSupplier getDataSupplier() {
        return masterDs.getDataSupplier();
    }

    @Override
    public void commit() {
        if (!allowCommit)
            return;

        if (Datasource.CommitMode.PARENT.equals(getCommitMode())) {
            if (parentDs == null)
                throw new IllegalStateException("parentDs is null while commitMode=PARENT");

            if (parentDs instanceof CollectionDatasource) {
                CollectionDatasource parentCollectionDs = (CollectionDatasource) parentDs;
                for (Object item : itemToCreate) {
                    parentCollectionDs.addItem((Entity) item);
                }
                for (Object item : itemToUpdate) {
                    parentCollectionDs.modifyItem((Entity) item);
                }
                for (Object item : itemToDelete) {
                    parentCollectionDs.removeItem((Entity) item);
                }
            } /* else {
                // ??? No idea what to do here
            } */
            clearCommitLists();
            modified = false;
        }
    }

    @Override
    public void refresh() {
    }

    @Override
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

    @Override
    public void invalidate() {
    }

    @Override
    public void modified(T item) {
        super.modified(item);

        if (masterDs != null)
            ((AbstractDatasource) masterDs).setModified(true);
    }

    @Override
    public void initialized() {
    }

    @Override
    public void valid() {
    }

    @Override
    public void committed(Set<Entity> entities) {
        Entity parentItem = masterDs.getItem();

        T prevItem = getItem();
        T newItem = null;
        for (Entity entity : entities) {
            if (entity.equals(prevItem)) {
                //noinspection unchecked
                newItem = (T) entity;
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
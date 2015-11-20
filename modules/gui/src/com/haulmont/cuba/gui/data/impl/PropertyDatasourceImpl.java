/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Objects;
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
        masterDs.addItemChangeListener(e -> {
            Entity prevValue = getItem(e.getPrevItem());
            Entity newValue = getItem(e.getItem());
            reattachListeners(prevValue, newValue);
            fireItemChanged((T) prevValue);
        });

        masterDs.addStateChangeListener(e -> fireStateChanged(e.getPrevState()));

        masterDs.addItemPropertyChangeListener(e -> {
            if (e.getProperty().equals(metaProperty.getName()) && !ObjectUtils.equals(e.getPrevValue(), e.getValue())) {
                reattachListeners((Entity) e.getPrevValue(), (Entity) e.getValue());
                fireItemChanged((T) e.getPrevValue());
            }
        });
    }

    protected void reattachListeners(Entity prevItem, Entity item) {
        if (prevItem != item) {
            detachListener(prevItem);
            attachListener(item);
        }
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

    @Override
    @Nullable
    public T getItemIfValid() {
        return getState() == State.VALID ? getItem() : null;
    }

    protected T getItem(Instance item) {
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
            MetaClass metaMetaClass = masterDs.getMetaClass();
            if (metadata.getTools().isPersistent(metaMetaClass)
                    || metadata.getTools().isEmbeddable(metaMetaClass)) {
                View masterView = masterDs.getView();
                if (masterView == null) {
                    throw new DevelopmentException("No view for datasource " + masterDs.getId(),
                            ParamsMap.of("masterDs", masterDs.getId(), "propertyDs", getId()));
                }

                ViewProperty property = masterView.getProperty(metaProperty.getName());
                if (property == null) {
                    return null;
                }

                if (property.getView() == null) {
                    throw new DevelopmentException(
                            "Invalid view definition: " + masterView + ". Property '" + property + "' must have a view",
                            ParamsMap.of("masterDs", masterDs.getId(),
                                         "propertyDs", getId(),
                                         "masterView", masterView,
                                         "property", property)
                    );
                }
                view = metadata.getViewRepository().findView(getMetaClass(), property.getView().getName());
                //anonymous (nameless) view
                if (view == null)
                    view = property.getView();
            }
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
        if (!allowCommit) {
            return;
        }

        if (getCommitMode() == CommitMode.PARENT) {
            if (parentDs == null) {
                throw new IllegalStateException("parentDs is null while commitMode=PARENT");
            }

            if (parentDs instanceof CollectionDatasource) {
                CollectionDatasource parentCollectionDs = (CollectionDatasource) parentDs;
                for (Object item : itemsToCreate) {
                    parentCollectionDs.addItem((Entity) item);
                }
                for (Object item : itemsToUpdate) {
                    parentCollectionDs.modifyItem((Entity) item);
                }
                for (Object item : itemsToDelete) {
                    parentCollectionDs.removeItem((Entity) item);
                }
                // after repeated edit of new items the parent datasource can contain items-to-create which are deleted
                // in this datasource, so we need to delete them
                for (Iterator it = ((DatasourceImplementation) parentCollectionDs).getItemsToCreate().iterator(); it.hasNext(); ) {
                    Entity item = (Entity) it.next();
                    if (!itemsToCreate.contains(item)) {
                        MetaProperty inverseProp = metaProperty.getInverse();
                        // delete only if they have the same master item
                        if (inverseProp != null
                                && PersistenceHelper.isLoaded(item, inverseProp.getName())
                                && Objects.equals(item.getValue(inverseProp.getName()), masterDs.getItem())) {
                            it.remove();
                        }
                    }
                }
            }
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
            itemsToUpdate.add(item);
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
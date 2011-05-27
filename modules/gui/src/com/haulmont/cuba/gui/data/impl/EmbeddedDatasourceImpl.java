/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 28.03.11 16:11
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityFactory;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class EmbeddedDatasourceImpl<T extends EmbeddableEntity>
        extends
        AbstractDatasource<T>
        implements
        Datasource<T>, DatasourceImplementation<T>, EmbeddedDatasource<T> {

    protected Datasource ds;
    protected MetaProperty metaProperty;

    public EmbeddedDatasourceImpl(String id, Datasource ds, String property) {
        super(id);
        this.ds = ds;
        metaProperty = ds.getMetaClass().getProperty(property);
        initParentDsListeners();
    }

    protected void initParentDsListeners() {
        ds.addListener(new DatasourceListener<Entity>() {

            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                Entity prevValue = getItem(prevItem);
                Entity newValue = getItem(item);
                reattachListeners(prevValue, newValue);
                forceItemChanged(prevValue);
            }

            public void stateChanged(Datasource ds, State prevState, State state) {
                for (DatasourceListener dsListener : new ArrayList<DatasourceListener>(dsListeners)) {
                    dsListener.stateChanged(EmbeddedDatasourceImpl.this, prevState, state);
                }
            }

            public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                if (property.equals(metaProperty.getName()) && !ObjectUtils.equals(prevValue, value)) {
                    reattachListeners((Entity) prevValue, (Entity) value);
                    forceItemChanged(prevValue);
                }
            }

            private void reattachListeners(Entity prevItem, Entity item) {
                if (!ObjectUtils.equals(prevItem, item)) {
                    detachListener(prevItem);
                    attachListener(item);
                }
            }
        });
    }

    public DsContext getDsContext() {
        return ds.getDsContext();
    }

    public DataService getDataService() {
        return ds.getDataService();
    }

    public void commit() {
        throw new UnsupportedOperationException();
    }

    public State getState() {
        return ds.getState();
    }

    public T getItem() {
        final Instance item = ds.getItem();
        return getItem(item);
    }

    private T getItem(Instance item) {
        return item == null ? null : (T) item.getValue(metaProperty.getName());
    }

    public void setItem(T item) {
        if (getItem() != null) {
            InstanceUtils.copy(item, getItem());
            itemToUpdate.add(item);
        } else {
            final Instance parentItem = ds.getItem();
            parentItem.setValue(metaProperty.getName(), item);
        }
        setModified(true);
        ((DatasourceImplementation) ds).modified(ds.getItem());
    }

    public MetaClass getMetaClass() {
        MetaClass metaClass = metaProperty.getRange().asClass();
        Class replacedClass = EntityFactory.getReplacedClass(metaClass);
        return replacedClass != null ? MetadataProvider.getSession().getClass(replacedClass) : metaClass;
    }

    public View getView() {
        final ViewProperty property = ds.getView().getProperty(metaProperty.getName());
        return property == null ? null : MetadataProvider.getViewRepository().getView(getMetaClass(), property.getView().getName());
    }

    public void commited(Map<Entity, Entity> map) {
        Entity previousItem = getItem();
        T newItem = (T) map.get(previousItem);
        boolean isModified = ds.isModified();

        AbstractInstance parentItem = (AbstractInstance) ds.getItem();
        parentItem.setValue(metaProperty.getName(), newItem, false);
        detachListener(previousItem);
        attachListener(newItem);

        ((DatasourceImplementation) ds).setModified(isModified);

        modified = false;
        clearCommitLists();
    }

    public MetaProperty getProperty() {
        return null;
    }

    public void invalidate() {
    }

    public void refresh() {
    }

    public void initialized() {
    }

    public void valid() {
    }

    @Override
    public void modified(T item) {
        super.modified(item);
        ((DatasourceImplementation) ds).modified(ds.getItem());
    }

    @Override
    public Collection<T> getItemsToCreate() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<T> getItemsToUpdate() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<T> getItemsToDelete() {
        return Collections.EMPTY_LIST;
    }
}
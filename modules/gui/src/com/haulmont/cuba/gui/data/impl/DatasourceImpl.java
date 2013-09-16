/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nullable;
import java.util.Set;

/**
 *
 * @param <T>
 *
 * @author abramov
 * @version $Id$
 */
public class DatasourceImpl<T extends Entity>
        extends AbstractDatasource<T>
        implements DatasourceImplementation<T> {

    protected DsContext dsContext;
    protected DataSupplier dataSupplier;

    protected MetaClass metaClass;
    protected View view;

    protected State state = State.NOT_INITIALIZED;
    protected T item;

    @Override
    public void setup(DsContext dsContext, DataSupplier dataSupplier, String id,
                      MetaClass metaClass, @Nullable View view) {
        this.id = id;
        this.dsContext = dsContext;
        this.dataSupplier = dataSupplier;
        this.metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(metaClass);
        this.view = view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setView(String viewName) {
        this.view = metadata.getViewRepository().getView(metaClass, viewName);
    }

    @Override
    public DsContext getDsContext() {
        return dsContext;
    }

    public void setDsContext(DsContext dsContext) {
        this.dsContext = dsContext;
    }

    @Override
    public DataSupplier getDataSupplier() {
        return dataSupplier;
    }

    public void setDataSupplier(DataSupplier dataservice) {
        this.dataSupplier = dataservice;
    }

    @Override
    public void commit() {
        if (!allowCommit)
            return;

        if (Datasource.CommitMode.DATASTORE.equals(getCommitMode())) {
            final DataSupplier supplier = getDataSupplier();
            item = supplier.commit(item, getView());

            clearCommitLists();
            modified = false;

        } else if (Datasource.CommitMode.PARENT.equals(getCommitMode())) {
            if (parentDs == null)
                throw new IllegalStateException("parentDs is null while commitMode=PARENT");

            if (parentDs instanceof CollectionDatasource) {
                CollectionDatasource ds = (CollectionDatasource) parentDs;
                if (ds.containsItem(item.getId())) {
                    ds.modifyItem(item);
                } else {
                    ds.addItem(item);
                    ds.setItem(item); // This is necessary for nested property datasources to work correctly
                }
            } else {
                parentDs.setItem(item);
            }
            clearCommitLists();
            modified = false;

        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    @Override
    public View getView() {
        return view;
    }


    @Override
    public State getState() {
        return state;
    }

    @Override
    public T getItem() {
        return State.VALID.equals(state) ? item : null;
    }

    @Override
    public void refresh() {
        // Do Nothing
    }

    @Override
    public void setItem(T item) {
        if (State.NOT_INITIALIZED.equals(this.state)) {
            __setItem(item);
        } else {
            Object prevItem = this.item;
            State prevStatus = this.state;

            __setItem(item);
            state = State.VALID;

            fireStateChanged(prevStatus);
            fireItemChanged(prevItem);
        }
    }

    protected void __setItem(T item) {
        if (this.item != null) {
            detachListener(this.item);
        }

        if (item instanceof Instance) {
            final MetaClass aClass = item.getMetaClass();
            if (!aClass.equals(metaClass) && !metaClass.getDescendants().contains(aClass)) {
                throw new IllegalStateException(String.format("Invalid item MetaClass: " + aClass));
            }
            attachListener(item);
        }

        this.item = item;
        this.modified = false;
        clearCommitLists();

        if (item != null && PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
            this.modified = true;
        }
    }

    @Override
    public void invalidate() {
        if (State.NOT_INITIALIZED != state && State.INVALID != state) {
            State prevState = state;
            state = State.INVALID;
            fireStateChanged(prevState);
        }
        modified = false;
        clearCommitLists();
    }

    @Override
    public void initialized() {
        state = State.INVALID;
    }

    @Override
    public void valid() {
        state = State.VALID;
    }

    @Override
    public void committed(Set<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.equals(item)) {
                detachListener(item);
                item = (T) entity;
                attachListener(item);
            }
        }

        modified = false;
        clearCommitLists();
    }
}
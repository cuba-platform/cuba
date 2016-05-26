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
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.DsContext;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @param <T>
 */
public class DatasourceImpl<T extends Entity> extends AbstractDatasource<T> implements DatasourceImplementation<T> {

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
        backgroundWorker.checkUIAccess();

        if (!allowCommit) {
            return;
        }

        if (getCommitMode() == CommitMode.DATASTORE) {
            final DataSupplier supplier = getDataSupplier();
            item = supplier.commit(item, getView());

            clearCommitLists();
            modified = false;

        } else if (getCommitMode() == CommitMode.PARENT) {
            if (parentDs == null) {
                throw new IllegalStateException("parentDs is null while commitMode=PARENT");
            }

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
            throw new UnsupportedOperationException("Unsupported commitMode: " + getCommitMode());
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
        backgroundWorker.checkUIAccess();

        return state == State.VALID ? item : null;
    }

    @Nullable
    @Override
    public T getItemIfValid() {
        backgroundWorker.checkUIAccess();

        return state == State.VALID ? item : null;
    }

    @Override
    public void refresh() {
        backgroundWorker.checkUIAccess();

        State prevState = state;
        if (!prevState.equals(State.VALID)) {
            state = State.VALID;
            fireStateChanged(prevState);
        }
        if (item != null) {
            T prevItem = item;
            T reloadedItem = dataSupplier.reload(item, view);
            __setItem(reloadedItem);
            fireItemChanged(prevItem);
        }
    }

    @Override
    public void setItem(T item) {
        backgroundWorker.checkUIAccess();

        if (this.state == State.NOT_INITIALIZED) {
            __setItem(item);
        } else {
            T prevItem = this.item;
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

        if (item != null) {
            final MetaClass aClass = item.getMetaClass();
            if (!aClass.equals(metaClass) && !metaClass.getDescendants().contains(aClass)) {
                throw new DevelopmentException(String.format("Invalid item metaClass '%s'", aClass),
                        ParamsMap.of("datasource", getId(), "metaClass", aClass));
            }
            attachListener(item);
        }

        this.item = item;
        this.modified = false;
        clearCommitLists();

        if (item != null && PersistenceHelper.isNew(item)) {
            itemsToCreate.add(item);
            this.modified = true;
        }
    }

    @Override
    public void invalidate() {
        if (state != State.NOT_INITIALIZED && state != State.INVALID) {
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
        if (!State.VALID.equals(state)) {
            return;
        }

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
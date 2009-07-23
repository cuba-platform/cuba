/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 14:40:01
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DatasourceImpl<T extends Entity>
    extends
        AbstractDataSource<T>
    implements
        DatasourceImplementation<T>
{
    protected DsContext dsContext;
    protected DataService dataservice;

    protected MetaClass metaClass;
    protected View view;

    protected State state = State.NOT_INITIALIZAED;
    protected T item;

    public DatasourceImpl(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(id);
        this.dsContext = dsContext;
        this.dataservice = dataservice;

        this.metaClass = metaClass;
        this.view = StringUtils.isEmpty(viewName) ? null : MetadataProvider.getViewRepository().getView(metaClass, viewName);

    }

    public DsContext getDsContext() {
        return dsContext;
    }

    public DataService getDataService() {
        return dataservice;
    }

    public CommitMode getCommitMode() {
        return CommitMode.DATASTORE;
    }

    public void commit() {
        if (Datasource.CommitMode.DATASTORE.equals(getCommitMode())) {
            final DataService service = getDataService();
            item = service.commit(item, getView());

            clearCommitLists();
            modified = false;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public View getView() {
        return view;
    }

    public State getState() {
        return state;
    }

    public T getItem() {
        if (State.VALID.equals(state)) return item;

        throw new UnsupportedOperationException();
    }

    public void refresh() {
        // Do Nothing
    }

    public synchronized void setItem(T item) {
        if (State.NOT_INITIALIZAED.equals(this.state)) {
            __setItem(item);
        } else {
            Object prevItem = this.item;
            State prevStatus = this.state;

            __setItem(item);
            state = State.VALID;

            forceStateChanged(prevStatus);
            forceItemChanged(prevItem);
        }
    }

    protected void __setItem(T item) {
        if (this.item != null) {
            detachListener((Instance) this.item);
        }

        if (item instanceof Instance) {
            final MetaClass aClass = ((Instance) item).getMetaClass();
            if (!aClass.equals(metaClass)) {
                throw new IllegalStateException(String.format("Invalid item metaClass"));
            }
            attachListener((Instance) item);
        }

        this.item = item;

        // TODO (abramov) should we clear modified state there?
        this.modified = false;
        clearCommitLists();

        if (PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
            this.modified = true;
        }
    }

    public void invalidate() {
        if (State.NOT_INITIALIZAED != this.state) {
            final State prevStatus = this.state;
            this.state = State.INVALID;
            forceStateChanged(prevStatus);
        }
        modified = false;
        clearCommitLists();
    }

    public void initialized() {
        state = State.INVALID;
    }

    public void commited(Map<Entity, Entity> map) {
        if (map.containsKey(item)) {
            item = (T) map.get(item);
        }

        modified = false;
        clearCommitLists();
    }

    public DatasourceListener getParentDSListener() {
        return null;
    }

}

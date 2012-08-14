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
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import org.apache.commons.lang.StringUtils;

import java.util.Set;

public class DatasourceImpl<T extends Entity>
    extends
        AbstractDatasource<T>
    implements
        DatasourceImplementation<T>
{
    protected DsContext dsContext;
    protected DataService dataservice;

    protected MetaClass metaClass;
    protected View view;

    protected State state = State.NOT_INITIALIZED;
    protected T item;

    private static final long serialVersionUID = -3022228782833455835L;

    public DatasourceImpl(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        this(dsContext, dataservice, id, metaClass,
                StringUtils.isEmpty(viewName) ? null : MetadataProvider.getViewRepository().getView(metaClass, viewName));
    }

    public DatasourceImpl(
            DsContext dsContext, DataService dataservice,
                String id, MetaClass metaClass, View view)
    {
        super(id);
        this.dsContext = dsContext;
        this.dataservice = dataservice;

        this.metaClass = metaClass;
        this.view = view;

    }

    public DsContext getDsContext() {
        return dsContext;
    }

    public DataService getDataService() {
        return dataservice;
    }

    public void commit() {
        if (Datasource.CommitMode.DATASTORE.equals(getCommitMode())) {
            final DataService service = getDataService();
            item = service.commit(item, getView());

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
        return State.VALID.equals(state) ? item : null;
    }

    public void refresh() {
        // Do Nothing
    }

    public synchronized void setItem(T item) {
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

        // TODO (abramov) should we clear modified state there?
        this.modified = false;
        clearCommitLists();

        if (item != null && PersistenceHelper.isNew(item)) {
            itemToCreate.add(item);
            this.modified = true;
        }
    }

    public void invalidate() {
        if (State.NOT_INITIALIZED != state && State.INVALID != state) {
            State prevState = state;
            state = State.INVALID;
            fireStateChanged(prevState);
        }
        modified = false;
        clearCommitLists();
    }

    public void initialized() {
        state = State.INVALID;
    }

    public void valid() {
        state = State.VALID;
    }

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

    public DatasourceListener getParentDSListener() {
        return null;
    }

}

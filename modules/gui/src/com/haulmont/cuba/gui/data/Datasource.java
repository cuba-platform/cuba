/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 25.12.2008 11:06:47
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.entity.Entity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Root of datasources abstraction layer. Contains one entity instance.
 * @param <T> type of entity this datasource is working with
 */
public interface Datasource<T extends Entity> extends Serializable {

    /** Datasource ID as defined in XML descriptor */
    String getId();

    /** Enclosing DsContext  */
    DsContext getDsContext();

    DataService getDataService();

    /** True if the datasource contains uncommitted changes */
    boolean isModified();

    /** Where to commit changes */
    enum CommitMode {
        NOT_SUPPORTED,
        DATASTORE,
        PARENT
    }

    /** Where to commit changes */
    CommitMode getCommitMode();

    /** Performs commit */
    void commit();

    /** Possible states of datasource: {@link State#NOT_INITIALIZED}, {@link State#INVALID}, {@link State#VALID} */
    enum State {
        /** Datasource is just created */
        NOT_INITIALIZED,
        /** The whole {@link DsContext} is created but this datasources is not yet refreshed */
        INVALID,
        /** Datasource is refreshed and may contain data */
        VALID
    }

    /** Current state */
    State getState();

    /** Current item */
    T getItem();

    /** Set current item */
    void setItem(T item);

    /**
     * Clears internal data and sets the datasource in {@link State#INVALID} state.
     * In {@link State#NOT_INITIALIZED} does nothing.
     */
    void invalidate();

    /**
     * Refreshes the datasource moving it to the {@link State#VALID} state
     */
    void refresh();

    /** MetaClass of the enclosed entity */
    MetaClass getMetaClass();

    /** View which was used to load the data */
    View getView();

    /** Add listener to datasource events */
    void addListener(DatasourceListener<T> listener);
    /** Remove listener to datasource events */
    void removeListener(DatasourceListener<T> listener);
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;

/**
 * Root of datasources abstraction layer. Contains one entity instance.
 * @param <T> type of entity this datasource is working with
 *
 * @author abramov
 * @version $Id$
 */
public interface Datasource<T extends Entity> {

    /** Where to commit changes */
    enum CommitMode {
        DATASTORE,
        PARENT
    }

    /** Possible states of datasource: {@link State#NOT_INITIALIZED}, {@link State#INVALID}, {@link State#VALID} */
    enum State {
        /** Datasource is just created */
        NOT_INITIALIZED,
        /** The whole {@link DsContext} is created but this datasources is not yet refreshed */
        INVALID,
        /** Datasource is refreshed and may contain data */
        VALID
    }

    /**
     * Setup the datasource right after creation.
     * This method should be called only once.
     *
     * @param dsContext     DsContext instance
     * @param dataSupplier  DataSupplier instance
     * @param id            datasource ID
     * @param metaClass     MetaClass of an entity that will be stored in this datasource
     * @param view          a view that will be used to load entities form DB, can be null
     * @throws UnsupportedOperationException    if an implementation doesn't support this method. This is the case
     * for example for {@link NestedDatasource} implementors, that have their own setup method.
     */
    void setup(DsContext dsContext, DataSupplier dataSupplier, String id,
               MetaClass metaClass, @Nullable View view) throws UnsupportedOperationException;

    /**
     * @return this datasource ID as defined in XML descriptor
     */
    String getId();

    /**
     * @return enclosing DsContext
     */
    DsContext getDsContext();

    /**
     * @return a DataSupplier that is used to work with Middleware
     */
    DataSupplier getDataSupplier();

    /**
     * @return true if the datasource contains uncommitted changes
     */
    boolean isModified();

    /**
     * @return true if this datasource can commit data to the database
     */
    boolean isAllowCommit();

    /**
     * Switch on/off ability to commit.
     * If disabled, {@link #isModified()} always returns false and {@link #commit()} has no effect.
     */
    void setAllowCommit(boolean allowCommit);

    /**
     * @return where to commit changes
     */
    CommitMode getCommitMode();

    /**
     * Performs commit
     */
    void commit();

    /**
     * @return current state
     */
    State getState();

    /**
     * @return current entity contained in the datasource
     */
    T getItem();

    /**
     * @return current entity contained in the datasource or null if state is not VALID
     */
    @Nullable
    T getItemIfValid();

    /**
     * Set current entity in the datasource.
     * @param item  entity instance
     */
    void setItem(@Nullable T item);

    /**
     * Clears internal data and sets the datasource in {@link State#INVALID} state.
     * In {@link State#NOT_INITIALIZED} does nothing.
     */
    void invalidate();

    /**
     * Refreshes the datasource moving it to the {@link State#VALID} state
     */
    void refresh();

    /**
     * @return MetaClass of an entity contained in the datasource
     */
    MetaClass getMetaClass();

    /**
     * @return View that is used to load entities form DB, can be null
     */
    @Nullable
    View getView();

    /**
     * Add listener to datasource events
     */
    @Deprecated
    void addListener(DatasourceListener<T> listener);

    /**
     * Remove listener to datasource events
     */
    @Deprecated
    void removeListener(DatasourceListener<T> listener);

    class ItemChangeEvent<T extends Entity> {
        private final Datasource<T> ds;
        private final T prevItem;
        private final T item;

        public ItemChangeEvent(Datasource<T> ds, T prevItem, T item) {
            this.ds = ds;
            this.prevItem = prevItem;
            this.item = item;
        }

        public Datasource<T> getDs() {
            return ds;
        }

        @Nullable
        public T getItem() {
            return item;
        }

        @Nullable
        public T getPrevItem() {
            return prevItem;
        }
    }

    interface ItemChangeListener<T extends Entity> {
        void itemChanged(ItemChangeEvent<T> e);
    }

    void addItemChangeListener(ItemChangeListener<T> listener);
    void removeItemChangeListener(ItemChangeListener<T> listener);

    class StateChangeEvent<T extends Entity> {
        private final Datasource<T> ds;
        private final Datasource.State prevState;
        private final Datasource.State state;

        public StateChangeEvent(Datasource<T> ds, State prevState, State state) {
            this.ds = ds;
            this.prevState = prevState;
            this.state = state;
        }

        public Datasource<T> getDs() {
            return ds;
        }

        public State getPrevState() {
            return prevState;
        }

        public State getState() {
            return state;
        }
    }

    interface StateChangeListener<T extends Entity> {
        void stateChanged(StateChangeEvent<T> e);
    }

    void addStateChangeListener(StateChangeListener<T> listener);
    void removeStateChangeListener(StateChangeListener<T> listener);

    class ItemPropertyChangeEvent<T extends Entity> {
        private final Datasource<T> ds;
        private final T item;
        private final String property;
        private final Object prevValue;
        private final Object value;

        public ItemPropertyChangeEvent(Datasource<T> ds, T item, String property, Object prevValue, Object value) {
            this.ds = ds;
            this.item = item;
            this.property = property;
            this.prevValue = prevValue;
            this.value = value;
        }

        public Datasource<T> getDs() {
            return ds;
        }

        public T getItem() {
            return item;
        }

        public String getProperty() {
            return property;
        }

        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        @Nullable
        public Object getValue() {
            return value;
        }
    }

    interface ItemPropertyChangeListener<T extends Entity> {
        void itemPropertyChanged(ItemPropertyChangeEvent<T> e);
    }

    void addItemPropertyChangeListener(ItemPropertyChangeListener<T> listener);
    void removeItemPropertyChangeListener(ItemPropertyChangeListener<T> listener);

    /**
     * @return whether to load dynamic attributes
     */
    boolean getLoadDynamicAttributes();

    /**
     * @param value whether to load dynamic attributes
     */
    void setLoadDynamicAttributes(boolean value);
}
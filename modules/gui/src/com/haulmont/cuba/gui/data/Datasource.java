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
package com.haulmont.cuba.gui.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.annotation.Nullable;

/**
 * Root of datasources abstraction layer. Contains one entity instance.
 * @param <T> type of entity this datasource is working with
 *
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
     * Add listener to datasource events.
     *
     * @deprecated See new methods <br/>
     * {@link #addItemChangeListener(ItemChangeListener)} <br/>
     * {@link #addItemPropertyChangeListener(ItemPropertyChangeListener)} <br/>
     * {@link #addStateChangeListener(StateChangeListener)} <br/>
     * {@link CollectionDatasource#addCollectionChangeListener(CollectionDatasource.CollectionChangeListener)}
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

        /**
         * @return datasource
         */
        public Datasource<T> getDs() {
            return ds;
        }

        /**
         * @return current item
         */
        @Nullable
        public T getItem() {
            return item;
        }

        /**
         * @return previous selected item
         */
        @Nullable
        public T getPrevItem() {
            return prevItem;
        }
    }

    /**
     * Listener to datasource item change events.
     */
    interface ItemChangeListener<T extends Entity> {
        /**
         * Current item changed, that is now {@link com.haulmont.cuba.gui.data.Datasource#getItem()} returns a different
         * instance.
         */
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

        /**
         * @return datasource
         */
        public Datasource<T> getDs() {
            return ds;
        }

        /**
         * @return previous state
         */
        public State getPrevState() {
            return prevState;
        }

        /**
         * @return current state
         */
        public State getState() {
            return state;
        }
    }

    /**
     * Listener to datasource state change events.
     */
    interface StateChangeListener<T extends Entity> {
        /**
         * Datasource state changed.
         */
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

        /**
         * @return datasource
         */
        public Datasource<T> getDs() {
            return ds;
        }

        /**
         * @return item, which property value is changed
         */
        public T getItem() {
            return item;
        }

        /**
         * @return property name
         */
        public String getProperty() {
            return property;
        }

        /**
         * @return previous value of item property
         */
        @Nullable
        public Object getPrevValue() {
            return prevValue;
        }

        /**
         * @return current value of item property
         */
        @Nullable
        public Object getValue() {
            return value;
        }
    }

    /**
     * Listener to datasource item property value change events.
     */
    interface ItemPropertyChangeListener<T extends Entity> {
        /**
         * Property value of some datasource item changed. In case of {@link CollectionDatasource} this method may be
         * called for any item of collection if its property value changed.
         */
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
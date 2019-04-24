/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.model;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.screen.InstallSubject;
import com.haulmont.cuba.gui.screen.Subscribe;

import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Loader of {@link KeyValueEntity} collections.
 */
@InstallSubject("loadDelegate")
public interface KeyValueCollectionLoader extends BaseCollectionLoader {

    /**
     * Returns the container which accepts loaded entities.
     */
    KeyValueCollectionContainer getContainer();

    /**
     * Sets the container which accepts loaded entities.
     */
    void setContainer(KeyValueCollectionContainer container);

    /**
     * Returns {@code ValueLoadContext} which is created by the parameters of this loader. The {@code ValueLoadContext}
     * can be used with {@code DataManager} to load data by the same conditions.
     */
    ValueLoadContext createLoadContext();

    /**
     * Returns data store name.
     */
    String getStoreName();

    /**
     * Sets the data store name. By default, the main data store is used.
     */
    void setStoreName(String name);

    /**
     * Returns a function which will be used to load data instead of standard implementation.
     */
    Function<ValueLoadContext, List<KeyValueEntity>> getDelegate();

    /**
     * Sets a function which will be used to load data instead of standard implementation.
     */
    void setLoadDelegate(Function<ValueLoadContext, List<KeyValueEntity>> delegate);

    /**
     * Event sent before loading entities.
     * <p>
     * You can prevent load using the {@link #preventLoad()} method of the event, for example:
     * <pre>
     *     &#64;Subscribe(id = "fooDl", target = Target.DATA_LOADER)
     *     private void onFooDlPreLoad(KeyValueCollectionLoader.PreLoadEvent event) {
     *         if (doNotLoad()) {
     *             event.preventLoad();
     *         }
     *     }
     * </pre>
     *
     * @see #addPreLoadListener(Consumer)
     */
    class PreLoadEvent extends EventObject {

        private final ValueLoadContext loadContext;
        private boolean loadPrevented;

        public PreLoadEvent(KeyValueCollectionLoader loader, ValueLoadContext loadContext) {
            super(loader);
            this.loadContext = loadContext;
        }

        /**
         * The data loader which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public KeyValueCollectionLoader getSource() {
            return (KeyValueCollectionLoader) super.getSource();
        }

        /**
         * Returns the load context of the current data loader.
         */
        public ValueLoadContext getLoadContext() {
            return loadContext;
        }

        /**
         * Invoke this method if you want to abort the loading.
         */
        public void preventLoad() {
            loadPrevented = true;
        }

        /**
         * Returns true if {@link #preventLoad()} method was called and loading will be aborted.
         */
        public boolean isLoadPrevented() {
            return loadPrevented;
        }
    }

    /**
     * Adds a listener to {@link PreLoadEvent}.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe(id = "fooDl", target = Target.DATA_LOADER)
     *     private void onFooDlPreLoad(KeyValueCollectionLoader.PreLoadEvent event) {
     *         // handle event here
     *     }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    Subscription addPreLoadListener(Consumer<PreLoadEvent> listener);

    /**
     * Event sent after successful loading of entities and setting to
     * the container.
     *
     * @see #addPostLoadListener(Consumer)
     */
    class PostLoadEvent extends EventObject {

        private final List<KeyValueEntity> loadedEntities;

        public PostLoadEvent(KeyValueCollectionLoader loader, List<KeyValueEntity> loadedEntities) {
            super(loader);
            this.loadedEntities = loadedEntities;
        }

        /**
         * The data loader which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public KeyValueCollectionLoader getSource() {
            return (KeyValueCollectionLoader) super.getSource();
        }

        /**
         * Returns the list of loaded entities.
         */
        public List<KeyValueEntity> getLoadedEntities() {
            return loadedEntities;
        }
    }

    /**
     * Adds a listener to {@link PostLoadEvent}.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe(id = "fooDl", target = Target.DATA_LOADER)
     *     private void onFooDlPostLoad(KeyValueCollectionLoader.PostLoadEvent event) {
     *         // handle event here
     *     }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    Subscription addPostLoadListener(Consumer<PostLoadEvent> listener);
}

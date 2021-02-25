/*
 * Copyright (c) 2008-2017 Haulmont.
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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.screen.InstallSubject;
import com.haulmont.cuba.gui.screen.Subscribe;

import javax.annotation.Nullable;
import java.util.EventObject;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Loader of a single entity instance.
 */
@InstallSubject("loadDelegate")
public interface InstanceLoader<E extends Entity> extends DataLoader {

    /**
     * Returns the container which accepts the loaded entity.
     */
    InstanceContainer<E> getContainer();

    /**
     * Sets the container which accepts the loaded entity.
     */
    void setContainer(InstanceContainer<E> container);

    /**
     * Returns id of the entity to load.
     */
    @Nullable
    Object getEntityId();

    /**
     * Sets the id of the entity to load.
     */
    void setEntityId(Object entityId);

    /**
     * Returns the view which is used when loading.
     */
    View getView();

    /**
     * Sets the view which is used when loading.
     */
    void setView(View view);

    /**
     * Sets the name of the view which is used when loading.
     * @throws IllegalStateException if the view has already been set by {@link #setView(View)}
     */
    void setView(String viewName);

    /**
     * Returns true if the entity's dynamic attributes are loaded.
     */
    boolean isLoadDynamicAttributes();

    /**
     * Set to true to load the entity's dynamic attributes. Dynamic attributes are not loaded by default.
     */
    void setLoadDynamicAttributes(boolean loadDynamicAttributes);

    /**
     * Returns a function which will be used to load data instead of standard implementation.
     */
    Function<LoadContext<E>, E> getLoadDelegate();

    /**
     * Sets a function which will be used to load data instead of standard implementation.
     */
    void setLoadDelegate(Function<LoadContext<E>, E> delegate);

    /**
     * Event sent before loading an entity instance.
     * <p>
     * You can prevent load using the {@link #preventLoad()} method of the event, for example:
     * <pre>
     *     &#64;Subscribe(id = "fooDl", target = Target.DATA_LOADER)
     *     private void onFooDlPreLoad(InstanceLoader.PreLoadEvent event) {
     *         if (doNotLoad()) {
     *             event.preventLoad();
     *         }
     *     }
     * </pre>
     *
     * @see #addPreLoadListener(Consumer)
     */
    class PreLoadEvent<T extends Entity> extends EventObject {

        private final LoadContext<T> loadContext;
        private boolean loadPrevented;

        public PreLoadEvent(InstanceLoader<T> loader, LoadContext<T> loadContext) {
            super(loader);
            this.loadContext = loadContext;
        }

        /**
         * The data loader which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public InstanceLoader<T> getSource() {
            return (InstanceLoader<T>) super.getSource();
        }

        /**
         * Returns the load context of the current data loader.
         */
        public LoadContext<T> getLoadContext() {
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
     *     private void onFooDlPreLoad(InstanceLoader.PreLoadEvent event) {
     *         // handle event here
     *     }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    Subscription addPreLoadListener(Consumer<PreLoadEvent> listener);

    /**
     * Event sent after successful loading of an entity instance, merging it into {@code DataContext} and setting to
     * the container.
     *
     * @see #addPostLoadListener(Consumer)
     */
    class PostLoadEvent<T extends Entity> extends EventObject {

        private final T loadedEntity;

        public PostLoadEvent(InstanceLoader<T> loader, T loadedEntity) {
            super(loader);
            this.loadedEntity = loadedEntity;
        }

        /**
         * The data loader which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public InstanceLoader<T> getSource() {
            return (InstanceLoader<T>) super.getSource();
        }

        /**
         * Returns the loaded entity instance.
         */
        public T getLoadedEntity() {
            return loadedEntity;
        }
    }

    /**
     * Adds a listener to {@link PostLoadEvent}.
     * <p>
     * You can also add an event listener declaratively using a controller method annotated with {@link Subscribe}:
     * <pre>
     *    &#64;Subscribe(id = "fooDl", target = Target.DATA_LOADER)
     *     private void onFooDlPostLoad(InstanceLoader.PostLoadEvent event) {
     *         // handle event here
     *     }
     * </pre>
     *
     * @param listener listener
     * @return subscription
     */
    Subscription addPostLoadListener(Consumer<PostLoadEvent> listener);
}
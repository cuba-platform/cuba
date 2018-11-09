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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;

/**
 * Container that holds a collection of entity instances.
 */
public interface CollectionContainer<E extends Entity> extends InstanceContainer<E> {

    /**
     * Returns immutable list of contained entities.
     */
    List<E> getItems();

    /**
     * Sets a collection of entities to the container.
     */
    void setItems(@Nullable Collection<E> entities);

    /**
     * Returns mutable list of contained entities. Changes in the list produce {@link CollectionChangeEvent}.
     */
    List<E> getMutableItems();

    /**
     * Returns entity by its id.
     *
     * @throws IllegalArgumentException if the container doesn't have an entity with the given id
     */
    @Nonnull
    E getItem(Object entityId);

    /**
     * Returns entity by its id or null if the container doesn't have an entity with the given id.
     */
    @Nullable
    E getItemOrNull(Object entityId);

    /**
     * Returns the index in the items list of the entity with the given id or -1 if there is no such entity.
     */
    int getItemIndex(Object entityId);

    /**
     * Returns true if an item with the given id exists in the container.
     */
    boolean containsItem(Object entityId);

    /**
     * If the item with the same id exists in the container, it is replaced with the given instance. If not, the given
     * instance is added to the items list.
     * <p>
     * Sends {@link CollectionChangeEvent} of the {@code SET_ITEM} or {@code ADD_ITEMS} type.
     */
    void replaceItem(E entity);

    /**
     * Returns sorter object currently set for the container.
     */
    Sorter getSorter();

    /**
     * Sets sorter object.
     */
    void setSorter(Sorter sorter);

    /**
     * Enables all event listeners. Events fired on this call depend on the passed {@code mode}.
     *
     * @param mode mode
     */
    void unmute(UnmuteEventsMode mode);

    /**
     * Adds listener to {@link CollectionChangeEvent}.
     */
    Subscription addCollectionChangeListener(Consumer<CollectionChangeEvent<E>> listener);

    enum UnmuteEventsMode {
        /**
         * No events are raised on unmute.
         */
        SILENT,

        /**
         * Fire a {@link CollectionChangeEvent} with {@link CollectionChangeType#REFRESH} type on unmute.
         */
        FIRE_REFRESH_EVENT
    }

    /**
     * Event sent on changes in the container items collection - adding, removing, replacing elements.
     */
    class CollectionChangeEvent<T extends Entity> extends EventObject {

        private final CollectionChangeType changeType;
        private final Collection<? extends T> changes;

        public CollectionChangeEvent(CollectionContainer<T> container,
                                     CollectionChangeType changeType,
                                     Collection<? extends T> changes) {
            super(container);
            this.changeType = changeType;
            this.changes = changes;
        }

        /**
         * Returns the container which sent the event.
         */
        @SuppressWarnings("unchecked")
        @Override
        public CollectionContainer<T> getSource() {
            return (CollectionContainer) super.getSource();
        }

        /**
         * Returns the type of change.
         */
        public CollectionChangeType getChangeType() {
            return changeType;
        }

        /**
         * Returns changed items. If {@link #getChangeType()} is {@link CollectionChangeType#REFRESH}, the method
         * returns empty collection.
         */
        public Collection<? extends T> getChanges() {
            return changes;
        }

        @Override
        public String toString() {
            return "CollectionChangeEvent{" +
                    "source=" + source +
                    ", changeType=" + changeType +
                    ", changes=" + changes +
                    '}';
        }
    }
}
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

import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Container that holds a collection of entity instances which is a collection property of another entity.
 */
public interface CollectionPropertyContainer<E extends Entity> extends CollectionContainer<E>, Nested {

    /**
     * Returns mutable list of entities. Changes in the list are reflected in the underlying property.
     */
    @Override
    List<E> getMutableItems();

    /**
     * Same as {@link #getMutableItems()} but changes in the list are <b>not</b> reflected in the underlying property.
     */
    List<E> getDisconnectedItems();

    /**
     * Same as {@link #setItems(Collection)} but does <b>not</b> affect the underlying property.
     */
    void setDisconnectedItems(@Nullable Collection<E> entities);
}

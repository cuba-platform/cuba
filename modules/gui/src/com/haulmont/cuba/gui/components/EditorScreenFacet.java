/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.builders.EditMode;
import com.haulmont.cuba.gui.meta.*;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.Screen;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Prepares and shows editor screens.
 */
@StudioFacet(
        caption = "Editor Screen",
        description = "Prepares and shows editor screens"
)
@StudioProperties(
        properties = {
                @StudioProperty(name = "id", required = true)
        }
)
public interface EditorScreenFacet<E extends Entity, S extends Screen & EditorScreen<E>>
        extends ScreenFacet<S>, EntityAwareScreenFacet<E> {

    /**
     * Sets {@link EditMode} to use in editor.
     *
     * @param editMode edit mode
     */
    @StudioProperty(type = PropertyType.ENUMERATION)
    void setEditMode(EditMode editMode);

    /**
     * @return editor edit mode
     */
    EditMode getEditMode();

    /**
     * Defines whether a new item will be added to the beginning or to the end of collection. Affects only standalone
     * containers, for nested containers new items are always added to the end.
     *
     * @param addFirst add first
     */
    @StudioProperty(type = PropertyType.BOOLEAN)
    void setAddFirst(boolean addFirst);

    /**
     * @return whether a new item will be added to the beginning or to the end
     */
    boolean getAddFirst();

    /**
     * Sets entity provider.
     *
     * @param entityProvider entity provider
     */
    @StudioDelegate
    void setEntityProvider(Supplier<E> entityProvider);

    /**
     * @return entity provider
     */
    Supplier<E> getEntityProvider();

    /**
     * Sets code to initialize a new entity instance.
     * <p>
     * The initializer is invoked only when {@link EditMode} is {@code CREATE}.
     */
    @StudioDelegate
    void setInitializer(Consumer<E> initializer);

    /**
     * @return entity initializer
     */
    Consumer<E> getInitializer();

    /**
     * Sets parent {@link DataContext} supplier for the editor screen.
     * <p>
     * The screen will commit data to the parent context instead of directly to {@code DataManager}.
     */
    @StudioDelegate
    void setParentDataContextProvider(Supplier<DataContext> parentDataContextProvider);

    /**
     * @return parent DataContext provider
     */
    Supplier<DataContext> getParentDataContextProvider();

    /**
     * Sets code to transform the edited entity after editor commit.
     * <p>
     * Applied only if either field or container or listComponent is assigned.
     *
     * @param transformation transformation
     */
    @StudioDelegate
    void setTransformation(Function<E, E> transformation);
}

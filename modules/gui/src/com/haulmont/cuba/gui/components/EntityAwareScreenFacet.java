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
import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioProperty;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Nested;

/**
 * Interface for entity aware screen facets.
 *
 * @param <E> entity type
 *
 * @see EditorScreenFacet
 * @see LookupScreenFacet
 */
public interface EntityAwareScreenFacet<E extends Entity> {

    /**
     * Sets entity class.
     *
     * @param entityClass entity class
     */
    @StudioProperty(type = PropertyType.JAVA_CLASS_NAME)
    void setEntityClass(Class<E> entityClass);

    /**
     * @return entity class
     */
    Class<E> getEntityClass();

    /**
     * Sets list component.
     * <p>
     * The component is used to get the {@code container} if it is not set explicitly by
     * {@link #setContainer(CollectionContainer)} method.
     * <p>
     * Usually, the list component is a {@code Table} or {@code DataGrid} displaying the list of entities.
     */
    @StudioProperty(type = PropertyType.COMPONENT_REF)
    void setListComponent(ListComponent<E> listComponent);

    /**
     * @return list component
     */
    ListComponent<E> getListComponent();

    /**
     * Sets the {@link PickerField} component.
     * <p>
     * If the field is set, the framework sets the committed entity to the field after successful editor commit.
     */
    @StudioProperty(type = PropertyType.COMPONENT_REF)
    void setPickerField(PickerField<E> pickerField);

    /**
     * @return {@link PickerField}
     */
    PickerField<E> getPickerField();

    /**
     * Sets {@link CollectionContainer}.
     * <p>
     * The container is updated after the screen is committed. If the container is {@link Nested},
     * the framework automatically initializes the reference to the parent entity and sets up data contexts
     * for editing compositions.
     */
    @StudioProperty(type = PropertyType.DATACONTAINER_REF)
    void setContainer(CollectionContainer<E> container);

    /**
     * @return {@link CollectionContainer}
     */
    CollectionContainer<E> getContainer();
}

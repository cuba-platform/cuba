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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.EntityAwareScreenFacet;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.Screen;

public abstract class WebAbstractEntityAwareScreenFacet<E extends Entity, S extends Screen>
        extends WebAbstractScreenFacet<S>
        implements EntityAwareScreenFacet<E> {

    protected Class<E> entityClass;

    protected PickerField<E> pickerField;
    protected ListComponent<E> listComponent;
    protected CollectionContainer<E> container;

    @Override
    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public void setListComponent(ListComponent<E> listComponent) {
        this.listComponent = listComponent;
    }

    @Override
    public ListComponent<E> getListComponent() {
        return listComponent;
    }

    @Override
    public void setPickerField(PickerField<E> pickerField) {
        this.pickerField = pickerField;
    }

    @Override
    public PickerField<E> getPickerField() {
        return pickerField;
    }

    @Override
    public void setContainer(CollectionContainer<E> container) {
        this.container = container;
    }

    @Override
    public CollectionContainer<E> getContainer() {
        return container;
    }
}

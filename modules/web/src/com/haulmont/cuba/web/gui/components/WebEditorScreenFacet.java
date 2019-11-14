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
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.builders.EditMode;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.EditorScreenFacet;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.Screen;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class WebEditorScreenFacet<E extends Entity, S extends Screen & EditorScreen<E>>
        extends WebAbstractEntityAwareScreenFacet<E, S>
        implements EditorScreenFacet<E, S> {

    protected Supplier<E> entityProvider;

    protected Supplier<DataContext> parentDataContextProvider;
    protected Consumer<E> initializer;
    protected Function<E, E> transformation;

    protected EditMode editMode = EditMode.CREATE;
    protected boolean addFirst = false;

    @Override
    public void setEntityProvider(Supplier<E> entityProvider) {
        this.entityProvider = entityProvider;
    }

    @Override
    public Supplier<E> getEntityProvider() {
        return entityProvider;
    }

    @Override
    public void setInitializer(Consumer<E> initializer) {
        this.initializer = initializer;
    }

    @Override
    public Consumer<E> getInitializer() {
        return initializer;
    }

    @Override
    public void setParentDataContextProvider(Supplier<DataContext> parentDataContextProvider) {
        this.parentDataContextProvider = parentDataContextProvider;
    }

    @Override
    public Supplier<DataContext> getParentDataContextProvider() {
        return parentDataContextProvider;
    }

    @Override
    public void setTransformation(Function<E, E> transformation) {
        this.transformation = transformation;
    }

    @Override
    public void setEditMode(EditMode editMode) {
        this.editMode = editMode;
    }

    @Override
    public void setAddFirst(boolean addFirst) {
        this.addFirst = addFirst;
    }

    @Override
    public EditMode getEditMode() {
        return editMode;
    }

    @Override
    public boolean getAddFirst() {
        return addFirst;
    }

    @Override
    public S create() {
        Frame owner = getOwner();
        if (owner == null) {
            throw new IllegalStateException("Screen facet is not attached to Frame");
        }

        EditorBuilder<E> editorBuilder = createEditorBuilder(owner, getEntityToEdit());

        screen = createScreen(editorBuilder);

        initScreenListeners(screen);
        injectScreenProperties(screen, properties);

        return screen;
    }

    @Override
    public S show() {
        return (S) create().show();
    }

    protected S createScreen(EditorBuilder<E> builder) {
        return (S) builder
                .withListComponent(listComponent)
                .withField(pickerField)
                .withContainer(container)
                .withAddFirst(addFirst)
                .withScreenId(screenId)
                .withLaunchMode(launchMode)
                .withOptions(getScreenOptions())
                .withInitializer(initializer)
                .withTransformation(transformation)
                .withParentDataContext(getParentDataContext())
                .build();
    }

    @SuppressWarnings("unchecked")
    protected EditorBuilder<E> createEditorBuilder(Frame owner, @Nullable E entityToEdit) {
        EditorBuilder<E> builder;

        ScreenBuilders screenBuilders = beanLocator.get(ScreenBuilders.class);

        if (entityClass != null) {
            builder = screenBuilders.editor(entityClass, owner.getFrameOwner());
        } else if (entityToEdit != null) {
            builder = (EditorBuilder<E>) screenBuilders.editor(entityToEdit.getClass(), owner.getFrameOwner());
        } else if (listComponent != null) {
            builder = screenBuilders.editor(listComponent);
        } else if (pickerField != null) {
            builder = screenBuilders.editor(pickerField);
        } else {
            throw new IllegalStateException(
                    "Unable to create EditorScreen Facet. At least one of entityClass, listComponent or field must be specified");
        }

        if (editMode == EditMode.CREATE) {
            builder.newEntity(entityToEdit);
        } else {
            if (entityToEdit != null) {
                builder.editEntity(entityToEdit);
            } else {
                throw new DevelopmentException("No entity to edit is passed for EditorScreen");
            }
        }

        if (screenClass != null) {
            builder.withScreenClass(screenClass);
        }

        return builder;
    }

    @Nullable
    protected E getEntityToEdit() {
        E entity = null;

        if (entityProvider != null) {
            entity = entityProvider.get();
        }

        if (entity == null
                && listComponent != null) {
            entity = listComponent.getSingleSelected();
        }

        if (entity == null
                && pickerField != null) {
            entity = pickerField.getValue();
        }

        return entity;
    }

    protected DataContext getParentDataContext() {
        return parentDataContextProvider != null
                ? parentDataContextProvider.get()
                : null;
    }
}

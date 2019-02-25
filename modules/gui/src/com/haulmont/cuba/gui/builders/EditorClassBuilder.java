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

package com.haulmont.cuba.gui.builders;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Editor screen builder that knows the concrete screen class. It's {@link #build()} method returns that class.
 */
public class EditorClassBuilder<E extends Entity, S extends Screen & EditorScreen<E>> extends EditorBuilder<E> {

    protected Class<S> screenClass;
    protected Consumer<AfterScreenCloseEvent<S>> closeListener;

    public EditorClassBuilder(EditorBuilder<E> builder, Class<S> screenClass) {
        super(builder);

        this.screenClass = screenClass;
    }

    @Override
    public EditorClassBuilder<E, S> newEntity() {
        super.newEntity();
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> editEntity(E entity) {
        super.editEntity(entity);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> newEntity(E entity) {
        super.newEntity(entity);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withContainer(CollectionContainer<E> container) {
        super.withContainer(container);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withInitializer(Consumer<E> initializer) {
        super.withInitializer(initializer);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withAddFirst(boolean addFirst) {
        super.withAddFirst(addFirst);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withLaunchMode(Screens.LaunchMode launchMode) {
        super.withLaunchMode(launchMode);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withOpenMode(OpenMode openMode) {
        super.withOpenMode(openMode);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withParentDataContext(DataContext parentDataContext) {
        super.withParentDataContext(parentDataContext);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withOptions(ScreenOptions options) {
        super.withOptions(options);
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withListComponent(ListComponent<E> listComponent) {
        super.withListComponent(listComponent);
        return this;
    }

    @Override
    public EditorBuilder<E> withScreenId(String screenId) {
        throw new IllegalStateException("EditorClassBuilder does not support screenId");
    }

    /**
     * Adds {@link Screen.AfterCloseEvent} listener to the screen.
     *
     * @param listener listener
     */
    public EditorClassBuilder<E, S> withAfterCloseListener(Consumer<AfterScreenCloseEvent<S>> listener) {
        this.closeListener = listener;
        return this;
    }

    @Override
    public EditorClassBuilder<E, S> withTransformation(Function<E, E> transformation) {
        super.withTransformation(transformation);
        return this;
    }

    @Override
    public <T extends HasValue<E>> EditorClassBuilder<E, S> withField(T field) {
        super.withField(field);
        return this;
    }

    /**
     * Returns editor screen class.
     */
    public Class<S> getScreenClass() {
        return screenClass;
    }

    /**
     * Returns screen close listener.
     */
    public Consumer<AfterScreenCloseEvent<S>> getCloseListener() {
        return closeListener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public S build() {
        return (S) handler.apply(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public S show() {
        return (S) super.show();
    }
}
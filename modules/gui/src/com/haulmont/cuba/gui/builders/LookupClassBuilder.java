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
import com.haulmont.cuba.gui.screen.LookupScreen;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Lookup screen builder that knows the concrete screen class. It's {@link #build()} method returns that class.
 */
public class LookupClassBuilder<E extends Entity, S extends Screen & LookupScreen<E>> extends LookupBuilder<E> {

    protected Class<S> screenClass;
    protected Consumer<AfterScreenCloseEvent<S>> closeListener;

    public LookupClassBuilder(LookupBuilder<E> builder, Class<S> screenClass) {
        super(builder);

        this.screenClass = screenClass;
    }

    @Override
    public LookupClassBuilder<E, S> withLaunchMode(Screens.LaunchMode launchMode) {
        super.withLaunchMode(launchMode);
        return this;
    }

    @Override
    public LookupClassBuilder<E, S> withOpenMode(OpenMode openMode) {
        super.withOpenMode(openMode);
        return this;
    }

    @Override
    public LookupClassBuilder<E, S> withOptions(ScreenOptions options) {
        super.withOptions(options);
        return this;
    }

    @Override
    public LookupClassBuilder<E, S> withSelectValidator(Predicate<LookupScreen.ValidationContext<E>> selectValidator) {
        super.withSelectValidator(selectValidator);
        return this;
    }

    @Override
    public LookupClassBuilder<E, S> withSelectHandler(Consumer<Collection<E>> selectHandler) {
        super.withSelectHandler(selectHandler);
        return this;
    }

    @Override
    public <T extends HasValue<E>> LookupClassBuilder<E, S> withField(T field) {
        super.withField(field);
        return this;
    }

    @Override
    public LookupBuilder<E> withScreenId(String screenId) {
        throw new IllegalStateException("LookupClassBuilder does not support screenId");
    }

    @Override
    public LookupClassBuilder<E, S> withListComponent(ListComponent<E> target) {
        super.withListComponent(target);
        return this;
    }

    @Override
    public LookupClassBuilder<E, S> withContainer(CollectionContainer<E> container) {
        super.withContainer(container);
        return this;
    }

    /**
     * Adds {@link Screen.AfterCloseEvent} listener to the screen.
     *
     * @param listener listener
     */
    public LookupClassBuilder<E, S> withAfterCloseListener(Consumer<AfterScreenCloseEvent<S>> listener) {
        this.closeListener = listener;
        return this;
    }

    @Override
    public LookupClassBuilder<E, S> withTransformation(Function<Collection<E>, Collection<E>> transformation) {
        super.withTransformation(transformation);
        return this;
    }

    /**
     * Returns lookup screen class.
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
        return (S) this.handler.apply(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public S show() {
        return (S) super.show();
    }
}
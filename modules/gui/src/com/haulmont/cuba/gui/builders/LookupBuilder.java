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
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.screen.*;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Lookup screen builder that is not aware of concrete screen class. It's {@link #build()} method returns {@link Screen}.
 */
public class LookupBuilder<E extends Entity> {

    protected final FrameOwner origin;
    protected final Class<E> entityClass;
    protected final Function<LookupBuilder<E>, Screen> handler;

    protected Predicate<LookupScreen.ValidationContext<E>> selectValidator;
    protected Function<Collection<E>, Collection<E>> transformation;
    protected Consumer<Collection<E>> selectHandler;
    protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
    protected ScreenOptions options = FrameOwner.NO_OPTIONS;
    protected CollectionContainer<E> container;

    protected String screenId;
    protected ListComponent<E> listComponent;
    protected HasValue<E> field;

    public LookupBuilder(LookupBuilder<E> builder) {
        this.entityClass = builder.entityClass;
        this.origin = builder.origin;
        this.handler = builder.handler;

        this.launchMode = builder.launchMode;
        this.options = builder.options;
        this.selectHandler = builder.selectHandler;
        this.selectValidator = builder.selectValidator;
        this.field = builder.field;
        this.listComponent = builder.listComponent;
        this.container = builder.container;
        this.screenId = builder.screenId;
        this.transformation = builder.transformation;
    }

    public LookupBuilder(FrameOwner origin, Class<E> entityClass, Function<LookupBuilder<E>, Screen> handler) {
        this.origin = origin;
        this.entityClass = entityClass;
        this.handler = handler;
    }

    /**
     * Sets {@link Screens.LaunchMode} for the lookup screen and returns the builder for chaining.
     * <p>For example: {@code builder.withLaunchMode(OpenMode.DIALOG).build();}
     */
    public LookupBuilder<E> withLaunchMode(Screens.LaunchMode launchMode) {
        checkNotNullArgument(launchMode);

        this.launchMode = launchMode;
        return this;
    }

    /**
     * Sets {@link OpenMode} for the lookup screen and returns the builder for chaining.
     * <p>For example: {@code builder.withOpenMode(OpenMode.DIALOG).build();}
     */
    public LookupBuilder<E> withOpenMode(OpenMode openMode) {
        checkNotNullArgument(openMode);

        this.launchMode = openMode;
        return this;
    }

    /**
     * Sets {@link ScreenOptions} for the lookup screen and returns the builder for chaining.
     */
    public LookupBuilder<E> withOptions(ScreenOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Sets selection validator for the lookup screen and returns the builder for chaining.
     */
    public LookupBuilder<E> withSelectValidator(Predicate<LookupScreen.ValidationContext<E>> selectValidator) {
        this.selectValidator = selectValidator;
        return this;
    }

    /**
     * Sets selection handler for the lookup screen and returns the builder for chaining.
     */
    public LookupBuilder<E> withSelectHandler(Consumer<Collection<E>> selectHandler) {
        this.selectHandler = selectHandler;
        return this;
    }

    /**
     * Sets the field component and returns the builder for chaining.
     * <p>If the field is set, the framework sets the selected entity to the field after successful lookup.
     */
    public <T extends HasValue<E>> LookupBuilder<E> withField(T field) {
        this.field = field;
        return this;
    }

    /**
     * Sets screen class and returns the {@link LookupClassBuilder} for chaining.
     *
     * @param screenClass class of the screen controller
     */
    public <S extends Screen & LookupScreen<E>> LookupClassBuilder<E, S> withScreenClass(Class<S> screenClass) {
        return new LookupClassBuilder<>(this, screenClass);
    }

    /**
     * Sets screen id and returns the builder for chaining.
     *
     * @param screenId  identifier of the lookup screen as specified in the {@code UiController} annotation
     *                  or {@code screens.xml}.
     */
    public LookupBuilder<E> withScreenId(String screenId) {
        this.screenId = screenId;
        return this;
    }

    /**
     * Sets list component and returns the builder for chaining.
     * <p>The component is used to get the {@code container} if it is not set explicitly by
     * {@link #withContainer(CollectionContainer)} method. Usually, the list component is a {@code Table}
     * or {@code DataGrid} displaying the list of entities.
     */
    public LookupBuilder<E> withListComponent(ListComponent<E> target) {
        this.listComponent = target;
        return this;
    }

    /**
     * Sets {@code CollectionContainer} and returns the builder for chaining.
     * <p>The container is updated after the lookup screen is closed. If the container is {@link Nested},
     * the framework automatically initializes the reference to the parent entity and sets up data contexts
     * for added One-To-Many and Many-To-Many relations.
     */
    public LookupBuilder<E> withContainer(CollectionContainer<E> container) {
        this.container = container;
        return this;
    }

    /**
     * Sets code to transform entities after selection and returns the builder for chaining.
     * <br>
     * Applied only if either field or container or listComponent is assigned.
     *
     * @param transformation edited entity transformation
     * @see #withContainer(CollectionContainer)
     * @see #withField(HasValue)
     * @see #withListComponent(ListComponent)
     */
    public LookupBuilder<E> withTransformation(Function<Collection<E>, Collection<E>> transformation) {
        this.transformation = transformation;
        return this;
    }

    /**
     * Returns screen id set by {@link #withScreenId(String)}.
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * Returns launch mode set by {@link #withLaunchMode(Screens.LaunchMode)}.
     */
    public Screens.LaunchMode getLaunchMode() {
        return launchMode;
    }

    /**
     * Returns screen options set by {@link #withOptions(ScreenOptions)}.
     */
    public ScreenOptions getOptions() {
        return options;
    }

    /**
     * Returns invoking screen.
     */
    @Nonnull
    public FrameOwner getOrigin() {
        return origin;
    }

    /**
     * Returns class of the entity to lookup.
     */
    public Class<E> getEntityClass() {
        return entityClass;
    }

    /**
     * Returns selection handler set by {@link #withSelectHandler(Consumer)}.
     */
    public Consumer<Collection<E>> getSelectHandler() {
        return selectHandler;
    }

    /**
     * Returns selection validator set by {@link #withSelectValidator(Predicate)}.
     */
    public Predicate<LookupScreen.ValidationContext<E>> getSelectValidator() {
        return selectValidator;
    }

    /**
     * Returns the field component set by {@link #withField(HasValue)}.
     */
    public HasValue<E> getField() {
        return field;
    }

    /**
     * Returns container set by {@link #withContainer(CollectionContainer)}.
     */
    public CollectionContainer<E> getContainer() {
        return container;
    }

    /**
     * Returns list component set by {@link #withListComponent(ListComponent)}.
     */
    public ListComponent<E> getListComponent() {
        return listComponent;
    }

    public Function<Collection<E>, Collection<E>> getTransformation() {
        return transformation;
    }

    /**
     * Builds the lookup screen. Screen should be shown using {@link Screen#show()}.
     */
    public Screen build() {
        return this.handler.apply(this);
    }

    /**
     * Builds and shows the lookup screen.
     */
    public Screen show() {
        return handler.apply(this)
                .show();
    }
}
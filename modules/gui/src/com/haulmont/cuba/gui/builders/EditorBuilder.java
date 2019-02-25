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
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.screen.*;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * Editor screen builder that is not aware of concrete screen class. It's {@link #build()} method returns {@link Screen}.
 */
public class EditorBuilder<E extends Entity> {

    protected final FrameOwner origin;
    protected final Class<E> entityClass;
    protected final Function<EditorBuilder<E>, Screen> handler;

    protected E newEntity;
    protected E editedEntity;
    protected CollectionContainer<E> container;
    protected Consumer<E> initializer;
    protected Function<E, E> transformation;
    protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
    protected ScreenOptions options = FrameOwner.NO_OPTIONS;
    protected ListComponent<E> listComponent;
    protected HasValue<E> field;

    protected String screenId;
    protected DataContext parentDataContext;
    protected EditMode mode = EditMode.CREATE;

    protected Boolean addFirst;

    protected EditorBuilder(EditorBuilder<E> builder) {
        this.origin = builder.origin;
        this.entityClass = builder.entityClass;
        this.handler = builder.handler;

        // copy all properties

        this.mode = builder.mode;
        this.newEntity = builder.newEntity;
        this.editedEntity = builder.editedEntity;
        this.container = builder.container;
        this.initializer = builder.initializer;
        this.options = builder.options;
        this.launchMode = builder.launchMode;
        this.parentDataContext = builder.parentDataContext;
        this.listComponent = builder.listComponent;
        this.field = builder.field;
        this.screenId = builder.screenId;
        this.addFirst = builder.addFirst;
        this.transformation = builder.transformation;
    }

    public EditorBuilder(FrameOwner origin, Class<E> entityClass, Function<EditorBuilder<E>, Screen> handler) {
        this.origin = origin;
        this.entityClass = entityClass;
        this.handler = handler;
    }

    /**
     * Sets {@link EditMode} to {@code CREATE} and returns the builder for chaining.
     * <p>A new entity instance will be created automatically. It can be initialized by code passed to
     * the {@link #withInitializer(Consumer)} method.
     *
     * @see #newEntity(Entity)
     */
    public EditorBuilder<E> newEntity() {
        this.mode = EditMode.CREATE;
        return this;
    }

    /**
     * Sets {@link EditMode} to {@code CREATE} and returns the builder for chaining.
     * <p>The new entity instance is accepted as the parameter. It can be initialized by code passed to
     * the {@link #withInitializer(Consumer)} method.
     *
     * @param entity new entity instance to be passed to the editor screen
     * @see #newEntity()
     */
    public EditorBuilder<E> newEntity(E entity) {
        this.newEntity = entity;
        this.mode = EditMode.CREATE;
        return this;
    }

    /**
     * Sets {@link EditMode} to {@code EDIT} and returns the builder for chaining.
     *
     * @param entity entity instance to be passed to the editor screen
     * @see #newEntity()
     */
    public EditorBuilder<E> editEntity(E entity) {
        this.editedEntity = entity;
        this.mode = EditMode.EDIT;
        return this;
    }

    /**
     * Sets {@code CollectionContainer} and returns the builder for chaining.
     * <p>The container is updated after the editor screen is committed. If the container is {@link Nested},
     * the framework automatically initializes the reference to the parent entity and sets up data contexts
     * for editing compositions.
     */
    public EditorBuilder<E> withContainer(CollectionContainer<E> container) {
        this.container = container;
        return this;
    }

    /**
     * Sets code to initialize a new entity instance and returns the builder for chaining.
     * <p>The initializer is invoked only when {@link EditMode} is {@code CREATE}, i.e. when {@link #newEntity()} or
     * {@link #newEntity(Entity)} methods are invoked on the builder.
     */
    public EditorBuilder<E> withInitializer(Consumer<E> initializer) {
        this.initializer = initializer;

        return this;
    }

    /**
     * Sets code to transform the edited entity after editor commit and returns the builder for chaining.
     * <br>
     * Applied only if either field or container or listComponent is assigned.
     *
     * @param transformation edited entity transformation
     * @see #withContainer(CollectionContainer)
     * @see #withField(HasValue)
     * @see #withListComponent(ListComponent)
     */
    public EditorBuilder<E> withTransformation(Function<E, E> transformation) {
        this.transformation = transformation;
        return this;
    }

    /**
     * Sets {@link Screens.LaunchMode} for the editor screen and returns the builder for chaining.
     * <p>For example: {@code builder.withLaunchMode(OpenMode.DIALOG).build();}
     */
    public EditorBuilder<E> withLaunchMode(Screens.LaunchMode launchMode) {
        checkNotNullArgument(launchMode);

        this.launchMode = launchMode;
        return this;
    }

    /**
     * Sets {@link OpenMode} for the editor screen and returns the builder for chaining.
     * <p>For example: {@code builder.withOpenMode(OpenMode.DIALOG).build();}
     */
    public EditorBuilder<E> withOpenMode(OpenMode openMode) {
        checkNotNullArgument(openMode);

        this.launchMode = openMode;
        return this;
    }

    /**
     * Defines whether a new item will be added to the beginning or to the end of collection. Affects only standalone
     * containers, for nested containers new items are always added to the end.
     */
    public EditorBuilder<E> withAddFirst(boolean addFirst) {
        this.addFirst = addFirst;
        return this;
    }

    /**
     * Sets parent {@link DataContext} for the editor screen and returns the builder for chaining.
     * <p>The screen will commit data to the parent context instead of directly to {@code DataManager}.
     */
    public EditorBuilder<E> withParentDataContext(DataContext parentDataContext) {
        this.parentDataContext = parentDataContext;
        return this;
    }

    /**
     * Sets {@link ScreenOptions} for the editor screen and returns the builder for chaining.
     */
    public EditorBuilder<E> withOptions(ScreenOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Sets list component and returns the builder for chaining.
     * <p>The component is used to get the {@code container} if it is not set explicitly by
     * {@link #withContainer(CollectionContainer)} method. Usually, the list component is a {@code Table}
     * or {@code DataGrid} displaying the list of entities.
     */
    public EditorBuilder<E> withListComponent(ListComponent<E> listComponent) {
        this.listComponent = listComponent;
        return this;
    }

    /**
     * Sets screen id and returns the builder for chaining.
     *
     * @param screenId identifier of the editor screen as specified in the {@code UiController} annotation
     *                 or {@code screens.xml}.
     */
    public EditorBuilder<E> withScreenId(String screenId) {
        this.screenId = screenId;
        return this;
    }

    /**
     * Sets screen class and returns the {@link EditorClassBuilder} for chaining.
     *
     * @param screenClass class of the screen controller
     */
    public <S extends Screen & EditorScreen<E>> EditorClassBuilder<E, S> withScreenClass(Class<S> screenClass) {
        return new EditorClassBuilder<>(this, screenClass);
    }

    /**
     * Sets the field component and returns the builder for chaining.
     * <p>If the field is set, the framework sets the committed entity to the field after successful editor commit.
     */
    public <T extends HasValue<E>> EditorBuilder<E> withField(T field) {
        this.field = field;
        return this;
    }

    /**
     * Returns the field component set by {@link #withField(com.haulmont.cuba.gui.components.HasValue)}.
     */
    public HasValue<E> getField() {
        return field;
    }

    /**
     * Returns screen id set by {@link #withScreenId(String)}.
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * Returns parent data context set by {@link #withParentDataContext(DataContext)}.
     */
    public DataContext getParentDataContext() {
        return parentDataContext;
    }

    /**
     * Returns edited entity class.
     */
    public Class<E> getEntityClass() {
        return entityClass;
    }

    /**
     * Returns new entity set by {@link #newEntity(Entity)}.
     */
    public E getNewEntity() {
        return newEntity;
    }

    /**
     * Returns entity set by {@link #editEntity(Entity)}.
     */
    public E getEditedEntity() {
        return editedEntity;
    }

    /**
     * Returns container set by {@link #withContainer(CollectionContainer)}.
     */
    public CollectionContainer<E> getContainer() {
        return container;
    }

    /**
     * Returns initializer set by {@link #withInitializer(Consumer)}.
     */
    public Consumer<E> getInitializer() {
        return initializer;
    }

    /**
     * Returns launch mode set by {@link #withLaunchMode(Screens.LaunchMode)}.
     */
    public Screens.LaunchMode getLaunchMode() {
        return launchMode;
    }

    /**
     * Returns invoking screen.
     */
    public FrameOwner getOrigin() {
        return origin;
    }

    /**
     * Returns screen options set by {@link #withOptions(ScreenOptions)}.
     */
    public ScreenOptions getOptions() {
        return options;
    }

    /**
     * Returns if a new item will be added to the beginning or to the end of collection. Affects only standalone
     * containers, for nested containers new items are always added to the end.
     */
    public Boolean getAddFirst() {
        return addFirst;
    }

    /**
     * Returns list component set by {@link #withListComponent(ListComponent)}.
     */
    public ListComponent<E> getListComponent() {
        return listComponent;
    }

    /**
     * Returns builder mode derived from previous calls to {@link #newEntity()} or {@link #editEntity(Entity)}.
     */
    public EditMode getMode() {
        return mode;
    }

    /**
     * Returns edited entity transformation.
     */
    public Function<E, E> getTransformation() {
        return transformation;
    }

    /**
     * Builds the editor screen. Screen should be shown using {@link Screen#show()}.
     */
    public Screen build() {
        return handler.apply(this);
    }

    /**
     * Builds and shows the editor screen.
     */
    public Screen show() {
        return handler.apply(this)
                .show();
    }
}
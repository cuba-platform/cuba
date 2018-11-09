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

package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Component.Focusable;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;
import static java.util.Collections.singletonMap;

/**
 * Class that provides fluent interface for building entity editor screens with various options.
 * <p>
 * Inject the class into your screen controller and use {@link #builder(Class, FrameOwner)} or
 * {@link #builder(ListComponent)} methods as entry points.
 *
 * @see Screens
 * @see LookupScreens
 */
@Component("cuba_EditorScreens")
public class EditorScreens {

    @Inject
    protected Metadata metadata;
    @Inject
    protected ExtendedEntities extendedEntities;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected ClientConfig clientConfig;

    /**
     * Creates a screen builder.
     * <p>
     * Example of building a screen for editing an entity:
     * <pre>{@code
     * SomeCustomerEditor screen = editorScreens.builder(Customer.class, this)
     *         .withScreen(SomeCustomerEditor.class)
     *         .withListComponent(customersTable)
     *         .editEntity(customersTable.getSingleSelected())
     *         .build();
     * }</pre>
     * <p>
     * Example of building a screen for creating a new entity instance:
     * <pre>{@code
     * SomeCustomerEditor screen = editorScreens.builder(Customer.class, this)
     *         .withScreen(SomeCustomerEditor.class)
     *         .withListComponent(customersTable)
     *         .newEntity()
     *         .build();
     * }</pre>
     *
     * @param entityClass   edited entity class
     * @param origin        invoking screen
     *
     * @see #builder(ListComponent)
     */
    public <E extends Entity> EditorBuilder<E> builder(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new EditorBuilder<>(origin, entityClass, this::buildEditor);
    }

    /**
     * Creates a screen builder using list component.
     * <p>
     * Example of building a screen for editing a currently selected entity:
     * <pre>{@code
     * SomeCustomerEditor screen = editorScreens.builder(customersTable)
     *          .withScreen(SomeCustomerEditor.class)
     *          .build();
     * }</pre>
     * <p>
     * Example of building a screen for creating a new entity instance:
     * <pre>{@code
     * SomeCustomerEditor screen = editorScreens.builder(customersTable)
     *          .withScreen(SomeCustomerEditor.class)
     *          .newEntity()
     *          .build();
     * }</pre>
     *
     * @param listComponent {@link Table}, {@link DataGrid} or another component containing the list of entities
     *
     * @see #builder(Class, FrameOwner)
     */
    public <E extends Entity> EditorBuilder<E> builder(ListComponent<E> listComponent) {
        checkNotNullArgument(listComponent);
        checkNotNullArgument(listComponent.getFrame());

        FrameOwner frameOwner = listComponent.getFrame().getFrameOwner();
        Class<E> entityClass;
        DataUnit items = listComponent.getItems();
        if (items instanceof EntityDataUnit) {
            entityClass = ((EntityDataUnit) items).getEntityMetaClass().getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to data", listComponent));
        }

        EditorBuilder<E> builder = new EditorBuilder<>(frameOwner, entityClass, this::buildEditor);
        builder.withListComponent(listComponent);
        builder.editEntity(listComponent.getSingleSelected());
        return builder;
    }

    /**
     * Creates a screen builder using {@link PickerField} component.
     * <p>
     * Example of building a screen for editing a currently set value:
     * <pre>{@code
     * SomeCustomerEditor screen = editorScreens.builder(customerPickerField)
     *          .withScreen(SomeCustomerEditor.class)
     *          .build();
     * }</pre>
     * <p>
     * Example of building a screen for creating a new entity instance:
     * <pre>{@code
     * SomeCustomerEditor screen = editorScreens.builder(customerPickerField)
     *          .withScreen(SomeCustomerEditor.class)
     *          .newEntity()
     *          .build();
     * }</pre>
     *
     * @param field {@link PickerField}, {@link LookupPickerField} or another picker component
     *
     * @see #builder(Class, FrameOwner)
     */
    public <E extends Entity> EditorBuilder<E> builder(PickerField<E> field) {
        checkNotNullArgument(field);
        checkNotNullArgument(field.getFrame());

        FrameOwner frameOwner = field.getFrame().getFrameOwner();
        Class<E> entityClass;
        MetaClass metaClass = field.getMetaClass();
        if (metaClass != null) {
            entityClass = metaClass.getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to meta class", field));
        }

        EditorBuilder<E> builder = new EditorBuilder<>(frameOwner, entityClass, this::buildEditor);
        builder.withField(field);
        builder.editEntity(field.getValue());
        return builder;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity, S extends Screen> S buildEditor(EditorBuilder<E> builder) {
        if (builder.getMode() == Mode.EDIT && builder.getEditedEntity() == null) {
            throw new IllegalStateException(String.format("Editor of %s cannot be open with mode EDIT, entity is not set",
                    builder.getEntityClass()));
        }

        FrameOwner origin = builder.getOrigin();
        Screens screens = getScreenContext(origin).getScreens();

        ListComponent<E> listComponent = builder.getListComponent();

        CollectionContainer<E> container = null;

        if (listComponent != null) {
            DataUnit items = listComponent.getItems();
            CollectionContainer<E> listComponentContainer = items instanceof ContainerDataUnit ?
                    ((ContainerDataUnit) items).getContainer() : null;
            container = builder.getContainer() != null ? builder.getContainer() : listComponentContainer;
        }

        E entity = initEntity(builder, container);

        Screen screen = createScreen(builder, screens, entity);

        EditorScreen<E> editorScreen = (EditorScreen<E>) screen;
        editorScreen.setEntityToEdit(entity);

        DataContext parentDataContext = builder.getParentDataContext();
        if (parentDataContext != null) {
            UiControllerUtils.getScreenData(screen).getDataContext().setParent(parentDataContext);
        } else if (container instanceof Nested) {
            setupParentDataContextForComposition(origin, screen, (Nested) container);
        }

        if (container != null) {
            CollectionContainer<E> ct = container;
            screen.addAfterCloseListener(event -> {
                CloseAction closeAction = event.getCloseAction();
                if (isCommitCloseAction(closeAction)) {
                    if (builder.getMode() == Mode.CREATE) {
                        if (ct instanceof Nested || !clientConfig.getCreateActionAddsFirst()) {
                            ct.getMutableItems().add(editorScreen.getEditedEntity());
                        } else {
                            ct.getMutableItems().add(0, editorScreen.getEditedEntity());
                        }
                    } else {
                        ct.replaceItem(editorScreen.getEditedEntity());
                    }
                }
                if (listComponent instanceof Focusable) {
                    ((Focusable) listComponent).focus();
                }
            });
        }

        com.haulmont.cuba.gui.components.Component field = builder.getField();
        if (field != null) {
            screen.addAfterCloseListener(event -> {
                CloseAction closeAction = event.getCloseAction();
                if (isCommitCloseAction(closeAction)) {
                    // todo do we need to remove listeners from entity here ?
                    // todo composition support
                    ((HasValue) field).setValue(editorScreen.getEditedEntity());
                }

                if (field instanceof Focusable) {
                    ((Focusable) field).focus();
                }
            });
        }

        return (S) screen;
    }

    protected <E extends Entity> E initEntity(EditorBuilder<E> builder, CollectionContainer<E> container) {
        E entity;

        if (builder.getMode() == Mode.CREATE) {
            if (builder.getNewEntity() == null) {
                entity = metadata.create(builder.getEntityClass());
            } else {
                entity = builder.getNewEntity();
            }
            if (container instanceof Nested) {
                initializeNestedEntity(entity, (Nested) container);
            }
            if (builder.getInitializer() != null) {
                builder.getInitializer().accept(entity);
            }
        } else {
            entity = builder.getEditedEntity();
        }

        return entity;
    }

    protected <E extends Entity> Screen createScreen(EditorBuilder<E> builder, Screens screens, E entity) {
        Screen screen;

        if (builder instanceof EditorClassBuilder) {
            @SuppressWarnings("unchecked")
            Class<? extends Screen> screenClass = ((EditorClassBuilder) builder).getScreenClass();
            screen = screens.create(screenClass, builder.getLaunchMode(), builder.getOptions());
        } else {
            String editorScreenId;

            if (builder.getScreenId() != null) {
                editorScreenId = builder.getScreenId();
            } else {
                editorScreenId = windowConfig.getEditorScreen(entity).getId();
            }

            // legacy screens support
            WindowInfo windowInfo = windowConfig.getWindowInfo(editorScreenId);
            ScreenOptions options = builder.getOptions();

            if (LegacyFrame.class.isAssignableFrom(windowInfo.getControllerClass())
                && options == FrameOwner.NO_OPTIONS) {
                options = new MapScreenOptions(singletonMap(WindowParams.ITEM.name(), entity));
            }

            screen = screens.create(editorScreenId, builder.getLaunchMode(), options);
        }

        if (!(screen instanceof EditorScreen)) {
            throw new IllegalArgumentException(String.format("Screen %s does not implement EditorScreen: %s",
                    screen.getId(), screen.getClass()));
        }

        return screen;
    }

    protected  <E extends Entity> void initializeNestedEntity(E entity, Nested container) {
        InstanceContainer masterContainer = container.getMaster();
        String property = container.getProperty();

        MetaClass masterMetaClass = masterContainer.getEntityMetaClass();
        MetaProperty metaProperty = masterMetaClass.getPropertyNN(property);

        MetaProperty inverseProp = metaProperty.getInverse();
        if (inverseProp != null) {
            Class<?> inversePropClass = extendedEntities.getEffectiveClass(inverseProp.getDomain());
            Class<?> containerEntityClass = extendedEntities.getEffectiveClass(((CollectionContainer) container).getEntityMetaClass());
            if (inversePropClass.isAssignableFrom(containerEntityClass)) {
                entity.setValue(inverseProp.getName(), masterContainer.getItem());
            }
        }
    }

    protected void setupParentDataContextForComposition(FrameOwner origin, Screen screen, Nested container) {
        InstanceContainer masterContainer = container.getMaster();
        String property = container.getProperty();

        MetaClass masterMetaClass = masterContainer.getEntityMetaClass();
        MetaProperty metaProperty = masterMetaClass.getPropertyNN(property);

        if (metaProperty.getType() == MetaProperty.Type.COMPOSITION) {
            ScreenData screenData = UiControllerUtils.getScreenData(origin);
            UiControllerUtils.getScreenData(screen).getDataContext().setParent(screenData.getDataContext());
        }
    }

    protected boolean isCommitCloseAction(CloseAction closeAction) {
        return (closeAction instanceof StandardCloseAction)
                && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID);
    }

    /**
     * Editor screen purpose: to create a new entity instance or to edit an existing one.
     */
    public enum Mode {
        CREATE,
        EDIT
    }

    /**
     * Builder that is not aware of concrete screen class. It's {@link #build()} method returns {@link Screen}.
     */
    public static class EditorBuilder<E extends Entity> {

        protected final FrameOwner origin;
        protected final Class<E> entityClass;
        protected final Function<EditorBuilder<E>, Screen> handler;

        protected E newEntity;
        protected E editedEntity;
        protected CollectionContainer<E> container;
        protected Consumer<E> initializer;
        protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
        protected ScreenOptions options = FrameOwner.NO_OPTIONS;
        protected ListComponent<E> listComponent;
        protected com.haulmont.cuba.gui.components.Component field;

        protected String screenId;
        protected DataContext parentDataContext;
        protected Mode mode = Mode.CREATE;

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
        }

        public EditorBuilder(FrameOwner origin, Class<E> entityClass, Function<EditorBuilder<E>, Screen> handler) {
            this.origin = origin;
            this.entityClass = entityClass;
            this.handler = handler;
        }

        /**
         * Sets {@link Mode} to {@code CREATE} and returns the builder for chaining.
         * <p>A new entity instance will be created automatically. It can be initialized by code passed to
         * the {@link #withInitializer(Consumer)} method.
         *
         * @see #newEntity(Entity)
         */
        public EditorBuilder<E> newEntity() {
            this.mode = Mode.CREATE;
            return this;
        }

        /**
         * Sets {@link Mode} to {@code CREATE} and returns the builder for chaining.
         * <p>The new entity instance is accepted as the parameter. It can be initialized by code passed to
         * the {@link #withInitializer(Consumer)} method.
         *
         * @param entity new entity instance to be passed to the editor screen
         * @see #newEntity()
         */
        public EditorBuilder<E> newEntity(E entity) {
            this.newEntity = entity;
            this.mode = Mode.CREATE;
            return this;
        }


        /**
         * Sets {@link Mode} to {@code EDIT} and returns the builder for chaining.
         *
         * @param entity entity instance to be passed to the editor screen
         * @see #newEntity()
         */
        public EditorBuilder<E> editEntity(E entity) {
            this.editedEntity = entity;
            this.mode = Mode.EDIT;
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
         * <p>The initializer is invoked only when {@link Mode} is {@code CREATE}, i.e. when {@link #newEntity()} or
         * {@link #newEntity(Entity)} methods are invoked on the builder.
         */
        public EditorBuilder<E> withInitializer(Consumer<E> initializer) {
            this.initializer = initializer;

            return this;
        }

        /**
         * Sets {@link Screens.LaunchMode} for the editor screen and returns the builder for chaining.
         * <p>For example: {@code builder.withLaunchMode(OpenMode.DIALOG).build();}
         */
        public EditorBuilder<E> withLaunchMode(Screens.LaunchMode launchMode) {
            this.launchMode = launchMode;
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
         * @param screenId  identifier of the editor screen as specified in the {@code UiController} annotation
         *                  or {@code screens.xml}.
         */
        public EditorBuilder<E> withScreen(String screenId) {
            this.screenId = screenId;
            return this;
        }

        /**
         * Sets screen class and returns the {@link EditorClassBuilder} for chaining.
         *
         * @param screenClass class of the screen controller
         */
        public <S extends Screen & EditorScreen<E>> EditorClassBuilder<E, S> withScreen(Class<S> screenClass) {
            return new EditorClassBuilder<>(this, screenClass);
        }

        /**
         * Sets the field component and returns the builder for chaining.
         * <p>If the field is set, the framework sets the committed entity to the field after successful editor commit.
         */
        public <T extends com.haulmont.cuba.gui.components.Component & HasValue<E>> EditorBuilder<E> withField(T field) {
            this.field = field;
            return this;
        }

        /**
         * Returns the field component set by {@link #withField(com.haulmont.cuba.gui.components.Component)}.
         */
        public com.haulmont.cuba.gui.components.Component getField() {
            return field;
        }

        /**
         * Returns screen id set by {@link #withScreen(String)}.
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
         * Returns list component set by {@link #withListComponent(ListComponent)}.
         */
        public ListComponent<E> getListComponent() {
            return listComponent;
        }

        /**
         * Returns builder mode derived from previous calls to {@link #newEntity()} or {@link #editEntity(Entity)}.
         */
        public Mode getMode() {
            return mode;
        }

        /**
         * Builds the editor screen.
         */
        public Screen build() {
            return handler.apply(this);
        }
    }

    /**
     * Builder that knows the concrete screen class. It's {@link #build()} method returns that class.
     */
    public static class EditorClassBuilder<E extends Entity, S extends Screen & EditorScreen<E>>
            extends EditorBuilder<E> {

        protected Class<S> screenClass;

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
        public EditorClassBuilder<E, S> withLaunchMode(Screens.LaunchMode launchMode) {
            super.withLaunchMode(launchMode);
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
        public EditorBuilder<E> withScreen(String screenId) {
            throw new IllegalStateException("EditorClassBuilder does not support screenId");
        }

        @Override
        public <T extends com.haulmont.cuba.gui.components.Component & HasValue<E>> EditorClassBuilder<E, S> withField(T field) {
            super.withField(field);
            return this;
        }

        /**
         * Returns editor screen class.
         */
        public Class<S> getScreenClass() {
            return screenClass;
        }

        @SuppressWarnings("unchecked")
        @Override
        public S build() {
            return (S) handler.apply(this);
        }
    }
}
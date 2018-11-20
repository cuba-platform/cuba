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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.gui.Screens.LaunchMode;
import com.haulmont.cuba.gui.components.Component.Focusable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityDataUnit;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupScreen.ValidationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

/**
 * Class that provides fluent interface for building entity lookup screens with various options. <br>
 * Inject the class into your screen controller and use {@link #builder(Class, FrameOwner)} method as an entry point.
 *
 * @see Screens
 * @see EditorScreens
 */
@Component("cuba_LookupScreens")
public class LookupScreens {

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected ExtendedEntities extendedEntities;

    /**
     * Creates a screen builder.
     * <p>
     * Example of building a lookup screen:
     * <pre>{@code
     * SomeCustomerListScreen screen = lookupScreens.builder(Customer.class, this)
     *         .withScreen(SomeCustomerListScreen.class)
     *         .withLaunchMode(OpenMode.DIALOG)
     *         .build();
     * }</pre>
     *
     * @param entityClass   entity class
     * @param origin        invoking screen
     */
    public <E extends Entity> LookupBuilder<E> builder(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new LookupBuilder<>(origin, entityClass, this::buildLookup);
    }

    /**
     * Creates a screen builder.
     * <p>
     * Example of building a lookup screen:
     * <pre>{@code
     * SomeCustomerListScreen screen = lookupScreens.builder(Customer.class, this)
     *         .withScreen(SomeCustomerListScreen.class)
     *         .withLaunchMode(OpenMode.DIALOG)
     *         .build();
     * }</pre>
     *
     * @param listComponent {@code Table}, {@code DataGrid} or another component containing the list of entities
     *
     * @see #builder(Class, FrameOwner)
     */
    public <E extends Entity> LookupBuilder<E> builder(ListComponent<E> listComponent) {
        checkNotNullArgument(listComponent);
        checkNotNullArgument(listComponent.getFrame());

        FrameOwner frameOwner = listComponent.getFrame().getFrameOwner();
        Class<E> entityClass;
        DataUnit<E> items = listComponent.getItems();
        if (items instanceof EntityDataUnit<?>) {
            entityClass = ((EntityDataUnit<E>) items).getEntityMetaClass().getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to data", listComponent));
        }

        LookupBuilder<E> builder = new LookupBuilder<>(frameOwner, entityClass, this::buildLookup);
        builder.withListComponent(listComponent);
        return builder;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> Screen buildLookup(LookupBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = getScreenContext(origin).getScreens();

        Screen screen;

        if (builder instanceof LookupClassBuilder) {
            Class screenClass = ((LookupClassBuilder) builder).getScreenClass();
            screen = screens.create(screenClass, builder.getLaunchMode(), builder.getOptions());
        } else {
            String lookupScreenId;
            if (builder.getScreenId() != null) {
                lookupScreenId = builder.getScreenId();
            } else {
                lookupScreenId = windowConfig.getLookupScreen(builder.getEntityClass()).getId();
            }

            screen = screens.create(lookupScreenId, builder.getLaunchMode(), builder.getOptions());
        }

        if (!(screen instanceof LookupScreen)) {
            throw new IllegalArgumentException(String.format("Screen %s does not implement LookupScreen: %s",
                    screen.getId(), screen.getClass()));
        }

        LookupScreen<E> lookupScreen = (LookupScreen) screen;

        if (builder.getField() != null) {
            com.haulmont.cuba.gui.components.Component field = builder.getField();

            if (field instanceof Focusable) {
                screen.addAfterCloseListener(event -> {
                    // move focus to owner
                    ((Focusable) field).focus();
                });
            }
            lookupScreen.setSelectHandler(items ->
                    handleSelectionWithField(builder, (HasValue<E>) field, items)
            );
        }

        CollectionContainer<E> container = null;

        if (builder.getListComponent() != null) {
            ListComponent<E> listComponent = builder.getListComponent();

            if (listComponent instanceof Focusable) {
                screen.addAfterCloseListener(event -> {
                    // move focus to owner
                    ((Focusable) listComponent).focus();
                });
            }

            if (listComponent.getItems() instanceof ContainerDataUnit) {
                container = ((ContainerDataUnit<E>) listComponent.getItems()).getContainer();
            }
        }

        if (builder.getContainer() != null) {
            container = builder.getContainer();
        }

        if (container != null) {
            CollectionContainer<E> collectionDc = container;

            lookupScreen.setSelectHandler(items ->
                    handleSelectionWithContainer(builder, collectionDc, items)
            );
        }

        if (builder.getSelectHandler() != null) {
            lookupScreen.setSelectHandler(builder.getSelectHandler());
        }

        if (builder.getSelectValidator() != null) {
            lookupScreen.setSelectValidator(builder.getSelectValidator());
        }

        return screen;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> void handleSelectionWithField(LookupBuilder<E> builder,
                                                               HasValue<E> field, Collection<E> selectedItems) {
        if (!selectedItems.isEmpty()) {
            Entity newValue = selectedItems.iterator().next();
            field.setValue((E) newValue);
        }
    }

    protected <E extends Entity> void handleSelectionWithContainer(LookupBuilder<E> builder,
                                                                   CollectionContainer<E> collectionDc,
                                                                   Collection<E> selectedItems) {
        if (selectedItems.isEmpty()) {
            return;
        }

        boolean initializeMasterReference = false;
        Entity masterItem = null;
        MetaProperty inverseMetaProperty = null;

        // update holder reference if needed
        if (collectionDc instanceof Nested) {
            InstanceContainer masterDc = ((Nested) collectionDc).getMaster();

            String property = ((Nested) collectionDc).getProperty();
            masterItem = masterDc.getItem();

            MetaProperty metaProperty = masterItem.getMetaClass().getPropertyNN(property);
            inverseMetaProperty = metaProperty.getInverse();

            if (inverseMetaProperty != null
                && !inverseMetaProperty.getRange().getCardinality().isMany()) {

                Class<?> inversePropClass = extendedEntities.getEffectiveClass(inverseMetaProperty.getDomain());
                Class<?> dcClass = extendedEntities.getEffectiveClass(collectionDc.getEntityMetaClass());

                initializeMasterReference = inversePropClass.isAssignableFrom(dcClass);
            }
        }

        DataContext dataContext = UiControllerUtils.getScreenData(builder.getOrigin()).getDataContext();

        List<E> mergedItems = new ArrayList<>(selectedItems.size());
        for (E item : selectedItems) {
            if (!collectionDc.containsItem(item.getId())) {
                // track changes in the related instance
                E mergedItem = dataContext.merge(item);
                if (initializeMasterReference) {
                    // change reference, now it will be marked as modified
                    mergedItem.setValue(inverseMetaProperty.getName(), masterItem);
                }
                mergedItems.add(mergedItem);
            }
        }

        collectionDc.getMutableItems().addAll(mergedItems);
    }

    /**
     * Builder that is not aware of concrete screen class. It's {@link #build()} method returns {@link Screen}.
     */
    public static class LookupBuilder<E extends Entity> {

        protected final FrameOwner origin;
        protected final Class<E> entityClass;
        protected final Function<LookupBuilder<E>, Screen> handler;

        protected Predicate<ValidationContext<E>> selectValidator;
        protected Consumer<Collection<E>> selectHandler;
        protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
        protected ScreenOptions options = FrameOwner.NO_OPTIONS;
        protected CollectionContainer<E> container;

        protected String screenId;
        protected ListComponent<E> listComponent;
        protected com.haulmont.cuba.gui.components.Component field;

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
        public LookupBuilder<E> withLaunchMode(LaunchMode launchMode) {
            this.launchMode = launchMode;
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
        public LookupBuilder<E> withSelectValidator(Predicate<ValidationContext<E>> selectValidator) {
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
        public <T extends com.haulmont.cuba.gui.components.Component & HasValue<E>> LookupBuilder<E> withField(T field) {
            this.field = field;
            return this;
        }

        /**
         * Sets screen class and returns the {@link LookupClassBuilder} for chaining.
         *
         * @param screenClass class of the screen controller
         */
        public <S extends Screen & LookupScreen<E>> LookupClassBuilder<E, S> withScreen(Class<S> screenClass) {
            return new LookupClassBuilder<>(this, screenClass);
        }

        /**
         * Sets screen id and returns the builder for chaining.
         *
         * @param screenId  identifier of the lookup screen as specified in the {@code UiController} annotation
         *                  or {@code screens.xml}.
         */
        public LookupBuilder<E> withScreen(String screenId) {
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
         * Returns screen id set by {@link #withScreen(String)}.
         */
        public String getScreenId() {
            return screenId;
        }

        /**
         * Returns launch mode set by {@link #withLaunchMode(Screens.LaunchMode)}.
         */
        public LaunchMode getLaunchMode() {
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
        public Predicate<ValidationContext<E>> getSelectValidator() {
            return selectValidator;
        }

        /**
         * Returns the field component set by {@link #withField(com.haulmont.cuba.gui.components.Component)}.
         */
        public com.haulmont.cuba.gui.components.Component getField() {
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

        /**
         * Builds the lookup screen.
         */
        public Screen build() {
            return this.handler.apply(this);
        }
    }

    /**
     * Builder that knows the concrete screen class. It's {@link #build()} method returns that class.
     */
    public static class LookupClassBuilder<E extends Entity, S extends Screen & LookupScreen<E>>
            extends LookupBuilder<E> {

        protected final Class<S> screenClass;

        public LookupClassBuilder(LookupBuilder<E> builder, Class<S> screenClass) {
            super(builder);

            this.screenClass = screenClass;
        }

        /**
         * Returns lookup screen class.
         */
        public Class<S> getScreenClass() {
            return screenClass;
        }

        @Override
        public LookupClassBuilder<E, S> withLaunchMode(LaunchMode launchMode) {
            super.withLaunchMode(launchMode);
            return this;
        }

        @Override
        public LookupClassBuilder<E, S> withOptions(ScreenOptions options) {
            super.withOptions(options);
            return this;
        }

        @Override
        public LookupClassBuilder<E, S> withSelectValidator(Predicate<ValidationContext<E>> selectValidator) {
            super.withSelectValidator(selectValidator);
            return this;
        }

        @Override
        public LookupClassBuilder<E, S> withSelectHandler(Consumer<Collection<E>> selectHandler) {
            super.withSelectHandler(selectHandler);
            return this;
        }

        @Override
        public <T extends com.haulmont.cuba.gui.components.Component & HasValue<E>> LookupClassBuilder<E, S> withField(T field) {
            super.withField(field);
            return this;
        }

        @Override
        public LookupBuilder<E> withScreen(String screenId) {
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

        @SuppressWarnings("unchecked")
        public S build() {
            return (S) this.handler.apply(this);
        }
    }
}
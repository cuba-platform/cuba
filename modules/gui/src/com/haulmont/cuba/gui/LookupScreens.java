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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.Screens.LaunchMode;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupScreen.ValidationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

/**
 * Class that provides fluent interface for building entity lookup screens with various options.
 * <p>
 * Inject the class into your screen controller and use {@link #builder(Class, FrameOwner)} method as an entry point.
 *
 * @see Screens
 * @see EditorScreens
 */
@Component("cuba_LookupScreens")
public class LookupScreens {

    @Inject
    protected WindowConfig windowConfig;

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

            if (field instanceof com.haulmont.cuba.gui.components.Component.Focusable) {
                screen.addAfterCloseListener(afterCloseEvent -> {
                    // move focus to owner
                    ((com.haulmont.cuba.gui.components.Component.Focusable) field).focus();
                });
            }
            lookupScreen.setSelectHandler(items -> {
                if (!items.isEmpty()) {
                    Entity newValue = items.iterator().next();
                    ((HasValue<E>) field).setValue((E) newValue);
                }
            });
        }

        if (builder.getSelectHandler() != null) {
            lookupScreen.setSelectHandler(builder.getSelectHandler());
        }

        if (builder.getSelectValidator() != null) {
            lookupScreen.setSelectValidator(builder.getSelectValidator());
        }

        return screen;
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

        protected String screenId;
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

        @SuppressWarnings("unchecked")
        public S build() {
            return (S) this.handler.apply(this);
        }
    }
}
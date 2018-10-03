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
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupScreen.ValidationContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component("cuba_LookupScreens")
public class LookupScreens {

    @Inject
    protected WindowConfig windowConfig;

    public <E extends Entity> LookupBuilder<E> builder(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new LookupBuilder<>(origin, entityClass, this::buildLookup);
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> Screen buildLookup(LookupBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = origin.getScreenContext().getScreens();

        Screen screen;

        if (builder instanceof LookupClassBuilder) {
            Class sreenClass = ((LookupClassBuilder) builder).getSreenClass();
            screen = screens.create(sreenClass, builder.getLaunchMode(), builder.getOptions());
        } else {
            WindowInfo windowInfo;
            if (builder.getScreenId() != null) {
                windowInfo = windowConfig.getWindowInfo(builder.getScreenId());
            }else {
                windowInfo = windowConfig.getLookupScreen(builder.getEntityClass());
            }

            screen = screens.create(windowInfo, builder.getLaunchMode(), builder.getOptions());
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
        }

        public LookupBuilder(FrameOwner origin, Class<E> entityClass, Function<LookupBuilder<E>, Screen> handler) {
            this.origin = origin;
            this.entityClass = entityClass;
            this.handler = handler;
        }

        public LookupBuilder<E> withLaunchMode(LaunchMode launchMode) {
            this.launchMode = launchMode;
            return this;
        }

        public LookupBuilder<E> withOptions(ScreenOptions options) {
            this.options = options;
            return this;
        }

        public LookupBuilder<E> withSelectValidator(Predicate<ValidationContext<E>> selectValidator) {
            this.selectValidator = selectValidator;
            return this;
        }

        public LookupBuilder<E> withSelectHandler(Consumer<Collection<E>> selectHandler) {
            this.selectHandler = selectHandler;
            return this;
        }

        public <T extends com.haulmont.cuba.gui.components.Component & HasValue<E>> LookupBuilder<E> withField(T field) {
            this.field = field;
            return this;
        }

        public <S extends Screen & LookupScreen<E>> LookupClassBuilder<E, S> withScreen(Class<S> screenClass) {
            return new LookupClassBuilder<>(this, screenClass);
        }

        public LookupBuilder<E> withScreen(String screenId) {
            this.screenId = screenId;
            return this;
        }

        public String getScreenId() {
            return screenId;
        }

        public LaunchMode getLaunchMode() {
            return launchMode;
        }

        public ScreenOptions getOptions() {
            return options;
        }

        public FrameOwner getOrigin() {
            return origin;
        }

        public Class<E> getEntityClass() {
            return entityClass;
        }

        public Function<LookupBuilder<E>, Screen> getHandler() {
            return handler;
        }

        public Consumer<Collection<E>> getSelectHandler() {
            return selectHandler;
        }

        public Predicate<ValidationContext<E>> getSelectValidator() {
            return selectValidator;
        }

        public Screen create() {
            return this.handler.apply(this);
        }

        public com.haulmont.cuba.gui.components.Component getField() {
            return field;
        }
    }

    public static class LookupClassBuilder<E extends Entity, S extends Screen & LookupScreen<E>>
            extends LookupBuilder<E> {

        protected final Class<S> sreenClass;

        public LookupClassBuilder(LookupBuilder<E> builder, Class<S> sreenClass) {
            super(builder);

            this.sreenClass = sreenClass;
        }

        public Class<S> getSreenClass() {
            return sreenClass;
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
        public S create() {
            return (S) this.handler.apply(this);
        }
    }
}
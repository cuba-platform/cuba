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
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Component.Focusable;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.SupportsContainerBinding;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component("cuba_EditorScreens")
public class EditorScreens {
    @Inject
    protected Metadata metadata;
    @Inject
    protected WindowConfig windowConfig;

    public <E extends Entity> EditorBuilder<E> builder(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new EditorBuilder<>(origin, entityClass, this::buildEditor);
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity, S extends Screen> S buildEditor(EditorBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = origin.getScreenContext().getScreens();

        if (builder.getMode() == Mode.EDIT && builder.getEntity() == null) {
            throw new IllegalStateException(String.format("Editor of %s cannot be open with mode EDIT, entity is not set",
                    builder.getEntityClass()));
        }

        E entity = builder.getEntity();

        if (builder.getMode() == Mode.CREATE) {
            if (entity == null) {
                entity = metadata.create(builder.getEntityClass());
            }
            if (builder.getInitializer() != null) {
                builder.getInitializer().accept(entity);
            }
        }

        Screen screen;

        if (builder instanceof EditorClassBuilder) {
            Class screenClass = ((EditorClassBuilder) builder).getScreenClass();
            screen = screens.create(screenClass, builder.getLaunchMode(), builder.getOptions());
        } else {
            WindowInfo windowInfo;

            if (builder.getScreenId() != null) {
                windowInfo = windowConfig.getWindowInfo(builder.getScreenId());
            }else {
                windowInfo = windowConfig.getEditorScreen(entity);
            }

            // legacy screens support
            ScreenOptions options = builder.getOptions();
            if (LegacyFrame.class.isAssignableFrom(windowInfo.getControllerClass())
                && options == FrameOwner.NO_OPTIONS) {
                options = new MapScreenOptions(Collections.singletonMap(WindowParams.ITEM.name(), entity));
            }

            screen = screens.create(windowInfo, builder.getLaunchMode(), options);
        }

        if (!(screen instanceof EditorScreen)) {
            throw new IllegalArgumentException(String.format("Screen %s does not implement EditorScreen: %s",
                    screen.getId(), screen.getClass()));
        }

        EditorScreen<E> editorScreen = (EditorScreen<E>) screen;

        editorScreen.setEntityToEdit(entity);

        DataContext parentDataContext = builder.getParentDataContext();
        if (parentDataContext != null) {
            UiControllerUtils.getScreenData(screen).getDataContext().setParent(parentDataContext);
        }

        ListComponent<E> listComponent = builder.getListComponent();

        CollectionContainer<E> listComponentContainer = listComponent instanceof SupportsContainerBinding ?
                ((SupportsContainerBinding) listComponent).getBindingContainer() : null;

        CollectionContainer<E> container = builder.getContainer() != null ? builder.getContainer() : listComponentContainer;

        if (container != null) {
            screen.addAfterCloseListener(afterCloseEvent -> {
                CloseAction closeAction = afterCloseEvent.getCloseAction();
                if (isCommitCloseAction(closeAction)) {
                    if (builder.getMode() == Mode.CREATE) {
                        container.getMutableItems().add(0, editorScreen.getEditedEntity());
                    } else {
                        container.replaceItem(editorScreen.getEditedEntity());
                    }
                }
                if (listComponent instanceof Focusable) {
                    ((Focusable) listComponent).focus();
                }
            });
        }

        return (S) screen;
    }

    protected boolean isCommitCloseAction(CloseAction closeAction) {
        return (closeAction instanceof StandardCloseAction)
                && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID);
    }

    public enum Mode {
        CREATE,
        EDIT
    }

    public static class EditorBuilder<E extends Entity> {

        protected final FrameOwner origin;
        protected final Class<E> entityClass;
        protected final Function<EditorBuilder<E>, Screen> handler;

        protected E entity;
        protected CollectionContainer<E> container;
        protected Consumer<E> initializer;
        protected Screens.LaunchMode launchMode = OpenMode.THIS_TAB;
        protected ScreenOptions options = FrameOwner.NO_OPTIONS;
        protected ListComponent<E> listComponent;

        protected String screenId;
        protected DataContext parentDataContext;
        protected Mode mode = Mode.CREATE;

        protected EditorBuilder(EditorBuilder<E> builder) {
            this.origin = builder.origin;
            this.entityClass = builder.entityClass;
            this.handler = builder.handler;

            // copy all properties

            this.mode = builder.mode;
            this.entity = builder.entity;
            this.container = builder.container;
            this.initializer = builder.initializer;
            this.options = builder.options;
            this.launchMode = builder.launchMode;
            this.parentDataContext = builder.parentDataContext;
            this.listComponent = builder.listComponent;
        }

        public EditorBuilder(FrameOwner origin, Class<E> entityClass, Function<EditorBuilder<E>, Screen> handler) {
            this.origin = origin;
            this.entityClass = entityClass;
            this.handler = handler;
        }

        public EditorBuilder<E> newEntity() {
            this.mode = Mode.CREATE;
            return this;
        }

        public EditorBuilder<E> withEntity(E entity) {
            this.entity = entity;
            return this;
        }

        public EditorBuilder<E> editEntity(E entity) {
            this.entity = entity;
            this.mode = Mode.EDIT;
            return this;
        }

        public EditorBuilder<E> withContainer(CollectionContainer<E> container) {
            this.container = container;
            return this;
        }

        public EditorBuilder<E> withInitializer(Consumer<E> initializer) {
            this.initializer = initializer;

            return this;
        }

        public EditorBuilder<E> withLaunchMode(Screens.LaunchMode launchMode) {
            this.launchMode = launchMode;
            return this;
        }

        public EditorBuilder<E> withParentDataContext(DataContext parentDataContext) {
            this.parentDataContext = parentDataContext;
            return this;
        }

        public EditorBuilder<E> withOptions(ScreenOptions options) {
            this.options = options;
            return this;
        }

        public EditorBuilder<E> withListComponent(ListComponent<E> listComponent) {
            this.listComponent = listComponent;
            return this;
        }

        public EditorBuilder<E> withScreen(String screenId) {
            this.screenId = screenId;
            return this;
        }

        public <S extends Screen & EditorScreen<E>> EditorClassBuilder<E, S> withScreen(Class<S> screenClass) {
            return new EditorClassBuilder<>(this, screenClass);
        }

        public String getScreenId() {
            return screenId;
        }

        public DataContext getParentDataContext() {
            return parentDataContext;
        }

        public Class<E> getEntityClass() {
            return entityClass;
        }

        public E getEntity() {
            return entity;
        }

        public CollectionContainer<E> getContainer() {
            return container;
        }

        public Consumer<E> getInitializer() {
            return initializer;
        }

        public Screens.LaunchMode getLaunchMode() {
            return launchMode;
        }

        public FrameOwner getOrigin() {
            return origin;
        }

        public ScreenOptions getOptions() {
            return options;
        }

        public ListComponent<E> getListComponent() {
            return listComponent;
        }

        public Mode getMode() {
            return mode;
        }

        public Screen create() {
            return handler.apply(this);
        }
    }

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
        public EditorClassBuilder<E, S> withEntity(E entity) {
            super.withEntity(entity);
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

        public Class<S> getScreenClass() {
            return screenClass;
        }

        @SuppressWarnings("unchecked")
        @Override
        public S create() {
            return (S) handler.apply(this);
        }
    }
}
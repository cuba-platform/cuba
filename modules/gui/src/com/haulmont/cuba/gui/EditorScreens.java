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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Component.Focusable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
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
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

@Component("cuba_EditorScreens")
public class EditorScreens {

    @Inject
    protected Metadata metadata;
    @Inject
    protected ExtendedEntities extendedEntities;
    @Inject
    protected WindowConfig windowConfig;

    public <E extends Entity> EditorBuilder<E> builder(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new EditorBuilder<>(origin, entityClass, this::buildEditor);
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> EditorBuilder<E> builder(ListComponent<E> listComponent) {
        checkNotNullArgument(listComponent);

        FrameOwner frameOwner = listComponent.getFrame().getFrameOwner();
        Class<E> entityClass;
        DataUnit<E> items = listComponent.getItems();
        if (items instanceof EntityDataUnit<?>) {
            entityClass = ((EntityDataUnit<E>) items).getEntityMetaClass().getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to data", listComponent));
        }
        EditorBuilder<E> builder = new EditorBuilder<>(frameOwner, entityClass, this::buildEditor);
        builder.withListComponent(listComponent);
        builder.editEntity(listComponent.getSingleSelected());
        return builder;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity, S extends Screen> S buildEditor(EditorBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = origin.getScreenContext().getScreens();

        if (builder.getMode() == Mode.EDIT && builder.getEditedEntity() == null) {
            throw new IllegalStateException(String.format("Editor of %s cannot be open with mode EDIT, entity is not set",
                    builder.getEntityClass()));
        }

        ListComponent<E> listComponent = builder.getListComponent();

        CollectionContainer<E> container = null;

        if (listComponent != null) {
            DataUnit<E> dataSource = listComponent.getItems();
            CollectionContainer<E> listComponentContainer = dataSource instanceof ContainerDataUnit ?
                    ((ContainerDataUnit) dataSource).getContainer() : null;
            container = builder.getContainer() != null ? builder.getContainer() : listComponentContainer;
        }


        E entity;
        if (builder.getMode() == Mode.CREATE) {
            if (builder.getNewEntity() == null) {
                entity = metadata.create(builder.getEntityClass());
            } else {
                entity = builder.getNewEntity();
            }
            if (builder.getInitializer() != null) {
                builder.getInitializer().accept(entity);
            } else if (container instanceof Nested) {
                initializeNestedEntity(entity, (Nested) container);
            }
        } else {
            entity = builder.getEditedEntity();
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
        } else if (container instanceof Nested) {
            setupParentDataContextForComposition(origin, screen, (Nested) container);
        }

        if (container != null) {
            CollectionContainer<E> ct = container;
            screen.addAfterCloseListener(afterCloseEvent -> {
                CloseAction closeAction = afterCloseEvent.getCloseAction();
                if (isCommitCloseAction(closeAction)) {
                    if (builder.getMode() == Mode.CREATE) {
                        ct.getMutableItems().add(0, editorScreen.getEditedEntity());
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
            screen.addAfterCloseListener(afterCloseEvent -> {
                CloseAction closeAction = afterCloseEvent.getCloseAction();
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

    protected  <E extends Entity> void initializeNestedEntity(E entity, Nested container) {
        InstanceContainer parentContainer = container.getParent();
        String property = container.getProperty();

        MetaClass parentMetaClass = parentContainer.getEntityMetaClass();
        MetaProperty metaProperty = parentMetaClass.getPropertyNN(property);

        MetaProperty inverseProp = metaProperty.getInverse();
        if (inverseProp != null) {
            Class<?> inversePropClass = extendedEntities.getEffectiveClass(inverseProp.getDomain());
            Class<?> containerEntityClass = extendedEntities.getEffectiveClass(((CollectionContainer) container).getEntityMetaClass());
            if (inversePropClass.isAssignableFrom(containerEntityClass)) {
                entity.setValue(inverseProp.getName(), parentContainer.getItem());
            }
        }
    }

    protected void setupParentDataContextForComposition(FrameOwner origin, Screen screen, Nested container) {
        InstanceContainer parentContainer = container.getParent();
        String property = container.getProperty();

        MetaClass parentMetaClass = parentContainer.getEntityMetaClass();
        MetaProperty metaProperty = parentMetaClass.getPropertyNN(property);

        if (metaProperty.getType() == MetaProperty.Type.COMPOSITION) {
            ScreenData screenData = UiControllerUtils.getScreenData(origin);
            UiControllerUtils.getScreenData(screen).getDataContext().setParent(screenData.getDataContext());
        }
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

        public EditorBuilder<E> newEntity(E entity) {
            this.newEntity = entity;
            return this;
        }

        public EditorBuilder<E> editEntity(E entity) {
            this.editedEntity = entity;
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

        public <T extends com.haulmont.cuba.gui.components.Component & HasValue<E>> EditorBuilder<E> withField(T field) {
            this.field = field;
            return this;
        }

        public com.haulmont.cuba.gui.components.Component getField() {
            return field;
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

        public E getNewEntity() {
            return newEntity;
        }

        public E getEditedEntity() {
            return editedEntity;
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
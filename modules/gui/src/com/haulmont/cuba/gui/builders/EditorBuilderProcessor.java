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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

@Component("cuba_EditorBuilderProcessor")
public class EditorBuilderProcessor {

    @Inject
    protected Metadata metadata;
    @Inject
    protected ExtendedEntities extendedEntities;
    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected ClientConfig clientConfig;
    @Inject
    protected ViewRepository viewRepository;
    @Inject
    protected EntityStates entityStates;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools;

    @SuppressWarnings("unchecked")
    public  <E extends Entity, S extends Screen> S buildEditor(EditorBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = getScreenContext(origin).getScreens();

        ListComponent<E> listComponent = builder.getListComponent();

        CollectionContainer<E> container = builder.getContainer();

        if (container == null && listComponent != null) {
            DataUnit items = listComponent.getItems();
            container = items instanceof ContainerDataUnit ? ((ContainerDataUnit) items).getContainer() : null;
        }

        E entity = initEntity(builder, container);

        if (builder.getMode() == EditMode.EDIT && entity == null) {
            throw new IllegalStateException(String.format("Editor of %s cannot be open with mode EDIT, entity is not set",
                    builder.getEntityClass()));
        }

        Screen screen = createScreen(builder, screens, entity);

        EditorScreen<E> editorScreen = (EditorScreen<E>) screen;
        editorScreen.setEntityToEdit(entity);

        DataContext parentDataContext = setupParentDataContext(
                origin, screen, container, builder.getParentDataContext());

        if (container != null) {
            CollectionContainer<E> ct = container;
            screen.addAfterCloseListener(event -> {
                CloseAction closeAction = event.getCloseAction();
                if (isCommitCloseAction(closeAction)) {
                    E entityFromEditor = getCommittedEntity(editorScreen, parentDataContext);
                    E reloadedEntity = reloadIfNeeded(entityFromEditor, ct, builder);
                    E committedEntity = transform(reloadedEntity, builder);
                    E mergedEntity = merge(committedEntity, origin, parentDataContext);

                    if (builder.getMode() == EditMode.CREATE) {
                        boolean addsFirst;

                        if (!(ct instanceof Nested)) {
                            addsFirst = clientConfig.getCreateActionAddsFirst();
                            if (builder.getAddFirst() != null) {
                                addsFirst = builder.getAddFirst();
                            }
                        } else {
                            addsFirst = false;
                        }

                        if (ct instanceof Nested || !addsFirst) {
                            ct.getMutableItems().add(mergedEntity);
                        } else {
                            ct.getMutableItems().add(0, mergedEntity);
                        }
                    } else {
                        ct.replaceItem(mergedEntity);
                    }
                }
                if (listComponent instanceof com.haulmont.cuba.gui.components.Component.Focusable) {
                    ((com.haulmont.cuba.gui.components.Component.Focusable) listComponent).focus();
                }
            });
        }

        HasValue<E> field = builder.getField();
        if (field != null) {

            if (parentDataContext == null && field instanceof HasValueSource) {
                ValueSource fieldValueSource = ((HasValueSource) field).getValueSource();
                if (fieldValueSource instanceof EntityValueSource) {
                    if (isCompositionProperty((EntityValueSource) fieldValueSource)) {
                        DataContext thisDataContext = UiControllerUtils.getScreenData(origin).getDataContext();
                        DataContext dataContext = UiControllerUtils.getScreenData(screen).getDataContext();
                        checkDataContext(screen, dataContext);
                        dataContext.setParent(thisDataContext);
                    }
                }
            }

            screen.addAfterCloseListener(event -> {
                CloseAction closeAction = event.getCloseAction();
                if (isCommitCloseAction(closeAction)) {
                    E entityFromEditor = editorScreen.getEditedEntity();
                    E editedEntity = transform(entityFromEditor, builder);

                    if (field instanceof LookupPickerField) {
                        LookupPickerField lookupPickerField = ((LookupPickerField) field);
                        Options options = lookupPickerField.getOptions();
                        if (options instanceof EntityOptions) {
                            EntityOptions entityOptions = (EntityOptions) options;
                            if (entityOptions.containsItem(editedEntity)) {
                                entityOptions.updateItem(editedEntity);
                            }
                        }
                    }

                    if (field instanceof SupportsUserAction) {
                        ((SupportsUserAction) field).setValueFromUser(editedEntity);
                    } else {
                        field.setValue(editedEntity);
                    }
                }

                if (field instanceof com.haulmont.cuba.gui.components.Component.Focusable) {
                    ((com.haulmont.cuba.gui.components.Component.Focusable) field).focus();
                }
            });
        }

        if (builder instanceof EditorClassBuilder) {
            @SuppressWarnings("unchecked")
            Consumer<AfterScreenCloseEvent> closeListener = ((EditorClassBuilder) builder).getCloseListener();
            if (closeListener != null) {
                screen.addAfterCloseListener(new AfterCloseListenerAdapter(closeListener));
            }
        }

        return (S) screen;
    }

    protected <E extends Entity> E merge(E entity, FrameOwner screen, @Nullable DataContext parentDataContext) {
        if (Boolean.parseBoolean(AppContext.getProperty("cuba.doNotMergeEditedEntityIntoBrowserDataContext")))
            return entity;

        if (parentDataContext == null) {
            DataContext thisDataContext = UiControllerUtils.getScreenData(screen).getDataContext();
            if (thisDataContext != null) {
                return thisDataContext.merge(entity);
            }
        }
        return entity;
    }

    protected  <E extends Entity> E transform(E entity, EditorBuilder<E> builder) {
        if (builder.getTransformation() != null) {
            return builder.getTransformation().apply(entity);
        }
        return entity;
    }

    private <E extends Entity> E reloadIfNeeded(E entity, CollectionContainer<E> container, EditorBuilder<E> builder) {
        if (container == null || builder.getTransformation() != null) {
            return entity;
        }

        boolean needDynamicAttributes = false;
        boolean dynamicAttributesAreLoaded = true;
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity e = (BaseGenericIdEntity) entity;
            dynamicAttributesAreLoaded = e.getDynamicAttributes() != null;

            if (container instanceof HasLoader) {
                DataLoader loader = ((HasLoader) container).getLoader();
                if (loader instanceof CollectionLoader) {
                    needDynamicAttributes = ((CollectionLoader) loader).isLoadDynamicAttributes();
                }
            }
        }

        View view = container.getView();
        if (view == null) {
            view = viewRepository.getView(entity.getClass(), View.LOCAL);
        }

        if (!entityStates.isLoadedWithView(entity, view)) {
            entity = dataManager.reload(entity, view, null, needDynamicAttributes);
        } else if (needDynamicAttributes && !dynamicAttributesAreLoaded) {
            dynamicAttributesGuiTools.reloadDynamicAttributes((BaseGenericIdEntity) entity);
        }
        return entity;
    }

    protected <E extends Entity> E getCommittedEntity(EditorScreen<E> editorScreen, @Nullable DataContext parentDataContext) {
        E editedEntity = editorScreen.getEditedEntity();
        if (parentDataContext != null) {
            E trackedEntity = parentDataContext.find(editedEntity);
            if (trackedEntity != null) { // makes sense for NoopDataContext
                return trackedEntity;
            }
        }
        return editedEntity;
    }

    protected <E extends Entity> E initEntity(EditorBuilder<E> builder, CollectionContainer<E> container) {
        E entity;

        boolean oneToOneComposition = false;
        EntityValueSource entityValueSource = null;

        HasValue<E> field = builder.getField();
        if (field instanceof HasValueSource) {
            ValueSource valueSource = ((HasValueSource) field).getValueSource();
            if (valueSource instanceof EntityValueSource) {
                entityValueSource = (EntityValueSource) valueSource;
                oneToOneComposition = isCompositionProperty(entityValueSource);
            }
        }

        if (builder.getMode() == EditMode.CREATE || (oneToOneComposition && field.getValue() == null)) {
            if (builder.getNewEntity() == null) {
                entity = metadata.create(builder.getEntityClass());
            } else {
                entity = builder.getNewEntity();
            }
            if (container instanceof Nested) {
                initializeNestedEntity(entity, (Nested) container);
            }
            if (oneToOneComposition) {
                Entity ownerEntity = entityValueSource.getItem();
                MetaProperty inverseProp = entityValueSource.getMetaPropertyPath().getMetaProperty().getInverse();
                if (inverseProp != null) {
                    entity.setValue(inverseProp.getName(), ownerEntity);
                }
            }
            if (builder.getInitializer() != null) {
                builder.getInitializer().accept(entity);
            }
        } else {
            entity = builder.getEditedEntity();
        }

        return entity;
    }

    protected boolean isCompositionProperty(EntityValueSource entityValueSource) {
        MetaPropertyPath metaPropertyPath = entityValueSource.getMetaPropertyPath();
        return metaPropertyPath != null
                && metaPropertyPath.getMetaProperty().getType() == MetaProperty.Type.COMPOSITION;
    }

    protected <E extends Entity> Screen createScreen(EditorBuilder<E> builder, Screens screens, E entity) {
        Screen screen;

        if (builder instanceof EditorClassBuilder) {
            @SuppressWarnings("unchecked")
            Class<? extends Screen> screenClass = ((EditorClassBuilder) builder).getScreenClass();

            if (screenClass == null) {
                throw new IllegalArgumentException("Screen class is not set");
            }

            screen = screens.create(screenClass, builder.getLaunchMode(), builder.getOptions());
        } else {
            String editorScreenId;

            if (builder.getScreenId() != null) {
                editorScreenId = builder.getScreenId();
            } else {
                editorScreenId = windowConfig.getEditorScreen(entity).getId();
            }

            if (editorScreenId == null) {
                throw new IllegalArgumentException("Screen id is not set");
            }

            // legacy screens support
            WindowInfo windowInfo = windowConfig.getWindowInfo(editorScreenId);
            ScreenOptions options = builder.getOptions();

            if (LegacyFrame.class.isAssignableFrom(windowInfo.getControllerClass())
                    && options == FrameOwner.NO_OPTIONS) {
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(WindowParams.ITEM.name(), entity);
                options = new MapScreenOptions(paramsMap);
            }

            screen = screens.create(editorScreenId, builder.getLaunchMode(), options);
        }

        if (!(screen instanceof EditorScreen)) {
            throw new IllegalArgumentException(String.format("Screen %s does not implement EditorScreen: %s",
                    screen.getId(), screen.getClass()));
        }

        return screen;
    }

    protected <E extends Entity> void initializeNestedEntity(E entity, Nested container) {
        InstanceContainer masterContainer = container.getMaster();
        String property = container.getProperty();

        MetaClass masterMetaClass = masterContainer.getEntityMetaClass();
        MetaProperty metaProperty = masterMetaClass.getPropertyNN(property);

        MetaProperty inverseProp = metaProperty.getInverse();
        if (inverseProp != null && !inverseProp.getRange().getCardinality().isMany()) {
            Class<?> inversePropClass = extendedEntities.getEffectiveClass(inverseProp.getDomain());
            Class<?> containerEntityClass = extendedEntities.getEffectiveClass(((CollectionContainer) container).getEntityMetaClass());
            if (inversePropClass.isAssignableFrom(containerEntityClass)) {
                entity.setValue(inverseProp.getName(), masterContainer.getItem());
            }
        }
    }

    @Nullable
    protected DataContext setupParentDataContext(FrameOwner origin, Screen screen, InstanceContainer container,
                                                 @Nullable DataContext parentContext) {
        DataContext dataContext = parentContext;
        if (dataContext == null && container instanceof Nested) {
            InstanceContainer masterContainer = ((Nested) container).getMaster();
            String property = ((Nested) container).getProperty();

            MetaClass masterMetaClass = masterContainer.getEntityMetaClass();
            MetaProperty metaProperty = masterMetaClass.getPropertyNN(property);

            if (metaProperty.getType() == MetaProperty.Type.COMPOSITION) {
                dataContext = UiControllerUtils.getScreenData(origin).getDataContext();
            }
        }
        if (dataContext != null) {
            DataContext childContext = UiControllerUtils.getScreenData(screen).getDataContext();
            checkDataContext(screen, childContext);
            childContext.setParent(dataContext);
        }
        return dataContext;
    }

    protected void checkDataContext(Screen screen, @Nullable DataContext dataContext) {
        if (dataContext == null) {
            throw new DevelopmentException(
                    String.format("No DataContext in screen '%s'. Composition editing is impossible.", screen.getId()));
        }
    }

    protected boolean isCommitCloseAction(CloseAction closeAction) {
        return (closeAction instanceof StandardCloseAction)
                && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID);
    }
}
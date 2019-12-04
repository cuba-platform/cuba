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

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.SupportsUserAction;
import com.haulmont.cuba.gui.components.data.HasValueSource;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.LookupScreen;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

@Component("cuba_LookupBuilderProcessor")
public class LookupBuilderProcessor {

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected ClientConfig clientConfig;
    @Inject
    protected ExtendedEntities extendedEntities;
    @Inject
    protected EntityStates entityStates;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected MetadataTools metadataTools;

    @SuppressWarnings("unchecked")
    public <E extends Entity> Screen buildLookup(LookupBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = getScreenContext(origin).getScreens();

        Screen screen = createScreen(builder, screens);

        if (!(screen instanceof LookupScreen)) {
            throw new IllegalArgumentException(String.format("Screen %s does not implement LookupScreen: %s",
                    screen.getId(), screen.getClass()));
        }

        LookupScreen<E> lookupScreen = (LookupScreen) screen;

        if (builder.getField() != null) {
            HasValue<E> field = builder.getField();

            if (field instanceof com.haulmont.cuba.gui.components.Component.Focusable) {
                screen.addAfterCloseListener(event -> {
                    // move focus to owner
                    ((com.haulmont.cuba.gui.components.Component.Focusable) field).focus();
                });
            }
            lookupScreen.setSelectHandler(items ->
                    handleSelectionWithField(builder, field, items)
            );
        }

        CollectionContainer<E> container = null;

        if (builder.getListComponent() != null) {
            ListComponent<E> listComponent = builder.getListComponent();

            if (listComponent instanceof com.haulmont.cuba.gui.components.Component.Focusable) {
                screen.addAfterCloseListener(event -> {
                    // move focus to owner
                    ((com.haulmont.cuba.gui.components.Component.Focusable) listComponent).focus();
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

        if (builder instanceof LookupClassBuilder) {
            @SuppressWarnings("unchecked")
            Consumer<AfterScreenCloseEvent> closeListener = ((LookupClassBuilder) builder).getCloseListener();
            if (closeListener != null) {
                screen.addAfterCloseListener(new AfterCloseListenerAdapter(closeListener));
            }
        }

        return screen;
    }

    protected <E extends Entity> Screen createScreen(LookupBuilder<E> builder, Screens screens) {
        Screen screen;

        if (builder instanceof LookupClassBuilder) {
            LookupClassBuilder lookupClassBuilder = (LookupClassBuilder) builder;
            @SuppressWarnings("unchecked")
            Class<? extends Screen> screenClass = lookupClassBuilder.getScreenClass();
            if (screenClass == null) {
                throw new IllegalArgumentException("Screen class is not set");
            }

            screen = screens.create(screenClass, builder.getLaunchMode(), builder.getOptions());
        } else {
            String lookupScreenId;
            if (builder.getScreenId() != null) {
                lookupScreenId = builder.getScreenId();
            } else {
                lookupScreenId = windowConfig.getLookupScreen(builder.getEntityClass()).getId();
            }

            if (lookupScreenId == null) {
                throw new IllegalArgumentException("Screen id is not set");
            }

            screen = screens.create(lookupScreenId, builder.getLaunchMode(), builder.getOptions());
        }
        return screen;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> void handleSelectionWithField(@SuppressWarnings("unused") LookupBuilder<E> builder,
                                                               HasValue<E> field, Collection<E> itemsFromLookup) {
        if (itemsFromLookup.isEmpty()) {
            return;
        }

        Collection<E> selectedItems = transform(itemsFromLookup, builder);

        Entity newValue = selectedItems.iterator().next();

        View viewForField = clientConfig.getReloadUnfetchedAttributesFromLookupScreens() && metadataTools.isPersistent(newValue.getClass()) ?
                getViewForField(field) :
                null;
        if (viewForField != null && !entityStates.isLoadedWithView(newValue, viewForField)) {
            newValue = dataManager.reload(newValue, viewForField);
        }

        if (field instanceof LookupPickerField) {
            LookupPickerField lookupPickerField = (LookupPickerField) field;
            Options options = lookupPickerField.getOptions();
            if (options instanceof EntityOptions) {
                EntityOptions entityOptions = (EntityOptions) options;
                if (entityOptions.containsItem(newValue)) {
                    entityOptions.updateItem(newValue);
                }
                if (lookupPickerField.isRefreshOptionsOnLookupClose()) {
                    entityOptions.refresh();
                }
            }
        }

        // In case of PickerField set the value as if the user had set it
        if (field instanceof SupportsUserAction) {
            ((SupportsUserAction<E>) field).setValueFromUser((E) newValue);
        } else {
            field.setValue((E) newValue);
        }
    }

    protected <E extends Entity> void handleSelectionWithContainer(LookupBuilder<E> builder,
                                                                   CollectionContainer<E> collectionDc,
                                                                   Collection<E> itemsFromLookup) {
        if (itemsFromLookup.isEmpty()) {
            return;
        }

        Collection<E> selectedItems = transform(itemsFromLookup, builder);

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
        View viewForCollectionContainer = clientConfig.getReloadUnfetchedAttributesFromLookupScreens() &&
                collectionDc.getEntityMetaClass() != null && metadataTools.isPersistent(collectionDc.getEntityMetaClass()) ?
                getViewForCollectionContainer(collectionDc, initializeMasterReference, inverseMetaProperty) :
                null;
        for (E item : selectedItems) {
            if (!collectionDc.containsItem(item.getId())) {
                if (viewForCollectionContainer != null && !entityStates.isLoadedWithView(item, viewForCollectionContainer)) {
                    item = dataManager.reload(item, viewForCollectionContainer);
                }
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

    protected <E extends Entity> Collection<E> transform(Collection<E> selectedItems, LookupBuilder<E> builder) {
        if (builder.getTransformation() != null) {
            return builder.getTransformation().apply(selectedItems);
        }
        return selectedItems;
    }

    /**
     * If the value for a component (e.g. {@link com.haulmont.cuba.gui.components.PickerField}) is selected from lookup screen then there may be cases
     * when in entities in lookup screen some attributes required in the editor are not loaded.
     * <p>
     * The method evaluates the view that is used for the entity in the given {@code field}
     *
     * @return a view or null if the view cannot be evaluated
     */
    @Nullable
    protected <E extends Entity> View getViewForField(HasValue<E> field) {
        if (field instanceof HasValueSource) {
            ValueSource valueSource = ((HasValueSource) field).getValueSource();
            if (valueSource instanceof ContainerValueSource) {
                ContainerValueSource containerValueSource = (ContainerValueSource) valueSource;
                InstanceContainer<E> container = containerValueSource.getContainer();
                View view = container.getView();
                if (view != null) {
                    MetaPropertyPath metaPropertyPath = containerValueSource.getMetaPropertyPath();
                    View curView = view;
                    for (MetaProperty metaProperty : metaPropertyPath.getMetaProperties()) {
                        ViewProperty viewProperty = curView.getProperty(metaProperty.getName());
                        if (viewProperty != null) {
                            curView = viewProperty.getView();
                        }
                        if (curView == null) break;
                    }
                    if (curView != view) {
                        return curView;
                    }
                }
            }
        }
        return null;
    }

    /**
     * See {@link #getViewForField(HasValue)} javadoc.
     *
     * @return a view or null if the view cannot be evaluated
     */
    @Nullable
    protected <E extends Entity> View getViewForCollectionContainer(CollectionContainer<E> collectionDc,
                                                                    boolean initializeMasterReference,
                                                                    MetaProperty inverseMetaProperty) {
        View view = null;
        if (collectionDc instanceof Nested) {
            InstanceContainer masterDc = ((Nested) collectionDc).getMaster();
            View masterView = masterDc.getView();
            if (masterView != null) {
                String property = ((Nested) collectionDc).getProperty();
                ViewProperty viewProperty = masterView.getProperty(property);
                if (viewProperty != null) {
                    view = viewProperty.getView();
                    if (view != null && initializeMasterReference && inverseMetaProperty != null) {
                        view.addProperty(inverseMetaProperty.getName());
                    }
                }
            }
        } else {
            view = collectionDc.getView();
        }
        return view;
    }
}
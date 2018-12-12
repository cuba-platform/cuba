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
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.SupportsUserAction;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

@Component("cuba_LookupBuilderProcessor")
public class LookupBuilderProcessor {

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected ExtendedEntities extendedEntities;

    @SuppressWarnings("unchecked")
    public <E extends Entity> Screen buildLookup(LookupBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = getScreenContext(origin).getScreens();

        Screen screen;

        if (builder instanceof LookupClassBuilder) {
            Class screenClass = ((LookupClassBuilder) builder).getScreenClass();
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

        return screen;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> void handleSelectionWithField(@SuppressWarnings("unused") LookupBuilder<E> builder,
                                                               HasValue<E> field, Collection<E> selectedItems) {
        if (!selectedItems.isEmpty()) {
            Entity newValue = selectedItems.iterator().next();
            // In case of PickerField set the value as if the user had set it
            if (field instanceof SupportsUserAction) {
                ((SupportsUserAction<E>) field).setValueFromUser((E) newValue);
            } else {
                field.setValue((E) newValue);
            }
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
}
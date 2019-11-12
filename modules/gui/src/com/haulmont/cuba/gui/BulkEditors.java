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

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.app.core.bulk.BulkEditorWindow;
import com.haulmont.cuba.gui.app.core.bulk.ColumnsMode;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.HasLoader;
import com.haulmont.cuba.gui.screen.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

/**
 * A bean that creates an instance of {@link EditorBuilder}.
 */
@Component("cuba_BulkEditors")
public class BulkEditors {

    private static final Logger log = LoggerFactory.getLogger(BulkEditors.class);

    /**
     * Field sorter for bulk editor window.
     */
    @FunctionalInterface
    public interface FieldSorter {

        /**
         * Sorts properties from bulk editor window.
         *
         * @param properties properties from bulk editor window to be sort
         * @return map with metaProperties and their indexes
         */
        Map<MetaProperty, Integer> sort(List<MetaProperty> properties);
    }

    @Inject
    protected WindowConfig windowConfig;

    public <E extends Entity> EditorBuilder<E> builder(MetaClass metaClass,
                                                       Collection<E> entities, FrameOwner origin) {
        checkNotNullArgument(metaClass);
        checkNotNullArgument(entities);
        checkNotNullArgument(origin);

        return new EditorBuilder<>(metaClass, entities, origin, this::buildEditor);
    }

    protected <E extends Entity> BulkEditorWindow buildEditor(EditorBuilder<E> builder) {
        FrameOwner origin = builder.getOrigin();
        Screens screens = getScreenContext(origin).getScreens();

        if (CollectionUtils.isEmpty(builder.getEntities())) {
            throw new IllegalStateException(String.format("BulkEditor of %s cannot be open with no entities were set",
                    builder.getMetaClass()));
        }

        ScreenOptions options = new MapScreenOptions(ParamsMap.of()
                .pair("metaClass", builder.getMetaClass())
                .pair("selected", builder.getEntities())
                .pair("exclude", builder.getExclude())
                .pair("includeProperties", builder.getIncludeProperties() != null
                        ? builder.getIncludeProperties()
                        : Collections.emptyList())
                .pair("fieldValidators", builder.getFieldValidators())
                .pair("modelValidators", builder.getModelValidators())
                .pair("loadDynamicAttributes", builder.isLoadDynamicAttributes())
                .pair("useConfirmDialog", builder.isUseConfirmDialog())
                .pair("fieldSorter", builder.getFieldSorter())
                .pair("columnsMode", builder.getColumnsMode())
                .create());

        BulkEditorWindow bulkEditorWindow = (BulkEditorWindow) screens.create("bulkEditor", builder.launchMode, options);

        bulkEditorWindow.addAfterCloseListener(afterCloseEvent -> {
            ListComponent<E> listComponent = builder.getListComponent();
            CloseAction closeAction = afterCloseEvent.getCloseAction();
            if (isCommitCloseAction(closeAction)
                    && listComponent != null) {
                refreshItems(listComponent.getItems());
            }
            if (listComponent instanceof com.haulmont.cuba.gui.components.Component.Focusable) {
                ((com.haulmont.cuba.gui.components.Component.Focusable) listComponent).focus();
            }
        });

        return bulkEditorWindow;
    }

    protected <E extends Entity> void refreshItems(DataUnit dataSource) {
        CollectionContainer<E> container = dataSource instanceof ContainerDataUnit ?
                ((ContainerDataUnit) dataSource).getContainer() : null;
        if (container != null) {
            DataLoader loader = null;
            if (container instanceof HasLoader) {
                loader = ((HasLoader) container).getLoader();
            }
            if (loader != null) {
                loader.load();
            } else {
                log.warn("Target container has no loader, refresh is impossible");
            }
        }
    }

    protected boolean isCommitCloseAction(CloseAction closeAction) {
        return (closeAction instanceof StandardCloseAction)
                && ((StandardCloseAction) closeAction).getActionId().equals(Window.COMMIT_ACTION_ID);
    }

    /**
     * A builder that creates a new {@link BulkEditorWindow} with defined parameters.
     *
     * @param <E> item type
     */
    public static class EditorBuilder<E extends Entity> {

        protected final MetaClass metaClass;
        protected final FrameOwner origin;
        protected final Collection<E> entities;
        protected final Function<EditorBuilder<E>, BulkEditorWindow> handler;

        protected Screens.LaunchMode launchMode = OpenMode.DIALOG;
        protected ListComponent<E> listComponent;

        protected String exclude;
        protected List<String> includeProperties = Collections.emptyList();
        protected Map<String, Field.Validator> fieldValidators;
        protected List<Field.Validator> modelValidators;
        protected Boolean loadDynamicAttributes;
        protected Boolean useConfirmDialog;
        protected FieldSorter fieldSorter;
        protected ColumnsMode columnsMode;

        public EditorBuilder(EditorBuilder<E> builder) {
            this.metaClass = builder.metaClass;
            this.origin = builder.origin;
            this.handler = builder.handler;
            this.entities = builder.entities;

            this.launchMode = builder.launchMode;
            this.listComponent = builder.listComponent;

            this.exclude = builder.exclude;
            this.includeProperties = builder.includeProperties;
            this.fieldValidators = builder.fieldValidators;
            this.modelValidators = builder.modelValidators;
            this.loadDynamicAttributes = builder.loadDynamicAttributes;
            this.useConfirmDialog = builder.useConfirmDialog;
            this.fieldSorter = builder.fieldSorter;
            this.columnsMode = builder.columnsMode;
        }

        public EditorBuilder(MetaClass metaClass, Collection<E> entities, FrameOwner origin,
                             Function<EditorBuilder<E>, BulkEditorWindow> handler) {
            this.metaClass = metaClass;
            this.entities = entities;
            this.origin = origin;
            this.handler = handler;
        }

        /**
         * Sets screen launch mode.
         *
         * @param launchMode the launch mode to set
         * @return this builder
         */
        public EditorBuilder<E> withLaunchMode(Screens.LaunchMode launchMode) {
            this.launchMode = launchMode;
            return this;
        }

        /**
         * Sets the list component that displays the items to be edited.
         *
         * @param listComponent the list component to be used
         * @return this builder
         */
        public EditorBuilder<E> withListComponent(ListComponent<E> listComponent) {
            this.listComponent = listComponent;
            return this;
        }

        /**
         * Sets a regular expression to exclude some fields explicitly
         * from the list of attributes available for editing.
         *
         * @param exclude a regular expression
         * @return this builder
         */
        public EditorBuilder<E> withExclude(String exclude) {
            this.exclude = exclude;
            return this;
        }

        /**
         * Sets the entity attributes to be included to bulk editor window.
         * If set, other attributes will be ignored.
         *
         * @param includeProperties the entity attributes to be included to bulk editor window
         * @return this builder
         */
        public EditorBuilder<E> withIncludeProperties(List<String> includeProperties) {
            this.includeProperties = includeProperties;
            return this;
        }

        /**
         * Sets a map with validators for fields that will be used for editing certain properties.
         *
         * @param fieldValidators a map with validators for fields that will be used for editing certain properties
         * @return this builder
         */
        public EditorBuilder<E> withFieldValidators(Map<String, Field.Validator> fieldValidators) {
            this.fieldValidators = fieldValidators;
            return this;
        }

        /**
         * Sets a map with validators for the result of bulk editing.
         *
         * @param modelValidators a map with validators for the result of bulk editing
         * @return this builder
         */
        public EditorBuilder<E> withModelValidators(List<Field.Validator> modelValidators) {
            this.modelValidators = modelValidators;
            return this;
        }

        /**
         * Sets whether or not the dynamic attributes of the edited entity should be displayed on
         * the entity's bulk editor screen. The default value is true.
         *
         * @param loadDynamicAttributes whether or not the dynamic attributes
         *                              of the edited entity should be displayed
         * @return this builder
         */
        public EditorBuilder<E> withLoadDynamicAttributes(Boolean loadDynamicAttributes) {
            this.loadDynamicAttributes = loadDynamicAttributes;
            return this;
        }

        /**
         * Sets whether or not the confirmation dialog should be displayed to
         * the user before saving the changes. The default value is true.
         *
         * @param useConfirmDialog whether or not the confirmation dialog should be displayed
         * @return this builder
         */
        public EditorBuilder<E> withUseConfirmDialog(Boolean useConfirmDialog) {
            this.useConfirmDialog = useConfirmDialog;
            return this;
        }

        /**
         * Sets field sorter that allows you to sort fields by custom logic.
         *
         * @param fieldSorter field sorter
         * @return this builder
         */
        public EditorBuilder<E> withFieldSorter(FieldSorter fieldSorter) {
            this.fieldSorter = fieldSorter;
            return this;
        }

        /**
         * Sets the columns mode for editor which defines number of columns.
         *
         * @param columnsMode columns mode
         * @return this builder
         * @see ColumnsMode#ONE_COLUMN
         * @see ColumnsMode#TWO_COLUMNS
         */
        public EditorBuilder<E> withColumnsMode(ColumnsMode columnsMode) {
            this.columnsMode = columnsMode;
            return this;
        }

        /**
         * @return a {@link FrameOwner} of bulk editor
         */
        public FrameOwner getOrigin() {
            return origin;
        }

        /**
         * @return a {@link MetaClass} of items
         */
        public MetaClass getMetaClass() {
            return metaClass;
        }

        /**
         * @return a collection of items to be edited
         */
        public Collection<E> getEntities() {
            return entities;
        }

        /**
         * @return screen launch mode
         */
        public Screens.LaunchMode getLaunchMode() {
            return launchMode;
        }

        /**
         * @return the list component that displays the items to be edited
         */
        public ListComponent<E> getListComponent() {
            return listComponent;
        }

        /**
         * @return a regular expression to exclude some fields
         * explicitly from the list of attributes available for editing
         */
        public String getExclude() {
            return exclude;
        }

        /**
         * @return the entity attributes to be included to bulk editor window
         */
        public List<String> getIncludeProperties() {
            return includeProperties;
        }

        /**
         * @return a map with validators for fields that will be used for editing certain properties
         */
        public Map<String, Field.Validator> getFieldValidators() {
            return fieldValidators;
        }

        /**
         * @return a map with validators for the result of bulk editing
         */
        public List<Field.Validator> getModelValidators() {
            return modelValidators;
        }

        /**
         * @return whether or not the dynamic attributes of the edited entity should be displayed on
         * the entity's bulk editor screen
         */
        public Boolean isLoadDynamicAttributes() {
            return loadDynamicAttributes;
        }

        /**
         * @return whether or not the confirmation dialog should be displayed to the user before saving the changes
         */
        public Boolean isUseConfirmDialog() {
            return useConfirmDialog;
        }

        /**
         * @return field sorter
         */
        public FieldSorter getFieldSorter() {
            return fieldSorter;
        }

        /**
         * @return columns mode
         * @see ColumnsMode#ONE_COLUMN
         * @see ColumnsMode#TWO_COLUMNS
         */
        public ColumnsMode getColumnsMode() {
            return columnsMode;
        }

        /**
         * @return a new instance of {@link BulkEditorWindow}
         */
        public BulkEditorWindow create() {
            return handler.apply(this);
        }
    }
}
/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.gui.app.core.entityinspector;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.ReferenceImportBehaviour;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.export.ByteArrayDataProvider;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class EntityInspectorBrowse extends AbstractLookup {

    public interface Companion {
        void setHorizontalScrollEnabled(Table table, boolean enabled);
    }

    public static final String SCREEN_NAME = "entityInspector.browse";
    public static final WindowManager.OpenType WINDOW_OPEN_TYPE = WindowManager.OpenType.THIS_TAB;
    public static final int MAX_TEXT_LENGTH = 50;

    protected static final Logger log = LoggerFactory.getLogger(EntityInspectorBrowse.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected BoxLayout lookupBox;

    @Inject
    protected BoxLayout tableBox;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Inject
    protected DsContext dsContext;

    @Inject
    protected Configuration configuration;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected LookupField entitiesLookup;

    @Inject
    protected CheckBox removedRecords;

    @Inject
    protected BoxLayout filterBox;

    @Inject
    protected ExportDisplay exportDisplay;

    @Inject
    protected EntityImportExportService entityImportExportService;

    @Inject
    protected FileUploadingAPI fileUploadingAPI;

    protected Filter filter;
    protected Table entitiesTable;

    /**
     * Buttons
     */
    protected Button createButton;
    protected Button editButton;
    protected Button removeButton;
    protected Button excelButton;
    protected Button refreshButton;
    protected Button exportButton;
    protected FileUploadField importUpload;

    protected CollectionDatasource entitiesDs;
    protected MetaClass selectedMeta;

    @Override
    public void init(Map<String, Object> params) {
        String entityName = (String) params.get("entity");
        if (entityName != null) {
            Session session = metadata.getSession();
            selectedMeta = session.getClass(entityName);
            createEntitiesTable(selectedMeta);
            if (frame instanceof Lookup) {
                setLookupComponent(entitiesTable);
            }
            lookupBox.setVisible(false);
        } else {
            entitiesLookup.setOptionsMap(getEntitiesLookupFieldOptions());
            entitiesLookup.addValueChangeListener(e -> showEntities());
            removedRecords.addValueChangeListener(e -> showEntities());
        }
    }

    protected Map<String, Object> getEntitiesLookupFieldOptions() {
        Map<String, Object> options = new TreeMap<>();

        for (MetaClass metaClass : metadata.getTools().getAllPersistentMetaClasses()) {
            if (readPermitted(metaClass)) {
                Class javaClass = metaClass.getJavaClass();
                if (Entity.class.isAssignableFrom(javaClass)) {
                    options.put(messages.getTools().getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
                }
            }
        }

        return options;
    }

    private void showEntities() {
        selectedMeta = entitiesLookup.getValue();
        if (selectedMeta != null) {
            createEntitiesTable(selectedMeta);

            //TODO: set tab caption
            //EntityInspectorBrowse.this.setCaption(selectedMeta.getName());
        }
    }

    protected void createEntitiesTable(MetaClass meta) {
        if (entitiesTable != null)
            tableBox.remove(entitiesTable);
        if (filter != null) {
            filterBox.remove(filter);
        }

        entitiesTable = componentsFactory.createComponent(Table.class);
        entitiesTable.setFrame(frame);
        Companion companion = getCompanion();
        if (companion != null) {
            companion.setHorizontalScrollEnabled(entitiesTable, true);
        }

        //collect properties in order to add non-system columns first
        LinkedList<Table.Column> nonSystemPropertyColumns = new LinkedList<>();
        LinkedList<Table.Column> systemPropertyColumns = new LinkedList<>();
        for (MetaProperty metaProperty : meta.getProperties()) {
            //don't show embedded & multiple referred entities
            if (isEmbedded(metaProperty))
                continue;
            if (metaProperty.getRange().getCardinality().isMany())
                continue;

            Table.Column column = new Table.Column(meta.getPropertyPath(metaProperty.getName()));
            if (metaProperty.getJavaType().equals(String.class)) {
                column.setMaxTextLength(MAX_TEXT_LENGTH);
            }

            if (!metadata.getTools().isSystem(metaProperty)) {
                column.setCaption(getPropertyCaption(meta, metaProperty));
                nonSystemPropertyColumns.add(column);
            } else {
                column.setCaption(metaProperty.getName());
                systemPropertyColumns.add(column);
            }
        }
        for (Table.Column column : nonSystemPropertyColumns)
            entitiesTable.addColumn(column);

        for (Table.Column column : systemPropertyColumns)
            entitiesTable.addColumn(column);

        if (entitiesDs != null) {
            ((DsContextImplementation) getDsContext()).unregister(entitiesDs);
        }

        entitiesDs = new DsBuilder(getDsContext())
                .setId("entitiesDs")
                .setMetaClass(meta)
                .setView(createView(meta))
                .buildCollectionDatasource();

        entitiesDs.setLoadDynamicAttributes(true);
        entitiesDs.setSoftDeletion(BooleanUtils.isFalse(removedRecords.getValue()));
        entitiesDs.setQuery(String.format("select e from %s e", meta.getName()));

        entitiesTable.setDatasource(entitiesDs);

        tableBox.add(entitiesTable);
        entitiesTable.setWidth("100%");
        entitiesTable.setHeight("100%");

        createButtonsPanel(entitiesTable);

        RowsCount rowsCount = componentsFactory.createComponent(RowsCount.class);
        rowsCount.setDatasource(entitiesDs);
        entitiesTable.setRowsCount(rowsCount);

        entitiesTable.setEnterPressAction(entitiesTable.getAction("edit"));
        entitiesTable.setItemClickAction(entitiesTable.getAction("edit"));
        entitiesTable.setMultiSelect(true);

        createFilter();
    }

    protected void createFilter() {
        filter = componentsFactory.createComponent(Filter.class);
        filter.setId("filter");
        filter.setFrame(frame);

        filterBox.add(filter);

        filter.setUseMaxResults(true);
        filter.setManualApplyRequired(true);
        filter.setEditable(true);

        filter.setDatasource(entitiesDs);
        ((FilterImplementation)filter).loadFiltersAndApplyDefault();
        filter.apply(true);
    }

    protected boolean isEmbedded(MetaProperty metaProperty) {
        return metaProperty.getAnnotatedElement().isAnnotationPresent(javax.persistence.Embedded.class);
    }

    protected void createButtonsPanel(Table table) {
        ButtonsPanel buttonsPanel = componentsFactory.createComponent(ButtonsPanel.class);

        createButton = componentsFactory.createComponent(Button.class);
        createButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "create"));
        CreateAction createAction = new CreateAction();
        table.addAction(createAction);
        createButton.setAction(createAction);
        createButton.setIcon("icons/create.png");
        createButton.setFrame(frame);

        editButton = componentsFactory.createComponent(Button.class);
        editButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "edit"));
        EditAction editAction = new EditAction();
        table.addAction(editAction);
        editButton.setAction(editAction);
        editButton.setIcon("icons/edit.png");
        editButton.setFrame(frame);

        removeButton = componentsFactory.createComponent(Button.class);
        removeButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "remove"));
        RemoveAction removeAction = new RemoveAction(entitiesTable);
        table.addAction(removeAction);
        removeButton.setAction(removeAction);
        removeButton.setIcon("icons/remove.png");
        removeButton.setFrame(frame);

        excelButton = componentsFactory.createComponent(Button.class);
        excelButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "excel"));
        excelButton.setAction(new ExcelAction(entitiesTable));
        excelButton.setIcon("icons/excel.png");
        excelButton.setFrame(frame);

        refreshButton = componentsFactory.createComponent(Button.class);
        refreshButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "refresh"));
        refreshButton.setAction(new RefreshAction(entitiesTable));
        refreshButton.setIcon("icons/refresh.png");
        refreshButton.setFrame(frame);

        exportButton = componentsFactory.createComponent(Button.class);
        exportButton.setAction(new ExportAction());
        exportButton.setCaption(getMessage("export"));

        importUpload = componentsFactory.createComponent(FileUploadField.class);
        importUpload.setCaption(getMessage("import"));
        importUpload.addFileUploadSucceedListener(event -> {
            File file = fileUploadingAPI.getFile(importUpload.getFileId());
            if (file == null) {
                String errorMsg = String.format("Entities import upload error. File with id %s not found", importUpload.getFileId());
                throw new RuntimeException(errorMsg);
            }
            byte[] zipBytes;
            try (InputStream is = new FileInputStream(file)) {
                zipBytes = IOUtils.toByteArray(is);
            } catch (IOException e) {
                throw new RuntimeException("Unable to upload file", e);
            }
            try {
                fileUploadingAPI.deleteFile(importUpload.getFileId());
            } catch (FileStorageException e) {
                log.error("Unable to delete temp file", e);
            }
            try {
                Collection<Entity> importedEntities = entityImportExportService.importEntities(zipBytes, createEntityImportView(selectedMeta));
                showNotification(importedEntities.size() + " entities imported", NotificationType.HUMANIZED);
            } catch (Exception e) {
                showNotification(getMessage("importFailed"), e.getMessage(), NotificationType.ERROR);
                log.error("Entities import error", e);
            }
            entitiesDs.refresh();
        });

        buttonsPanel.add(createButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(excelButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(exportButton);
        buttonsPanel.add(importUpload);

        table.setButtonsPanel(buttonsPanel);
    }

    protected View createView(MetaClass meta) {
        //noinspection unchecked
        View view = new View(meta.getJavaClass(), false);
        for (MetaProperty metaProperty : meta.getProperties()) {
            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    view.addProperty(metaProperty.getName());
                    break;
                case ASSOCIATION:
                case COMPOSITION:
                    if (!metaProperty.getRange().getCardinality().isMany()) {
                        View minimal = metadata.getViewRepository()
                                .getView(metaProperty.getRange().asClass(), View.MINIMAL);
                        View propView = new View(minimal, metaProperty.getName() + "Ds", false);
                        view.addProperty(metaProperty.getName(), propView);
                    }
                    break;
                default:
                    throw new IllegalStateException("unknown property type");
            }
        }
        return view;
    }

    protected EntityImportView createEntityImportView(MetaClass metaClass) {
        EntityImportView entityImportView = new EntityImportView(metaClass.getJavaClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            switch (metaProperty.getType()) {
                case DATATYPE:
                case ENUM:
                    entityImportView.addProperty(metaProperty.getName());
                    break;
                case ASSOCIATION:
                case COMPOSITION:
                    if (!metaProperty.getRange().getCardinality().isMany()) {
                        entityImportView.addProperty(metaProperty.getName(), ReferenceImportBehaviour.IGNORE_MISSING);
                    }
                    break;
                default:
                    throw new IllegalStateException("unknown property type");
            }
        }
        return entityImportView;
    }

    protected class CreateAction extends AbstractAction {

        public CreateAction() {
            super("create");
            setShortcut(configuration.getConfig(ClientConfig.class).getTableInsertShortcut());
        }

        @Override
        public void actionPerform(Component component) {
            Map<String, Object> editorParams = new HashMap<>();
            editorParams.put("metaClass", selectedMeta.getName());
            Window window = openWindow("entityInspector.edit", WINDOW_OPEN_TYPE, editorParams);
            window.addCloseListener(actionId -> {
                entitiesDs.refresh();
                entitiesTable.requestFocus();
            });
        }
    }

    protected class EditAction extends ItemTrackingAction {

        public EditAction() {
            super("edit");
        }

        @Override
        public void actionPerform(Component component) {
            Set selected = entitiesTable.getSelected();
            if (selected.size() != 1)
                return;

            Entity item = (Entity) selected.toArray()[0];
            Map<String, Object> editorParams = new HashMap<>();
            editorParams.put("item", item);
            Window window = openWindow("entityInspector.edit", WINDOW_OPEN_TYPE, editorParams);
            window.addCloseListener(actionId -> {
                entitiesDs.refresh();
                entitiesTable.requestFocus();
            });
        }
    }

    protected String getPropertyCaption(MetaClass metaClass, MetaProperty metaProperty) {
        return messageTools.getPropertyCaption(metaClass, metaProperty.getName());
    }

    protected boolean readPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.READ);
    }

    protected boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(metaClass, entityOp);
    }

    protected class ExportAction extends ItemTrackingAction {

        public ExportAction() {
            super("export");
        }

        @Override
        public void actionPerform(Component component) {
            Set<Entity> selected = entitiesTable.getSelected();
            if (!selected.isEmpty()) {
                try {
                    exportDisplay.show(new ByteArrayDataProvider(entityImportExportService.exportEntities(selected)),
                            selectedMeta.getJavaClass().getSimpleName() + ".zip", ExportFormat.ZIP);
                } catch (Exception e) {
                    showNotification(getMessage("exportFailed"), e.getMessage(), NotificationType.ERROR);
                    log.error("Entities export failed", e);
                }
            }
        }
    }
}
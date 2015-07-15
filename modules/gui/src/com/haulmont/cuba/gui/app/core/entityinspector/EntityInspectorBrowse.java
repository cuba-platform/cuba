/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entityinspector;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.ExcelAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * @author korotkov
 * @version $Id$
 */
public class EntityInspectorBrowse extends AbstractLookup {

    public interface Companion {
        void setHorizontalScrollEnabled(Table table, boolean enabled);
    }

    public static final String SCREEN_NAME = "entityInspector.browse";
    public static final WindowManager.OpenType WINDOW_OPEN_TYPE = WindowManager.OpenType.THIS_TAB;
    public static final int MAX_TEXT_LENGTH = 50;

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
    protected Filter filter;

    @Inject
    protected Configuration configuration;

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected LookupField entitiesLookup;

    @Inject
    protected CheckBox removedRecords;

    protected Table entitiesTable;

    /**
     * Buttons
     */
    protected Button createButton;
    protected Button editButton;
    protected Button removeButton;
    protected Button excelButton;
    protected Button refreshButton;

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
            entitiesLookup.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                    showEntities();
                }
            });
            removedRecords.addListener(new ValueListener() {
                @Override
                public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                    showEntities();
                }
            });
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

        entitiesTable = componentsFactory.createComponent(Table.NAME);
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
                column.setCaption(getPropertyCaption(metaProperty));
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
        entitiesDs.setSoftDeletion(BooleanUtils.isFalse(removedRecords.<Boolean>getValue()));
        entitiesDs.setQuery(String.format("select e from %s e", meta.getName()));

        entitiesTable.setDatasource(entitiesDs);

        tableBox.add(entitiesTable);
        entitiesTable.setWidth("100%");
        entitiesTable.setHeight("100%");

        createButtonsPanel(entitiesTable);

        RowsCount rowsCount = componentsFactory.createComponent(RowsCount.NAME);
        rowsCount.setDatasource(entitiesDs);
        entitiesTable.setRowsCount(rowsCount);

        entitiesTable.setEnterPressAction(entitiesTable.getAction("edit"));
        entitiesTable.setItemClickAction(entitiesTable.getAction("edit"));
        entitiesTable.setMultiSelect(true);
        filter.setDatasource(entitiesDs);
        filter.setVisible(true);
        ((FilterImplementation)filter).loadFiltersAndApplyDefault();
        filter.apply(true);
    }

    protected boolean isEmbedded(MetaProperty metaProperty) {
        return metaProperty.getAnnotatedElement().isAnnotationPresent(javax.persistence.Embedded.class);
    }

    protected void createButtonsPanel(Table table) {
        ButtonsPanel buttonsPanel = componentsFactory.createComponent("buttonsPanel");

        createButton = componentsFactory.createComponent(Button.NAME);
        createButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "create"));
        CreateAction createAction = new CreateAction();
        table.addAction(createAction);
        createButton.setAction(createAction);
        createButton.setIcon("icons/create.png");
        createButton.setFrame(frame);

        editButton = componentsFactory.createComponent(Button.NAME);
        editButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "edit"));
        EditAction editAction = new EditAction();
        table.addAction(editAction);
        editButton.setAction(editAction);
        editButton.setIcon("icons/edit.png");
        editButton.setFrame(frame);

        removeButton = componentsFactory.createComponent(Button.NAME);
        removeButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "remove"));
        RemoveAction removeAction = new RemoveAction(entitiesTable);
        table.addAction(removeAction);
        removeButton.setAction(removeAction);
        removeButton.setIcon("icons/remove.png");
        removeButton.setFrame(frame);

        excelButton = componentsFactory.createComponent(Button.NAME);
        excelButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "excel"));
        excelButton.setAction(new ExcelAction(entitiesTable));
        excelButton.setIcon("icons/excel.png");
        excelButton.setFrame(frame);

        refreshButton = componentsFactory.createComponent(Button.NAME);
        refreshButton.setCaption(messages.getMessage(EntityInspectorBrowse.class, "refresh"));
        refreshButton.setAction(new RefreshAction(entitiesTable));
        refreshButton.setIcon("icons/refresh.png");
        refreshButton.setFrame(frame);

        buttonsPanel.add(createButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(excelButton);
        buttonsPanel.add(refreshButton);

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
            window.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    entitiesDs.refresh();
                    entitiesTable.requestFocus();
                }
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
            window.addListener(new CloseListener() {
                @Override
                public void windowClosed(String actionId) {
                    entitiesDs.refresh();
                    entitiesTable.requestFocus();
                }
            });
        }
    }

    protected String getPropertyCaption(MetaProperty metaProperty) {
        return messageTools.getPropertyCaption(metaProperty);
    }

    protected boolean readPermitted(MetaClass metaClass) {
        return entityOpPermitted(metaClass, EntityOp.READ);
    }

    protected boolean entityOpPermitted(MetaClass metaClass, EntityOp entityOp) {
        Security security = AppBeans.get(Security.NAME);
        return security.isEntityOpPermitted(metaClass, entityOp);
    }
}
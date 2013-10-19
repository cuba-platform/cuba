/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.ui.core.restore;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.WebConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * @author novikov
 * @version $Id$
 */
public class EntityRestore extends AbstractWindow {

    @Inject
    protected LookupField entities;

    @Inject
    protected Button refreshButton;

    @Inject
    protected Filter primaryFilter;

    @Inject
    protected BoxLayout tablePanel;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected MessageTools messageTools;

    protected GroupDatasource entitiesDs;

    protected Table entitiesTable;

    protected Filter filter;

    protected Button restoreButton;

    @Override
    public void init(Map<String, Object> params) {
        refreshButton.setAction(new AbstractAction("refresh") {
            @Override
            public void actionPerform(Component component) {
                buildLayout();
            }

            @Override
            public String getCaption() {
                return getMessage("actions.Refresh");
            }
        });

        Window layout = getComponent();
        layout.remove(primaryFilter);

        entities.setOptionsMap(getEntitiesLookupFieldOptions());
    }

    private void buildLayout() {
        Object value = entities.getValue();
        if (value != null) {
            MetaClass metaClass = (MetaClass) value;
            MetaProperty deleteTsMetaProperty = metaClass.getProperty("deleteTs");
            if (deleteTsMetaProperty != null) {
                if (entitiesTable != null)
                    tablePanel.remove(entitiesTable);
                if (filter != null)
                    tablePanel.remove(filter);

                ComponentsFactory componentsFactory = AppConfig.getFactory();

                entitiesTable = componentsFactory.createComponent(Table.NAME);
                entitiesTable.setFrame(frame);

                restoreButton = componentsFactory.createComponent(Button.NAME);
                restoreButton.setId("restore");
                restoreButton.setCaption(getMessage("entityRestore.restore"));

                ButtonsPanel buttonsPanel = componentsFactory.createComponent(ButtonsPanel.NAME);
                buttonsPanel.addButton(restoreButton);
                entitiesTable.setButtonsPanel(buttonsPanel);

                RowsCount rowsCount = componentsFactory.createComponent(RowsCount.NAME);
                entitiesTable.setRowsCount(rowsCount);

                //collect properties in order to add non-system columns first
                LinkedList<Table.Column> nonSystemPropertyColumns = new LinkedList<Table.Column>();
                LinkedList<Table.Column> systemPropertyColumns = new LinkedList<Table.Column>();
                for (MetaProperty metaProperty : metaClass.getProperties()) {
                    //don't show embedded & multiple referred entities
                    if (isEmbedded(metaProperty))
                        continue;

                    if (metaProperty.getRange().getCardinality().isMany())
                        continue;

                    Table.Column column = new Table.Column(metaClass.getPropertyPath(metaProperty.getName()));
                    if (!metadataTools.isSystem(metaProperty)) {
                        column.setCaption(getPropertyCaption(metaClass, metaProperty));
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

                entitiesDs = new DsBuilder(getDsContext())
                        .setId("entitiesDs")
                        .setMetaClass(metaClass)
                        .setViewName(View.LOCAL)
                        .buildGroupDatasource();

                entitiesDs.setQuery("select e from " + metaClass.getName() + " e " +
                        "where e.deleteTs is not null order by e.deleteTs");

                entitiesDs.setSoftDeletion(false);
                entitiesDs.refresh();
                entitiesTable.setDatasource(entitiesDs);

                filter = componentsFactory.createComponent(Filter.NAME);
                filter.setId("primaryFilter");
                filter.setFrame(getFrame());
                filter.setStyleName(primaryFilter.getStyleName());
                filter.setXmlDescriptor(primaryFilter.getXmlDescriptor());
                filter.setUseMaxResults(true);
                filter.setDatasource(entitiesDs);

                entitiesTable.setWidth("100%");
                entitiesTable.setHeight("100%");
                entitiesTable.setMultiSelect(true);
                entitiesTable.addAction(new AbstractAction("restore") {
                    @Override
                    public void actionPerform(Component component) {
                        showRestoreDialog();
                    }

                    @Override
                    public String getCaption() {
                        return getMessage("entityRestore.restore");
                    }
                });

                restoreButton.setAction(entitiesTable.getAction("restore"));

                tablePanel.add(filter);
                tablePanel.add(entitiesTable);
                tablePanel.expand(entitiesTable, "100%", "100%");

                entitiesTable.refresh();

                filter.loadFiltersAndApplyDefault();
            }
        }
    }

    private void showRestoreDialog() {
        final Set<Entity> listEntity = entitiesTable.getSelected();
        Entity entity = entitiesDs.getItem();
        if (listEntity != null && entity != null && listEntity.size() > 0) {
            if (entity instanceof SoftDelete) {
                showOptionDialog(
                        getMessage("dialogs.Confirmation"),
                        getMessage("dialogs.Message"),
                        MessageType.CONFIRMATION,
                        new Action[]{
                                new DialogAction(DialogAction.Type.OK) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        for (Entity ent : listEntity) {
                                            SoftDelete d = (SoftDelete) ent;
                                            d.setDeleteTs(null);
                                        }
                                        entitiesDs.commit();
                                        entitiesTable.refresh();
                                    }
                                },
                                new DialogAction(DialogAction.Type.CANCEL)
                        }
                );
            }
        } else {
            showNotification(getMessage("entityRestore.restoreMsg"), NotificationType.HUMANIZED);
        }
    }

    private boolean isEmbedded(MetaProperty metaProperty) {
        return metaProperty.getAnnotatedElement().isAnnotationPresent(javax.persistence.Embedded.class);
    }

    private String getPropertyCaption(MetaClass meta, MetaProperty metaProperty) {
        return messageTools.getPropertyCaption(meta, metaProperty.getFullName());
    }

    protected Map<String, Object> getEntitiesLookupFieldOptions() {
        List<String> restoreEntities = new ArrayList<String>();
        String restoreEntitiesProp = ConfigProvider.getConfig(WebConfig.class).getRestoreEntityId();
        if (StringUtils.isNotBlank(restoreEntitiesProp))
            restoreEntities.addAll(Arrays.asList(StringUtils.split(restoreEntitiesProp, ',')));

        Map<String, Object> options = new TreeMap<String, Object>();

        for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
            Boolean enableRestore = (Boolean) metaClass.getAnnotations().get(EnableRestore.class.getName());
            if (BooleanUtils.isTrue(enableRestore) || restoreEntities.contains(metaClass.getName())) {
                options.put(messageTools.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
            }
        }
        return options;
    }
}
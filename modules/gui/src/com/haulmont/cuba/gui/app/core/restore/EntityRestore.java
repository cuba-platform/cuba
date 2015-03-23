/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.app.core.restore;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.inject.Inject;
import java.lang.reflect.AnnotatedElement;
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
    protected BoxLayout tablePanel;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected ViewRepository viewRepository;

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
        entities.setOptionsMap(getEntitiesLookupFieldOptions());
    }

    protected void buildLayout() {
        Object value = entities.getValue();
        if (value != null) {
            MetaClass metaClass = (MetaClass) value;
            MetaProperty deleteTsMetaProperty = metaClass.getProperty("deleteTs");
            if (deleteTsMetaProperty != null) {
                if (entitiesTable != null) {
                    tablePanel.remove(entitiesTable);
                }
                if (filter != null) {
                    tablePanel.remove(filter);
                }

                ComponentsFactory componentsFactory = AppConfig.getFactory();

                entitiesTable = componentsFactory.createComponent(Table.NAME);
                entitiesTable.setFrame(frame);

                restoreButton = componentsFactory.createComponent(Button.NAME);
                restoreButton.setId("restore");
                restoreButton.setCaption(getMessage("entityRestore.restore"));

                ButtonsPanel buttonsPanel = componentsFactory.createComponent(ButtonsPanel.NAME);
                buttonsPanel.add(restoreButton);
                entitiesTable.setButtonsPanel(buttonsPanel);

                RowsCount rowsCount = componentsFactory.createComponent(RowsCount.NAME);
                entitiesTable.setRowsCount(rowsCount);

                //collect properties in order to add non-system columns first
                LinkedList<Table.Column> nonSystemPropertyColumns = new LinkedList<>();
                LinkedList<Table.Column> systemPropertyColumns = new LinkedList<>();
                List<MetaProperty> metaProperties = new ArrayList<>();
                for (MetaProperty metaProperty : metaClass.getProperties()) {
                    //don't show embedded & multiple referred entities
                    Range range = metaProperty.getRange();
                    if (isEmbedded(metaProperty)
                        || range.getCardinality().isMany()
                        || metadataTools.isSystemLevel(metaProperty)
                        || (range.isClass() && metadataTools.isSystemLevel(range.asClass()))) {
                        continue;
                    }

                    metaProperties.add(metaProperty);
                    Table.Column column = new Table.Column(metaClass.getPropertyPath(metaProperty.getName()));
                    if (!metadataTools.isSystem(metaProperty)) {
                        column.setCaption(getPropertyCaption(metaClass, metaProperty));
                        nonSystemPropertyColumns.add(column);
                    } else {
                        column.setCaption(metaProperty.getName());
                        systemPropertyColumns.add(column);
                    }
                }

                for (Table.Column column : nonSystemPropertyColumns) {
                    entitiesTable.addColumn(column);
                }

                for (Table.Column column : systemPropertyColumns) {
                    entitiesTable.addColumn(column);
                }

                DsContext dsContext = getDsContext();
                if (entitiesDs != null) {
                    ((DsContextImplementation) dsContext).unregister(entitiesDs);
                }

                entitiesDs = new DsBuilder(dsContext)
                        .setId("entitiesDs")
                        .setMetaClass(metaClass)
                        .setView(buildView(metaClass, metaProperties))
                        .buildGroupDatasource();

                entitiesDs.setQuery("select e from " + metaClass.getName() + " e " +
                        "where e.deleteTs is not null order by e.deleteTs");

                entitiesDs.setSoftDeletion(false);
                entitiesDs.refresh();
                entitiesTable.setDatasource(entitiesDs);

                String filterId = metaClass.getName().replace("$", "") + "GenericFilter";

                filter = componentsFactory.createComponent(Filter.NAME);
                filter.setId(filterId);
                filter.setFrame(getFrame());

                StringBuilder sb = new StringBuilder("");
                for (MetaProperty property : metaClass.getProperties()) {
                    AnnotatedElement annotatedElement = property.getAnnotatedElement();
                    if (annotatedElement.getAnnotation(com.haulmont.chile.core.annotations.MetaProperty.class) != null) {
                        sb.append(property.getName()).append("|");
                    }
                }
                Element filterElement = Dom4j.readDocument(String.format(
                        "<filter id=\"%s\">\n" +
                        "    <properties include=\".*\" exclude=\"\"/>\n" +
                        "</filter>", filterId)).getRootElement();

                String excludedProperties = sb.toString();
                if (StringUtils.isNotEmpty(excludedProperties)) {
                    Element properties = filterElement.element("properties");
                    properties.attribute("exclude").setValue(excludedProperties
                            .substring(0, excludedProperties.lastIndexOf("|")));
                }
                filter.setXmlDescriptor(filterElement);
                filter.setUseMaxResults(true);
                filter.setDatasource(entitiesDs);

                entitiesTable.setWidth("100%");
                entitiesTable.setHeight("100%");
                entitiesTable.setMultiSelect(true);
                entitiesTable.addAction(new ItemTrackingAction("restore") {
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

                ( (FilterImplementation)filter).loadFiltersAndApplyDefault();
            }
        }
    }

    protected View buildView(MetaClass metaClass, List<MetaProperty> props) {
        View view = new View(metaClass.getJavaClass());
        for (MetaProperty property : props) {
            if (Entity.class.isAssignableFrom(property.getJavaType())) {
                view.addProperty(property.getName(),
                        viewRepository.getView((Class) property.getJavaType(), View.MINIMAL));
            } else {
                view.addProperty(property.getName());
            }
        }
        return view;
    }

    protected void showRestoreDialog() {
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

                                        entitiesTable.requestFocus();
                                    }
                                },
                                new DialogAction(DialogAction.Type.CANCEL) {
                                    @Override
                                    public void actionPerform(Component component) {
                                        entitiesTable.requestFocus();
                                    }
                                }
                        }
                );
            }
        } else {
            showNotification(getMessage("entityRestore.restoreMsg"), NotificationType.HUMANIZED);
        }
    }

    protected boolean isEmbedded(MetaProperty metaProperty) {
        return metaProperty.getAnnotatedElement().isAnnotationPresent(javax.persistence.Embedded.class);
    }

    protected String getPropertyCaption(MetaClass meta, MetaProperty metaProperty) {
        return messageTools.getPropertyCaption(meta, metaProperty.getName());
    }

    protected Map<String, Object> getEntitiesLookupFieldOptions() {
        Map<String, Object> options = new TreeMap<>();
        for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
            Boolean enableRestore = (Boolean) metaClass.getAnnotations().get(EnableRestore.class.getName());
            if (BooleanUtils.isTrue(enableRestore)) {
                options.put(messageTools.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
            }
        }
        return options;
    }
}
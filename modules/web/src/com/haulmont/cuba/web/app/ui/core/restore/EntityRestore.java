/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 27.08.2010 9:22:20
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.core.restore;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.EnableRestore;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.data.impl.GroupDatasourceImpl;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class EntityRestore extends AbstractWindow {

    protected LookupField entities;
    protected GroupDatasourceImpl entitiesDs;
    protected Table entitiesTable;
    protected Filter filter;
    protected Filter primaryFilter;
    protected WebVBoxLayout tablePanel;
    protected Button refreshButton;
    protected Button restore;

    public EntityRestore(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        entities = getComponent("entities");
        //entitiesDs = getDsContext().get("entitiesDs");
        primaryFilter = getComponent("genericFilter");
        refreshButton = getComponent("refresh");
        refreshButton.setAction(new AbstractAction("refresh"){
            public void actionPerform(Component component) {
                Object value = entities.getValue();
                if (value != null) {
                    MetaClass metaClass = (MetaClass) value;
                    MetaProperty metaProperty = metaClass.getProperty("deleteTs");
                    if (metaProperty != null) {
                        if (entitiesTable != null)
                            tablePanel.remove(entitiesTable);
                        if (restore != null)
                            tablePanel.remove(restore);
                        if (filter != null)
                            tablePanel.remove(filter);
                        entitiesTable = new WebTable();
                        entitiesTable.setFrame(frame);
                        entitiesTable.setRowsCount(new WebRowsCount());
                        for (MetaProperty mp : metaClass.getProperties()) {
                            if (MetaProperty.Type.DATATYPE == mp.getType() /*&& !Arrays.asList(removeFields).contains(mp.getName())*/) {
                                Table.Column column = new Table.Column(metaClass.getPropertyEx(mp.getName()));
                                Class classJava = metaClass.getJavaClass();
                                column.setCaption(MessageProvider.getMessage(classJava, classJava.getSimpleName() + "." + mp.getName()));
                                entitiesTable.addColumn(column);
                            }
                        }
                        entitiesDs = new GroupDatasourceImpl(getDsContext(), new GenericDataService(), "entitiesDs", metaClass, "_local");
                        entitiesDs.setQuery("select e from " + metaClass.getName() + " e where e.deleteTs is not null order by e.deleteTs");
                        entitiesDs.setSoftDeletion(false);
                        entitiesDs.refresh();
                        entitiesTable.setDatasource(entitiesDs);
                        filter = new WebFilter();
                        filter.setId("genericFilter");
                        filter.setFrame(getFrame());
                        filter.setStyleName(primaryFilter.getStyleName());
                        filter.setXmlDescriptor(primaryFilter.getXmlDescriptor());
                        filter.setUseMaxResults(true);
                        filter.setDatasource(entitiesDs);
                        entitiesTable.setWidth("100%");
                        entitiesTable.setHeight("100%");
                        entitiesTable.setMultiSelect(true);
                        entitiesTable.setRowsCount(new WebRowsCount());
                        entitiesTable.addAction(new AbstractAction("restore") {
                            public void actionPerform(Component component) {
                                final Set<Entity> listEntity = entitiesTable.getSelected();
                                Entity entity = entitiesDs.getItem();
                                if (listEntity != null && entity != null && listEntity.size() > 0) {
                                    if (entity instanceof SoftDelete) {
                                        showOptionDialog(
                                                getMessage("dialogs.Confirmation"),
                                                getMessage("dialogs.Message"),
                                                IFrame.MessageType.CONFIRMATION,
                                                new com.haulmont.cuba.gui.components.Action[]{
                                                        new DialogAction(DialogAction.Type.OK) {
                                                            @Override
                                                            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                                                for (Entity ent : listEntity) {
                                                                    SoftDelete d = (SoftDelete) ent;
                                                                    d.setDeleteTs(null);
                                                                    d.setDeletedBy(null);
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

                            @Override
                            public String getCaption() {
                                return getMessage("entityRestore.restore");
                            }
                        });
                        restore = new WebButton();
                        restore.setId("restore");
                        restore.setCaption(getMessage("entityRestore.restore"));
                        restore.setAction(entitiesTable.getAction("restore"));
                        tablePanel.add(filter);
                        tablePanel.add(restore);
                        tablePanel.add(entitiesTable);
                        tablePanel.expand(entitiesTable, "100%", "100%");
                        entitiesTable.refresh();
                        filter.loadFiltersAndApplyDefault();
                    }
                }
            }
            @Override
             public String getCaption() {
                return getMessage("actions.Refresh");
            }
        });
        primaryFilter.setVisible(false);
        tablePanel = getComponent("table-panel");
        entities.setOptionsMap(getEntitiesLookupFieldOptions());
    }

    protected Map<String, Object> getEntitiesLookupFieldOptions() {
        List<String> restoreEntities = new ArrayList<String>();
        String restoreEntitiesProp = ConfigProvider.getConfig(WebConfig.class).getRestoreEntityId();
        if (StringUtils.isNotBlank(restoreEntitiesProp))
            restoreEntities.addAll(Arrays.asList(StringUtils.split(restoreEntitiesProp, ',')));

        Map<String, Object> options = new TreeMap<String, Object>();

        for (MetaClass metaClass : MetadataHelper.getAllPersistentMetaClasses()) {
            Boolean enableRestore = (Boolean) metaClass.getAnnotations().get(EnableRestore.class.getName());
            if (BooleanUtils.isTrue(enableRestore) || restoreEntities.contains(metaClass.getName())) {
                options.put(MessageUtils.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
            }
        }
        return options;
    }
}
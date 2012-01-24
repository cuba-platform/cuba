/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.KeyValueEntity;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class SystemInfoWindow extends AbstractWindow {

    protected Entity instance;
    protected MetaClass metaClass;

    public interface Companion {
        void initInfoTable(Table infoTable);
    }

    @Inject
    protected CollectionDatasource<KeyValueEntity, UUID> paramsDs;

    @Inject
    protected Table infoTable;

    public SystemInfoWindow(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        instance = (Entity) params.get("item");
        metaClass = (MetaClass) params.get("metaClass");

        Companion companion = getCompanion();
        companion.initInfoTable(infoTable);

        // remove all actions
        List<Action> tableActions = new ArrayList<Action>(infoTable.getActions());
        for (Action action : tableActions)
            infoTable.removeAction(action);

        compileInfo();
    }

    private void compileInfo() {
        Class<?> javaClass = metaClass.getJavaClass();
        includeParam("table.showInfoAction.entityName", metaClass.getName());
        includeParam("table.showInfoAction.entityClass", javaClass.getName());

        javax.persistence.Table annotation = javaClass.getAnnotation(javax.persistence.Table.class);
        if (annotation != null)
            includeParam("table.showInfoAction.entityTable", annotation.name());

        if (instance != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            includeParam("table.showInfoAction.id", instance.getId().toString());
            if (instance instanceof Versioned && ((Versioned) instance).getVersion() != null) {
                Integer version = ((Versioned) instance).getVersion();
                includeParam("table.showInfoAction.version", version.toString());
            }

            if (instance instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) instance;
                if (baseEntity.getCreateTs() != null)
                    includeParam("table.showInfoAction.createTs", df.format(((BaseEntity) instance).getCreateTs()));
                if (baseEntity.getCreatedBy() != null)
                    includeParam("table.showInfoAction.createdBy", baseEntity.getCreatedBy());
            }

            if (instance instanceof Updatable) {
                Updatable updatableEntity = (Updatable) instance;
                if (updatableEntity.getUpdateTs() != null)
                    includeParam("table.showInfoAction.updateTs", df.format(updatableEntity.getUpdateTs()));
                if (updatableEntity.getUpdatedBy() != null)
                    includeParam("table.showInfoAction.updatedBy", updatableEntity.getUpdatedBy());
            }

            if (instance instanceof SoftDelete) {
                SoftDelete softDeleteEntity = (SoftDelete) instance;
                if (softDeleteEntity.getDeleteTs() != null)
                    includeParam("table.showInfoAction.deleteTs", df.format(softDeleteEntity.getDeleteTs()));
                if (softDeleteEntity.getDeletedBy() != null)
                    includeParam("table.showInfoAction.deletedBy", softDeleteEntity.getDeletedBy());
            }
        }
    }

    private void includeParam(String messageKey, String value) {
        paramsDs.includeItem(new KeyValueEntity(getMessage(messageKey), value));
    }
}

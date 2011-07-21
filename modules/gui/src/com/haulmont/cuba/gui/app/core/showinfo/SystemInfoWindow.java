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
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class SystemInfoWindow extends AbstractWindow {

    private CollectionDatasource itemDs;
    private CollectionDatasource<KeyValueEntity, UUID> paramsDs;

    public SystemInfoWindow(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        paramsDs = getDsContext().get("paramsDs");
        itemDs = (CollectionDatasource) params.get("itemDs");

        compileInfo();
    }

    private void compileInfo() {
        MetaClass metaClass = itemDs.getMetaClass();
        includeParam("table.showInfoAction.entityName", metaClass.getName());
        includeParam("table.showInfoAction.entityClass", metaClass.getJavaClass().getName());

        javax.persistence.Table annotation = (javax.persistence.Table) metaClass.getJavaClass().getAnnotation(javax.persistence.Table.class);
        if (annotation != null)
            includeParam("table.showInfoAction.entityTable", annotation.name());

        Entity instance = itemDs.getItem();
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

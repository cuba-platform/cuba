/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.KeyValueEntity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityParamsDatasource extends CollectionDatasourceImpl<KeyValueEntity, UUID> {

    private Entity instance;
    private MetaClass instanceMetaClass;

    @Override
    protected void loadData(Map<String, Object> params) {
        if ((instance != null) && (instanceMetaClass != null)) {
            compileInfo();
        }
    }

    private void compileInfo() {
        Class<?> javaClass = instanceMetaClass.getJavaClass();
        includeParam("table.showInfoAction.entityName", instanceMetaClass.getName());
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
        KeyValueEntity keyValueEntity = new KeyValueEntity(MessageProvider.getMessage(getClass(), messageKey), value);
        data.put(keyValueEntity.getId(), keyValueEntity);
    }

    public Entity getInstance() {
        return instance;
    }

    public void setInstance(Entity instance) {
        this.instance = instance;
    }

    public MetaClass getInstanceMetaClass() {
        return instanceMetaClass;
    }

    public void setInstanceMetaClass(MetaClass instanceMetaClass) {
        this.instanceMetaClass = instanceMetaClass;
    }
}

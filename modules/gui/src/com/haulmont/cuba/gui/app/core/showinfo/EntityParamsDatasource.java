/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.showinfo;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class EntityParamsDatasource extends CollectionDatasourceImpl<KeyValueEntity, UUID> {

    protected static final String TIMESTAMP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    protected Entity instance;
    protected MetaClass instanceMetaClass;

    protected Messages messages;

    public EntityParamsDatasource() {
        messages = AppBeans.get(Messages.NAME);
    }

    @Override
    protected void loadData(Map<String, Object> params) {
        if (instance != null
                && instanceMetaClass != null
                && instance.getMetaClass() != null) {
            data.clear();
            compileInfo();
        }
    }

    protected void compileInfo() {
        if (instance instanceof BaseUuidEntity && !PersistenceHelper.isNew(instance)) {
            instance = reloadInstance(instance);
        }

        MetaClass effectiveMetaClass = instance.getMetaClass();
        Class<?> javaClass = effectiveMetaClass.getJavaClass();
        includeParam("table.showInfoAction.entityName", instanceMetaClass.getName());

        if (!StringUtils.equals(effectiveMetaClass.getFullName(), instanceMetaClass.getFullName())) {
            includeParam("table.showInfoAction.entityEffectiveName", effectiveMetaClass.getName());
        }
        includeParam("table.showInfoAction.entityClass", javaClass.getName());

        MetadataTools metaTools = AppBeans.get(MetadataTools.NAME);

        if ((metaTools.isEmbeddable(effectiveMetaClass) || metaTools.isPersistent(effectiveMetaClass))
                && PersistenceHelper.isNew(instance)) {
            includeParam("table.showInfoAction.state", messages.getMessage(getClass(), "table.showInfoAction.isNew"));
        }

        if (metaTools.isEmbeddable(effectiveMetaClass)) {
            includeParam("table.showInfoAction.specificInstance", messages.getMessage(getClass(), "table.showInfoAction.embeddableInstance"));
        } else if (!metaTools.isPersistent(effectiveMetaClass)) {
            includeParam("table.showInfoAction.specificInstance", messages.getMessage(getClass(), "table.showInfoAction.nonPersistentInstance"));
        }

        String dbTable = metaTools.getDatabaseTable(effectiveMetaClass);
        if (dbTable != null) {
            includeParam("table.showInfoAction.entityTable", dbTable);
        }

        if (instance != null) {
            SimpleDateFormat df = new SimpleDateFormat(TIMESTAMP_DATE_FORMAT);

            includeParam("table.showInfoAction.id", instance.getId().toString());
            if (instance instanceof Versioned && ((Versioned) instance).getVersion() != null) {
                Integer version = ((Versioned) instance).getVersion();
                includeParam("table.showInfoAction.version", version.toString());
            }

            if (instance instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) instance;
                if (baseEntity.getCreateTs() != null) {
                    includeParam("table.showInfoAction.createTs", df.format(((BaseEntity) instance).getCreateTs()));
                }
                if (baseEntity.getCreatedBy() != null) {
                    includeParam("table.showInfoAction.createdBy", baseEntity.getCreatedBy());
                }
            }

            if (instance instanceof Updatable) {
                Updatable updatableEntity = (Updatable) instance;
                if (updatableEntity.getUpdateTs() != null) {
                    includeParam("table.showInfoAction.updateTs", df.format(updatableEntity.getUpdateTs()));
                }
                if (updatableEntity.getUpdatedBy() != null) {
                    includeParam("table.showInfoAction.updatedBy", updatableEntity.getUpdatedBy());
                }
            }

            if (instance instanceof SoftDelete) {
                SoftDelete softDeleteEntity = (SoftDelete) instance;
                if (softDeleteEntity.getDeleteTs() != null) {
                    includeParam("table.showInfoAction.deleteTs", df.format(softDeleteEntity.getDeleteTs()));
                }
                if (softDeleteEntity.getDeletedBy() != null) {
                    includeParam("table.showInfoAction.deletedBy", softDeleteEntity.getDeletedBy());
                }
            }
        }
    }

    protected Entity reloadInstance(Entity instance) {
        View reloadView = new View(instance.getMetaClass().getJavaClass(), true);

        if (instance instanceof BaseEntity) {
            reloadView.addProperty("id");
            reloadView.addProperty("createTs");
            reloadView.addProperty("createdBy");
        }

        if (instance instanceof Versioned && ((Versioned) instance).getVersion() != null) {
            reloadView.addProperty("version");
        }

        if (instance instanceof Updatable) {
            reloadView.addProperty("updateTs");
            reloadView.addProperty("updatedBy");
        }

        if (instance instanceof SoftDelete) {
            reloadView.addProperty("deleteTs");
            reloadView.addProperty("deletedBy");
        }

        LoadContext loadContext = new LoadContext(instance.getMetaClass());
        loadContext.setSoftDeletion(false);
        loadContext.setId(instance.getId());
        loadContext.setView(reloadView);

        DataSupplier supplier = getDataSupplier();
        return supplier.load(loadContext);
    }

    protected void includeParam(String messageKey, String value) {
        KeyValueEntity keyValueEntity = new KeyValueEntity(messages.getMessage(getClass(), messageKey), value);
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
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

public class EntityParamsDatasource extends CollectionDatasourceImpl<InfoParamEntity, UUID> {

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

        if (!StringUtils.equals(effectiveMetaClass.getName(), instanceMetaClass.getName())) {
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

            if (instance instanceof Creatable) {
                Creatable creatableEntity = (Creatable) instance;
                if (creatableEntity.getCreateTs() != null) {
                    includeParam("table.showInfoAction.createTs", df.format(((Creatable) instance).getCreateTs()));
                }
                if (creatableEntity.getCreatedBy() != null) {
                    includeParam("table.showInfoAction.createdBy", creatableEntity.getCreatedBy());
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

        reloadView.addProperty("id");

        if (instance instanceof Creatable) {
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
        InfoParamEntity infoParamEntity = new InfoParamEntity(messages.getMessage(getClass(), messageKey), value);
        data.put(infoParamEntity.getId(), infoParamEntity);
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
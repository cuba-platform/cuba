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

package com.haulmont.cuba.core.app.importexport;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.SecurityTokenManager;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

@Component(EntityImportExportAPI.NAME)
public class EntityImportExport implements EntityImportExportAPI {

    @Inject
    protected EntitySerializationAPI entitySerialization;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Inject
    protected PersistenceSecurity persistenceSecurity;

    @Override
    public byte[] exportEntities(Collection<? extends Entity> entities, View view) {
        return exportEntities(reloadEntities(entities, view));
    }

    @Override
    public byte[] exportEntities(Collection<? extends Entity> entities) {
        String json = entitySerialization.toJson(entities, null, EntitySerializationOption.COMPLEX_ID_FORMAT,
                EntitySerializationOption.COMPACT_REPEATED_ENTITIES);
        byte[] jsonBytes = json.getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(byteArrayOutputStream);
        zipOutputStream.setMethod(ZipArchiveOutputStream.STORED);
        zipOutputStream.setEncoding(StandardCharsets.UTF_8.name());
        ArchiveEntry singleDesignEntry = newStoredEntry("entities.json", jsonBytes);
        try {
            zipOutputStream.putArchiveEntry(singleDesignEntry);
            zipOutputStream.write(jsonBytes);
            zipOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            throw new RuntimeException("Error on creating zip archive during entities export", e);
        } finally {
            IOUtils.closeQuietly(zipOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    protected Collection<? extends Entity> reloadEntities(Collection<? extends Entity> entities, View view) {
        List<Object> ids = entities.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());

        Collection<? extends Entity> result;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            MetaClass metaClass = metadata.getClassNN(view.getEntityClass());
            Query query = em.createQuery("select e from " + metaClass.getName() + " e where e.id in :ids")
                    .setParameter("ids", ids)
                    .setView(view);
            result = query.getResultList();
            tx.commit();
        }
        return result;
    }

    protected ArchiveEntry newStoredEntry(String name, byte[] data) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    @Override
    public Collection<Entity> importEntities(byte[] zipBytes, EntityImportView view) {
        Collection<Entity> result = new ArrayList<>();
        Collection<? extends Entity> entities = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream archiveReader = new ZipArchiveInputStream(byteArrayInputStream);
        try {
            try {
                while (archiveReader.getNextZipEntry() != null) {
                    String json = new String(readBytesFromEntry(archiveReader));
                    entities = entitySerialization.entitiesCollectionFromJson(json,
                            null,
                            EntitySerializationOption.COMPLEX_ID_FORMAT, EntitySerializationOption.COMPACT_REPEATED_ENTITIES);
                    result.addAll(importEntities(entities, view));
                }
            } catch (IOException e) {
                throw new RuntimeException("Exception occurred while importing report", e);
            }
        } finally {
            IOUtils.closeQuietly(archiveReader);
        }
        return result;
    }

    protected byte[] readBytesFromEntry(ZipArchiveInputStream archiveReader) throws IOException {
        return IOUtils.toByteArray(archiveReader);
    }

    @Override
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView view) {
        Collection<Entity> result = new ArrayList<>();
        Map<Object, Entity> entitiesToCreate = new HashMap<>();
        Set<Entity> entitiesToRemove = new HashSet<>();
        List<ReferenceInfo> referenceInfoList = new ArrayList<>();
        Map<Object, Entity> loadedEntities = new HashMap<>();

        try (Transaction tx = persistence.getTransaction()) {

            //import is performed in two steps. We have to do so, because imported entity may have a reference to
            //some next imported entity.
            //1. entities that should be created processed first, fields that should be references to existing entities
            //are stored in the referenceInfoList variable
            for (Entity entity : entities) {
                importEntity(entity, view, entitiesToCreate, entitiesToRemove, referenceInfoList);
            }

            //2. references to existing entities are processed
            for (ReferenceInfo referenceInfo : referenceInfoList) {
                processReferenceInfo(referenceInfo, entitiesToCreate, loadedEntities);
            }

            EntityManager em = persistence.getEntityManager();
            for (Entity entity : entitiesToCreate.values()) {
                if (PersistenceHelper.isNew(entity)) {
                    em.persist(entity);
                    result.add(entity);
                } else {
                    if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted()) {
                        ((SoftDelete) entity).setDeleteTs(null);
                    }
                    Entity merged = em.merge(entity);
                    result.add(merged);
                }

                if (entityHasDynamicAttributes(entity)) {
                    dynamicAttributesManagerAPI.storeDynamicAttributes((BaseGenericIdEntity) entity);
                }
            }

            entitiesToRemove.forEach(em::remove);

            tx.commit();
        }
        return result;
    }

    protected Entity importEntity(Entity srcEntity,
                                  EntityImportView view,
                                  Map<Object, Entity> entitiesToCreate,
                                  Set<Entity> entitiesToRemove,
                                  Collection<ReferenceInfo> referenceInfoList) {
        EntityManager em = persistence.getEntityManager();
        //set softDeletion to false because we can import deleted entity, so we'll restore it and update
        em.setSoftDeletion(false);
        Entity dstEntity = em.reload(srcEntity);
        if (dstEntity instanceof BaseGenericIdEntity) {
            byte[] securityToken = BaseEntityInternalAccess.getSecurityToken((BaseGenericIdEntity) srcEntity);
            BaseEntityInternalAccess.setSecurityToken((BaseGenericIdEntity) dstEntity, securityToken);
        }
        MetaClass metaClass = srcEntity.getMetaClass();
        if (dstEntity == null) {
            dstEntity = metadata.create(metaClass);
            dstEntity.setValue("id", srcEntity.getId());
        }

        entitiesToCreate.put(dstEntity.getId(), dstEntity);

        for (EntityImportViewProperty viewProperty : view.getProperties()) {
            MetaProperty metaProperty = metaClass.getPropertyNN(viewProperty.getName());
            if ((metaProperty.getRange().isDatatype() && !"version".equals(metaProperty.getName())) || metaProperty.getRange().isEnum()) {
                dstEntity.setValue(viewProperty.getName(), srcEntity.getValue(viewProperty.getName()));
            } else if (metaProperty.getRange().isClass()) {
                if (metadata.getTools().isEmbedded(metaProperty)) {
                    if (viewProperty.getView() != null) {
                        Entity embeddedEntity = importEmbeddedAttribute(srcEntity, dstEntity, viewProperty, entitiesToCreate, entitiesToRemove, referenceInfoList);
                        dstEntity.setValue(viewProperty.getName(), embeddedEntity);
                    }
                } else if (metaProperty.getRange().getCardinality().isMany()) {
                    importCollectionAttribute(srcEntity, dstEntity, viewProperty, entitiesToCreate, entitiesToRemove, referenceInfoList);
                } else {
                    importReference(srcEntity, dstEntity, viewProperty, entitiesToCreate, entitiesToRemove, referenceInfoList);
                }
            }
        }

        if (entityHasDynamicAttributes(srcEntity)) {
            ((BaseGenericIdEntity) dstEntity).setDynamicAttributes(((BaseGenericIdEntity) srcEntity).getDynamicAttributes());
        }

        return dstEntity;
    }

    private boolean entityHasDynamicAttributes(Entity entity) {
        return entity instanceof BaseGenericIdEntity && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null;
    }

    protected Entity importEmbeddedAttribute(Entity srcEntity,
                                             Entity dstEntity,
                                             EntityImportViewProperty viewProperty,
                                             Map<Object, Entity> entitiesToCreate,
                                             Set<Entity> entitiesToRemove,
                                             Collection<ReferenceInfo> referenceInfoList) {
        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(viewProperty.getName());
        Entity srcEmbeddedEntity = srcEntity.getValue(viewProperty.getName());
        if (srcEmbeddedEntity == null) {
            return null;
        }
        Entity dstEmbeddedEntity = dstEntity.getValue(viewProperty.getName());
        MetaClass embeddedAttrMetaClass = metaProperty.getRange().asClass();
        if (dstEmbeddedEntity == null) {
            dstEmbeddedEntity = metadata.create(embeddedAttrMetaClass);
        }

        for (EntityImportViewProperty vp : viewProperty.getView().getProperties()) {
            MetaProperty mp = embeddedAttrMetaClass.getPropertyNN(vp.getName());
            if ((mp.getRange().isDatatype() && !"version".equals(mp.getName())) || mp.getRange().isEnum()) {
                dstEmbeddedEntity.setValue(vp.getName(), srcEmbeddedEntity.getValue(vp.getName()));
            } else if (mp.getRange().isClass()) {
                if (mp.getRange().getCardinality().isMany()) {
                    importCollectionAttribute(srcEmbeddedEntity, dstEmbeddedEntity, vp, entitiesToCreate, entitiesToRemove, referenceInfoList);
                } else {
                    importReference(srcEmbeddedEntity, dstEmbeddedEntity, vp, entitiesToCreate, entitiesToRemove, referenceInfoList);
                }
            }
        }

        return dstEmbeddedEntity;

    }

    protected void importReference(Entity srcEntity,
                                   Entity dstEntity,
                                   EntityImportViewProperty viewProperty,
                                   Map<Object, Entity> entitiesToCreate,
                                   Set<Entity> entitiesToRemove, Collection<ReferenceInfo> referenceInfoList) {
        Entity srcPropertyValue = srcEntity.<Entity>getValue(viewProperty.getName());
        if (viewProperty.getView() == null) {
            ReferenceInfo referenceInfo = new ReferenceInfo(dstEntity, viewProperty.getName(), srcPropertyValue, viewProperty.getReferenceImportBehaviour());
            referenceInfoList.add(referenceInfo);
        } else {
            Entity dstPropertyValue = importEntity(srcPropertyValue, viewProperty.getView(), entitiesToCreate, entitiesToRemove, referenceInfoList);
            dstEntity.setValue(viewProperty.getName(), dstPropertyValue);
        }
    }

    protected void importCollectionAttribute(Entity srcEntity,
                                             Entity dstEntity,
                                             EntityImportViewProperty viewProperty,
                                             Map<Object, Entity> entitiesToCreate,
                                             Set<Entity> entitiesToRemove,
                                             Collection<ReferenceInfo> referenceInfoList) {
        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(viewProperty.getName());
        MetaProperty inverseMetaProperty = metaProperty.getInverse();
        boolean isComposition = metaProperty.getType() == MetaProperty.Type.COMPOSITION;

//        if (srcEntity instanceof BaseGenericIdEntity) {
//            persistenceSecurity.restoreFilteredData((BaseGenericIdEntity<?>) srcEntity);
//        }

        Multimap<String, UUID> filteredItems = ArrayListMultimap.create();
        if (srcEntity instanceof BaseGenericIdEntity) {
            //create an entity copy here, because filtered items must not be reloaded in the srcEntity for now,
            //we only need a collection of filtered properties
            byte[] securityToken = BaseEntityInternalAccess.getSecurityToken((BaseGenericIdEntity) srcEntity);
            Entity srcEntityCopy = metadata.getTools().deepCopy(srcEntity);
            BaseEntityInternalAccess.setSecurityToken((BaseGenericIdEntity) srcEntityCopy, securityToken);
            persistenceSecurity.restoreFilteredData((BaseGenericIdEntity<?>) srcEntityCopy);
            filteredItems = BaseEntityInternalAccess.getFilteredData((BaseGenericIdEntity) srcEntityCopy);
        }

        Collection<Entity> srcPropertyValue = srcEntity.getValue(viewProperty.getName());
        if (srcPropertyValue == null && filteredItems.isEmpty()) {
            //remove absent items from the composition collection
            if (isComposition) {
                Collection<? extends Entity> value = dstEntity.getValue(viewProperty.getName());
                if (value != null) {
                    entitiesToRemove.addAll(value);
                }
            }
            dstEntity.setValue(viewProperty.getName(), null);
            return;
        }
        Collection<Entity> collection;
        try {
            collection = srcPropertyValue.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error on import entities", e);
        }

        for (Entity srcChildEntity : srcPropertyValue) {
            if (viewProperty.getView() == null) {
                ReferenceInfo referenceInfo = new ReferenceInfo(dstEntity, viewProperty.getName(), srcPropertyValue, viewProperty.getReferenceImportBehaviour());
                referenceInfoList.add(referenceInfo);
            } else {
                //create new referenced entity
                Entity dstChildEntity = importEntity(srcChildEntity, viewProperty.getView(), entitiesToCreate, entitiesToRemove, referenceInfoList);
                if (inverseMetaProperty != null) {
                    dstChildEntity.setValue(inverseMetaProperty.getName(), dstEntity);
                }
                collection.add(dstChildEntity);
            }
        }

        if (isComposition) {
            Collection<? extends Entity> dstValue = dstEntity.getValue(viewProperty.getName());
            if (dstValue != null) {
                Multimap<String, UUID> finalFilteredItems = filteredItems;
                List<? extends Entity> compositionEntitiesToRemove = dstValue.stream()
                        .filter(entity -> !collection.contains(entity) &&
                                (finalFilteredItems == null || !finalFilteredItems.containsValue(entity.getId())))
                        .collect(Collectors.toList());
                entitiesToRemove.addAll(compositionEntitiesToRemove);
            }
        }

        dstEntity.setValue(viewProperty.getName(), collection);
    }

    protected void processReferenceInfo(ReferenceInfo referenceInfo, Map<Object, Entity> entitiesToCreate, Map<Object, Entity> loadedEntities) {
        Entity entity = referenceInfo.getEntity();

        String propertyName = referenceInfo.getPropertyName();

        MetaProperty metaProperty = entity.getMetaClass().getPropertyNN(propertyName);
        if (metaProperty.getRange().getCardinality().isMany()) {
            Collection<Entity> propertyValue = (Collection<Entity>) referenceInfo.getPropertyValue();

            if (propertyValue == null) {
                entity.setValue(propertyName, null);
                return;
            }

            Collection<Entity> collection;
            try {
                collection = propertyValue.getClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error on import entities", e);
            }

            for (Entity childEntity : propertyValue) {
                if (loadedEntities.get(childEntity.getId()) != null) {
                    collection.add(loadedEntities.get(childEntity.getId()));
                } else if (entitiesToCreate.get(childEntity.getId()) != null) {
                    collection.add(entitiesToCreate.get(childEntity.getId()));
                } else {
                    EntityManager em = persistence.getEntityManager();
                    Entity loadedReference = em.reload(childEntity);
                    if (loadedReference == null) {
                        if (referenceInfo.getImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                            throw new EntityImportException("Referenced entity for property '" + propertyName + "' with id = " + entity.getId() + " is missing");
                        }
                    } else {
                        collection.add(loadedReference);
                    }
                    loadedEntities.put(entity.getId(), loadedReference);
                }
            }
            entity.setValue(propertyName, collection);

            //restore filtered data, otherwise they will be lost
            persistenceSecurity.restoreFilteredData((BaseGenericIdEntity<?>) entity);
        } else {
            Entity propertyValue = (Entity) referenceInfo.getPropertyValue();
            if (propertyValue == null) {
                entity.setValue(propertyName, null);
            } else if (loadedEntities.get(propertyValue.getId()) != null) {
                entity.setValue(propertyName, loadedEntities.get(propertyValue.getId()));
            } else if (entitiesToCreate.get(propertyValue.getId()) != null) {
                entity.setValue(propertyName, entitiesToCreate.get(propertyValue.getId()));
            } else {
                EntityManager em = persistence.getEntityManager();
                Entity loadedReference = em.find(propertyValue.getClass(), propertyValue.getId());
                if (loadedReference == null) {
                    if (referenceInfo.getImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                        throw new EntityImportException("Referenced entity for property '" + propertyName + "' with id = " + propertyValue.getId() + " is missing");
                    }
                } else {
                    entity.setValue(propertyName, loadedReference);
                }
                loadedEntities.put(propertyValue.getId(), loadedReference);
            }
        }
    }

    protected class ReferenceInfo {
        protected Entity entity;
        protected String propertyName;
        protected Object propertyValue;
        protected ReferenceImportBehaviour importBehaviour;

        public ReferenceInfo(Entity entity, String propertyName, Object propertyValue, ReferenceImportBehaviour importBehaviour) {
            this.entity = entity;
            this.propertyName = propertyName;
            this.propertyValue = propertyValue;
            this.importBehaviour = importBehaviour;
        }

        public Entity getEntity() {
            return entity;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public Object getPropertyValue() {
            return propertyValue;
        }

        public ReferenceImportBehaviour getImportBehaviour() {
            return importBehaviour;
        }
    }
}
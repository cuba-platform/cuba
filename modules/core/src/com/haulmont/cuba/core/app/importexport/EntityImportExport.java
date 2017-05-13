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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.DataStore;
import com.haulmont.cuba.core.app.RdbmsStore;
import com.haulmont.cuba.core.app.StoreFactory;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
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
    protected DataManager dataManager;

    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Inject
    protected PersistenceSecurity persistenceSecurity;

    @Inject
    protected StoreFactory storeFactory;

    @Inject
    protected ViewRepository viewRepository;

    @Override
    public byte[] exportEntitiesToZIP(Collection<? extends Entity> entities, View view) {
        return exportEntitiesToZIP(reloadEntities(entities, view));
    }

    @Override
    public byte[] exportEntitiesToZIP(Collection<? extends Entity> entities) {
        String json = entitySerialization.toJson(entities, null, EntitySerializationOption.COMPACT_REPEATED_ENTITIES);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

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

    @Override
    public String exportEntitiesToJSON(Collection<? extends Entity> entities, View view) {
        return exportEntitiesToJSON(reloadEntities(entities, view));
    }

    @Override
    public String exportEntitiesToJSON(Collection<? extends Entity> entities) {
        return entitySerialization.toJson(entities, null,
                EntitySerializationOption.COMPACT_REPEATED_ENTITIES, EntitySerializationOption.PRETTY_PRINT);
    }

    protected Collection<? extends Entity> reloadEntities(Collection<? extends Entity> entities, View view) {
        List<Object> ids = entities.stream()
                .map(Entity::getId)
                .collect(Collectors.toList());

        MetaClass metaClass = metadata.getClassNN(view.getEntityClass());
        LoadContext.Query query = LoadContext.createQuery("select e from " + metaClass.getName() + " e where e.id in :ids")
                .setParameter("ids", ids);
        LoadContext<? extends Entity> ctx = LoadContext.create(view.getEntityClass())
                .setQuery(query)
                .setView(view);

        return dataManager.loadList(ctx);
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
    public Collection<Entity> importEntitiesFromJson(String json, EntityImportView view) {
        Collection<Entity> result = new ArrayList<>();
        Collection<? extends Entity> entities = entitySerialization.entitiesCollectionFromJson(json,
                null,
                EntitySerializationOption.COMPACT_REPEATED_ENTITIES);
        result.addAll(importEntities(entities, view));
        return result;
    }

    @Override
    public Collection<Entity> importEntitiesFromZIP(byte[] zipBytes, EntityImportView view) {
        Collection<Entity> result = new ArrayList<>();
        Collection<? extends Entity> entities;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        ZipArchiveInputStream archiveReader = new ZipArchiveInputStream(byteArrayInputStream);
        try {
            try {
                while (archiveReader.getNextZipEntry() != null) {
                    String json = new String(readBytesFromEntry(archiveReader), StandardCharsets.UTF_8);
                    entities = entitySerialization.entitiesCollectionFromJson(json,
                            null,
                            EntitySerializationOption.COMPACT_REPEATED_ENTITIES);
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
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView importView) {
        List<ReferenceInfo> referenceInfoList = new ArrayList<>();
        CommitContext commitContext = new CommitContext();

        //import is performed in two steps. We have to do so, because imported entity may have a reference to
        //the reference that is imported in the same batch.
        //
        //1. entities that should be persisted are processed first, fields that should be references to existing entities
        //are stored in the referenceInfoList variable
        for (Entity srcEntity : entities) {
            View regularView = buildViewFromImportView(importView);
            //set softDeletion to false because we can import deleted entity, so we'll restore it and update
            LoadContext<? extends Entity> ctx = LoadContext.create(srcEntity.getClass())
                    .setSoftDeletion(false)
                    .setView(regularView)
                    .setId(srcEntity.getId());
            Entity dstEntity = dataManager.secure().load(ctx);

            importEntity(srcEntity, dstEntity, importView, regularView, commitContext, referenceInfoList);
        }

        //2. references to existing entities are processed

        //store a list of loaded entities in the collection to prevent unnecessary database requests for searching the
        //same instance
        Set<Entity> loadedEntities = new HashSet<>();
        for (ReferenceInfo referenceInfo : referenceInfoList) {
            processReferenceInfo(referenceInfo, commitContext, loadedEntities);
        }

        for (Entity commitInstance : commitContext.getCommitInstances()) {
            if (!PersistenceHelper.isNew(commitInstance)) {
                if (commitInstance instanceof SoftDelete && ((SoftDelete) commitInstance).isDeleted()) {
                    ((SoftDelete) commitInstance).setDeleteTs(null);
                }
            }
            if (entityHasDynamicAttributes(commitInstance)) {
                dynamicAttributesManagerAPI.storeDynamicAttributes((BaseGenericIdEntity) commitInstance);
            }
        }

        return dataManager.commit(commitContext);
    }

    /**
     * Method imports the entity.
     *
     * @param srcEntity         entity that came to the {@code EntityImportExport} bean
     * @param dstEntity         reloaded srcEntity or null if entity doesn't exist in the database
     * @param importView        importView used for importing the entity
     * @param regularView       view that was used for loading dstEntity
     * @param commitContext     entities that must be commited or deleted will be set to the commitContext
     * @param referenceInfoList list of referenceInfos for further processing
     * @return dstEntity that has fields values from the srcEntity
     */
    protected Entity importEntity(Entity srcEntity,
                                  @Nullable Entity dstEntity,
                                  EntityImportView importView,
                                  View regularView,
                                  CommitContext commitContext,
                                  Collection<ReferenceInfo> referenceInfoList) {
        MetaClass metaClass = srcEntity.getMetaClass();
        if (dstEntity == null) {
            dstEntity = metadata.create(metaClass);
            dstEntity.setValue("id", srcEntity.getId());
        }

        //we must specify a view here because otherwise we may get UnfetchedAttributeException during merge
        commitContext.addInstanceToCommit(dstEntity, regularView);

        for (EntityImportViewProperty importViewProperty : importView.getProperties()) {
            String propertyName = importViewProperty.getName();
            MetaProperty metaProperty = metaClass.getPropertyNN(propertyName);
            if ((metaProperty.getRange().isDatatype() && !"version".equals(metaProperty.getName())) || metaProperty.getRange().isEnum()) {
                dstEntity.setValue(propertyName, srcEntity.getValue(propertyName));
            } else if (metaProperty.getRange().isClass()) {
                View regularPropertyView = regularView.getProperty(propertyName) != null ? regularView.getProperty(propertyName).getView() : null;
                if (metadata.getTools().isEmbedded(metaProperty)) {
                    if (importViewProperty.getView() != null) {
                        Entity embeddedEntity = importEmbeddedAttribute(srcEntity, dstEntity, importViewProperty, regularPropertyView, commitContext, referenceInfoList);
                        dstEntity.setValue(propertyName, embeddedEntity);
                    }
                } else {
                    switch (metaProperty.getRange().getCardinality()) {
                        case MANY_TO_MANY:
                            importManyToManyCollectionAttribute(srcEntity, dstEntity, importViewProperty, regularPropertyView, commitContext, referenceInfoList);
                            break;
                        case ONE_TO_MANY:
                            importOneToManyCollectionAttribute(srcEntity, dstEntity, importViewProperty, regularPropertyView, commitContext, referenceInfoList);
                            break;
                        default:
                            importReference(srcEntity, dstEntity, importViewProperty, regularPropertyView, commitContext, referenceInfoList);
                    }
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

    protected void importReference(Entity srcEntity,
                                   Entity dstEntity,
                                   EntityImportViewProperty importViewProperty,
                                   View regularView,
                                   CommitContext commitContext,
                                   Collection<ReferenceInfo> referenceInfoList) {
        Entity srcPropertyValue = srcEntity.<Entity>getValue(importViewProperty.getName());
        Entity dstPropertyValue = dstEntity.<Entity>getValue(importViewProperty.getName());
        if (importViewProperty.getView() == null) {
            ReferenceInfo referenceInfo = new ReferenceInfo(dstEntity, importViewProperty, srcPropertyValue);
            referenceInfoList.add(referenceInfo);
        } else {
            dstPropertyValue = importEntity(srcPropertyValue, dstPropertyValue, importViewProperty.getView(), regularView, commitContext, referenceInfoList);
            dstEntity.setValue(importViewProperty.getName(), dstPropertyValue);
        }
    }

    protected void importOneToManyCollectionAttribute(Entity srcEntity,
                                                      Entity dstEntity,
                                                      EntityImportViewProperty importViewProperty,
                                                      View regularView,
                                                      CommitContext commitContext,
                                                      Collection<ReferenceInfo> referenceInfoList) {
        String propertyName = importViewProperty.getName();
        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(propertyName);
        MetaProperty inverseMetaProperty = metaProperty.getInverse();

        //filteredItems collection will contain entities filtered by the row-level security
        Multimap<String, UUID> filteredItems = ArrayListMultimap.create();
        if (srcEntity instanceof BaseGenericIdEntity) {
            String storeName = metadata.getTools().getStoreName(srcEntity.getMetaClass());
            DataStore dataStore = storeFactory.get(storeName);

            //row-level security works only for entities from RdbmsStore
            if (dataStore instanceof RdbmsStore) {
                //create an entity copy here, because filtered items must not be reloaded in the srcEntity for now,
                //we only need a collection of filtered properties
                try (Transaction tx = persistence.getTransaction()) {
                    byte[] securityToken = BaseEntityInternalAccess.getSecurityToken((BaseGenericIdEntity) srcEntity);
                    Entity srcEntityCopy = metadata.getTools().deepCopy(srcEntity);
                    BaseEntityInternalAccess.setSecurityToken((BaseGenericIdEntity) srcEntityCopy, securityToken);
                    persistenceSecurity.restoreFilteredData((BaseGenericIdEntity<?>) srcEntityCopy);
                    filteredItems = BaseEntityInternalAccess.getFilteredData((BaseGenericIdEntity) srcEntityCopy);
                    tx.commit();
                }
            }
        }

        Collection<Entity> srcPropertyValue = srcEntity.getValue(propertyName);
        Collection<Entity> dstPropertyValue = dstEntity.getValue(propertyName);
        if (dstPropertyValue == null) dstPropertyValue = new ArrayList<>();
        Collection<Entity> collection;
        try {
            collection = srcPropertyValue.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error on import entities", e);
        }

        if (srcPropertyValue != null) {
            for (Entity srcChildEntity : srcPropertyValue) {
                if (importViewProperty.getView() != null) {
                    //create new referenced entity
                    Entity dstChildEntity = null;
                    for (Entity _entity : dstPropertyValue) {
                        if (_entity.equals(srcChildEntity)) {
                            dstChildEntity = _entity;
                            break;
                        }
                    }
                    dstChildEntity = importEntity(srcChildEntity, dstChildEntity, importViewProperty.getView(), regularView, commitContext, referenceInfoList);
                    if (inverseMetaProperty != null) {
                        dstChildEntity.setValue(inverseMetaProperty.getName(), dstEntity);
                    }
                    collection.add(dstChildEntity);
                }
            }
        }

        if (importViewProperty.getCollectionImportPolicy() == CollectionImportPolicy.REMOVE_ABSENT_ITEMS) {
            Collection<? extends Entity> dstValue = dstEntity.getValue(propertyName);
            if (dstValue != null) {
                Multimap<String, UUID> finalFilteredItems = filteredItems;
                List<? extends Entity> collectionItemsToRemove = dstValue.stream()
                        .filter(entity -> !collection.contains(entity) &&
                                (finalFilteredItems == null || !finalFilteredItems.containsValue(entity.getId())))
                        .collect(Collectors.toList());
                for (Entity _entity : collectionItemsToRemove) {
                    commitContext.addInstanceToRemove(_entity);
                }
            }
        }

        dstEntity.setValue(propertyName, collection);
    }

    protected void importManyToManyCollectionAttribute(Entity srcEntity,
                                                       Entity dstEntity,
                                                       EntityImportViewProperty importViewProperty,
                                                       View regularView,
                                                       CommitContext commitContext,
                                                       Collection<ReferenceInfo> referenceInfoList) {
        Collection<Entity> srcPropertyValue = srcEntity.getValue(importViewProperty.getName());
        Collection<Entity> dstPropertyValue = dstEntity.getValue(importViewProperty.getName());
        if (dstPropertyValue == null) dstPropertyValue = new ArrayList<>();
        if (importViewProperty.getView() != null) {
            //create/update passed entities
            Collection<Entity> collection;
            try {
                collection = srcPropertyValue.getClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error on import entities", e);
            }

            for (Entity srcChildEntity : srcPropertyValue) {
                //create new referenced entity
                Entity dstChildEntity = null;
                for (Entity _entity : dstPropertyValue) {
                    if (_entity.equals(srcChildEntity)) {
                        dstChildEntity = _entity;
                        break;
                    }
                }
                dstChildEntity = importEntity(srcChildEntity, dstChildEntity, importViewProperty.getView(), regularView, commitContext, referenceInfoList);
                collection.add(dstChildEntity);
            }

            if (importViewProperty.getCollectionImportPolicy() == CollectionImportPolicy.KEEP_ABSENT_ITEMS) {
                Collection<Entity> existingCollectionValue = dstEntity.getValue(importViewProperty.getName());
                if (existingCollectionValue != null) {
                    for (Entity existingCollectionItem : existingCollectionValue) {
                        if (!collection.contains(existingCollectionItem)) collection.add(existingCollectionItem);
                    }
                }
            }

            dstEntity.setValue(importViewProperty.getName(), collection);
        } else {
            //create ReferenceInfo objects - they will be parsed later
            Collection<Entity> existingCollectionValue = dstEntity.getValue(importViewProperty.getName());
            if (existingCollectionValue != null) {
                ReferenceInfo referenceInfo = new ReferenceInfo(dstEntity, importViewProperty, srcPropertyValue, existingCollectionValue);
                referenceInfoList.add(referenceInfo);
            }
        }
    }

    protected Entity importEmbeddedAttribute(Entity srcEntity,
                                             Entity dstEntity,
                                             EntityImportViewProperty importViewProperty,
                                             View regularView,
                                             CommitContext commitContext,
                                             Collection<ReferenceInfo> referenceInfoList) {
        String propertyName = importViewProperty.getName();
        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(propertyName);
        Entity srcEmbeddedEntity = srcEntity.getValue(propertyName);
        if (srcEmbeddedEntity == null) {
            return null;
        }
        Entity dstEmbeddedEntity = dstEntity.getValue(propertyName);
        MetaClass embeddedAttrMetaClass = metaProperty.getRange().asClass();
        if (dstEmbeddedEntity == null) {
            dstEmbeddedEntity = metadata.create(embeddedAttrMetaClass);
        }

        for (EntityImportViewProperty vp : importViewProperty.getView().getProperties()) {
            MetaProperty mp = embeddedAttrMetaClass.getPropertyNN(vp.getName());
            if ((mp.getRange().isDatatype() && !"version".equals(mp.getName())) || mp.getRange().isEnum()) {
                dstEmbeddedEntity.setValue(vp.getName(), srcEmbeddedEntity.getValue(vp.getName()));
            } else if (mp.getRange().isClass()) {
                View propertyRegularView = regularView.getProperty(propertyName) != null ? regularView.getProperty(propertyName).getView() : null;
                if (metaProperty.getRange().getCardinality() == Range.Cardinality.ONE_TO_MANY) {
                    importOneToManyCollectionAttribute(srcEmbeddedEntity, dstEmbeddedEntity, vp, propertyRegularView, commitContext, referenceInfoList);
                } else if (metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY) {
                    importManyToManyCollectionAttribute(srcEmbeddedEntity, dstEmbeddedEntity, vp, propertyRegularView, commitContext, referenceInfoList);
                } else {
                    importReference(srcEmbeddedEntity, dstEmbeddedEntity, vp, propertyRegularView, commitContext, referenceInfoList);
                }
            }
        }

        return dstEmbeddedEntity;
    }

    /**
     * Method finds and set a reference value to the entity or throws EntityImportException if ERROR_ON_MISSING policy
     * is violated
     */
    protected void processReferenceInfo(ReferenceInfo referenceInfo, CommitContext commitContext, Set<Entity> loadedEntities) {
        Entity entity = referenceInfo.getEntity();
        String propertyName = referenceInfo.getViewProperty().getName();
        MetaProperty metaProperty = entity.getMetaClass().getPropertyNN(propertyName);
        if (metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY) {
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
                Entity entityFromLoadedEntities = findEntityInCollection(loadedEntities, childEntity);
                if (entityFromLoadedEntities != null) {
                    collection.add(entityFromLoadedEntities);
                } else {
                    Entity entityFromCommitContext = findEntityInCollection(commitContext.getCommitInstances(), childEntity);
                    if (entityFromCommitContext != null) {
                        collection.add(entityFromCommitContext);
                    } else {
                        LoadContext<? extends Entity> ctx = LoadContext.create(childEntity.getClass())
                                .setSoftDeletion(false)
                                .setView(View.MINIMAL)
                                .setId(childEntity.getId());
                        Entity loadedReference = dataManager.load(ctx);
                        if (loadedReference == null) {
                            if (referenceInfo.getViewProperty().getReferenceImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                                throw new EntityImportException("Referenced entity for property '" + propertyName + "' with id = " + entity.getId() + " is missing");
                            }
                        } else {
                            collection.add(loadedReference);
                            loadedEntities.add(loadedReference);
                        }
                    }
                }
            }

            //keep absent collection members if we need it
            if (referenceInfo.getViewProperty().getCollectionImportPolicy() == CollectionImportPolicy.KEEP_ABSENT_ITEMS) {
                Collection<Entity> prevCollectionValue = (Collection<Entity>) referenceInfo.getPrevPropertyValue();
                if (prevCollectionValue != null) {
                    for (Entity prevCollectionItem : prevCollectionValue) {
                        if (!collection.contains(prevCollectionItem)) {
                            collection.add(prevCollectionItem);
                        }
                    }
                }
            }

            entity.setValue(propertyName, collection);

            //row-level security works only for entities from RdbmsStore
            String storeName = metadata.getTools().getStoreName(entity.getMetaClass());
            DataStore dataStore = storeFactory.get(storeName);
            if (dataStore instanceof RdbmsStore) {
                //restore filtered data, otherwise they will be lost
                try (Transaction tx = persistence.getTransaction()) {
                    persistenceSecurity.restoreFilteredData((BaseGenericIdEntity<?>) entity);
                    tx.commit();
                }
            }
            //end of many-to-many processing block
        } else {
            //all other reference types (except many-to-many)
            Entity propertyValue = (Entity) referenceInfo.getPropertyValue();
            if (propertyValue == null) {
                entity.setValue(propertyName, null);
            } else {
                Entity entityFromLoadedEntities = findEntityInCollection(loadedEntities, propertyValue);
                if (entityFromLoadedEntities != null) {
                    entity.setValue(propertyName, entityFromLoadedEntities);
                } else {
                    Entity entityFromCommitContext = findEntityInCollection(commitContext.getCommitInstances(), propertyValue);

                    if (entityFromCommitContext != null) {
                        entity.setValue(propertyName, entityFromCommitContext);
                    } else {
                        LoadContext<? extends Entity> ctx = LoadContext.create(propertyValue.getClass())
                                .setId(propertyValue.getId());
                        dataManager.load(ctx);
                        Entity loadedReference = dataManager.load(ctx);
                        if (loadedReference == null) {
                            if (referenceInfo.getViewProperty().getReferenceImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                                throw new EntityImportException("Referenced entity for property '" + propertyName + "' with id = " + propertyValue.getId() + " is missing");
                            }
                        } else {
                            entity.setValue(propertyName, loadedReference);
                            loadedEntities.add(loadedReference);
                        }
                    }
                }
            }
        }
    }

    /**
     * Method builds a regular {@link View} from the {@link EntityImportView}. The regular view will include all
     * properties defined in the import view.
     */
    protected View buildViewFromImportView(EntityImportView importView) {
        View regularView = new View(importView.getEntityClass());
        MetaClass metaClass = metadata.getClassNN(importView.getEntityClass());
        for (EntityImportViewProperty importViewProperty : importView.getProperties()) {
            EntityImportView importViewPropertyView = importViewProperty.getView();
            if (importViewPropertyView == null) {
                MetaProperty metaProperty = metaClass.getPropertyNN(importViewProperty.getName());
                if (metaProperty.getRange().isClass()) {
                    MetaClass propertyMetaClass = metaProperty.getRange().asClass();
                    regularView.addProperty(importViewProperty.getName(), viewRepository.getView(propertyMetaClass, View.MINIMAL));
                } else {
                    regularView.addProperty(importViewProperty.getName());
                }
            } else {
                regularView.addProperty(importViewProperty.getName(), buildViewFromImportView(importViewPropertyView));
            }
        }
        return regularView;
    }

    @Nullable
    protected Entity findEntityInCollection(Collection<Entity> collection, Entity entity) {
        for (Entity entityFromCollection : collection) {
            if (entityFromCollection.equals(entity)) return entityFromCollection;
        }
        return null;
    }

    protected class ReferenceInfo {
        protected Entity entity;
        protected EntityImportViewProperty viewProperty;
        protected Object propertyValue;
        protected Object prevPropertyValue;

        public ReferenceInfo(Entity entity, EntityImportViewProperty viewProperty, Object propertyValue) {
            this.entity = entity;
            this.viewProperty = viewProperty;
            this.propertyValue = propertyValue;
        }

        public ReferenceInfo(Entity entity, EntityImportViewProperty viewProperty, Object propertyValue, Object prevPropertyValue) {
            this.entity = entity;
            this.viewProperty = viewProperty;
            this.propertyValue = propertyValue;
            this.prevPropertyValue = prevPropertyValue;
        }

        public EntityImportViewProperty getViewProperty() {
            return viewProperty;
        }

        public Object getPrevPropertyValue() {
            return prevPropertyValue;
        }

        public Entity getEntity() {
            return entity;
        }

        public Object getPropertyValue() {
            return propertyValue;
        }
    }
}
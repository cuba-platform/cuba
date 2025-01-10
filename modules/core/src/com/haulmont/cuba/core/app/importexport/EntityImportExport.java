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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.app.DataStore;
import com.haulmont.cuba.core.app.RdbmsStore;
import com.haulmont.cuba.core.app.StoreFactory;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.validation.CustomValidationException;
import com.haulmont.cuba.core.global.validation.groups.RestApiChecks;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.groups.Default;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import static java.lang.String.format;

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

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected GlobalConfig globalConfig;

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
        ZipArchiveEntry singleDesignEntry = newStoredEntry("entities.json", jsonBytes);
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

    protected ZipArchiveEntry newStoredEntry(String name, byte[] data) {
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
        return importEntities(entities, importView, false, false);
    }

    @Override
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView importView, boolean validate) {
        return importEntities(entities, importView, validate, false);
    }

    @Override
    public Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView importView, boolean validate, boolean optimisticLocking) {
        List<ReferenceInfo> referenceInfoList = new ArrayList<>();
        CommitContext commitContext = new CommitContext();
        commitContext.setSoftDeletion(false);

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
                    .setLoadDynamicAttributes(true)
                    .setId(srcEntity.getId())
                    .setAuthorizationRequired(true);
            Entity dstEntity = dataManager.load(ctx);

            importEntity(srcEntity, dstEntity, importView, regularView, commitContext, referenceInfoList, optimisticLocking);
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

        if (validate) {
            commitContext.setValidationMode(CommitContext.ValidationMode.ALWAYS_VALIDATE);
            commitContext.setValidationGroups(Arrays.asList(Default.class, RestApiChecks.class));
        } else {
            commitContext.setValidationMode(CommitContext.ValidationMode.NEVER_VALIDATE);
        }

        //we shouldn't remove entities with the softDeletion = false
        if (!commitContext.getRemoveInstances().isEmpty()) {
            commitContext.setSoftDeletion(true);
        }

        commitContext.setAuthorizationRequired(true);

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
     * @param optimisticLocking whether the passed entity version should be validated before entity is persisted
     * @return dstEntity that has fields values from the srcEntity
     */
    protected Entity importEntity(Entity srcEntity,
                                  @Nullable Entity dstEntity,
                                  EntityImportView importView,
                                  View regularView,
                                  CommitContext commitContext,
                                  Collection<ReferenceInfo> referenceInfoList,
                                  boolean optimisticLocking) {
        MetaClass metaClass = srcEntity.getMetaClass();
        boolean createOp = false;
        if (dstEntity == null) {
            dstEntity = metadata.create(metaClass);
            dstEntity.setValue("id", srcEntity.getId());
            createOp = true;
        }

        //we must specify a view here because otherwise we may get UnfetchedAttributeException during merge
        commitContext.addInstanceToCommit(dstEntity, regularView);

        SecurityState dstSecurityState = null;
        SecurityState srcSecurityState = null;
        if (dstEntity instanceof BaseGenericIdEntity && !createOp) {
            String storeName = metadata.getTools().getStoreName(dstEntity.getMetaClass());
            DataStore dataStore = storeFactory.get(storeName);
            if (RdbmsStore.class.equals(AopUtils.getTargetClass(dataStore))) {
                if (useSecurityToken()) {
                    persistenceSecurity.assertTokenForREST(srcEntity, regularView);
                    persistenceSecurity.restoreSecurityState(srcEntity);
                    srcSecurityState = BaseEntityInternalAccess.getSecurityState(srcEntity);
                }
                persistenceSecurity.restoreSecurityState(dstEntity);
                dstSecurityState = BaseEntityInternalAccess.getSecurityState(dstEntity);
            }
        }

        for (EntityImportViewProperty importViewProperty : importView.getProperties()) {
            String propertyName = importViewProperty.getName();
            MetaProperty metaProperty = metaClass.getPropertyNN(propertyName);
            if (BaseEntityInternalAccess.isHiddenOrReadOnly(dstSecurityState, propertyName)) {
                continue;
            }
            if (BaseEntityInternalAccess.isRequired(dstSecurityState, propertyName) && srcEntity.getValue(propertyName) == null) {
                throw new CustomValidationException(format("Attribute [%s] is required for entity %s", propertyName, srcEntity));
            }
            if (metaProperty.getRange().isDatatype()) {
                if (!"version".equals(metaProperty.getName())) {
                    dstEntity.setValue(propertyName, srcEntity.getValue(propertyName));
                } else if (optimisticLocking){
                    dstEntity.setValue(propertyName, srcEntity.getValue(propertyName));
                }
            } else if (metaProperty.getRange().isEnum()) {
                dstEntity.setValue(propertyName, srcEntity.getValue(propertyName));
            } else if (metaProperty.getRange().isClass()) {
                View regularPropertyView = regularView.getProperty(propertyName) != null ? regularView.getProperty(propertyName).getView() : null;
                if (metadata.getTools().isEmbedded(metaProperty)) {
                    if (importViewProperty.getView() != null) {
                        Entity embeddedEntity = importEmbeddedAttribute(srcEntity, dstEntity, createOp, importViewProperty, regularPropertyView,
                                commitContext, referenceInfoList, optimisticLocking);
                        dstEntity.setValue(propertyName, embeddedEntity);
                    }
                } else {
                    switch (metaProperty.getRange().getCardinality()) {
                        case MANY_TO_MANY:
                            importManyToManyCollectionAttribute(srcEntity, dstEntity, srcSecurityState,
                                    importViewProperty, regularPropertyView, commitContext, referenceInfoList, optimisticLocking);
                            break;
                        case ONE_TO_MANY:
                            importOneToManyCollectionAttribute(srcEntity, dstEntity, srcSecurityState,
                                    importViewProperty, regularPropertyView, commitContext, referenceInfoList, optimisticLocking);
                            break;
                        default:
                            importReference(srcEntity, dstEntity, importViewProperty, regularPropertyView, commitContext, referenceInfoList, optimisticLocking);
                    }
                }
            }
        }

        if (entityHasDynamicAttributes(srcEntity)) {
            if (PersistenceHelper.isNew(dstEntity) && ((BaseGenericIdEntity) dstEntity).getDynamicAttributes() == null) {
                ((BaseGenericIdEntity) dstEntity).setDynamicAttributes(new HashMap<>());
            }
            Map<String, CategoryAttributeValue> srcDynamicAttributes = ((BaseGenericIdEntity) srcEntity).getDynamicAttributes();
            for (Map.Entry<String, CategoryAttributeValue> entry : srcDynamicAttributes.entrySet()) {
                String dynamicAttributeCode = entry.getKey();
                CategoryAttributeValue srcDynamicAttribute = entry.getValue();
                dstEntity.setValue(DynamicAttributesUtils.encodeAttributeCode(dynamicAttributeCode), srcDynamicAttribute.getValue());
            }
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
                                   Collection<ReferenceInfo> referenceInfoList,
                                   boolean optimisticLocking) {
        Entity srcPropertyValue = srcEntity.getValue(importViewProperty.getName());
        Entity dstPropertyValue = dstEntity.getValue(importViewProperty.getName());
        if (importViewProperty.getView() == null) {
            ReferenceInfo referenceInfo = new ReferenceInfo(dstEntity, null, importViewProperty, srcPropertyValue, dstPropertyValue);
            referenceInfoList.add(referenceInfo);
        } else {
            dstPropertyValue = srcPropertyValue != null ?
                    importEntity(srcPropertyValue, dstPropertyValue, importViewProperty.getView(), regularView, commitContext, referenceInfoList,
                            optimisticLocking) :
                    null;
            dstEntity.setValue(importViewProperty.getName(), dstPropertyValue);
        }
    }

    protected void importOneToManyCollectionAttribute(Entity srcEntity,
                                                      Entity dstEntity,
                                                      @Nullable SecurityState srcSecurityState,
                                                      EntityImportViewProperty viewProperty,
                                                      @Nullable View regularView,
                                                      CommitContext commitContext,
                                                      Collection<ReferenceInfo> referenceInfoList,
                                                      boolean optimisticLocking) {
        Collection<Entity> collectionValue = srcEntity.getValue(viewProperty.getName());
        Collection<Entity> prevCollectionValue = dstEntity.getValue(viewProperty.getName());
        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(viewProperty.getName());
        MetaProperty inverseMetaProperty = metaProperty.getInverse();
        Collection dstFilteredIds = getFilteredIds(dstEntity, metaProperty.getName());
        Collection srcFilteredIds = getFilteredIds(srcSecurityState, metaProperty.getName());
        Collection<Entity> newCollectionValue = createNewCollection(metaProperty);
        CollectionCompare.with()
                .onCreate(e -> {
                    if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                        Entity result = importEntity(e, null, viewProperty.getView(), regularView,
                                commitContext, referenceInfoList, optimisticLocking);
                        if (inverseMetaProperty != null) {
                            result.setValue(inverseMetaProperty.getName(), dstEntity);
                        }
                        newCollectionValue.add(result);
                    }
                })
                .onUpdate((src, dst) -> {
                    if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(src))) {
                        Entity result = importEntity(src, dst, viewProperty.getView(), regularView,
                                commitContext, referenceInfoList, optimisticLocking);
                        if (inverseMetaProperty != null) {
                            result.setValue(inverseMetaProperty.getName(), dstEntity);
                        }
                        newCollectionValue.add(result);
                    }
                })
                .onDelete(e -> {
                    Object refId = referenceToEntitySupport.getReferenceId(e);
                    if (viewProperty.getCollectionImportPolicy() == CollectionImportPolicy.REMOVE_ABSENT_ITEMS) {
                        if (!dstFilteredIds.contains(refId) && !srcFilteredIds.contains(refId)) {
                            commitContext.addInstanceToRemove(e);
                        }
                    }
                    if (srcFilteredIds.contains(refId)) {
                        newCollectionValue.add(e);
                    }
                })
                .compare(collectionValue, prevCollectionValue);
        dstEntity.setValue(metaProperty.getName(), newCollectionValue);
    }

    protected void importManyToManyCollectionAttribute(Entity srcEntity,
                                                       Entity dstEntity,
                                                       SecurityState srcSecurityState,
                                                       EntityImportViewProperty viewProperty,
                                                       View regularView,
                                                       CommitContext commitContext,
                                                       Collection<ReferenceInfo> referenceInfoList,
                                                       boolean optimisticLocking) {
        Collection<Entity> collectionValue = srcEntity.getValue(viewProperty.getName());
        Collection<Entity> prevCollectionValue = dstEntity.getValue(viewProperty.getName());
        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(viewProperty.getName());
        Collection dstFilteredIds = getFilteredIds(dstEntity, metaProperty.getName());
        Collection srcFilteredIds = getFilteredIds(dstEntity, metaProperty.getName());

        if (viewProperty.getView() != null) {
            Collection<Entity> newCollectionValue = createNewCollection(metaProperty);
            CollectionCompare.with()
                    .onCreate(e -> {
                        if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                            Entity result = importEntity(e, null, viewProperty.getView(), regularView,
                                    commitContext, referenceInfoList, optimisticLocking);
                            newCollectionValue.add(result);
                        }
                    })
                    .onUpdate((src, dst) -> {
                        if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(src))) {
                            Entity result = importEntity(src, dst, viewProperty.getView(), regularView,
                                    commitContext, referenceInfoList, optimisticLocking);
                            newCollectionValue.add(result);
                        }
                    })
                    .onDelete(e -> {
                        if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                            if (srcFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                                newCollectionValue.add(e);
                            } else if (viewProperty.getCollectionImportPolicy() == CollectionImportPolicy.KEEP_ABSENT_ITEMS) {
                                newCollectionValue.add(e);
                            }
                        }
                    })
                    .compare(collectionValue, prevCollectionValue);
            dstEntity.setValue(metaProperty.getName(), newCollectionValue);
        } else {
            //create ReferenceInfo objects - they will be parsed later
            ReferenceInfo referenceInfo = new ReferenceInfo(dstEntity, srcSecurityState, viewProperty, collectionValue, prevCollectionValue);
            referenceInfoList.add(referenceInfo);
        }
    }

    protected Entity importEmbeddedAttribute(Entity srcEntity,
                                             Entity dstEntity,
                                             boolean createOp,
                                             EntityImportViewProperty importViewProperty,
                                             View regularView,
                                             CommitContext commitContext,
                                             Collection<ReferenceInfo> referenceInfoList,
                                             boolean optimisticLock) {
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

        SecurityState dstSecurityState = null;
        SecurityState srcSecurityState = null;
        if (dstEntity instanceof BaseGenericIdEntity && !createOp) {
            String storeName = metadata.getTools().getStoreName(dstEntity.getMetaClass());
            DataStore dataStore = storeFactory.get(storeName);
            //row-level security works only for entities from RdbmsStore
            if (RdbmsStore.class.equals(AopUtils.getTargetClass(dataStore))) {
                if (useSecurityToken()) {
                    persistenceSecurity.assertTokenForREST(srcEmbeddedEntity, regularView);
                    persistenceSecurity.restoreSecurityState(srcEmbeddedEntity);
                    srcSecurityState = BaseEntityInternalAccess.getSecurityState(srcEmbeddedEntity);
                }
                persistenceSecurity.restoreSecurityState(dstEmbeddedEntity);
                dstSecurityState = BaseEntityInternalAccess.getSecurityState(dstEmbeddedEntity);
            }
        }

        for (EntityImportViewProperty vp : importViewProperty.getView().getProperties()) {
            MetaProperty mp = embeddedAttrMetaClass.getPropertyNN(vp.getName());
            if (BaseEntityInternalAccess.isHiddenOrReadOnly(dstSecurityState, mp.getName())) {
                continue;
            }
            if (BaseEntityInternalAccess.isRequired(dstSecurityState, mp.getName()) && srcEmbeddedEntity.getValue(mp.getName()) == null) {
                throw new CustomValidationException(format("Attribute [%s] is required for entity %s", mp.getName(), srcEmbeddedEntity));
            }
            if ((mp.getRange().isDatatype() && !"version".equals(mp.getName())) || mp.getRange().isEnum()) {
                dstEmbeddedEntity.setValue(vp.getName(), srcEmbeddedEntity.getValue(vp.getName()));
            } else if (mp.getRange().isClass()) {
                View propertyRegularView = regularView.getProperty(propertyName) != null ? regularView.getProperty(propertyName).getView() : null;
                if (metaProperty.getRange().getCardinality() == Range.Cardinality.ONE_TO_MANY) {
                    importOneToManyCollectionAttribute(srcEmbeddedEntity, dstEmbeddedEntity, srcSecurityState,
                            vp, propertyRegularView, commitContext, referenceInfoList, optimisticLock);
                } else if (metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY) {
                    importManyToManyCollectionAttribute(srcEmbeddedEntity, dstEmbeddedEntity, srcSecurityState,
                            vp, propertyRegularView, commitContext, referenceInfoList, optimisticLock);
                } else {
                    importReference(srcEmbeddedEntity, dstEmbeddedEntity, vp, propertyRegularView, commitContext, referenceInfoList, optimisticLock);
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
        EntityImportViewProperty viewProperty = referenceInfo.getViewProperty();
        MetaProperty metaProperty = entity.getMetaClass().getPropertyNN(viewProperty.getName());
        Collection dstFilteredIds = getFilteredIds(entity, metaProperty.getName());
        Collection srcFilteredIds = getFilteredIds(referenceInfo.getPrevSecurityState(), metaProperty.getName());

        if (metaProperty.getRange().getCardinality() == Range.Cardinality.MANY_TO_MANY) {
            @SuppressWarnings("unchecked")
            Collection<Entity> collectionValue = (Collection<Entity>) referenceInfo.getPropertyValue();
            @SuppressWarnings("unchecked")
            Collection<Entity> prevCollectionValue = (Collection<Entity>) referenceInfo.getPrevPropertyValue();
            if (collectionValue == null && srcFilteredIds.isEmpty()) {
                entity.setValue(metaProperty.getName(), createNewCollection(metaProperty));
                return;
            }
            Collection<Entity> newCollectionValue = createNewCollection(metaProperty);
            CollectionCompare.with()
                    .onCreate(e -> {
                        if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                            Entity result = findReferenceEntity(e, viewProperty, commitContext, loadedEntities);
                            if (result != null) {
                                newCollectionValue.add(result);
                            }
                        }
                    })
                    .onUpdate((src, dst) -> {
                        if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(dst))) {
                            Entity result = findReferenceEntity(src, viewProperty, commitContext, loadedEntities);
                            if (result != null) {
                                newCollectionValue.add(result);
                            }
                        }
                    })
                    .onDelete(e -> {
                        if (!dstFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                            if (srcFilteredIds.contains(referenceToEntitySupport.getReferenceId(e))) {
                                newCollectionValue.add(e);
                            } else if (viewProperty.getCollectionImportPolicy() == CollectionImportPolicy.KEEP_ABSENT_ITEMS) {
                                newCollectionValue.add(e);
                            }
                        }
                    })
                    .compare(collectionValue, prevCollectionValue);
            entity.setValue(metaProperty.getName(), newCollectionValue);
            //end of many-to-many processing block
        } else {
            //all other reference types (except many-to-many)
            Entity entityValue = (Entity) referenceInfo.getPropertyValue();
            if (entityValue == null) {
                if (dstFilteredIds.isEmpty()) {
                    entity.setValue(metaProperty.getName(), null);
                    //in case of NULL value we must delete COMPOSITION entities
                    if (metaProperty.getType() == MetaProperty.Type.COMPOSITION) {
                        Entity prevEntityValue = (Entity) referenceInfo.getPrevPropertyValue();
                        if (prevEntityValue != null) {
                            commitContext.addInstanceToRemove(prevEntityValue);
                        }
                    }
                }
            } else {
                if (dstFilteredIds.isEmpty()) {
                    Entity result = findReferenceEntity(entityValue, viewProperty, commitContext, loadedEntities);
                    if (result != null) {
                        entity.setValue(metaProperty.getName(), result);
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
                if (metaProperty.isReadOnly()) continue;
                if (metaProperty.getRange().isClass()) {
                    MetaClass propertyMetaClass = metaProperty.getRange().asClass();
                    regularView.addProperty(importViewProperty.getName(), new View(propertyMetaClass.getJavaClass(), false));
                } else {
                    regularView.addProperty(importViewProperty.getName());
                }
            } else {
                regularView.addProperty(importViewProperty.getName(), buildViewFromImportView(importViewPropertyView));
            }
        }
        return regularView;
    }

    protected Collection getFilteredIds(Entity entity, String propertyName) {
        if (entity instanceof BaseGenericIdEntity) {
            String storeName = metadata.getTools().getStoreName(entity.getMetaClass());
            DataStore dataStore = storeFactory.get(storeName);
            if (RdbmsStore.class.equals(AopUtils.getTargetClass(dataStore))) {
                persistenceSecurity.restoreSecurityState(entity);
                return Optional.ofNullable(BaseEntityInternalAccess.getFilteredData(entity))
                        .map(v -> v.get(propertyName))
                        .orElse(Collections.emptyList());
            }
        }
        return Collections.emptyList();
    }

    protected Collection getFilteredIds(@Nullable SecurityState securityState, String propertyName) {
        if (securityState != null) {
            return Optional.ofNullable(BaseEntityInternalAccess.getFilteredData(securityState))
                    .map(v -> v.get(propertyName))
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    protected Collection<Entity> createNewCollection(MetaProperty metaProperty) {
        Collection<Entity> entities;
        Class<?> propertyType = metaProperty.getJavaType();
        if (List.class.isAssignableFrom(propertyType)) {
            entities = new ArrayList<>();
        } else if (Set.class.isAssignableFrom(propertyType)) {
            entities = new LinkedHashSet<>();
        } else {
            throw new RuntimeException(String.format("Could not instantiate collection with class [%s].", propertyType));
        }
        return entities;
    }

    protected boolean useSecurityToken() {
        return globalConfig.getRestRequiresSecurityToken();
    }

    protected Entity findReferenceEntity(Entity entity, EntityImportViewProperty viewProperty, CommitContext commitContext,
                                         Set<Entity> loadedEntities) {
        Entity result = Stream.concat(loadedEntities.stream(), commitContext.getCommitInstances().stream())
                .filter(item -> item.equals(entity))
                .findFirst().orElse(null);
        if (result == null) {
            LoadContext<? extends Entity> ctx = LoadContext.create(entity.getClass())
                    .setSoftDeletion(false)
                    .setView(viewRepository.getView(entity.getMetaClass().getJavaClass(), View.MINIMAL))
                    .setId(entity.getId());
            result = dataManager.load(ctx);
            if (result == null) {
                if (viewProperty.getReferenceImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                    throw new EntityImportException(String.format("Referenced entity for property '%s' with id = %s is missing",
                            viewProperty.getName(), entity.getId()));
                }
            } else {
                loadedEntities.add(result);
            }
        }
        return result;
    }

    protected static class ReferenceInfo {
        protected Entity entity;
        protected SecurityState prevSecurityState;
        protected EntityImportViewProperty viewProperty;
        protected Object propertyValue;
        protected Object prevPropertyValue;

        public ReferenceInfo(Entity entity, SecurityState prevSecurityState, EntityImportViewProperty viewProperty, Object propertyValue, Object prevPropertyValue) {
            this.entity = entity;
            this.prevSecurityState = prevSecurityState;
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

        public SecurityState getPrevSecurityState() {
            return prevSecurityState;
        }

        public Object getPropertyValue() {
            return propertyValue;
        }
    }
}
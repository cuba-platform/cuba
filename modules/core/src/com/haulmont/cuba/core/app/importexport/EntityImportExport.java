/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.importexport;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
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

/**
 * @author gorbunkov
 * @version $Id$
 */
@Component(EntityImportExportAPI.NAME)
public class EntityImportExport implements EntityImportExportAPI {

    @Inject
    protected EntitySerializationAPI entitySerialization;

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Override
    public byte[] exportEntities(Collection<? extends Entity> entities, View view) {
        return exportEntities(reloadEntities(entities, view));
    }

    @Override
    public byte[] exportEntities(Collection<? extends Entity> entities) {
        String json = entitySerialization.toJson(entities);
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
                    entities = entitySerialization.fromJson(json);
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

    protected Collection<Entity> importEntities(Collection<? extends Entity> entities, EntityImportView view) {
        Collection<Entity> result = new ArrayList<>();
        Map<Object, Entity> entitiesToCreate = new HashMap<>();
        Map<Object, Entity> loadedReferences = new HashMap<>();

        try (Transaction tx = persistence.getTransaction()) {
            for (Entity entity : entities) {
                importEntity(entity, view, entitiesToCreate, loadedReferences);
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
            }
            tx.commit();
        }
        return result;
    }

    protected Entity importEntity(Entity srcEntity,
                                  EntityImportView view,
                                  Map<Object, Entity> entitiesToCreate,
                                  Map<Object, Entity> loadedReferences) {
        EntityManager em = persistence.getEntityManager();
        //set softDeletion to false because we can import deleted entity, so we'll restore them and update
        em.setSoftDeletion(false);
        Entity dstEntity = em.reload(srcEntity);
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
                if (metaProperty.getRange().getCardinality().isMany()) {
                    importCollectionAttribute(srcEntity, dstEntity, viewProperty, entitiesToCreate, loadedReferences);
                } else {
                    importReference(srcEntity, dstEntity, viewProperty, entitiesToCreate, loadedReferences);
                }
            }
        }

        return dstEntity;
    }

    protected void importReference(Entity srcEntity,
                                   Entity dstEntity,
                                   EntityImportViewProperty viewProperty,
                                   Map<Object, Entity> entitiesToCreate,
                                   Map<Object, Entity> loadedReferences) {
        Entity srcPropertyValue = srcEntity.<Entity>getValue(viewProperty.getName());
        if (viewProperty.getView() == null) {
            //if no view defined for the property then try to find the reference
            if (srcPropertyValue == null) {
                dstEntity.setValue(viewProperty.getName(), null);
            } else if (loadedReferences.get(srcPropertyValue.getId()) != null) {
                dstEntity.setValue(viewProperty.getName(), loadedReferences.get(srcPropertyValue.getId()));
            } else if (entitiesToCreate.get(srcPropertyValue.getId()) != null) {
                dstEntity.setValue(viewProperty.getName(), entitiesToCreate.get(srcPropertyValue.getId()));
            } else {
                EntityManager em = persistence.getEntityManager();
                Entity loadedReference = em.find(srcPropertyValue.getClass(), srcPropertyValue.getId());
                if (loadedReference == null) {
                    if (viewProperty.getReferenceImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                        throw new RuntimeException("Referenced entity " + viewProperty.getName() + " with id = " + srcPropertyValue.getId() + " is missing");
                    }
                } else {
                    dstEntity.setValue(viewProperty.getName(), loadedReference);
                }
                loadedReferences.put(srcPropertyValue.getId(), loadedReference);
            }
        } else {
            //if import view was defined then create new referenced entity
            importEntity(srcPropertyValue, viewProperty.getView(), entitiesToCreate, loadedReferences);
        }
    }

    protected void importCollectionAttribute(Entity srcEntity,
                                             Entity dstEntity,
                                             EntityImportViewProperty viewProperty,
                                             Map<Object, Entity> entitiesToCreate,
                                             Map<Object, Entity> loadedReferences) {
        Collection<Entity> srcPropertyValue = srcEntity.getValue(viewProperty.getName());
        if (srcPropertyValue == null) {
            dstEntity.setValue(viewProperty.getName(), null);
            return;
        }
        Collection<Entity> collection;
        try {
            collection = srcPropertyValue.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error on import entities", e);
        }

        MetaProperty metaProperty = srcEntity.getMetaClass().getPropertyNN(viewProperty.getName());
        MetaProperty inverseMetaProperty = metaProperty.getInverse();

        for (Entity srcChildEntity : srcPropertyValue) {
            if (viewProperty.getView() == null) {
                //if no view defined for the property then try to find the reference
                if (loadedReferences.get(srcChildEntity.getId()) != null) {
                    collection.add(loadedReferences.get(srcChildEntity.getId()));
                } else if (entitiesToCreate.get(srcChildEntity.getId()) != null) {
                    collection.add(entitiesToCreate.get(srcChildEntity.getId()));
                } else {
                    EntityManager em = persistence.getEntityManager();
                    Entity loadedReference = em.reload(srcChildEntity);
                    if (loadedReference == null) {
                        if (viewProperty.getReferenceImportBehaviour() == ReferenceImportBehaviour.ERROR_ON_MISSING) {
                            throw new RuntimeException("Referenced entity " + viewProperty.getName() + " with id = " + srcChildEntity.getId() + " is missing");
                        }
                    } else {
                        collection.add(loadedReference);
                    }
                    loadedReferences.put(srcChildEntity.getId(), loadedReference);
                }
            } else {
                //create new referenced entity
                Entity dstChildEntity = importEntity(srcChildEntity, viewProperty.getView(), entitiesToCreate, loadedReferences);
                if (inverseMetaProperty != null) {
                    dstChildEntity.setValue(inverseMetaProperty.getName(), dstEntity);
                }
                collection.add(dstChildEntity);
            }
        }

        dstEntity.setValue(viewProperty.getName(), collection);
    }
}

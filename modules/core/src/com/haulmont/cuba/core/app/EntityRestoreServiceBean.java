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

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service(EntityRestoreService.NAME)
public class EntityRestoreServiceBean implements EntityRestoreService {

    private static final Logger log = LoggerFactory.getLogger(EntityRestoreServiceBean.class);

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Override
    public void restoreEntities(Collection<Entity> entities) {
        for (Entity entity : entities) {
            if (!(entity instanceof SoftDelete))
                continue;

            String storeName = metadata.getTools().getStoreName(metadata.getClassNN(entity.getClass()));
            if (storeName == null) {
                log.warn("Unable to restore entity {}: cannot determine data store", entity);
                continue;
            }

            Transaction tx = persistence.createTransaction(storeName);
            try {
                persistence.getEntityManager(storeName).setSoftDeletion(false);
                restoreEntity(entity, storeName);
                tx.commit();
            } finally {
                tx.end();
            }
        }
    }

    protected void restoreEntity(Entity entity, String storeName) {
        EntityManager em = persistence.getEntityManager(storeName);
        Entity reloadedEntity = em.find(entity.getClass(), entity.getId());
        if (reloadedEntity != null && ((SoftDelete) reloadedEntity).isDeleted()) {
            log.info("Restoring deleted entity " + entity);
            Date deleteTs = ((SoftDelete) reloadedEntity).getDeleteTs();
            ((SoftDelete) reloadedEntity).setDeleteTs(null);
            em.merge(reloadedEntity);
            restoreDetails(reloadedEntity, deleteTs, storeName);
        }
    }

    protected void restoreDetails(Entity entity, Date deleteTs, String storeName) {
        EntityManager em = persistence.getEntityManager(storeName);
        MetaClass metaClass = metadata.getClassNN(entity.getClass());

        List<MetaProperty> properties = new ArrayList<>();
        fillProperties(metaClass, properties, OnDelete.class.getName());
        for (MetaProperty property : properties) {
            OnDelete annotation = property.getAnnotatedElement().getAnnotation(OnDelete.class);
            DeletePolicy deletePolicy = annotation.value();
            if (deletePolicy == DeletePolicy.CASCADE) {
                MetaClass detailMetaClass = property.getRange().asClass();
                if (!storeName.equals(metadata.getTools().getStoreName(detailMetaClass))) {
                    log.debug("Cannot restore " + property.getRange().asClass() + " because it is from different data store");
                    continue;
                }
                if (!SoftDelete.class.isAssignableFrom(detailMetaClass.getJavaClass())) {
                    log.debug("Cannot restore " + property.getRange().asClass() + " because it is hard deleted");
                    continue;
                }
                if (metadataTools.isOwningSide(property)) {
                    Object value = entity.getValue(property.getName());
                    if (value instanceof Entity) {
                        restoreEntity((Entity) value, storeName);
                    } else if (value instanceof Collection) {
                        for (Object detailEntity : (Collection) value) {
                            restoreEntity((Entity) detailEntity, storeName);
                        }
                    }
                } else {
                    MetaProperty inverseProp = property.getInverse();
                    if (inverseProp == null) {
                        log.debug("Cannot restore " + property.getRange().asClass() + " because it has no inverse property for " + metaClass);
                        continue;
                    }
                    String jpql = "select e from " + detailMetaClass + " e where e." + inverseProp.getName() + ".id = ?1 " +
                            "and e.deleteTs >= ?2 and e.deleteTs <= ?3";
                    Query query = em.createQuery(jpql);
                    query.setParameter(1, entity.getId());
                    query.setParameter(2, DateUtils.addMilliseconds(deleteTs, -100));
                    query.setParameter(3, DateUtils.addMilliseconds(deleteTs, 1000));
                    //noinspection unchecked
                    List<Entity> list = query.getResultList();
                    for (Entity detailEntity : list) {
                        if (entity instanceof SoftDelete) {
                            restoreEntity(detailEntity, storeName);
                        }
                    }
                }
            }
        }

        properties = new ArrayList<>();
        fillProperties(metaClass, properties, OnDeleteInverse.class.getName());
        for (MetaProperty property : properties) {
            OnDeleteInverse annotation = property.getAnnotatedElement().getAnnotation(OnDeleteInverse.class);
            DeletePolicy deletePolicy = annotation.value();
            if (deletePolicy == DeletePolicy.CASCADE) {
                MetaClass detailMetaClass = property.getDomain();
                if (!storeName.equals(metadata.getTools().getStoreName(detailMetaClass))) {
                    log.debug("Cannot restore " + property.getRange().asClass() + " because it is from different data store");
                    continue;
                }
                if (!SoftDelete.class.isAssignableFrom(detailMetaClass.getJavaClass())) {
                    log.debug("Cannot restore " + property.getRange().asClass() + " because it is hard deleted");
                    continue;
                }
                List<MetaClass> metClassesToRestore = new ArrayList<>();
                metClassesToRestore.add(detailMetaClass);
                metClassesToRestore.addAll(detailMetaClass.getDescendants());
                for (MetaClass metaClassToRestore : metClassesToRestore) {
                    if (!metadata.getTools().isPersistent(metaClassToRestore))
                        continue;
                    String jpql;
                    if (property.getRange().getCardinality().isMany()) {
                        jpql = "select e from " + metaClassToRestore.getName() + " e join e." + property.getName() + " p"
                                + " where p.id = ?1 and e.deleteTs >= ?2 and e.deleteTs <= ?3";
                    } else {
                        jpql = "select e from " + metaClassToRestore.getName() + " e where e." + property.getName()
                                + ".id = ?1 and e.deleteTs >= ?2 and e.deleteTs <= ?3";
                    }
                    Query query = em.createQuery(jpql);
                    query.setParameter(1, entity.getId());
                    query.setParameter(2, DateUtils.addMilliseconds(deleteTs, -100));
                    query.setParameter(3, DateUtils.addMilliseconds(deleteTs, 1000));
                    //noinspection unchecked
                    List<Entity> list = query.getResultList();
                    for (Entity detailEntity : list) {
                        if (entity instanceof SoftDelete) {
                            restoreEntity(detailEntity, storeName);
                        }
                    }
                }
            }
        }
    }

    protected void fillProperties(MetaClass metaClass, List<MetaProperty> properties, String annotationName) {
        properties.clear();
        MetaProperty[] metaProperties = (MetaProperty[]) metaClass.getAnnotations().get(annotationName);
        if (metaProperties != null)
            properties.addAll(Arrays.asList(metaProperties));
        for (MetaClass aClass : metaClass.getAncestors()) {
            metaProperties = (MetaProperty[]) aClass.getAnnotations().get(annotationName);
            if (metaProperties != null)
                properties.addAll(Arrays.asList(metaProperties));
        }
    }
}
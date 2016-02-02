/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.PersistenceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(DeletePolicyProcessor.NAME)
@Scope("prototype")
public class DeletePolicyProcessor {

    public static final String NAME = "cuba_DeletePolicyProcessor";

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected BaseEntity entity;
    protected MetaClass metaClass;

    protected Persistence persistence;
    protected EntityManager entityManager;

    @Inject
    protected Metadata metadata;

    @Inject
    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
        this.entityManager = persistence.getEntityManager();
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
        this.metaClass = metadata.getSession().getClass(entity.getClass());
    }

    public void process() {
        List<MetaProperty> properties = new ArrayList<MetaProperty>();

        fillProperties(properties, OnDeleteInverse.class.getName());
        if (!properties.isEmpty())
            processOnDeleteInverse(properties);

        fillProperties(properties, OnDelete.class.getName());
        if (!properties.isEmpty())
            processOnDelete(properties);
    }

    protected void fillProperties(List<MetaProperty> properties, String annotationName) {
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

    protected void processOnDeleteInverse(List<MetaProperty> properties) {
        for (MetaProperty property : properties) {
            MetaClass metaClass = property.getDomain();

            List<MetaClass> persistentEntities = new ArrayList<>();
            if (isPersistent(metaClass))
                persistentEntities.add(metaClass);
            for (MetaClass descendant : metaClass.getDescendants()) {
                if (isPersistent(descendant))
                    persistentEntities.add(descendant);
            }

            for (MetaClass persistentEntity : persistentEntities) {
                OnDeleteInverse annotation = property.getAnnotatedElement().getAnnotation(OnDeleteInverse.class);
                DeletePolicy deletePolicy = annotation.value();
                switch (deletePolicy) {
                    case DENY:
                        if (referenceExists(persistentEntity.getName(), property))
                            throw new DeletePolicyException(this.metaClass.getName(), persistentEntity.getName());
                        break;
                    case CASCADE:
                        cascade(persistentEntity.getName(), property);
                        break;
                    case UNLINK:
                        unlink(persistentEntity.getName(), property);
                        break;
                }
            }

        }
    }

    protected void processOnDelete(List<MetaProperty> properties) {
        for (MetaProperty property : properties) {
            MetaClass metaClass = property.getRange().asClass();
            OnDelete annotation = property.getAnnotatedElement().getAnnotation(OnDelete.class);
            DeletePolicy deletePolicy = annotation.value();
            switch (deletePolicy) {
                case DENY:
                    if (property.getRange().getCardinality().isMany()) {
                        if (!isCollectionEmpty(property))
                            throw new DeletePolicyException(this.metaClass.getName(), metaClass.getName());
                    } else {
                        Object value = getReference(entity, property);
                        if (value != null)
                            throw new DeletePolicyException(this.metaClass.getName(), metaClass.getName());
                    }
                    break;
                case CASCADE:
                    if (property.getRange().getCardinality().isMany()) {
                        Collection<Entity> value = getCollection(property);
                        if (value != null && !value.isEmpty()) {
                            for (Entity e : value) {
                                entityManager.remove(e);
                            }
                        }
                    } else {
                        BaseEntity value = getReference(entity, property);
                        if (value != null && checkIfEntityBelongsToMaster(property, value)) {
                            if (!(value instanceof SoftDelete)) {
                                if (PersistenceHelper.isLoaded(entity, property.getName())) {
                                    entity.setValue(property.getName(), null);
                                    entityManager.remove(value);
                                } else {
                                    hardDeleteNotLoadedReference(entity, property, value);
                                }
                            } else {
                                entityManager.remove(value);
                            }
                        }
                    }
                    break;
                case UNLINK:
                    if (property.getRange().getCardinality().isMany()) {
                        throw new UnsupportedOperationException("Unable to unlink nested collection items");
                    } else {
                        setReferenceNull(entity, property);
                    }
                    break;
            }
        }
    }

    protected void hardDeleteNotLoadedReference(BaseEntity entity, MetaProperty property, BaseEntity reference) {
        List<Runnable> list = persistence.getEntityManagerContext().getAttribute(PersistenceImpl.RUN_BEFORE_COMMIT_ATTR);
        if (list == null) {
            list = new ArrayList<>();
            persistence.getEntityManagerContext().setAttribute(PersistenceImpl.RUN_BEFORE_COMMIT_ATTR, list);
        }
        list.add(() -> {
            MetadataTools metadataTools = metadata.getTools();
            QueryRunner queryRunner = new QueryRunner();
            try {
                String column = metadataTools.getDatabaseColumn(property);
                if (column != null) { // is null for mapped-by property
                    String updateMasterSql = "update " + metadataTools.getDatabaseTable(metaClass)
                            + " set " + column + " = null where "
                            + metadataTools.getPrimaryKeyName(metaClass) + " = ?";
                    log.debug("Hard delete unfetched reference: " + updateMasterSql + ", bind: [" + entity.getId() + "]");
                    queryRunner.update(entityManager.getConnection(), updateMasterSql, persistence.getDbTypeConverter().getSqlObject(entity.getId()));
                }

                MetaClass refMetaClass = property.getRange().asClass();
                String deleteRefSql = "delete from " + metadataTools.getDatabaseTable(refMetaClass) + " where "
                        + metadataTools.getPrimaryKeyName(refMetaClass) + " = ?";
                log.debug("Hard delete unfetched reference: " + deleteRefSql + ", bind: [" + reference.getId() + "]");
                queryRunner.update(entityManager.getConnection(), deleteRefSql, persistence.getDbTypeConverter().getSqlObject(reference.getId()));
            } catch (SQLException e) {
                throw new RuntimeException("Error processing deletion of " + entity, e);
            }
        });
    }

    protected void setReferenceNull(Entity entity, MetaProperty property) {
        if (PersistenceHelper.isLoaded(entity, property.getName())) {
            entity.setValue(property.getName(), null);
        } else {
            List<Runnable> list = persistence.getEntityManagerContext().getAttribute(PersistenceImpl.RUN_BEFORE_COMMIT_ATTR);
            if (list == null) {
                list = new ArrayList<>();
                persistence.getEntityManagerContext().setAttribute(PersistenceImpl.RUN_BEFORE_COMMIT_ATTR, list);
            }
            list.add(() -> {
                QueryRunner queryRunner = new QueryRunner();
                MetaClass entityMetaClass = metadata.getClassNN(entity.getClass());
                MetadataTools metadataTools = metadata.getTools();
                String sql = "update " + metadataTools.getDatabaseTable(entityMetaClass)
                        + " set " + metadataTools.getDatabaseColumn(property) + " = null where "
                        + metadataTools.getPrimaryKeyName(entityMetaClass) + " = ?";
                try {
                    log.debug("Set reference to null: " + sql + ", bind: [" + entity.getId() + "]");
                    queryRunner.update(entityManager.getConnection(), sql, persistence.getDbTypeConverter().getSqlObject(entity.getId()));
                } catch (SQLException e) {
                    throw new RuntimeException("Error processing deletion of " + entity, e);
                }
            });
        }
    }

    protected BaseEntity getReference(Entity entity, MetaProperty property) {
        if (PersistenceHelper.isLoaded(entity, property.getName()))
            return entity.getValue(property.getName());
        else {
            Query query = entityManager.createQuery(
                    "select e." + property.getName() + " from " + entity.getMetaClass().getName() + " e where e.id = ?1");
            query.setParameter(1, entity.getId());
            Object refEntity = query.getFirstResult();
            return (BaseEntity) refEntity;
        }
    }

    protected boolean checkIfEntityBelongsToMaster(MetaProperty property, BaseEntity entityToRemove) {
        MetaProperty inverseProperty = property.getInverse();
        if (inverseProperty != null) {
            Entity master = entityToRemove.getValue(inverseProperty.getName());
            return entity.equals(master);
        } else {
            return true;
        }
    }

    protected boolean isCollectionEmpty(MetaProperty property) {
        MetaProperty inverseProperty = property.getInverse();
        if (inverseProperty == null) {
            log.warn("Inverse property not found for property " + property);
            Collection<Entity> value = entity.getValue(property.getName());
            return value == null || value.isEmpty();
        }

        String invPropName = inverseProperty.getName();
        String qlStr = "select e.id from " + property.getRange().asClass().getName() + " e where e." + invPropName + ".id = ?1";

        Query query = entityManager.createQuery(qlStr);
        query.setParameter(1, entity.getId());
        query.setMaxResults(1);
        List<Entity> list = query.getResultList();

        return list.isEmpty();
    }

    protected Collection<Entity> getCollection(MetaProperty property) {
        MetaProperty inverseProperty = property.getInverse();
        if (inverseProperty == null) {
            log.warn("Inverse property not found for property " + property);
            Collection<Entity> value = entity.getValue(property.getName());
            return value == null ? Collections.EMPTY_LIST : value;
        }

        String invPropName = inverseProperty.getName();
        String qlStr = "select e from " + property.getRange().asClass().getName() + " e where e." + invPropName + ".id = ?1";

        Query query = entityManager.createQuery(qlStr);
        query.setParameter(1, entity.getId());
        List<Entity> list = query.getResultList();

        // If the property is not loaded, it means it was not modified and further check is not needed
        if (!PersistenceHelper.isLoaded(entity, property.getName())) {
            return list;
        }
        // Check whether the collection items still belong to the master entity, because they could be changed in the
        // current transaction that did not affect the database yet
        List<Entity> result = new ArrayList<>(list.size());
        for (Entity item : list) {
            Entity master = item.getValue(invPropName);
            if (entity.equals(master))
                result.add(item);
        }
        return result;
    }

    protected boolean referenceExists(String entityName, MetaProperty property) {
        String template = property.getRange().getCardinality().isMany() ?
                "select e.id from %s e join e.%s c where c.id = ?1" :
                "select e.id from %s e where e.%s.id = ?1";
        String qstr = String.format(template, entityName, property.getName());
        Query query = entityManager.createQuery(qstr);
        query.setParameter(1, entity.getId());
        query.setMaxResults(1);
        List list = query.getResultList();
        return !list.isEmpty();
    }

    protected boolean isPersistent(MetaClass metaClass) {
        return metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class);
    }

    protected void cascade(String entityName, MetaProperty property) {
        String template = property.getRange().getCardinality().isMany() ?
                "select e from %s e join e.%s c where c.id = ?1" :
                "select e from %s e where e.%s.id = ?1";
        String qstr = String.format(template, entityName, property.getName());
        Query query = entityManager.createQuery(qstr);
        query.setParameter(1, entity.getId());
        List<BaseEntity> list = query.getResultList();
        for (BaseEntity e : list) {
            entityManager.remove(e);
        }
    }

    protected void unlink(String entityName, MetaProperty property) {
        String template = property.getRange().getCardinality().isMany() ?
                "select e from %s e join e.%s c where c.id = ?1" :
                "select e from %s e where e.%s.id = ?1";
        String qstr = String.format(template, entityName, property.getName());
        Query query = entityManager.createQuery(qstr);
        query.setParameter(1, entity.getId());
        List<BaseEntity> list = query.getResultList();
        for (BaseEntity e : list) {
            if (property.getRange().getCardinality().isMany()) {
                Collection collection = e.getValue(property.getName());
                if (collection != null) {
                    for (Iterator it = collection.iterator(); it.hasNext(); ) {
                        if (entity.equals(it.next())) {
                            it.remove();
                        }
                    }
                }
            } else {
                setReferenceNull(e, property);
            }
        }
    }
}
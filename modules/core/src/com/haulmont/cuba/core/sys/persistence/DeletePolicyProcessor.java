/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

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
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.core.global.Metadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(DeletePolicyProcessor.NAME)
@Scope("prototype")
public class DeletePolicyProcessor {

    public static final String NAME = "cuba_DeletePolicyProcessor";

    protected Log log = LogFactory.getLog(getClass());

    protected BaseEntity entity;
    protected MetaClass metaClass;

    protected EntityManager entityManager;

    @Inject
    protected Metadata metadata;

    @Inject
    public void setPersistence(Persistence persistence) {
        entityManager = persistence.getEntityManager();
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
            OnDeleteInverse annotation = property.getAnnotatedElement().getAnnotation(OnDeleteInverse.class);
            DeletePolicy deletePolicy = annotation.value();
            switch (deletePolicy) {
                case DENY:
                    if (referenceExists(property))
                        throw new DeletePolicyException(metaClass.getName());
                    break;
                case CASCADE:
                    cascade(property);
                    break;
                case UNLINK:
                    unlink(property);
                    break;
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
                            throw new DeletePolicyException(metaClass.getName());
                    } else {
                        Object value = entity.getValue(property.getName());
                        if (value != null)
                            throw new DeletePolicyException(metaClass.getName());
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
                        BaseEntity value = entity.getValue(property.getName());
                        if (value != null && checkIfEntityBelongsToMaster(property, value)) {
                            entityManager.remove(value);
                            if (!(value instanceof SoftDelete)) {
                                entity.setValue(property.getName(), null);
                            }
                        }
                    }
                    break;
                case UNLINK:
                    if (property.getRange().getCardinality().isMany()) {
                        throw new UnsupportedOperationException("Unable to unlink nested collection items");
                    } else {
                        entity.setValue(property.getName(), null);
                    }
                    break;
            }
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

        // Check whether the collection items still belong to the master entity, because they could be changed in the
        // current transaction that not affected the database yet
        List<Entity> result = new ArrayList<>(list.size());
        for (Entity item : list) {
            Entity master = item.getValue(invPropName);
            if (entity.equals(master))
                result.add(item);
        }

        return result;
    }

    protected boolean referenceExists(MetaProperty property) {
        MetaClass metaClass = property.getDomain();
        List<MetaClass> classes = new ArrayList<MetaClass>();
        if (isPersistent(metaClass))
            classes.add(metaClass);
        for (MetaClass descendant : metaClass.getDescendants()) {
            if (isPersistent(descendant))
                classes.add(descendant);
        }

        for (MetaClass persistentMetaClass : classes) {
            String qstr = String.format("select e.id from %s e where e.%s.id = ?1",
                    persistentMetaClass.getName(), property.getName());
            Query query = entityManager.createQuery(qstr);
            query.setParameter(1, entity.getId());
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty())
                return true;
        }
        return false;
    }

    protected boolean isPersistent(MetaClass metaClass) {
        return metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class);
    }

    protected void cascade(MetaProperty property) {
        MetaClass metaClass = property.getDomain();
        String qstr = String.format("select e from %s e where e.%s.id = ?1",
                metaClass.getName(), property.getName());
        Query query = entityManager.createQuery(qstr);
        query.setParameter(1, entity.getId());
        List<BaseEntity> list = query.getResultList();
        for (BaseEntity e : list) {
            entityManager.remove(e);
        }
    }

    protected void unlink(MetaProperty property) {
        MetaClass metaClass = property.getDomain();
        String qstr = String.format("select e from %s e where e.%s.id = ?1",
                metaClass.getName(), property.getName());
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
                e.setValue(property.getName(), null);
            }
        }
    }
}

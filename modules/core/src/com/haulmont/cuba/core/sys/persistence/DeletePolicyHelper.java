/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.01.2009 13:05:26
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.core.global.MetadataProvider;

import java.util.*;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class DeletePolicyHelper
{
    private Log log = LogFactory.getLog(DeletePolicyHelper.class);

    private BaseEntity entity;
    private MetaClass metaClass;

    public DeletePolicyHelper(BaseEntity entity) {
        this.entity = entity;
        this.metaClass = MetadataProvider.getSession().getClass(entity.getClass());
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

    private void fillProperties(List<MetaProperty> properties, String annotationName) {
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

    private void processOnDeleteInverse(List<MetaProperty> properties) {
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

    private void processOnDelete(List<MetaProperty> properties) {
        EntityManager em = PersistenceProvider.getEntityManager();

        for (MetaProperty property : properties) {
            MetaClass metaClass = property.getRange().asClass();
            OnDelete annotation = property.getAnnotatedElement().getAnnotation(OnDelete.class);
            DeletePolicy deletePolicy = annotation.value();
            switch (deletePolicy) {
                case DENY:
                    if (property.getRange().getCardinality().isMany()) {
                        if (!isCollectionEmpty(property))
                            throw new DeletePolicyException(metaClass.getName());
                    }
                    else {
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
                                em.remove(e);
                            }
                        }
                    }
                    else {
                        BaseEntity value = entity.getValue(property.getName());
                        if (value != null) {
                            em.remove(value);
                        }
                    }
                    break;
                case UNLINK:
                    if (property.getRange().getCardinality().isMany()) {
                        throw new UnsupportedOperationException("Unable to unlink nested collection items");
                    }
                    else {
                        entity.setValue(property.getName(), null);
                    }
                    break;
            }
        }
    }

    private boolean isCollectionEmpty(MetaProperty property) {
        MetaProperty inverseProperty = property.getInverse();
        if (inverseProperty == null) {
            log.warn("Inverse property not found for property " + property);
            Collection<Entity> value = entity.getValue(property.getName());
            return value == null || value.isEmpty();
        }

        String invPropName = inverseProperty.getName();
        String qlStr = "select e.id from " + property.getRange().asClass().getName() + " e where e." + invPropName + ".id = ?1";

        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery(qlStr);
        query.setParameter(1, entity.getId());
        query.setMaxResults(1);
        List<Entity> list = query.getResultList();

        return list.isEmpty();
    }

    private Collection<Entity> getCollection(MetaProperty property) {
        MetaProperty inverseProperty = property.getInverse();
        if (inverseProperty == null) {
            log.warn("Inverse property not found for property " + property);
            Collection<Entity> value = entity.getValue(property.getName());
            return value == null ? Collections.EMPTY_LIST : value;
        }

        String invPropName = inverseProperty.getName();
        String qlStr = "select e from " + property.getRange().asClass().getName() + " e where e." + invPropName + ".id = ?1";

        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery(qlStr);
        query.setParameter(1, entity.getId());
        List<Entity> list = query.getResultList();

        return list;
    }

    private boolean referenceExists(MetaProperty property) {
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
            EntityManager em = PersistenceProvider.getEntityManager();
            Query query = em.createQuery(qstr);
            query.setParameter(1, entity.getId());
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty())
                return true;
        }
        return false;
    }

    private boolean isPersistent(MetaClass metaClass) {
        return metaClass.getJavaClass().isAnnotationPresent(javax.persistence.Entity.class);
    }

    private void cascade(MetaProperty property) {
        MetaClass metaClass = property.getDomain();
        String qstr = String.format("select e from %s e where e.%s.id = ?1",
                metaClass.getName(), property.getName());
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery(qstr);
        query.setParameter(1, entity.getId());
        List<BaseEntity> list = query.getResultList();
        for (BaseEntity e : list) {
            em.remove(e);
        }
    }

    private void unlink(MetaProperty property) {
        MetaClass metaClass = property.getDomain();
        String qstr = String.format("select e from %s e where e.%s.id = ?1",
                metaClass.getName(), property.getName());
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery(qstr);
        query.setParameter(1, entity.getId());
        List<BaseEntity> list = query.getResultList();
        for (BaseEntity e : list) {
            e.setValue(property.getName(), null);
        }
    }
}

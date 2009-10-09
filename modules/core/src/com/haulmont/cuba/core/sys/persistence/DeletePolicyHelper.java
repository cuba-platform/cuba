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
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.global.DeletePolicyException;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

public class DeletePolicyHelper
{
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
        for (MetaProperty property : properties) {
            MetaClass metaClass = property.getRange().asClass();
            OnDelete annotation = property.getAnnotatedElement().getAnnotation(OnDelete.class);
            DeletePolicy deletePolicy = annotation.value();
            switch (deletePolicy) {
                case DENY:
                    if (property.getRange().getCardinality().isMany()) {
                        Collection value = ((Instance) entity).getValue(property.getName());
                        if (!value.isEmpty())
                            throw new DeletePolicyException(metaClass.getName());
                    }
                    else {
                        Object value = ((Instance) entity).getValue(property.getName());
                        if (value != null)
                            throw new DeletePolicyException(metaClass.getName());
                    }
                    break;
                case CASCADE:
                    if (property.getRange().getCardinality().isMany()) {
                        Collection<BaseEntity> value = ((Instance) entity).getValue(property.getName());
                        if (!value.isEmpty()) {
                            EntityManager em = PersistenceProvider.getEntityManager();
                            for (BaseEntity e : value) {
                                em.remove(e);
                            }
                        }
                    }
                    else {
                        BaseEntity value = ((Instance) entity).getValue(property.getName());
                        if (value != null) {
                            EntityManager em = PersistenceProvider.getEntityManager();
                            em.remove(value);
                        }
                    }
                    break;
                case UNLINK:
                    if (property.getRange().getCardinality().isMany()) {
                        throw new UnsupportedOperationException("Unable to unlink nested collection items");
                    }
                    else {
                        ((Instance) entity).setValue(property.getName(), null);
                    }
                    break;
            }
        }
    }

    private boolean referenceExists(MetaProperty property) {
        MetaClass metaClass = property.getDomain();
        String qstr = String.format("select count(e) from %s e where e.%s.id = ?1",
                metaClass.getName(), property.getName());
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery(qstr);
        query.setParameter(1, entity.getId());
        Number count = (Number) query.getSingleResult();
        return count.longValue() > 0;
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
            ((Instance) e).setValue(property.getName(), null);
        }
    }
}

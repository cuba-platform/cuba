/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 15:34:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.BasicInvocationContext;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;

import javax.ejb.Stateless;
import java.util.List;

@Stateless(name = BasicWorker.JNDI_NAME)
public class BasicWorkerBean implements BasicWorker
{
    public <T extends BaseEntity> T create(T entity) {
        EntityManager em = PersistenceProvider.getEntityManager();
        em.persist(entity);
        return entity;
    }

    public <T extends BaseEntity> T update(T entity) {
        EntityManager em = PersistenceProvider.getEntityManager();
        T result = em.merge(entity);
        return result;
    }

    public void delete(BasicInvocationContext ctx) {
        EntityManager em = PersistenceProvider.getEntityManager();
        BaseEntity entity = em.find(ctx.getEntityClass(), ctx.getId());
        em.remove(entity);
    }

    public <T extends BaseEntity> T get(BasicInvocationContext ctx) {
        EntityManager em = PersistenceProvider.getEntityManager();
        BaseEntity result = em.find(ctx.getEntityClass(), ctx.getId());
        return (T) result;
    }

    public <T extends BaseEntity> T load(BasicInvocationContext ctx) {
        EntityManager em = PersistenceProvider.getEntityManager();
        String queryString =
                "select e from " +
                PersistenceProvider.getEntityName(ctx.getEntityClass()) + " e where e.id = ?1";
        Query query = em.createQuery(queryString);
        query.setParameter(1, ctx.getId());
        Object result = query.getSingleResult();
        return (T) result;
    }

    public <T extends BaseEntity> List<T> loadList(BasicInvocationContext ctx) {
        EntityManager em = PersistenceProvider.getEntityManager();
        Query query = em.createQuery(ctx.getQueryString());
        List resultList = query.getResultList();
        return resultList;
    }
}

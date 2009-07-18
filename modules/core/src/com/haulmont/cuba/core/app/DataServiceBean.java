/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:26:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.ServiceInterceptor;
import com.haulmont.cuba.core.sys.ViewHelper;
import com.haulmont.cuba.security.entity.PermissionType;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.*;

@Stateless(name = DataService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
public class DataServiceBean implements DataService, DataServiceRemote
{
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public DbDialect getDbDialect() {
        return PersistenceProvider.getDbDialect();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Map<Entity, Entity> commit(CommitContext<Entity> context) {
        final Map<Entity, Entity> res = new HashMap<Entity, Entity>();

        EntityManager em = PersistenceProvider.getEntityManager();
        checkPermissions(context);

        // persist new
        for (Entity entity : context.getCommitInstances()) {
            if (PersistenceHelper.isNew(entity)) {
                em.persist(entity);
                res.put(entity, entity);
            }
        }
        // merge detached
        for (Entity entity : context.getCommitInstances()) {
            if (!PersistenceHelper.isNew(entity)) {
                Entity e = em.merge(entity);
                res.put(entity, e);
            }
        }
        // remove
        for (Entity entity : context.getRemoveInstances()) {
            Entity e = em.merge(entity);
            em.remove(e);
            res.put(entity, e);
        }

        for (Map.Entry<Entity, Entity> entry : res.entrySet()) {
            View view = context.getViews().get(entry.getKey());
            if (view != null) {
                ViewHelper.fetchInstance((Instance) entry.getValue(), view);
            }
        }

        return res;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <A extends Entity> A load(LoadContext context) {
        final MetaClass metaClass = MetadataProvider.getSession().getClass(context.getMetaClass());
        checkPermission(metaClass, "view");

        final EntityManager em = PersistenceProvider.getEntityManager();

        com.haulmont.cuba.core.Query query;
        if (context.getId() != null) {
            final String entityName = PersistenceProvider.getEntityName(metaClass.getJavaClass());
            String queryString = String.format("select e from %s e where e.id = ?1", entityName);

            query = em.createQuery(queryString);
            query.setParameter(1, context.getId());

            if (context.getView() != null) {
                query.setView(context.getView());
            }
        } else {
            query = createQuery(em, context);
        }

        Object result = query.getSingleResult();
        return (A) result;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <A extends Entity> List<A> loadList(CollectionLoadContext context) {
        final MetaClass metaClass = MetadataProvider.getSession().getClass(context.getMetaClass());
        checkPermission(metaClass, "read");

        final EntityManager em = PersistenceProvider.getEntityManager();
        com.haulmont.cuba.core.Query query = createQuery(em, context);
        List resultList = query.getResultList();

        return resultList;
    }

    protected <A extends Entity> com.haulmont.cuba.core.Query createQuery(EntityManager em, AbstractLoadContext context) {
        final MetaClass metaClass = MetadataProvider.getSession().getClass(context.getMetaClass());

        com.haulmont.cuba.core.Query query = em.createQuery(context.getQuery().getQueryString());
        SecurityProvider.applyConstraints(query, metaClass.getName());

        if (context.getQuery().getFirstResult() != 0)
            query.setFirstResult(context.getQuery().getFirstResult());
        if (context.getQuery().getMaxResults() != 0)
            query.setMaxResults(context.getQuery().getMaxResults());

        final String queryString = context.getQuery().getQueryString();
        for (Map.Entry<String, Object> entry : context.getQuery().getParameters().entrySet()) {
            final String name = entry.getKey();
            if (queryString.contains(":" + name)) {
                final Object value = entry.getValue();
                if (value instanceof Entity) {
                    query.setParameter(entry.getKey(), ((Entity) value).getId());
                } else {
                    query.setParameter(entry.getKey(), value);
                }
            }
        }

        if (context.getView() != null) {
            query.setView(context.getView());
        }

        return query;
    }

    protected void checkPermission(MetaClass metaClass, String operation) {
        String target = metaClass.getName() + ":" + operation;
        if (!SecurityProvider.currentUserSession().isPermitted(PermissionType.ENTITY_OP, target))
            throw new AccessDeniedException(PermissionType.ENTITY_OP, target);
    }

    protected void checkPermissions(CommitContext<Entity> context) {
        Set<MetaClass> checkedCreateRights = new HashSet<MetaClass>();
        Set<MetaClass> checkedUpdateRights = new HashSet<MetaClass>();
        Set<MetaClass> checkedDeleteRights = new HashSet<MetaClass>();

        for (Entity entity : context.getCommitInstances()) {
            MetaClass metaClass = entity instanceof Instance ? ((Instance) entity).getMetaClass() : null;
            if (metaClass == null) continue;

            if (PersistenceHelper.isNew(entity)) {
                checkPermission(checkedUpdateRights, metaClass, "update");
            } else {
                checkPermission(checkedCreateRights, metaClass, "create");
            }
        }

        for (Entity entity : context.getRemoveInstances()) {
            MetaClass metaClass = entity instanceof Instance ? ((Instance) entity).getMetaClass() : null;
            if (metaClass == null) continue;
            checkPermission(checkedDeleteRights, metaClass, "delete");

        }
    }

    protected void checkPermission(Set<MetaClass> cache, MetaClass metaClass, String operation) {
        if (cache.contains(metaClass)) return;
        checkPermission(metaClass, operation);
        cache.add(metaClass);
    }
}

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

import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.ViewHelper;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(DataService.NAME)
public class DataServiceBean implements DataService
{
    private volatile static Boolean storeCacheEnabled;

    private Log log = LogFactory.getLog(DataServiceBean.class);

    public DbDialect getDbDialect() {
        return PersistenceProvider.getDbDialect();
    }

    public Map<Entity, Entity> commit(CommitContext<Entity> context) {
        if (log.isDebugEnabled())
            log.debug("commit: commitInstances=" + context.getCommitInstances()
                    + ", removeInstances=" + context.getRemoveInstances());

        final Map<Entity, Entity> res = new HashMap<Entity, Entity>();

        Transaction tx = Locator.getTransaction();
        try {
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
                if (PersistenceHelper.isDetached(entity)) {
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

            tx.commit();
        } finally {
            tx.end();
        }

        return res;
    }

    public <A extends Entity> A load(LoadContext context) {
        if (log.isDebugEnabled())
            log.debug("load: metaClass=" + context.getMetaClass() + ", id=" + context.getId() + ", view=" + context.getView());

        final MetaClass metaClass = MetadataProvider.getSession().getClass(context.getMetaClass());

        if (!SecurityProvider.currentUserSession().isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning null");
            return null;
        }

        Object result;

        Transaction tx = Locator.getTransaction();
        try {
            final EntityManager em = PersistenceProvider.getEntityManager();

            // Set view only if StoreCache is disabled
            if (!isStoreCacheEnabled() && context.getView() != null) {
                em.setView(context.getView());
            }

            if (context.getId() != null) {
                result = em.find(metaClass.getJavaClass(), context.getId());

                if (em.isSoftDeletion()
                        && result instanceof SoftDelete
                        && ((SoftDelete) result).isDeleted()) {
                    result = null;
                }
            } else {
                com.haulmont.cuba.core.Query query = createQuery(em, context);
                try {
                    result = query.getSingleResult();
                } catch (javax.persistence.NoResultException e) {
                    result = null;
                }
            }

            if (result != null && context.getView() != null) {
                em.setView(null);
                ViewHelper.fetchInstance((Instance) result, context.getView());
            }

            tx.commit();
        } finally {
            tx.end();
        }

        return (A) result;
    }

    public <A extends Entity> List<A> loadList(LoadContext context) {
        if (log.isDebugEnabled())
            log.debug("loadList: metaClass=" + context.getMetaClass() + ", view=" + context.getView()
                    + ", query=" + printQuery(context.getQuery()));

        final MetaClass metaClass = MetadataProvider.getSession().getClass(context.getMetaClass());

        if (!SecurityProvider.currentUserSession().isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning empty list");
            return Collections.emptyList();
        }

        List resultList;

        Transaction tx = Locator.getTransaction();
        try {
            final EntityManager em = PersistenceProvider.getEntityManager();
            em.setSoftDeletion(context.isSoftDeletion());
            com.haulmont.cuba.core.Query query = createQuery(em, context);
            resultList = query.getResultList();

            // Fetch if StoreCache is enabled or there are lazy properties in the view
            if (context.getView() != null && (isStoreCacheEnabled() || ViewHelper.hasLazyProperties(context.getView()))) {
                em.setView(null);
                for (Object entity : resultList) {
                    ViewHelper.fetchInstance((Instance) entity, context.getView());
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }

        return resultList;
    }

    private String printQuery(LoadContext.Query query) {
        if (query == null || query.getQueryString() == null)
            return null;

        String str = StringHelper.removeExtraSpaces(query.getQueryString());

        if (ConfigProvider.getConfig(LogConfig.class).getCutLoadListQueries()) {
            str = StringUtils.abbreviate(str.replaceAll("[\\n\\r]", " "), 50);
        }
        
        return str;
    }

    protected <A extends Entity> com.haulmont.cuba.core.Query createQuery(EntityManager em, LoadContext context) {
        if (context.getQuery() == null || StringUtils.isBlank(context.getQuery().getQueryString()))
            throw new IllegalArgumentException("QueryString is empty");

        final MetaClass metaClass = MetadataProvider.getSession().getClass(context.getMetaClass());

        com.haulmont.cuba.core.Query query = em.createQuery(context.getQuery().getQueryString());
        SecurityProvider.applyConstraints(query, metaClass.getName());

        if (context.getQuery().getFirstResult() != 0)
            query.setFirstResult(context.getQuery().getFirstResult());
        if (context.getQuery().getMaxResults() != 0)
            query.setMaxResults(context.getQuery().getMaxResults());

        final String queryString = context.getQuery().getQueryString();

        QueryParser parser = QueryTransformerFactory.createParser(queryString);
        Set<String> paramNames = parser.getParamNames();

        for (Map.Entry<String, Object> entry : context.getQuery().getParameters().entrySet()) {
            final String name = entry.getKey();
            if (paramNames.contains(name)) {
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

    protected void checkPermission(MetaClass metaClass, EntityOp operation) {
        String target = UserSession.getEntityOpPermissionTarget(metaClass, operation);
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
                checkPermission(checkedCreateRights, metaClass, EntityOp.CREATE);
            } else {
                checkPermission(checkedUpdateRights, metaClass, EntityOp.UPDATE);
            }
        }

        for (Entity entity : context.getRemoveInstances()) {
            MetaClass metaClass = entity instanceof Instance ? ((Instance) entity).getMetaClass() : null;
            if (metaClass == null) continue;
            checkPermission(checkedDeleteRights, metaClass, EntityOp.DELETE);

        }
    }

    protected void checkPermission(Set<MetaClass> cache, MetaClass metaClass, EntityOp operation) {
        if (cache.contains(metaClass))
            return;
        checkPermission(metaClass, operation);
        cache.add(metaClass);
    }

    private static boolean isStoreCacheEnabled() {
        if (storeCacheEnabled == null) {
            DataCacheAPI bean = Locator.lookup(DataCacheAPI.NAME);
            storeCacheEnabled = bean.isStoreCacheEnabled();
        }
        return storeCacheEnabled;
    }
}

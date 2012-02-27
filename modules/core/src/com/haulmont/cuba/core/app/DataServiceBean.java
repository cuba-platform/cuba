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
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.ViewHelper;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

@Service(DataService.NAME)
public class DataServiceBean implements DataService {

    @Inject
    private Metadata metadata;

    @Inject
    private Configuration configuration;

    @Inject
    private Persistence persistence;

    @Inject
    private PersistenceSecurity security;

    @Inject
    private DataCacheAPI dataCacheAPI;

    private Log log = LogFactory.getLog(DataServiceBean.class);

    public DbDialect getDbDialect() {
        return persistence.getDbDialect();
    }

    public Set<Entity> commit(CommitContext<Entity> context) {
        if (log.isDebugEnabled())
            log.debug("commit: commitInstances=" + context.getCommitInstances()
                    + ", removeInstances=" + context.getRemoveInstances());

        final Set<Entity> res = new HashSet<Entity>();

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            checkPermissions(context);

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);

            // persist new
            for (Entity entity : context.getCommitInstances()) {
                if (PersistenceHelper.isNew(entity)) {
                    em.persist(entity);
                    res.add(entity);
                }
            }
            // merge detached
            for (Entity entity : context.getCommitInstances()) {
                if (PersistenceHelper.isDetached(entity)) {
                    Entity e = em.merge(entity);
                    res.add(e);
                }
            }
            // remove
            for (Entity entity : context.getRemoveInstances()) {
                Entity e = em.merge(entity);
                em.remove(e);
                res.add(e);
            }

            for (Entity entity : res) {
                View view = context.getViews().get(entity);
                if (view != null) {
                    ViewHelper.fetchInstance(entity, view);
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }

        return res;
    }

    public Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext<Entity> context) {
        if (log.isDebugEnabled())
            log.debug("commit: commitInstances=" + context.getCommitInstances()
                    + ", removeInstances=" + context.getRemoveInstances());

        final Map<Entity, Entity> res = new HashMap<Entity, Entity>();

        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            checkPermissionsNotDetached(context);

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);

            // persist new or merge detached
            Set newInstanceIdSet = new HashSet(context.getNewInstanceIds());
            for (Entity entity : context.getCommitInstances()) {
                MetaClass metaClass = metadata.getSession().getClass(entity.getClass());
                if (newInstanceIdSet.contains(metaClass.getName() + "-" + entity.getId().toString())) {
                    em.persist(entity);
                    res.put(entity, entity);
                } else {
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
                    ViewHelper.fetchInstance(entry.getValue(), view);
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }

        return res;
    }

    @Nullable
    public <A extends Entity> A load(LoadContext context) {
        if (log.isDebugEnabled())
            log.debug("load: metaClass=" + context.getMetaClass() + ", id=" + context.getId() + ", view=" + context.getView());

        final MetaClass metaClass = metadata.getSession().getClass(context.getMetaClass());

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning null");
            return null;
        }

        Object result = null;

        Transaction tx = persistence.getTransaction();
        try {
            final EntityManager em = persistence.getEntityManager();

            // Set view only if StoreCache is disabled
            if (!dataCacheAPI.isStoreCacheEnabled() && context.getView() != null) {
                em.setView(context.getView());
            }

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);

            com.haulmont.cuba.core.Query query = createQuery(em, context);
            final List resultList = query.getResultList();
            if (!resultList.isEmpty())
                result = resultList.get(0);

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

    @Nonnull
    public <A extends Entity> List<A> loadList(LoadContext context) {
        if (log.isDebugEnabled())
            log.debug("loadList: metaClass=" + context.getMetaClass() + ", view=" + context.getView()
                    + ", query=" + (context.getQuery() == null ? null : printQuery(context.getQuery().getQueryString()))
                    + (context.getQuery() == null || context.getQuery().getFirstResult() == 0 ? "" : ", first=" + context.getQuery().getFirstResult())
                    + (context.getQuery() == null || context.getQuery().getMaxResults() == 0 ? "" : ", max=" + context.getQuery().getMaxResults()));

        final MetaClass metaClass = metadata.getSession().getClass(context.getMetaClass());

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning empty list");
            return Collections.emptyList();
        }

        List resultList;

        Transaction tx = persistence.getTransaction();
        try {
            final EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(context.isSoftDeletion());
            com.haulmont.cuba.core.Query query = createQuery(em, context);
            resultList = query.getResultList();

            // Fetch if StoreCache is enabled or there are lazy properties in the view
            if (context.getView() != null && (dataCacheAPI.isStoreCacheEnabled() || ViewHelper.hasLazyProperties(context.getView()))) {
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

    private String printQuery(String query) {
        if (query == null)
            return null;

        String str = StringHelper.removeExtraSpaces(query.replace("\n", " "));

        if (configuration.getConfig(ServerConfig.class).getCutLoadListQueries()) {
            str = StringUtils.abbreviate(str.replaceAll("[\\n\\r]", " "), 50);
        }

        return str;
    }

    protected <A extends Entity> com.haulmont.cuba.core.Query createQuery(EntityManager em, LoadContext context) {
        if ((context.getQuery() == null || StringUtils.isBlank(context.getQuery().getQueryString()))
                && context.getId() == null)
            throw new IllegalArgumentException("QueryString is empty");

        final MetaClass metaClass = metadata.getSession().getClass(context.getMetaClass());

        String queryString;
        Map<String, Object> queryParams;
        if (context.getQuery() != null && !StringUtils.isBlank(context.getQuery().getQueryString())) {
            queryString = context.getQuery().getQueryString();
            queryParams = context.getQuery().getParameters();
        } else {
            queryString = "select e from " + metaClass.getName() + " e where e.id = :entityId";
            queryParams = new HashMap<String, Object>();
            queryParams.put("entityId", context.getId());
        }

        com.haulmont.cuba.core.Query query = em.createQuery(queryString);

        if (context.isUseSecurityConstraints()) {
            boolean constraintsApplied = security.applyConstraints(query, metaClass.getName());
            if (constraintsApplied)
                log.debug("Constraints applyed: " + printQuery(query.getQueryString()));
        }

        if (context.getQuery() != null) {
            if (context.getQuery().getFirstResult() != 0)
                query.setFirstResult(context.getQuery().getFirstResult());
            if (context.getQuery().getMaxResults() != 0)
                query.setMaxResults(context.getQuery().getMaxResults());
        }

        QueryParser parser = QueryTransformerFactory.createParser(queryString);
        Set<String> paramNames = parser.getParamNames();

        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            final String name = entry.getKey();
            if (paramNames.contains(name)) {
                final Object value = entry.getValue();

                if (value instanceof Entity) {
                    query.setParameter(entry.getKey(), ((Entity) value).getId());

                } else if (value instanceof EnumClass) {
                    query.setParameter(entry.getKey(), ((EnumClass) value).getId());

                } else if (value instanceof Collection) {
                    List list = new ArrayList(((Collection) value).size());
                    for (Object item : (Collection) value) {
                        if (item instanceof Entity)
                            list.add(((Entity) item).getId());
                        else if (item instanceof EnumClass)
                            list.add(((EnumClass) item).getId());
                        else
                            list.add(item);
                    }
                    query.setParameter(entry.getKey(), list);

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
        if (!security.isEntityOpPermitted(metaClass, operation))
            throw new AccessDeniedException(PermissionType.ENTITY_OP, metaClass.getName());
    }

    protected void checkPermissions(CommitContext<Entity> context) {
        Set<MetaClass> checkedCreateRights = new HashSet<MetaClass>();
        Set<MetaClass> checkedUpdateRights = new HashSet<MetaClass>();
        Set<MetaClass> checkedDeleteRights = new HashSet<MetaClass>();

        for (Entity entity : context.getCommitInstances()) {
            MetaClass metaClass = entity != null ? entity.getMetaClass() : null;
            if (metaClass == null) continue;

            if (PersistenceHelper.isNew(entity)) {
                checkPermission(checkedCreateRights, metaClass, EntityOp.CREATE);
            } else {
                checkPermission(checkedUpdateRights, metaClass, EntityOp.UPDATE);
            }
        }

        for (Entity entity : context.getRemoveInstances()) {
            MetaClass metaClass = entity != null ? entity.getMetaClass() : null;
            if (metaClass == null) continue;
            checkPermission(checkedDeleteRights, metaClass, EntityOp.DELETE);

        }
    }

    protected void checkPermissionsNotDetached(NotDetachedCommitContext<Entity> context) {
        Set<MetaClass> checkedCreateRights = new HashSet<MetaClass>();
        Set<MetaClass> checkedUpdateRights = new HashSet<MetaClass>();
        Set<MetaClass> checkedDeleteRights = new HashSet<MetaClass>();

        Set newInstanceIdSet = new HashSet(context.getNewInstanceIds());
        for (Entity entity : context.getCommitInstances()) {
            MetaClass metaClass = entity != null ? entity.getMetaClass() : null;
            if (metaClass == null) continue;

            if (newInstanceIdSet.contains(metaClass.getName() + "-" + entity.getId())) {
                checkPermission(checkedCreateRights, metaClass, EntityOp.CREATE);
            } else {
                checkPermission(checkedUpdateRights, metaClass, EntityOp.UPDATE);
            }
        }

        for (Entity entity : context.getRemoveInstances()) {
            MetaClass metaClass = entity != null ? entity.getMetaClass() : null;
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
}

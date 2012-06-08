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
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.queryresults.QueryResultsManagerAPI;
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

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private QueryResultsManagerAPI queryResultsManager;

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

    @SuppressWarnings("unchecked")
    @Nonnull
    public <A extends Entity> List<A> loadList(LoadContext context) {
        if (log.isDebugEnabled())
            log.debug("loadList: metaClass=" + context.getMetaClass() + ", view=" + context.getView()
                    + (context.getPrevQueries().isEmpty() ? "" : ", to selected")
                    + ", query=" + (context.getQuery() == null ? null : DataServiceQueryBuilder.printQuery(context.getQuery().getQueryString()))
                    + (context.getQuery() == null || context.getQuery().getFirstResult() == 0 ? "" : ", first=" + context.getQuery().getFirstResult())
                    + (context.getQuery() == null || context.getQuery().getMaxResults() == 0 ? "" : ", max=" + context.getQuery().getMaxResults()));

        final MetaClass metaClass = metadata.getSession().getClass(context.getMetaClass());

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning empty list");
            return Collections.emptyList();
        }

        queryResultsManager.savePreviousQueryResults(context);

        List resultList;

        Transaction tx = persistence.getTransaction();
        try {
            final EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(context.isSoftDeletion());

            boolean ensureDistinct = false;
            if (configuration.getConfig(ServerConfig.class).getInMemoryDistinct()) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                        context.getQuery().getQueryString(), context.getMetaClass());
                ensureDistinct = transformer.removeDistinct();
                if (ensureDistinct) {
                    context.getQuery().setQueryString(transformer.getResult());
                }
            }
            Query query = createQuery(em, context);
            resultList = getResultList(context, query, ensureDistinct);

            // Fetch if StoreCache is enabled or there are lazy properties in the view
            if (context.getView() != null && (dataCacheAPI.isStoreCacheEnabled()
                    || ViewHelper.hasLazyProperties(context.getView())))
            {
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

    @SuppressWarnings("unchecked")
    private List getResultList(LoadContext context, Query query, boolean ensureDistinct) {
        List list = query.getResultList();
        if (!ensureDistinct)
            return list;

        int requestedFirst = context.getQuery().getFirstResult();
        LinkedHashSet set = new LinkedHashSet(list);
        if (set.size() == list.size() && requestedFirst == 0) {
            // If this is the first chunk and it has no duplicates, just return it
            return list;
        }
        // In case of not first chunk, even if there where no duplicates, start filling the set from zero
        // to ensure correct paging

        int requestedMax = context.getQuery().getMaxResults();
        int setSize = list.size() + requestedFirst;
        int factor = list.size() / set.size() * 2;

        set.clear();

        int firstResult = 0;
        int maxResults = (requestedFirst + requestedMax) * factor;
        int i = 0;
        while (set.size() < setSize) {
            if (i++ > 10) {
                log.warn("In-memory distinct: endless loop detected for " + context);
                break;
            }
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            list = query.getResultList();
            if (list.size() == 0)
                break;
            set.addAll(list);

            firstResult = firstResult + maxResults;
        }

        // Copy by iteration because subList() returns non-serializable class
        int max = Math.min(requestedFirst + requestedMax, set.size());
        List result = new ArrayList(max - requestedFirst);
        int j = 0;
        for (Object item : set) {
            if (j >= max)
                break;
            if (j >= requestedFirst)
                result.add(item);
            j++;
        }
        return result;
    }

    protected <A extends Entity> Query createQuery(EntityManager em, LoadContext context) {
        LoadContext.Query contextQuery = context.getQuery();
        if ((contextQuery == null || StringUtils.isBlank(contextQuery.getQueryString()))
                && context.getId() == null)
            throw new IllegalArgumentException("Query string or ID needed");

        DataServiceQueryBuilder queryBuilder = new DataServiceQueryBuilder(
                contextQuery == null ? null : contextQuery.getQueryString(),
                contextQuery == null ? null : contextQuery.getParameters(),
                context.getId(), context.getMetaClass(), context.isUseSecurityConstraints(), security);

        if (!context.getPrevQueries().isEmpty()) {
            log.debug("Restrict query by previous results");
            queryBuilder.restrictByPreviousResults(userSessionSource.getUserSession().getId(), context.getQueryKey());
        }
        Query query = queryBuilder.getQuery(em);

        if (contextQuery != null) {
            if (contextQuery.getFirstResult() != 0)
                query.setFirstResult(contextQuery.getFirstResult());
            if (contextQuery.getMaxResults() != 0)
                query.setMaxResults(contextQuery.getMaxResults());
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

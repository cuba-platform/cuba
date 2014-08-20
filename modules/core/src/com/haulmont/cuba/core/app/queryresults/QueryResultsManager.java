/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.queryresults;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.DataServiceQueryBuilder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * Supports functionality that allows queries from previously selected results.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(QueryResultsManagerAPI.NAME)
public class QueryResultsManager implements QueryResultsManagerAPI {

    @Inject
    private Persistence persistence;

    @Inject
    private PersistenceSecurity security;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private UserSessionsAPI userSessions;

    @Inject
    private ClusterManagerAPI clusterManager;

    @Inject
    private Configuration configuration;

    private static final int BATCH_SIZE = 100;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void savePreviousQueryResults(LoadContext loadContext) {
        List<LoadContext.Query> prevQueries = loadContext.getPrevQueries();
        if (prevQueries.isEmpty())
            return;

        LoadContext.Query contextQuery = prevQueries.get(prevQueries.size() - 1);
        String entityName = loadContext.getMetaClass();

        QueryParser parser = QueryTransformerFactory.createParser(contextQuery.getQueryString());
        if (!parser.isEntitySelect(entityName))
            return;

        int queryKey = loadContext.getQueryKey();

        if (resultsAlreadySaved(queryKey, contextQuery))
            return;

        List<UUID> idList;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(loadContext.isSoftDeletion());

            QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                    contextQuery.getQueryString(), entityName);
            transformer.replaceWithSelectId();
            transformer.removeOrderBy();
            String queryString = transformer.getResult();

            DataServiceQueryBuilder queryBuilder = new DataServiceQueryBuilder(queryString, contextQuery.getParameters(),
                    null, entityName, loadContext.isUseSecurityConstraints(), security);
            if (prevQueries.size() > 1) {
                queryBuilder.restrictByPreviousResults(userSessionSource.getUserSession().getId(), loadContext.getQueryKey());
            }
            Query query = queryBuilder.getQuery(em);

            String logMsg = "Load previous query results: " + DataServiceQueryBuilder.printQuery(query.getQueryString());
            log.debug(logMsg);
            long start = System.currentTimeMillis();

            idList = query.getResultList();
            tx.commit();

            log.debug("Done in " + (System.currentTimeMillis() - start) + "ms : " + logMsg);
        } finally {
            tx.end();
        }

        delete(queryKey);
        insert(queryKey, idList);
    }

    private boolean resultsAlreadySaved(Integer queryKey, LoadContext.Query query) {
        LinkedHashMap<Integer, QueryHolder> recentQueries =
                userSessionSource.getUserSession().getAttribute("_recentQueries");
        if (recentQueries == null) {
            recentQueries = new LinkedHashMap<Integer, QueryHolder>() {
                private static final long serialVersionUID = -901296839279897248L;

                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, QueryHolder> eldest) {
                    return size() > 10;
                }
            };
        }

        QueryHolder queryHolder = new QueryHolder(query);
        QueryHolder oldQueryHolder = recentQueries.put(queryKey, queryHolder);

        userSessionSource.getUserSession().setAttribute("_recentQueries", recentQueries);

        return queryHolder.equals(oldQueryHolder);
    }

    private void insert(int queryKey, List<UUID> idList) {
        UUID userSessionId = userSessionSource.getUserSession().getId();
        long start = System.currentTimeMillis();
        String logMsg = "Insert " + idList.size() + " query results for " + userSessionId + " / " + queryKey;
        log.debug(logMsg);

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            DbTypeConverter converter = persistence.getDbTypeConverter();

            String sql = "insert into SYS_QUERY_RESULT (SESSION_ID, QUERY_KEY, ENTITY_ID) values ('"
                    + userSessionId + "', " + queryKey + ", ?)";
            QueryRunner runner = new QueryRunner();
            try {

                int[] paramTypes = new int[] { Types.OTHER };
                for (int i = 0; i < idList.size(); i += BATCH_SIZE) {
                    List<UUID> sublist = idList.subList(i, Math.min(i + BATCH_SIZE, idList.size()));
                    Object[][] params = new Object[sublist.size()][1];
                    for (int j = 0; j < sublist.size(); j++) {
                        params[j][0] = converter.getSqlObject(sublist.get(j));
                    }
                    runner.batch(em.getConnection(), sql, params, paramTypes);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            log.debug("Done in " + (System.currentTimeMillis() - start) + "ms: " + logMsg);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private void delete(int queryKey) {
        UUID userSessionId = userSessionSource.getUserSession().getId();
        long start = System.currentTimeMillis();
        String logMsg = "Delete query results for " + userSessionId + " / " + queryKey;
        log.debug(logMsg);

        String sql = "delete from SYS_QUERY_RESULT where SESSION_ID = '"
                + userSessionId + "' and QUERY_KEY = " + queryKey;

        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            runner.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        log.debug("Done in " + (System.currentTimeMillis() - start) + "ms : " + logMsg);
    }

    @Override
    public void deleteForCurrentSession() {
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            runner.update("delete from SYS_QUERY_RESULT where SESSION_ID = '"
                    + userSessionSource.getUserSession().getId() + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteForInactiveSessions() {
        if (!AppContext.isStarted() || !clusterManager.isMaster()
                || !configuration.getConfig(GlobalConfig.class).getAllowQueryFromSelected())
            return;

        log.debug("Delete query results for inactive user sessions");

        StringBuilder sb = new StringBuilder("delete from SYS_QUERY_RESULT");
        Collection<UserSessionEntity> userSessionEntities = userSessions.getUserSessionInfo();
        if (!userSessionEntities.isEmpty()) {
            sb.append(" where SESSION_ID not in (");
            for (Iterator<UserSessionEntity> it = userSessionEntities.iterator(); it.hasNext(); ) {
                UserSessionEntity userSessionEntity = it.next();
                sb.append("'").append(userSessionEntity.getId()).append("'");
                if (it.hasNext())
                    sb.append(",");
            }
            sb.append(")");
        }
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            runner.update(sb.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static class QueryHolder implements Serializable {

        private static final long serialVersionUID = -6055610488135337366L;

        public final LoadContext.Query query;

        public QueryHolder(LoadContext.Query query) {
            this.query = query;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            QueryHolder that = (QueryHolder) o;

            if (query == null || that.query == null) return false;
            if (!ObjectUtils.equals(query.getQueryString(), that.query.getQueryString())) return false;
            if (!ObjectUtils.equals(query.getParameters(), that.query.getParameters())) return false;

            return true;
        }
    }
}
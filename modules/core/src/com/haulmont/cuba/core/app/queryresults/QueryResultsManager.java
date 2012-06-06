/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.queryresults;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.DataServiceQueryBuilder;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.app.UserSessionsAPI;
import com.haulmont.cuba.security.entity.UserSessionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.sql.SQLException;
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
    private Metadata metadata;

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

        List<UUID> idList;

        LoadContext.Query contextQuery = prevQueries.get(prevQueries.size() - 1);
        String entityName = loadContext.getMetaClass();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(loadContext.isSoftDeletion());

            QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                    contextQuery.getQueryString(), entityName);
            transformer.replaceWithSelectId();
            String queryString = transformer.getResult();

            DataServiceQueryBuilder queryBuilder = new DataServiceQueryBuilder(queryString, contextQuery.getParameters(),
                    null, entityName, loadContext.isUseSecurityConstraints(), security);
            if (prevQueries.size() > 1) {
                queryBuilder.restrictByPreviousResults(userSessionSource.getUserSession().getId(), loadContext.getQueryKey());
            }
            Query query = queryBuilder.getQuery(em);

            idList = query.getResultList();
            tx.commit();
        } finally {
            tx.end();
        }

        delete(loadContext.getQueryKey());
        insert(loadContext.getQueryKey(), idList);
    }

    private void insert(int queryKey, List<UUID> idList) {
        UUID userSessionId = userSessionSource.getUserSession().getId();
        log.debug("Insert query results for " + userSessionId + " / " + queryKey);

        String sql = "insert into SYS_QUERY_RESULT (SESSION_ID, QUERY_KEY, ENTITY_ID) values ('"
                + userSessionId + "', " + queryKey + ", ?)";
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            for (int i = 0; i < idList.size(); i += BATCH_SIZE) {
                List<UUID> sublist = idList.subList(i, Math.min(i + BATCH_SIZE, idList.size()));
                Object[][] params = new Object[sublist.size()][1];
                for (int j = 0; j < sublist.size(); j++) {
                    params[j][0] = sublist.get(j);
                }
                runner.batch(sql, params);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void delete(int queryKey) {
        UUID userSessionId = userSessionSource.getUserSession().getId();
        log.debug("Delete query results for " + userSessionId + " / " + queryKey);

        String sql = "delete from SYS_QUERY_RESULT where SESSION_ID = '"
                + userSessionId + "' and QUERY_KEY = " + queryKey;

        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            runner.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}

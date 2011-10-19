/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.DataSet;
import com.haulmont.cuba.report.EntityMap;
import com.haulmont.cuba.report.exception.ReportDataLoaderException;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class QueryDataLoader implements DataLoader {

    protected Map<String, Object> params = new HashMap<String, Object>();

    public QueryDataLoader(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public List<Map<String, Object>> loadData(DataSet dataSet, Band parentBand) {

        String queryParamName = dataSet.getQueryParamName();

        String queryString = (String) params.get(queryParamName);
        String view = (String) params.get(dataSet.getViewParamName());
        String entityClass = (String) params.get(dataSet.getEntityClassParamName());

        if (StringUtils.isEmpty(queryString) ||
                StringUtils.isEmpty(view) ||
                StringUtils.isEmpty(entityClass))
            throw new ReportDataLoaderException("Empty required parameters for QueryDataLoader");

        MetaClass metaClass;
        try {
            Class<?> clazz = Class.forName(entityClass);
            metaClass = MetadataProvider.getSession().getClass(clazz);
        } catch (ClassNotFoundException e) {
            throw new ReportDataLoaderException("Couldn't found entity class for Query");
        }

        Map<String, Object> queryParams = (Map<String, Object>) params.get(queryParamName + DataSet.QUERY_PARAMS_POSTFIX);

        View queryView = MetadataProvider.getViewRepository().getView(metaClass, view);

        Transaction tx = PersistenceProvider.createTransaction();

        EntityManager entityManager = PersistenceProvider.getEntityManager();
        Query query = entityManager.createQuery(queryString);
        query.setView(queryView);
        if (queryParams != null) {
            for (Map.Entry<String, Object> queryParamEntry : queryParams.entrySet()) {
                query.setParameter(queryParamEntry.getKey(), queryParamEntry.getValue());
            }
        }

        List queryResult;
        try {
            queryResult = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            throw new ReportDataLoaderException(e);
        } finally {
            tx.end();
        }

        List<Map<String, Object>> results = new LinkedList<Map<String, Object>>();
        for (Object entity : queryResult) {
            results.add(new EntityMap((Entity) entity));
        }

        return results;
    }
}

/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.report.exception.ReportingException;

import java.util.List;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class PrototypesLoader {

    /**
     * Load parameter data
     * @param parameterPrototype Parameter prototype
     * @return Entities list
     */
    public static List loadData(ParameterPrototype parameterPrototype) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(parameterPrototype.getMetaClassName());

        Map<String, Object> queryParams = parameterPrototype.getQueryParams();

        View queryView = MetadataProvider.getViewRepository().getView(metaClass, parameterPrototype.getViewName());

        Transaction tx = PersistenceProvider.createTransaction();

        EntityManager entityManager = PersistenceProvider.getEntityManager();
        Query query = entityManager.createQuery(parameterPrototype.getQueryString());
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
            throw new ReportingException(e);
        } finally {
            tx.end();
        }

        return queryResult;
    }
}
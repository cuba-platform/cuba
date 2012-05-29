/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.report.exception.ReportingException;
import com.haulmont.cuba.security.entity.EntityOp;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class PrototypesLoader {

    private Log log = LogFactory.getLog(PrototypesLoader.class);

    /**
     * Load parameter data
     *
     * @param parameterPrototype Parameter prototype
     * @return Entities list
     */
    public List loadData(ParameterPrototype parameterPrototype) {
        MetaClass metaClass = MetadataProvider.getSession().getClass(parameterPrototype.getMetaClassName());

        PersistenceSecurity security = AppContext.getBean(PersistenceSecurity.NAME);
        if (parameterPrototype.isUseSecurityConstraints()) {
            if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
                log.debug("reading of " + metaClass + " not permitted, returning empty list");
                return Collections.emptyList();
            }
        }

        Map<String, Object> queryParams = parameterPrototype.getQueryParams();

        View queryView = MetadataProvider.getViewRepository().getView(metaClass, parameterPrototype.getViewName());

        Transaction tx = PersistenceProvider.createTransaction();

        EntityManager entityManager = PersistenceProvider.getEntityManager();
        Query query = entityManager.createQuery(parameterPrototype.getQueryString());

        if (parameterPrototype.isUseSecurityConstraints()) {
            boolean constraintsApplied = security.applyConstraints(query, metaClass.getName());
            if (constraintsApplied)
                log.debug("Constraints applyed: " + printQuery(query.getQueryString()));
        }

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

    private String printQuery(String query) {
        if (query == null)
            return null;

        String str = StringHelper.removeExtraSpaces(query.replace("\n", " "));

        if (ConfigProvider.getConfig(ServerConfig.class).getCutLoadListQueries()) {
            str = StringUtils.abbreviate(str.replaceAll("[\\n\\r]", " "), 50);
        }

        return str;
    }
}
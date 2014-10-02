/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Builds {@link Query} instance to use in DataService.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DataServiceQueryBuilder {

    private Log log = LogFactory.getLog(getClass());

    private String queryString;
    private Map<String, Object> queryParams;
    private String entityName;
    private boolean useSecurityConstraints;
    private PersistenceSecurity security;

    public DataServiceQueryBuilder(String queryString, Map<String, Object> queryParams, Object id, String entityName,
                                   boolean useSecurityConstraints, PersistenceSecurity security)
    {
        this.entityName = entityName;
        this.useSecurityConstraints = useSecurityConstraints;
        this.security = security;
        if (!StringUtils.isBlank(queryString)) {
            this.queryString = queryString;
            this.queryParams = queryParams;
        } else {
            this.queryString = "select e from " + entityName + " e where e.id = :entityId";
            this.queryParams = new HashMap<String, Object>();
            this.queryParams.put("entityId", id);
        }
    }

    public void restrictByPreviousResults(UUID sessionId, int queryKey) {
        QueryTransformer transformer = QueryTransformerFactory.createTransformer(queryString, entityName);
        transformer.addJoinAndWhere(
                ", sys$QueryResult _qr",
                "_qr.entityId = {E}.id and _qr.sessionId = :_qr_sessionId and _qr.queryKey = " + queryKey
        );
        queryString = transformer.getResult();
        this.queryParams.put("_qr_sessionId", sessionId);
    }

    public Query getQuery(EntityManager em) {
        Query query = em.createQuery(queryString);

        if (useSecurityConstraints) {
            boolean constraintsApplied = security.applyConstraints(query);
            if (constraintsApplied && log.isDebugEnabled())
                log.debug("Constraints applyed: " + printQuery(query.getQueryString()));
        }

        QueryParser parser = QueryTransformerFactory.createParser(queryString);
        Set<String> paramNames = parser.getParamNames();

        for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
            String name = entry.getKey();
            if (paramNames.contains(name)) {
                Object value = entry.getValue();

                if (value instanceof Entity) {
                    value = ((Entity) value).getId();

                } else if (value instanceof EnumClass) {
                    value = ((EnumClass) value).getId();

                } else if (value instanceof Collection) {
                    List<Object> list = new ArrayList<Object>(((Collection) value).size());
                    for (Object item : (Collection) value) {
                        if (item instanceof Entity)
                            list.add(((Entity) item).getId());
                        else if (item instanceof EnumClass)
                            list.add(((EnumClass) item).getId());
                        else
                            list.add(item);
                    }
                    value = list;
                }

                query.setParameter(name, value);
            } else
                throw new DevelopmentException("Parameter '" + name + "' is not used in the query");
        }

        return query;
    }

    public static String printQuery(String query) {
        if (query == null)
            return null;

        String str = StringHelper.removeExtraSpaces(query.replace("\n", " "));

        if (AppBeans.get(Configuration.class).getConfig(ServerConfig.class).getCutLoadListQueries()) {
            str = StringUtils.abbreviate(str.replaceAll("[\\n\\r]", " "), 50);
        }

        return str;
    }
}

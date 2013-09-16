/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.PersistenceSecurity;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class PersistenceSecurityImpl extends SecurityImpl implements PersistenceSecurity {

    @Override
    public boolean applyConstraints(Query query, String entityName) {
        MetaClass original = metadata.getExtendedEntities().getOriginalMetaClass(metadata.getClassNN(entityName));

        List<String[]> constraints = userSessionSource.getUserSession().getConstraints(
                original == null ? entityName : original.getName());
        if (constraints.isEmpty())
            return false;

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                query.getQueryString(), entityName);

        for (String[] constraint : constraints) {
            String join = constraint[0];
            String where = constraint[1];
            if (StringUtils.isBlank(join)) {
                if (!StringUtils.isBlank(where))
                    transformer.addWhere(where);
            } else
                transformer.addJoinAndWhere(join, where);
        }
        query.setQueryString(transformer.getResult());
        for (String paramName : transformer.getAddedParams()) {
            setQueryParam(query, paramName);
        }
        return true;
    }

    @Override
    public void setQueryParam(Query query, String paramName) {
        if (paramName.startsWith(CONSTRAINT_PARAM_SESSION_ATTR)) {
            UserSession userSession = userSessionSource.getUserSession();

            String attrName = paramName.substring(CONSTRAINT_PARAM_SESSION_ATTR.length());

            if (CONSTRAINT_PARAM_USER_LOGIN.equals(attrName)) {
                String userLogin = userSession.getSubstitutedUser() != null ?
                        userSession.getSubstitutedUser().getLogin() :
                        userSession.getUser().getLogin();
                query.setParameter(paramName, userLogin);

            } else if (CONSTRAINT_PARAM_USER_ID.equals(attrName)) {
                UUID userId = userSession.getSubstitutedUser() != null ?
                        userSession.getSubstitutedUser().getId() :
                        userSession.getUser().getId();
                query.setParameter(paramName, userId);

            } else if (CONSTRAINT_PARAM_USER_GROUP_ID.equals(attrName)) {
                Object groupId = userSession.getSubstitutedUser() == null ?
                        userSession.getUser().getGroup().getId() :
                        userSession.getSubstitutedUser().getGroup().getId();
                query.setParameter(paramName, groupId);

            } else {
                Serializable value = userSession.getAttribute(attrName);
                query.setParameter(paramName, value);
            }
        }
    }
}

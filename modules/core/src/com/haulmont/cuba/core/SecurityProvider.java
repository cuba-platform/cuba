/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:27:17
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;

import java.util.Arrays;
import java.util.List;

public abstract class SecurityProvider
{
    public static final String IMPL_PROP = "cuba.SecurityProvider.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.SecurityProviderImpl";

    private static SecurityProvider instance;

    private static SecurityProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (SecurityProvider) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public static String currentUserLogin() {
        return getInstance().__currentUserSession().getLogin();
    }

    public static UserSession currentUserSession() {
        return getInstance().__currentUserSession();
    }

    public static boolean currentUserInRole(String role) {
        UserSession session = getInstance().__currentUserSession();
        return (Arrays.binarySearch(session.getRoles(), role) >= 0);
    }

    public static void applyConstraints(Query query, String entityName) {
        getInstance().__applyConstraints(query, entityName);
    }

    protected abstract UserSession __currentUserSession();

    protected void __applyConstraints(Query query, String entityName) {
        List<String> constraints = __currentUserSession().getConstraints(entityName);
        if (constraints.isEmpty())
            return;

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                query.getQueryString(), entityName);

        for (String constraint : constraints) {
            transformer.addWhere(constraint);
        }
        query.setQueryString(transformer.getResult());
        for (String paramName : transformer.getAddedParams()) {
            setQueryParam(query, paramName);
        }
    }

    protected void setQueryParam(Query query, String paramName) {
        if ("currentSubjectId".equals(paramName)) {
            query.setParameter("currentSubjectId", __currentUserSession().getSubjectId());
        }
        else if ("currentUserLogin".equals(paramName)) {
            query.setParameter("currentUserLogin", __currentUserSession().getLogin());
        }
        else if ("currentUserId".equals(paramName)) {
            query.setParameter("currentUserId", __currentUserSession().getUserId());
        }
    }
}

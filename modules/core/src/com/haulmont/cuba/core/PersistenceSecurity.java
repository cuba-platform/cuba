/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.Security;

/**
 * Interface providing methods to apply security on persistence layer.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface PersistenceSecurity extends Security {

    String NAME = "cuba_PersistenceSecurity";

    String CONSTRAINT_PARAM_SESSION_ATTR = "session$";
    String CONSTRAINT_PARAM_USER_LOGIN = "userLogin";
    String CONSTRAINT_PARAM_USER_ID = "userId";
    String CONSTRAINT_PARAM_USER_GROUP_ID = "userGroupId";

    /**
     * Modifies the query depending on current user's security constraints.
     * @param query         query to modify
     * @param entityName    name of entity which is quering
     * @return              true if any constraints have been applied
     */
    boolean applyConstraints(Query query, String entityName);

    /**
     * Sets the query param to a value provided by user session (see constants above).
     * @param query         Query instance
     * @param paramName     parameter to set
     */
    void setQueryParam(Query query, String paramName);
}

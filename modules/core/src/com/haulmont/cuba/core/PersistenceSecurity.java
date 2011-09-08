/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.Security;

/**
 * This interface provides some methods to apply security on persistence
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface PersistenceSecurity extends Security {

    String NAME = "cuba_PersistenceSecurity";

    String CONSTRAINT_PARAM_SESSION_ATTR = "session$";
    String CONSTRAINT_PARAM_USER_LOGIN = "userLogin";
    String CONSTRAINT_PARAM_USER_ID = "userId";
    String CONSTRAINT_PARAM_USER_GROUP_ID = "userGroupId";

    /**
     * Modifies the query depending on current user's security constraints
     * @param query         query to modify
     * @param entityName    name of entity which is quering
     */
    boolean applyConstraints(Query query, String entityName);

    /**
     * Sets the query param to a value provided by user session (see constants above)
     * @param query         Query instance
     * @param paramName     parameter to set
     */
    void setQueryParam(Query query, String paramName);
}

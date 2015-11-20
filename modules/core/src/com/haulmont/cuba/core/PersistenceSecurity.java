/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Security;

import java.util.Collection;

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
     *
     * @param query query to modify
     * @return true if any constraints have been applied
     */
    boolean applyConstraints(Query query);

    /**
     * Sets the query param to a value provided by user session (see constants above).
     *
     * @param query     Query instance
     * @param paramName parameter to set
     */
    void setQueryParam(Query query, String paramName);

    /**
     * Applies in-memory constraints to the entity
     * @param entity -
     * @return true, if entity should be filtered from client output
     */
    boolean applyConstraints(Entity entity);

    /**
     * Applies in-memory constraints to the collection of entities and filter the collection
     * @param entities -
     */
    void applyConstraints(Collection<Entity> entities);

    /**
     * Reads security token and restores filtered data
     * @param resultEntity -
     */
    void restoreFilteredData(BaseGenericIdEntity<?> resultEntity);
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

/**
 * Intfrastructure interface providing methods to check permissions of the current user.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface Security {

    String NAME = "cuba_Security";

    /**
     * Check if current user has permission to open a screen.
     *
     * @param windowAlias screen id as registered in <code>screens.xml</code>
     */
    boolean isScreenPermitted(String windowAlias);

    /**
     * Check if current user has permission to execute an entity operation.
     *
     * @param metaClass entity meta-class
     * @param entityOp  operation
     */
    boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp);

    /**
     * Check if current user has permission to execute an entity operation.
     *
     * @param entityClass entity class
     * @param entityOp    operation
     */
    boolean isEntityOpPermitted(Class<?> entityClass, EntityOp entityOp);

    /**
     * Check if current user has permission to an entity attribute.
     *
     * @param metaClass entity meta-class
     * @param property  entity attribute name
     * @param access    required access
     */
    boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access);

    /**
     * Check if current user has permission to an entity attribute.
     *
     * @param entityClass entity class
     * @param property    entity attribute name
     * @param access      required access
     */
    boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access);

    /**
     * Check if current user can modify an entity attribute. It means that he has the following permissions:
     * <ul>
     * <li> {@link EntityOp#CREATE} or {@link EntityOp#UPDATE} on the whole entity </li>
     * <li> {@link EntityAttrAccess#MODIFY} on the attribute </li>
     * </ul>
     *
     * @param metaClass    entity attribute's meta class
     * @param propertyName entity attribute's name
     */
    boolean isEntityAttrModificationPermitted(MetaClass metaClass, String propertyName);

    /**
     * Check if current user has permission to an entity attribute path.
     *
     * @param entityClass  entity class
     * @param propertyPath entity attribute path
     * @param access       required access
     */
    boolean isEntityPropertyPathPermitted(Class<?> entityClass, String propertyPath, EntityAttrAccess access);

    /**
     * Check if current user has permission to an entity attribute path.
     *
     * @param metaClass    entity meta-class
     * @param propertyPath entity attribute path
     * @param access       required access
     */
    boolean isEntityPropertyPathPermitted(MetaClass metaClass, String propertyPath, EntityAttrAccess access);

    /**
     * Check if current user has a specific permission.
     *
     * @param name specific permission id
     */
    boolean isSpecificPermitted(String name);
}
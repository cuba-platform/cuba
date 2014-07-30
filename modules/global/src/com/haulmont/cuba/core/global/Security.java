/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
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
     * Check if current user has permission to execute an entity operation. <br/>
     * Takes into account original metaclass of entity.
     *
     * @param metaClass entity meta-class
     * @param entityOp  operation
     */
    boolean isEntityOpPermitted(MetaClass metaClass, EntityOp entityOp);

    /**
     * Check if current user has permission to execute an entity operation. <br/>
     * Takes into account original metaclass of entity.
     *
     * @param entityClass entity class
     * @param entityOp    operation
     */
    boolean isEntityOpPermitted(Class<?> entityClass, EntityOp entityOp);

    /**
     * Check if current user has permission to an entity attribute. <br/>
     * Takes into account original metaclass of entity.
     *
     * @param metaClass entity meta-class
     * @param property  entity attribute name
     * @param access    required access
     */
    boolean isEntityAttrPermitted(MetaClass metaClass, String property, EntityAttrAccess access);

    /**
     * Check if current user has permission to an entity attribute. <br/>
     * Takes into account original metaclass of entity.
     *
     * @param entityClass entity class
     * @param property    entity attribute name
     * @param access      required access
     */
    boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access);

    /**
     * Check if current user can modify an entity attribute which is the last part of the path given.
     * It means that he has the following permissions:
     *
     * <ul>
     * <li> {@link EntityOp#CREATE} or {@link EntityOp#UPDATE} on the whole entity which the attribute belongs to</li>
     * <li> {@link EntityAttrAccess#MODIFY} on the attribute</li>
     * </ul>
     *
     * Takes into account original metaclass of entity.
     *
     * @param metaClass    entity meta class
     * @param propertyPath entity attribute path
     */
    boolean isEntityAttrUpdatePermitted(MetaClass metaClass, String propertyPath);

    /**
     * Check if current user can read an entity attribute which is the last part of the path given.
     * It means that he has the following permissions:
     *
     * <ul>
     * <li> {@link EntityOp#READ} on the whole entity which the attribute belongs to</li>
     * <li> {@link EntityAttrAccess#VIEW} on the attribute</li>
     * </ul>
     *
     * Takes into account original metaclass of entity.
     *
     * @param metaClass    entity meta class
     * @param propertyPath entity attribute path
     */
    boolean isEntityAttrReadPermitted(MetaClass metaClass, String propertyPath);

    /**
     * Check if current user has a specific permission.
     *
     * @param name specific permission id
     */
    boolean isSpecificPermitted(String name);
}
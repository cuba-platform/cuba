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
 * @author krivopustov
 * @version $Id$
 */
public interface Security {

    String NAME = "cuba_Security";

    /**
     * Check if current user has permission to open a screen.
     *
     * @param clientType    current client type
     * @param windowAlias   screen id as registered in <code>screens.xml</code>
     */
    boolean isScreenPermitted(ClientType clientType, String windowAlias);

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
     * @param entityClass   entity class
     * @param entityOp      operation
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
     * @param entityClass   entity class
     * @param property      entity attribute name
     * @param access        required access
     */
    boolean isEntityAttrPermitted(Class<?> entityClass, String property, EntityAttrAccess access);

    /**
     * Check if current user can modify an entity attribute. It means that he has the following permissions:
     * <ul>
     *     <li/> {@link EntityOp#CREATE} or {@link EntityOp#UPDATE} on the whole entity
     *     <li/> {@link EntityAttrAccess#MODIFY} on the attribute
     * </ul>
     * This method infers entity class from {@link MetaProperty#getDomain()}.
     *
     * @param metaProperty  entity attribute
     */
    boolean isEntityAttrModificationPermitted(MetaProperty metaProperty);

    /**
     * Check if current user has a specific permission.
     *
     * @param name specific permission id
     */
    boolean isSpecificPermitted(String name);
}

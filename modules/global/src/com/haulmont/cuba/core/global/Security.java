/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;

/**
 * Intfrastructure interface providing methods to check permissions of the current user.
 *
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

    /**
     * Check if current user has a specific permission.
     *
     * @param name specific permission id
     * @throws AccessDeniedException if the user has no specified permission
     */
    void checkSpecificPermission(String name);

    /**
     * Check if the operation type is permitted for the entity
     */
    boolean isPermitted(Entity entity, ConstraintOperationType operationType);

    /**
     * Check the special constraint permission for the entity
     */
    boolean isPermitted(Entity entity, String customCode);

    /**
     * Check if there are registered constraints for the metaClass or it's original metaClass
     */
    boolean hasConstraints(MetaClass metaClass);
}
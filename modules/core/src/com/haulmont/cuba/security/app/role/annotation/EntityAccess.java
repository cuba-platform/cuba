/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.security.app.role.annotation;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.EntityOp;

import java.lang.annotation.*;

/**
 * Defines permissions to allow operations on an entity (read, create, update, delete).
 *
 * <p>Example:
 *
 * <pre>
 * &#064;EntityAccess(entityClass = SomeEntity.class,
 *           operations = {EntityOp.DELETE, EntityOp.UPDATE})
 * </pre>
 * <p>
 * Instead of {@code entityClass} attribute an {@code entityName} can be used:
 *
 * <pre>
 * &#064;EntityAccess(entityName = "app_SomeEntity",
 *           operations = {EntityOp.DELETE, EntityOp.UPDATE})
 * </pre>
 * <p>
 * You may use wildcard for entity name if you want to allow operations for all entities:
 *
 * <pre>
 * &#064;EntityAccess(entityName = "*",
 *           operations = {EntityOp.CREATE, EntityOp.READ})
 * </pre>
 *
 * @see Role
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EntityAccessContainer.class)
public @interface EntityAccess {

    Class<? extends Entity> entityClass() default NullEntity.class;

    String entityName() default "";

    EntityOp[] operations() default {};

}

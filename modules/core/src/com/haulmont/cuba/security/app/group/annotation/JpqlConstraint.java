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

package com.haulmont.cuba.security.app.group.annotation;

import com.haulmont.cuba.core.entity.Entity;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Defines database READ constraint.
 *
 * <p>Example:
 *
 * <pre>
 *     &#064;JpqlConstraint(target = User.class, where = "{E}.active = true")
 *     &#064;Override
 *     public SetOfAccessConstraints accessConstraints() {
 *          return super.entityAccess();
 *     }
 * </pre>
 *
 * @see AccessGroup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(JpqlConstraintContainer.class)
public @interface JpqlConstraint {

    /**
     * Constraint entity class.
     * If target class isn't specified, uses entity class from the in-memory constraint method
     */
    Class<? extends Entity> target() default Entity.class;

    @AliasFor("where")
    String value() default "";

    /**
     * JPQL where clause
     */
    @AliasFor("value")
    String where() default "";

    /**
     * JPQL join clause
     */
    String join() default "";
}

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

import java.lang.annotation.*;

/**
 * Defines permissions to access individual entity attributes.
 *
 * <p>Example:
 *
 * <pre>
 *     &#064;EntityAttributeAccess(target = SomeEntity.class, allow = {"someAttribute"})
 * </pre>
 *
 * @see Role
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(EntityAttributeAccessContainer.class)
public @interface EntityAttributeAccess {

    Class<? extends Entity> target();

    String[] deny() default {};

    String[] allow() default {};

    String[] readOnly() default {};

}

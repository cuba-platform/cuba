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

import java.lang.annotation.*;

/**
 * Defines custom in-memory constraint for the access group.
 *
 * <p>Example:
 *
 * <pre>
 *     &#064;CustomConstraint("constraintCode")
 *     public boolean userConstraints(User user) {
 *          return Boolean.TRUE.equals(user.getActive());
 *     }
 * </pre>
 *
 * @see AccessGroup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(CustomConstraintContainer.class)
public @interface CustomConstraint {
    /**
     * Custom constraint code
     */
    String value();
}


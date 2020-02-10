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

import java.lang.annotation.*;

/**
 * Defines specific permissions access.
 *
 * <p>Example:
 *
 * <pre>
 * &#064;SpecificAccess(permissions = {"my.specific.permission1", "my.specific.permission2"})
 * </pre>
 *
 * Wildcard may be used if the role must grant access to all specific permissions:
 *
 * <pre>
 * &#064;SpecificAccess(permissions = "*")
 * </pre>
 *
 *
 * @see Role
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(SpecificAccessContainer.class)
public @interface SpecificAccess {

    String[] permissions();

}

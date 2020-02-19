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
 * Defines permissions to access screen elements.
 *
 * <p>Example:
 *
 * <pre>
 * &#064;ScreenComponentAccess(screenId = "myapp_SomeEntity.browse", deny = {"someGroupBox"})
 * </pre>
 *
 * @see Role
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ScreenComponentAccessContainer.class)
public @interface ScreenComponentAccess {

    String screenId();

    String[] deny() default {};

    String[] view() default {};

    String[] modify() default {};
}

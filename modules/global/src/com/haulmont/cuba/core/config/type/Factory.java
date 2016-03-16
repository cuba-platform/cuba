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

package com.haulmont.cuba.core.config.type;

import java.lang.annotation.*;

/**
 * Annotation that identifies how to create a type from a string.
 * This can be specified with either a static method that converts a
 * string to an instance of the class, or else a TypeFactory class that
 * performs this operation. Only one value should be specified. When
 * applied to a class, applies to the class itself; when applied to a
 * method, applies to the method's parameter.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Factory {
    /**
     * The name of a static method that can convert a string to an instance
     * of the class.
     */
    String method() default "";

    /**
     * A class that can convert a string to an instance of the class.
     */
    Class<? extends TypeFactory> factory();
}
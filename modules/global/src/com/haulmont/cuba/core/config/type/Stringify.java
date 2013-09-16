/*
 * A High-Level Framework for Application Configuration
 *
 * Copyright 2007 Merlin Hughes / Learning Objects, Inc.
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

package com.haulmont.cuba.core.config.type;

import java.lang.annotation.*;

/**
 * Annotation that identifies how to convert a type to a string. This
 * can be specified with either an instance method that converts an
 * object to its primitive (ultimately string) representation, or else a
 * TypeStringify class that performs this operation. Only one value should
 * be specified. When applied to a class, applies to the class itself; when
 * applied to a method, applies to the method's return type.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Stringify
{
    /**
     * The name of a method that can convert an object to its primitive
     * (ultimately string) form.
     */
    public String method() default "";

    /**
     * A class that can convert an object to its string form.
     */
    public Class<? extends TypeStringify> stringify();
}

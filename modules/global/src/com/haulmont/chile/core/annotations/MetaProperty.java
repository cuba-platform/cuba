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

package com.haulmont.chile.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define a non-persistent attribute, or to specify additional properties of a persistent
 * attribute.
 *
 */
@Target({java.lang.annotation.ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaProperty {

    /**
     * Whether the attribute is required.
     */
    boolean mandatory() default false;

    /**
     * Explicitly defined datatype that overrides a datatype inferred from the attribute Java type.
     */
    String datatype() default "";

    /**
     * Related properties are fetched from the database when this property is included in a view.
     */
    String[] related() default "";
}

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
 * Defines session attribute for the access group.
 *
 * <p>Example:
 *
 * <pre>
 *     &#064;SessionAttribute(name = "key1", value = "value1")
 *     &#064;Override
 *     public Map&lt;String, Serializable&gt; sessionAttributes() {
 *          return super.sessionAttributes();
 *     }
 * </pre>
 *
 * @see AccessGroup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(SessionAttributeContainer.class)
public @interface SessionAttribute {

    /**
     * Attribute name
     */
    String name();

    /**
     * Attribute value as string
     */
    String value();

    /**
     * Attribute java class.
     * Java class uses while transformation from string presentation to java class value
     */
    Class<?> javaClass() default String.class;
}

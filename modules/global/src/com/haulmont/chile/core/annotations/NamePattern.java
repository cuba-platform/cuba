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

import com.haulmont.cuba.core.entity.annotation.MetaAnnotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines an instance name format pattern in the form {0}|{1}, where
 * <ul>
 *     <li>{0} - format string which can be one of two types:
 *     <ul>
 *      <li>A string with {@code %s} placeholders for formatted values of entity attributes.
 *          Attribute values are formatted to strings according to their {@code Datatype}s.
 *      <li>A name of this object method, returning string, with {@code #} symbol in the beginning.
 *     </ul>
 *     <li>{1} - comma-separated list of field names, corresponding to format {0}. These fields are also used for
 *     defining a {@code _minimal} view of this entity.</li>
 * </ul>
 * Extra spaces between parts are not allowed.
 * <p>
 * Attribute placeholders example:
 * <p>
 * {@code @NamePattern("%s : %s|name,address")}
 * <p>
 * Method example:
 * <p>
 * <pre>
 * {@code @NamePattern("#getCaption|name,address")}
 * public class Foo extends StandardEntity {
 * ...
 *     public String getCaption() {
 *         return name + " : " + address;
 *     }
 * }
 * </pre>
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MetaAnnotation
public @interface NamePattern {
    String value();
}
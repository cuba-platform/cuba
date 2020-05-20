/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.gui.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method should be shown in Studio Screen Designer
 * as a nested group of elements of UI component, e.g. columns, actions, fields.
 */
@Target({ElementType.METHOD})
public @interface StudioCollection {
    /**
     * @return XML tag of the collection
     */
    String xmlElement() default "";

    /**
     * @return XML tag of the collection elements
     */
    String itemXmlElement() default "";

    /**
     * Specifies URL pointing to the documentation page for the annotated element.
     * Used in <i>CUBA Documentation</i> action in the Studio Screen Designer.<br>
     * If the documentation is version dependent, use %VERSION% as a placeholder.
     * It will be replaced with the minor version (e.g. 1.2) of the artifact containing UI component.
     *
     * @return URL of the documentation page
     */
    String documentationURL() default "";

    /**
     * @return collection item properties
     */
    StudioProperty[] itemProperties() default {};
}

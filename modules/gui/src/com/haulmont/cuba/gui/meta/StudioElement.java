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

package com.haulmont.cuba.gui.meta;

import java.lang.annotation.*;

/**
 * Indicates that the annotated interface should be available in Studio Screen Designer as a part of UI Component,
 * e.g. column, action, field, etc. Provides metadata for Properties Panel of Screen Designer.
 *
 * When used on the getter or setter method, indicates that the annotated method should be shown in
 * Studio Screen Designer as a nested element of UI component, e.g. validator, formatter.
 * Method return type or parameter type will be used to determine element type.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface StudioElement {
    /**
     * @return caption of the element in Studio Screen Designer
     */
    String caption() default "";

    /**
     * @return description of the element in Studio Screen Designer
     */
    String description() default "";

    /**
     * @return UI Components Hierarchy icon, SVG or PNG
     */
    String icon() default "";

    /**
     * @return XML tag of the element
     */
    String xmlElement() default "";

    /**
     * Specifies xml namespace required for the element.
     *
     * @return xml namespace
     */
    String xmlns() default "";

    /**
     * Specifies xml namespace alias required for the element.
     *
     * @return xml namespace alias
     */
    String xmlnsAlias() default "";

    /**
     * @return name of the default property, it will be automatically selected in Properties panel
     */
    String defaultProperty() default "";

    /**
     * @return names of unsupported properties that should be hidden from Properties panel
     */
    String[] unsupportedProperties() default {};
}
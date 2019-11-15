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
 * Indicates that the annotated method should be shown in Studio Screen Designer as UI component property.
 * The corresponding XML element is {@code property} with {@code name} and {@code value} attributes:
 * <pre>
 * &lt;properties&gt;
 *     &lt;property name=&quot;openMode&quot; value=&quot;DIALOG&quot;/&gt;
 * &lt;/properties&gt;
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface StudioPropertiesItem {
    /**
     * @return name of the property
     */
    String name() default "";

    /**
     * @return type of property
     */
    PropertyType type() default PropertyType.AUTO;

    /**
     * @return caption of the property in Screen Designer Properties
     */
    String caption() default "";

    /**
     * @return description of the property in Screen Designer Properties
     */
    String description() default "";

    /**
     * @return category of the property in Properties Panel, e.g. General, Size, Data
     */
    String category() default "";

    /**
     * @return true if the property is required for component definition in XML
     */
    boolean required() default false;

    /**
     * @return default value of the property that can be safely omitted from XML
     */
    String defaultValue() default "";

    /**
     * @return enumeration options
     */
    String[] options() default {};
}
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
 */
@Documented
@Target(ElementType.METHOD)
public @interface StudioProperty {
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
     * @return category of the property in Component Inspector, e.g. General, Size, Data
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
     * Context dependent list of options for the component property.<p>
     * {@link PropertyType#ENUMERATION}: enumeration options
     * {@link PropertyType#BEAN_REF}: list of the allowed Spring bean base classes
     * {@link PropertyType#COMPONENT_REF}: list of the allowed component base classes
     * {@link PropertyType#PROPERTY_PATH_REF}: list of the allowed types for the property.
     * Use registered {@link com.haulmont.chile.core.datatypes.Datatype} names for the datatype properties
     * or <i>"to_one"</i> and <i>"to_many"</i> for the association properties.
     *
     * @return options
     */
    String[] options() default {};

    /**
     * @return target XML attribute name, if not set then equal to name of the property
     */
    String xmlAttribute() default "";

    /**
     * @return target XML element name
     */
    String xmlElement() default "";

    /**
     * Specifies name of the type parameter for the generic UI component or Facet that is provided by the property.<br>
     * The actual class for the type parameter can be resolved for the following property types:
     * {@link PropertyType#JAVA_CLASS_NAME}, {@link PropertyType#ENTITY_CLASS}, {@link PropertyType#SCREEN_CLASS_NAME},
     * {@link PropertyType#DATACONTAINER_REF}, {@link PropertyType#COLLECTION_DATACONTAINER_REF}, {@link PropertyType#DATASOURCE_REF},
     * {@link PropertyType#COLLECTION_DATASOURCE_REF}, {@link PropertyType#COMPONENT_REF}
     *
     * @return name of the type parameter
     */
    String typeParameter() default "";
}
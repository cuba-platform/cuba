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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.gui.icons.CubaIcon;

/**
 * Type of UI component property.
 */
public enum PropertyType {
    /**
     * Infer type using parameter of the annotated method.
     */
    AUTO,

    /**
     * Integer type.
     */
    INTEGER,
    /**
     * Long type.
     */
    LONG,
    /**
     * Double type.
     */
    DOUBLE,
    /**
     * String type.
     */
    STRING,
    /**
     * Boolean type.
     */
    BOOLEAN,
    /**
     * Char type.
     */
    CHARACTER,

    /**
     * Date in standard format: YYYY-MM-DD
     */
    DATE,
    /**
     * Date with time in standard format: YYYY-MM-DD hh:mm:ss
     */
    DATE_TIME,
    /**
     * Time in standard format: hh:mm:ss
     */
    TIME,

    /**
     * Value from the property options.
     */
    ENUMERATION,

    /**
     * Identifier of a component, action or sub part. Must be a valid Java identifier.
     */
    COMPONENT_ID,
    /**
     * Icon path or ID of icon from predefined CUBA icons.
     *
     * @see CubaIcon
     */
    ICON_ID,
    /**
     * Size value, e.g. width or height
     */
    SIZE,
    /**
     * String value or message key with msg:// or mainMsg:// prefix.
     */
    LOCALIZED_STRING,
    /**
     * JPA QL string.
     */
    JPA_QUERY,
    /**
     * Name of Entity meta class.
     */
    ENTITY_NAME,
    /**
     * FQN of Java class.
     */
    JAVA_CLASS_NAME,

    /**
     * CSS classes separated with space symbol.
     */
    CSS_CLASSNAME_LIST,
    /**
     * Inline CSS properties.
     */
    CSS_BLOCK,

    /**
     * Spring Bean ID.
     */
    BEAN_REF,

    /**
     * ID of a component defined in screen.
     */
    COMPONENT_REF,
    /**
     * ID of a datasource.
     */
    DATASOURCE_REF,
    /**
     * ID of a data loader.
     */
    DATALOADER_REF,
    /**
     * ID of a data container
     */
    DATACONTAINER_REF,
    /**
     * ID of a collection data container.
     */
    COLLECTION_DATACONTAINER_REF,
    /**
     * Name of a data model property.
     */
    PROPERTY_REF,
    /**
     * Entity property path.
     */
    PROPERTY_PATH_REF,
    /**
     * ID of a datatype.
     *
     * @see Datatype
     */
    DATATYPE_ID,

    /**
     * Keyboard shortcut.
     */
    SHORTCUT
}
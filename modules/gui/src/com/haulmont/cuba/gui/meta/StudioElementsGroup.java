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

import java.lang.annotation.Documented;

/**
 * Indicates that the annotated method should be shown in Studio Screen Designer
 * as a nested group of elements of UI component, e.g. columns, actions, fields.
 */
@Documented
public @interface StudioElementsGroup {

    /**
     * @return target XML element name
     */
    String xmlElement() default "";

    /**
     * @return Component Hierarchy icon, SVG or PNG
     */
    String icon() default "";
}

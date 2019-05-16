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
 * JavaDoc
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface StudioFacet {
    /**
     * @return caption of the event in Screen Designer Events
     */
    String caption() default "";

    /**
     * @return description of the event in Screen Designer Events
     */
    String description() default "";

    /**
     * @return category of the event in Screen Designer Events, e.g. Data, Interaction, Lifecycle
     */
    String category() default "";

    /**
     * @return UI components Palette icon, SVG or PNG
     */
    String icon() default "";

    /**
     * @return XML tag of the facet
     */
    String xmlElement() default "";

    /**
     * JavaDoc
     *
     * @return
     */
    String xmlns() default "";

    /**
     * JavaDoc
     *
     * @return
     */
    String xmlnsAlias() default "";

    /**
     * @return name of the default property, it will be automatically selected in Properties panel
     */
    String defaultProperty() default "";

    /**
     * @return name of the default event, it will be used for scaffolding of the event handler on double click
     */
    String defaultEvent() default "";

}
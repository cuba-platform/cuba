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

import com.haulmont.cuba.gui.components.Facet;

import java.lang.annotation.*;

/**
 * Indicates that the annotated interface should be available in Studio Screen Designer as a non-visual component.
 * Provides metadata for components Palette and Component Inspector of Screen Designer. The annotated interface must be a
 * direct or indirect subclass of {@link Facet}.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface StudioFacet {
    /**
     * @return caption in Studio Screen Designer Palette
     */
    String caption() default "";

    /**
     * @return description of the facet in Studio Screen Designer Palette
     */
    String description() default "";

    /**
     * @return category of the event in Screen Designer Events, e.g. Data, Interaction, Lifecycle
     */
    String category() default "";

    /**
     * Specifies path to the component icon, SVG or PNG. Relative to the component module root.
     * The icon used in the Component Palette and Component Hierarchy.
     * @return relative path to the SVG or PNG icon file.
     */
    String icon() default "";

    /**
     * @return XML tag of the facet
     */
    String xmlElement() default "";

    /**
     * Specifies xml namespace required for the facet.
     *
     * @return xml namespace
     */
    String xmlns() default "";

    /**
     * Specifies xml namespace alias required for the facet.
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

    /**
     * Specifies URL pointing to the documentation page for the annotated Facet.
     * Used in <i>CUBA Documentation</i> action in the Studio Screen Designer.<br>
     * If the documentation is version dependent, use %VERSION% as a placeholder.
     * It will be replaced with the minor version (e.g. 1.2) of the artifact containing Facet.
     *
     * @return URL of the documentation page
     */
    String documentationURL() default "";
}
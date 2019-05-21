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

package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.components.Facet;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader.ComponentContext;
import org.dom4j.Element;

/**
 * Interface for Spring Beans providing non-visual components for screens.
 *
 * @param <T> type of facet
 * @see Facet
 */
public interface FacetProvider<T extends Facet> {
    /**
     * @return facet interface
     */
    Class<T> getFacetClass();

    /**
     * @return new instance of the facet class
     */
    T create();

    /**
     * @return facet XML tag
     */
    String getFacetTag();

    /**
     * Loads properties of the facet from XML.
     *
     * @param facet   facet
     * @param element XML element
     * @param context loading context
     */
    void loadFromXml(T facet, Element element, ComponentContext context);
}
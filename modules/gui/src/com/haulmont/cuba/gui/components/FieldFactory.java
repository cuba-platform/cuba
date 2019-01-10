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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

public interface FieldFactory {

    /**
     * Creates a component for {@link Table}.
     *
     * @param datasource    a datasource
     * @param property      a property
     * @param xmlDescriptor an xml descriptor
     * @return created component
     * @deprecated Use {@link #createField(EntityValueSource, String, Element)} instead
     */
    @Deprecated
    Component createField(Datasource datasource, String property, Element xmlDescriptor);

    /**
     * Creates a component for {@link Table}.
     *
     * @param valueSource   a value source
     * @param property      a property
     * @param xmlDescriptor an xml descriptor
     * @return created component
     */
    Component createField(EntityValueSource valueSource, String property, Element xmlDescriptor);
}
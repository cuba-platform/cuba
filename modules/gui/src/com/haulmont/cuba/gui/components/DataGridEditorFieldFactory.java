/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.data.Datasource;

/**
 * Factory that generates components for {@link DataGrid} editor.
 */
public interface DataGridEditorFieldFactory {
    String NAME = "cuba_DataGridEditorFieldFactory";

    /**
     * Generates component for {@link DataGrid} editor.
     *
     * @param datasource editing item datasource
     * @param property   editing item property
     * @return generated component
     * @throws IllegalStateException if created component doesn't implement the {@link Field} interface
     * @deprecated Use {@link #createField(EntityValueSource, String)} instead
     */
    @Deprecated
    Field createField(Datasource datasource, String property);

    /**
     * Generates component for {@link DataGrid} editor.
     *
     * @param valueSource editing item value source
     * @param property    editing item property
     * @return generated component
     * @throws IllegalStateException if created component doesn't implement the {@link Field} interface
     */
    Field createField(EntityValueSource valueSource, String property);
}

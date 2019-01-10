/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.factories;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentGenerationContext;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.DataGridEditorFieldFactory;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.UiComponentsGenerator;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.data.Datasource;

import javax.inject.Inject;

@org.springframework.stereotype.Component(DataGridEditorFieldFactory.NAME)
public class DataGridEditorFieldFactoryImpl implements DataGridEditorFieldFactory {

    @Inject
    protected UiComponentsGenerator uiComponentsGenerator;

    @SuppressWarnings("unchecked")
    @Override
    public Field createField(Datasource datasource, String property) {
        return createField(new DatasourceValueSource(datasource, property), property);
    }

    @Override
    public Field createField(EntityValueSource valueSource, String property) {
        MetaClass metaClass = valueSource.getEntityMetaClass();

        ComponentGenerationContext context = new ComponentGenerationContext(metaClass, property)
                .setValueSource(valueSource)
                .setComponentClass(DataGrid.class);

        Component component = uiComponentsGenerator.generate(context);
        if (component instanceof Field) {
            return (Field) component;
        }

        throw new IllegalStateException("Editor field must implement com.haulmont.cuba.gui.components.Field");
    }
}
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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;

// todo move to package
@org.springframework.stereotype.Component(DataGridEditorFieldFactory.NAME)
public class DataGridEditorFieldFactoryImpl implements DataGridEditorFieldFactory {

    @Inject
    protected ComponentsFactory componentsFactory;

    @Override
    public Field createField(Datasource datasource, String property) {
        return createFieldComponent(datasource, property);
    }

    protected Field createFieldComponent(Datasource datasource, String property) {
        MetaClass metaClass = resolveMetaClass(datasource);

        ComponentGenerationContext context = new ComponentGenerationContext(metaClass, property)
                .setDatasource(datasource)
                .setComponentClass(DataGrid.class);

        Component component = componentsFactory.createComponent(context);
        if (component instanceof Field) {
            return (Field) component;
        }

        throw new IllegalStateException("Editor field must implement com.haulmont.cuba.gui.components.Field");
    }

    protected MetaClass resolveMetaClass(Datasource datasource) {
        return datasource instanceof RuntimePropsDatasource ?
                ((RuntimePropsDatasource) datasource).resolveCategorizedEntityClass() : datasource.getMetaClass();
    }
}

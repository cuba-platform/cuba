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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentGenerationContext;
import com.haulmont.cuba.gui.components.FieldFactory;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.UiComponentsGenerator;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;

import javax.annotation.Nullable;

public abstract class AbstractFieldFactory implements FieldFactory {

    protected UiComponentsGenerator componentsGenerator = AppBeans.get(UiComponentsGenerator.NAME);

    @SuppressWarnings("unchecked")
    @Override
    public Component createField(Datasource datasource, String property, Element xmlDescriptor) {
        return createField(new DatasourceValueSource(datasource, property), property, xmlDescriptor);
    }

    @Override
    public Component createField(EntityValueSource valueSource, String property, Element xmlDescriptor) {
        MetaClass metaClass = valueSource.getEntityMetaClass();

        ComponentGenerationContext context = new ComponentGenerationContext(metaClass, property)
                .setValueSource(valueSource)
                .setOptions(getOptions(valueSource, property))
                .setXmlDescriptor(xmlDescriptor)
                .setComponentClass(Table.class);

        return componentsGenerator.generate(context);
    }

    @Nullable
    protected abstract Options getOptions(EntityValueSource container, String property);
}
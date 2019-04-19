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
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributeComponentsGenerator;
import org.apache.commons.lang3.BooleanUtils;

import javax.inject.Inject;

@org.springframework.stereotype.Component(FieldGroupFieldFactory.NAME)
public class FieldGroupFieldFactoryImpl implements FieldGroupFieldFactory {

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected UiComponentsGenerator uiComponentsGenerator;

    @Inject
    protected DynamicAttributeComponentsGenerator dynamicAttributeComponentsGenerator;

    @Override
    public GeneratedField createField(FieldGroup.FieldConfig fc) {
        return createFieldComponent(fc);
    }

    protected GeneratedField createFieldComponent(FieldGroup.FieldConfig fc) {
        MetaClass metaClass = resolveMetaClass(fc.getTargetDatasource());

        if (DynamicAttributesUtils.isDynamicAttribute(fc.getProperty())) {
            CategoryAttribute attribute = dynamicAttributes.getAttributeForMetaClass(metaClass, fc.getProperty());
            if (attribute != null && BooleanUtils.isTrue(attribute.getIsCollection())) {
                //noinspection unchecked
                DatasourceValueSource valueSource = new DatasourceValueSource(fc.getTargetDatasource(), fc.getProperty());
                Component fieldComponent = dynamicAttributeComponentsGenerator.generateComponent(valueSource, attribute);
                return new GeneratedField(fieldComponent);
            }
        }

        ComponentGenerationContext context = new ComponentGenerationContext(metaClass, fc.getProperty())
                .setDatasource(fc.getTargetDatasource())
                .setOptionsDatasource(fc.getOptionsDatasource())
                .setXmlDescriptor(fc.getXmlDescriptor())
                .setComponentClass(FieldGroup.class);

        return new GeneratedField(uiComponentsGenerator.generate(context));
    }

    protected MetaClass resolveMetaClass(Datasource datasource) {
        return datasource instanceof RuntimePropsDatasource ?
                ((RuntimePropsDatasource) datasource).resolveCategorizedEntityClass() : datasource.getMetaClass();
    }
}
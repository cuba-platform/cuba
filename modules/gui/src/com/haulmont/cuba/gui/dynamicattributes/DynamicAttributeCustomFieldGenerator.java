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
 */

package com.haulmont.cuba.gui.dynamicattributes;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Field generator that creates component for editing dynamic attributes with collection type.
 */
public class DynamicAttributeCustomFieldGenerator implements FieldGroup.CustomFieldGenerator {

    private static final Logger log = LoggerFactory.getLogger(DynamicAttributeCustomFieldGenerator.class);

    @Override
    public Component generateField(Datasource datasource, String propertyId) {
        ComponentsFactory componentsFactory = AppBeans.get(ComponentsFactory.class);
        ListEditor listEditor = componentsFactory.createComponent(ListEditor.class);

        MetaClass entityMetaClass = datasource instanceof RuntimePropsDatasource
                                    ? ((RuntimePropsDatasource) datasource).resolveCategorizedEntityClass()
                                    : datasource.getMetaClass();

        MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(entityMetaClass, propertyId);
        if (metaPropertyPath == null) {
            log.error("MetaPropertyPath for dynamic attribute {} not found", propertyId);
            return null;
        }
        CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaPropertyPath.getMetaProperty());
        if (categoryAttribute == null) {
            log.error("Dynamic attribute {} not found", propertyId);
            return null;
        }

        listEditor.setEntityJoinClause(categoryAttribute.getJoinClause());
        listEditor.setEntityWhereClause(categoryAttribute.getWhereClause());

        ListEditor.ItemType itemType = listEditorItemTypeFromDynamicAttrType(categoryAttribute.getDataType());
        listEditor.setItemType(itemType);

        Metadata metadata = AppBeans.get(Metadata.class);
        Scripting scripting = AppBeans.get(Scripting.class);
        if (!Strings.isNullOrEmpty(categoryAttribute.getEntityClass())) {
            Class<?> clazz = scripting.loadClass(categoryAttribute.getEntityClass());
            if (clazz == null) {
                log.error("Unable to find class of entity {} for dynamic attribute {}",
                        categoryAttribute.getEntityClass(), categoryAttribute.getCode());
                return null;
            }

            MetaClass metaClass = metadata.getClassNN(clazz);
            listEditor.setEntityName(metaClass.getName());
            listEditor.setUseLookupField(BooleanUtils.isTrue(categoryAttribute.getLookup()));
        }

        //noinspection unchecked
        datasource.addStateChangeListener(e -> {
            if (e.getState() == Datasource.State.VALID) {
                Object value = datasource.getItem().getValue(propertyId);
                if (value != null && value instanceof Collection) {
                    listEditor.setValue(value);
                }
            }
        });

        listEditor.addValueChangeListener(e -> {
            datasource.getItem().setValue(propertyId, e.getValue());
        });
        listEditor.setWidthFull();
        return listEditor;
    }

    protected ListEditor.ItemType listEditorItemTypeFromDynamicAttrType(PropertyType propertyType) {
        switch (propertyType) {
            case ENTITY:
                return ListEditor.ItemType.ENTITY;
            case DATE:
                return ListEditor.ItemType.DATETIME;
            case DOUBLE:
                return ListEditor.ItemType.DOUBLE;
            case INTEGER:
                return ListEditor.ItemType.INTEGER;
            case STRING:
            case ENUMERATION:
                return ListEditor.ItemType.STRING;
            default:
                throw new IllegalStateException(String.format("PropertyType %s not supported", propertyType));
        }
    }
}
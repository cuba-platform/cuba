/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.dynamicattributes;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListEditor;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Creates components for editing dynamic attributes
 */
@org.springframework.stereotype.Component(DynamicAttributeComponentsGenerator.NAME)
public class DynamicAttributeComponentsGenerator {
    public static final String NAME = "cuba_DynamicAttributeListEditorGenerator";

    private static final Logger log = LoggerFactory.getLogger(DynamicAttributeComponentsGenerator.class);

    /* Beans */
    protected UiComponents uiComponents;
    protected Metadata metadata;
    protected Scripting scripting;

    @Inject
    public void setUiComponents(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }

    @Inject
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Inject
    public void setScripting(Scripting scripting) {
        this.scripting = scripting;
    }

    /**
     * Creates ListEditor component.
     *
     * @param valueSource value source
     * @param propertyId  property id of dynamic attribute
     * @return list editor component or null
     */
    @Nullable
    public Component generateComponent(ValueSource valueSource, String propertyId) {
        if (valueSource instanceof EntityValueSource) {
            MetaClass metaClass = ((EntityValueSource) valueSource).getEntityMetaClass();
            MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(metaClass, propertyId);
            if (metaPropertyPath == null) {
                log.error("MetaPropertyPath for dynamic attribute {} not found", propertyId);
                return null;
            }
            return generateComponent(valueSource, metaPropertyPath);
        }
        return null;
    }

    /**
     * Creates ListEditor component.
     *
     * @param valueSource      value source
     * @param metaPropertyPath meta property path of dynamic attribute
     * @return list editor component or null
     */
    @Nullable
    public Component generateComponent(ValueSource valueSource, MetaPropertyPath metaPropertyPath) {
        CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaPropertyPath.getMetaProperty());
        if (categoryAttribute == null) {
            log.error("Dynamic attribute {} not found", metaPropertyPath.getMetaProperty().getName());
            return null;
        }
        return generateComponent(valueSource, categoryAttribute);
    }

    /**
     * Creates ListEditor component.
     *
     * @param valueSource       value source
     * @param categoryAttribute category attribute
     * @return list editor component or null
     */
    @Nullable
    public Component generateComponent(ValueSource valueSource, CategoryAttribute categoryAttribute) {
        ListEditor listEditor = uiComponents.create(ListEditor.NAME);

        listEditor.setEntityJoinClause(categoryAttribute.getJoinClause());
        listEditor.setEntityWhereClause(categoryAttribute.getWhereClause());

        ListEditor.ItemType itemType = getListEditorItemType(categoryAttribute.getDataType());
        listEditor.setItemType(itemType);

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
        listEditor.setValueSource(valueSource);

        if (PropertyType.ENUMERATION.equals(categoryAttribute.getDataType())) {
            //noinspection unchecked
            listEditor.setOptionsMap(categoryAttribute.getLocalizedEnumerationMap());
        }

        return listEditor;
    }

    protected ListEditor.ItemType getListEditorItemType(PropertyType propertyType) {
        switch (propertyType) {
            case ENTITY:
                return ListEditor.ItemType.ENTITY;
            case DATE:
                return ListEditor.ItemType.DATETIME;
            case DATE_WITHOUT_TIME:
                return ListEditor.ItemType.DATE;
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

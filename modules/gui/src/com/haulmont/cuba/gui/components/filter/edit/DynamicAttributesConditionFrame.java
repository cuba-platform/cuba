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

package com.haulmont.cuba.gui.components.filter.edit;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.ReferenceToEntitySupport;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.core.global.filter.OpManager;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.DynamicAttributesCondition;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;

import javax.inject.Inject;
import java.util.*;

public class DynamicAttributesConditionFrame extends ConditionFrame<DynamicAttributesCondition> {
    @Inject
    protected Metadata metadata;

    @Inject
    protected LookupField<Category> categoryLookup;

    @Inject
    protected LookupField<CategoryAttribute> attributeLookup;

    @Inject
    protected LookupField<Op> operationLookup;

    @Inject
    protected Label categoryLabel;

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected TextField<String> caption;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        categoryLookup.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                fillAttributeSelect((Category) e.getValue());
            }
        });

        attributeLookup.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                CategoryAttribute categoryAttribute = (CategoryAttribute) e.getValue();
                fillOperationSelect(categoryAttribute);
            }
        });
    }

    @Override
    public void setCondition(DynamicAttributesCondition condition) {
        super.setCondition(condition);
        fillCategorySelect();
        caption.setValue(condition.getCaption());
    }

    protected String checkCondition() {
        if (categoryLookup.getValue() == null) {
            return "filter.dynamicAttributesConditionFrame.selectCategory";
        }
        if (attributeLookup.getValue() == null) {
            return "filter.dynamicAttributesConditionFrame.selectAttribute";
        }
        if (operationLookup.getValue() == null) {
            return "filter.dynamicAttributesConditionFrame.selectOperator";
        }
        return null;
    }

    @Override
    public boolean commit() {
        if (!super.commit())
            return false;

        String error = checkCondition();
        if (error != null) {
            showNotification(messages.getMainMessage(error), Frame.NotificationType.TRAY);
            return false;
        }

        CategoryAttribute attribute = attributeLookup.getValue();

        String alias = condition.getEntityAlias();
        String cavAlias = "cav" + RandomStringUtils.randomNumeric(5);

        String paramName;
        String operation = operationLookup.<Op>getValue().forJpql();
        Op op = operationLookup.getValue();

        Class javaClass = DynamicAttributesUtils.getAttributeClass(attribute);
        String propertyPath = Strings.isNullOrEmpty(condition.getPropertyPath()) ? "" : "." + condition.getPropertyPath();
        ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);
        paramName = paramBuilder.createParamName(condition);

        String cavEntityId = referenceToEntitySupport.getReferenceIdPropertyName(condition.getDatasource().getMetaClass());

        String where;
        if (op == Op.NOT_EMPTY) {
            where = "(exists (select " + cavAlias + " from sys$CategoryAttributeValue " + cavAlias +
                    " where " + cavAlias + ".entity." + cavEntityId + "=" +
                    "{E}" +
                    propertyPath +
                    ".id and " + cavAlias + ".categoryAttribute.id='" +
                    attributeLookup.<CategoryAttribute>getValue().getId() + "'))";
        } else {
            String valueFieldName = "stringValue";
            if (Entity.class.isAssignableFrom(javaClass))
                valueFieldName = "entityValue." + referenceToEntitySupport.getReferenceIdPropertyName(metadata.getClassNN(javaClass)) ;
            else if (String.class.isAssignableFrom(javaClass))
                valueFieldName = "stringValue";
            else if (Integer.class.isAssignableFrom(javaClass))
                valueFieldName = "intValue";
            else if (Double.class.isAssignableFrom(javaClass))
                valueFieldName = "doubleValue";
            else if (Boolean.class.isAssignableFrom(javaClass))
                valueFieldName = "booleanValue";
            else if (Date.class.isAssignableFrom(javaClass))
                valueFieldName = "dateValue";

            if (!attribute.getIsCollection()) {
                condition.setJoin(", sys$CategoryAttributeValue " + cavAlias + " ");

                String paramStr = " ? ";
                where = cavAlias + ".entity." + cavEntityId + "=" +
                        "{E}" +
                        propertyPath +
                        ".id and " + cavAlias + "." +
                        valueFieldName +
                        " " +
                        operation +
                        (op.isUnary() ? " " : paramStr) + "and " + cavAlias + ".categoryAttribute.id='" +
                        attributeLookup.<CategoryAttribute>getValue().getId() + "'";
                where = where.replace("?", ":" + paramName);
            } else {
                where = "(exists (select " + cavAlias + " from sys$CategoryAttributeValue " + cavAlias +
                        " where " + cavAlias + ".entity." + cavEntityId + "=" + "{E}" + propertyPath + ".id and "
                        + cavAlias + "." + valueFieldName + " = :" + paramName + " and " +
                        cavAlias + ".categoryAttribute.id='" + attributeLookup.<CategoryAttribute>getValue().getId() + "'))";
            }
        }

        condition.setWhere(where);
        condition.setUnary(op.isUnary());
        condition.setEntityParamView(null);
        condition.setEntityParamWhere(null);
        condition.setInExpr(Op.IN.equals(op) || Op.NOT_IN.equals(op));
        condition.setOperator(operationLookup.<Op>getValue());
        Class paramJavaClass = op.isUnary() ? Boolean.class : javaClass;
        condition.setJavaClass(javaClass);

        Param param = Param.Builder.getInstance()
                .setName(paramName)
                .setJavaClass(paramJavaClass)
                .setDataSource(condition.getDatasource())
                .setProperty(DynamicAttributesUtils.getMetaPropertyPath(null, attribute).getMetaProperty())
                .setInExpr(condition.getInExpr())
                .setRequired(condition.getRequired())
                .setCategoryAttrId(attribute.getId())
                .build();

        Object defaultValue = condition.getParam().getDefaultValue();
        param.setDefaultValue(defaultValue);

        condition.setParam(param);
        condition.setCategoryId(categoryLookup.getValue().getId());
        condition.setCategoryAttributeId(attributeLookup.getValue().getId());
        condition.setIsCollection(BooleanUtils.isTrue(attributeLookup.getValue().getIsCollection()));
        condition.setLocCaption(attribute.getLocaleName());
        condition.setCaption(caption.getValue());

        return true;
    }

    protected void fillCategorySelect() {
        DynamicAttributes dynamicAttributes = AppBeans.get(DynamicAttributes.NAME);
        MetaClass metaClass = condition.getDatasource().getMetaClass();
        if (!Strings.isNullOrEmpty(condition.getPropertyPath())) {
            MetaPropertyPath propertyPath = metaClass.getPropertyPath(condition.getPropertyPath());
            if (propertyPath == null) {
                throw new RuntimeException("Property path " + condition.getPropertyPath() + " doesn't exist");
            }
            metaClass = propertyPath.getRange().asClass();

        }
        Collection<Category> categories = dynamicAttributes.getCategoriesForMetaClass(metaClass);
        UUID catId = condition.getCategoryId();
        Category selectedCategory = null;
        Map<String, Category> categoriesMap = new TreeMap<>();
        if (categories.size() == 1 && (catId == null || Objects.equals(catId, categories.iterator().next().getId()))) {
            Category category = categories.iterator().next();
            categoryLookup.setVisible(false);
            categoryLabel.setVisible(false);
            attributeLookup.requestFocus();
            categoriesMap.put(category.getName(), category);
            categoryLookup.setOptionsMap(categoriesMap);
            categoryLookup.setValue(category);
            fillAttributeSelect(category);
        } else {
            categoryLookup.setVisible(true);
            categoryLabel.setVisible(true);
            for (Category category : categories) {
                categoriesMap.put(category.getName(), category);
                if (category.getId().equals(catId)) {
                    selectedCategory = category;
                }
            }
            categoryLookup.setOptionsMap(categoriesMap);
            categoryLookup.setValue(selectedCategory);
        }
    }

    protected void fillAttributeSelect(Category category) {
        UUID attrId = condition.getCategoryAttributeId();
        CategoryAttribute selectedAttribute = null;
        Map<String, CategoryAttribute> attributesMap = new TreeMap<>();
        for (CategoryAttribute attribute : category.getCategoryAttrs()) {
            attributesMap.put(attribute.getLocaleName(), attribute);
            if (attribute.getId().equals(attrId)) {
                selectedAttribute = attribute;
            }
        }
        attributeLookup.setOptionsMap(attributesMap);
        attributeLookup.setValue(selectedAttribute);
    }

    protected void fillOperationSelect(CategoryAttribute categoryAttribute) {
        Class clazz = DynamicAttributesUtils.getAttributeClass(categoryAttribute);
        OpManager opManager = AppBeans.get(OpManager.class);
        EnumSet<Op> availableOps = BooleanUtils.isTrue(categoryAttribute.getIsCollection()) ?
                opManager.availableOpsForCollectionDynamicAttribute() : opManager.availableOps(clazz);
        List<Op> ops = new LinkedList<>(availableOps);
        operationLookup.setOptionsList(ops);
        Op operator = condition.getOperator();
        if (operator != null) {
            operationLookup.setValue(operator);
        }
    }
}
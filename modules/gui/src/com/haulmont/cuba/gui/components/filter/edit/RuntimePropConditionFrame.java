/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.RuntimePropCondition;
import com.haulmont.cuba.gui.data.RuntimePropertiesHelper;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.ObjectUtils;

import javax.inject.Inject;
import java.util.*;

/**
 * @author devyatkin
 * @version $Id$
 */
public class RuntimePropConditionFrame extends ConditionFrame<RuntimePropCondition> {

    @Inject
    protected ThemeConstants themeConstants;

    @Inject
    protected DataService dataService;

    @Inject
    protected Metadata metadata;

    @Inject
    protected LookupField categoryLookup;

    @Inject
    protected LookupField attributeLookup;

    @Inject
    protected LookupField operationLookup;

    @Inject
    protected Label categoryLabel;


    protected Messages messages;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        categoryLookup.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value != null)
                    fillAttributeSelect((Category) value);
            }
        });

        attributeLookup.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value != null)
                    fillOperationSelect(RuntimePropertiesHelper.getAttributeClass((CategoryAttribute) value));
            }
        });
    }

    @Override
    public void setCondition(RuntimePropCondition condition) {
        super.setCondition(condition);
        fillCategorySelect();
    }

    protected String checkCondition() {
        if (categoryLookup.getValue() == null) {
            return "RuntimePropConditionFrame.selectCategory";
        }
        if (attributeLookup.getValue() == null) {
            return "RuntimePropConditionFrame.selectAttribute";
        }
        if (operationLookup.getValue() == null) {
            return "RuntimePropConditionFrame.selectOperator";
        }
        return null;
    }

    @Override
    public boolean commit() {
        if (!super.commit())
            return false;

        String error = checkCondition();
        if (error != null) {
            showNotification(getMessage(error), IFrame.NotificationType.TRAY);
            return false;
        }

        CategoryAttribute attribute = attributeLookup.getValue();

        String alias = condition.getEntityAlias();
        condition.setJoin("join " + alias + ".category.categoryAttrs ca, sys$CategoryAttributeValue cav ");

        String paramName;
        String operation = operationLookup.<Op>getValue().getText();
        Op op = operationLookup.getValue();

        Class javaClass = RuntimePropertiesHelper.getAttributeClass(attribute);
        String valueFieldName = "stringValue";

        if (SetValueEntity.class.isAssignableFrom(javaClass)) {
            valueFieldName = "stringValue";
        } else if (Entity.class.isAssignableFrom(javaClass))
            valueFieldName = "entityValue";
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

        String paramStr = " ? ";
        if (!op.isUnary())
            if (Op.IN.equals(op) || Op.NOT_IN.equals(op))
                paramStr = " ( ? ) ";

        String where = "cav.entityId=" +
                alias +
                ".id and cav." +
                valueFieldName +
                " " +
                operation +
                (op.isUnary() ? " " : paramStr) + "and cav.categoryAttribute.id='" +
                attributeLookup.<CategoryAttribute>getValue().getId() + "'";
        paramName = condition.createParamName();
        where = where.replace("?", ":" + paramName);

        condition.setWhere(where);
        condition.setUnary(op.isUnary());
        condition.setEntityParamView(null);
        condition.setEntityParamWhere(null);
        condition.setInExpr(Op.IN.equals(op) || Op.NOT_IN.equals(op));
        condition.setOperator(operationLookup.<Op>getValue());
        Param param;
        Class paramJavaClass = op.isUnary() ? null : javaClass;
        if (SetValueEntity.class.isAssignableFrom(javaClass)) {
            condition.setJavaClass(String.class);
            param = new Param(paramName, paramJavaClass, null, null, condition.getDatasource(),
                    condition.getInExpr(), attribute.getId(), condition.getRequired());
        } else {
            condition.setJavaClass(javaClass);
            param = new Param(paramName, paramJavaClass, null, null, condition.getDatasource(),
                    condition.getInExpr(), condition.getRequired());
        }

        condition.setParam(param);
        condition.setCategoryId(categoryLookup.<Category>getValue().getId());
        condition.setCategoryAttributeId(attributeLookup.<CategoryAttribute>getValue().getId());
        condition.setLocCaption(attribute.getName());

        return true;
    }

    protected void fillCategorySelect() {
        List<String> metaClassName = getMetaClassNames();
        LoadContext context = new LoadContext(Category.class);
        context.setView("_minimal")
                .setQueryString("select c from sys$Category c where c.entityType in :entityType")
                .setParameter("entityType", metaClassName);
        List<Category> categories = dataService.loadList(context);
        UUID catId = condition.getCategoryId();
        Category selectedCategory = null;
        Map<String, Object> categoriesMap = new TreeMap<String, Object>();
        if (categories.size() == 1 && (catId == null || ObjectUtils.equals(catId, categories.get(0).getId()))) {
            Category category = categories.get(0);
            categoryLookup.setVisible(false);
            categoryLabel.setVisible(false);
            attributeLookup.requestFocus();
            categoriesMap.put(category.getName(), category);
            categoryLookup.setOptionsMap(categoriesMap);
            categoryLookup.setValue(category);
            fillAttributeSelect(categories.get(0));
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

    protected List<String> getMetaClassNames() {
        String metaClassName = condition.getDatasource().getMetaClass().getName();
        List<String> metaClassNames = new ArrayList<>();
        metaClassNames.add(metaClassName);
        MetaClass currentMetaClass = metadata.getClass(metaClassName);
        if (currentMetaClass == null)
            return metaClassNames;
        Collection<MetaClass> descendants = currentMetaClass.getDescendants();
        for (MetaClass metaClass : descendants) {
            metaClassNames.add(metaClass.getName());
        }
        return metaClassNames;
    }

    protected void fillAttributeSelect(Category category) {
        LoadContext context = new LoadContext(CategoryAttribute.class);
        LoadContext.Query query = context.setQueryString("select ca from sys$CategoryAttribute ca where ca.category.id = :id ");
        query.setParameter("id", category.getId());
        context.setView("_local");
        List<CategoryAttribute> attributes = dataService.loadList(context);
        UUID attrId = condition.getCategoryAttributeId();
        CategoryAttribute selectedAttribute = null;
        Map<String, Object> attributesMap = new TreeMap<>();
        for (CategoryAttribute attribute : attributes) {
            attributesMap.put(attribute.getName(), attribute);
            if (attribute.getId().equals(attrId)) {
                selectedAttribute = attribute;
            }
        }
        attributeLookup.setOptionsMap(attributesMap);
        attributeLookup.setValue(selectedAttribute);
    }

    protected void fillOperationSelect(Class clazz) {
        List ops = new LinkedList(Op.availableOps(clazz));
        operationLookup.setOptionsList(ops);
        Op operator = condition.getOperator();
        if (operator != null) {
            operationLookup.setValue(operator);
        }
    }
}

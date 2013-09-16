/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.RuntimePropertiesHelper;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public abstract class AbstractRuntimePropConditionEditDlg<T> {
    protected static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";
    protected static final String FIELD_WIDTH = "250px";

    protected AbstractRuntimePropCondition condition;
    protected String messagesPack;
    private DataService dataService;
    protected LookupField categorySelect;
    protected LookupField attributeSelect;
    protected LookupField operationSelect;
    protected Button btnOk;
    protected Button btnCancel;
    protected Label categoryLabel;
    protected Label attributeLabel;
    protected Label operationLabel;
    private ComponentsFactory factory = AppConfig.getFactory();

    public AbstractRuntimePropConditionEditDlg(AbstractRuntimePropCondition condition) {
        dataService = ServiceLocator.lookup(DataService.NAME);
        this.condition = condition;
        messagesPack = AppConfig.getMessagesPack();

        categoryLabel = factory.createComponent(Label.NAME);
        categoryLabel.setValue(MessageProvider.getMessage(MESSAGES_PACK, "RuntimePropConditionEditDlg.categoryLabel"));

        categorySelect = factory.createComponent(LookupField.NAME);
        categorySelect.setWidth(FIELD_WIDTH);
        categorySelect.setRequired(true);
        categorySelect.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value != null)
                    fillAttributeSelect((Category) value);
            }
        });
        categorySelect.requestFocus();

        attributeLabel = factory.createComponent(Label.NAME);
        attributeLabel.setValue(MessageProvider.getMessage(MESSAGES_PACK, "RuntimePropConditionEditDlg.attributeLabel"));

        attributeSelect = factory.createComponent(LookupField.NAME);
        attributeSelect.setWidth(FIELD_WIDTH);
        attributeSelect.setRequired(true);
        attributeSelect.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                if (value != null)
                    fillOperationSelect(RuntimePropertiesHelper.getAttributeClass((CategoryAttribute) value));
            }
        });

        operationLabel = factory.createComponent(Label.NAME);
        operationLabel.setValue(MessageProvider.getMessage(MESSAGES_PACK, "RuntimePropConditionEditDlg.operationLabel"));

        operationSelect = factory.createComponent(LookupField.NAME);
        operationSelect.setWidth(FIELD_WIDTH);
        operationSelect.setRequired(true);

        btnOk = factory.createComponent(Button.NAME);
        btnOk.setIcon("icons/ok.png");
        btnOk.setCaption(MessageProvider.getMessage(messagesPack, "actions.Ok"));
        btnOk.setAction(new AbstractAction("OK") {
            @Override
            public void actionPerform(Component component) {
                if (commit())
                    closeDlg();
            }
        });

        btnCancel = factory.createComponent(Button.NAME);
        btnCancel.setIcon("icons/cancel.png");
        btnCancel.setCaption(MessageProvider.getMessage(messagesPack, "actions.Cancel"));
        btnCancel.setAction(new AbstractAction("CANCEL") {
            @Override
            public void actionPerform(Component component) {
                closeDlg();
            }
        });
        fillCategorySelect();
    }

    protected String checkCondition() {
        if (categorySelect.getValue() == null) {
            return "RuntimePropConditionEditDlg.selectCategory";
        }
        if (attributeSelect.getValue() == null) {
            return "RuntimePropConditionEditDlg.selectAttribute";
        }
        if (operationSelect.getValue() == null) {
            return "RuntimePropConditionEditDlg.selectOperator";
        }
        return null;
    }

    protected boolean commit() {
        String error = checkCondition();
        if (error != null) {
            showNotification(MessageProvider.getMessage(MESSAGES_PACK, error), IFrame.NotificationType.TRAY);
            return false;
        }

        CategoryAttribute attribute = attributeSelect.getValue();

        condition.setLocCaption(attribute.getName());
        String alias = condition.getEntityAlias();
        condition.setJoin("join " + alias + ".category.categoryAttrs ca, sys$CategoryAttributeValue cav ");

        String paramName;
        String categoryAttrParamName = condition.createParamName();
        String operation = operationSelect.<Op>getValue().getText();
        Op op = operationSelect.getValue();

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
                (op.isUnary() ? " " : paramStr) + "and cav.categoryAttribute.id=:" +
                categoryAttrParamName;
        paramName = condition.createParamName();
        where = where.replace("?", ":" + paramName);

        condition.setWhere(where);
        condition.setUnary(op.isUnary());
        condition.setEntityParamView(null);
        condition.setEntityParamWhere(null);
        condition.setInExpr(Op.IN.equals(op) || Op.NOT_IN.equals(op));
        condition.setOperator(operationSelect.<Op>getValue());
        AbstractParam param;
        Class paramJavaClass = op.isUnary() ? null : javaClass;
        if (SetValueEntity.class.isAssignableFrom(javaClass)) {
            condition.setJavaClass(String.class);
            param = getParamFactory().createParam(paramName, paramJavaClass, null, null, condition.getDatasource(),
                    condition.isInExpr(), attribute.getId(), condition.isRequired());
        } else {
            condition.setJavaClass(javaClass);
            param = getParamFactory().createParam(paramName, paramJavaClass, null, null, condition.getDatasource(),
                    condition.isInExpr(), condition.isRequired());
        }

        condition.setParam(param);

        AbstractParam categoryAttrParam = getParamFactory().createParam(categoryAttrParamName, UUID.class,
                null, null, condition.getDatasource(), false, condition.isRequired());
        categoryAttrParam.setValue(attributeSelect.<CategoryAttribute>getValue().getId());
        condition.setCategoryAttributeParam(categoryAttrParam);
        condition.setCategoryId(categorySelect.<Category>getValue().getId());
        return true;
    }

    protected void fillCategorySelect() {
        LoadContext context = new LoadContext(Category.class);
        LoadContext.Query query = context.setQueryString("select c from sys$Category c where c.entityType=:entityType");
        query.setParameter("entityType", condition.getDatasource().getMetaClass().getName());
        context.setView("_minimal");
        List<Category> categories = dataService.loadList(context);
        UUID catId = condition.getCategoryId();
        Category selectedCategory = null;
        Map<String, Object> categoriesMap = new HashMap<String, Object>();
        if (categories.size() == 1 && (catId == null || ObjectUtils.equals(catId, categories.get(0).getId()))) {
            Category category = categories.get(0);
            categorySelect.setVisible(false);
            categoryLabel.setVisible(false);
            attributeSelect.requestFocus();
            categoriesMap.put(category.getName(), category);
            categorySelect.setOptionsMap(categoriesMap);
            categorySelect.setValue(category);
            fillAttributeSelect(categories.get(0));
        } else {
            categorySelect.setVisible(true);
            categoryLabel.setVisible(true);
            for (Category category : categories) {
                categoriesMap.put(category.getName(), category);
                if (category.getId().equals(catId)) {
                    selectedCategory = category;
                }
            }
            categorySelect.setOptionsMap(categoriesMap);
            categorySelect.setValue(selectedCategory);
        }
    }

    protected void fillAttributeSelect(Category category) {
        LoadContext context = new LoadContext(CategoryAttribute.class);
        LoadContext.Query query = context.setQueryString("select ca from sys$CategoryAttribute ca where ca.category.id = :id ");
        query.setParameter("id", category.getId());
        context.setView("_local");
        List<CategoryAttribute> attributes = dataService.loadList(context);
        UUID attrId = null;
        if (condition.getCategoryAttributeParam() != null) {
            AbstractParam p = condition.getCategoryAttributeParam();
            attrId = (UUID) p.getValue();
        }
        CategoryAttribute selectedAttribute = null;
        Map<String, Object> attributesMap = new HashMap<String, Object>();
        for (CategoryAttribute attribute : attributes) {
            attributesMap.put(attribute.getName(), attribute);
            if (attribute.getId().equals(attrId)) {
                selectedAttribute = attribute;
            }
        }
        attributeSelect.setOptionsMap(attributesMap);
        attributeSelect.setValue(selectedAttribute);
    }

    protected void fillOperationSelect(Class clazz) {
        List ops = new LinkedList(Op.availableOps(clazz));
        operationSelect.setOptionsList(ops);
        Op operator = condition.getOperator();
        if (operator != null) {
            operationSelect.setValue(operator);
        }
    }

    protected abstract void closeDlg();

    public abstract T getImpl();

    protected abstract ParamFactory getParamFactory();

    protected abstract void showNotification(String msg, IFrame.NotificationType type);
}

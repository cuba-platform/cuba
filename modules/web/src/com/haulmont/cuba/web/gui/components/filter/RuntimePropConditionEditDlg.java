/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.sys.SetValueEntity;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ServiceLocator;

import com.haulmont.cuba.gui.data.RuntimePropertiesHelper;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.data.Property;
import com.vaadin.ui.*;

import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropConditionEditDlg extends Window {

    private RuntimePropCondition condition;
    private String messagesPack;
    private TextField conditionName;
    private Select categorySelect;
    private Select attributeSelect;
    private Select operationSelect;
    private DataService dataService;

    private static final String FIELD_WIDTH = "250px";

    public RuntimePropConditionEditDlg(final RuntimePropCondition condition) {
        super(condition.getLocCaption());

        dataService = ServiceLocator.lookup(DataService.NAME);

        setWidth("370px");
        this.condition = condition;
        messagesPack = AppConfig.getInstance().getMessagesPack();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);

        GridLayout grid = new GridLayout();
        grid.setColumns(2);
        grid.setSpacing(true);
        grid.setMargin(true, false, true, false);
        grid.setRows(4);

        Label nameLab = new Label(MessageProvider.getMessage(getClass(), "RuntimePropConditionEditDlg.nameLabel"));
        grid.addComponent(nameLab, 0, 0);
        grid.setComponentAlignment(nameLab, Alignment.MIDDLE_RIGHT);

        conditionName = new TextField();
        conditionName.setWidth(FIELD_WIDTH);
        conditionName.setNullRepresentation("");
        conditionName.setValue(condition.getLocCaption());
        grid.addComponent(conditionName, 1, 0);

        Label categoryLabel = new Label(MessageProvider.getMessage(getClass(), "RuntimePropConditionEditDlg.categoryLabel"));
        grid.addComponent(categoryLabel, 0, 1);
        grid.setComponentAlignment(categoryLabel, Alignment.MIDDLE_RIGHT);

        categorySelect = new Select();
        categorySelect.setWidth(FIELD_WIDTH);
        categorySelect.setImmediate(true);
        categorySelect.setNullSelectionAllowed(false);
        categorySelect.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                fillAttributeSelect((Category) valueChangeEvent.getProperty().getValue());
            }
        });

        grid.addComponent(categorySelect, 1, 1);

        Label attributeLabel = new Label(MessageProvider.getMessage(getClass(), "RuntimePropConditionEditDlg.attributeLabel"));
        grid.addComponent(attributeLabel, 0, 2);
        grid.setComponentAlignment(attributeLabel, Alignment.MIDDLE_RIGHT);

        attributeSelect = new Select();
        attributeSelect.setWidth(FIELD_WIDTH);
        attributeSelect.setNullSelectionAllowed(false);
        grid.addComponent(attributeSelect, 1, 2);

        attributeSelect.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                fillOperationSelect(RuntimePropertiesHelper.getAttributeClass((CategoryAttribute) valueChangeEvent.getProperty().getValue()));
            }
        });

        Label operationLabel = new Label(MessageProvider.getMessage(getClass(), "RuntimePropConditionEditDlg.operationLabel"));
        grid.addComponent(operationLabel, 0, 3);
        grid.setComponentAlignment(operationLabel, Alignment.MIDDLE_RIGHT);

        operationSelect = new Select();
        operationSelect.setWidth(FIELD_WIDTH);
        operationSelect.setNullSelectionAllowed(false);
        grid.addComponent(operationSelect, 1, 3);

        layout.addComponent(grid);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        btnLayout.setMargin(true, false, false, false);

        Button btnOk = WebComponentsHelper.createButton("icons/ok.png");
        btnOk.setCaption(MessageProvider.getMessage(messagesPack, "actions.Ok"));
        btnOk.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (commit())
                    close();
            }
        });
        btnLayout.addComponent(btnOk);

        Button btnCancel = WebComponentsHelper.createButton("icons/cancel.png");
        btnCancel.setCaption(MessageProvider.getMessage(messagesPack, "actions.Cancel"));
        btnCancel.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        btnLayout.addComponent(btnCancel);

        layout.addComponent(btnLayout);


        fillCategorySelect();
    }

    private boolean commit() {

        if (conditionName != null) {
            condition.setLocCaption((String) conditionName.getValue());
        }

        String alias = condition.getEntityAlias();
        condition.setJoin("join " + alias + ".category.categoryAttrs ca, sys$CategoryAttributeValue cav ");

        String paramName;
        String categoryAttrParamName = condition.createParamName();
        String operation = ((PropertyCondition.Op) operationSelect.getValue()).getText();
        PropertyCondition.Op op = (PropertyCondition.Op) operationSelect.getValue();
        CategoryAttribute attribute = (CategoryAttribute) attributeSelect.getValue();
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
            if (PropertyCondition.Op.IN.equals(op))
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
        condition.setInExpr(PropertyCondition.Op.IN.equals(op));
        condition.setOperator((PropertyCondition.Op) operationSelect.getValue());
        Param param;

        if (SetValueEntity.class.isAssignableFrom(javaClass)) {
            condition.setJavaClass(String.class);
            param = new Param(paramName, javaClass, null, null, condition.getDatasource(),
                    condition.isInExpr(), attribute.getId());
        } else {
            condition.setJavaClass(javaClass);
            param = new Param(paramName, javaClass, null, null, condition.getDatasource(),
                    condition.isInExpr());
        }

        condition.setParam(param);


        Param categoryAttrParam = new Param(categoryAttrParamName, UUID.class, null, null, condition.getDatasource(), false);
        categoryAttrParam.setValue(((CategoryAttribute) attributeSelect.getValue()).getId());
        condition.setCategoryAttributeParam(categoryAttrParam);
        condition.setCategoryId(((Category) categorySelect.getValue()).getId());
        return true;
    }

    private void fillCategorySelect() {
        categorySelect.removeAllItems();
        LoadContext context = new LoadContext(Category.class);
        LoadContext.Query query = context.setQueryString("select c from sys$Category c where c.entityType=:entityType");
        query.addParameter("entityType", condition.getDatasource().getMetaClass().getName());
        context.setView("_minimal");
        List<Category> categories = dataService.loadList(context);
        UUID catId = condition.getCategoryId();
        for (Category category : categories) {
            categorySelect.addItem(category);
            categorySelect.setItemCaption(category, category.getInstanceName());
            if (category.getId().equals(catId)) {
                categorySelect.setValue(category);
            }
        }
    }

    private void fillAttributeSelect(Category category) {
        attributeSelect.removeAllItems();
        LoadContext context = new LoadContext(CategoryAttribute.class);
        LoadContext.Query query = context.setQueryString("select ca from sys$CategoryAttribute ca where ca.category.id = :id ");
        query.addParameter("id", category.getId());
        context.setView("_local");
        List<CategoryAttribute> attributes = dataService.loadList(context);
        UUID attrId = null;
        if (condition.getCategoryAttributeParam() != null) {
            Param p = condition.getCategoryAttributeParam();
            attrId = (UUID) p.getValue();
        }
        for (CategoryAttribute attribute : attributes) {
            attributeSelect.addItem(attribute);
            attributeSelect.setItemCaption(attribute, attribute.getInstanceName());
            if (attribute.getId().equals(attrId)) {
                attributeSelect.setValue(attribute);
            }
        }
    }

    private void fillOperationSelect(Class clazz) {
        EnumSet<PropertyCondition.Op> operations = PropertyCondition.Op.availableOps(clazz);
        operationSelect.removeAllItems();
        for (PropertyCondition.Op operation : operations) {
            operationSelect.addItem(operation);
            operationSelect.setItemCaption(operation, MessageProvider.getMessage(operation));
        }
        PropertyCondition.Op operator = condition.getOperator();
        if (operator != null) {
            operationSelect.setValue(operator);
        }
    }

}

/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 20.10.2009 16:57:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.datatypes.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;

public class CustomConditionEditDlg extends Window {

    public enum ParamType {
        STRING,
        DATE,
        DATETIME,
        DOUBLE,
        BIGDECIMAL,
        INTEGER,
        LONG,
        BOOLEAN,
        UUID,
        ENUM,
        ENTITY,
        UNARY
    }

    private CustomCondition condition;
    private Label entityLab;
    private Select entitySelect;
    private TextField nameText;
    private TextField whereText;
    private TextField joinText;
    private AbstractSelect typeSelect;
    private CheckBox typeCheckBox;
    private TextField entityParamWhereText;
    private TextField entityParamViewText;
    private Label entityParamWhereLab;
    private Label entityParamViewLab;

    private static final String FIELD_WIDTH = "250px";
    private String messagesPack;

    private static volatile Collection<MetaClass> metaClasses;
    private static volatile Collection<Class> enums;

    public CustomConditionEditDlg(final CustomCondition condition) {
        super(condition.getLocCaption());
        setWidth("470px");

        this.condition = condition;
        this.messagesPack = AppConfig.getInstance().getMessagesPack();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

        Label eaLab = new Label(MessageProvider.formatMessage(getClass(),
                "CustomConditionEditDlg.hintLabel", condition.getEntityAlias()));
        eaLab.setContentMode(Label.CONTENT_XHTML);
        layout.addComponent(eaLab);

        GridLayout grid = new GridLayout();
        grid.setColumns(2);
        grid.setSpacing(true);
        grid.setMargin(true, false, true, false);

        int i = 0;
        // allow to change caption if it isn't set in descriptor
        if (StringUtils.isBlank(condition.getCaption())) {
            grid.setRows(7);

            Label nameLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.nameLabel"));
            grid.addComponent(nameLab, 0, i);
            grid.setComponentAlignment(nameLab, Alignment.MIDDLE_RIGHT);

            nameText = new TextField();
            nameText.setWidth(FIELD_WIDTH);
            nameText.setNullRepresentation("");
            nameText.setValue(condition.getLocCaption());
            grid.addComponent(nameText, 1, i++);

        } else {
            grid.setRows(6);
        }

        Label joinLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.joinLabel"));
        grid.addComponent(joinLab, 0, i);
        grid.setComponentAlignment(joinLab, Alignment.MIDDLE_RIGHT);

        joinText = new TextField();
        joinText.setWidth(FIELD_WIDTH);
        joinText.setNullRepresentation("");
        joinText.setValue(condition.getJoin());
        grid.addComponent(joinText, 1, i++);

        Label whereLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.whereLabel"));
        grid.addComponent(whereLab, 0, i);
        grid.setComponentAlignment(whereLab, Alignment.MIDDLE_RIGHT);

        whereText = new TextField();
        whereText.setWidth(FIELD_WIDTH);
        whereText.setRows(4);
        whereText.setNullRepresentation("");
        String where = replaceParamWithQuestionMark(condition.getWhere());
        whereText.setValue(where);
        grid.addComponent(whereText, 1, i++);

        Label typeLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.paramTypeLabel"));
        grid.addComponent(typeLab, 0, i);
        grid.setComponentAlignment(typeLab, Alignment.MIDDLE_RIGHT);

        HorizontalLayout typeLayout = new HorizontalLayout();

        typeSelect = new Select();
        typeSelect.setImmediate(true);
        typeSelect.setNullSelectionAllowed(false);
        fillTypeSelect(typeSelect, condition.getParam());
        typeSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                boolean disableTypeCheckBox = ParamType.UNARY.equals(typeSelect.getValue()) ||
                        ParamType.BOOLEAN.equals(typeSelect.getValue());
                typeCheckBox.setEnabled(!disableTypeCheckBox);
                if (disableTypeCheckBox)
                    typeCheckBox.setValue(false);

                boolean isEntity = ParamType.ENTITY.equals(typeSelect.getValue());
                boolean isEnum = ParamType.ENUM.equals(typeSelect.getValue());
                entityLab.setEnabled(isEntity || isEnum);
                entitySelect.setEnabled(isEntity || isEnum);
                entityParamWhereLab.setEnabled(isEntity);
                entityParamWhereText.setEnabled(isEntity);
                entityParamViewLab.setEnabled(isEntity);
                entityParamViewText.setEnabled(isEntity);

                fillEntitySelect(entitySelect, condition.getParam());
            }
        });
        typeLayout.addComponent(typeSelect);

        typeCheckBox = new CheckBox(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.typeCheckBox"));
        typeCheckBox.setValue(condition.isInExpr());
        typeLayout.addComponent(typeCheckBox);

        grid.addComponent(typeLayout, 1, i++);

        entityLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.entityLabel"));
        entityLab.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        grid.addComponent(entityLab, 0, i);
        grid.setComponentAlignment(entityLab, Alignment.MIDDLE_RIGHT);

        entitySelect = new Select();
        entitySelect.setImmediate(true);
        entitySelect.setWidth(FIELD_WIDTH);
        entitySelect.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()) || ParamType.ENUM.equals(typeSelect.getValue()));
        entitySelect.setFilteringMode(Select.FILTERINGMODE_CONTAINS);
        fillEntitySelect(entitySelect, condition.getParam());
        grid.addComponent(entitySelect, 1, i++);
        grid.setComponentAlignment(entitySelect, Alignment.MIDDLE_RIGHT);

        entityParamWhereLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.entityParamWhereLab"));
        entityParamWhereLab.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        grid.addComponent(entityParamWhereLab, 0, i);
        grid.setComponentAlignment(entityParamWhereLab, Alignment.MIDDLE_RIGHT);

        entityParamWhereText = new TextField();
        entityParamWhereText.setWidth(FIELD_WIDTH);
        entityParamWhereText.setRows(3);
        entityParamWhereText.setNullRepresentation("");
        entityParamWhereText.setValue(condition.getEntityParamWhere());
        entityParamWhereText.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        grid.addComponent(entityParamWhereText, 1, i++);

        entityParamViewLab = new Label(MessageProvider.getMessage(getClass(), "CustomConditionEditDlg.entityParamViewLab"));
        entityParamViewLab.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        grid.addComponent(entityParamViewLab, 0, i);
        grid.setComponentAlignment(entityParamViewLab, Alignment.MIDDLE_RIGHT);

        entityParamViewText = new TextField();
        entityParamViewText.setWidth(FIELD_WIDTH);
        entityParamViewText.setNullRepresentation("");
        entityParamViewText.setValue(condition.getEntityParamView());
        entityParamViewText.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        grid.addComponent(entityParamViewText, 1, i++);

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
    }

    private boolean commit() {
        ParamType type = (ParamType) typeSelect.getValue();
        if (ParamType.ENTITY.equals(type) && entitySelect.getValue() == null) {
            showNotification("Select entity", Window.Notification.TYPE_HUMANIZED_MESSAGE);
            return false;
        }

        if (nameText != null) {
            condition.setLocCaption((String) nameText.getValue());
        }

        condition.setJoin((String) joinText.getValue());

        String paramName = null;
        String where = (String) whereText.getValue();
        if (where != null) {
            paramName = condition.createParamName();
            where = where.replace("?", ":" + paramName);
        }
        condition.setWhere(where);

        condition.setUnary(ParamType.UNARY.equals(type));

        condition.setInExpr(BooleanUtils.isTrue((Boolean) typeCheckBox.getValue()));

        if (paramName != null || ParamType.UNARY.equals(type)) {
            Class javaClass = getParamJavaClass(type);
            condition.setJavaClass(javaClass);

            String entityParamWhere = (String) entityParamWhereText.getValue();
            condition.setEntityParamWhere(entityParamWhere);

            String entityParamView = (String) entityParamViewText.getValue();
            condition.setEntityParamView(entityParamView);

            Param param = new Param(paramName, javaClass, entityParamWhere, entityParamView, condition.getDatasource(),
                    condition.isInExpr());
            condition.setParam(param);
        }

        return true;
    }

    private Class getParamJavaClass(ParamType type) {
        switch (type) {
            case STRING:
                return String.class;
            case DATE:
                return java.sql.Date.class;
            case DATETIME:
                return Date.class;
            case DOUBLE:
                return Double.class;
            case BIGDECIMAL:
                return BigDecimal.class;
            case INTEGER:
                return Integer.class;
            case LONG:
                return Long.class;
            case BOOLEAN:
                return Boolean.class;
            case UUID:
                return UUID.class;
            case ENTITY:
                MetaClass entity = (MetaClass) entitySelect.getValue();
                return entity.getJavaClass();
            case ENUM:
                Class enumClass = (Class) entitySelect.getValue();
                return enumClass;
            case UNARY:
                return null;
        }
        return null;
    }

    private String replaceParamWithQuestionMark(String where) {
        String res = where.trim();
        if (!StringUtils.isBlank(res)) {
            Matcher matcher = QueryParserRegex.PARAM_PATTERN.matcher(res);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                if (!matcher.group().startsWith(":session$"))
                    matcher.appendReplacement(sb, "?");
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
        return res;
    }

    private Collection<MetaClass> getMetaClasses() {
        if (metaClasses == null) {
            synchronized (CustomConditionEditDlg.class) {
                metaClasses = MetadataHelper.getAllMetaClasses();
            }
        }
        return metaClasses;
    }

    private Collection<Class> getEnums() {
        if (enums == null) {
            synchronized (CustomConditionEditDlg.class) {
                enums = MetadataHelper.getAllEnums();
            }
        }
        return enums;
    }

    private void fillEntitySelect(AbstractSelect select, Param param) {
        if (!select.isEnabled())
            return;

        select.removeAllItems();

        Map<String, Object> items = new TreeMap<String, Object>();

        if (ParamType.ENTITY.equals(typeSelect.getValue())) {
            for (MetaClass metaClass : getMetaClasses()) {
                if(metaClass.getJavaClass().getAnnotation(javax.persistence.Entity.class) != null){
                    items.put(metaClass.getName() + " (" + MessageUtils.getEntityCaption(metaClass) + ")", metaClass);
                }
            }
            for (Map.Entry<String, Object> entry : items.entrySet()) {
                select.addItem(entry.getValue());
                select.setItemCaption(entry.getValue(), entry.getKey());
            }
            if (param != null && Param.Type.ENTITY.equals(param.getType())) {
                Class javaClass = param.getJavaClass();
                MetaClass metaClass = MetadataProvider.getSession().getClass(javaClass);
                select.setValue(metaClass);
            }

        } else if (ParamType.ENUM.equals(typeSelect.getValue())) {
            for (Class enumClass : getEnums()) {
                items.put(enumClass.getSimpleName() + " (" + MessageProvider.getMessage(enumClass, enumClass.getSimpleName()) + ")", enumClass);
            }
            for (Map.Entry<String, Object> entry : items.entrySet()) {
                select.addItem(entry.getValue());
                select.setItemCaption(entry.getValue(), entry.getKey());
            }
            if (param != null && Param.Type.ENUM.equals(param.getType())) {
                Class javaClass = param.getJavaClass();
                select.setValue(javaClass);
            }
        }
    }

    private void fillTypeSelect(AbstractSelect select, Param param) {
        for (ParamType type : ParamType.values()) {
            select.addItem(type);
            select.setItemCaption(type, MessageProvider.getMessage(type));
        }
        if (param == null) {
            select.setValue(ParamType.STRING);
        } else {
            switch (param.getType()) {
                case ENTITY:
                    select.setValue(ParamType.ENTITY);
                    break;
                case ENUM:
                    select.setValue(ParamType.ENUM);
                    break;
                case DATATYPE:
                    if (String.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.STRING);
                    else if (java.sql.Date.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.DATE);
                    else if (Date.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.DATETIME);
                    else if (Boolean.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.BOOLEAN);
                    else if (BigDecimal.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.BIGDECIMAL);
                    else if (Double.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.DOUBLE);
                    else if (Integer.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.INTEGER);
                    else if (Long.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.LONG);
                    else if (UUID.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.UUID);
                    else
                        throw new UnsupportedOperationException("Unsupported param class: " + param.getJavaClass());
                    break;
                case UNARY:
                    select.setValue(ParamType.UNARY);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported param type: " + param.getType());
            }
        }
    }
}

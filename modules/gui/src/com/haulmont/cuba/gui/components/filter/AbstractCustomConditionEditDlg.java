/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.autocomplete.Suggester;
import com.haulmont.cuba.gui.autocomplete.Suggestion;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @author devyatkin
 * @version $Id$
 */
public abstract class AbstractCustomConditionEditDlg<T> {

    protected static final String WHERE = " where ";
    protected static final String MESSAGES_PACK = "com.haulmont.cuba.gui.components.filter";
    protected static final String FIELD_WIDTH = "250px";
    protected static final int COMPONENT_WIDTH = 250;
    protected String messagesPack;

    protected Label entityLab;
    protected Label nameLab;
    protected Label joinLab;
    protected Label whereLab;
    protected Label typeLab;
    protected Label entityParamWhereLab;
    protected Label entityParamViewLab;
    protected LookupField entitySelect;
    protected TextField nameText;
    protected AutoCompleteTextField whereText;
    protected AutoCompleteTextField joinText;
    protected LookupField typeSelect;
    protected CheckBox typeCheckBox;
    protected TextArea entityParamWhereText;
    protected TextField entityParamViewText;
    protected Button btnOk;
    protected Button btnCancel;

    protected String entityAlias;

    protected ParamFactory paramFactory = getParamFactory();
    protected ComponentsFactory factory = AppConfig.getFactory();
    protected AbstractCustomCondition condition;

    protected Messages messages;

    public AbstractCustomConditionEditDlg(final AbstractCustomCondition condition) {
        this.condition = condition;
        this.messagesPack = AppConfig.getMessagesPack();
        this.messages = AppBeans.get(Messages.class);

        entityAlias = condition.getEntityAlias();

        nameLab = factory.createComponent(Label.NAME);
        nameLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.nameLabel"));

        nameText = factory.createComponent(TextField.NAME);
        nameText.setWidth(FIELD_WIDTH);
        nameText.setValue(condition.getLocCaption());
        nameText.requestFocus();

        joinLab = factory.createComponent(Label.NAME);
        joinLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.joinLabel"));

        joinText = factory.createComponent(AutoCompleteTextField.NAME);
        joinText.setWidth(FIELD_WIDTH);
        joinText.setValue(condition.getJoin());

        joinText.setSuggester(new Suggester() {
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(joinText, text, cursorPosition);
            }
        });

        whereLab = factory.createComponent(Label.NAME);
        whereLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.whereLabel"));

        whereText = factory.createComponent(AutoCompleteTextField.NAME);
        whereText.setWidth(FIELD_WIDTH);
        whereText.setHeight("200px");
        String where = replaceParamWithQuestionMark(condition.getWhere());
        whereText.setValue(where);

        whereText.setSuggester(
                new Suggester() {
                    public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                        return requestHint(whereText, text, cursorPosition);
                    }
                }
        );

        typeLab = factory.createComponent(Label.NAME);
        typeLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.paramTypeLabel"));

        typeSelect = factory.createComponent(LookupField.NAME);
        fillTypeSelect(condition.getParam());

        typeSelect.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
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
                fillEntitySelect(condition.getParam());
            }
        });

        typeCheckBox = factory.createComponent(CheckBox.NAME);
        typeCheckBox.setCaption(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.typeCheckBox"));
        typeCheckBox.setValue(condition.isInExpr());

        boolean entitySelectEnabled = ParamType.ENTITY.equals(typeSelect.getValue())
                || ParamType.ENUM.equals(typeSelect.getValue());

        entityLab = factory.createComponent(Label.NAME);
        entityLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.entityLabel"));
        entityLab.setEnabled(entitySelectEnabled);

        entitySelect = factory.createComponent(LookupField.NAME);

        entitySelect.setWidth(FIELD_WIDTH);
        entitySelect.setEnabled(entitySelectEnabled);
        fillEntitySelect(condition.getParam());

        entityParamWhereLab = factory.createComponent(Label.NAME);
        entityParamWhereLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.entityParamWhereLab"));
        entityParamWhereLab.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));

        entityParamWhereText = factory.createComponent(TextArea.NAME);
        entityParamWhereText.setWidth(FIELD_WIDTH);
        entityParamWhereText.setRows(4);
        entityParamWhereText.setValue(condition.getEntityParamWhere());
        entityParamWhereText.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));

        entityParamViewLab = factory.createComponent(Label.NAME);
        entityParamViewLab.setValue(messages.getMessage(MESSAGES_PACK, "CustomConditionEditDlg.entityParamViewLab"));
        entityParamViewLab.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));

        entityParamViewText = factory.createComponent(TextField.NAME);
        entityParamViewText.setWidth(FIELD_WIDTH);
        entityParamViewText.setValue(condition.getEntityParamView());
        entityParamViewText.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));

        btnOk = factory.createComponent(Button.NAME);
        btnOk.setIcon("icons/ok.png");
        btnOk.setCaption(messages.getMessage(messagesPack, "actions.Ok"));
        btnOk.setAction(new AbstractAction("Ok") {
            @Override
            public void actionPerform(Component component) {
                if (commit())
                    closeDlg();
            }
        });

        btnCancel = factory.createComponent(Button.NAME);
        btnCancel.setCaption(messages.getMessage(messagesPack, "actions.Cancel"));
        btnCancel.setIcon("icons/cancel.png");
        btnCancel.setAction(new AbstractAction("Cancel") {
            @Override
            public void actionPerform(Component component) {
                closeDlg();
            }
        });
    }

    private List<Suggestion> requestHint(AutoCompleteTextField sender, String text, int senderCursorPosition) {
        String joinStr = joinText.getValue();
        String whereStr = whereText.getValue();
        CollectionDatasource ds = (CollectionDatasource) condition.getDatasource();

        int queryPosition = -1;
        String queryStart = "select " + entityAlias + " from " + ds.getMetaClass().getName() + " " + entityAlias + " ";

        StringBuilder queryBuilder = new StringBuilder(queryStart);
        if (joinStr != null && !joinStr.equals("")) {
            if (sender == joinText) {
                queryPosition = queryBuilder.length() + senderCursorPosition - 1;
            }
            queryBuilder.append(joinStr);
        }
        if (whereStr != null && !whereStr.equals("")) {
            if (sender == whereText) {
                queryPosition = queryBuilder.length() + WHERE.length() + senderCursorPosition - 1;
            }
            queryBuilder.append(WHERE).append(whereStr);
        }
        String query = queryBuilder.toString();

        return JpqlSuggestionFactory.requestHint(query, queryPosition, sender.getAutoCompleteSupport(), senderCursorPosition);
    }

    protected String replaceParamWithQuestionMark(String where) {
        String res = StringUtils.trim(where);
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

    protected boolean commit() {
        ParamType type = typeSelect.getValue();
        if (ParamType.ENTITY.equals(type) && entitySelect.getValue() == null) {
            showNotification("Select entity", IFrame.NotificationType.HUMANIZED);
            return false;
        }
        String nameText = this.nameText.getValue();

        if (nameText != null) {
            condition.setLocCaption(nameText);
        }

        condition.setJoin(joinText.<String>getValue());

        String paramName = null;
        String where = whereText.getValue();
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

            String entityParamWhere = entityParamWhereText.getValue();
            condition.setEntityParamWhere(entityParamWhere);

            String entityParamView = entityParamViewText.getValue();
            condition.setEntityParamView(entityParamView);

            AbstractParam param = paramFactory.createParam(
                    paramName, javaClass, entityParamWhere, entityParamView, condition.getDatasource(),
                    condition.isInExpr(), condition.isRequired());
            condition.setParam(param);
        }

        return true;
    }

    protected Class getParamJavaClass(ParamType type) {
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
                MetaClass entity = entitySelect.getValue();
                return entity.getJavaClass();
            case ENUM:
                Class enumClass = entitySelect.getValue();
                return enumClass;
            case UNARY:
                return null;
        }
        return null;
    }


    protected void fillEntitySelect(AbstractParam param) {
        if (!entitySelect.isEnabled())
            return;

        MetadataTools metadataTools = AppBeans.get(MetadataTools.class);
        MessageTools messageTools = AppBeans.get(MessageTools.class);

        Map<String, Object> items = new TreeMap<>();
        Object selectedItem = null;
        if (ParamType.ENTITY.equals(typeSelect.getValue())) {
            for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
                if (!BooleanUtils.isTrue((Boolean) metaClass.getAnnotations().get(SystemLevel.class.getName()))) {
                    items.put(messageTools.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
                }
            }

            if (param != null && AbstractParam.Type.ENTITY.equals(param.getType())) {
                Class javaClass = param.getJavaClass();
                selectedItem = AppBeans.get(Metadata.class).getClass(javaClass);
            }
            entitySelect.setOptionsMap(items);
            entitySelect.setValue(selectedItem);

        } else if (ParamType.ENUM.equals(typeSelect.getValue())) {
            if (param != null && AbstractParam.Type.ENUM.equals(param.getType())) {
                selectedItem = param.getJavaClass();
            }

            boolean selectedItemFound = false;
            for (Class enumClass : metadataTools.getAllEnums()) {
                items.put(getEnumClassName(enumClass), enumClass);

                if (selectedItem == null || selectedItem.equals(enumClass))
                    selectedItemFound = true;
            }
            // In case of a predefined custom condition parameter value may be of type which is not contained in
            // the metamodel, hence not in MetadataHelper.getAllEnums(). So we just add it here.
            if (selectedItem != null && !selectedItemFound) {
                items.put(getEnumClassName((Class) selectedItem), selectedItem);
            }

            entitySelect.setOptionsMap(items);
            entitySelect.setValue(selectedItem);
        }
    }

    protected String getEnumClassName(Class enumClass) {
        return enumClass.getSimpleName() + " (" + messages.getMessage(enumClass, enumClass.getSimpleName()) + ")";
    }

    protected void fillTypeSelect(AbstractParam param) {
        List<ParamType> values = new LinkedList<>();
        Collections.addAll(values, ParamType.values());

        typeSelect.setOptionsList(values);
        if (param == null) {
            typeSelect.setValue(ParamType.STRING);
        } else {
            switch (param.getType()) {
                case ENTITY:
                    typeSelect.setValue(ParamType.ENTITY);
                    break;
                case ENUM:
                    typeSelect.setValue(ParamType.ENUM);
                    break;
                case DATATYPE:
                    if (String.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.STRING);
                    else if (java.sql.Date.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.DATE);
                    else if (Date.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.DATETIME);
                    else if (Boolean.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.BOOLEAN);
                    else if (BigDecimal.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.BIGDECIMAL);
                    else if (Double.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.DOUBLE);
                    else if (Integer.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.INTEGER);
                    else if (Long.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.LONG);
                    else if (UUID.class.equals(param.getJavaClass()))
                        typeSelect.setValue(ParamType.UUID);
                    else
                        throw new UnsupportedOperationException("Unsupported param class: " + param.getJavaClass());
                    break;
                case UNARY:
                    typeSelect.setValue(ParamType.UNARY);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported param type: " + param.getType());
            }
        }
    }

    public abstract T getImpl();

    protected abstract ParamFactory getParamFactory();

    protected abstract void showNotification(String msg, IFrame.NotificationType type);

    protected abstract void closeDlg();
}
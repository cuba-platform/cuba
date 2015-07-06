/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.autocomplete.AutoCompleteSupport;
import com.haulmont.cuba.gui.components.autocomplete.JpqlSuggestionFactory;
import com.haulmont.cuba.gui.components.autocomplete.Suggester;
import com.haulmont.cuba.gui.components.autocomplete.Suggestion;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.ParamType;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.ValueListener;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class CustomConditionFrame extends ConditionFrame<CustomCondition> {

    protected static final String WHERE = " where ";

    @Inject
    protected LookupField typeSelect;

    @Inject
    protected LookupField entitySelect;

    @Inject
    protected CheckBox inExprCb;

    @Inject
    protected TextField nameField;

    @Inject
    protected TextField entityParamViewField;

    @Inject
    protected SourceCodeEditor joinField;

    @Inject
    protected SourceCodeEditor whereField;

    @Inject
    protected SourceCodeEditor entityParamWhereField;

    @Inject
    protected Label paramViewLab;

    @Inject
    protected Label paramWhereLab;

    @Inject
    protected Label entityLab;

    @Inject
    protected Label nameLab;

    protected boolean initializing;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        typeSelect.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, Object prevValue, Object value) {
                boolean disableTypeCheckBox = ParamType.UNARY.equals(typeSelect.getValue()) ||
                        ParamType.BOOLEAN.equals(typeSelect.getValue());
                inExprCb.setEnabled(!disableTypeCheckBox);
                if (disableTypeCheckBox)
                    inExprCb.setValue(false);

                boolean isEntity = ParamType.ENTITY.equals(typeSelect.getValue());
                boolean isEnum = ParamType.ENUM.equals(typeSelect.getValue());
                entityLab.setEnabled(isEntity || isEnum);
                entitySelect.setEnabled(isEntity || isEnum);
                entitySelect.setRequired(entitySelect.isEnabled());
                paramWhereLab.setEnabled(isEntity);
                entityParamWhereField.setEnabled(isEntity);
                paramViewLab.setEnabled(isEntity);
                entityParamViewField.setEnabled(isEntity);
                Param param = condition.getParam();
                fillEntitySelect(param);

                //recreate default value component based on param type
                if (!initializing && defaultValueLayout.isVisible()) {
                    if ((isEntity || isEnum) && (entitySelect.getValue() == null)) {
                        defaultValueLayout.remove(defaultValueComponent);
                        param.setJavaClass(null);
                    } else {
                        Class paramJavaClass = getParamJavaClass((ParamType) value);
                        param.setJavaClass(paramJavaClass);
                        param.setDefaultValue(null);
                        createDefaultValueComponent();
                    }
                }
            }
        });

        entitySelect.addListener(new ValueListener() {
            @Override
            public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                if (initializing || !defaultValueLayout.isVisible()) {
                    return;
                }
                if (value == null) {
                    defaultValueLayout.remove(defaultValueComponent);
                    return;
                }
                Param param = condition.getParam();
                Class paramJavaClass = value instanceof Class ? (Class) value : ((MetaClass) value).getJavaClass();
                param.setJavaClass(paramJavaClass);
                param.setDefaultValue(null);
                createDefaultValueComponent();
            }
        });

        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.setLookupNullSelectionAllowed(typeSelect, false);
        filterHelper.setLookupFieldPageLength(typeSelect, 12);

        joinField.setSuggester(new Suggester() {
            @Override
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(joinField, text, cursorPosition);
            }
        });
        joinField.setHighlightActiveLine(false);
        joinField.setShowGutter(false);

        whereField.setSuggester(new Suggester() {
            @Override
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHint(whereField, text, cursorPosition);
            }
        });
        whereField.setHighlightActiveLine(false);
        whereField.setShowGutter(false);

        entityParamWhereField.setSuggester(new Suggester() {
            @Override
            public List<Suggestion> getSuggestions(AutoCompleteSupport source, String text, int cursorPosition) {
                return requestHintParamWhere(entityParamWhereField, text, cursorPosition);
            }
        });
        entityParamWhereField.setHighlightActiveLine(false);
        entityParamWhereField.setShowGutter(false);

    }

    @Override
    public void setCondition(final CustomCondition condition) {
        super.setCondition(condition);
        initializing = true;

        nameField.setValue(condition.getLocCaption());
        boolean isNameEditable = Strings.isNullOrEmpty(condition.getCaption()) || !condition.getCaption().startsWith("msg://");
        nameField.setEnabled(isNameEditable);
        nameLab.setEnabled(isNameEditable);
        joinField.setValue(condition.getJoin());
        String where = replaceParamWithQuestionMark(condition.getWhere());
        whereField.setValue(where);
        inExprCb.setValue(condition.getInExpr());
        entityParamWhereField.setValue(condition.getEntityParamWhere());
        entityParamViewField.setValue(condition.getEntityParamView());

        fillTypeSelect(condition.getParam());
        fillEntitySelect(condition.getParam());
        initializing = false;
    }

    protected void fillEntitySelect(Param param) {
        if (!entitySelect.isEnabled()) {
            entitySelect.setValue(null);
            return;
        }

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MessageTools messageTools = AppBeans.get(MessageTools.NAME);

        Map<String, Object> items = new TreeMap<>();
        Object selectedItem = null;
        if (ParamType.ENTITY.equals(typeSelect.getValue())) {
            for (MetaClass metaClass : metadataTools.getAllPersistentMetaClasses()) {
                if (!BooleanUtils.isTrue((Boolean) metaClass.getAnnotations().get(SystemLevel.class.getName()))) {
                    items.put(messageTools.getEntityCaption(metaClass) + " (" + metaClass.getName() + ")", metaClass);
                }
            }

            if (param != null && Param.Type.ENTITY.equals(param.getType())) {
                Class javaClass = param.getJavaClass();
                Metadata metadata = AppBeans.get(Metadata.NAME);
                selectedItem = metadata.getClass(javaClass);
            }
            entitySelect.setOptionsMap(items);
            entitySelect.setValue(selectedItem);

        } else if (ParamType.ENUM.equals(typeSelect.getValue())) {
            if (param != null && Param.Type.ENUM.equals(param.getType())) {
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


    protected void fillTypeSelect(Param param) {
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

    @Override
    public boolean commit() {
        if (!super.commit())
            return false;

        ParamType type = typeSelect.getValue();
        if (ParamType.ENTITY.equals(type) && entitySelect.getValue() == null) {
            showNotification("Select entity", NotificationType.HUMANIZED);
            return false;
        }

        if (nameField.isEnabled()) {
            String nameText = nameField.getValue();
            if (!Strings.isNullOrEmpty(nameText)) {
                condition.setLocCaption(nameText);
            }
        }

        condition.setJoin(joinField.<String>getValue());

        String paramName = condition.createParamName();
        String where = whereField.getValue();
        if (where != null) {
            where = where.replace("?", ":" + paramName);
        }

        condition.setWhere(where);
        condition.setUnary(ParamType.UNARY.equals(type));
        condition.setInExpr(BooleanUtils.isTrue((Boolean) inExprCb.getValue()));

        Class javaClass = getParamJavaClass(type);
        condition.setJavaClass(javaClass);

        String entityParamWhere = entityParamWhereField.getValue();
        condition.setEntityParamWhere(entityParamWhere);

        String entityParamView = entityParamViewField.getValue();
        condition.setEntityParamView(entityParamView);

        Param param = new Param(
                paramName, javaClass, entityParamWhere, entityParamView, condition.getDatasource(),
                condition.getInExpr(), condition.getRequired());

        param.setDefaultValue(condition.getParam().getDefaultValue());

        condition.setParam(param);

        return true;
    }

    @Nullable
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
                if (entity == null)
                    return null;
                return entity.getJavaClass();
            case ENUM:
                Class enumClass = entitySelect.getValue();
                if (enumClass == null)
                    return null;
                return enumClass;
            case UNARY:
                return null;
        }
        return null;
    }

    protected List<Suggestion> requestHint(SourceCodeEditor sender, String text, int senderCursorPosition) {
        String joinStr = joinField.getValue();
        String whereStr = whereField.getValue();
        CollectionDatasource ds = (CollectionDatasource) condition.getDatasource();

        // CAUTION: the magic entity name!  The length is three character to match "{E}" length in query
        String entityAlias = "a39";

        int queryPosition = -1;
        String queryStart = "select " + entityAlias + " from " + ds.getMetaClass().getName() + " " + entityAlias + " ";

        StringBuilder queryBuilder = new StringBuilder(queryStart);
        if (joinStr != null && !joinStr.equals("")) {
            if (sender == joinField) {
                queryPosition = queryBuilder.length() + senderCursorPosition - 1;
            }
            queryBuilder.append(joinStr);
        }
        if (whereStr != null && !whereStr.equals("")) {
            if (sender == whereField) {
                queryPosition = queryBuilder.length() + WHERE.length() + senderCursorPosition - 1;
            }
            queryBuilder.append(WHERE).append(whereStr);
        }
        String query = queryBuilder.toString();
        query = query.replace("{E}", entityAlias);

        return JpqlSuggestionFactory.requestHint(query, queryPosition, sender.getAutoCompleteSupport(), senderCursorPosition);
    }

    protected List<Suggestion> requestHintParamWhere(SourceCodeEditor sender, String text, int senderCursorPosition) {
        String whereStr = entityParamWhereField.getValue();
        MetaClass metaClass = entitySelect.getValue();
        if (metaClass == null) {
            return new ArrayList<>();
        }

        // CAUTION: the magic entity name!  The length is three character to match "{E}" length in query
        String entityAlias = "a39";

        int queryPosition = -1;
        String queryStart = "select " + entityAlias + " from " + metaClass.getName() + " " + entityAlias + " ";

        StringBuilder queryBuilder = new StringBuilder(queryStart);
        if (whereStr != null && !whereStr.equals("")) {
            queryPosition = queryBuilder.length() + WHERE.length() + senderCursorPosition - 1;
            queryBuilder.append(WHERE).append(whereStr);
        }
        String query = queryBuilder.toString();
        query = query.replace("{E}", entityAlias);

        return JpqlSuggestionFactory.requestHint(query, queryPosition, sender.getAutoCompleteSupport(), senderCursorPosition);
    }

    public void getJoinClauseHelp() {
        getDialogParams().setModal(false).setWidth(600);
        showMessageDialog(getMessage("CustomConditionFrame.join"), getMessage("CustomConditionFrame.joinClauseHelp"), MessageType.CONFIRMATION_HTML);
    }

    public void getWhereClauseHelp() {
        getDialogParams().setModal(false).setWidth(600);
        showMessageDialog(getMessage("CustomConditionFrame.where"), getMessage("CustomConditionFrame.whereClauseHelp"), MessageType.CONFIRMATION_HTML);
    }

    public void getParamWhereClauseHelp() {
        getDialogParams().setModal(false).setWidth(600);
        showMessageDialog(getMessage("CustomConditionFrame.entityParamWhere"), getMessage("CustomConditionFrame.paramWhereClauseHelp"), MessageType.CONFIRMATION_HTML);
    }
}

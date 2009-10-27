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

import com.vaadin.ui.*;
import com.vaadin.data.Property;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.MetaModel;
import com.haulmont.chile.core.model.MetaClass;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;

public class CustomConditionEditDlg extends Window {

    private CustomCondition condition;
    private Label entityLab;
    private AbstractSelect entitySelect;
    private TextField nameText;
    private TextField whereText;
    private TextField joinText;
    private AbstractSelect typeSelect;

    public enum ParamType {
        STRING,
        DATE,
        NUMBER,
        BOOLEAN,
        UUID,
        ENTITY,
        UNARY
    }

    private static final String FIELD_WIDTH = "250px";
    private String messagesPack;

    public CustomConditionEditDlg(CustomCondition condition) {
        super(condition.getLocCaption());
        setWidth("400px");

        this.condition = condition;
        this.messagesPack = AppConfig.getInstance().getMessagesPack();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);

        Label eaLab = new Label("Main entity alias: <b>" + condition.getEntityAlias() + "</b>. Use <b>?</b> for parameter.");
        eaLab.setContentMode(Label.CONTENT_XHTML);
        layout.addComponent(eaLab);

        GridLayout grid = new GridLayout();
        grid.setColumns(2);
        grid.setSpacing(true);
        grid.setMargin(true, false, true, false);

        int i = 0;
        // allow to change caption if it doesn't set in descriptor
        if (StringUtils.isBlank(condition.getCaption())) {
            grid.setRows(5);

            Label nameLab = new Label("Name");
            grid.addComponent(nameLab, 0, i);
            grid.setComponentAlignment(nameLab, Alignment.MIDDLE_RIGHT);

            nameText = new TextField();
            nameText.setWidth(FIELD_WIDTH);
            nameText.setNullRepresentation("");
            nameText.setValue(condition.getLocCaption());
            grid.addComponent(nameText, 1, i++);

        } else {
            grid.setRows(4);
        }

        Label joinLab = new Label("Join");
        grid.addComponent(joinLab, 0, i);
        grid.setComponentAlignment(joinLab, Alignment.MIDDLE_RIGHT);

        joinText = new TextField();
        joinText.setWidth(FIELD_WIDTH);
        joinText.setNullRepresentation("");
        joinText.setValue(condition.getJoin());
        grid.addComponent(joinText, 1, i++);

        Label whereLab = new Label("Where");
        grid.addComponent(whereLab, 0, i);
        grid.setComponentAlignment(whereLab, Alignment.MIDDLE_RIGHT);

        whereText = new TextField();
        whereText.setWidth(FIELD_WIDTH);
        whereText.setRows(2);
        whereText.setNullRepresentation("");
        String where = replaceParamWithQuestionMark(condition.getWhere());
        whereText.setValue(where);
        grid.addComponent(whereText, 1, i++);

        Label typeLab = new Label("Parameter Type");
        grid.addComponent(typeLab, 0, i);
        grid.setComponentAlignment(typeLab, Alignment.MIDDLE_RIGHT);

        typeSelect = new NativeSelect();
        typeSelect.setImmediate(true);
        typeSelect.setNullSelectionAllowed(false);
        fillTypeSelect(typeSelect, condition.getParam());
        typeSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                boolean entity = ParamType.ENTITY.equals(typeSelect.getValue());
                entityLab.setEnabled(entity);
                entitySelect.setEnabled(entity);
            }
        });
        grid.addComponent(typeSelect, 1, i++);
        grid.setComponentAlignment(typeSelect, Alignment.MIDDLE_LEFT);

        entityLab = new Label("Entity");
        entityLab.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        grid.addComponent(entityLab, 0, i);
        grid.setComponentAlignment(entityLab, Alignment.MIDDLE_RIGHT);

        entitySelect = new Select();
        entitySelect.setImmediate(true);
        entitySelect.setSizeFull();
        entitySelect.setEnabled(ParamType.ENTITY.equals(typeSelect.getValue()));
        fillEntitySelect(entitySelect, condition.getParam());
        grid.addComponent(entitySelect, 1, i++);
        grid.setComponentAlignment(entitySelect, Alignment.MIDDLE_RIGHT);

        layout.addComponent(grid);

        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setSpacing(true);
        btnLayout.setMargin(true, false, false, false);

        Button btnOk = WebComponentsHelper.createButton();
        btnOk.setCaption(MessageProvider.getMessage(messagesPack, "actions.Ok"));
        btnOk.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (commit())
                    close();
            }
        });
        btnLayout.addComponent(btnOk);

        Button btnCancel = WebComponentsHelper.createButton();
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

        if (condition instanceof NewCustomCondition) {
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

        if (paramName != null || ParamType.UNARY.equals(type)) {
            Class javaClass = getParamJavaClass(type);
            condition.setJavaClass(javaClass);

            Param param = new Param(paramName, javaClass);
            condition.setParam(param);
        }

        return true;
    }

    private Class getParamJavaClass(ParamType type) {
        switch (type) {
            case STRING:
                return String.class;
            case DATE:
                return Date.class;
            case NUMBER:
                return Double.class;
            case BOOLEAN:
                return Boolean.class;
            case UUID:
                return UUID.class;
            case ENTITY:
                MetaClass entity = (MetaClass) entitySelect.getValue();
                return entity.getJavaClass();
            case UNARY:
                return null;
        }
        return null;
    }

    private String replaceParamWithQuestionMark(String where) {
        String res = where.trim();
        if (!StringUtils.isBlank(res)) {
            Matcher matcher = QueryParserRegex.PARAM_PATTERN.matcher(res);
            res = matcher.replaceAll("?");
        }
        return res;
    }

    private void fillEntitySelect(AbstractSelect select, Param param) {
        Session session = MetadataProvider.getSession();
        for (MetaModel model : session.getModels()) {
            for (MetaClass metaClass : model.getClasses()) {
                select.addItem(metaClass);
            }
        }

        if (param != null && Param.Type.ENTITY.equals(param.getType())) {
            Class javaClass = param.getJavaClass();
            MetaClass metaClass = session.getClass(javaClass);
            select.setValue(metaClass);
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
                case DATATYPE:
                    if (String.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.STRING);
                    else if (Date.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.DATE);
                    else if (Boolean.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.BOOLEAN);
                    else if (Number.class.equals(param.getJavaClass()))
                        select.setValue(ParamType.NUMBER);
                    else
                        throw new UnsupportedOperationException("Unsupported param class: " + param.getJavaClass());
                    break;
                case UNARY:
                    select.setValue(ParamType.UNARY);
                    break;
                case ENUM:
                    // TODO
                default:
                    throw new UnsupportedOperationException("Unsupported param type: " + param.getType());
            }
        }
    }
}

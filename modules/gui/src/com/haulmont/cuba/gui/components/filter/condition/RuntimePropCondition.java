/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.condition;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.RuntimePropOperationEditor;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Element;

import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author devyatkin
 * @version $Id$
 */
@MetaClass(name = "sec$RuntimePropCondition")
@SystemLevel
public class RuntimePropCondition extends AbstractCondition {

    private Param categoryAttributeParam;
    private UUID categoryId;
    protected String join;

    public RuntimePropCondition(RuntimePropCondition condition) {
        super(condition);
        this.join = condition.getJoin();
        this.categoryId = condition.getCategoryId();
        this.categoryAttributeParam = condition.getCategoryAttributeParam();
    }

    public RuntimePropCondition(AbstractConditionDescriptor descriptor, String entityAlias) {
        super(descriptor);
        this.entityAlias = entityAlias;
        this.name = RandomStringUtils.randomAlphabetic(10);
        Messages messages = AppBeans.get(Messages.class);
        this.locCaption = messages.getMessage(RuntimePropCondition.class, "newRuntimePropCondition");
    }

    public RuntimePropCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);

        if (isBlank(caption)) {
            locCaption = element.attributeValue("locCaption");
        } else {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            locCaption = messageTools.loadString(messagesPack, caption);
        }

        entityAlias = element.attributeValue("entityAlias");
        text = element.getText();
        join = element.attributeValue("join");
        categoryId = UUID.fromString(element.attributeValue("category"));

        List<Element> paramElements = Dom4j.elements(element, "param");
        for (Element paramElement : paramElements) {
            if (BooleanUtils.toBoolean(paramElement.attributeValue("hidden", "false"), "true", "false")) {
                String paramName = paramElement.attributeValue("name");
                categoryAttributeParam = new Param(paramName, UUID.class, null, null, this.getDatasource(), false, required);
                categoryAttributeParam.parseValue(paramElement.getText());
            }
        }
    }

    @Override
    public void toXml(Element element, Param.ValueProperty valueProperty) {
        super.toXml(element, valueProperty);
        element.addAttribute("type", ConditionType.RUNTIME_PROPERTY.name());
        if (isBlank(caption)) {
            element.addAttribute("locCaption", locCaption);
        }
        element.addAttribute("category", categoryId.toString());
        element.addAttribute("entityAlias", entityAlias);
        if (!isBlank(join)) {
            element.addAttribute("join", StringEscapeUtils.escapeXml(join));
        }

        Element paramElem = element.addElement("param");
        paramElem.addAttribute("name", categoryAttributeParam.getName());
        paramElem.addAttribute("hidden", "true");
        paramElem.setText(categoryAttributeParam.formatValue(categoryAttributeParam.getValue()));
    }

    public Param getCategoryAttributeParam() {
        return categoryAttributeParam;
    }

    public void setCategoryAttributeParam(Param categoryAttributeParam) {
        this.categoryAttributeParam = categoryAttributeParam;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID id) {
        categoryId = id;
    }

    @Override
    public void setOperator(Op operator) {
        if (!ObjectUtils.equals(this.operator, operator)) {
            this.operator = operator;
            String paramName = param.getName();
            if (operator.isUnary()) {
                unary = true;
                inExpr = false;
                setParam(new Param(paramName, null, null, null, null, false, required));
            } else {
                unary = false;
                inExpr = operator.equals(Op.IN) || operator.equals(Op.NOT_IN);
                setParam(new Param(
                        paramName, javaClass, entityParamWhere, entityParamView, datasource, param.getProperty(), inExpr, required));
            }
        }
    }

    @Override
    public String getOperationCaption() {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMessage(operator);
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        operationEditor = new RuntimePropOperationEditor(this);
        return operationEditor;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getWhere() {
        return text;
    }

    public void setWhere(String where) {
        this.text = where;
    }

//    @Override
//    protected void copyFrom(AbstractCondition condition) {
//        super.copyFrom(condition);
//        if (condition instanceof RuntimePropCondition) {
//            this.join = ((RuntimePropCondition) condition).getJoin();
//            this.categoryId = ((RuntimePropCondition) condition).getCategoryId();
//            this.categoryAttributeParam = ((RuntimePropCondition) condition).getCategoryAttributeParam();
//        }
//    }

    @Override
    public AbstractCondition createCopy() {
//        RuntimePropCondition runtimePropCondition = new RuntimePropCondition();
//        runtimePropCondition.copyFrom(this);
//        return runtimePropCondition;
        return new RuntimePropCondition(this);
    }
}
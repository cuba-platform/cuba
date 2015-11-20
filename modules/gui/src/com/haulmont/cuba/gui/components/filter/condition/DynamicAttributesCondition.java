/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.condition;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.components.filter.operationedit.DynamicAttributesOperationEditor;
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
@MetaClass(name = "sec$DynamicAttributesCondition")
@SystemLevel
public class DynamicAttributesCondition extends AbstractCondition {

    protected UUID categoryId;
    protected UUID categoryAttributeId;
    protected String propertyPath;
    protected String join;

    public DynamicAttributesCondition(DynamicAttributesCondition condition) {
        super(condition);
        this.join = condition.getJoin();
        this.categoryId = condition.getCategoryId();
        this.categoryAttributeId = condition.getCategoryAttributeId();
    }

    public DynamicAttributesCondition(AbstractConditionDescriptor descriptor, String entityAlias, String propertyPath) {
        super(descriptor);
        this.entityAlias = entityAlias;
        this.name = RandomStringUtils.randomAlphabetic(10);
        Messages messages = AppBeans.get(Messages.class);
        this.locCaption = messages.getMainMessage("newDynamicAttributeCondition");
        this.propertyPath = propertyPath;
    }

    public DynamicAttributesCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, messagesPack, filterComponentName, datasource);

        propertyPath = element.attributeValue("propertyPath");

        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        locCaption = isBlank(caption)
                ? element.attributeValue("locCaption")
                : messageTools.loadString(messagesPack, caption);

        entityAlias = element.attributeValue("entityAlias");
        text = element.getText();
        join = element.attributeValue("join");
        categoryId = UUID.fromString(element.attributeValue("category"));
        String categoryAttributeValue = element.attributeValue("categoryAttribute");
        if (!Strings.isNullOrEmpty(categoryAttributeValue)) {
            categoryAttributeId = UUID.fromString(categoryAttributeValue);
        } else {
            //for backward compatibility
            List<Element> paramElements = Dom4j.elements(element, "param");
            for (Element paramElement : paramElements) {
                if (BooleanUtils.toBoolean(paramElement.attributeValue("hidden", "false"), "true", "false")) {
                    categoryAttributeId = UUID.fromString(paramElement.getText());
                    String paramName = paramElement.attributeValue("name");
                    text = text.replace(":" + paramName, "'" + categoryAttributeId + "'");
                }
            }
        }

        resolveParam(element);
    }

    @Override
    public void toXml(Element element, Param.ValueProperty valueProperty) {
        super.toXml(element, valueProperty);
        element.addAttribute("type", ConditionType.RUNTIME_PROPERTY.name());
        if (isBlank(caption)) {
            element.addAttribute("locCaption", locCaption);
        }
        element.addAttribute("category", categoryId.toString());
        element.addAttribute("categoryAttribute", categoryAttributeId.toString());
        element.addAttribute("entityAlias", entityAlias);
        if (!isBlank(propertyPath)) {
            element.addAttribute("propertyPath", propertyPath);
        }
        if (!isBlank(join)) {
            element.addAttribute("join", StringEscapeUtils.escapeXml(join));
        }
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID id) {
        categoryId = id;
    }

    public UUID getCategoryAttributeId() {
        return categoryAttributeId;
    }

    public void setCategoryAttributeId(UUID categoryAttributeId) {
        this.categoryAttributeId = categoryAttributeId;
    }

    @Override
    public void setOperator(Op operator) {
        if (!ObjectUtils.equals(this.operator, operator)) {
            this.operator = operator;
            String paramName = param.getName();
            ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);
            if (operator.isUnary()) {
                unary = true;
                inExpr = false;
                setParam(new Param(paramName, Boolean.class, null, null, null, false, required));
            } else {
                unary = false;
                inExpr = operator.equals(Op.IN) || operator.equals(Op.NOT_IN);
                Param param = paramBuilder.createParam(this);
                setParam(param);
            }
        }
    }

    @Override
    public String getOperationCaption() {
        return operator.getLocCaption();
    }

    @Override
    public AbstractOperationEditor createOperationEditor() {
        operationEditor = new DynamicAttributesOperationEditor(this);
        return operationEditor;
    }

    @Override
    protected void updateText() {
        if (operator == Op.NOT_EMPTY) {
            if (BooleanUtils.isTrue((Boolean) param.getValue())) {
                text = text.replace(" is null ", " is not null ");
            } else if (BooleanUtils.isFalse((Boolean) param.getValue())) {
                text = text.replace(" is not null ", " is null ");
            }
        }
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public String getWhere() {
        updateText();
        return text;
    }

    public void setWhere(String where) {
        this.text = where;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    @Override
    public AbstractCondition createCopy() {
        return new DynamicAttributesCondition(this);
    }

    @Override
    public String getLocCaption() {
        if (isBlank(caption) && !isBlank(propertyPath)) {
            MessageTools messageTools = AppBeans.get(MessageTools.class);
            String propertyCaption = messageTools.getPropertyCaption(datasource.getMetaClass(), propertyPath);
            if (!isBlank(propertyCaption)) {
                return propertyCaption + "." + locCaption;
            }
        }
        return super.getLocCaption();
    }
}
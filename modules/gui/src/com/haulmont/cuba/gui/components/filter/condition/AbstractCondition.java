/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.condition;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.gui.components.filter.descriptor.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.operationedit.AbstractOperationEditor;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.*;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Class that encapsulates common filter condition behaviour.
 *
 * @author devyatkin
 * @version $Id$
 */
@MetaClass(name = "sec$AbstractCondition")
@SystemLevel
public abstract class AbstractCondition extends AbstractNotPersistentEntity {

    public interface Listener {

        void captionChanged();

        void paramChanged(Param oldParam, Param newParam);
    }

    protected String name;
    protected String paramName;
    protected String caption;
    protected String messagesPack;
    @MetaProperty
    protected String locCaption;
    protected String filterComponentName;
    protected String text;
    protected Boolean group = false;
    protected Boolean unary = false;
    protected Boolean inExpr = false;
    protected Class javaClass;
    protected Class paramClass;
    protected Param param;
    protected String entityAlias;
    protected Boolean hidden = false;
    protected Boolean required = false;
    protected String entityParamWhere;
    protected String entityParamView;
    protected Datasource datasource;
    protected Integer width = 1;
    protected Op operator;


    protected List<Listener> listeners = new ArrayList<>();
    protected AbstractOperationEditor operationEditor;

    protected AbstractCondition() {}

    protected AbstractCondition(AbstractCondition other) {
        this.name = other.name;
        this.caption = other.caption;
        this.messagesPack = other.messagesPack;
        this.locCaption = other.locCaption;
        this.filterComponentName = other.filterComponentName;
        this.group = other.group;
        this.unary = other.unary;
        this.inExpr = other.inExpr;
        this.javaClass = other.javaClass;
        this.paramClass = other.paramClass;
        this.entityAlias = other.entityAlias;
        this.hidden = other.hidden;
        this.required = other.required;
        this.entityParamWhere = other.entityParamWhere;
        this.entityParamView = other.entityParamView;
        this.datasource = other.datasource;
        this.width = other.width;
        this.param = other.param;
        this.text = other.text;
        this.operator = other.operator;
    }

    protected AbstractCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        this.messagesPack = messagesPack;
        this.filterComponentName = filterComponentName;
        name = element.attributeValue("name");
        text = StringEscapeUtils.unescapeXml(element.getText());
        if (text == null)
            text = "";

        caption = element.attributeValue("caption");
        MessageTools messageTools = AppBeans.get(MessageTools.class);
        locCaption = messageTools.loadString(messagesPack, caption);

        unary = Boolean.valueOf(element.attributeValue("unary"));
        inExpr = Boolean.valueOf(element.attributeValue("inExpr"));
        hidden = Boolean.valueOf(element.attributeValue("hidden"));
        required = Boolean.valueOf(element.attributeValue("required"));
        entityParamWhere = element.attributeValue("paramWhere");
        entityParamView = element.attributeValue("paramView");
        width = Strings.isNullOrEmpty(element.attributeValue("width")) ? 1 : Integer.parseInt(element.attributeValue("width"));
        this.datasource = datasource;

        resolveParam(element);
    }

    protected AbstractCondition(AbstractConditionDescriptor descriptor) {
        name = descriptor.getName();
        caption = descriptor.getCaption();
        locCaption = descriptor.getLocCaption();
        filterComponentName = descriptor.getFilterComponentName();
        javaClass = descriptor.getJavaClass();
        unary = javaClass == null;
        entityParamWhere = descriptor.getEntityParamWhere();
        entityParamView = descriptor.getEntityParamView();
        datasource = descriptor.getDatasource();
        messagesPack = descriptor.getMessagesPack();
        ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);
        if (Strings.isNullOrEmpty(paramName)) {
            paramName = paramBuilder.createParamName(this);
        }
        param = paramBuilder.createParam(this);
        String operatorType = descriptor.getOperatorType();
        if (operatorType != null) {
            operator = Op.valueOf(operatorType);
        }
    }

    protected void resolveParam(Element element) {
        Scripting scripting = AppBeans.get(Scripting.NAME);
        String aclass = element.attributeValue("class");
        if (!isBlank(aclass)) {
            javaClass = scripting.loadClass(aclass);
        }

        List<Element> paramElements = Dom4j.elements(element, "param");
        if (!paramElements.isEmpty()) {
            Element paramElem = paramElements.iterator().next();

            if (BooleanUtils.toBoolean(paramElem.attributeValue("hidden", "false"), "true", "false")) {
                paramElem = paramElements.iterator().next();
            }
            paramName = paramElem.attributeValue("name");

            if (!isBlank(paramElem.attributeValue("javaClass"))) {
                paramClass = scripting.loadClass(paramElem.attributeValue("javaClass"));
            }

            ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);
            if (Strings.isNullOrEmpty(paramName)) {
                paramName = paramBuilder.createParamName(this);
            }
            param = paramBuilder.createParam(this);
            param.parseValue(paramElem.getText());
            param.setDefaultValue(param.getValue());
        }

        String operatorName = element.attributeValue("operatorType", null);
        if (operatorName != null) {
            //for backward compatibility with old filters that still use EMPTY operator
            if ("EMPTY".equals(operatorName)) {
                operatorName = "NOT_EMPTY";
                if (BooleanUtils.isTrue((Boolean) param.getValue()))
                    param.setValue(false);
                param.setDefaultValue(false);
            }

            operator = Op.valueOf(operatorName);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocCaption() {
        return locCaption;
    }

    public void setLocCaption(String locCaption) {
        if (ObjectUtils.equals(this.locCaption, locCaption))
            return;

        this.locCaption = locCaption;
        for (Listener listener : listeners) {
            listener.captionChanged();
        }
    }

    public String getText() {
        updateText();
        return text;
    }

    protected void updateText() {
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        Param oldParam = this.param;
        this.param = param;

        for (AbstractCondition.Listener listener : listeners) {
            listener.paramChanged(oldParam, param);
        }
    }

    public String getParamName() {
        return paramName;
    }

    public Class getParamClass() {
        return paramClass;
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public String getFilterComponentName() {
        return filterComponentName;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public void toXml(Element element, Param.ValueProperty valueProperty) {
        String text = getText();
        if (StringUtils.isNotBlank(text))
            element.addCDATA(text);

        element.addAttribute("name", name);

        if (javaClass != null)
            element.addAttribute("class", javaClass.getName());

        if (caption != null)
            element.addAttribute("caption", caption);

        if (unary)
            element.addAttribute("unary", "true");

        if (inExpr)
            element.addAttribute("inExpr", "true");

        if (hidden)
            element.addAttribute("hidden", "true");

        if (required)
            element.addAttribute("required", "true");

        if (operator != null) {
            element.addAttribute("operatorType", operator.name());
        }

        if (param != null) {
            param.toXml(element, valueProperty);
            if (entityParamWhere != null)
                element.addAttribute("paramWhere", entityParamWhere);
            if (entityParamView != null)
                element.addAttribute("paramView", entityParamView);
        }

        if (width != null) {
            element.addAttribute("width", width.toString());
        }
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }

    public Boolean isGroup() {
        return group;
    }

    public Boolean getUnary() {
        return unary;
    }

    public void setUnary(Boolean unary) {
        this.unary = unary;
    }

    public Boolean getInExpr() {
        return inExpr;
    }

    public void setInExpr(Boolean inExpr) {
        this.inExpr = inExpr;
    }

    public String getOperationCaption() {
        return "";
    }

    public Op getOperator() {
        return operator;
    }

    public void setOperator(Op operator) {
        this.operator = operator;
    }

    public String getEntityParamView() {
        return entityParamView;
    }

    public void setEntityParamView(String entityParamView) {
        this.entityParamView = entityParamView;
    }

    public String getEntityParamWhere() {
        return entityParamWhere;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setEntityParamWhere(String entityParamWhere) {
        this.entityParamWhere = entityParamWhere;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

//    public String createParamName() {
//        return "component$" + getFilterComponentName() + "." +
//                getName().replace('.', '_') + RandomStringUtils.randomNumeric(5);
//    }

    public AbstractOperationEditor createOperationEditor() {
        return null;
    }

    public AbstractOperationEditor getOperationEditor() {
        return operationEditor;
    }

    public abstract AbstractCondition createCopy();

    public boolean canBeRequired() {
        return true;
    }

    public boolean canHasWidth() {
        return true;
    }

    public boolean canHasDefaultValue() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                '}';
    }
}
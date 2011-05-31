/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.10.2009 18:19:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Element;

import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyCondition extends Condition {

    public enum Op {
        EQUAL("=", false),
        IN("in", false),
        NOT_EQUAL("<>", false),
        GREATER(">", false),
        GREATER_OR_EQUAL(">=", false),
        LESSER("<", false),
        LESSER_OR_EQUAL("<=", false),
        CONTAINS("like", false),
        DOES_NOT_CONTAIN("not like", false),
        EMPTY("is null", true),
        NOT_EMPTY("is not null", true),
        STARTS_WITH("like", false),
        ENDS_WITH("like", false);

        private String text;
        private boolean unary;

        Op(String text, boolean unary) {
            this.text = text;
            this.unary = unary;
        }

        public String getText() {
            return text;
        }

        public boolean isUnary() {
            return unary;
        }

        public static Op fromString(String str) {
            for (Op op : values()) {
                if (op.text.equals(str))
                    return op;
            }
            throw new UnsupportedOperationException("Unsupported operation: " + str);
        }

        public static EnumSet<Op> availableOps(Class javaClass) {
            if (String.class.equals(javaClass))
                return EnumSet.of(EQUAL, IN, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, EMPTY, NOT_EMPTY, STARTS_WITH, ENDS_WITH);

            else if (Date.class.isAssignableFrom(javaClass)
                    || Number.class.isAssignableFrom(javaClass))
                return EnumSet.of(EQUAL, IN, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, EMPTY, NOT_EMPTY);

            else if (Boolean.class.equals(javaClass))
                return EnumSet.of(EQUAL, NOT_EQUAL, EMPTY, NOT_EMPTY);

            else if (UUID.class.equals(javaClass)
                    || Enum.class.isAssignableFrom(javaClass)
                    || Entity.class.isAssignableFrom(javaClass))
                return EnumSet.of(EQUAL, IN, NOT_EQUAL, EMPTY, NOT_EMPTY);

            else
                throw new UnsupportedOperationException("Unsupported java class: " + javaClass);
        }
    }

    private static Pattern PATTERN = Pattern.compile("\\s*(\\S+)\\s+((?:not\\s+)*\\S+)\\s+(\\S+)\\s*");
    private static Pattern PATTERN_NULL = Pattern.compile("\\s*(\\S+)\\s+(is\\s+(?:not\\s+)?null)\\s*");

    private Op operator;

    public PropertyCondition(Element element, String messagesPack, String filterComponentName, Datasource datasource) {
        super(element, filterComponentName, datasource);

        this.locCaption = MessageUtils.loadString(messagesPack, caption);

        String text = element.getText();
        Matcher matcher = PATTERN_NULL.matcher(text);
        if (!matcher.matches()) {
            matcher = PATTERN.matcher(text);
            if (!matcher.matches())
                throw new IllegalStateException("Unable to build condition from: " + text);
        }
        String operatorName = element.attributeValue("operatorType",null);
        if (operatorName != null)
            operator = Op.valueOf(operatorName);
        else
            operator = Op.fromString(matcher.group(2));

        String prop = matcher.group(1);
        entityAlias = prop.substring(0, prop.indexOf('.'));
    }

    public PropertyCondition(ConditionDescriptor descriptor, String entityAlias) {
        super(descriptor);
        this.entityAlias = entityAlias;
    }

    @Override
    protected Param createParam(String paramName) {
        MetaProperty metaProperty = datasource.getMetaClass().getProperty(name);
        return new Param(paramName, javaClass, entityParamWhere, entityParamView, datasource, metaProperty, inExpr);
    }

    @Override
    protected void updateText() {
        StringBuilder sb = new StringBuilder();

        sb.append(entityAlias).append(".").append(name);

        if (Param.Type.ENTITY.equals(param.getType()))
            sb.append(".id");

        sb.append(" ").append(operator.getText());

        if (!operator.isUnary()) {
            if (inExpr)
                sb.append(" (");
            else
                sb.append(" ");
            sb.append(":").append(param.getName());
            if (inExpr)
                sb.append(")");
        }

        text = sb.toString();
    }

    public String getOperatorType(){
        return operator.name();
    }

    @Override
    public void toXml(Element element) {
        super.toXml(element);
        element.addAttribute("type", ConditionType.PROPERTY.name());
        element.addAttribute("operatorType",getOperatorType());

    }

    @Override
    public OperationEditor createOperationEditor() {
        return new PropertyOperationEditor(this);
    }

    public Op getOperator() {
        return operator;
    }

    public void setOperator(Op operator) {
        if (!ObjectUtils.equals(this.operator, operator)) {
            this.operator = operator;
            String paramName = param.getName();

            if (operator.isUnary()) {
                unary = true;
                inExpr = false;
                setParam(new Param(paramName, null, null, null, null, false));
            } else {
                unary = false;
                inExpr = operator.equals(Op.IN);
                setParam(new Param(
                        paramName, javaClass, entityParamWhere, entityParamView, datasource, param.getProperty(), inExpr));
            }
        }
    }

    @Override
    public String getError() {
        String res = super.getError();
        if (res != null)
            return res;

        if (operator == null)
            return locCaption + ": " + MessageProvider.getMessage(getClass(), "PropertyCondition.operatorNotDefined");
        else
            return null;
    }

    @Override
    public String getOperationCaption() {
        return MessageProvider.getMessage(operator);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }
}

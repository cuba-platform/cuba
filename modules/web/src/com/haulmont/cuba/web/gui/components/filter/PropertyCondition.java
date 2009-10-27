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

import org.dom4j.Element;
import org.apache.commons.lang.ObjectUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.entity.Entity;

public class PropertyCondition extends Condition {

    public enum Op {
        EQUAL("=", false),
        NOT_EQUAL("<>", false),
        GREATER(">", false),
        GREATER_OR_EQUAL(">=", false),
        LESSER("<", false),
        LESSER_OR_EQUAL("<=", false),
        CONTAINS("like", false),
        DOES_NOT_CONTAIN("not like", false),
        EMPTY("is null", true),
        NOT_EMPTY("is not null", true);

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
                return EnumSet.of(EQUAL, NOT_EQUAL, CONTAINS, DOES_NOT_CONTAIN, EMPTY, NOT_EMPTY);

            else if (Date.class.isAssignableFrom(javaClass)
                    || Number.class.isAssignableFrom(javaClass))
                return EnumSet.of(EQUAL, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESSER, LESSER_OR_EQUAL, EMPTY, NOT_EMPTY);

            else if (Boolean.class.equals(javaClass)
                    || UUID.class.equals(javaClass)
                    || Entity.class.isAssignableFrom(javaClass))
                return EnumSet.of(EQUAL, NOT_EQUAL, EMPTY, NOT_EMPTY);

            else
                throw new UnsupportedOperationException("Unsupported java class: " + javaClass);
        }
    }

    private static Pattern PATTERN = Pattern.compile("\\s*(\\S+)\\s+((?:not\\s+)*\\S+)\\s+(\\S+)\\s*");
    private static Pattern PATTERN_NULL = Pattern.compile("\\s*(\\S+)\\s+(is\\s+(?:not\\s+)?null)\\s*");

    private Op operator;

    public PropertyCondition(Element element, String messagesPack, String filterComponentName) {
        super(element, filterComponentName);

        this.locCaption = MessageUtils.loadString(messagesPack, caption);

        String text = element.getText();
        Matcher matcher = PATTERN_NULL.matcher(text);
        if (!matcher.matches()) {
            matcher = PATTERN.matcher(text);
            if (!matcher.matches())
                throw new IllegalStateException("Unable to build condition from: " + text);
        }

        operator = Op.fromString(matcher.group(2));

        String prop = matcher.group(1);
        entityAlias = prop.substring(0, prop.indexOf('.'));
    }

    public PropertyCondition(ConditionDescriptor descriptor, String entityAlias) {
        super(descriptor);
        this.entityAlias = entityAlias;
    }

    @Override
    protected void updateText() {
        StringBuilder sb = new StringBuilder();

        sb.append(entityAlias).append(".").append(name);

        if (Param.Type.ENTITY.equals(param.getType()))
            sb.append(".id");

        sb.append(" ").append(operator.getText());

        if (!operator.isUnary())
            sb.append(" :").append(param.getName());

        text = sb.toString();
    }

    @Override
    public void toXml(Element element) {
        super.toXml(element);
        element.addAttribute("type", ConditionType.PROPERTY.name());
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
                setParam(new Param(paramName, null));
            } else {
                unary = false;
                setParam(new Param(paramName, javaClass));
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
        return operator.getText();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }
}

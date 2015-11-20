/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.core.global.TemplateHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.*;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class FilterParser {
    protected final Condition root;

    public FilterParser(Element element) {
        if (element.elements().isEmpty())
            throw new IllegalArgumentException("filter element is empty");
        Element rootElem = (Element) element.elements().get(0);
        root = createCondition(rootElem);
        parse(rootElem, root.getConditions());
    }

    public FilterParser(Condition root) {
        this.root = root;
    }

    public Condition getRoot() {
        return root;
    }

    protected Condition createCondition(Element conditionElement) {
        Condition condition;

        String conditionName = conditionElement.attributeValue("name");
        if ("c".equals(conditionElement.getName())) {
            condition = new Clause(conditionName, conditionElement.getText(),
                    getJoinValue(conditionElement),
                    conditionElement.attributeValue("operatorType"),
                    conditionElement.attributeValue("type"));
            // support unary conditions without parameters in text (e.g. "is null")
            for (Element paramElem : Dom4j.elements(conditionElement, "param")) {
                Set<ParameterInfo> params = ParametersHelper.parseQuery(":" + paramElem.attributeValue("name"));
                Attribute javaClass = paramElem.attribute("javaClass");
                if (javaClass != null) {
                    for (ParameterInfo param : params) {
                        try {
                            param.setJavaClass(Class.forName(javaClass.getValue()));
                            param.setConditionName(conditionName);
                            param.setValue(paramElem.getText());
                        } catch (ClassNotFoundException e) {
                            //do not fail
                        }

                        if (condition.getParameters().contains(param)) {
                            for (ParameterInfo parameterInfo : condition.getParameters()) {
                                if (parameterInfo.equals(param)) {
                                    parameterInfo.setJavaClass(param.getJavaClass());
                                    parameterInfo.setConditionName(param.getConditionName());
                                    parameterInfo.setValue(param.getValue());
                                }
                            }
                        }
                    }
                }

                condition.getParameters().addAll(params);
            }
        } else {
            condition = new LogicalCondition(conditionName, LogicalOp.fromString(conditionElement.getName()));
        }

        return condition;
    }

    protected String getJoinValue(Element conditionElement) {
        Element joinElement = conditionElement.element("join");
        String join;
        if (joinElement != null) {
            join = joinElement.getText();
        } else {
            //for backward compatibility
            join = conditionElement.attributeValue("join");
        }
        return join;
    }

    protected void parse(Element parentElem, List<Condition> conditions) {
        for (Element element : Dom4j.elements(parentElem)) {
            if ("param".equals(element.getName()) || "join".equals(element.getName()))
                continue;

            Condition condition = createCondition(element);
            conditions.add(condition);
            parse(element, condition.getConditions());
        }
    }
}

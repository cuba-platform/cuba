/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.core.global.TemplateHelper;
import com.haulmont.cuba.gui.xml.ParameterInfo;
import com.haulmont.cuba.gui.xml.ParametersHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class QueryFilter {

    private final Condition root;
    private final String targetEntity;

    public QueryFilter(Condition condition, String targetEntity) {
        this.targetEntity = targetEntity;
        root = condition;
    }

    public QueryFilter(Element element, String targetEntity) {
        this.targetEntity = targetEntity;
        if (element.elements().isEmpty())
            throw new IllegalArgumentException("filter element is empty");
        Element rootElem = (Element) element.elements().get(0);
        root = createCondition(rootElem);
        parse(rootElem, root.getConditions());
    }

    public QueryFilter(QueryFilter src1, QueryFilter src2) {
        if (src1 == null || src2 == null)
            throw new IllegalArgumentException("Source query filter is null");

        if (!src1.targetEntity.equals(src2.targetEntity))
            throw new IllegalArgumentException("Target entities do not match");

        targetEntity = src1.targetEntity;

        root = new LogicalCondition(LogicalOp.AND);
        root.getConditions().add(src1.root);
        root.getConditions().add(src2.root);
    }

    public Condition getRoot() {
        return root;
    }

    private Condition createCondition(Element conditionElement) {
        Condition condition;

        if ("c".equals(conditionElement.getName())) {
            condition = new Clause(conditionElement.getText(), conditionElement.attributeValue("join"));
            // support unary conditions without parameters in text (e.g. "is null")
            for (Element paramElem : Dom4j.elements(conditionElement, "param")) {
                Set<ParameterInfo> params = ParametersHelper.parseQuery(":" + paramElem.attributeValue("name"));
                Attribute javaClass = paramElem.attribute("javaClass");
                if (javaClass != null) {
                    for (ParameterInfo param : params) {
                        try {
                            param.setJavaClass(Class.forName(javaClass.getValue()));
                            param.setConditionName(conditionElement.attributeValue("name"));
                        } catch (ClassNotFoundException e) {
                            //do not fail
                        }

                        if (condition.getParameters().contains(param)) {
                            for (ParameterInfo parameterInfo : condition.getParameters()) {
                                if (parameterInfo.equals(param)) {
                                    parameterInfo.setJavaClass(param.getJavaClass());
                                    parameterInfo.setConditionName(param.getConditionName());
                                }
                            }
                        }
                    }
                }

                condition.getParameters().addAll(params);
            }
        } else {
            condition = new LogicalCondition(LogicalOp.fromString(conditionElement.getName()));
        }

        return condition;
    }

    private void parse(Element parentElem, List<Condition> conditions) {
        for (Element element : Dom4j.elements(parentElem)) {
            if ("param".equals(element.getName()))
                continue;

            Condition condition = createCondition(element);
            conditions.add(condition);
            parse(element, condition.getConditions());
        }
    }

    public Collection<ParameterInfo> getParameters() {
        return root.getParameters();
    }

    public String processQuery(String query, Map<String, Object> paramValues) {
        Set<String> params = new HashSet<>();
        for (Map.Entry<String, Object> entry : paramValues.entrySet()) {
            if (paramValueIsOk(entry.getValue()))
                params.add(entry.getKey());
        }

        query = TemplateHelper.processTemplate(query, paramValues);
        QueryTransformer transformer = QueryTransformerFactory.createTransformer(query, targetEntity);

        if (isActual(root, params)) {
            Condition refined = refine(root, params);
            String where = refined.getContent();

            if (!StringUtils.isBlank(where)) {
                Set<String> joins = refined.getJoins();
                if (!joins.isEmpty()) {
                    String joinsStr = new StrBuilder().appendWithSeparators(joins, " ").toString();
                    transformer.addJoinAndWhere(joinsStr, where);
                } else {
                    transformer.addWhere(where);
                }
            }
        }
        return transformer.getResult();
    }

    private boolean paramValueIsOk(Object value) {
        if (value instanceof String)
            return !StringUtils.isBlank((String) value);
        else return value != null;
    }

    private Condition refine(Condition src, Set<String> params) {
        Condition copy = src.copy();
        List<Condition> list = new ArrayList<Condition>();
        for (Condition condition : src.getConditions()) {
            if (isActual(condition, params)) {
                list.add(refine(condition, params));
            }
        }
        copy.setConditions(list.isEmpty() ? Collections.EMPTY_LIST : list);
        return copy;
    }

    private boolean isActual(Condition condition, Set<String> params) {
        Set<ParameterInfo> declaredParams = condition.getParameters();

        if (declaredParams.isEmpty())
            return true;

        // Return true only if declared params have values and there is at least one non-session parameter among them.
        // This is necessary to exclude generic filter conditions that contain only session parameters. Otherwise
        // there is no way to handle exclusion. Unfortunately this imposes the restriction on custom filters design:
        // condition with session-only parameters must be avoided, they must be coded as part of main query body or as
        // part of another condition.
        boolean found = false;
        for (ParameterInfo paramInfo : declaredParams) {
            if (params.contains(paramInfo.getName())) {
                found = found || !paramInfo.getType().equals(ParameterInfo.Type.SESSION);
            }
        }
        return found;
    }
}

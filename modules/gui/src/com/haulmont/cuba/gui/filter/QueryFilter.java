/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2009 12:09:22
 *
 * $Id$
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.util.*;

public class QueryFilter {

    private final Condition root;
    private final String targetEntity;

    public QueryFilter(Element element, String targetEntity) {
        this.targetEntity = targetEntity;
        if (element.elements().isEmpty())
            throw new IllegalArgumentException("filter element is empty");
        Element rootElem = (Element) element.elements().get(0);
        root = createCondition(rootElem);
        parse(rootElem, root.getConditions());
    }

    private Condition createCondition(Element element) {
        Condition condition;

        if ("c".equals(element.getName())) {
            condition = new Clause(element.getText(), element.attributeValue("join"));
        } else {
            condition = new LogicalCondition(LogicalOp.fromString(element.getName()));
        }

        return condition;
    }

    private void parse(Element parentElem, List<Condition> conditions) {
        for (Element element : Dom4j.elements(parentElem)) {
            Condition condition = createCondition(element);
            conditions.add(condition);
            parse(element, condition.getConditions());
        }
    }

    public Collection<String> getParameters() {
        return root.getParameters();
    }

    public String processQuery(String query, Map<String, Object> paramValues) {
        Set<String> params = new HashSet<String>();
        for (Map.Entry<String, Object> entry : paramValues.entrySet()) {
            if (paramValueIsOk(entry.getValue()))
                params.add(entry.getKey());
        }

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(query, targetEntity);

        if (isActual(root, paramValues.keySet())) {
            Condition refined = refine(root, params);
            String where = refined.getContent();

            if (!StringUtils.isBlank(where)) {
                Set<String> joins = refined.getJoins();
                if (!joins.isEmpty()) {
                    String joinsStr = new StrBuilder().appendAll(joins).toString();
                    transformer.addJoinAsIs(joinsStr);
                }
                transformer.addWhereAsIs(where);
            }
        }
        return transformer.getResult();
    }

    private boolean paramValueIsOk(Object value) {
        if (value instanceof String)
            return !StringUtils.isBlank((String) value);
        else if (value instanceof Number)
            return ((Number) value).intValue() != 0;
        else
            return value != null;
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
        Set<String> declaredParams = condition.getParameters();

        if (declaredParams.isEmpty())
            return true;

        for (String param : declaredParams) {
            if (params.contains(param))
                return true;
        }

        return false;
    }
}

/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.global.filter;

import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import com.haulmont.cuba.core.global.TemplateHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.dom4j.Element;

import java.util.*;

/**
 */
public class QueryFilter extends FilterParser {
    private final String targetEntity;

    public QueryFilter(Condition condition, String targetEntity) {
        super(condition);
        this.targetEntity = targetEntity;
    }

    public QueryFilter(Element element, String targetEntity) {
        super(element);
        this.targetEntity = targetEntity;
    }

    public static QueryFilter merge(QueryFilter src1, QueryFilter src2) {
        if (src1 == null || src2 == null)
            throw new IllegalArgumentException("Source query filter is null");

        if (!src1.targetEntity.equals(src2.targetEntity))
            throw new IllegalArgumentException("Target entities do not match");


        Condition root = new LogicalCondition("root", LogicalOp.AND);
        root.getConditions().add(src1.getRoot());
        root.getConditions().add(src2.getRoot());

        QueryFilter queryFilter = new QueryFilter(root, src1.targetEntity);
        return queryFilter;
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
        QueryTransformer transformer = QueryTransformerFactory.createTransformer(query);

        if (isActual(root, params)) {
            Condition refined = refine(root, params);
            String where = new FilterJpqlGenerator().generateJpql(refined);

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

    protected boolean paramValueIsOk(Object value) {
        if (value instanceof String)
            return !StringUtils.isBlank((String) value);
        else return value != null;
    }

    protected Condition refine(Condition src, Set<String> params) {
        Condition copy = src.copy();
        List<Condition> list = new ArrayList<>();
        for (Condition condition : src.getConditions()) {
            if (isActual(condition, params)) {
                list.add(refine(condition, params));
            }
        }
        copy.setConditions(list.isEmpty() ? Collections.EMPTY_LIST : list);
        return copy;
    }

    protected boolean isActual(Condition condition, Set<String> params) {
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

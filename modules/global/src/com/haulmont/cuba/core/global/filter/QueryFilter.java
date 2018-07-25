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

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.queryconditions.JpqlCondition;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QueryFilter extends FilterParser implements Serializable {

    protected boolean enableSessionParams = AppBeans.get(Configuration.class)
            .getConfig(GlobalConfig.class).getEnableSessionParamsInQueryFilter();

    public QueryFilter(Condition condition) {
        super(condition);
    }

    public QueryFilter(Element element) {
        super(element);
    }

    public static QueryFilter merge(QueryFilter src1, QueryFilter src2) {
        if (src1 == null || src2 == null)
            throw new IllegalArgumentException("Source query filter is null");

        Condition root = new LogicalCondition("root", LogicalOp.AND);
        root.getConditions().add(src1.getRoot());
        root.getConditions().add(src2.getRoot());

        QueryFilter queryFilter = new QueryFilter(root);
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

        if (isActual(root, params)) {
            Condition refined = refine(root, params);
            if (refined != null) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(query);
                String where = new FilterJpqlGenerator().generateJpql(refined);

                if (!StringUtils.isBlank(where)) {
                    Set<String> joins = refined.getJoins();
                    if (!joins.isEmpty()) {
                        String joinsStr = joins.stream().collect(Collectors.joining(" "));
                        transformer.addJoinAndWhere(joinsStr, where);
                    } else {
                        transformer.addWhere(where);
                    }
                }
                return transformer.getResult();
            }
        }
        return query;
    }

    protected boolean paramValueIsOk(Object value) {
        if (value instanceof String)
            return !StringUtils.isBlank((String) value);
        else return value != null;
    }

    @Nullable
    protected Condition refine(Condition src, Set<String> params) {
        Condition copy = src.copy();
        List<Condition> list = new ArrayList<>();
        for (Condition condition : src.getConditions()) {
            if (isActual(condition, params)) {
                Condition refined = refine(condition, params);
                if (refined != null && !(refined instanceof LogicalCondition && refined.getConditions().isEmpty()))
                    list.add(refined);
            }
        }
        if (copy instanceof LogicalCondition && list.isEmpty()) {
            return null;
        }
        copy.setConditions(list.isEmpty() ? Collections.EMPTY_LIST : list);
        return copy;
    }

    protected boolean isActual(Condition condition, Set<String> params) {
        Set<ParameterInfo> declaredParams = condition.getParameters();

        if (declaredParams.isEmpty())
            return true;
        if (enableSessionParams) {
            Predicate<ParameterInfo> paramHasValue = paramInfo -> params.contains(paramInfo.getName());
            if (condition.getConditions().isEmpty()) {
                // for leaf condition all parameters must have values
                return declaredParams.stream().allMatch(paramHasValue);
            } else {
                // for branch conditions at least some parameters must have values
                return declaredParams.stream().anyMatch(paramHasValue);
            }
        } else {
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

    public com.haulmont.cuba.core.global.queryconditions.Condition toQueryCondition() {
        return createQueryCondition(root);
    }

    protected com.haulmont.cuba.core.global.queryconditions.Condition createQueryCondition(Condition condition) {
        com.haulmont.cuba.core.global.queryconditions.Condition result;
        if (condition instanceof LogicalCondition) {
            LogicalCondition logicalCondition = (LogicalCondition) condition;
            if (logicalCondition.getOperation() == LogicalOp.AND) {
                result = new com.haulmont.cuba.core.global.queryconditions.LogicalCondition(com.haulmont.cuba.core.global.queryconditions.LogicalCondition.Type.AND);
            } else if (logicalCondition.getOperation() == LogicalOp.OR) {
                result = new com.haulmont.cuba.core.global.queryconditions.LogicalCondition(com.haulmont.cuba.core.global.queryconditions.LogicalCondition.Type.OR);
            } else {
                throw new UnsupportedOperationException("Operation is not supported: " + logicalCondition.getOperation());
            }
            for (Condition nestedCondition : logicalCondition.getConditions()) {
                ((com.haulmont.cuba.core.global.queryconditions.LogicalCondition) result).add(createQueryCondition(nestedCondition));
            }
        } else if (condition instanceof Clause) {
            Clause clause = (Clause) condition;
            result = new JpqlCondition(
                    clause.getJoins().isEmpty() ? null : clause.getJoins().iterator().next(),
                    clause.getContent());
        } else {
            throw new UnsupportedOperationException("Condition is not supported: " + condition);
        }
        return result;
    }
}
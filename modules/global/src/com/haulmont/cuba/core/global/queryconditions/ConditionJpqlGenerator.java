package com.haulmont.cuba.core.global.queryconditions;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.QueryTransformer;
import com.haulmont.cuba.core.global.QueryTransformerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Modifies JPQL query according to the tree of conditions. See {@link #processQuery(String, Condition)} method.
 */
@Component(ConditionJpqlGenerator.NAME)
public class ConditionJpqlGenerator {

    public static final String NAME = "cuba_ConditionJpqlGenerator";

    @Inject
    private QueryTransformerFactory queryTransformerFactory;

    /**
     * Returns a JPQL query modified according to the given tree of conditions.
     * @param query JPQL query
     * @param condition root condition. If null, the query is returned as is.
     */
    public String processQuery(String query, @Nullable Condition condition) {
        if (condition == null) {
            return query;
        }
        QueryTransformer transformer = queryTransformerFactory.transformer(query);

        addJoinStatements(transformer, condition);
        transformer.addWhere(generateWhere(condition));
        return transformer.getResult();
    }

    protected void addJoinStatements(QueryTransformer transformer, Condition condition) {
        if (condition instanceof LogicalCondition) {
            LogicalCondition logical = (LogicalCondition) condition;
            List<Condition> conditions = logical.getConditions();
            if (!conditions.isEmpty()) {
                conditions.forEach(c -> addJoinStatements(transformer, c));
            }
            return;
        } else if (condition instanceof JpqlCondition) {
            String join = ((JpqlCondition) condition).getValue("join");
            if (!Strings.isNullOrEmpty(join)) {
                transformer.addJoin(join);
            }
            return;
        }
        throw new UnsupportedOperationException("Condition is not supported: " + condition);
    }

    protected String generateWhere(Condition condition) {
        if (condition instanceof LogicalCondition) {
            LogicalCondition logical = (LogicalCondition) condition;
            List<Condition> conditions = logical.getConditions();
            if (conditions.isEmpty())
                return "";
            else {
                StringBuilder sb = new StringBuilder();

                String op = logical.getType() == LogicalCondition.Type.AND ? " and " : " or ";

                String where = conditions.stream().map(this::generateWhere)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.joining(op));

                if (StringUtils.isNotBlank(where)) {
                    sb.append("(")
                            .append(where)
                            .append(")");
                }

                return sb.toString();
            }
        } else if (condition instanceof JpqlCondition) {
            return ((JpqlCondition) condition).getValue("where");
        }
        throw new UnsupportedOperationException("Condition is not supported: " + condition);
    }

}

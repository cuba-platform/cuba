package com.haulmont.cuba.core.global.queryconditions;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Logical condition (AND, OR) which contains other conditions.
 * <p>
 * {@link #getParameters()} returns parameters of nested conditions.
 */
public class LogicalCondition implements Condition {

    public enum Type {
        AND, OR
    }

    private List<Condition> conditions = new ArrayList<>();

    private Type type;

    public static LogicalCondition and() {
        return new LogicalCondition(Type.AND);
    }

    public static LogicalCondition or() {
        return new LogicalCondition(Type.OR);
    }

    public LogicalCondition(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public LogicalCondition add(Condition condition) {
        conditions.add(condition);
        return this;
    }

    @Override
    public Collection<String> getParameters() {
        Set<String> parameters = new HashSet<>();
        for (Condition nestedCondition : conditions) {
            parameters.addAll(nestedCondition.getParameters());
        }
        return parameters;
    }

    @Nullable
    @Override
    public Condition actualize(Set<String> actualParameters) {
        LogicalCondition copy = new LogicalCondition(type);
        for (Condition condition : conditions) {
            Condition actualized = condition.actualize(actualParameters);
            if (actualized != null) {
                copy.add(actualized);
            }
        }
        if (copy.getConditions().isEmpty()) {
            return null;
        } else if (copy.getConditions().size() == 1) {
            return copy.getConditions().get(0);
        } else {
            return copy;
        }
    }

    @Override
    public Condition copy() {
        LogicalCondition copy = new LogicalCondition(type);
        copy.conditions = new ArrayList<>(conditions.size());
        for (Condition nestedCondition : conditions) {
            copy.add(nestedCondition.copy());
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        for (Condition condition : conditions) {
            result.append(condition);
            if (conditions.indexOf(condition) != conditions.size() - 1) {
                result.append(" ").append(type).append(" ");
            }
        }
        result.append(")");
        return result.toString();
    }
}

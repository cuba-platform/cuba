/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global.filter;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Clause extends Condition {
    protected String content;

    protected String join;

    protected Set<ParameterInfo> parameters;

    protected Op operator;

    protected String type;

    public Clause(String name, String content, @Nullable String join, @Nullable String operator, @Nullable String type) {
        super(name);

        this.content = content;
        this.join = join;
        this.parameters = ParametersHelper.parseQuery(content);
        if (operator != null) {
            this.operator = Op.valueOf(operator);
        }
        this.type = type;
    }

    @Override
    public List<Condition> getConditions() {
        return Collections.emptyList();
    }

    @Override
    public void setConditions(List<Condition> conditions) {
    }

    public String getContent() {
        return content;
    }

    @Override
    public Set<ParameterInfo> getParameters() {
        return parameters;
    }

    @Override
    public Set<String> getJoins() {
        return join == null ? Collections.EMPTY_SET : Collections.singleton(join);
    }

    public Op getOperator() {
        return operator;
    }
}

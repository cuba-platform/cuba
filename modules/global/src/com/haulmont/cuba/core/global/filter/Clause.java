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

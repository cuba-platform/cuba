/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.cuba.gui.xml.ParametersHelper;
import com.haulmont.cuba.gui.xml.ParameterInfo;

import java.util.*;

public class Clause extends Condition {

    private String content;

    private Set<ParameterInfo> parameters;

    private String join;

    public Clause(String content, String join) {
        this.content = content;
        this.join = join;
        parameters = ParametersHelper.parseQuery(content);
    }

    @Override
    public List<Condition> getConditions() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void setConditions(List<Condition> conditions) {
    }

    @Override
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
}

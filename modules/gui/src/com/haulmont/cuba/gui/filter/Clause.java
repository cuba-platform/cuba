/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.09.2009 11:31:21
 *
 * $Id$
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.cuba.gui.xml.ParametersHelper;

import java.util.*;

public class Clause extends Condition {

    private String content;

    private Set<String> parameters;

    private String join;

    public Clause(String content, String join) {
        this.content = content;
        this.join = join;
        parameters = ParametersHelper.extractNames(content);
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
    public Set<String> getParameters() {
        return parameters;
    }

    @Override
    public Set<String> getJoins() {
        return join == null ? Collections.EMPTY_SET : Collections.singleton(join);
    }
}

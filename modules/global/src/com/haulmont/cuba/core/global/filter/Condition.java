/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global.filter;

import java.util.List;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class Condition implements Cloneable {
    protected String name;

    public Condition(String name) {
        this.name = name;
    }

    public Condition copy() {
        try {
            return (Condition) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract List<Condition> getConditions();

    public abstract void setConditions(List<Condition> conditions);

    public abstract Set<ParameterInfo> getParameters();

    public abstract Set<String> getJoins();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
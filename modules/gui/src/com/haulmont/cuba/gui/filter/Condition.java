/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.filter;

import com.haulmont.cuba.gui.xml.ParameterInfo;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public abstract class Condition implements Cloneable {

    public Condition copy() {
        try {
            return (Condition) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract List<Condition> getConditions();
    public abstract void setConditions(List<Condition> conditions);

    public abstract String getContent();

    public abstract Set<ParameterInfo> getParameters();

    public abstract Set<String> getJoins();
}

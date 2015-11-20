/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global.filter;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ParameterInfo {

    public enum Type {
        DATASOURCE("ds"),
        COMPONENT("component"),
        PARAM("param"),
        SESSION("session"),
        CUSTOM("custom");

        private String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    protected Type type;
    protected String path;
    protected Class javaClass;
    protected String conditionName;
    protected boolean caseInsensitive;
    protected String value;

    ParameterInfo(String name, Type type, boolean caseInsensitive) {
        this.path = name;
        this.type = type;
        this.caseInsensitive = caseInsensitive;
    }

    public Type getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return (type.getPrefix() + "$" + path);
    }

    public String getFlatName() {
        return (type.getPrefix() + "." + path).replaceAll("\\.", "_");
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    @Nullable
    public Class getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }

    @Nullable
    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterInfo that = (ParameterInfo) o;

        return path.equals(that.path) && type == that.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return type + " : " + path;
    }
}
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

/**
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
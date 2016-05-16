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

package com.haulmont.cuba.core.app.scheduled;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class MethodInfo implements Serializable {

    private String name;
    private List<MethodParameterInfo> parameters = new ArrayList<>();

    public MethodInfo(String name, List<MethodParameterInfo> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MethodParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(List<MethodParameterInfo> parameters) {
        Preconditions.checkNotNull(parameters, "Parameters can not be null");
        this.parameters = parameters;
    }

    public MethodParameterInfo getParameter(String paramName) {
        for (MethodParameterInfo parameter : parameters) {
            if (paramName.equals(parameter.getName()))
                return parameter;
        }
        return null;
    }

    public String getMethodSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("(");

        int count = 0;
        for (MethodParameterInfo param : parameters) {
            sb.append(param.getType().getSimpleName())
                    .append(" ")
                    .append(param.getName());

            if (++count != parameters.size())
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return getMethodSignature();
    }

    public boolean definitionEquals(MethodInfo methodInfo) {
        if (!this.name.equals(methodInfo.getName()))
            return false;
        if (this.getParameters().size() != methodInfo.getParameters().size())
            return false;

        for (int i = 0; i < this.parameters.size(); i++) {
            MethodParameterInfo param1 = this.parameters.get(i);
            MethodParameterInfo param2 = methodInfo.getParameters().get(i);
            if (!param1.getTypeName().equals(param2.getTypeName()))
                return false;
        }

        return true;
    }
}
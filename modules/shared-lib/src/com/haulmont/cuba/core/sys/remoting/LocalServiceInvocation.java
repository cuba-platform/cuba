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

package com.haulmont.cuba.core.sys.remoting;

import java.util.UUID;

/**
 *
 */
public class LocalServiceInvocation {

    private String methodName;
    private String[] parameterTypeNames;
    private byte[][] argumentsData;
    private Object[] notSerializableArguments;
    private UUID sessionId;

    public LocalServiceInvocation(String methodName, String[] parameterTypeNames,
                                  byte[][] argumentsData, Object[] notSerializableArguments, UUID sessionId) {
        this.methodName = methodName;
        this.parameterTypeNames = parameterTypeNames;
        this.argumentsData = argumentsData;
        this.notSerializableArguments = notSerializableArguments;
        this.sessionId = sessionId;
    }

    public byte[][] getArgumentsData() {
        return argumentsData;
    }

    public Object[] getNotSerializableArguments() {
        return notSerializableArguments;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParameterTypeNames() {
        return parameterTypeNames;
    }

    public UUID getSessionId() {
        return sessionId;
    }
}

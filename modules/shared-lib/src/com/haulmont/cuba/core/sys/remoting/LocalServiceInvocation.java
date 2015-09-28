/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
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

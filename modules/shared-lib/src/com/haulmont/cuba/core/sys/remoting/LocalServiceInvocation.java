/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
    private UUID sessionId;

    public LocalServiceInvocation(String methodName, String[] parameterTypeNames, byte[][] argumentsData, UUID sessionId) {
        this.methodName = methodName;
        this.parameterTypeNames = parameterTypeNames;
        this.argumentsData = argumentsData;
        this.sessionId = sessionId;
    }

    public byte[][] getArgumentsData() {
        return argumentsData;
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

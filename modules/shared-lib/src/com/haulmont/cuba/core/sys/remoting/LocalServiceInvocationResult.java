/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LocalServiceInvocationResult {

    private byte[] data;
    private Object notSerializableData;
    private byte[] exception;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Object getNotSerializableData() {
        return notSerializableData;
    }

    public void setNotSerializableData(Object notSerializableData) {
        this.notSerializableData = notSerializableData;
    }

    public byte[] getException() {
        return exception;
    }

    public void setException(byte[] exception) {
        this.exception = exception;
    }
}

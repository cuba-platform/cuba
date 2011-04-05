/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.remoting;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LocalServiceInvocationResult {

    private byte[] data;
    private byte[] exception;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getException() {
        return exception;
    }

    public void setException(byte[] exception) {
        this.exception = exception;
    }
}

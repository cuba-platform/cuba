package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.SilentException;

/**
 * Created by ikuchmin on 05.12.16.
 */
public class CycleException extends SilentException {

    public CycleException() {
        super();
    }

    public CycleException(String message) {
        super(message);
    }

    public CycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public CycleException(Throwable cause) {
        super(cause);
    }

    protected CycleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

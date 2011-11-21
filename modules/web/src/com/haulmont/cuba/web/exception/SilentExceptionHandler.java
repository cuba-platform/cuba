/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.web.App;

import javax.annotation.Nullable;

/**
 * Handler that does nothing in respond to {@link SilentException}.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class SilentExceptionHandler extends AbstractExceptionHandler {

    public SilentExceptionHandler() {
        super(SilentException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
    }
}

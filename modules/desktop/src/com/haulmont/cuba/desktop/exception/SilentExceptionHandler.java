/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.SilentException;

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
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
    }
}

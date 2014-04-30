/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.web.App;

import javax.annotation.Nullable;

/**
 * Handler that does nothing in respond to {@link SilentException}.
 *
 * @author krivopustov
 * @version $Id$
 */
public class SilentExceptionHandler extends AbstractExceptionHandler {

    public SilentExceptionHandler() {
        super(SilentException.class.getName());
    }

    @Override
    protected void doHandle(App app, String className, String message, @Nullable Throwable throwable) {
    }
}
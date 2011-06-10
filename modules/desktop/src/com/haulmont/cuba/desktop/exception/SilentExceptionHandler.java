/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.SilentException;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class SilentExceptionHandler extends AbstractExceptionHandler<SilentException> {

    public SilentExceptionHandler() {
        super(SilentException.class);
    }

    @Override
    protected void doHandle(Thread thread, SilentException e) {
    }
}

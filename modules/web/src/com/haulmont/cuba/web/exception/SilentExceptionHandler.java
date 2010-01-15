/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 15.01.2010 16:42:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.web.App;

public class SilentExceptionHandler extends AbstractExceptionHandler<SilentException> {

    public SilentExceptionHandler() {
        super(SilentException.class);
    }

    @Override
    protected void doHandle(SilentException e, App app) {
    }
}

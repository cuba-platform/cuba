/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class IllegalComponentStateExceptionHandler extends AbstractExceptionHandler {

    public IllegalComponentStateExceptionHandler() {
        super(IllegalComponentStateException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        // Just swallow this exception because it usually occurs when user clicks on Language drop-down list
        // while the app is logging in and UI is freezed for some time.
    }
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.desktop.App;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class AWTExceptionHandler {

    public void handle(Throwable throwable) {
        App.getInstance().handleException(Thread.currentThread(), throwable);
    }
}

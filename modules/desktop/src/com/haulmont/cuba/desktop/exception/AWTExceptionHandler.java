/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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

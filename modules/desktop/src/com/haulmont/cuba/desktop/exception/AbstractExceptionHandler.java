/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.AppConfig;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractExceptionHandler<T extends Throwable> implements ExceptionHandler {

    private final Class<T> tClass;

    public AbstractExceptionHandler(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        Throwable t = exception;
        while (t != null) {
            if (tClass.isAssignableFrom(t.getClass())) {
                doHandle(thread, (T) t);
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    protected abstract void doHandle(Thread thread, T t);

    protected String getMessage(String key) {
        return MessageProvider.getMessage(AppConfig.getMessagesPack(), key, App.getInstance().getLocale());
    }
}

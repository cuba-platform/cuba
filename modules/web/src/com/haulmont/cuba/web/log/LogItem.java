/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 16.12.2008 11:09:18
 *
 * $Id$
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.global.TimeProvider;

import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;

public class LogItem
{
    private Date timestamp;
    private LogLevel level;
    private String message;
    private String stacktrace;

    public LogItem(LogLevel level, String message, Throwable throwable) {
        this.timestamp = TimeProvider.currentTimestamp();
        this.level = level;
        this.message = message;
        if (throwable != null)
            this.stacktrace = ExceptionUtils.getStackTrace(throwable);
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

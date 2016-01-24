/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.log;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Date;

/**
 * @author krivopustov
 * @version $Id$
 */
public class LogItem {

    private Date timestamp;
    private LogLevel level;
    private String message;
    private String stacktrace;

    public LogItem(LogLevel level, String message, Throwable throwable) {
        TimeSource timeSource = AppBeans.get(TimeSource.NAME);
        this.timestamp = timeSource.currentTimestamp();
        this.level = level;
        this.message = message;
        if (throwable != null) {
            this.stacktrace = ExceptionUtils.getFullStackTrace(throwable);
        }
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

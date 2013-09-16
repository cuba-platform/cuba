/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractLogWrapper implements Log {

    protected final Log delegate;

    protected ThreadLocal<Boolean> inLogging = new ThreadLocal<Boolean>();

    public AbstractLogWrapper(Log delegate) {
        this.delegate = delegate;
    }

    protected String getInfo() {
        if (BooleanUtils.isTrue(inLogging.get()))
            return "";

        inLogging.set(true);
        try {
            StringBuilder sb = new StringBuilder();

            String logAppName = AppContext.getProperty("cuba.logAppName");
            if (logAppName == null || logAppName.equals("") || Boolean.valueOf(logAppName)) {
                String webContextName = AppContext.getProperty("cuba.webContextName");
                if (webContextName != null && !webContextName.equals("")) {
                    sb.append("[").append(webContextName).append("] ");
                }
            }

            String userInfo = getUserInfo();
            if (userInfo != null) {
                sb.append("[").append(userInfo).append("] ");
            }

            return sb.toString();
        } finally {
            inLogging.set(null);
        }
    }

    protected abstract String getUserInfo();

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return delegate.isFatalEnabled();
    }

    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    public void trace(Object o) {
        delegate.trace(getInfo() + o);
    }

    public void trace(Object o, Throwable throwable) {
        delegate.trace(getInfo() + o, throwable);
    }

    public void debug(Object o) {
        delegate.debug(getInfo() + o);
    }

    public void debug(Object o, Throwable throwable) {
        delegate.debug(getInfo() + o, throwable);
    }

    public void info(Object o) {
        delegate.info(getInfo() + o);
    }

    public void info(Object o, Throwable throwable) {
        delegate.info(getInfo() + o, throwable);
    }

    public void warn(Object o) {
        delegate.warn(getInfo() + o);
    }

    public void warn(Object o, Throwable throwable) {
        delegate.warn(getInfo() + o, throwable);
    }

    public void error(Object o) {
        delegate.error(getInfo() + o);
    }

    public void error(Object o, Throwable throwable) {
        delegate.error(getInfo() + o, throwable);
    }

    public void fatal(Object o) {
        delegate.fatal(getInfo() + o);
    }

    public void fatal(Object o, Throwable throwable) {
        delegate.fatal(getInfo() + o, throwable);
    }
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.logging;

import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;

/**
 * @author krivopustov
 * @version $id$
 */
public abstract class AbstractLogWrapper implements Log {

    protected final Log delegate;

    protected ThreadLocal<Boolean> inLogging = new ThreadLocal<>();

    public AbstractLogWrapper(Log delegate) {
        this.delegate = delegate;
    }

    protected String getInfo() {
        if (BooleanUtils.isTrue(inLogging.get())) {
            return "";
        }

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

    @Override
    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return delegate.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return delegate.isFatalEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return delegate.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return delegate.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled();
    }

    @Override
    public void trace(Object o) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(getInfo() + o);
        }
    }

    @Override
    public void trace(Object o, Throwable throwable) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(getInfo() + o, throwable);
        }
    }

    @Override
    public void debug(Object o) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(getInfo() + o);
        }
    }

    @Override
    public void debug(Object o, Throwable throwable) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(getInfo() + o, throwable);
        }
    }

    @Override
    public void info(Object o) {
        if (delegate.isInfoEnabled()) {
            delegate.info(getInfo() + o);
        }
    }

    @Override
    public void info(Object o, Throwable throwable) {
        if (delegate.isInfoEnabled()) {
            delegate.info(getInfo() + o, throwable);
        }
    }

    @Override
    public void warn(Object o) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(getInfo() + o);
        }
    }

    @Override
    public void warn(Object o, Throwable throwable) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(getInfo() + o, throwable);
        }
    }

    @Override
    public void error(Object o) {
        if (delegate.isErrorEnabled()) {
            delegate.error(getInfo() + o);
        }
    }

    @Override
    public void error(Object o, Throwable throwable) {
        if (delegate.isErrorEnabled()) {
            delegate.error(getInfo() + o, throwable);
        }
    }

    @Override
    public void fatal(Object o) {
        if (delegate.isFatalEnabled()) {
            delegate.fatal(getInfo() + o);
        }
    }

    @Override
    public void fatal(Object o, Throwable throwable) {
        if (delegate.isFatalEnabled()) {
            delegate.fatal(getInfo() + o, throwable);
        }
    }
}

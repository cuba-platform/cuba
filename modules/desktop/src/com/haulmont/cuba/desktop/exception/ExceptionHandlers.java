/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class that holds the collection of exception handlers and delegates unhandled exception processing to them. Handlers
 * form the chain of responsibility.
 *
 * <p>A set of exception handlers is configured by defining <code>ExceptionHandlersConfiguration</code> beans
 * in spring.xml. If a project needs specific handlers, it should define a bean of such type with its own
 * <strong>id</strong>, e.g. <code>refapp_ExceptionHandlersConfiguration</code></p>
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_ExceptionHandlers")
public class ExceptionHandlers {

    protected LinkedList<ExceptionHandler> handlers = new LinkedList<ExceptionHandler>();

    protected ExceptionHandler defaultHandler;

    private Log log = LogFactory.getLog(getClass());

    public ExceptionHandlers() {
        this.defaultHandler = new DefaultExceptionHandler();
        createMinimalSet();
    }

    /**
     * @return default exception handler which is used when none of registered handlers have handled an exception
     */
    public ExceptionHandler getDefaultHandler() {
        return defaultHandler;
    }

    /**
     * Set the default handler instead of initialized in constructor.
     * @param defaultHandler    default handler instance
     */
    public void setDefaultHandler(ExceptionHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * Adds new handler if it is not yet registered.
     * @param handler   handler instance
     */
    public void addHandler(ExceptionHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    /**
     * Return all registered handlers.
     * @return  modifiable handlers list
     */
    public LinkedList<ExceptionHandler> getHandlers() {
        return handlers;
    }

    /**
     * Delegates exception handling to registered handlers.
     * @param thread    current thread
     * @param exception exception instance
     */
    public void handle(Thread thread, Throwable exception) {
        for (ExceptionHandler handler : handlers) {
            if (handler.handle(thread, exception))
                return;
        }
        defaultHandler.handle(thread, exception);
    }

    /**
     * Create all handlers defined by <code>ExceptionHandlersConfiguration</code> beans in spring.xml.
     */
    public void createByConfiguration() {
        handlers.clear();
        Map<String, ExceptionHandlersConfiguration> map = AppBeans.getAll(ExceptionHandlersConfiguration.class);
        for (ExceptionHandlersConfiguration conf : map.values()) {
            for (Class aClass : conf.getHandlerClasses()) {
                try {
                    handlers.add(ReflectionHelper.<ExceptionHandler>newInstance(aClass));
                } catch (NoSuchMethodException e) {
                    log.error("Unable to instantiate " + aClass, e);
                }
            }
        }
    }

    /**
     * Create a minimal set of handlers for disconnected client.
     */
    public void createMinimalSet() {
        handlers.clear();
        addHandler(new SilentExceptionHandler());
        addHandler(new ConnectExceptionHandler());
        addHandler(new IllegalComponentStateExceptionHandler());
    }

    /**
     * Remove all handlers.
     */
    public void removeAll() {
        handlers.clear();
    }
}

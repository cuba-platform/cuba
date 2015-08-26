/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.sys.DesktopWindowManager;
import com.haulmont.cuba.gui.exception.GenericExceptionHandler;
import com.haulmont.cuba.gui.exception.SilentExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.OrderComparator;

import javax.annotation.ManagedBean;
import java.util.*;

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

    protected LinkedList<ExceptionHandler> handlers = new LinkedList<>();

    protected LinkedList<GenericExceptionHandler> genericHandlers = new LinkedList<>();

    protected ExceptionHandler defaultHandler;

    private Logger log = LoggerFactory.getLogger(getClass());

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
     * Adds new Web-level handler if it is not yet registered.
     * @param handler   handler instance
     */
    public void addHandler(ExceptionHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    /**
     * Adds new GUI-level handler if it is not yet registered.
     * @param handler   handler instance
     */
    private void addHandler(GenericExceptionHandler handler) {
        if (!genericHandlers.contains(handler))
            genericHandlers.add(handler);
    }

    /**
     * Delegates exception handling to registered handlers.
     * @param thread    current thread
     * @param exception exception instance
     * @param windowManager
     */
    public void handle(Thread thread, Throwable exception, DesktopWindowManager windowManager) {
        for (ExceptionHandler handler : handlers) {
            if (handler.handle(thread, exception))
                return;
        }
        for (GenericExceptionHandler handler : genericHandlers) {
            if (handler.handle(exception, windowManager))
                return;
        }
        defaultHandler.handle(thread, exception);
    }

    /**
     * Create all handlers defined by <code>ExceptionHandlersConfiguration</code> beans in spring.xml and
     * GUI handlers defined as Spring-beans.
     */
    public void createByConfiguration() {
        removeAll();

        // Desktop handlers
        Map<String, ExceptionHandlersConfiguration> map = AppBeans.getAll(ExceptionHandlersConfiguration.class);

        // Project-level handlers must run before platform-level
        List<ExceptionHandlersConfiguration> configurations = new ArrayList<>(map.values());
        Collections.reverse(configurations);

        for (ExceptionHandlersConfiguration conf : configurations) {
            for (Class aClass : conf.getHandlerClasses()) {
                try {
                    handlers.add(ReflectionHelper.<ExceptionHandler>newInstance(aClass));
                } catch (NoSuchMethodException e) {
                    log.error("Unable to instantiate " + aClass, e);
                }
            }
        }

        // GUI handlers
        Map<String, GenericExceptionHandler> handlerMap = AppBeans.getAll(GenericExceptionHandler.class);

        List<GenericExceptionHandler> handlers = new ArrayList<>(handlerMap.values());
        Collections.sort(handlers, new OrderComparator());

        for (GenericExceptionHandler handler : handlers) {
            addHandler(handler);
        }
    }

    /**
     * Create a minimal set of handlers for disconnected client.
     */
    public void createMinimalSet() {
        removeAll();
        addHandler(new SilentExceptionHandler());
        addHandler(new EntitySerializationExceptionHandler());
        addHandler(new ConnectExceptionHandler());
        addHandler(new IllegalComponentStateExceptionHandler());
    }

    /**
     * Remove all handlers.
     */
    public void removeAll() {
        handlers.clear();
        genericHandlers.clear();
    }
}

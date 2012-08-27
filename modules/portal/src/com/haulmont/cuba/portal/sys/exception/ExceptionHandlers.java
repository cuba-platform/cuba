/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.sys.exception;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class that holds the collection of exception handlers and delegates unhandled exception processing to them. Handlers
 * form the chain of responsibility.
 * <p/>
 * <p>A set of exception handlers is configured by defining <code>ExceptionHandlersConfiguration</code> beans
 * in spring.xml. If a project needs specific handlers, it should define a bean of such type with its own
 * <strong>id</strong>, e.g. <code>refapp_ExceptionHandlersConfiguration</code></p>
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(ExceptionHandlers.NAME)
public class ExceptionHandlers {

    public final static String NAME = "cuba_ExceptionHandlers";

    protected LinkedList<ExceptionHandler> handlers = new LinkedList<>();

    protected ExceptionHandler defaultHandler;

    protected Log log = LogFactory.getLog(getClass());

    public ExceptionHandlers() {
        this.defaultHandler = new DefaultExceptionHandler();
    }

    /**
     * Adds new handler if it is not yet registered.
     *
     * @param handler handler instance
     */
    public void addHandler(ExceptionHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    /**
     * Return all registered handlers.
     *
     * @return modifiable handlers list
     */
    public LinkedList<ExceptionHandler> getHandlers() {
        return handlers;
    }

    /**
     * Delegates exception handling to registered handlers.
     *
     * @param request
     * @param response
     * @param ex
     * @return
     */
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        for (ExceptionHandler handler : handlers) {
            ModelAndView modelAndView = handler.handle(request, response, ex);
            if (modelAndView != null)
                return modelAndView;
        }
        return defaultHandler.handle(request, response, ex);
    }

    /**
     * Create all handlers defined by <code>ExceptionHandlersConfiguration</code> beans in spring.xml.
     */
    public void createByConfiguration() {
        Map<String, ExceptionHandler> availableExceptionHandlers = AppContext.getBeansOfType(ExceptionHandler.class);

        Map<String, ExceptionHandlersConfiguration> map = AppContext.getBeansOfType(ExceptionHandlersConfiguration.class);
        for (ExceptionHandlersConfiguration conf : map.values()) {
            for (String id : conf.getHandlerBeans()) {
                Object bean = availableExceptionHandlers.get(id);
                if (bean != null) {
                    handlers.add((ExceptionHandler) bean);
                    availableExceptionHandlers.remove(id);
                } else {
                    log.warn(String.format("Object %s is not exception handler", id));
                }
            }

            for (Class aClass : conf.getHandlerClasses()) {
                try {
                    handlers.add(ReflectionHelper.<ExceptionHandler>newInstance(aClass));
                } catch (NoSuchMethodException e) {
                    log.error("Unable to instantiate " + aClass, e);
                }
            }

            for (ExceptionHandler handler : availableExceptionHandlers.values()) {
                handlers.add(handler);
            }
        }
    }

    /**
     * Remove all handlers.
     */
    public void removeAll() {
        handlers.clear();
    }
}

/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OptionalDataException;

/**
 * Exports a middleware service bean as an HTTP invoker service endpoint.
 *
 * @author krivopustov
 * @version $Id$
 */
public class HttpServiceExporter extends HttpInvokerServiceExporter implements BeanNameAware {

    public HttpServiceExporter() {
        super();
        setRegisterTraceInterceptor(false);
        setRemoteInvocationExecutor(new CubaRemoteInvocationExecutor());
    }

    @Override
    public void setService(Object service) {
        super.setService(service);
    }

    @Override
    public void setBeanName(String name) {
        Object service = getService();
        if (service == null)
            throw new IllegalStateException("Target service is null");

        String entryName = AppContext.getProperty("cuba.webContextName") + name;

        LocalServiceInvoker invoker = new LocalServiceInvokerImpl(service);
        LocalServiceDirectory.registerInvoker(entryName, invoker);
    }

    /*
     * In base implementation, exceptions which are thrown during reading remote invocation request, are not handled.
     * Client gets useless HTTP status 500 in this case.
     *
     * This implementation passes some known exceptions to client.
     */
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            RemoteInvocationResult result;
            try {
                RemoteInvocation invocation = readRemoteInvocation(request);
                result = invokeAndCreateResult(invocation, getProxy());
            } catch (OptionalDataException | ClassCastException e) { // typical binary incompatibility exceptions
                logger.error("Failed to read remote invocation request", e);
                result = new RemoteInvocationResult(e);
            }

            writeRemoteInvocationResult(request, response, result);
        } catch (ClassNotFoundException ex) {
            throw new NestedServletException("Class not found during deserialization", ex);
        }
    }
}
/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.serialization.SerializationSupport;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

/**
 * Exports a middleware service bean as an HTTP invoker service endpoint.
 *
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

    @Override
    protected void doWriteRemoteInvocationResult(RemoteInvocationResult result, ObjectOutputStream oos) throws IOException {
        SerializationSupport.serialize(result, oos);
    }

    @Override
    protected RemoteInvocation doReadRemoteInvocation(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (RemoteInvocation) SerializationSupport.deserialize(ois);
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
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
}
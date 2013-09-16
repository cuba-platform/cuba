/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.global.RemoteException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationResult;

import java.lang.reflect.InvocationTargetException;

/**
 * @author krivopustov
 * @version $Id$
 */
public class HttpServiceProxy extends HttpInvokerProxyFactoryBean {

    public HttpServiceProxy(ClusterInvocationSupport support) {
        super();
        setRemoteInvocationFactory(new CubaRemoteInvocationFactory());

        ClusteredHttpInvokerRequestExecutor executor = new ClusteredHttpInvokerRequestExecutor(support);
        executor.setBeanClassLoader(getBeanClassLoader());
        setHttpInvokerRequestExecutor(executor);
    }

    @Override
    protected Object recreateRemoteInvocationResult(RemoteInvocationResult result) throws Throwable {
        Throwable throwable = result.getException();
        if (throwable != null) {
            if (throwable instanceof InvocationTargetException)
                throwable = ((InvocationTargetException) throwable).getTargetException();
            if (throwable instanceof RemoteException) {
                Exception exception = ((RemoteException) throwable).getFirstCauseException();
                // This is a checked exception declared in a service method
                // or rumtime exception supported by client
                if (exception != null)
                    throw exception;
            }
        }
        return super.recreateRemoteInvocationResult(result);
    }
}

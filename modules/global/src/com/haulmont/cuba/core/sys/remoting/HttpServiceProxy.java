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

import com.haulmont.cuba.core.global.RemoteException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationResult;

import java.lang.reflect.InvocationTargetException;

public class HttpServiceProxy extends HttpInvokerProxyFactoryBean {

    public HttpServiceProxy(ClusterInvocationSupport support) {
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
                // or runtime exception supported by client
                if (exception != null)
                    throw exception;
            }
        }
        return super.recreateRemoteInvocationResult(result);
    }
}
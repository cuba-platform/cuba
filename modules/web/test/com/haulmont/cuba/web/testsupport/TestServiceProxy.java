/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.testsupport;

import com.haulmont.cuba.web.sys.remoting.LocalServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class TestServiceProxy extends LocalServiceProxy {

    private static final Logger log = LoggerFactory.getLogger(TestServiceProxy.class);

    private static Map<Class, Object> mocks = new HashMap<>();

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(
                getBeanClassLoader(),
                new Class[]{getServiceInterface()},
                new ServiceInvocationHandler(getServiceInterface()));
    }

    public static <T> void mock(Class<T> serviceInterface, @Nullable T mock) {
        mocks.put(serviceInterface, mock);
    }

    public static void clear() {
        mocks.clear();
    }

    private class ServiceInvocationHandler implements InvocationHandler {

        Class serviceInterface;

        ServiceInvocationHandler(Class serviceInterface) {
            this.serviceInterface = serviceInterface;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object stub = mocks.get(serviceInterface);
            if (stub != null) {
                log.info("Invoking " + method + " on " + stub);
                return method.invoke(stub, args);
            }
            log.info("Returning null from " + method);
            return null;
        }
    }
}
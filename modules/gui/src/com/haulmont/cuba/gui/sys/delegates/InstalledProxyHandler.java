/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.sys.delegates;

import com.haulmont.cuba.gui.screen.FrameOwner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstalledProxyHandler implements InvocationHandler {
    private final FrameOwner frameOwner;
    private final Method method;

    public InstalledProxyHandler(FrameOwner frameOwner, Method method) {
        this.frameOwner = frameOwner;
        this.method = method;
    }

    @Override
    public Object invoke(Object proxy, Method invokedMethod, Object[] args) throws Throwable {
        if ("toString".equals(invokedMethod.getName())) {
            return this.toString();
        }
        if ("equals".equals(invokedMethod.getName())) {
            return args.length == 1 && args[0] == proxy;
        }
        if ("hashCode".equals(invokedMethod.getName())) {
            return this.hashCode();
        }

        if (invokedMethod.getParameterCount() == method.getParameterCount()) {
            try {
                return this.method.invoke(frameOwner, args);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof RuntimeException) {
                    throw e.getTargetException();
                }

                throw e.getTargetException();
            }
        }

        throw new UnsupportedOperationException(
                String.format("InstalledProxyHandler does not support method %s. Check types and number of parameters",
                        invokedMethod));
    }

    @Override
    public String toString() {
        return "InstalledProxyHandler{" +
                "frameOwner=" + frameOwner.getClass() +
                ", method=" + method +
                '}';
    }
}
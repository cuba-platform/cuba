/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.sys.delegates;

import com.haulmont.cuba.gui.screen.FrameOwner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

public class InstalledSupplier implements Supplier {
    private final FrameOwner frameOwner;
    private final Method method;

    public InstalledSupplier(FrameOwner frameOwner, Method method) {
        this.frameOwner = frameOwner;
        this.method = method;
    }

    @Override
    public Object get() {
        try {
            return method.invoke(frameOwner);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (e instanceof InvocationTargetException
                    && ((InvocationTargetException) e).getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) ((InvocationTargetException) e).getTargetException();
            }

            throw new RuntimeException("Exception on @Install invocation", e);
        }
    }

    @Override
    public String toString() {
        return "InstalledSupplier{" +
                "target=" + frameOwner.getClass() +
                ", method=" + method +
                '}';
    }
}
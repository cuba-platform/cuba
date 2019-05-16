/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.gui.sys.delegates;

import com.haulmont.cuba.gui.screen.FrameOwner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class InstalledFunction implements Function {
    private final FrameOwner frameOwner;
    private final Method method;

    public InstalledFunction(FrameOwner frameOwner, Method method) {
        this.frameOwner = frameOwner;
        this.method = method;
    }

    @Override
    public Object apply(Object o) {
        try {
            return method.invoke(frameOwner, o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Exception on @Install invocation", e);
        }
    }

    @Override
    public String toString() {
        return "InstalledFunction{" +
                "frameOwner=" + frameOwner.getClass() +
                ", method=" + method +
                '}';
    }
}
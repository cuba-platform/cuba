/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

/**
 *
 */
package com.haulmont.cuba.core.sys.javacl.test3;

public class OuterClass {
    private static class InnerClass {

    }

    public void init() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
    }
}

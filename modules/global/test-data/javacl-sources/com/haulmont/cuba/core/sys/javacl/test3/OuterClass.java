/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

/**
 *
 * @author degtyarjov
 * @version $Id$
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

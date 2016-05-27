/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.javacl.test2.pack1;

import com.haulmont.cuba.core.sys.javacl.test2.pack2.Dependency2Class;

public class DependencyClass {

    public void doSomething() {
        System.out.println("DependencyClass");
        new Dependency2Class().doSomething();
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl.test;

import com.haulmont.cuba.core.sys.javacl.test.pack1.SimpleClass1;
import com.haulmont.cuba.core.sys.javacl.test.pack2.SimpleClass2;

public class SimpleClass extends AbstractClass {
    SimpleClass1 simpleClass1 = new SimpleClass1();
    SimpleClass2 simpleClass2 = new SimpleClass2();

    public SimpleClass() {
        simpleClass1.setSimpleClass(this);
    }
}

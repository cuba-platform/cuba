/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: degtyarjov
 * Created: 14.03.13 14:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.javacl.test;

import com.haulmont.cuba.core.sys.javacl.test.pack1.SimpleClass1;
import com.haulmont.cuba.core.sys.javacl.test.pack2.SimpleClass2;

public class SimpleClass {
    SimpleClass1 simpleClass1 = new SimpleClass1();
    SimpleClass2 simpleClass2 = new SimpleClass2();

    public SimpleClass() {
        simpleClass1.setSimpleClass(this);
    }
}

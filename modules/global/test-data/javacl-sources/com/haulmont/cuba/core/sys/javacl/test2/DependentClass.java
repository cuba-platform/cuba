/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

/**
 *
 * @author degtyarjov
 * @version $Id$
 */
package com.haulmont.cuba.core.sys.javacl.test2;

import com.haulmont.cuba.core.sys.javacl.test2.pack1.*;

public class DependentClass {
    public void doSomething() {
        new DependencyClass().doSomething();
    }
}

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

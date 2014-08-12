/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl.test4.pack1;

import java.lang.String;

public class MainClass {
    public static interface MainInterface {

    }

    MainInterface object;

    public MainInterface getObject() {
        return object;
    }

    public void setObject(MainInterface object) {
        this.object = object;
    }
}

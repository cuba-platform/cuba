/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.dbupdate;

/**
* @author degtyarjov
* @version $Id$
*/
public enum ScriptType {
    INIT, UPDATE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

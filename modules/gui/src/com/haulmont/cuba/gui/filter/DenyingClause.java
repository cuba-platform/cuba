/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.filter;

public class DenyingClause extends Clause {

    public DenyingClause() {
        super("0<>0", null);
    }
}

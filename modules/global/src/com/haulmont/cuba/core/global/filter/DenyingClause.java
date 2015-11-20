/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global.filter;

public class DenyingClause extends Clause {

    public DenyingClause() {
        super("deny", "0<>0", null, null, null);
    }
}

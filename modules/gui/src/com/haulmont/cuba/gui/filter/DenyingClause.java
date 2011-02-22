/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.02.11 12:43
 *
 * $Id$
 */
package com.haulmont.cuba.gui.filter;

public class DenyingClause extends Clause {

    public DenyingClause() {
        super("0<>0", null);
    }
}

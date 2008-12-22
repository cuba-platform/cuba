/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 18:20:47
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

public class ViewNotFoundException extends RuntimeException
{
    private static final long serialVersionUID = -7372799415486288473L;

    public ViewNotFoundException(String message) {
        super(message);
    }
}

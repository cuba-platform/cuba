/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.app;

import java.io.Serializable;

/**
 * DTO for managing user's own time zone.
 *
* @author krivopustov
* @version $Id$
*/
public class UserTimeZone implements Serializable {

    public final String name;
    public final boolean auto;

    public UserTimeZone(String name, boolean auto) {
        this.name = name;
        this.auto = auto;
    }

    @Override
    public String toString() {
        return "timeZone=" + name + ", auto=" + auto;
    }
}

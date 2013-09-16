/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class SingleSecurityContextHolder implements SecurityContextHolder {

    private SecurityContext ctx;

    public SecurityContext get() {
        return ctx;
    }

    public void set(SecurityContext value) {
        ctx = value;
    }
}

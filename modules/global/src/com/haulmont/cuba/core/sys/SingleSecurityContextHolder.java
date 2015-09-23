/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

/**
 * @author krivopustov
 * @version $Id$
 */
public class SingleSecurityContextHolder implements SecurityContextHolder {

    private SecurityContext ctx;

    @Override
    public SecurityContext get() {
        return ctx;
    }

    @Override
    public void set(SecurityContext value) {
        ctx = value;
    }
}
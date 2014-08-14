/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.javacl;

import org.springframework.context.ApplicationContext;

/**
 * @author degtyarjov
 * @version $Id$
 */
public final class RemotingContextHolder {
    private RemotingContextHolder() {
    }

    private static volatile ApplicationContext remotingApplicationContext;

    public static ApplicationContext getRemotingApplicationContext() {
        return remotingApplicationContext;
    }

    public static void setRemotingApplicationContext(ApplicationContext remotingApplicationContext) {
        RemotingContextHolder.remotingApplicationContext = remotingApplicationContext;
    }
}

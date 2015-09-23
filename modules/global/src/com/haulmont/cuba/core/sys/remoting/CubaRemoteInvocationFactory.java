/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaRemoteInvocationFactory implements RemoteInvocationFactory {

    @Override
    public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        SecurityContext securityContext = AppContext.getSecurityContext();
        return new CubaRemoteInvocation(methodInvocation, securityContext == null ? null : securityContext.getSessionId());
    }
}
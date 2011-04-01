/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 15:34
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;

public class CubaRemoteInvocationFactory implements RemoteInvocationFactory {

    public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
        SecurityContext securityContext = AppContext.getSecurityContext();
        return new CubaRemoteInvocation(methodInvocation, securityContext == null ? null : securityContext.getSessionId());
    }
}

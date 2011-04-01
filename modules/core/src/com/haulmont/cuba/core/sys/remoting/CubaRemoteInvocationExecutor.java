/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 15:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationExecutor;

import java.lang.reflect.InvocationTargetException;

public class CubaRemoteInvocationExecutor implements RemoteInvocationExecutor {

    public Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (invocation instanceof CubaRemoteInvocation) {
            AppContext.setSecurityContext(new SecurityContext(((CubaRemoteInvocation) invocation).getSessionId()));
        }
        return invocation.invoke(targetObject);
    }
}

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 14:13:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.ejb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.security.SecurityAssociation;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.security.Principal;

public class ServiceInterceptor
{
    @AroundInvoke
    private Object aroundInvoke(InvocationContext ctx) throws Exception {
        Log log = LogFactory.getLog(ctx.getTarget().getClass());

        Principal principal = SecurityAssociation.getCallerPrincipal();
        char[] credential = (char[]) SecurityAssociation.getCredential();
        log.debug("Invoking method " + ctx.getMethod().getName() +
                ", user=" + principal.getName() + ", session=" + String.valueOf(credential));

        try {
            return ctx.proceed();
        } catch (Exception e) {
            log.error("ServiceInterceptor caught exception: ", e);
            throw e;
        }
    }
}

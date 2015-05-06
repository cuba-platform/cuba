/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.remoting;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;

import java.util.UUID;

/**
 * Encapsulates a remote invocation of a middleware service.
 * Additionally transfers the current user session identifier.
 *
 * @author krivopustov
 * @version $Id$
 */
public class CubaRemoteInvocation extends RemoteInvocation {

    private static final long serialVersionUID = 5460262566597755733L;

    private UUID sessionId;

    public CubaRemoteInvocation(MethodInvocation methodInvocation, UUID sessionId) {
        super(methodInvocation);
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }
}

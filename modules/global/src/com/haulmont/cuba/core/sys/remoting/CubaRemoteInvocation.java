/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 15:02
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.remoting;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.RemoteInvocation;

import java.util.UUID;

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

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2008 18:29:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.global.SecurityProvider;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.session.UserSessionManager;

import java.util.UUID;

import org.jboss.security.SecurityAssociation;

public class SecurityProviderImpl extends SecurityProvider
{
    protected UserSession __currentUserSession() {
        char[] credential = (char[]) SecurityAssociation.getCredential();
        if (credential == null)
            throw new SecurityException("No security context");

        UUID sessionId;
        try {
            sessionId = UUID.fromString(String.valueOf(credential));
        } catch (Exception e) {
            throw new SecurityException("Invalid session ID", e);
        }

        UserSession session = UserSessionManager.getInstance().getSession(sessionId);
        return session;
    }
}

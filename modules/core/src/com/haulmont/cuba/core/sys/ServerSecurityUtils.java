/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.01.2009 11:30:59
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

import java.security.Principal;
import java.util.UUID;

import com.haulmont.cuba.core.Utils;

public class ServerSecurityUtils
{
    private static final String DELIMITER = " : ";

    static {
        if (!Utils.isUnitTestMode())
            SecurityAssociation.setServer();
    }

    public static void setSecurityAssociation(String userName, UUID sessionId) {
        SecurityAssociation.setPrincipal(new SimplePrincipal(userName + DELIMITER + sessionId));
        SecurityAssociation.setCredential(null);
    }

    public static void setSecurityAssociation(String userName, String password) {
        SecurityAssociation.setPrincipal(new SimplePrincipal(userName));
        SecurityAssociation.setCredential(password);
    }

    public static UUID getSessionId() {
        if (Utils.isUnitTestMode())
            return UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        Principal principal = SecurityAssociation.getPrincipal();
        if (principal == null)
            return null;
        String str = principal.getName();
        int p = str.indexOf(DELIMITER);
        if (p <= 0)
            return null;
        try {
            UUID sessionId = UUID.fromString(str.substring(p + DELIMITER.length()));
            return sessionId;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String[] getUserInfo() {
        Principal principal = SecurityAssociation.getPrincipal();
        if (principal == null)
            return null;

        Object credential = SecurityAssociation.getCredential();
        if (credential == null || !(credential instanceof String))
            return null;

        String[] info = new String[2];
        info[0] = principal.getName();
        info[1] = (String) credential;
        return info;
    }
}

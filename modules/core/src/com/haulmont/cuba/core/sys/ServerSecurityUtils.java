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

import java.util.UUID;

import com.haulmont.cuba.core.Utils;

public class ServerSecurityUtils
{
    private static final String DELIMITER = " : ";

    public static void setSecurityAssociation(String userName, UUID sessionId) {
        AppContext.setSecurityContext(new SecurityContext(userName, null, sessionId));
//        SecurityAssociation.setPrincipal(new SimplePrincipal(userName + DELIMITER + sessionId));
//        SecurityAssociation.setCredential(null);
    }

    public static void setSecurityAssociation(String userName, String password) {
        AppContext.setSecurityContext(new SecurityContext(userName, password, null));
//        SecurityAssociation.setPrincipal(new SimplePrincipal(userName));
//        SecurityAssociation.setCredential(password);
    }

    public static void clearSecurityAssociation() {
        AppContext.setSecurityContext(null);
    }

    public static UUID getSessionId() {
        if (Utils.isUnitTestMode())
            return UUID.fromString("60885987-1b61-4247-94c7-dff348347f93");

        SecurityContext securityContext = AppContext.getSecurityContext();
        return securityContext == null ? null : securityContext.getSessionId();

//        Principal principal = SecurityAssociation.getPrincipal();
//        if (principal == null)
//            return null;
//        String str = principal.getName();
//        int p = str.indexOf(DELIMITER);
//        if (p <= 0)
//            return null;
//        try {
//            UUID sessionId = UUID.fromString(str.substring(p + DELIMITER.length()));
//            return sessionId;
//        } catch (RuntimeException e) {
//            return null;
//        }
    }

    public static SecurityContext getSecurityAssociation() {
        return AppContext.getSecurityContext();

//        Principal principal = SecurityAssociation.getPrincipal();
//        if (principal == null)
//            return null;
//
//        Object credential = SecurityAssociation.getCredential();
//        if (credential == null || !(credential instanceof String))
//            return null;
//
//        String[] info = new String[2];
//        info[0] = principal.getName();
//        info[1] = (String) credential;
//        return info;
    }
}

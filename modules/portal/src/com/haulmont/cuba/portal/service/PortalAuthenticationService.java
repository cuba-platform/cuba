/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.portal.service;

import com.haulmont.cuba.portal.security.PortalSession;

import javax.servlet.http.HttpSession;

/**
 * @author minaev
 * @version $Id$
 */
public interface PortalAuthenticationService {

    String NAME = "cuba_PortalAuthenticationService";

    /**
     * Internal portal authentication.
     * Assigns necessary roles by PortalSession which is already should be authenticated.
     *
     * @param portalSession PortalSession
     */
    void authenticate(PortalSession portalSession);

    /**
     * Logout user from webportal security context. Invalidates Portal session.
     *
     * @param session HttpSession
     */
    void logout(HttpSession session);

}

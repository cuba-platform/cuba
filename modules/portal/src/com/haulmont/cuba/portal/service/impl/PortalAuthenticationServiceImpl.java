/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.service.impl;

import com.haulmont.cuba.portal.App;
import com.haulmont.cuba.portal.security.PortalSession;
import com.haulmont.cuba.portal.sys.security.RoleGrantedAuthority;
import com.haulmont.cuba.portal.service.PortalAuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author minaev
 * @version $Id$
 */
@Service(PortalAuthenticationService.NAME)
public class PortalAuthenticationServiceImpl implements PortalAuthenticationService {

    @Override
    public void authenticate(PortalSession portalSession) {
        if (!portalSession.isAuthenticated()) {
            throw new RuntimeException("Portal session is not authenticated");
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(portalSession, portalSession.getId(),
                getRoleUserAuthorities(portalSession));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext(); //invalidate webportal security context
        App.getInstance().getConnection().logout(); //invalidate webtier session
    }

    private List<GrantedAuthority> getRoleUserAuthorities(PortalSession portalSession) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        if (!portalSession.isAuthenticated()) {
            return grantedAuthorities;
        } else {
            grantedAuthorities.add(new RoleGrantedAuthority());
        }
        return grantedAuthorities;
    }
}

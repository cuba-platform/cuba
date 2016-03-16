/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!portalSession.isAuthenticated()) {
            return grantedAuthorities;
        } else {
            grantedAuthorities.add(new RoleGrantedAuthority());
        }
        return grantedAuthorities;
    }
}
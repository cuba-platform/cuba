/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.12.2008 17:01:59
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class CubaHttpServletRequest extends HttpServletRequestWrapper
{
    private Principal principal;

    public CubaHttpServletRequest(HttpServletRequest httpServletRequest, Principal principal) {
        super(httpServletRequest);
        this.principal = principal;
    }

    public String getRemoteUser() {
        return principal.getName();
    }

    public Principal getUserPrincipal() {
        return principal;
    }

    public String getAuthType() {
        return principal != null ? "NTLM" : "none";
    }
}

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.12.2008 17:00:30
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.web.Configuration;
import jcifs.http.NtlmHttpFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public class CubaHttpFilter extends NtlmHttpFilter
{
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (Configuration.useNtlmAuthentication()) {
            HttpServletRequest req = (HttpServletRequest)request;
            HttpServletResponse resp = (HttpServletResponse)response;

            Principal principal;
            try {
                principal = negotiate(req, resp, false);
                if (principal == null)
                    return;
            } catch (Exception e) {
                principal = null;
            }
            chain.doFilter(new CubaHttpServletRequest(req, principal), response);
        }
        else {
            chain.doFilter(request, response);
        }
    }
}

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

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.web.WebConfig;
import jcifs.Config;
import jcifs.http.NtlmHttpFilter;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public class CubaHttpFilter extends NtlmHttpFilter
{
    public void init(FilterConfig filterConfig) throws ServletException {
        WebConfig config = ConfigProvider.getConfig(WebConfig.class);

        String s = config.getActiveDirectoryDomainController();
        if (!StringUtils.isBlank(s))
            Config.setProperty("jcifs.http.domainController", s);

        s = config.getActiveDirectoryDomain();
        if (!StringUtils.isBlank(s))
            Config.setProperty("jcifs.smb.client.domain", s);

        s = config.getActiveDirectoryUser();
        if (!StringUtils.isBlank(s))
            Config.setProperty("jcifs.smb.client.username", s);

        s = config.getActiveDirectoryPassword();
        if (!StringUtils.isBlank(s))
            Config.setProperty("jcifs.smb.client.password", s);

        super.init(filterConfig);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (ActiveDirectoryHelper.useActiveDirectory()) {
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

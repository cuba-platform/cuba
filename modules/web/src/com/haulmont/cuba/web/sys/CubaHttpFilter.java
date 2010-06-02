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
import com.haulmont.cuba.core.global.GlobalConfig;
import jespa.http.HttpSecurityService;
import jespa.security.SecurityProviderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CubaHttpFilter extends HttpSecurityService implements Filter
{
    private static Log log = LogFactory.getLog(CubaHttpFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        Map<String, String> properties = new HashMap<String, String>();

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            GlobalConfig config = ConfigProvider.getConfig(GlobalConfig.class);

            properties.put("jespa.bindstr", ActiveDirectoryHelper.getBindStr());
            properties.put("jespa.service.acctname", ActiveDirectoryHelper.getAcctName());
            properties.put("jespa.service.password", ActiveDirectoryHelper.getAcctPassword());
            properties.put("jespa.account.canonicalForm", "3");
            properties.put("jespa.log.path", config.getLogDir() + "/jespa.log");
            ActiveDirectoryHelper.fillFromSystemProperties(properties);
        }
        try {
            super.init(properties);
        } catch (SecurityProviderException e) {
            throw new ServletException(e);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            super.doFilter(request, response, chain);
        }
        else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}

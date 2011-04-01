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
import com.haulmont.cuba.web.WebConfig;
import jespa.http.HttpSecurityService;
import jespa.security.SecurityProviderException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubaHttpFilter extends HttpSecurityService implements Filter
{
    private static Log log = LogFactory.getLog(CubaHttpFilter.class);

    private List<String> bypassUrls = new ArrayList<String>();

    public void init(FilterConfig filterConfig) throws ServletException {
        Map<String, String> properties = new HashMap<String, String>();

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            properties.put("jespa.bindstr", ActiveDirectoryHelper.getBindStr());
            properties.put("jespa.service.acctname", ActiveDirectoryHelper.getAcctName());
            properties.put("jespa.service.password", ActiveDirectoryHelper.getAcctPassword());
            properties.put("jespa.account.canonicalForm", "3");
            properties.put("jespa.log.path", ConfigProvider.getConfig(GlobalConfig.class).getLogDir() + "/jespa.log");
            ActiveDirectoryHelper.fillFromSystemProperties(properties);

            String urls = ConfigProvider.getConfig(WebConfig.class).getCubaHttpFilterBypassUrls();
            String[] strings = urls.split("[, ]");
            for (String string : strings) {
                if (StringUtils.isNotBlank(string))
                    bypassUrls.add(string);
            }
        }
        try {
            super.init(properties);
        } catch (SecurityProviderException e) {
            throw new ServletException(e);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        request.setCharacterEncoding("UTF-8");

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            String requestURI = ((HttpServletRequest) request).getRequestURI();
            if (!requestURI.endsWith("/"))
                requestURI = requestURI + "/";

            boolean bypass = false;
            for (String bypassUrl : bypassUrls) {
                if (requestURI.contains(bypassUrl)) {
                    bypass = true;
                    break;
                }
            }
            if (!bypass)
                super.doFilter(request, response, chain);
            else
                chain.doFilter(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}

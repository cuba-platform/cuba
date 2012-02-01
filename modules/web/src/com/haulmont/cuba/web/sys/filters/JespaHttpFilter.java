/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.filters;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import jespa.http.HttpSecurityService;
import jespa.security.SecurityProviderException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@SuppressWarnings("unused")
public class JespaHttpFilter extends HttpSecurityService implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Map<String, String> properties = new HashMap<String, String>();

        properties.put("jespa.bindstr", ActiveDirectoryHelper.getBindStr());
        properties.put("jespa.service.acctname", ActiveDirectoryHelper.getAcctName());
        properties.put("jespa.service.password", ActiveDirectoryHelper.getAcctPassword());
        properties.put("jespa.account.canonicalForm", "3");
        properties.put("jespa.log.path", ConfigProvider.getConfig(GlobalConfig.class).getLogDir() + "/jespa.log");
        ActiveDirectoryHelper.fillFromSystemProperties(properties);

        try {
            super.init(properties);
        } catch (SecurityProviderException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
    }
}

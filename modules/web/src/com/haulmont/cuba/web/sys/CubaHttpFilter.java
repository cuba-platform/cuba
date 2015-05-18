/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.auth.ActiveDirectoryHelper;
import com.haulmont.cuba.web.auth.CubaAuthProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaHttpFilter implements Filter {
    private static Log log = LogFactory.getLog(CubaHttpFilter.class);

    protected List<String> bypassUrls = new ArrayList<>();
    protected CubaAuthProvider authProvider;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (ActiveDirectoryHelper.useActiveDirectory()) {
            try {
                authProvider = AppBeans.get(CubaAuthProvider.NAME);
                authProvider.init(filterConfig);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            // Fill bypassUrls
            Configuration configuration = AppBeans.get(Configuration.NAME);
            String urls = configuration.getConfig(WebConfig.class).getCubaHttpFilterBypassUrls();
            String[] strings = urls.split("[, ]");
            for (String string : strings) {
                if (StringUtils.isNotBlank(string)) {
                    bypassUrls.add(string);
                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");

        String requestURI = request.getRequestURI();

        boolean filtered = false;

        if (authProvider != null) {
            // Active Directory integration
            if (!requestURI.endsWith("/")) {
                requestURI = requestURI + "/";
            }

            boolean bypass = false;
            for (String bypassUrl : bypassUrls) {
                if (requestURI.contains(bypassUrl)) {
                    log.debug("Skip AD auth for by pass url: " + bypassUrl);
                    bypass = true;
                    break;
                }
            }

            filtered = filterByAuthProvider(request, response, chain, bypass);
        }

        if (!filtered) {
            handleNotFiltered(request, response, chain);
        }
    }

    @Override
    public void destroy() {
        if (authProvider != null) {
            authProvider.destroy();
        }
    }

    protected void handleNotFiltered(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    protected boolean filterByAuthProvider(HttpServletRequest request, HttpServletResponse response,
                                           FilterChain chain, boolean byPass) throws IOException, ServletException {
        if (!byPass) {
            authProvider.doFilter(request, response, chain);
            return true;
        }
        return false;
    }
}
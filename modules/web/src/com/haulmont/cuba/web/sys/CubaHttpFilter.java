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

    private List<String> bypassUrls = new ArrayList<>();
    private CubaAuthProvider activeDirectoryFilter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (ActiveDirectoryHelper.useActiveDirectory()) {
            try {
                activeDirectoryFilter = AppBeans.get(CubaAuthProvider.NAME);
                activeDirectoryFilter.init(filterConfig);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            // Fill bypassUrls
            String urls = AppBeans.get(Configuration.class).getConfig(WebConfig.class).getCubaHttpFilterBypassUrls();
            String[] strings = urls.split("[, ]");
            for (String string : strings) {
                if (StringUtils.isNotBlank(string))
                    bypassUrls.add(string);
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

        if (ActiveDirectoryHelper.useActiveDirectory()) {
            // Active Directory integration
            if (!requestURI.endsWith("/"))
                requestURI = requestURI + "/";

            boolean bypass = false;
            for (String bypassUrl : bypassUrls) {
                if (requestURI.contains(bypassUrl)) {
                    log.debug("Skip AD auth for by pass url: " + bypassUrl);
                    bypass = true;
                    break;
                }
            }
            if (!bypass) {
                if (activeDirectoryFilter.needAuth(request) || !checkApplicationSession(request)) {
                    activeDirectoryFilter.doFilter(request, response, chain);
                    filtered = true;
                }
            }
        }

        if (!filtered) {
            chain.doFilter(request, response);
        }
    }

    private boolean checkApplicationSession(HttpServletRequest request) {
        // vaadin7 ActiveDirectory
        /*if (request.getSession() == null)
            return false;

        final HttpSession session = request.getSession(true);
        if (session == null)
            return false;

        if (isWebResourcesRequest(request))
            return true;

        WebApplicationContext applicationContext = CubaApplicationContext.getExistingApplicationContext(session);
        if (applicationContext == null)
            return false;

        final Collection<Application> applications = applicationContext.getApplications();

        for (Application app : applications) {
            String appPath = app.getURL().getPath();

            String servletPath = request.getContextPath();
            if (!servletPath.equals("/"))
                servletPath += "/";

            if (servletPath.equals(appPath)) {
                if (app.isRunning() && (app instanceof AppUI)) {
                    if (((AppUI) app).getConnection().isConnected())
                        return true;
                }
            }
        }*/

        return false;
    }

    private boolean isWebResourcesRequest(HttpServletRequest request) {
        return (request.getRequestURI() != null) && (request.getRequestURI().contains("/VAADIN/"));
    }

    @Override
    public void destroy() {
        if (activeDirectoryFilter != null)
            activeDirectoryFilter.destroy();
    }
}
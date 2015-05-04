/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.WebStatisticsAccumulator;
import com.haulmont.cuba.web.auth.RequestContext;
import com.vaadin.server.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Main CUBA web-application servlet
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaApplicationServlet extends VaadinServlet {

    private static final long serialVersionUID = -8701539520754293569L;

    private Log log = LogFactory.getLog(CubaApplicationServlet.class);

    protected WebConfig webConfig;

    //private StatisticsCounter statisticsCounter;
    private WebStatisticsAccumulator statisticsCounter;

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        CubaVaadinServletService service = new CubaVaadinServletService(this, deploymentConfiguration);
        service.init();
        return service;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        webConfig = configuration.getConfig(WebConfig.class);
        statisticsCounter = AppBeans.get(WebStatisticsAccumulator.class);

        super.init(servletConfig);
    }

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        getService().addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                CubaBootstrapListener bootstrapListener = AppBeans.get(CubaBootstrapListener.NAME);

                event.getSession().addBootstrapListener(bootstrapListener);
            }
        });
    }

    @Override
    protected DeploymentConfiguration createDeploymentConfiguration(Properties initParameters) {
        int sessionExpirationTimeout = webConfig.getHttpSessionExpirationTimeoutSec();
        int sessionPingPeriod = sessionExpirationTimeout / 3;

        if (sessionPingPeriod > 0) {
            // configure Vaadin heartbeat according to web config
            initParameters.setProperty(Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL, String.valueOf(sessionPingPeriod));
        }

        return super.createDeploymentConfiguration(initParameters);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String contextName = request.getContextPath().length() == 0 ? "" : request.getContextPath().substring(1);

        if (request.getParameter("restartApp") != null) {
            request.getSession().invalidate();
            response.sendRedirect(requestURI);
            return;
        }

        String[] uriParts = requestURI.split("/");
        String action = null;

        if (uriParts.length > 0) {
            String lastPart = uriParts[uriParts.length - 1];

            if (webConfig.getLoginAction().equals(lastPart) || webConfig.getLinkHandlerActions().contains(lastPart)) {
                action = lastPart;
            }
        }

        boolean needRedirect = action != null;
        if (needRedirect) {
            redirectToApp(request, response, contextName, uriParts, action);
        } else {
            serviceAppRequest(request, response);
        }
    }

    protected void redirectToApp(HttpServletRequest request, HttpServletResponse response,
                                 String contextName, String[] uriParts, String action) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uriParts.length; i++) {
            sb.append(uriParts[i]);
            if (uriParts[i].equals(contextName)) {
                break;
            }
            if (i < uriParts.length - 1)
                sb.append("/");
        }

        HttpSession httpSession = request.getSession();
        if (action != null) {
            httpSession.setAttribute(AppUI.LAST_REQUEST_ACTION_ATTR, action);
        }
        if (request.getParameterNames().hasMoreElements()) {
            Map<String, String> params = new HashMap<>();
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = (String) parameterNames.nextElement();
                params.put(name, request.getParameter(name));
            }
            httpSession.setAttribute(AppUI.LAST_REQUEST_PARAMS_ATTR, params);
        }

        statisticsCounter.incWebRequestsCount();
        log.debug("Redirect to application " + httpSession.getId());
        response.addCookie(new Cookie("JSESSIONID", httpSession.getId()));
        response.sendRedirect(sb.toString());
    }

    protected void serviceAppRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestContext.create(request, response);
        AppContext.setSecurityContext(new VaadinSessionAwareSecurityContext());
        statisticsCounter.incWebRequestsCount();

        long startTs = System.currentTimeMillis();

        try {
            super.service(request, response);
        } finally {
            RequestContext.destroy();
            AppContext.setSecurityContext(null);
        }

        long t = System.currentTimeMillis() - startTs;
        if (t > (webConfig.getLogLongRequestsThresholdSec() * 1000)) {
            log.warn(String.format("Too long request processing [%d ms]: ip=%s, url=%s",
                    t, request.getRemoteAddr(), request.getRequestURI()));
        }
    }
}
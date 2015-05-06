/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * Executes {@link CubaRemoteInvocation} on middleware, setting and clearing a {@link SecurityContext} in the request
 * handling thread.
 *
 * @author krivopustov
 * @version $Id$
 */
public class CubaRemoteInvocationExecutor implements RemoteInvocationExecutor {

    private Log log = LogFactory.getLog(CubaRemoteInvocationExecutor.class);

    private UserSessionManager userSessionManager;

    private volatile ClusterInvocationSupport clusterInvocationSupport;

    private Configuration configuration;

    public CubaRemoteInvocationExecutor() {
        userSessionManager = AppBeans.get("cuba_UserSessionManager");
        configuration = AppBeans.get(Configuration.NAME);
    }

    @Override
    public Object invoke(RemoteInvocation invocation, Object targetObject)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (invocation instanceof CubaRemoteInvocation) {
            UUID sessionId = ((CubaRemoteInvocation) invocation).getSessionId();
            if (sessionId != null) {
                UserSession session = userSessionManager.findSession(sessionId);
                if (session == null) {
                    String sessionProviderUrl = configuration.getConfig(ServerConfig.class).getUserSessionProviderUrl();
                    if (StringUtils.isNotBlank(sessionProviderUrl)) {
                        log.debug("User session " + sessionId + " not found, trying to get it from " + sessionProviderUrl);
                        try {
                            HttpServiceProxy proxyFactory = new HttpServiceProxy(
                                    getClusterInvocationSupport(sessionProviderUrl));
                            proxyFactory.setServiceUrl("cuba_LoginService");
                            proxyFactory.setServiceInterface(LoginService.class);
                            proxyFactory.afterPropertiesSet();
                            LoginService loginService = (LoginService) proxyFactory.getObject();
                            if (loginService != null) {
                                UserSession userSession = loginService.getSession(sessionId);
                                if (userSession != null) {
                                    userSessionManager.storeSession(userSession);
                                } else {
                                    log.debug("User session " + sessionId + " not found on " + sessionProviderUrl);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Error getting user session from " + sessionProviderUrl, e);
                        }
                    }
                }
                AppContext.setSecurityContext(new SecurityContext(sessionId));
            }
        }
        Object result = invocation.invoke(targetObject);
        AppContext.setSecurityContext(null);
        return result;
    }

    private ClusterInvocationSupport getClusterInvocationSupport(String sessionProviderUrl) {
        if (clusterInvocationSupport == null) {
            synchronized (this) {
                if (clusterInvocationSupport == null) {
                    ClusterInvocationSupport result = new ClusterInvocationSupport();
                    result.setBaseUrl(sessionProviderUrl);
                    result.init();
                    clusterInvocationSupport = result;
                }
            }
        }
        return clusterInvocationSupport;
    }
}
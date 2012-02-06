/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationExecutor;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CubaRemoteInvocationExecutor implements RemoteInvocationExecutor {

    private Log log = LogFactory.getLog(CubaRemoteInvocationExecutor.class);

    private UserSessionManager userSessionManager;

    private ClusterInvocationSupport clusterInvocationSupport;

    public CubaRemoteInvocationExecutor() {
        userSessionManager = Locator.lookup("cuba_UserSessionManager");
    }

    public Object invoke(RemoteInvocation invocation, Object targetObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (invocation instanceof CubaRemoteInvocation) {
            UUID sessionId = ((CubaRemoteInvocation) invocation).getSessionId();
            if (sessionId != null) {
                UserSession session = userSessionManager.findSession(sessionId);
                if (session == null) {
                    String sessionProviderUrl = ConfigProvider.getConfig(ServerConfig.class).getUserSessionProviderUrl();
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
        return invocation.invoke(targetObject);
    }

    private ClusterInvocationSupport getClusterInvocationSupport(String sessionProviderUrl) {
        if (clusterInvocationSupport == null) {
            clusterInvocationSupport = new ClusterInvocationSupport();
            clusterInvocationSupport.setBaseUrl(sessionProviderUrl);
            clusterInvocationSupport.init();
        }
        return clusterInvocationSupport;
    }
}

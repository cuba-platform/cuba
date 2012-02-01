/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.filters;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.web.WebConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ietf.jgss.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedAction;

/**
 * @author artamonov
 * @version $Id$
 */
public class KerberosHttpFilter implements Filter {

    private Log log = LogFactory.getLog(KerberosHttpFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        // setup system properties
        System.setProperty("sun.security.krb5.debug", "false");
        System.setProperty("java.security.krb5.conf", webConfig.getKerberosConf());
        System.setProperty("java.security.krb5.realm", webConfig.getKerberosRealm());
        System.setProperty("java.security.krb5.kdc", webConfig.getKerberosKeyCenter());
        System.setProperty("java.security.auth.login.config", webConfig.getKerberosJaasConf());
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String auth = request.getHeader("Authorization");
        if (auth == null) {
            // Request auth credentials
            response.reset();
            response.setHeader("WWW-Authenticate", "Negotiate");
            response.setContentLength(0);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.flushBuffer();
        } else {
            try {
                String authString = auth.substring("Negotiate ".length());
                final byte[] kerberosToken = Base64.decodeBase64(authString.getBytes());
                
                Subject serviceSubject = createSeviceSubject();
                if (serviceSubject != null) {
                    final GSSName clientName = Subject.doAs(serviceSubject, new PrivilegedAction<GSSName>() {
                        @Override
                        public GSSName run() {
                            try {
                                GSSManager manager = GSSManager.getInstance();
                                GSSContext context = manager.createContext((GSSCredential) null);
                                context.acceptSecContext(kerberosToken, 0, kerberosToken.length);
                                return context.getSrcName();
                            } catch (GSSException e) {
                                log.info("Unable to login user", e);
                                return null;
                            }
                        }
                    });
                    if (clientName != null) {
                        filterChain.doFilter(new HttpServletRequestWrapper(request) {
                            @Override
                            public Principal getUserPrincipal() {
                                return new KerberosPrincipal(clientName);
                            }
                        }, response);
                    }
                } else
                    throw new Exception("Unable to obtain kerberos service context");
                
            } catch (Exception e) {
                log.info("Exception while authetification", e);
            }
        }
    }
    
    private Subject createSeviceSubject() throws LoginException {
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        LoginContext loginContext = new LoginContext(webConfig.getKerberosLoginModule(),
                new ServiceLoginCallbackHandler(webConfig.getServicePrincipalPass()));
        loginContext.login();
        return loginContext.getSubject();
    }

    private static class ServiceLoginCallbackHandler implements CallbackHandler {

        private String password;

        private ServiceLoginCallbackHandler(String password) {
            this.password = password;
        }

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof PasswordCallback) {
                    PasswordCallback passwordCallback = (PasswordCallback) callback;
                    passwordCallback.setPassword(password.toCharArray());
                } else
                    throw new UnsupportedCallbackException(callback, "Unrecognized login callback");
            }
        }
    }

    private static class KerberosPrincipal implements Principal {

        private String name;

        private KerberosPrincipal(GSSName clientName) {
            String clientString = clientName.toString().toLowerCase();
            if (clientString.contains("@")) {
                String[] nameParts = StringUtils.split(clientString, "@");
                String userName = nameParts[0];
                String domain = nameParts[1];
                this.name = domain + "\\" + userName;
            } else
                this.name = clientString;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public void destroy() {
    }
}

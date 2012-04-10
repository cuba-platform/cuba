/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.auth;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ietf.jgss.*;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class KerberosAuthProvider implements CubaAuthProvider {

    private static final String AD_INTEGRATION_SUPPORT = "ADIntegrationSupport";
    private Log log = LogFactory.getLog(KerberosAuthProvider.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
        // Setup system properties
        System.setProperty("sun.security.krb5.debug", "false");
        System.setProperty("java.security.krb5.conf", StringUtils.trim(webConfig.getKerberosConf()));
        System.setProperty("java.security.auth.login.config", StringUtils.trim(webConfig.getKerberosJaasConf()));
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Filter unsupported sessions
        if (Boolean.FALSE.equals(request.getSession().getAttribute(AD_INTEGRATION_SUPPORT))) {
            filterChain.doFilter(request, response);
            return;
        }

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
                final String authString = auth.substring("Negotiate ".length());

                WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);
                LoginContext loginContext = new LoginContext(StringUtils.trim(webConfig.getKerberosJaasConf()),
                        new CallbackHandler() {
                            @Override
                            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                                // Do nothing,
                                // We are use keytab file, no password needed
                                // Principal specified in jaas.conf
                            }
                        });

                // Login as Service Principal
                loginContext.login();

                try {
                    Subject serviceSubject = loginContext.getSubject();
                    if (serviceSubject != null) {
                        final GSSName clientName = authentication(serviceSubject, authString);
                        if (clientName != null) {
                            // New User Principal for request
                            HttpServletRequestWrapper securedRequest = new HttpServletRequestWrapper(request) {
                                @Override
                                public Principal getUserPrincipal() {
                                    return new KerberosPrincipal(clientName);
                                }
                            };
                            log.debug("Success auth: " + securedRequest.getUserPrincipal().getName());

                            // Proceed filter with new User Principal with auth name
                            filterChain.doFilter(securedRequest, response);
                        } else {
                            // Allow user to login normally
                            // Mark session, unsupported AD flag
                            request.getSession().setAttribute(AD_INTEGRATION_SUPPORT, Boolean.FALSE);
                            filterChain.doFilter(request, response);
                        }
                    } else
                        throw new LoginException("Null serviceSubject returned from LoginContext");
                } finally {
                    loginContext.logout();
                }

            } catch (LoginException le) {
                log.error("Unabled to login service subject: ", le);
            } catch (Exception e) {
                log.debug("Exception while authetification", e);
            }
        }
    }

    /**
     * Acquire client context in priveleged action with Seprvice Principal permissions
     *
     * @param serviceSubject Service Principal subject
     * @param authString     Client auth string
     * @return Client name
     */
    private GSSName authentication(Subject serviceSubject, final String authString) {
        final byte[] kerberosToken = Base64.decodeBase64(authString.getBytes());
        return Subject.doAs(serviceSubject, new PrivilegedAction<GSSName>() {
            @Override
            public GSSName run() {
                try {
                    GSSManager manager = GSSManager.getInstance();
                    GSSContext context = manager.createContext((GSSCredential) null);
                    context.acceptSecContext(kerberosToken, 0, kerberosToken.length);
                    return context.getSrcName();
                } catch (GSSException e) {
                    log.debug("Unable to login user with token: " + authString, e);
                    return null;
                }
            }
        });
    }

    @Override
    public void authenticate(String login, String password, Locale loc)
            throws com.haulmont.cuba.security.global.LoginException {
        WebConfig webConfig = ConfigProvider.getConfig(WebConfig.class);

        // Convert domain name to kerberos form "user@DOMAIN"
        int slashPos = login.indexOf("\\");
        if (slashPos >= 0) {
            // Specify realms in krb5.ini in UPPER_CASE only
            String domain = login.substring(0, slashPos).toUpperCase();
            String userName = login.substring(slashPos + 1);
            login = userName + "@" + domain;
        }

        LoginContext loginContext;
        try {
            loginContext = new LoginContext(webConfig.getKerberosAuthModule(),
                    new ClientLoginCallbackHandler(login, password));

            loginContext.login();

            // Check client subject
            Subject clientSubject = loginContext.getSubject();

            loginContext.logout();

            if (clientSubject == null)
                throw new LoginException("Couldn't initialize subject for this login");

        } catch (LoginException e) {
            log.debug("Fail Login: " + login, e);

            throw new com.haulmont.cuba.security.global.LoginException(
                    MessageProvider.getMessage(ActiveDirectoryHelper.class, "activeDirectory.authenticationError", loc),
                    e.getMessage());
        }
    }

    /**
     * Replace principal in request with Kerberos auth principal
     */
    private static class KerberosPrincipal implements Principal {

        private String name;

        private KerberosPrincipal(GSSName clientName) {
            // Convert kerberos name to ntlm form "DOMAIN\\user"
            String clientString = clientName.toString().toLowerCase();
            int delimeterIndex = clientString.indexOf("@");
            if (delimeterIndex >= 0) {
                String userName = clientString.substring(0, delimeterIndex);
                String domain = clientString.substring(delimeterIndex + 1);
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

    /**
     * Handle user and password request while authenticate
     */
    private static class ClientLoginCallbackHandler implements CallbackHandler {

        private String login;

        private String password;

        public ClientLoginCallbackHandler(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    NameCallback nc = (NameCallback) callback;
                    nc.setName(login);
                } else if (callback instanceof PasswordCallback) {
                    PasswordCallback pc = (PasswordCallback) callback;
                    pc.setPassword(password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
                }
            }
        }
    }
}

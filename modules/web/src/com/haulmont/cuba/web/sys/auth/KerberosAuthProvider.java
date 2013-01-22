/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys.auth;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.ActiveDirectoryHelper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ietf.jgss.*;

import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.*;
import javax.servlet.http.*;
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
    private static final String WINDOWS_USER_AGENT = "Windows";
    private static final String KERBEROS_PRINCIPAL_KEY = "KerberosPrincipal";

    private Log log = LogFactory.getLog(KerberosAuthProvider.class);

    @Inject
    private Configuration configuration;

    @Inject
    private Messages messages;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        // Setup system properties
        System.setProperty("sun.security.krb5.debug", Boolean.toString(webConfig.getActiveDirectoryDebug()));
        System.setProperty("sun.security.jgss.debug", Boolean.toString(webConfig.getActiveDirectoryDebug()));
        System.setProperty("sun.security.krb5.msinterop.kstring", "true");
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
        HttpSession httpSession = request.getSession();
        if (Boolean.FALSE.equals(httpSession.getAttribute(AD_INTEGRATION_SUPPORT))) {
            filterChain.doFilter(request, response);
            return;
        }

        // Filter unsupported User-Agent
        String userAgent = request.getHeader("User-Agent");
        if (!StringUtils.contains(userAgent, WINDOWS_USER_AGENT)) {
            // Unsupported User-Agent, use default auth
            markUnsupportedSession(request);
            filterChain.doFilter(request, response);
            return;
        }

        // Filter localhost
        if (StringUtils.equals(request.getRemoteAddr(), request.getLocalAddr())) {
            log.debug("Skip local connection");
            markUnsupportedSession(request);
            filterChain.doFilter(request, response);
            return;
        }

        // Filter redirect
        final Object principal = httpSession.getAttribute(KERBEROS_PRINCIPAL_KEY);
        if (principal instanceof KerberosPrincipal) {
            log.trace("Skip redirect to application for " + httpSession.getId());
            HttpServletRequestWrapper securedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public Principal getUserPrincipal() {
                    return (Principal) principal;
                }
            };
            filterChain.doFilter(securedRequest, response);
            return;
        }

        // Need auth
        String auth = request.getHeader("Authorization");
        if (auth == null) {
            requestCredentials(request, response);
        } else {
            kerberosAuth(filterChain, request, response, auth);
        }
    }

    /**
     * Allow user to login normally
     * Mark session, unsupported AD flag
     *
     * @param request Request
     */
    private void markUnsupportedSession(HttpServletRequest request) {
        log.debug("Mark unsupported session: " + request.getSession().getId() + " for " + getRemoteComputerPlace(request));
        request.getSession().setAttribute(AD_INTEGRATION_SUPPORT, Boolean.FALSE);
    }

    /**
     * Send negotiation to client
     *
     * @param request  Request
     * @param response Response
     * @throws IOException IO exception
     */
    private void requestCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Request auth credentials
        response.reset();
        // save session id on next request
        response.addCookie(new Cookie("JSESSIONID", request.getSession().getId()));
        response.setHeader("WWW-Authenticate", "Negotiate");
        response.setContentLength(0);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.flushBuffer();

        log.trace("Request Kerberos ticket for " + request.getSession().getId()
                + " " + getRemoteComputerPlace(request));
    }

    /**
     * Kerberos Auth proceedure
     *
     * @param filterChain Filter chain
     * @param request     HttpRequest
     * @param response    HttpResponse
     * @param auth        Auth header
     * @throws ServletException Exception in filter chain
     */
    private synchronized void kerberosAuth(FilterChain filterChain,
                                           final HttpServletRequest request, HttpServletResponse response, String auth)
            throws ServletException {

        try {
            LoginContext loginContext = loginAsServicePrincipal();
            try {
                Subject serviceSubject = loginContext.getSubject();
                if (serviceSubject != null) {
                    final String authString = auth.substring("Negotiate ".length());

                    if (authString != null)
                        log.trace("Ticket size: " + authString.length());

                    final GSSName clientName = acquireClientInfo(serviceSubject, request, authString);
                    if (clientName != null) {
                        HttpServletRequestWrapper securedRequest = initPrincipal(request, clientName);
                        // Proceed filter with new User Principal with auth name
                        filterChain.doFilter(securedRequest, response);
                    } else {
                        markUnsupportedSession(request);
                        filterChain.doFilter(request, response);
                    }
                } else
                    throw new LoginException("Null serviceSubject returned from LoginContext");
            } finally {
                loginContext.logout();
            }
        } catch (LoginException le) {
            log.error("Unabled to login service subject: ", le);
        } catch (ServletException se) {
            throw se;
        } catch (Exception e) {
            log.debug("Exception while authetification", e);
        }
    }

    /**
     * Init user principal for Active directory user
     *
     * @param request    Http request
     * @param clientName Active directory user name
     * @return Enhanced request
     */
    private HttpServletRequestWrapper initPrincipal(final HttpServletRequest request, final GSSName clientName) {
        // New User Principal for request
        HttpServletRequestWrapper securedRequest = new HttpServletRequestWrapper(request) {
            @Override
            public Principal getUserPrincipal() {
                return new KerberosPrincipal(clientName);
            }
        };
        HttpSession httpSession = request.getSession();

        log.debug("Success auth " + httpSession.getId() +
                " " + securedRequest.getUserPrincipal().getName() +
                " from " + getRemoteComputerPlace(request));

        httpSession.setAttribute(KERBEROS_PRINCIPAL_KEY, securedRequest.getUserPrincipal());
        return securedRequest;
    }

    /**
     * Login as Service Principal
     *
     * @return Login contxext
     * @throws LoginException Kerberos login exception
     */
    private LoginContext loginAsServicePrincipal() throws LoginException {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        LoginContext loginContext = new LoginContext(StringUtils.trim(webConfig.getKerberosLoginModule()),
                new CallbackHandler() {
                    @Override
                    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                        // Do nothing,
                        // We are using keytab file, no password needed
                        // Principal specified in jaas.conf
                    }
                });
        loginContext.login();
        return loginContext;
    }

    /**
     * Acquire client context in priveleged action with Seprvice Principal permissions
     *
     * @param serviceSubject Service Principal subject
     * @param request        Http request
     * @param authString     Client auth string
     * @return Client name
     */
    private GSSName acquireClientInfo(Subject serviceSubject,
                                      final ServletRequest request, final String authString) {
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
                    log.warn("Unable to login user with token from " +
                            getRemoteComputerPlace(request) +
                            "\n" + authString, e);
                    return null;
                }
            }
        });
    }

    @Override
    public void authenticate(String login, String password, Locale loc)
            throws com.haulmont.cuba.security.global.LoginException {
        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        DomainAliasesResolver aliasesResolver = AppBeans.get(DomainAliasesResolver.NAME);

        // Convert domain name to kerberos form "user@DOMAIN"
        // Specify realms in krb5.ini in UPPER_CASE only
        int slashPos = login.indexOf("\\");
        if (slashPos >= 0) {
            String domainAlias = login.substring(0, slashPos);
            String domain = aliasesResolver.getDomainName(domainAlias).toUpperCase();
            String userName = login.substring(slashPos + 1);
            login = userName + "@" + domain;
        } else {
            int atSignPos = login.indexOf("@");
            if (atSignPos <= 0) {
                throw new com.haulmont.cuba.security.global.LoginException(
                        messages.getMessage(ActiveDirectoryHelper.class, "activeDirectory.invalidName", loc),
                        login
                );
            }

            String domainAlias = login.substring(atSignPos + 1);
            String domain = aliasesResolver.getDomainName(domainAlias).toUpperCase();
            String userName = login.substring(0, atSignPos);
            login = userName + "@" + domain;
        }

        LoginContext loginContext;
        try {
            loginContext = new LoginContext(StringUtils.trim(webConfig.getKerberosAuthModule()),
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
                    messages.getMessage(ActiveDirectoryHelper.class, "activeDirectory.authenticationError", loc),
                    e.getMessage());
        }
    }

    @Override
    public boolean needAuth(ServletRequest request) {
        return false;
    }

    @Override
    public boolean authSupported(HttpSession session) {
        final Object principal = session.getAttribute(KERBEROS_PRINCIPAL_KEY);
        return principal instanceof KerberosPrincipal;
    }

    private String getRemoteComputerPlace(ServletRequest request) {
        return request.getRemoteAddr();
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
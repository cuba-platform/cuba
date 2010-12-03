/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 24.11.2010 21:21:52
 *
 * $Id: CasAuthenticationFilter.java 3252 2010-11-25 12:30:12Z gorodnov $
 */
package com.haulmont.cuba.web.sys;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

public class CasAuthenticationFilter extends AbstractCasFilter {

    private static final String AJAX_UIDL_URI = "/UIDL";

    /**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;

    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();

    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));
            log.trace("Loaded CasServerLoginUrl parameter: " + this.casServerLoginUrl);
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            log.trace("Loaded renew parameter: " + this.renew);
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            log.trace("Loaded gateway parameter: " + this.gateway);

            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
                } catch (final Exception e) {
                    log.error(e,e);
                    throw new ServletException(e);
                }
            }
        }
    }

    public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final Assertion assertion = getAssertion(request);

        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String serviceUrl = constructServiceUrl(request, response);
        final String ticket = CommonUtils.safeGetParameter(request,getArtifactParameterName());
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

        String modifiedServiceUrl;

        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("no ticket and no assertion found");
        if (this.gateway) {
            log.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        if (log.isDebugEnabled()) {
            log.debug("Constructed service url: " + modifiedServiceUrl);
        }

        if (isStaticResourceRequest(request)) {
            if (log.isDebugEnabled()) {
                log.debug("Statis resource request");
            }
            return;
        }

        if (isUIDLRequest(request)) {
            modifiedServiceUrl = modifiedServiceUrl.substring(0, modifiedServiceUrl.indexOf(AJAX_UIDL_URI) + 1);
            final String urlToRedirectTo = CommonUtils.constructRedirectUrl(
                    this.casServerLoginUrl,
                    getServiceParameterName(),
                    modifiedServiceUrl,
                    this.renew,
                    this.gateway
            );

            if (log.isDebugEnabled()) {
                log.debug("UIDL request, redirecting to \"" + urlToRedirectTo + "\" by Ajax");
            }
            sendRedirectionToLoginByAjax(request, response, urlToRedirectTo);
            return;
        }

        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(
                this.casServerLoginUrl,
                getServiceParameterName(),
                modifiedServiceUrl,
                this.renew,
                this.gateway
        );

        if (log.isDebugEnabled()) {
            log.debug("redirecting to \"" + urlToRedirectTo + "\"");
        }

        sendRedirectionToLogin(request, response, urlToRedirectTo);
    }

    private boolean isUIDLRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo != null && (pathInfo.startsWith(AJAX_UIDL_URI + "/")
                || pathInfo.endsWith(AJAX_UIDL_URI));
    }

    private boolean isStaticResourceRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 10) {
            return false;
        }

        if ((request.getContextPath() != null)
                && (request.getRequestURI().startsWith("/VAADIN/"))) {
            return true;
        } else if (request.getRequestURI().startsWith(
                request.getContextPath() + "/VAADIN/")) {
            return true;
        }

        return false;
    }

    protected Assertion getAssertion(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;
        return assertion;
    }

    protected void sendRedirectionToLoginByAjax(
            HttpServletRequest request,
            HttpServletResponse response,
            String redirectionUrl
    ) throws IOException {
        final OutputStream out = response.getOutputStream();
        response.setContentType("application/json; charset=UTF-8");
        final PrintWriter outWriter = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(out, "UTF-8")));
        outWriter.print("for(;;);[{");
        outWriter.print("\"redirect\":{");
        outWriter.write("\"url\":\"" + redirectionUrl + "\"");
        outWriter.write(", \"top\":\"true\"");
        outWriter.write("}}]");
        outWriter.flush();
        outWriter.close();
        out.flush();
    }

    protected void sendRedirectionToLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            String redirectionUrl
    ) throws IOException, ServletException {
        response.sendRedirect(redirectionUrl);
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public boolean isGateway() {
        return gateway;
    }

    public void setGateway(boolean gateway) {
        this.gateway = gateway;
    }

    public GatewayResolver getGatewayStorage() {
        return gatewayStorage;
    }

    public void setGatewayStorage(GatewayResolver gatewayStorage) {
        this.gatewayStorage = gatewayStorage;
    }

    public boolean isRenew() {
        return renew;
    }

    public void setRenew(boolean renew) {
        this.renew = renew;
    }
}

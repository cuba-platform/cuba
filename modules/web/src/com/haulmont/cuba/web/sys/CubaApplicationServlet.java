/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.sys;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.app.WebStatisticsAccumulator;
import com.vaadin.server.*;
import com.vaadin.shared.ApplicationConstants;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Main CUBA web-application servlet.
 */
public class CubaApplicationServlet extends VaadinServlet {
    public static final String INTERNAL_ERROR_TEXT = "Internal Server Error. Please contact system administrator";

    private static final long serialVersionUID = -8701539520754293569L;

    public static final String FROM_HTML_REDIRECT_PARAM = "fromCubaHtmlRedirect";
    private static final String REDIRECT_PAGE_TEMPLATE_PATH = "/com/haulmont/cuba/web/sys/redirect-page-template.html";

    private final Logger log = LoggerFactory.getLogger(CubaApplicationServlet.class);

    protected WebConfig webConfig;
    protected Resources resources;

    protected WebStatisticsAccumulator statisticsCounter;

    protected volatile ClassLoader classLoader;

    /*
     * The field is used to prevent double initialization of the servlet.
     * Double initialization might occur during single WAR deployment when we call the method from initializer.
     */
    protected volatile boolean initialized = false;

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        CubaVaadinServletService service = new CubaVaadinServletService(this, deploymentConfiguration);
        if (classLoader != null) {
            service.setClassLoader(classLoader);
        }
        service.init();
        return service;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        if (!initialized) {
            Configuration configuration = AppBeans.get(Configuration.NAME);
            webConfig = configuration.getConfig(WebConfig.class);
            statisticsCounter = AppBeans.get(WebStatisticsAccumulator.class);
            resources = AppBeans.get(Resources.class);

            if (configuration.getConfig(GlobalConfig.class).getTestMode()) {
                System.setProperty(getPackageName() + ".disable-xsrf-protection", "true");
            }

            super.init(servletConfig);
            initialized = true;
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();

        getService().addSessionInitListener(event -> {
            BootstrapListener bootstrapListener = AppBeans.get(CubaBootstrapListener.NAME);
            event.getSession().addBootstrapListener(bootstrapListener);
        });
    }

    @Override
    protected DeploymentConfiguration createDeploymentConfiguration(Properties initParameters) {
        int sessionExpirationTimeout = webConfig.getHttpSessionExpirationTimeoutSec();
        int sessionPingPeriod = sessionExpirationTimeout / 3;

        if (Strings.isNullOrEmpty(initParameters.getProperty(Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL))) {
            if (sessionPingPeriod > 0) {
                // configure Vaadin heartbeat according to web config
                initParameters.setProperty(Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL, String.valueOf(sessionPingPeriod));
            }
        }

        if (Strings.isNullOrEmpty(initParameters.getProperty(Constants.PARAMETER_WIDGETSET))) {
            String widgetSet = webConfig.getWidgetSet();
            initParameters.setProperty(Constants.PARAMETER_WIDGETSET, widgetSet);
        }

        if (Strings.isNullOrEmpty(initParameters.getProperty(Constants.SERVLET_PARAMETER_PRODUCTION_MODE))) {
            boolean productionMode = webConfig.getProductionMode();
            if (productionMode) {
                initParameters.setProperty(Constants.SERVLET_PARAMETER_PRODUCTION_MODE, String.valueOf(true));
            }
        }

        if (Strings.isNullOrEmpty(initParameters.getProperty(Constants.SERVLET_PARAMETER_UI_PROVIDER))) {
            initParameters.setProperty(Constants.SERVLET_PARAMETER_UI_PROVIDER, CubaUIProvider.class.getCanonicalName());
        }

        if (Strings.isNullOrEmpty(initParameters.getProperty(VaadinSession.UI_PARAMETER))) {
            // not actually used by CubaUIProvider
            initParameters.setProperty(VaadinSession.UI_PARAMETER, AppUI.class.getCanonicalName());
        }

        return super.createDeploymentConfiguration(initParameters);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (handleContextRootWithoutSlash(request, response)) {
                return;
            }

            String requestURI = request.getRequestURI();

            if (request.getParameter("restartApp") != null) {
                try {
                    request.getSession().invalidate();
                } catch (Exception e) {
                    // Vaadin listens to invalidate of web session and can throw exceptions during invalildate() call
                    log.debug("Exception during session invalidation", e);
                } finally {
                    // always send redirect to client
                    response.sendRedirect(requestURI);
                }
                return;
            }

            String[] uriParts = requestURI.split("/");
            String action = getTargetAction(uriParts);

            boolean needRedirect = action != null;
            if (needRedirect) {
                if (webConfig.getUseRedirectWithBlankPageForLinkAction() &&
                        request.getParameter(FROM_HTML_REDIRECT_PARAM) == null) {
                    redirectWithBlankHtmlPage(request, response);
                } else {
                    String contextName = request.getContextPath().length() == 0 ?
                            "" : request.getContextPath().substring(1);

                    redirectToApp(request, response, contextName, uriParts, action);
                }
            } else {
                serviceAppRequest(request, response);
            }
        } catch (Throwable t) {
            // try to handle error here
            handleServerError(request, response, t);
        }
    }

    protected String getTargetAction(String[] uriParts) {
        String action = null;
        if (uriParts.length > 0) {
            String lastPart = uriParts[uriParts.length - 1];

            if (webConfig.getLoginAction().equals(lastPart) || webConfig.getLinkHandlerActions().contains(lastPart)) {
                action = lastPart;
            }
        }
        return action;
    }

    protected void redirectWithBlankHtmlPage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final BufferedWriter page = new BufferedWriter(new OutputStreamWriter(
                response.getOutputStream(), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.getRequestURI());
        stringBuilder.append("?");
        stringBuilder.append(FROM_HTML_REDIRECT_PARAM);
        stringBuilder.append("=true");

        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> paramEntry : parameterMap.entrySet()) {
            String[] paramValue = paramEntry.getValue();

            for (String paramPart : paramValue) {
                stringBuilder.append("&");
                stringBuilder.append(paramEntry.getKey());
                stringBuilder.append("=");
                stringBuilder.append(paramPart);
            }
        }
        String url = stringBuilder.toString();

        page.write(String.format(IOUtils.toString(resources.getResourceAsStream(REDIRECT_PAGE_TEMPLATE_PATH),
                StandardCharsets.UTF_8.name()), url, url));
        page.close();
    }

    protected void redirectToApp(HttpServletRequest request, HttpServletResponse response,
                                 String contextName, String[] uriParts, String action) throws IOException {
        StringBuilder redirectAddress = new StringBuilder();
        for (int i = 0; i < uriParts.length; i++) {
            redirectAddress.append(uriParts[i]);
            if (uriParts[i].equals(contextName)) {
                break;
            }
            if (i < uriParts.length - 1) {
                redirectAddress.append("/");
            }
        }

        // redirect to ROOT context
        if (redirectAddress.length() == 0) {
            redirectAddress.append("/");
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
                if (!FROM_HTML_REDIRECT_PARAM.equals(name)) {
                    params.put(name, request.getParameter(name));
                }
            }
            httpSession.setAttribute(AppUI.LAST_REQUEST_PARAMS_ATTR, params);
        }

        statisticsCounter.incWebRequestsCount();
        String httpSessionId = httpSession.getId();
        log.debug("Redirect to application {}", httpSessionId);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JSESSIONID".equals(cookie.getName()) && !httpSessionId.equals(cookie.getValue())) {
                    cookie.setValue(httpSessionId);
                    break;
                }
            }
        }
        response.sendRedirect(redirectAddress.toString());
    }

    protected void serviceAppRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestContext.create(request, response);
        statisticsCounter.incWebRequestsCount();

        long startTs = System.currentTimeMillis();

        try {
            super.service(request, response);
        } finally {
            RequestContext.destroy();
        }

        if (hasPathPrefix(request, ApplicationConstants.UIDL_PATH + '/')) {
            long t = System.currentTimeMillis() - startTs;
            if (t > (webConfig.getLogLongRequestsThresholdSec() * 1000)) {
                log.warn(String.format("Too long request processing [%d ms]: ip=%s, url=%s",
                        t, request.getRemoteAddr(), request.getRequestURI()));
            }
        }
    }

    protected boolean hasPathPrefix(HttpServletRequest request, String prefix) {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            return false;
        }

        if (!prefix.startsWith("/")) {
            prefix = '/' + prefix;
        }

        return pathInfo.startsWith(prefix);
    }

    protected String getPackageName() {
        String pkgName;
        final Package pkg = this.getClass().getPackage();
        if (pkg != null) {
            pkgName = pkg.getName();
        } else {
            final String className = this.getClass().getName();
            pkgName = new String(className.toCharArray(), 0,
                    className.lastIndexOf('.'));
        }
        return pkgName;
    }

    @Override
    protected boolean isAllowedVAADINResourceUrl(HttpServletRequest request, URL resourceUrl) {
        boolean isUberJar = Boolean.parseBoolean(AppContext.getProperty("cuba.uberJar"));
        if (isUberJar) {
            String resourcePath = resourceUrl.getPath();
            if ("jar".equals(resourceUrl.getProtocol())) {
                if (resourcePath.contains("!/LIB-INF/app/VAADIN/")) {
                    return true;
                }
            }
        }
        return super.isAllowedVAADINResourceUrl(request, resourceUrl);
    }

    public void handleServerError(HttpServletRequest req, HttpServletResponse resp, Throwable exception) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter out = resp.getWriter();

        try {
            String errorHtml = prepareErrorHtml(req, exception);
            out.print(errorHtml);
        } catch (Throwable t) {
            log.error("Unable to show error page", t);
            out.print(INTERNAL_ERROR_TEXT);
        }
    }

    protected String prepareErrorHtml(HttpServletRequest req, Throwable exception) {
        Messages messages = AppBeans.get(Messages.NAME);
        Configuration configuration = AppBeans.get(Configuration.NAME);

        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);

        // SimpleTemplateEngine requires mutable map
        Map<String, Object> binding = new HashMap<>();
        binding.put("tryAgainUrl", "?restartApp");
        binding.put("productionMode", webConfig.getProductionMode());
        binding.put("messages", messages);
        binding.put("exception", exception);
        binding.put("exceptionName", exception.getClass().getName());
        binding.put("exceptionMessage", exception.getMessage());
        binding.put("exceptionStackTrace", ExceptionUtils.getStackTrace(exception));

        Locale locale = resolveLocale(req, messages, globalConfig);

        String serverErrorPageTemplatePath = webConfig.getServerErrorPageTemplate();

        String localeString = messages.getTools().localeToString(locale);
        String templateContent = getLocalizedTemplateContent(resources, serverErrorPageTemplatePath, localeString);
        if (templateContent == null) {
            templateContent = resources.getResourceAsString(serverErrorPageTemplatePath);

            if (templateContent == null) {
                throw new IllegalStateException("Unable to find server error page template " + serverErrorPageTemplatePath);
            }
        }

        SimpleTemplateEngine templateEngine = new SimpleTemplateEngine();
        Template template = getTemplate(templateEngine, templateContent);

        Writable writable = template.make(binding);

        String html;
        try {
            html = writable.writeTo(new StringWriter()).toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to write server error page", e);
        }

        return html;
    }

    protected Locale resolveLocale(HttpServletRequest req, Messages messages, GlobalConfig globalConfig) {
        Map<String, Locale> locales = globalConfig.getAvailableLocales();

        if (globalConfig.getLocaleSelectVisible()) {
            String lastLocale = getCookieValue(req, "LAST_LOCALE");
            if (lastLocale != null) {
                for (Locale locale : locales.values()) {
                    if (locale.toLanguageTag().equals(lastLocale)) {
                        return locale;
                    }
                }
            }
        }

        Locale requestLocale = req.getLocale();
        if (requestLocale != null) {
            Locale requestTrimmedLocale = messages.getTools().trimLocale(requestLocale);
            if (locales.containsValue(requestTrimmedLocale)) {
                return requestTrimmedLocale;
            }

            // if not found and application locale contains country, try to match by language only
            if (!StringUtils.isEmpty(requestLocale.getCountry())) {
                Locale appLocale = Locale.forLanguageTag(requestLocale.getLanguage());
                for (Locale locale : locales.values()) {
                    if (Locale.forLanguageTag(locale.getLanguage()).equals(appLocale)) {
                        return locale;
                    }
                }
            }
        }

        return messages.getTools().getDefaultLocale();
    }

    protected String getCookieValue(HttpServletRequest req, String cookieName) {
        if (req.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : req.getCookies()) {
            if (Objects.equals(cookieName, cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Nullable
    protected String getLocalizedTemplateContent(Resources resources, String defaultTemplateName, String locale) {
        String localizedTemplate = FilenameUtils.getFullPath(defaultTemplateName)
                + FilenameUtils.getBaseName(defaultTemplateName) +
                "_" + locale +
                "." + FilenameUtils.getExtension(defaultTemplateName);

        return resources.getResourceAsString(localizedTemplate);
    }

    protected Template getTemplate(SimpleTemplateEngine templateEngine, String templateString) {
        Template bodyTemplate;
        try {
            bodyTemplate = templateEngine.createTemplate(templateString);
        } catch (Exception e) {
            throw new RuntimeException("Unable to compile Groovy template", e);
        }
        return bodyTemplate;
    }
}
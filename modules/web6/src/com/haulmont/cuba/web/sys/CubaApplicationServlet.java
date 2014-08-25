/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.auth.RequestContext;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.*;
import com.vaadin.ui.Window;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author gorodnov
 * @version $Id$
 */
public class CubaApplicationServlet extends ApplicationServlet {
    private static final long serialVersionUID = -8701539520754293569L;

    private static String releaseTimestamp = null;

    private Log log = LogFactory.getLog(CubaApplicationServlet.class);

    private WebConfig webConfig;

    private GlobalConfig globalConfig;
    private String webResourceTimestamp;

    @Override
    protected boolean isTestingMode() {
        return globalConfig.getTestMode();
    }

    @Override
    protected CubaApplicationContext getApplicationContext(HttpSession session) {
        return CubaApplicationContext.getApplicationContext(session);
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        Configuration configuration = AppBeans.get(Configuration.class);
        webConfig = configuration.getConfig(WebConfig.class);
        globalConfig = configuration.getConfig(GlobalConfig.class);

        webResourceTimestamp = getResourceVersion();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String contextName = request.getContextPath().substring(1);

        if (request.getParameter("restartApp") != null) {
            request.getSession().invalidate();
            response.sendRedirect(requestURI);
            return;
        }

        // Code for multiple windows support
        String[] uriParts = requestURI.split("/");
        boolean needRedirect = uriParts.length > 0 && !App.auxillaryUrl(requestURI) &&
                (request.getParameter("multiupload") == null);

        String action = null;

        if (needRedirect) {
            String lastPart = uriParts[uriParts.length - 1];

            if (webConfig.getLoginAction().equals(lastPart) || webConfig.getLinkHandlerActions().contains(lastPart)) {
                action = lastPart;
            }
            needRedirect = contextName.equals(lastPart) || action != null;
            int i = 0;
            while ((i < uriParts.length) && needRedirect) {
                Matcher m = App.WIN_PATTERN.matcher(uriParts[i]);
                if (m.matches())
                    needRedirect = false;
                i++;
            }
        }

        if (needRedirect) {
            if (webConfig.getRedirectByPageOnLinkActionEnabled() &&
                    action != null &&
                    request.getParameter(App.FROM_HTML_REDIRECT_PARAM) == null) {
                redirectByBlankHtmlPage(request, response);
            } else {
                redirectToApp(request, response, contextName, uriParts, action);
            }
        } else {
            RequestContext.create(request, response);

            try {
                doService(request, response);
            } finally {
                RequestContext.destroy();
            }
        }
    }

    protected void redirectByBlankHtmlPage(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final BufferedWriter page = new BufferedWriter(new OutputStreamWriter(
                response.getOutputStream(), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(request.getRequestURI());
        Map<String, String[]> parameterMap = request.getParameterMap();
        stringBuilder.append("?");
        stringBuilder.append(App.FROM_HTML_REDIRECT_PARAM);
        stringBuilder.append("=true");

        for (String key : parameterMap.keySet()) {
            for (String value : parameterMap.get(key)) {
                stringBuilder.append("&");
                stringBuilder.append(key);
                stringBuilder.append("=");
                stringBuilder.append(value);
            }
        }
        String url = stringBuilder.toString();

        page.write("<!DOCTYPE HTML>");
        page.write("<head>");
        page.write("<meta charset=\"UTF-8\"");
        page.write("<meta http-equiv=\"refresh\" content=\"1;url=");
        page.write(url);
        page.write("\">");
        page.write("<script type=\"text/javascript\">");
        page.write("<meta charset=\"UTF-8\">");
        page.write("<meta http-equiv=\"refresh\" content=\"1;url=");
        page.write(url);
        page.write("\"><script type=\"text/javascript\">");
        page.write("window.location.href = \"");
        page.write(url);
        page.write("\"</script>");
        page.write("</head>");
        page.write("<body/>");
        page.write("</html>");
        page.close();
    }

    private void redirectToApp(HttpServletRequest request, HttpServletResponse response,
                               String contextName, String[] uriParts, String action) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uriParts.length; i++) {
            String windowName = App.generateWebWindowName();
            sb.append(uriParts[i]);
            if (uriParts[i].equals(contextName)) {
                sb.append("/").append(windowName);
                break;
            }
            if (i < uriParts.length - 1)
                sb.append("/");
        }

        HttpSession httpSession = request.getSession();
        if (action != null) {
            httpSession.setAttribute(App.LAST_REQUEST_ACTION_ATTR, action);
        }
        if (request.getParameterNames().hasMoreElements()) {
            Map<String, String> params = new HashMap<String, String>();
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = (String) parameterNames.nextElement();
                if (!App.FROM_HTML_REDIRECT_PARAM.equals(name)) {
                    params.put(name, request.getParameter(name));
                }
            }
            httpSession.setAttribute(App.LAST_REQUEST_PARAMS_ATTR, params);
        }

        log.debug("Redirect to application " + httpSession.getId());
        response.addCookie(new Cookie("JSESSIONID", httpSession.getId()));
        response.sendRedirect(sb.toString());
    }

    private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestType requestType = getRequestType(request);
        if ((requestType == RequestType.FILE_UPLOAD) &&
                ("POST".equals(request.getMethod())) &&
                (request.getParameter("multiupload") != null)) {
            Application application = null;
            try {
                application = findApplicationInstance(request, requestType);
                if (application == null) {
                    return;
                }

                WebApplicationContext webApplicationContext = getApplicationContext(request.getSession());
                CommunicationManager applicationManager = webApplicationContext
                        .getApplicationManager(application, this);

                if (applicationManager instanceof CubaCommunicationManager) {
                    ((CubaCommunicationManager) applicationManager).handleMultiUpload(request, response, (App) application);
                    return;
                }
            } catch (SessionExpiredException e) {
                handleServiceException(request, response, application, e);
            }
        }

        super.service(request, response);
    }

    @Override
    protected RequestType getRequestType(HttpServletRequest request) {
        if (isMultiUpload(request))
            return RequestType.FILE_UPLOAD;
        return super.getRequestType(request);
    }

    private boolean isMultiUpload(HttpServletRequest request) {
        return ServletFileUpload.isMultipartContent(request);
    }

    @Override
    protected void writeAjaxPageHtmlVaadinScripts(Window window, String themeName, Application application,
                                                  final BufferedWriter page, String appUrl, String themeUri,
                                                  String appId, HttpServletRequest request)
            throws ServletException, IOException {

        // request widgetset takes precedence (e.g portlet include)
        String requestWidgetset = (String) request
                .getAttribute(REQUEST_WIDGETSET);
        String sharedWidgetset = (String) request
                .getAttribute(REQUEST_SHARED_WIDGETSET);
        if (requestWidgetset == null && sharedWidgetset == null) {
            // Use the value from configuration or DEFAULT_WIDGETSET.
            // If no shared widgetset is specified, the default widgetset is
            // assumed to be in the servlet/portlet itself.
            requestWidgetset = getApplicationOrSystemProperty(
                    PARAMETER_WIDGETSET, DEFAULT_WIDGETSET);
        }

        String widgetset;
        String widgetsetBasePath;
        if (requestWidgetset != null) {
            widgetset = requestWidgetset;
            widgetsetBasePath = getWebApplicationsStaticFileLocation(request);
        } else {
            widgetset = sharedWidgetset;
            widgetsetBasePath = getStaticFilesLocation(request);
        }

        final String widgetsetFilePath = widgetsetBasePath + "/"
                + WIDGETSET_DIRECTORY_PATH + widgetset + "/" + widgetset
                + ".nocache.js?" + new Date().getTime();

        // Get system messages
        App.CubaSystemMessages systemMessages;
        try {
            systemMessages = (App.CubaSystemMessages) getSystemMessages();
        } catch (SystemMessageException e) {
            // failing to get the system messages is always a problem
            throw new ServletException("CommunicationError!", e);
        }

        page.write("<script type=\"text/javascript\">\n");
        page.write("//<![CDATA[\n");
        page.write("if(!vaadin || !vaadin.vaadinConfigurations) {\n "
                + "if(!vaadin) { var vaadin = {}} \n"
                + "vaadin.vaadinConfigurations = {};\n"
                + "if (!vaadin.themesLoaded) { vaadin.themesLoaded = {}; }\n");
        if (!isProductionMode()) {
            page.write("vaadin.debug = true;\n");
        }
        page.write("document.write('<iframe tabIndex=\"-1\" id=\"__gwt_historyFrame\" "
                + "style=\"position:absolute;width:0;height:0;border:0;overflow:"
                + "hidden;\" src=\"javascript:false\"></iframe>');\n");
        page.write("document.write(\"<script language='javascript' src='"
                + widgetsetFilePath + "'><\\/script>\");\n}\n");

        page.write("vaadin.vaadinConfigurations[\"" + appId + "\"] = {");
        page.write("appUri:'" + appUrl + "', ");

        String pathInfo = getRequestPathInfo(request);
        if (pathInfo == null) {
            pathInfo = "/";
        }

        page.write("pathInfo: '" + pathInfo + "', ");
        if (window != application.getMainWindow()) {
            page.write("windowName: '" + window.getName() + "', ");
        }
        // Write Session ID
        page.write("sessionId: '" + request.getSession().getId() + "', ");

        page.write("themeUri:");
        page.write(themeUri != null ? "'" + themeUri + "'" : "null");
        page.write(", versionInfo : {vaadinVersion:\"");
        page.write(VERSION);
        page.write("\",applicationVersion:\"");
        page.write(application.getVersion());
        page.write("\"},");
        //Gorodnov: support client components IDs
        if (isTestingMode()) {
            page.write("useDebugIdInDom: true,\n");
        }

        if (webConfig.getAllowHandleBrowserHistoryBack())
            page.write("handleHistoryBack: true,\n");

        page.write("\n\"uiBlocking\" : {");
        page.write(" \"blockUiMessage\" : \"" + systemMessages.getUiBlockingMessage() + "\" ,");
        page.write(" \"useUiBlocking\" : " +
                BooleanUtils.toString(webConfig.getUseUiBlocking(), "true", "false") + "");
        page.write("} ,\n");

        if (systemMessages != null) {
            // Write the CommunicationError -message to client
            String caption = systemMessages.getCommunicationErrorCaption();
            if (caption != null) {
                caption = "\"" + caption + "\"";
            }
            String message = systemMessages.getCommunicationErrorMessage();
            if (message != null) {
                message = "\"" + message + "\"";
            }
            String url = systemMessages.getCommunicationErrorURL();
            if (url != null) {
                url = "\"" + url + "\"";
            }

            page.write("\"comErrMsg\": {" + "\"caption\":" + caption + ","
                    + "\"message\" : " + message + "," + "\"url\" : " + url
                    + "}");
        }
        page.write("};\n//]]>\n</script>\n");

        if (themeName != null) {
            injectThemeScript(themeName, page, themeUri);
        }

        // Warn if the widgetset has not been loaded after 15 seconds on
        // inactivity
        page.write("<script type=\"text/javascript\">\n");
        page.write("//<![CDATA[\n");
        page.write("setTimeout('if (typeof "
                + widgetset.replace('.', '_')
                + " == \"undefined\") {alert(\"Failed to load the widgetset: "
                + widgetsetFilePath + "\")};',15000);\n"
                + "//]]>\n</script>\n");
    }

    @Override
    protected void writeAjaxPageHtmlHeader(HttpServletRequest request, BufferedWriter page, String title,
                                           String themeUri) throws IOException {
        page.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n");

        page.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=9\"/>\n");
        page.write("<style type=\"text/css\">"
                + "html, body {height:100%;margin:0;}</style>");

        // Add favicon links
        page.write("<link rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" href=\""
                + themeUri + "/favicon.ico\" />");
        page.write("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\""
                + themeUri + "/favicon.ico\" />");

        page.write("<title>" + title + "</title>");

        writeScriptResource(request, page, "jquery-1.4.2.min.js", false);
        writeScriptResource(request, page, "jquery.blockUI.js", false);
        writeScriptResource(request, page, "scripts.js", true);
    }

    private void writeScriptResource(HttpServletRequest request, BufferedWriter page, String fileName, boolean nocache) throws IOException {
        page.write("<script src=\"" + request.getContextPath() + "/VAADIN/resources/js");
        if (!fileName.startsWith("/")) {
            page.write("/");
        }
        page.write(fileName);
        if (nocache) {
            page.write("?v=" + webResourceTimestamp);
        }
        page.write("\" language=\"javascript\"> </script>");
    }

    @Override
    protected void injectThemeScript(String themeName, BufferedWriter page, String themeUri) throws IOException {
        // Custom theme's stylesheet, load only once, in different
        // script
        // tag to be dominate styles injected by widget
        // set

        page.write("<script type=\"text/javascript\">\n");
        page.write("//<![CDATA[\n");
        page.write("if(!vaadin.themesLoaded['" + themeName + "']) {\n");
        page.write("var stylesheet = document.createElement('link');\n");
        page.write("stylesheet.setAttribute('rel', 'stylesheet');\n");
        page.write("stylesheet.setAttribute('type', 'text/css');\n");

        page.write("stylesheet.setAttribute('href', '" + themeUri
                + "/styles.css" + "?v=" + webResourceTimestamp + "');\n");
        page.write("document.getElementsByTagName('head')[0].appendChild(stylesheet);\n");
        page.write("vaadin.themeReleaseTimestamp=\"" + webResourceTimestamp + "\";\n");
        page.write("vaadin.themesLoaded['" + themeName + "'] = true;\n}\n");
        page.write("//]]>\n</script>\n");
    }

    protected String getResourceVersion() {
        String webResourceTimestamp;
        String resourcesTimestamp = getServletContext().getInitParameter("webResourcesTs");
        if (StringUtils.isNotEmpty(resourcesTimestamp)) {
            webResourceTimestamp = resourcesTimestamp;
        } else {
            webResourceTimestamp = "DEBUG";
        }
        return webResourceTimestamp;
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request) throws ServletException {
        try {
            // Creates a new application instance
            final Application application;
            try {
                application = getApplicationClass().newInstance();
            } catch (ClassNotFoundException e) {
                throw new ServletException(e);
            }

            App app = (App) application;

            app.setWebResourceTimestamp(webResourceTimestamp);
            app.getCookies().updateCookies(request);              // Handles requested cookies

            return application;
        } catch (final IllegalAccessException | InstantiationException e) {
            throw new ServletException("getNewApplication failed", e);
        }
    }

    private static String releaseTimestamp() {
        if (releaseTimestamp == null) {
            ServerInfoService service = ServiceLocator.lookup(ServerInfoService.NAME);
            String timestamp = service.getReleaseTimestamp();
            if (timestamp == null || timestamp.equals("") || timestamp.equals("?")) {
                timestamp = "0.9";
            }
            timestamp = timestamp.replaceAll("[^0-9]", "");
            releaseTimestamp = timestamp;
        }
        return releaseTimestamp;
    }
}
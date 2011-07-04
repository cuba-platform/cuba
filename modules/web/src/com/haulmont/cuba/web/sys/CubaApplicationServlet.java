/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 27.08.2009 18:39:09
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.app.CubaDeployerService;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Browser;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.*;
import com.vaadin.ui.Window;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class CubaApplicationServlet extends ApplicationServlet {
    private static final long serialVersionUID = -8701539520754293569L;

    private static String releaseTimestamp = null;

    @Override
    protected boolean isTestingMode() {
        return ConfigProvider.getConfig(GlobalConfig.class).getTestMode();
    }

    @Override
    protected CubaApplicationContext getApplicationContext(HttpSession session) {
        return CubaApplicationContext.getApplicationContext(session);
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

        if (request.getParameter("testSS") != null) {
            testSessionSerialization(request.getSession());
        }

        // Code for multiple windows support
        String[] uriParts = requestURI.split("/");
        boolean needRedirect = uriParts.length > 0 && !App.auxillaryUrl(requestURI) &&
                (request.getParameter("multiupload") == null);

        String action = null;

        if (needRedirect) {
            String lastPart = uriParts[uriParts.length - 1];
            action = App.ACTION_NAMES.contains(lastPart) ? lastPart : null;
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
            redirectToApp(request, response, contextName, uriParts, action);
        } else {
            doService(request, response);
        }
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
        if (action != null) {
            request.getSession().setAttribute(App.LAST_REQUEST_ACTION_ATTR, action);
        }
        if (request.getParameterNames().hasMoreElements()) {
            Map<String, String> params = new HashMap<String, String>();
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = (String) parameterNames.nextElement();
                params.put(name, request.getParameter(name));
            }
            request.getSession().setAttribute(App.LAST_REQUEST_PARAMS_ATTR, params);
        }
        response.sendRedirect(sb.toString());
    }

    private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestType requestType = getRequestType(request);
        if (requestType == RequestType.OTHER && isChartRequest(request)) {
            Application application = null;
            CubaApplicationContext webApplicationContext = null;

            try {
                application = findApplicationInstance(request, requestType);
                if (application == null) {
                    return;
                }

                webApplicationContext = getApplicationContext(request.getSession());
                webApplicationContext.startTransaction(application, request);

                CommunicationManager applicationManager = webApplicationContext
                        .getApplicationManager(application, this);

                if (applicationManager instanceof CubaCommunicationManager) {
                    ((CubaCommunicationManager) applicationManager).handleChartRequest(request, response, (App)application);
                    return;
                }

            } catch (SessionExpiredException e) {
                handleServiceException(request, response, application, e);
            } finally {
                if (webApplicationContext != null && application != null) {
                    webApplicationContext.endTransaction(application, request);
                }
            }
        } else if ((requestType == RequestType.FILE_UPLOAD) &&
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
                    ((CubaCommunicationManager) applicationManager).handleMultiUpload(request, response,(App)application);
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

    private boolean isChartRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            return false;
        }

        String compare = "/chart";

        return pathInfo.startsWith(compare + "/") || pathInfo.endsWith(compare);
    }

    private void testSessionSerialization(HttpSession session) {
        String tempDir = ConfigProvider.getConfig(GlobalConfig.class).getTempDir();
        String fileName = tempDir + "/" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + ".ser";
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fos);

            Enumeration names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                Object value = session.getAttribute((String) names.nextElement());
                oos.writeObject(value);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                //
            }
        }
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
        Application.SystemMessages systemMessages = null;
        try {
            systemMessages = getSystemMessages();
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
        page
                .write("document.write('<iframe tabIndex=\"-1\" id=\"__gwt_historyFrame\" "
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
        page
                .write("setTimeout('if (typeof "
                        + widgetset.replace('.', '_')
                        + " == \"undefined\") {alert(\"Failed to load the widgetset: "
                        + widgetsetFilePath + "\")};',15000);\n"
                        + "//]]>\n</script>\n");
    }

    @Override
    protected void writeAjaxPageHtmlHeader(HttpServletRequest request, BufferedWriter page, String title,
                                           String themeUri) throws IOException {
        page.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n");
        if (ConfigProvider.getConfig(WebConfig.class).getUseChromeFramePlugin()
                && Browser.getBrowserInfo(request).isChromeFrame()) {
            page.write("<meta http-equiv=\"X-UA-Compatible\" content=\"chrome=1\" />\n");
/*
        } else {
            page.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\" />\n");
*/
        }
        page.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=8\"/>\n");
        page.write("<style type=\"text/css\">"
                + "html, body {height:100%;margin:0;}</style>");

        // Add favicon links
        page.write("<link rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" href=\""
                + themeUri + "/favicon.ico\" />");
        page.write("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\""
                + themeUri + "/favicon.ico\" />");

        page.write("<title>" + title + "</title>");

        writeScriptResource(request, page, "jquery-1.4.2.min.js", false);
        writeScriptResource(request, page, "jquery.disable.text.select.pack.js", false);
        writeScriptResource(request, page, "scripts.js", true);
    }

    private void writeScriptResource(HttpServletRequest request, BufferedWriter page, String fileName, boolean nocache) throws IOException {
        page.write("<script src=\"" + request.getContextPath() + "/VAADIN/resources/js");
        if (!fileName.startsWith("/")) {
            page.write("/");
        }
        page.write(fileName);
        if (nocache) {
            String timestamp = releaseTimestamp();
            page.write(timestamp == null ? "" : "?" + timestamp);
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

        String timestamp = releaseTimestamp();
        page.write("stylesheet.setAttribute('href', '" + themeUri
                + "/styles.css" + (timestamp == null ? "" : "?" + timestamp) + "');\n");
        page.write("document.getElementsByTagName('head')[0].appendChild(stylesheet);\n");
        if (timestamp != null) {
            page.write("vaadin.themeReleaseTimestamp=" + timestamp + ";\n");
        }
        page.write("vaadin.themesLoaded['" + themeName + "'] = true;\n}\n");
        page.write("//]]>\n</script>\n");
    }

    void sendCriticalNotification(HttpServletRequest request,
                                  HttpServletResponse response, String caption, String message,
                                  String details, String url) throws IOException {
        criticalNotification(request, response, caption, message, details, url);
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

            // Handles requested cookies
            ((App) application).getCookies().updateCookies(request);

            return application;
        } catch (final IllegalAccessException e) {
            throw new ServletException("getNewApplication failed", e);
        } catch (final InstantiationException e) {
            throw new ServletException("getNewApplication failed", e);
        }
    }

    private static String releaseTimestamp() {
        if (releaseTimestamp == null) {
            CubaDeployerService service = ServiceLocator.lookup(CubaDeployerService.NAME);
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

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

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.GlobalUtils;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.Browser;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.SessionExpiredException;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;
import java.util.regex.Matcher;

public class CubaApplicationServlet extends ApplicationServlet {
    private static final long serialVersionUID = -8701539520754293569L;

    @Override
    protected boolean isTestingMode() {
        return ConfigProvider.getConfig(GlobalConfig.class).getTestMode();
    }

    @Override
    protected WebApplicationContext getApplicationContext(HttpSession session) {
        return CubaApplicationContext.getApplicationContext(session);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String contextName = request.getContextPath().substring(1);

        if (request.getParameter("testSS") != null) {
            testSessionSerialization(request.getSession());
        }

        String[] parts = requestURI.split("/");
        String lastPart = parts[parts.length - 1];
        boolean needRedirect = parts.length > 0 && (contextName.equals(lastPart) || "open".equals(lastPart) || "login".equals(lastPart));
        if (needRedirect) {
            for (String part : parts) {
                Matcher m = App.WIN_PATTERN.matcher(part);
                if (part.equals("UIDL") || part.equals("VAADIN") || m.matches()) {
                    needRedirect = false;
                    break;
                }
            }
        }
        if (needRedirect) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                sb.append(parts[i]);
                if (parts[i].equals(contextName)) {
                    sb.append("/").append(GlobalUtils.generateWebWindowName());
                }
                if (i < parts.length - 1)
                    sb.append("/");
            }
            if (request.getParameterNames().hasMoreElements())
                sb.append("?");
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String param = (String) parameterNames.nextElement();
                sb.append(param).append("=").append(request.getParameter(param));
                if (parameterNames.hasMoreElements())
                    sb.append("&");
            }
            response.sendRedirect(sb.toString());
        } else {
            doService(request, response);
        }
    }

    private void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestType requestType = getRequestType(request);
        if (requestType == RequestType.OTHER && isChartRequest(request)) {
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
                    ((CubaCommunicationManager) applicationManager).handleChartRequest(request, response, this);
                    return;
                }

            } catch (SessionExpiredException e) {
                handleServiceException(request, response, application, e);
            }
        }
        super.service(request, response);
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
        page.write("<style type=\"text/css\">"
                + "html, body {height:100%;margin:0;}</style>");

        // Add favicon links
        page.write("<link rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" href=\""
                        + themeUri + "/favicon.ico\" />");
        page.write("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\""
                        + themeUri + "/favicon.ico\" />");

        page.write("<title>" + title + "</title>");

        page.write("<script src=\"" + request.getContextPath() + "/VAADIN/resources/js/jquery-1.4.2.min.js\" laguage=\"javascript\"> </script>");
        page.write("<script src=\"" + request.getContextPath() + "/VAADIN/resources/js/jquery.disable.text.select.pack.js\" laguage=\"javascript\"> </script>");
        page.write("<script src=\"" + request.getContextPath() + "/VAADIN/resources/js/scripts.js\" laguage=\"javascript\"> </script>");
    }

/*
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

        String timestamp = ConfigProvider.getConfig(GlobalConfig.class).getBuildTimestamp();
        page.write("stylesheet.setAttribute('href', '" + themeUri
                + "/styles.css" + (timestamp == null ? "" : "?" + timestamp) + "');\n");
        page
                .write("document.getElementsByTagName('head')[0].appendChild(stylesheet);\n");
        page.write("vaadin.themesLoaded['" + themeName + "'] = true;\n}\n");
        page.write("//]]>\n</script>\n");
    }
*/

    void sendCriticalNotification(HttpServletRequest request,
            HttpServletResponse response, String caption, String message,
            String details, String url) throws IOException {
        criticalNotification(request, response, caption, message, details, url);
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request) throws ServletException {
        try {
            // Creates a new application instance
            final Application application = getApplicationClass().newInstance();

            // Handles requested cookies
            ((App) application).getCookies().updateCookies(request);

            return application;
        } catch (final IllegalAccessException e) {
            throw new ServletException("getNewApplication failed", e);
        } catch (final InstantiationException e) {
            throw new ServletException("getNewApplication failed", e);
        }
    }
}

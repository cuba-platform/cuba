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
import com.haulmont.cuba.web.Browser;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.App;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.Application;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.*;
import java.util.Enumeration;

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
        boolean needRedirect = parts.length > 0 && "open".equals(parts[parts.length - 1]);
        if (needRedirect) {
            for (String part : parts) {
                if (part.startsWith("win") || part.equals("UIDL")) {
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
            super.service(request, response);
        }
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
        } else {
            page.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=7\" />\n");
        }
        page.write("<style type=\"text/css\">"
                + "html, body {height:100%;margin:0;}</style>");

        // Add favicon links
        page.write("<link rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" href=\""
                        + themeUri + "/favicon.ico\" />");
        page.write("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\""
                        + themeUri + "/favicon.ico\" />");

        page.write("<title>" + title + "</title>");
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

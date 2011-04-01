/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Gorodnov
 * Created: 04.03.2011 10:57:41
 *
 * $Id$
 */
package com.haulmont.cuba.web.controllers;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.sys.WebSecurityUtils;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

@Controller
public class FileDownloadController {

    private static final String CORE_FILE_DOWNLOAD_CONTEXT = "/remoting/download";

    private static Log log = LogFactory.getLog(FileDownloadController.class);

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView download(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        UserSession userSession = getSession(request, response);
        if (userSession == null) {
            error(response);
        }

        WebSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());
        try {
            UUID fileId;
            try {
                fileId = UUID.fromString(request.getParameter("f"));
            } catch (Exception e) {
                error(response);
                return null;
            }

            boolean attach = Boolean.valueOf(request.getParameter("a"));

            FileDescriptor fd = ServiceLocator.getDataService().load(
                    new LoadContext(FileDescriptor.class).setId(fileId)
            );

            String fileName;
            try {
                fileName = URLEncoder.encode(fd.getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Type", getContentType(fd));
            response.setHeader("Content-Disposition", (attach ? "attachment" : "inline")
                    + "; filename=" + fileName);

            InputStream is = null;
            ServletOutputStream os = null;
            try {
                is = openInputStream(userSession, fileId);
                os = response.getOutputStream();
                IOUtils.copy(is, os);
                os.flush();
            } catch (Exception e) {
                log.error("Unable to download file", e);
                error(response);
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

        } finally {
            WebSecurityUtils.clearSecurityAssociation();
        }

        return null;
    }

    private InputStream openInputStream(UserSession userSession, UUID fileId) throws IOException {
        String connectionUrl = ConfigProvider.getConfig(WebConfig.class).getConnectionUrl();
        URL url = new URL(connectionUrl + CORE_FILE_DOWNLOAD_CONTEXT + "?s=" + userSession.getId() + "&f=" + fileId.toString());
        return url.openStream();
    }


    protected UserSession getSession(HttpServletRequest request, HttpServletResponse response) {
        App app = getExistingApplication(request, response);
        if (app == null || !app.getConnection().isConnected()) {
            return null;
        }

        UUID sessionId;
        try {
            sessionId = UUID.fromString(request.getParameter("s"));
        } catch (Exception e) {
            return null;
        }
        UserSession userSession = app.getConnection().getSession();
        if (!sessionId.equals(userSession.getId())) {
            return null;
        }
        return userSession;
    }

    protected String getContentType(FileDescriptor fd) {
        return FileTypesHelper.getMIMEType("." + fd.getExtension().toLowerCase());
    }

    private void error(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private App getExistingApplication(HttpServletRequest request,
                                       HttpServletResponse response) {
        // Ensures that the session is still valid
        final HttpSession session = request.getSession(true);

        // Gets application list for the session.
        final Collection applications = WebApplicationContext
                .getApplicationContext(session).getApplications();

        // Search for the application (using the application URI) from the list
        for (final Iterator i = applications.iterator(); i.hasNext();) {
            final Application a = (Application) i.next();
            final String aPath = a.getURL().getPath();

            String servletPath = request.getContextPath();
            if (!servletPath.endsWith("/")) {
                servletPath += "/";
            }

            if (servletPath.equals(aPath)) {
                // Found a running application
                if (a.isRunning() && a instanceof App) {
                    return (App) a;
                }
                break;
            }
        }

        // Existing application not found
        return null;
    }

}

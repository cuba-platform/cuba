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

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.gui.ServiceLocator;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
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

    private static Log log = LogFactory.getLog(FileDownloadController.class);

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView download(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        UserSession userSession = getSession(request, response);
        if (userSession == null) {
            error(response);
            return null;
        }

        AppContext.setSecurityContext(new SecurityContext(userSession));
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
            response.setHeader("Pragma", "no-cache");
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
            AppContext.setSecurityContext(null);
        }

        return null;
    }

    private InputStream openInputStream(UserSession userSession, UUID fileId) throws IOException {
        ClientConfig clientConfig = ConfigProvider.getConfig(ClientConfig.class);

        String connectionUrl = clientConfig.getConnectionUrl();
        String fileDownloadContext = clientConfig.getFileDownloadContext();

        URL url = new URL(connectionUrl + fileDownloadContext + "?s=" + userSession.getId() + "&f=" + fileId.toString());
        return url.openStream();
    }

    protected UserSession getSession(HttpServletRequest request, HttpServletResponse response) {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(request.getParameter("s"));
        } catch (Exception e) {
            return null;
        }
        AppContext.setSecurityContext(new SecurityContext(sessionId));
        try {
            UserSessionService uss = ServiceLocator.lookup(UserSessionService.NAME);
            UserSession userSession = uss.getUserSession(sessionId);
            return userSession;
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    protected String getContentType(FileDescriptor fd) {
        return FileTypesHelper.getMIMEType("." + fd.getExtension().toLowerCase());
    }

    private void error(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

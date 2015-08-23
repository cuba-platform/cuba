/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.UUID;

/**
 * Handles file download requests to the middleware.
 * <p/> This controller is deployed in Spring context defined by <code>cuba.dispatcherSpringContextConfig</code>
 * app property.
 *
 * @author gorodnov
 * @version $Id$
 */
@Controller
public class FileDownloadController {

    private static Log log = LogFactory.getLog(FileDownloadController.class);

    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    private FileStorageAPI fileStorage;

    @Inject
    private DataService dataService;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserSession userSession = getSession(request, response);
        if (userSession == null)
            return;

        AppContext.setSecurityContext(new SecurityContext(userSession));
        try {
            File file = null;
            FileDescriptor fd = null;
            if (request.getParameter("p") != null)
                file = getFile(request, response);
            else
                fd = getFileDescriptor(request, response);
            if (fd == null && file == null)
                return;

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setIntHeader("Expires", -1);
            response.setHeader("Content-Type", FileTypesHelper.DEFAULT_MIME_TYPE);

            InputStream is = null;
            ServletOutputStream os = null;
            try {
                is = fd != null ? fileStorage.openStream(fd) : FileUtils.openInputStream(file);
                os = response.getOutputStream();
                IOUtils.copy(is, os);
                os.flush();
            } catch (FileStorageException e) {
                log.error("Unable to download file", e);
                response.sendError(e.getType().getHttpStatus());
            } catch (Exception ex) {
                log.error("Unable to download file", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    protected UserSession getSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(request.getParameter("s"));
        } catch (Exception e) {
            log.error("Error parsing sessionId from URL param", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        UserSession session = userSessionManager.findSession(sessionId);
        if (session == null)
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return session;
    }

    private FileDescriptor getFileDescriptor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID fileId;
        try {
            fileId = UUID.fromString(request.getParameter("f"));
        } catch (Exception e) {
            log.error("Error parsing fileId from URL param", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        FileDescriptor fileDescriptor = dataService.load(new LoadContext<>(FileDescriptor.class).setId(fileId));
        if (fileDescriptor == null)
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return fileDescriptor;
    }


    public File getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filePath = decodeUTF8(request.getParameter("p"));
        if (filePath != null) {
            if (isPermittedDirectory(filePath)) {
                return new File(filePath);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {            
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    protected boolean isPermittedDirectory(String filePath) {
        String directories = AppContext.getProperty("cuba.download.directories");
        if (directories != null && filePath != null ) {
            filePath = filePath.replace("\\", "/");
            for (String d : directories.split(";")) {
                d = d.replace("\\", "/");
                if (!d.endsWith("/"))
                    d = d + "/";
                if (filePath.startsWith(d)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected String decodeUTF8(String str) {
        try {
            return URLDecoder.decode(str, "UTF8");
        } catch (Exception e) {
            log.error("Decode string from URL param failed", e);
            return null;
        }
    }
}

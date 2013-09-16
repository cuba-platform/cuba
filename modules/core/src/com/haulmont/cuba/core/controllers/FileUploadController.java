/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@Controller
public class FileUploadController {

    private Log log = LogFactory.getLog(FileUploadController.class);

    @Inject
    private UserSessionManager userSessionManager;

    @Inject
    private FileStorageAPI fileStorage;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserSession userSession = getSession(request, response);
        if (userSession == null)
            return;

        AppContext.setSecurityContext(new SecurityContext(userSession));
        try {
            InputStream is = request.getInputStream();
            if (is == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            FileDescriptor fd = getFileDescriptor(request, response);
            if (fd == null)
                return;

            try {
                fileStorage.saveStream(fd, is);
            } catch (FileStorageException e) {
                log.error("Unable to upload file", e);
                response.sendError(e.getType().getHttpStatus());
            } finally {
                IOUtils.closeQuietly(is);
            }
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    private UserSession getSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        FileDescriptor fd;
        try {
            fd = FileDescriptor.fromUrlParam(request.getParameter("f"));
        } catch (Exception e) {
            log.error("Error parsing FileDescriptor from URL param", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        return fd;
    }
}

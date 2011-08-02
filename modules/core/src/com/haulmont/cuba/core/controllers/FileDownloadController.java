/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.sys.ServerSecurityUtils;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class FileDownloadController {
    private static final int HTTP_NOT_FOUND = 468;

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

        ServerSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());
        try {
            FileDescriptor fd = getFileDescriptor(request, response);
            if (fd == null)
                return;

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setIntHeader("Expires", -1);
            response.setHeader("Content-Type", FileTypesHelper.DEFAULT_MIME_TYPE);

            InputStream is = null;
            ServletOutputStream os = null;
            try {
                is = fileStorage.openFileInputStream(fd);
                os = response.getOutputStream();

                byte[] buffer = new byte[1024 * 64];
                long count = 0;
                int n = 0;
                while (-1 != (n = is.read(buffer))) {
                    os.write(buffer, 0, n);
                    count += n;
                }

                os.flush();
            } catch (FileStorageException e) {
                log.error("Unable to download file", e);
                response.sendError(HTTP_NOT_FOUND);
            } catch (Exception ex) {
                log.error("Unable to download file", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
        } finally {
            ServerSecurityUtils.clearSecurityAssociation();
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
        FileDescriptor fileDescriptor = dataService.load(new LoadContext(FileDescriptor.class).setId(fileId));
        if (fileDescriptor == null)
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return fileDescriptor;
    }
}

/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.Locator;
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
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class FileDownloadController {

    private static Log log = LogFactory.getLog(FileDownloadController.class);

    @Inject
    private UserSessionManager userSessionManager;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView download(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        UserSession userSession = getSession(request, response);
        if (userSession == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        ServerSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());
        try {
            UUID fileId;
            try {
                fileId = UUID.fromString(request.getParameter("f"));
            } catch (Exception e) {
                error(response);
                return null;
            }

            DataService dataService = Locator.lookup(DataService.NAME);
            FileDescriptor fd = dataService.load(new LoadContext(FileDescriptor.class).setId(fileId));

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Type", FileTypesHelper.DEFAULT_MIME_TYPE);

            FileStorageAPI fileStorage = Locator.lookup(FileStorageAPI.NAME);
            InputStream is = null;
            ServletOutputStream os = null;
            try {
                is = fileStorage.openFileInputStream(fd);
                os = response.getOutputStream();
                IOUtils.copy(is, os);
                os.flush();
            } catch (FileStorageException e) {
                log.error("Unable to download file", e);
                error(response);
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }

        } finally {
            ServerSecurityUtils.clearSecurityAssociation();
        }

        return null;
    }

    protected UserSession getSession(HttpServletRequest request, HttpServletResponse response) {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(request.getParameter("s"));
        } catch (Exception e) {
            return null;
        }
        UserSession userSession = userSessionManager.getSession(sessionId);
        return userSession;
    }

    private void error(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

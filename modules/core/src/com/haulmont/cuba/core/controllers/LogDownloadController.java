/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.global.LogControl;
import com.haulmont.cuba.core.sys.logging.LogArchiver;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
@Controller
public class LogDownloadController {

    private final static Log log = LogFactory.getLog(LogDownloadController.class);

    @Inject
    protected LogControl logControl;

    @Inject
    protected UserSessionManager userSessionManager;

    @RequestMapping(value = "/log/{file:[a-zA-Z0-9\\.\\-_]+}", method = RequestMethod.GET)
    public void getLogFile(HttpServletResponse response,
                           @RequestParam(value = "s") String sessionId,
                           @PathVariable(value = "file") String logFileName) throws IOException {
        UserSession userSession = getSession(sessionId, response);
        if (userSession == null)
            return;

        if (!userSession.isSpecificPermitted("cuba.gui.administration.downloadlogs")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // security check, handle only valid file name
        String filename = FilenameUtils.getName(logFileName);

        try {
            File logFile = logControl.getLogFile(filename);

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Type", "application/zip");
            response.setHeader("Pragma", "no-cache");

            response.setHeader("Content-Disposition", "attachment; filename=" + filename);

            OutputStream outputStream = null;
            try {
                outputStream = response.getOutputStream();

                LogArchiver.writeArchivedLogToStream(logFile, outputStream);
            } catch (Exception ex) {
                log.error("Unable to download file", ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                IOUtils.closeQuietly(outputStream);
            }

        } catch (LogFileNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected UserSession getSession(String sessionId, HttpServletResponse response) throws IOException {
        UUID sessionUUID;
        try {
            sessionUUID = UUID.fromString(sessionId);
        } catch (Exception e) {
            log.error("Error parsing sessionId from URL param", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        UserSession session = userSessionManager.findSession(sessionUUID);
        if (session == null)
            response.sendError(HttpServletResponse.SC_FORBIDDEN);

        return session;
    }
}
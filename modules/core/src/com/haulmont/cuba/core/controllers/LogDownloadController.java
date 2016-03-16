/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.controllers;

import com.haulmont.cuba.core.global.LogControl;
import com.haulmont.cuba.core.sys.logging.LogArchiver;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.sys.UserSessionManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 */
@Controller
public class LogDownloadController {

    private final static Logger log = LoggerFactory.getLogger(LogDownloadController.class);

    @Inject
    protected LogControl logControl;

    @Inject
    protected UserSessionManager userSessionManager;

    @RequestMapping(value = "/log/{file:[a-zA-Z0-9\\.\\-_]+}", method = RequestMethod.GET)
    public void getLogFile(HttpServletResponse response,
                           @RequestParam(value = "s") String sessionId,
                           @RequestParam(value = "full", required = false) Boolean downloadFull,
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

                if (BooleanUtils.isTrue(downloadFull)) {
                    LogArchiver.writeArchivedLogToStream(logFile, outputStream);
                } else {
                    LogArchiver.writeArchivedLogTailToStream(logFile, outputStream);
                }
            } catch (RuntimeException | IOException ex) {
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
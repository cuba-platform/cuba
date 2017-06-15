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
package com.haulmont.cuba.web.controllers;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileLoader;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Handles file download requests to the web client.
 * <br> This controller is deployed in Spring context defined by {@code cuba.dispatcherSpringContextConfig}
 * app property.
 */
@Controller
public class FileDownloadController {

    private final Logger log = LoggerFactory.getLogger(FileDownloadController.class);

    @Inject
    protected DataService dataService;

    @Inject
    protected UserSessionService userSessionService;

    @Inject
    protected FileLoader fileLoader;

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                log.error(e.toString());
                error(response);
                return null;
            }

            FileDescriptor fd = dataService.load(LoadContext.create(FileDescriptor.class).setId(fileId));
            if (fd == null) {
                log.warn("Unable to find file with id {}", fileId);
                error(response);
                return null;
            }

            String fileName;
            try {
                fileName = URLEncoder.encode(fd.getName(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                log.error(e.toString());
                error(response);
                return null;
            }

            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            response.setDateHeader(HttpHeaders.EXPIRES, 0);
            response.setHeader(HttpHeaders.CONTENT_TYPE, getContentType(fd));
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");

            boolean attach = Boolean.valueOf(request.getParameter("a"));
            response.setHeader("Content-Disposition", (attach ? "attachment" : "inline")
                    + "; filename=" + fileName);

            downloadFromMiddlewareAndWriteResponse(fd, response);
        } finally {
            AppContext.setSecurityContext(null);
        }
        return null;
    }

    protected void downloadFromMiddlewareAndWriteResponse(FileDescriptor fd, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        try (InputStream is = fileLoader.openStream(fd)) {
            IOUtils.copy(is, os);
            os.flush();
        } catch (FileStorageException e) {
            log.error("Unable to load file from middleware", e);
            error(response);
        }
    }

    protected UserSession getSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(request.getParameter("s"));
        } catch (Exception e) {
            return null;
        }

        AppContext.setSecurityContext(new SecurityContext(sessionId));
        try {
            UserSession userSession = userSessionService.getUserSession(sessionId);
            return userSession;
        } catch (NoUserSessionException e) {
            return null;
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    protected String getContentType(FileDescriptor fd) {
        if (StringUtils.isEmpty(fd.getExtension())) {
            return FileTypesHelper.DEFAULT_MIME_TYPE;
        }

        return FileTypesHelper.getMIMEType("." + fd.getExtension().toLowerCase());
    }

    protected void error(HttpServletResponse response) throws IOException {
        if (!response.isCommitted())
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
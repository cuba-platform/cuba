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

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

/**
 */
@Controller
public class FileUploadController {

    private Logger log = LoggerFactory.getLogger(FileUploadController.class);

    @Inject
    protected UserSessionService userSessionService;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected DataService dataService;

    @Inject
    protected Metadata metadata;

    @Resource(name = ClusterInvocationSupport.NAME)
    protected ClusterInvocationSupport clusterInvocationSupport;

    protected static final String CORE_FILE_UPLOAD_CONTEXT = "/upload";

    @RequestMapping(value = "/api/upload", method = RequestMethod.POST)
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
                uploadToMiddleware(userSession, is, fd);
                saveFileDescriptor(fd);

                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter writer = new PrintWriter(response.getOutputStream());
                writer.write(fd.getId().toString());
                writer.close();

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

    protected void uploadToMiddleware(UserSession userSession, InputStream is, FileDescriptor fd)
            throws FileStorageException, InterruptedIOException {
        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String url = iterator.next()
                    + CORE_FILE_UPLOAD_CONTEXT
                    + "?s=" + userSession.getId()
                    + "&f=" + fd.toUrlParam();

            HttpPost method = new HttpPost(url);
            InputStreamEntity entity = new InputStreamEntity(is, -1);

            method.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse coreResponse = client.execute(method);
                int statusCode = coreResponse.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    break;
                } else {
                    log.debug("Unable to upload file to " + url + "\n" + coreResponse.getStatusLine());
                    if (statusCode == HttpStatus.SC_NOT_FOUND && iterator.hasNext())
                        log.debug("Trying next URL");
                    else
                        throw new FileStorageException(FileStorageException.Type.fromHttpStatus(statusCode), fd.getName());
                }
            } catch (InterruptedIOException e) {
                log.trace("Uploading has been interrupted");
                throw e;
            } catch (IOException e) {
                log.debug("Unable to upload file to " + url + "\n" + e);
                if (iterator.hasNext())
                    log.debug("Trying next URL");
                else
                    throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, fd.getName(), e);
            } finally {
                client.getConnectionManager().shutdown();
            }
        }
    }

    protected void saveFileDescriptor(FileDescriptor fd) {
        CommitContext commitContext = new CommitContext(Collections.singleton(fd));
        dataService.commit(commitContext);
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
            return userSessionService.getUserSession(sessionId);
        } catch (NoUserSessionException e) {
            return null;
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    protected FileDescriptor getFileDescriptor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        if (name == null) {
            log.error("'name' request parameter is not specified");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        String ext = request.getParameter("ext");
        if (ext == null) {
            log.error("'ext' request parameter is not specified");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        String size = request.getParameter("size");

        FileDescriptor fd = metadata.create(FileDescriptor.class);
        fd.setName(name);
        fd.setExtension(ext);
        fd.setCreateDate(timeSource.currentTimestamp());
        if (size != null) {
            fd.setSize(Long.parseLong(size));
        }

        return fd;
    }
}

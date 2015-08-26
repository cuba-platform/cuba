/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.portal.restapi;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.UUID;

/**
 * Handles file download requests to the portal client.
 * <p/> This controller is deployed in Spring context defined by <code>cuba.dispatcherSpringContextConfig</code>
 * app property.
 *
 * @author gorodnov
 * @version $Id$
 */
@Controller
public class FileDownloadController {

    private static Logger log = LoggerFactory.getLogger(FileDownloadController.class);

    @Inject
    protected DataService dataService;

    @Inject
    protected UserSessionService userSessionService;

    @Resource(name = ClusterInvocationSupport.NAME)
    protected ClusterInvocationSupport clusterInvocationSupport;

    protected String fileDownloadContext;

    @Inject
    public void setConfiguration(Configuration configuration) {
        fileDownloadContext = configuration.getConfig(ClientConfig.class).getFileDownloadContext();
    }

    @RequestMapping(value = "/api/download", method = RequestMethod.GET)
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

            FileDescriptor fd = dataService.load(new LoadContext<>(FileDescriptor.class).setId(fileId));
            if (fd == null) {
                log.warn("Unable to find file with id " + fileId);
                error(response);
                return null;
            }

            String fileName;
            try {
                fileName = URLEncoder.encode(fd.getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.toString());
                error(response);
                return null;
            }

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Type", getContentType(fd));
            response.setHeader("Pragma", "no-cache");

            boolean attach = Boolean.valueOf(request.getParameter("a"));
            response.setHeader("Content-Disposition", (attach ? "attachment" : "inline")
                    + "; filename=" + fileName);

            writeResponse(response, userSession, fd);

        } finally {
            AppContext.setSecurityContext(null);
        }
        return null;
    }

    private void writeResponse(HttpServletResponse response, UserSession userSession, FileDescriptor fd)
            throws IOException {
        InputStream is = null;
        ServletOutputStream os = response.getOutputStream();
        try {
            for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
                String url = iterator.next() + fileDownloadContext +
                        "?s=" + userSession.getId() +
                        "&f=" + fd.getId().toString();

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);

                try {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    int httpStatus = httpResponse.getStatusLine().getStatusCode();
                    if (httpStatus == HttpStatus.SC_OK) {
                        HttpEntity httpEntity = httpResponse.getEntity();
                        if (httpEntity != null) {
                            is = httpEntity.getContent();
                            IOUtils.copy(is, os);
                            os.flush();
                            break;
                        } else {
                            log.debug("Unable to download file from " + url + "\nHttpEntity is null");
                            if (iterator.hasNext())
                                log.debug("Trying next URL");
                            else
                                error(response);
                        }
                    } else {
                        log.debug("Unable to download file from " + url + "\n" + httpResponse.getStatusLine());
                        if (iterator.hasNext())
                            log.debug("Trying next URL");
                        else
                            error(response);
                    }
                } catch (IOException ex) {
                    log.debug("Unable to download file from " + url + "\n" + ex);
                    if (iterator.hasNext())
                        log.debug("Trying next URL");
                    else
                        error(response);
                } finally {
                    IOUtils.closeQuietly(is);
                    httpClient.getConnectionManager().shutdown();
                }
            }
        } finally {
            IOUtils.closeQuietly(os);
        }
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
            UserSession userSession = userSessionService.getUserSession(sessionId);
            return userSession;
        } catch (NoUserSessionException e) {
            return null;
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    protected String getContentType(FileDescriptor fd) {
        return FileTypesHelper.getMIMEType("." + fd.getExtension().toLowerCase());
    }

    private void error(HttpServletResponse response) throws IOException {
        if (!response.isCommitted())
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}

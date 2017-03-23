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
 */

package com.haulmont.restapi.controllers;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.remoting.LocalFileExchangeService;
import com.haulmont.cuba.core.sys.remoting.discovery.ServerSelector;
import com.haulmont.restapi.exception.RestAPIException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * REST API controller that is used for downloading files by the {@link FileDescriptor} identifier
 */
@RestController
@RequestMapping("/v2/files")
public class FileDownloadController {

    private Logger log = LoggerFactory.getLogger(FileDownloadController.class);

    // Using injection by name here, because an application project can define several instances
    // of ServerSelector type to work with different middleware blocks
    @Resource(name = ServerSelector.NAME)
    protected ServerSelector serverSelector;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected DataService dataService;

    @GetMapping("/{fileDescriptorId}")
    public void downloadFile(@PathVariable String fileDescriptorId,
                             @RequestParam(required = false) Boolean attachment,
                             HttpServletResponse response) {
        UUID uuid;
        try {
            uuid = UUID.fromString(fileDescriptorId);
        } catch (IllegalArgumentException e) {
            throw new RestAPIException("Invalid entity ID",
                    String.format("Cannot convert %s into valid entity ID", fileDescriptorId),
                    HttpStatus.BAD_REQUEST);
        }
        LoadContext<FileDescriptor> ctx = LoadContext.create(FileDescriptor.class).setId(uuid);
        FileDescriptor fd = dataService.load(ctx);
        if (fd == null) {
            throw new RestAPIException("File not found", "File not found. Id: " + fileDescriptorId, HttpStatus.NOT_FOUND);
        }

        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Content-Type", getContentType(fd));
            response.setHeader("Content-Disposition", (BooleanUtils.isTrue(attachment) ? "attachment" : "inline")
                    + "; filename=\"" + fd.getName() + "\"");

            downloadFromMiddlewareAndWriteResponse(fd, response);
        } catch (Exception e) {
            log.error("Error on downloading the file {}", fileDescriptorId, e);
            throw new RestAPIException("Error on downloading the file", "", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected void downloadFromMiddlewareAndWriteResponse(FileDescriptor fd, HttpServletResponse response) throws IOException {
        String useLocalInvocation = AppContext.getProperty("cuba.useLocalServiceInvocation");
        if (Boolean.parseBoolean(useLocalInvocation)) {
            downloadLocallyAndWriteResponse(fd, response);
        } else {
            downloadWithServletAndWriteResponse(fd, response);
        }
    }

    protected void downloadLocallyAndWriteResponse(FileDescriptor fd, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        LocalFileExchangeService fileExchangeService = AppBeans.get(LocalFileExchangeService.NAME);
        InputStream is = fileExchangeService.downloadFile(fd);
        IOUtils.copy(is, os);
        os.flush();
    }

    protected void downloadWithServletAndWriteResponse(FileDescriptor fd, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        Object context = serverSelector.initContext();
        String selectedUrl = serverSelector.getUrl(context);
        if (selectedUrl == null) {
            throw new RestAPIException("Unable to download file from FileStorage",
                    "Unable to download file from FileStorage: " + fd.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        while (true) {
            String url = selectedUrl + "/download" +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&f=" + fd.getId().toString();

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                int httpStatus = httpResponse.getStatusLine().getStatusCode();
                if (httpStatus == org.apache.http.HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        InputStream is = httpEntity.getContent();
                        IOUtils.copy(is, os);
                        os.flush();
                        return;
                    } else {
                        log.debug("Unable to download file from {}. HttpEntity is null", url);
                        selectedUrl = failAndGetNextUrl(context, fd, null);
                    }
                } else {
                    log.debug("Unable to download file from {} {}", url, httpResponse.getStatusLine());
                    selectedUrl = failAndGetNextUrl(context, fd, null);
                }
            } catch (IOException ex) {
                log.debug("Unable to download file from {} {}", url, ex);
                selectedUrl = failAndGetNextUrl(context, fd, ex);
            }
        }
    }

    private String failAndGetNextUrl(Object context, FileDescriptor fd, @Nullable Exception ex) {
        serverSelector.fail(context);
        String url = serverSelector.getUrl(context);
        if (url != null) {
            log.debug("Trying next URL");
            return url;
        } else {
            log.error("Unable to download file from FileStorage: {}", fd.getId(), ex);
            throw new RestAPIException("Unable to download file from FileStorage",
                    "Unable to download file from FileStorage: " + fd.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected String getContentType(FileDescriptor fd) {
        if (StringUtils.isEmpty(fd.getExtension())) {
            return FileTypesHelper.DEFAULT_MIME_TYPE;
        }

        return FileTypesHelper.getMIMEType("." + fd.getExtension().toLowerCase());
    }

}

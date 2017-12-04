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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.remoting.discovery.ServerSelector;
import com.haulmont.restapi.data.FileInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Collections;

/**
 * REST API controller that is used for uploading files
 */
@RestController("cuba_FileUploadController")
@RequestMapping(path = "/v2/files")
public class FileUploadController {

    private final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    // Using injection by name here, because an application project can define several instances
    // of ServerSelector type to work with different middleware blocks
    @Resource(name = ServerSelector.NAME)
    protected ServerSelector serverSelector;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected DataService dataService;

    @Inject
    protected FileLoader fileLoader;

    /**
     * Method for simple file upload. File contents are placed in the request body. Optional file name parameter is
     * passed as a query param.
     */
    @PostMapping(consumes = "!multipart/form-data")
    public ResponseEntity<FileInfo> uploadFile(HttpServletRequest request,
                                               @RequestParam(required = false) String name) {
        try {
            String contentLength = request.getHeader("Content-Length");

            long size = 0;
            try {
                size = Long.parseLong(contentLength);
            } catch (NumberFormatException ignored) {
            }

            FileDescriptor fd = createFileDescriptor(name, size);

            ServletInputStream is = request.getInputStream();
            uploadToMiddleware(is, fd);
            saveFileDescriptor(fd);

            return createFileInfoResponseEntity(request, fd);
        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RestAPIException("File upload failed", "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for multipart file upload. It expects the file contents to be passed in the part called 'file'
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam(required = false) String name,
                                               HttpServletRequest request) {
        try {
            if (Strings.isNullOrEmpty(name)) {
                name = file.getOriginalFilename();
            }

            long size = file.getSize();
            FileDescriptor fd = createFileDescriptor(name, size);

            InputStream is = file.getInputStream();
            uploadToMiddleware(is, fd);
            saveFileDescriptor(fd);

            return createFileInfoResponseEntity(request, fd);
        } catch (Exception e) {
            log.error("File upload failed", e);
            throw new RestAPIException("File upload failed", "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected ResponseEntity<FileInfo> createFileInfoResponseEntity(HttpServletRequest request, FileDescriptor fd) {
        FileInfo fileInfo = new FileInfo(fd.getId(), fd.getName(), fd.getSize());

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .path("/{id}")
                .buildAndExpand(fd.getId().toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(fileInfo, httpHeaders, HttpStatus.CREATED);
    }

    protected void saveFileDescriptor(FileDescriptor fd) {
        CommitContext commitContext = new CommitContext(Collections.singleton(fd));
        dataService.commit(commitContext);
    }

    protected FileDescriptor createFileDescriptor(@Nullable String fileName, long size) {
        FileDescriptor fd = metadata.create(FileDescriptor.class);
        if (Strings.isNullOrEmpty(fileName)) {
            fileName = fd.getId().toString();
        }
        fd.setName(fileName);
        fd.setExtension(FilenameUtils.getExtension(fileName));
        fd.setSize(size);
        fd.setCreateDate(timeSource.currentTimestamp());
        return fd;
    }

    protected void uploadToMiddleware(InputStream is, FileDescriptor fd) throws FileStorageException {
        try {
            fileLoader.saveStream(fd, new FileLoader.SingleInputStreamSupplier(is));
        } catch (FileStorageException e) {
            throw new RestAPIException("Unable to upload file to FileStorage",
                    "Unable to upload file to FileStorage: " + fd.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
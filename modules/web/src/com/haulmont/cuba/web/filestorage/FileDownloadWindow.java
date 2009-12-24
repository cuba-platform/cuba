/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 20.11.2009 16:02:58
 *
 * $Id$
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.haulmont.cuba.gui.ServiceLocator;
import com.vaadin.terminal.DownloadStream;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;

public class FileDownloadWindow extends FileWindow {

    public FileDownloadWindow(String windowName, FileDescriptor fd) {
        super(windowName, fd);
    }

    public FileDownloadWindow(String windowName, File f) {
        super(windowName, f);
    }

    @Override
    public DownloadStream handleURI(URL context, String relativeUri) {
        DownloadStream downloadStream = new FileDownloadStream(new ByteArrayInputStream(getFileData()),
                FileTypesHelper.getMIMEType("." + getExtension()), getFileName());

        return downloadStream;
    }

    protected String getFileName() {
        if (fd != null) {
            try {
                return URLEncoder.encode(fd.getName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else if (f != null) {
            return f.getName();
        } else {
            throw new RuntimeException("file descriptor and file are null");
        }
    }

    protected byte[] getFileData() {
        if (fd != null) {
            FileStorageService fss = ServiceLocator.lookup(FileStorageService.JNDI_NAME);
            try {
                return fss.loadFile(fd);
            } catch (FileStorageException e) {
                log.error("Unable to download file", e);
                throw new RuntimeException(e);
            }
        } else if (f != null){
            try {
                return FileUtils.readFileToByteArray(f);
            } catch (IOException e) {
                log.error("Unable to download file", e);
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("file descriptor and file are null");
        }
    }

    protected String getExtension() {
        if (fd != null) {
            return fd.getExtension();
        } else if (f != null){
            int i = f.getName().lastIndexOf(".");
            return i > 0 ? f.getName().substring(i, f.getName().length()) : "txt";
        } else {
            throw new RuntimeException("file descriptor and file are null");
        }
    }

    public class FileDownloadStream extends DownloadStream implements Closeable {
        public FileDownloadStream(InputStream stream, String contentType, String fileName) {
            super(stream, contentType, fileName);
        }

        public void close() throws IOException {
        }
    }
}

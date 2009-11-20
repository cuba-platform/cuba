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

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.FileStorageMBean;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.FileTypesHelper;
import com.vaadin.terminal.DownloadStream;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

public class FileDownloadWindow extends FileWindow {

    public FileDownloadWindow(String windowName, FileDescriptor fd) {
        super(windowName, fd);
    }

    @Override
    public DownloadStream handleURI(URL context, String relativeUri) {
        String fileName;
        try {
            fileName = URLEncoder.encode(fd.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        byte[] data;
        FileStorageMBean mbean = Locator.lookupMBean(FileStorageMBean.class, FileStorageMBean.OBJECT_NAME);
        try {
            data = mbean.getAPI().loadFile(fd);
        } catch (FileStorageException e) {
            log.error("Unable to download file", e);
            throw new RuntimeException(e);
        }

        DownloadStream downloadStream = new FileDownloadStream(new ByteArrayInputStream(data),
                FileTypesHelper.getMIMEType("." + fd.getExtension()), fileName);

        return downloadStream;
    }

    public class FileDownloadStream extends DownloadStream implements Closeable {
        public FileDownloadStream(InputStream stream, String contentType, String fileName) {
            super(stream, contentType, fileName);
        }

        public void close() throws IOException {
        }
    }
}

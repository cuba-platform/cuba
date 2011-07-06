/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.UserSessionProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Data provider for FileDescriptor
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class FileDataProvider implements ExportDataProvider {

    private FileDescriptor fileDescriptor;
    private InputStream inputStream;
    private boolean closed = false;

    public FileDataProvider(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public InputStream provide() {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        if (fileDescriptor == null)
            throw new IllegalArgumentException("Null file descriptor");

        String fileDownloadContext = ConfigProvider.getConfig(ClientConfig.class).getFileDownloadContext();
        try {
            String connectionUrl = ConfigProvider.getConfig(ClientConfig.class).getConnectionUrl();
            URL url = new URL(connectionUrl + fileDownloadContext +
                    "?s=" + UserSessionProvider.getUserSession().getId() +
                    "&f=" + fileDescriptor.getId().toString());
            inputStream = url.openStream();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return inputStream;
    }

    public void close() {
        if (inputStream != null) {
            closed = true;
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                inputStream = null;
                fileDescriptor = null;
            }
        } else
            throw new RuntimeException("DataProvider is closed");
    }
}

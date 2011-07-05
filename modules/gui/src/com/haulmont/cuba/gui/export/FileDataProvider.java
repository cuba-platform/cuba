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

    public FileDataProvider(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public InputStream provide() {
        String fileDownloadContext = ConfigProvider.getConfig(ClientConfig.class).getFileDownloadContext();
        try {
            // TODO How about not saved fileDescriptors ?
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
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inputStream = null;
        } else
            throw new RuntimeException("DataProvider is closed");
    }
}

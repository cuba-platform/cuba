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
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.*;

/**
 * Data provider for FileDescriptor
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class FileDataProvider implements ExportDataProvider {

    private static final int HTTP_OK = 200;
    private static final int HTTP_NOT_FOUND = 468;

    private FileDescriptor fileDescriptor;
    private InputStream inputStream;
    private boolean closed = false;

    protected ClientConnectionManager connectionManager;

    public FileDataProvider(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
    }

    public InputStream provide() {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        if (fileDescriptor == null)
            throw new IllegalArgumentException("Null file descriptor");

        String fileDownloadContext = ConfigProvider.getConfig(ClientConfig.class).getFileDownloadContext();
        String connectionUrl = ConfigProvider.getConfig(ClientConfig.class).getConnectionUrl();

        String url = connectionUrl + fileDownloadContext +
                "?s=" + UserSessionProvider.getUserSession().getId() +
                "&f=" + fileDescriptor.getId().toString();

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            switch (httpResponse.getStatusLine().getStatusCode()) {
                case HTTP_OK:
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null)
                        inputStream = httpEntity.getContent();
                    else
                        throw new IOException("Couldn't load file from core layer");
                    break;

                case HTTP_NOT_FOUND:
                    throw new FileMissingException(fileDescriptor.getName());

                default:
                    throw new IOException("Unknown status code");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            connectionManager = httpClient.getConnectionManager();
        }

        return inputStream;
    }

    public void close() {
        if (inputStream != null) {
            closed = true;
            try {
                inputStream.close();
                if (connectionManager != null)
                    connectionManager.shutdown();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                inputStream = null;
                fileDescriptor = null;
                connectionManager = null;
            }
        }
    }
}

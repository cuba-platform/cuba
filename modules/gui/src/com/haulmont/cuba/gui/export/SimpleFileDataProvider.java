/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.UserSessionProvider;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * <p>$Id$</p>
 * Data provider for File on name and path
 *
 * @author novikov
 */
public class SimpleFileDataProvider implements ExportDataProvider {

    private static final int HTTP_OK = 200;

    private String filePath;
    private InputStream inputStream;
    private boolean closed = false;

    protected ClientConnectionManager connectionManager;

    public SimpleFileDataProvider(String filePath) {
        this.filePath = filePath;
    }

    public InputStream provide() {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        if (filePath == null)
            throw new IllegalArgumentException("Null file path");

        String fileDownloadContext = ConfigProvider.getConfig(ClientConfig.class).getFileDownloadContext();
        String connectionUrl = ConfigProvider.getConfig(ClientConfig.class).getConnectionUrl();
        String url = connectionUrl + fileDownloadContext +
                "?s=" + UserSessionProvider.getUserSession().getId() +
                "&p=" + encodeUTF8(filePath);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int httpStatus = httpResponse.getStatusLine().getStatusCode();
            switch (httpStatus) {
                case HTTP_OK:
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null)
                        inputStream = httpEntity.getContent();
                    else
                        throw new FileStorageException(FileStorageException.Type.IO_EXCEPTION, filePath);
                    break;
                default:
                    throw new FileStorageException(FileStorageException.Type.fromHttpStatus(httpStatus), filePath);
            }
        } catch (Exception ex) {
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
                filePath = null;
                connectionManager = null;
            }
        }
    }

    protected String encodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF8");
        } catch (Exception e) {
            throw new IllegalArgumentException("Encode string from URL param failed", e);
        }
    }
}

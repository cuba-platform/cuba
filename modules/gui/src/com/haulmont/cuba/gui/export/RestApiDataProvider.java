/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class providing data using Rest API by the query given
 *
 * @author korotkov
 * @version $Id$
 */
public class RestApiDataProvider implements ExportDataProvider {

    public static final String API_URL = "/api/";

    private static final int HTTP_OK = 200;

    private ClientConnectionManager connectionManager;
    private String query;
    private InputStream inputStream;
    private boolean closed = false;

    public RestApiDataProvider(String query) {
        this.query = query;
    }

    @Override
    public InputStream provide() throws ResourceException {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        String connectionUrl = ConfigProvider.getConfig(ClientConfig.class).getConnectionUrl();
        String url = connectionUrl + API_URL + query;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == HTTP_OK) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null)
                    inputStream = httpEntity.getContent();
                else
                    throw new RuntimeException("Cannot retrieve data using Rest API and the query given: " + query);
            } else {
                throw new RuntimeException("Cannot retrieve data using Rest API and the query given: " + query
                        + ". Http status: " + status);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager = httpClient.getConnectionManager();
        }

        return inputStream;
    }

    @Override
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
                connectionManager = null;
            }
        }
    }
}

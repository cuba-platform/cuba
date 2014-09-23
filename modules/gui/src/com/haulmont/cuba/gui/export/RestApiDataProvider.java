/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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

    protected Log log = LogFactory.getLog(getClass());

    protected ClientConnectionManager connectionManager;
    protected String query;
    protected InputStream inputStream;
    protected boolean closed = false;

    protected String restApiUrl;

    public RestApiDataProvider(String query) {
        this.query = query;
        Configuration configuration = AppBeans.get(Configuration.NAME);
        restApiUrl = configuration.getConfig(GlobalConfig.class).getRestApiUrl();
        if (!restApiUrl.endsWith("/"))
            restApiUrl = restApiUrl + "/";
    }

    @Override
    public InputStream provide() throws ResourceException, ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

        String url = restApiUrl + query;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    inputStream = httpEntity.getContent();
                } else {
                    throw new RuntimeException("Unable to retrieve data using REST API from " + url
                            + "\nHttpEntity is null");
                }
            } else {
                throw new RuntimeException("Unable to retrieve data using REST API from " + url + "\n"
                        + response.getStatusLine());
            }
        } catch (IOException e) {
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
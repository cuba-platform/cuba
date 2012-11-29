/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
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
import java.util.Iterator;

/**
 * Class providing data using Rest API by the query given
 *
 * @author korotkov
 * @version $Id$
 */
public class RestApiDataProvider implements ExportDataProvider {

    public static final String API_URL = "/api/";

    private Log log = LogFactory.getLog(getClass());

    protected ClientConnectionManager connectionManager;
    protected String query;
    protected InputStream inputStream;
    protected boolean closed = false;

    protected ClusterInvocationSupport clusterInvocationSupport = AppBeans.get(ClusterInvocationSupport.NAME);

    public RestApiDataProvider(String query) {
        this.query = query;
    }

    @Override
    public InputStream provide() throws ResourceException {
        if (closed)
            throw new IllegalStateException("DataProvider is closed");

        int remotingServletPathLen = clusterInvocationSupport.getServletPath().length() + 1;

        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String remotingUrl = iterator.next();
            String url = remotingUrl.substring(0, remotingUrl.length() - remotingServletPathLen) + API_URL + query;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = response.getEntity();
                    if (httpEntity != null) {
                        inputStream = httpEntity.getContent();
                        break;
                    } else {
                        log.debug("Unable to retrieve data using REST API from " + url + "\nHttpEntity is null");
                        if (iterator.hasNext())
                            log.debug("Trying next URL");
                        else
                            throw new RuntimeException("Unable to retrieve data using REST API from " + url
                                    + "\nHttpEntity is null");
                    }
                } else {
                    log.debug("Unable to retrieve data using REST API from " + url + "\n" + response.getStatusLine());
                    if (iterator.hasNext())
                        log.debug("Trying next URL");
                    else
                        throw new RuntimeException("Unable to retrieve data using REST API from " + url + "\n"
                                + response.getStatusLine());
                }
            } catch (IOException e) {
                log.debug("Unable to retrieve data using REST API from " + url + "\n" + e);
                if (iterator.hasNext())
                    log.debug("Trying next URL");
                else
                    throw new RuntimeException(e);
            } finally {
                connectionManager = httpClient.getConnectionManager();
            }
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

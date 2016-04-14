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
 *
 */

package com.haulmont.cuba.web.export;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.web.jmx.JmxRemoteLoggingAPI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Data provider for log files from specified JMX instance
 *
 */
public class LogDataProvider implements ExportDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected InputStream inputStream;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected JmxRemoteLoggingAPI jmxRemoteLoggingAPI = AppBeans.get(JmxRemoteLoggingAPI.NAME);

    protected final String logFileName;

    protected JmxInstance jmxInstance;

    protected String remoteContext;
    protected boolean downloadFullLog = false;

    protected String url;

    public LogDataProvider(JmxInstance jmxInstance, String logFileName) {
        this(jmxInstance, logFileName, null, false);
    }

    public LogDataProvider(JmxInstance jmxInstance, String logFileName, boolean downloadFullLog) {
        this(jmxInstance, logFileName, null, downloadFullLog);
    }

    public LogDataProvider(JmxInstance jmxInstance, String logFileName, String remoteContext, boolean downloadFullLog) {
        this.logFileName = logFileName;
        this.jmxInstance = jmxInstance;
        this.remoteContext = remoteContext;
        this.downloadFullLog = downloadFullLog;
    }

    public void obtainUrl() {
        try {
            url = jmxRemoteLoggingAPI.getLogFileLink(jmxInstance, remoteContext, logFileName);
        } catch (Exception e) {
            log.error("Unable to get log file link from JMX interface");

            throw new RuntimeException(e);
        }
    }

    /**
    * You should call {@link LogDataProvider#obtainUrl()} before
    * */
    @Override
    public InputStream provide() {
        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        String uri = url + "?s=" + userSessionSource.getUserSession().getId();
        if (downloadFullLog) {
            uri += "&full=true";
        }

        HttpGet httpGet = new HttpGet(uri);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int httpStatus = httpResponse.getStatusLine().getStatusCode();
            if (httpStatus == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    inputStream = httpEntity.getContent();
                } else {
                    log.debug("Unable to download log from " + url + "\nHttpEntity is null");

                    throw new RuntimeException("Unable to download log from " + url + "\nHttpEntity is null");
                }
            } else {
                log.debug("Unable to download log from " + url + "\n" + httpResponse.getStatusLine());
                throw new RuntimeException("Unable to download log from " + url + "\n" + httpResponse.getStatusLine());
            }
        } catch (IOException e) {
            log.debug("Unable to download log from " + url + "\n" + e);
            throw new RuntimeException(e);
        } finally {
            connectionManager.shutdown();
        }

        return inputStream;
    }
}
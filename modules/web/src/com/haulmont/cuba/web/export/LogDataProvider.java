/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.export;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.export.ClosedDataProviderException;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ResourceException;
import com.haulmont.cuba.web.jmx.JmxRemoteLoggingAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Data provider for log files from specified JMX instance
 *
 * @author artamonov
 * @version $Id$
 */
public class LogDataProvider implements ExportDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected InputStream inputStream;
    protected boolean closed = false;

    protected ClientConnectionManager connectionManager;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected JmxRemoteLoggingAPI jmxRemoteLoggingAPI = AppBeans.get(JmxRemoteLoggingAPI.NAME);

    protected final String logFileName;

    protected JmxInstance jmxInstance;

    protected String remoteContext;
    protected boolean downloadFullLog = false;

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

    @Override
    public InputStream provide() throws ResourceException, ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

        String url;
        try {
            url = jmxRemoteLoggingAPI.getLogFileLink(jmxInstance, remoteContext, logFileName);
        } catch (Exception e) {
            log.error("Unable to get log file link from JMX interface");

            throw new ResourceException(e);
        }

        HttpClient httpClient = new DefaultHttpClient();
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

                    throw new ResourceException("Unable to download log from " + url + "\nHttpEntity is null");
                }
            } else {
                log.debug("Unable to download log from " + url + "\n" + httpResponse.getStatusLine());
                throw new ResourceException("Unable to download log from " + url + "\n" + httpResponse.getStatusLine());
            }
        } catch (IOException ex) {
            log.debug("Unable to download log from " + url + "\n" + ex);
            throw new ResourceException(ex);
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
                log.warn("Error while closing log data provider", e);
            } finally {
                inputStream = null;
                connectionManager = null;
            }
        }
    }
}
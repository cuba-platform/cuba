/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.export;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.export.ClosedDataProviderException;
import com.haulmont.cuba.gui.export.ExportDataProvider;
import com.haulmont.cuba.gui.export.ResourceException;
import com.haulmont.cuba.web.jmx.JmxRemoteLoggingAPI;
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
 * Data provider for log files from specified JMX instance
 *
 * @author artamonov
 * @version $Id$
 */
public class LogDataProvider implements ExportDataProvider {

    protected Log log = LogFactory.getLog(getClass());

    protected FileDescriptor fileDescriptor;
    protected InputStream inputStream;
    protected boolean closed = false;

    protected ClientConnectionManager connectionManager;

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    protected JmxRemoteLoggingAPI jmxRemoteLoggingAPI = AppBeans.get(JmxRemoteLoggingAPI.NAME);

    protected final String logFileName;

    protected JmxInstance jmxInstance;

    protected boolean downloadFullLog = false;

    public LogDataProvider(JmxInstance jmxInstance, String logFileName) {
        this.logFileName = logFileName;
        this.jmxInstance = jmxInstance;
    }

    public LogDataProvider(JmxInstance jmxInstance, String logFileName, boolean downloadFullLog) {
        this.logFileName = logFileName;
        this.jmxInstance = jmxInstance;
        this.downloadFullLog = downloadFullLog;
    }

    @Override
    public InputStream provide() throws ResourceException, ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

        String url;
        try {
            url = jmxRemoteLoggingAPI.getLogFileLink(jmxInstance, logFileName);
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
                throw new RuntimeException(e);
            } finally {
                inputStream = null;
                fileDescriptor = null;
                connectionManager = null;
            }
        }
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
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
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Data provider for File on name and path
 *
 * @author novikov
 * @version $Id$
 */
public class SimpleFileDataProvider implements ExportDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected String filePath;
    protected InputStream inputStream;
    protected boolean closed = false;

    protected ClientConnectionManager connectionManager;

    protected ClusterInvocationSupport clusterInvocationSupport = AppBeans.get(ClusterInvocationSupport.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    protected String fileDownloadContext;

    public SimpleFileDataProvider(String filePath) {
        this.filePath = filePath;
        fileDownloadContext = configuration.getConfig(ClientConfig.class).getFileDownloadContext();
    }

    @Override
    public InputStream provide() throws ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

        if (filePath == null)
            throw new IllegalArgumentException("Null file path");

        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String url = iterator.next() + fileDownloadContext +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&p=" + encodeUTF8(filePath);

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int httpStatus = httpResponse.getStatusLine().getStatusCode();
                if (httpStatus == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        inputStream = httpEntity.getContent();
                        break;
                    } else {
                        log.debug("Unable to download file from " + url + "\nHttpEntity is null");
                        if (iterator.hasNext())
                            log.debug("Trying next URL");
                        else
                            throw new RuntimeException(
                                    new FileStorageException(FileStorageException.Type.IO_EXCEPTION, filePath)
                            );
                    }
                } else {
                    log.debug("Unable to download file from " + url + "\n" + httpResponse.getStatusLine());
                    if (iterator.hasNext())
                        log.debug("Trying next URL");
                    else
                        throw new RuntimeException(
                                new FileStorageException(FileStorageException.Type.fromHttpStatus(httpStatus), filePath)
                        );
                }
            } catch (IOException ex) {
                log.debug("Unable to download file from " + url + "\n" + ex);
                if (iterator.hasNext())
                    log.debug("Trying next URL");
                else
                    throw new RuntimeException(
                            new FileStorageException(FileStorageException.Type.IO_EXCEPTION, filePath, ex)
                    );
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
                log.warn("Error while closing simple file data provider", e);
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
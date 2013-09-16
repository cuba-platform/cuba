/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
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
 * Data provider for FileDescriptor
 *
 * @author artamonov
 * @version $Id$
 */
public class FileDataProvider implements ExportDataProvider {

    protected Log log = LogFactory.getLog(getClass());

    protected FileDescriptor fileDescriptor;
    protected InputStream inputStream;
    protected boolean closed = false;

    protected ClientConnectionManager connectionManager;

    protected ClusterInvocationSupport clusterInvocationSupport = AppBeans.get(ClusterInvocationSupport.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    protected Configuration configuration = AppBeans.get(Configuration.class);

    protected String fileDownloadContext;

    public FileDataProvider(FileDescriptor fileDescriptor) {
        this.fileDescriptor = fileDescriptor;
        fileDownloadContext = configuration.getConfig(ClientConfig.class).getFileDownloadContext();
    }

    @Override
    public InputStream provide() throws ClosedDataProviderException {
        if (closed)
            throw new ClosedDataProviderException();

        if (fileDescriptor == null)
            throw new IllegalArgumentException("Null file descriptor");

        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String url = iterator.next() + fileDownloadContext +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&f=" + fileDescriptor.getId().toString();

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
                                    new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                                            fileDescriptor.getName())
                            );
                    }
                } else {
                    log.debug("Unable to download file from " + url + "\n" + httpResponse.getStatusLine());
                    if (iterator.hasNext())
                        log.debug("Trying next URL");
                    else
                        throw new RuntimeException(
                                new FileStorageException(FileStorageException.Type.fromHttpStatus(httpStatus),
                                        fileDescriptor.getName())
                        );
                }
            } catch (IOException ex) {
                log.debug("Unable to download file from " + url + "\n" + ex);
                if (iterator.hasNext())
                    log.debug("Trying next URL");
                else
                    throw new RuntimeException(
                            new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                                    fileDescriptor.getName(), ex)
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
                log.warn("Error while closing file data provider", e);
            } finally {
                inputStream = null;
                fileDescriptor = null;
                connectionManager = null;
            }
        }
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
import com.haulmont.cuba.core.sys.remoting.LocalFileExchangeService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Data provider for FileDescriptor
 *
 * @author artamonov
 */
public class FileDataProvider implements ExportDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected FileDescriptor fileDescriptor;
    protected InputStream inputStream;

    protected ClusterInvocationSupport clusterInvocationSupport = AppBeans.get(ClusterInvocationSupport.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    protected String fileDownloadContext;

    public FileDataProvider(FileDescriptor fileDescriptor) {
        Preconditions.checkNotNullArgument(fileDescriptor, "Null file descriptor");

        this.fileDescriptor = fileDescriptor;

        fileDownloadContext = configuration.getConfig(ClientConfig.class).getFileDownloadContext();
    }

    @Override
    public InputStream provide() {
        String useLocalInvocation = AppContext.getProperty("cuba.useLocalServiceInvocation");
        if (Boolean.parseBoolean(useLocalInvocation)) {
            downloadLocally();
        } else {
            downloadWithServlet();
        }

        return inputStream;
    }

    protected void downloadLocally() {
        try {
            SecurityContext securityContext = new SecurityContext(userSessionSource.getUserSession());
            AppContext.setSecurityContext(securityContext);

            LocalFileExchangeService fileExchangeService = AppBeans.get(LocalFileExchangeService.NAME);
            inputStream = fileExchangeService.downloadFile(fileDescriptor);
        } finally {
            AppContext.setSecurityContext(null);
        }
    }

    protected void downloadWithServlet() {
        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String url = iterator.next() + fileDownloadContext +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&f=" + fileDescriptor.getId().toString();

            HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connectionManager)
                    .build();

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
                    if (iterator.hasNext()) {
                        log.debug("Trying next URL");
                    } else {
                        throw new RuntimeException(
                                new FileStorageException(FileStorageException.Type.fromHttpStatus(httpStatus),
                                        fileDescriptor.getName())
                        );
                    }
                }
            } catch (IOException ex) {
                log.debug("Unable to download file from " + url + "\n" + ex);
                if (iterator.hasNext()) {
                    log.debug("Trying next URL");
                } else {
                    throw new RuntimeException(
                            new FileStorageException(FileStorageException.Type.IO_EXCEPTION,
                                    fileDescriptor.getName(), ex)
                    );
                }
            } finally {
                connectionManager.shutdown();
            }
        }
    }
}
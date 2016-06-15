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

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.remoting.ClusterInvocationSupport;
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
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Data provider for File on name and path
 *
 */
public class SimpleFileDataProvider implements ExportDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected String filePath;
    protected InputStream inputStream;

    protected ClusterInvocationSupport clusterInvocationSupport = AppBeans.get(ClusterInvocationSupport.NAME);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected Configuration configuration = AppBeans.get(Configuration.NAME);

    protected String fileDownloadContext;

    public SimpleFileDataProvider(String filePath) {
        this.filePath = filePath;
        fileDownloadContext = configuration.getConfig(ClientConfig.class).getFileDownloadContext();
    }

    @Override
    public InputStream provide() {
        if (filePath == null)
            throw new IllegalArgumentException("Null file path");

        for (Iterator<String> iterator = clusterInvocationSupport.getUrlList().iterator(); iterator.hasNext(); ) {
            String url = iterator.next() + fileDownloadContext +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&p=" + encodeUTF8(filePath);

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
            }
        }

        return inputStream;
    }

    protected String encodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF8");
        } catch (Exception e) {
            throw new IllegalArgumentException("Encode string from URL param failed", e);
        }
    }
}
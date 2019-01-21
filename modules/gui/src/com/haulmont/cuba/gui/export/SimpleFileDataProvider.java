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

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.remoting.discovery.ServerSelector;
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

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Data provider for downloading files from the middle tier by the path in the file system.
 * <p>
 * The path must be listed in the `cuba.download.directories` application property.
 */
public class SimpleFileDataProvider implements ExportDataProvider {

    private final Logger log = LoggerFactory.getLogger(SimpleFileDataProvider.class);

    protected String filePath;
    protected InputStream inputStream;

    protected ServerSelector serverSelector = AppBeans.get(ServerSelector.NAME);

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
            throw new IllegalStateException("File path is null");

        Object context = serverSelector.initContext();
        String selectedUrl = serverSelector.getUrl(context);
        if (selectedUrl == null) {
            throw new RuntimeException(String.format("Unable to download file '%s': no available server URLs", filePath));
        }
        while (true) {
            String url = selectedUrl + fileDownloadContext +
                    "?s=" + userSessionSource.getUserSession().getId() +
                    "&p=" + URLEncodeUtils.encodeUtf8(filePath);

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
                        selectedUrl = failAndGetNextUrl(context);
                        if (selectedUrl == null)
                            throw new RuntimeException(String.format("Unable to download file '%s': HttpEntity is null", filePath));
                    }
                } else {
                    log.debug("Unable to download file from " + url + "\n" + httpResponse.getStatusLine());
                    selectedUrl = failAndGetNextUrl(context);
                    if (selectedUrl == null)
                        throw new RuntimeException(String.format("Unable to download file '%s': HTTP status is %d", filePath, httpStatus));
                }
            } catch (IOException ex) {
                log.debug("Unable to download file from " + url + "\n" + ex);
                selectedUrl = failAndGetNextUrl(context);
                if (selectedUrl == null)
                    throw new RuntimeException(String.format("Unable to download file '%s'", filePath), ex);
            }
        }

        return inputStream;
    }

    @Nullable
    private String failAndGetNextUrl(Object context) {
        serverSelector.fail(context);
        String url = serverSelector.getUrl(context);
        if (url != null)
            log.debug("Trying next URL");
        return url;
    }
}
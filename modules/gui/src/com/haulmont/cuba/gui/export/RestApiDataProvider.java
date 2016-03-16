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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
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
 * Class providing data using Rest API by the query given
 *
 */
public class RestApiDataProvider implements ExportDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected String query;
    protected InputStream inputStream;

    protected String restApiUrl;

    public RestApiDataProvider(String query) {
        this.query = query;
        Configuration configuration = AppBeans.get(Configuration.NAME);
        restApiUrl = configuration.getConfig(GlobalConfig.class).getRestApiUrl();
        if (!restApiUrl.endsWith("/"))
            restApiUrl = restApiUrl + "/";
    }

    @Override
    public InputStream provide() {
        String url = restApiUrl + query;

        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();
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
            connectionManager.shutdown();
        }

        return inputStream;
    }
}
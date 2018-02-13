/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.security.idp;

import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component("cuba_IdpSessionPingConnector")
public class IdpSessionPingConnector {

    private static final Logger log = LoggerFactory.getLogger(IdpSessionPingConnector.class);

    @Inject
    protected WebIdpConfig webIdpConfig;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected AuthenticationService authenticationService;

    public void pingIdpSessionServer(String idpSessionId) {
        log.debug("Ping IDP session {}", idpSessionId);

        String idpBaseURL = webIdpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpSessionPingUrl = idpBaseURL + "service/ping";

        HttpPost httpPost = new HttpPost(idpSessionPingUrl);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("idpSessionId", idpSessionId),
                new BasicNameValuePair("trustedServicePassword", webIdpConfig.getIdpTrustedServicePassword())
        ), StandardCharsets.UTF_8);

        httpPost.setEntity(formEntity);

        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpClient client = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        try {
            HttpResponse httpResponse = client.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 410) {
                // we have to logout user
                log.debug("IDP session is expired {}", idpSessionId);

                if (userSessionSource.checkCurrentUserSession()) {
                    authenticationService.logout();

                    UserSession userSession = userSessionSource.getUserSession();

                    throw new NoUserSessionException(userSession.getId());
                }
            }
            if (statusCode != 200) {
                log.warn("IDP respond status {} on session ping", statusCode);
            }
        } catch (IOException e) {
            log.warn("Unable to ping IDP {} session {}", idpSessionPingUrl, idpSessionId, e);
        } finally {
            connectionManager.shutdown();
        }
    }
}
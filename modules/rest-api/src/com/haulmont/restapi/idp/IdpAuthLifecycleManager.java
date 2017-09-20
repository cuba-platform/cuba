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

package com.haulmont.restapi.idp;

import com.google.gson.Gson;
import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.restapi.auth.OAuthTokenRevoker;
import com.haulmont.restapi.events.BeforeRestInvocationEvent;
import com.haulmont.restapi.events.OAuthTokenRevokedResponseEvent;
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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static com.haulmont.restapi.idp.IdpAuthController.IDP_SESSION_ID_TOKEN_ATTRIBUTE;

/**
 * Component that synchronizes REST API token and IDP session life cycles.
 */
@Component("cuba_IdpAuthLifecycleManager")
public class IdpAuthLifecycleManager implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(IdpAuthLifecycleManager.class);

    @Inject
    protected Configuration configuration;
    @Inject
    protected OAuthTokenRevoker oAuthTokenRevoker;
    @Inject
    protected TokenStore tokenStore;

    protected RestIdpConfig idpConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.idpConfig = configuration.getConfig(RestIdpConfig.class);
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 100)
    @EventListener
    public void handleBeforeRestInvocationEvent(BeforeRestInvocationEvent event) {
        if (idpConfig.getIdpEnabled()) {
            if (idpConfig.getIdpPingSessionOnRequest()
                    && event.getAuthentication() instanceof OAuth2Authentication) {
                IdpSessionStatus status = pingIdpSession(event.getAuthentication());

                if (status == IdpSessionStatus.EXPIRED) {
                    Object details = event.getAuthentication().getDetails();
                    String accessToken = ((OAuth2AuthenticationDetails) details).getTokenValue();

                    oAuthTokenRevoker.revokeToken(accessToken);

                    log.info("IDP session is expired. REST token {} revoked", accessToken);

                    event.preventInvocation();

                    String idpLoginUrl = getIdpLoginUrl(idpConfig.getIdpDefaultRedirectUrl());
                    Gson gson = new Gson();
                    String body = gson.toJson(new IdpSessionExpiredResponse("idp_session_expired", idpLoginUrl));

                    HttpServletResponse response = (HttpServletResponse) event.getResponse();
                    try {
                        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
                        response.getWriter().write(body);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to send status to client", e);
                    }
                }
            }
        }
    }

    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 100)
    @EventListener
    public void handleOAuthTokenRevocationResponse(OAuthTokenRevokedResponseEvent event) {
        if (idpConfig.getIdpEnabled()) {
            if (event.getAccessToken() != null) {
                log.debug("OAuth2AccessToken {} revoked by client, redirect to IDP", event.getAccessToken());

                String idpLoginUrl = getIdpLogoutUrl(idpConfig.getIdpDefaultRedirectUrl());

                event.setResponseEntity(ResponseEntity.ok(new IdpLogoutResponse(idpLoginUrl)));
            }
        }
    }

    protected String getIdpLoginUrl(String redirectUrl) {
        String idpBaseURL = idpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }

        if (redirectUrl == null) {
            return idpBaseURL +
                    "?response_type=client-ticket";
        }

        return idpBaseURL +
                "?response_type=client-ticket" +
                "&sp=" + URLEncodeUtils.encodeUtf8(redirectUrl);
    }

    protected String getIdpLogoutUrl(String redirectUrl) {
        String idpBaseURL = idpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }

        return idpBaseURL +
                "logout?response_type=client-ticket" +
                "&sp=" + URLEncodeUtils.encodeUtf8(redirectUrl);
    }

    protected IdpSessionStatus pingIdpSession(Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            Object details = authentication.getDetails();
            String accessTokenId = ((OAuth2AuthenticationDetails) details).getTokenValue();

            OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenId);
            if (accessToken == null) {
                return IdpSessionStatus.UNSUPPORTED;
            }

            String idpSessionId = getIdpSessionId(accessToken);
            if (idpSessionId == null) {
                return IdpSessionStatus.UNSUPPORTED;
            }

            return pingIdpSessionServer(idpSessionId);
        }

        return IdpSessionStatus.UNSUPPORTED;
    }

    protected String getIdpSessionId(OAuth2AccessToken accessToken) {
        Map<String, Object> details = accessToken.getAdditionalInformation();
        if (details == null) {
            // OAuth2AccessToken does not contain details
            return null;
        }

        return (String) details.get(IDP_SESSION_ID_TOKEN_ATTRIBUTE);
    }

    protected IdpSessionStatus pingIdpSessionServer(String idpSessionId) {
        log.debug("Ping IDP session {}", idpSessionId);

        String idpBaseURL = idpConfig.getIdpBaseURL();
        if (!idpBaseURL.endsWith("/")) {
            idpBaseURL += "/";
        }
        String idpSessionPingUrl = idpBaseURL + "service/ping";

        HttpPost httpPost = new HttpPost(idpSessionPingUrl);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Arrays.asList(
                new BasicNameValuePair("idpSessionId", idpSessionId),
                new BasicNameValuePair("trustedServicePassword", idpConfig.getIdpTrustedServicePassword())
        ), StandardCharsets.UTF_8);

        httpPost.setEntity(formEntity);

        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpClient client = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .build();

        try {
            HttpResponse httpResponse = client.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.GONE.value()) {
                log.debug("IDP session expired {}", idpSessionId);

                return IdpSessionStatus.EXPIRED;
            }
            if (statusCode != HttpStatus.OK.value()) {
                log.warn("IDP respond status {} on session ping", statusCode);
            }
        } catch (IOException e) {
            log.warn("Unable to ping IDP {} session {}", idpSessionPingUrl, idpSessionId, e);
        } finally {
            connectionManager.shutdown();
        }

        return IdpSessionStatus.ALIVE;
    }

    public enum IdpSessionStatus {
        ALIVE,
        UNSUPPORTED,
        EXPIRED
    }
}
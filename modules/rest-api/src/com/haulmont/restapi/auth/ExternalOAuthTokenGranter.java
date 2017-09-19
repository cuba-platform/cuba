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

package com.haulmont.restapi.auth;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.TrustedClientCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.RestApiAccessDeniedException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.restapi.config.RestApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.inject.Inject;
import java.util.*;

import static com.haulmont.cuba.core.sys.AppContext.withSecurityContext;
import static com.haulmont.restapi.auth.CubaTokenEnhancer.EXTENDED_DETAILS_ATTRIBUTE_PREFIX;
import static com.haulmont.restapi.auth.CubaUserAuthenticationProvider.SESSION_ID_DETAILS_ATTRIBUTE;

public class ExternalOAuthTokenGranter extends AbstractTokenGranter implements OAuthTokenIssuer {

    protected static final String GRANT_TYPE = "external";

    private final Logger log = LoggerFactory.getLogger(ExternalOAuthTokenGranter.class);

    protected ClientDetailsService clientDetailsService;
    protected OAuth2RequestFactory requestFactory;

    @Inject
    protected Configuration configuration;
    @Inject
    protected AuthenticationService authenticationService;

    protected ExternalOAuthTokenGranter(AuthorizationServerTokenServices tokenServices,
                                        ClientDetailsService clientDetailsService,
                                        OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.clientDetailsService = clientDetailsService;
        this.requestFactory = requestFactory;
    }

    @Override
    public OAuth2AccessTokenResult issueToken(OAuth2AccessTokenRequest tokenRequest) {
        RestApiConfig config = configuration.getConfig(RestApiConfig.class);

        String login = tokenRequest.getLogin();
        Locale locale = tokenRequest.getLocale();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", login);
        parameters.put("client_id", config.getRestClientId());
        parameters.put("scope", "rest-api");
        parameters.put("grant", GRANT_TYPE);

        UserSession session;
        try {
            TrustedClientCredentials credentials = new TrustedClientCredentials(login, config.getTrustedClientPassword(), locale);
            credentials.setClientType(ClientType.REST_API);
            credentials.setClientInfo("REST API");
            credentials.setParams(tokenRequest.getLoginParams());

            session = authenticationService.login(credentials).getSession();
        } catch (RestApiAccessDeniedException ex) {
            log.info("User is not allowed to use the REST API {}", login);
            throw new BadCredentialsException("User is not allowed to use the REST API");
        } catch (LoginException e) {
            log.info("Unable to issue token for REST API: {}", login);
            throw new BadCredentialsException("Bad credentials");
        }

        parameters.put(SESSION_ID_DETAILS_ATTRIBUTE, session.getId().toString());
        for (Map.Entry<String, String> tokenParam : tokenRequest.getTokenDetails().entrySet()) {
            parameters.put(EXTENDED_DETAILS_ATTRIBUTE_PREFIX + tokenParam.getKey(), tokenParam.getValue());
        }

        // issue token using obtained Session, it is required for DB operations inside of persistent token store
        OAuth2AccessToken accessToken = withSecurityContext(new SecurityContext(session), () -> {
            ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(config.getRestClientId());
            TokenRequest tr = getRequestFactory().createTokenRequest(parameters, authenticatedClient);

            return grant(GRANT_TYPE, tr);
        });

        return new OAuth2AccessTokenResult(session, accessToken);
    }

    @Override
    public OAuth2AccessTokenResult issueToken(String login, Locale locale, Map<String, Object> loginParams) {
        OAuth2AccessTokenRequest tokenRequest = new OAuth2AccessTokenRequest();
        tokenRequest.setLogin(login);
        tokenRequest.setLocale(locale);
        tokenRequest.setLoginParams(loginParams);
        return issueToken(tokenRequest);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        String externalPrincipal = tokenRequest.getRequestParameters().get("username");
        String sessionId = tokenRequest.getRequestParameters().get(SESSION_ID_DETAILS_ATTRIBUTE);

        Preconditions.checkState(externalPrincipal != null);
        Preconditions.checkState(sessionId != null);

        OAuth2Request storedOAuth2Request = requestFactory.createOAuth2Request(client, tokenRequest);
        ExternalAuthenticationToken authentication = new ExternalAuthenticationToken(externalPrincipal,
                getRoleUserAuthorities(tokenRequest));

        @SuppressWarnings("unchecked")
        Map<String, String> details = (Map<String, String>) authentication.getDetails();
        if (details == null) {
            details = new HashMap<>();
        }
        details.put(SESSION_ID_DETAILS_ATTRIBUTE, sessionId);
        authentication.setDetails(details);

        return new OAuth2Authentication(storedOAuth2Request, authentication);
    }

    protected List<GrantedAuthority> getRoleUserAuthorities(TokenRequest tokenRequest) {
        return new ArrayList<>();
    }
}
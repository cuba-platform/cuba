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

package com.haulmont.restapi.ldap;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.restapi.auth.OAuthTokenIssuer;
import com.haulmont.restapi.auth.OAuthTokenIssuer.OAuth2AccessTokenResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@RestController
public class LdapAuthController implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(LdapAuthController.class);

    protected LdapTemplate defaultLdapTemplate;
    protected LdapContextSource defaultLdapContextSource;

    protected LdapContextSource ldapContextSource;
    protected LdapTemplate ldapTemplate;
    protected String ldapUserLoginField;

    @Inject
    protected Configuration configuration;
    @Inject
    protected OAuthTokenIssuer oAuthTokenIssuer;

    protected RestLdapConfig ldapConfig;

    protected Set<HttpMethod> allowedRequestMethods = Collections.singleton(HttpMethod.POST);

    protected WebResponseExceptionTranslator providerExceptionHandler = new DefaultWebResponseExceptionTranslator();

    @RequestMapping(value = "/v2/ldap/token", method = RequestMethod.GET)
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal,
                                                            @RequestParam Map<String, String> parameters,
                                                            HttpServletRequest request)
            throws HttpRequestMethodNotSupportedException {
        if (!allowedRequestMethods.contains(HttpMethod.GET)) {
            throw new HttpRequestMethodNotSupportedException("GET");
        }

        return postAccessToken(principal, parameters, request);
    }

    @RequestMapping(value = "/v2/ldap/token", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal,
                                                             @RequestParam Map<String, String> parameters,
                                                             HttpServletRequest request)
            throws HttpRequestMethodNotSupportedException {

        if (!ldapConfig.getLdapEnabled()) {
            log.debug("LDAP authentication is disabled. Property cuba.rest.ldap.enabled is false");

            throw new InvalidGrantException("LDAP is not supported");
        }

        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException(
                    "There is no client authentication. Try adding an appropriate authentication filter.");
        }

        String grantType = parameters.get(OAuth2Utils.GRANT_TYPE);
        if (!"password".equals(grantType)) {
            throw new InvalidGrantException("grant type not supported for ldap/token endpoint");
        }

        String username = parameters.get("username");
        String ipAddress = request.getRemoteAddr();

        String password = parameters.get("password");

        OAuth2AccessTokenResult tokenResult =
                authenticate(username, password, request.getLocale(), ipAddress, parameters);

        return ResponseEntity.ok(tokenResult.getAccessToken());
    }

    protected OAuth2AccessTokenResult authenticate(String username, String password, Locale locale,
                                                   String ipAddress, Map<String, String> parameters) {
        if (!ldapTemplate.authenticate(LdapUtils.emptyLdapName(), buildPersonFilter(username), password)) {
            log.info("REST API authentication failed: {} {}", username, ipAddress);
            throw new BadCredentialsException("Bad credentials");
        }

        return oAuthTokenIssuer.issueToken(username, locale, Collections.emptyMap());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.ldapConfig = configuration.getConfig(RestLdapConfig.class);

        if (ldapConfig.getLdapEnabled()) {
            checkRequiredConfigProperties(ldapConfig);

            defaultLdapContextSource = createLdapContextSource(ldapConfig);
            defaultLdapTemplate = createLdapTemplate(defaultLdapContextSource);
            if (ldapContextSource == null) {
                ldapContextSource = defaultLdapContextSource;
            }
            if (ldapTemplate == null) {
                ldapTemplate = defaultLdapTemplate;
            }
            if (ldapUserLoginField == null) {
                ldapUserLoginField = ldapConfig.getLdapUserLoginField();
            }
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        log.error("Exception in LDAP auth controller", e);
        return new ResponseEntity<>(new OAuth2Exception("Server error", e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<OAuth2Exception> handleBadCredentialsException(BadCredentialsException e) throws Exception {
        return getExceptionTranslator().translate(e);
    }

    @ExceptionHandler(ClientAuthenticationException.class)
    public ResponseEntity<OAuth2Exception> handleClientAuthenticationException(ClientAuthenticationException e) throws Exception {
        return getExceptionTranslator().translate(e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<OAuth2Exception> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws Exception {
        log.info("Handling error: {}, {}", e.getClass().getSimpleName(), e.getMessage());
        return getExceptionTranslator().translate(e);
    }

    public WebResponseExceptionTranslator getExceptionTranslator() {
        return providerExceptionHandler;
    }

    public void setExceptionTranslator(WebResponseExceptionTranslator providerExceptionHandler) {
        this.providerExceptionHandler = providerExceptionHandler;
    }

    protected LdapTemplate createLdapTemplate(LdapContextSource ldapContextSource) {
        LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource);
        ldapTemplate.setIgnorePartialResultException(true);

        return ldapTemplate;
    }

    protected LdapContextSource createLdapContextSource(RestLdapConfig ldapConfig) {
        LdapContextSource ldapContextSource = new LdapContextSource();

        ldapContextSource.setBase(ldapConfig.getLdapBase());
        List<String> ldapUrls = ldapConfig.getLdapUrls();
        ldapContextSource.setUrls(ldapUrls.toArray(new String[ldapUrls.size()]));
        ldapContextSource.setUserDn(ldapConfig.getLdapUser());
        ldapContextSource.setPassword(ldapConfig.getLdapPassword());

        ldapContextSource.afterPropertiesSet();

        return ldapContextSource;
    }

    protected void checkRequiredConfigProperties(RestLdapConfig ldapConfig) {
        List<String> missingProperties = new ArrayList<>();
        if (StringUtils.isBlank(ldapConfig.getLdapBase())) {
            missingProperties.add("cuba.web.ldap.base");
        }
        if (ldapConfig.getLdapUrls().isEmpty()) {
            missingProperties.add("cuba.web.ldap.urls");
        }
        if (StringUtils.isBlank(ldapConfig.getLdapUser())) {
            missingProperties.add("cuba.web.ldap.user");
        }
        if (StringUtils.isBlank(ldapConfig.getLdapPassword())) {
            missingProperties.add("cuba.web.ldap.password");
        }

        if (!missingProperties.isEmpty()) {
            throw new IllegalStateException("Please configure required application properties for LDAP integration: \n" +
                    StringUtils.join(missingProperties, "\n"));
        }
    }

    protected String buildPersonFilter(String login) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"))
                .and(new EqualsFilter(ldapUserLoginField, login));
        return filter.encode();
    }

    public Set<HttpMethod> getAllowedRequestMethods() {
        return allowedRequestMethods;
    }

    public void setAllowedRequestMethods(Set<HttpMethod> allowedRequestMethods) {
        this.allowedRequestMethods = allowedRequestMethods;
    }

    public LdapContextSource getLdapContextSource() {
        return ldapContextSource;
    }

    public void setLdapContextSource(LdapContextSource ldapContextSource) {
        this.ldapContextSource = ldapContextSource;
    }

    public LdapTemplate getLdapTemplate() {
        return ldapTemplate;
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public String getLdapUserLoginField() {
        return ldapUserLoginField;
    }

    public void setLdapUserLoginField(String ldapUserLoginField) {
        this.ldapUserLoginField = ldapUserLoginField;
    }
}
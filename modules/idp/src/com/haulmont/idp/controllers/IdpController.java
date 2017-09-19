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
 */

package com.haulmont.idp.controllers;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.idp.IdpService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.idp.IdpConfig;
import com.haulmont.idp.model.*;
import com.haulmont.idp.sys.IdpServiceLogoutCallbackInvoker;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * IDP login form endpoint.
 */
@Controller("cuba_IdpController")
public class IdpController {
    public static final String CUBA_IDP_COOKIE_NAME = "IDP_SESSION_ID";
    public static final String CUBA_IDP_TICKET_PARAMETER = "idp_ticket";

    private final Logger log = LoggerFactory.getLogger(IdpController.class);

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected IdpConfig idpConfig;

    @Inject
    protected IdpService idpService;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected IdpServiceLogoutCallbackInvoker logoutCallbackInvoker;

    @GetMapping(value = "/")
    public String checkIdpSession(@RequestParam(value = "sp", defaultValue = "") String serviceProviderUrl,
                                  @RequestParam(value = "response_type", defaultValue = "server-ticket") String responseType,
                                  @CookieValue(value = CUBA_IDP_COOKIE_NAME, defaultValue = "") String idpSessionCookie,
                                  HttpServletResponse response) {
        if (!Strings.isNullOrEmpty(serviceProviderUrl)
                && !idpConfig.getServiceProviderUrls().contains(serviceProviderUrl)) {
            log.warn("Incorrect serviceProviderUrl {} passed, will be used default", serviceProviderUrl);
            serviceProviderUrl = null;
        }

        if (Strings.isNullOrEmpty(serviceProviderUrl)) {
            if (!idpConfig.getServiceProviderUrls().isEmpty()) {
                serviceProviderUrl = idpConfig.getServiceProviderUrls().get(0);
            } else {
                log.error("IDP property cuba.idp.serviceProviderUrls is not set");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return null;
            }
        }

        if (!Strings.isNullOrEmpty(idpSessionCookie)) {
            String serviceProviderTicket = idpService.createServiceProviderTicket(idpSessionCookie);
            if (serviceProviderTicket != null) {
                String serviceProviderRedirectUrl;
                try {
                    URIBuilder uriBuilder = new URIBuilder(serviceProviderUrl);

                    if (ResponseType.CLIENT_TICKET.getCode().equals(responseType)) {
                        uriBuilder.setFragment(CUBA_IDP_TICKET_PARAMETER + "=" + serviceProviderTicket);
                    } else {
                        uriBuilder.setParameter(CUBA_IDP_TICKET_PARAMETER, serviceProviderTicket);
                    }

                    serviceProviderRedirectUrl = uriBuilder.build().toString();
                } catch (URISyntaxException e) {
                    log.warn("Unable to compose redirect URL", e);

                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return null;
                }

                try {
                    response.sendRedirect(serviceProviderRedirectUrl);
                } catch (IOException e) {
                    // do not log stacktrace here
                    log.warn("Unable to send redirect to service provider URL", e.getMessage());
                }

                log.debug("New ticket {} created for already logged in user", serviceProviderTicket);

                return null;
            } else {
                log.debug("IDP session {} not found, login required", idpSessionCookie);
            }
        }

        // remove auth cookie
        Cookie cookie = new Cookie(CUBA_IDP_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (ResponseType.CLIENT_TICKET.getCode().equals(responseType)) {
            return "redirect:login.html" +
                    "?response_type=" + ResponseType.CLIENT_TICKET.getCode()
                    + "&sp=" + URLEncodeUtils.encodeUtf8(serviceProviderUrl);
        }

        return "redirect:login.html?sp=" + URLEncodeUtils.encodeUtf8(serviceProviderUrl);
    }

    @GetMapping(value = "/logout")
    public String logout(@RequestParam(value = "sp", defaultValue = "") String serviceProviderUrl,
                         @RequestParam(value = "response_type", defaultValue = "server-ticket") String responseType,
                         @CookieValue(value = CUBA_IDP_COOKIE_NAME, defaultValue = "") String idpSessionCookie,
                         HttpServletResponse response) {
        if (!Strings.isNullOrEmpty(serviceProviderUrl)
                && !idpConfig.getServiceProviderUrls().contains(serviceProviderUrl)) {
            log.warn("Incorrect serviceProviderUrl {} passed, will be used default", serviceProviderUrl);
            serviceProviderUrl = null;
        }

        if (Strings.isNullOrEmpty(serviceProviderUrl)) {
            if (!idpConfig.getServiceProviderUrls().isEmpty()) {
                serviceProviderUrl = idpConfig.getServiceProviderUrls().get(0);
            } else {
                log.error("IDP property cuba.idp.serviceProviderUrls is not set");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return null;
            }
        }

        if (!Strings.isNullOrEmpty(idpSessionCookie)) {
            boolean loggedOut = idpService.logout(idpSessionCookie);

            if (loggedOut) {
                log.info("Logged out IDP session {}", idpSessionCookie);

                logoutCallbackInvoker.performLogoutOnServiceProviders(idpSessionCookie);
            }
        }

        // remove auth cookie
        Cookie cookie = new Cookie(CUBA_IDP_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (ResponseType.CLIENT_TICKET.getCode().equals(responseType)) {
            return "redirect:login.html" +
                    "?response_type=" + ResponseType.CLIENT_TICKET.getCode()
                    + "&sp=" + URLEncodeUtils.encodeUtf8(serviceProviderUrl);
        }

        return "redirect:login.html?sp=" + URLEncodeUtils.encodeUtf8(serviceProviderUrl);
    }

    @GetMapping(value = "/status", produces = "application/json; charset=UTF-8")
    public ResponseEntity status(@CookieValue(value = CUBA_IDP_COOKIE_NAME, defaultValue = "") String idpSessionCookie) {
        if (!Strings.isNullOrEmpty(idpSessionCookie)) {
            String serviceProviderTicket = idpService.createServiceProviderTicket(idpSessionCookie);
            if (serviceProviderTicket != null) {
                return ResponseEntity.ok(new IdpTicket(serviceProviderTicket));
            }
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/auth", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public AuthResponse authenticate(@RequestBody AuthRequest auth,
                                     @CookieValue(value = CUBA_IDP_COOKIE_NAME, defaultValue = "") String idpSessionCookie,
                                     HttpServletResponse response) {
        String serviceProviderUrl = auth.getServiceProviderUrl();

        if (!Strings.isNullOrEmpty(serviceProviderUrl)
                && !idpConfig.getServiceProviderUrls().contains(serviceProviderUrl)) {
            log.warn("Incorrect serviceProviderUrl {} passed, will be used default", serviceProviderUrl);
            serviceProviderUrl = null;
        }

        if (Strings.isNullOrEmpty(serviceProviderUrl)) {
            if (!idpConfig.getServiceProviderUrls().isEmpty()) {
                serviceProviderUrl = idpConfig.getServiceProviderUrls().get(0);
            } else {
                log.error("IDP property cuba.idp.serviceProviderUrls is not set");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                return null;
            }
        }

        Locale sessionLocale = null;
        if (globalConfig.getLocaleSelectVisible() && auth.getLocale() != null) {
            Map<String, Locale> availableLocales = globalConfig.getAvailableLocales();
            Locale requestedLocale = Locale.forLanguageTag(auth.getLocale());

            if (availableLocales.containsValue(requestedLocale)) {
                sessionLocale = requestedLocale;
            }
        }
        if (sessionLocale == null) {
            sessionLocale = messageTools.getDefaultLocale();
        }

        if (!Strings.isNullOrEmpty(idpSessionCookie)) {
            boolean loggedOut = idpService.logout(idpSessionCookie);

            if (loggedOut) {
                log.info("Logged out IDP session {}", idpSessionCookie);

                logoutCallbackInvoker.performLogoutOnServiceProviders(idpSessionCookie);
            }
        }

        IdpService.IdpLoginResult loginResult;
        try {
            loginResult = idpService.login(auth.getUsername(),
                    passwordEncryption.getPlainHash(auth.getPassword()),
                    sessionLocale,
                    ImmutableMap.of(ClientType.class.getName(), ClientType.WEB.name()));
        } catch (LoginException e) {
            // remove auth cookie
            Cookie cookie = new Cookie(CUBA_IDP_COOKIE_NAME, "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            log.warn("Unable to login user {}", auth.getUsername());
            return AuthResponse.failed("invalid_credentials");
        }

        if (loginResult.getSessionId() != null) {
            Cookie idpCookie = new Cookie(CUBA_IDP_COOKIE_NAME, loginResult.getSessionId());
            idpCookie.setMaxAge(idpConfig.getIdpCookieMaxAge());
            idpCookie.setHttpOnly(idpConfig.getIdpCookieHttpOnly());
            response.addCookie(idpCookie);
        }

        String serviceProviderRedirectUrl;
        try {
            URIBuilder uriBuilder = new URIBuilder(serviceProviderUrl);

            if ("client-ticket".equals(auth.getResponseType())) {
                uriBuilder.setFragment(CUBA_IDP_TICKET_PARAMETER + "=" + loginResult.getServiceProviderTicket());
            } else {
                uriBuilder.setParameter(CUBA_IDP_TICKET_PARAMETER, loginResult.getServiceProviderTicket());
            }

            serviceProviderRedirectUrl = uriBuilder.build().toString();
        } catch (URISyntaxException e) {
            return AuthResponse.failed("invalid_params");
        }

        log.info("Logged in IDP session with ticket {}, user: {}", loginResult.getServiceProviderTicket(), auth.getUsername());

        return AuthResponse.authenticated(serviceProviderRedirectUrl);
    }

    @GetMapping(value = "/locales", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public LocalesInfo getLocales() {
        LocalesInfo localesInfo = new LocalesInfo();

        localesInfo.setLocaleSelectVisible(globalConfig.getLocaleSelectVisible());

        Map<String, String> locales = new LinkedHashMap<>();
        for (Map.Entry<String, Locale> entry : globalConfig.getAvailableLocales().entrySet()) {
            locales.put(entry.getValue().toLanguageTag(), entry.getKey());
        }
        localesInfo.setLocales(locales);

        return localesInfo;
    }
}
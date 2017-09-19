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

package com.haulmont.cuba.restapi;

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.auth.AbstractClientCredentials;
import com.haulmont.cuba.security.auth.AuthenticationService;
import com.haulmont.cuba.security.auth.LoginPasswordCredentials;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class LoginServiceController {

    private final Logger log = LoggerFactory.getLogger(LoginServiceController.class);

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected Authentication authentication;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionService userSessionService;

    private static MimeType FORM_TYPE;

    static {
        try {
            FORM_TYPE = new MimeType("application/x-www-form-urlencoded;charset=UTF-8");
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public void loginByPost(@RequestBody String requestBody,
                            @RequestHeader(value = "Content-Type") MimeType contentType,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException, JSONException {

        String username;
        String password;
        String localeStr;
        if (contentType.match(JSONConverter.MIME_TYPE_JSON)) {
            try {
                JSONObject json = new JSONObject(requestBody);
                username = json.getString("username");
                password = json.getString("password");
                localeStr = json.getString("locale");
            } catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else if (contentType.match(FORM_TYPE)) {
            String[] pairs = requestBody.split("\\&");
            Map<String, String> name2value = new HashMap<>();
            for (String pair : pairs) {
                String[] fields = pair.split("=");
                if (fields.length < 2) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                String name = URLEncodeUtils.decodeUtf8(fields[0]);
                String value = URLEncodeUtils.decodeUtf8(fields[1]);
                name2value.put(name, value);
            }
            username = name2value.get("username");
            password = name2value.get("password");
            localeStr = name2value.get("locale");
        } else {
            throw new IllegalStateException("Unsupported content type: " + contentType);
        }

        doLogin(username, password, localeStr, request, response);
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public void loginByGet(@RequestParam(value = "u") String username,
                           @RequestParam(value = "p") String password,
                           @RequestParam(value = "l", required = false) String localeStr,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException, JSONException {

        doLogin(username, password, localeStr, request, response);
    }

    protected Locale localeFromString(String localeStr) {
        return StringUtils.isBlank(localeStr) ?
                globalConfig.getAvailableLocales().values().iterator().next()
                : LocaleUtils.toLocale(localeStr);
    }

    protected void doLogin(String username, String password, String localeStr,
                           HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        Locale locale = localeFromString(localeStr);

        AuthenticationService authenticationService = AppBeans.get(AuthenticationService.NAME);
        try {
            AbstractClientCredentials credentials = new LoginPasswordCredentials(username, passwordEncryption.getPlainHash(password), locale);
            UserSession userSession = authenticationService.login(credentials).getSession();

            if (!userSession.isSpecificPermitted(Authentication.PERMISSION_NAME)) {
                log.info(String.format("User %s is not allowed to use REST-API", username));
                AppContext.setSecurityContext(new SecurityContext(userSession));
                try {
                    authenticationService.logout();
                } finally {
                    AppContext.setSecurityContext(null);
                }
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                AppContext.setSecurityContext(new SecurityContext(userSession));
                setSessionInfo(request, userSession);
            } finally {
                AppContext.setSecurityContext(null);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(userSession.getId().toString());
            writer.close();

            log.debug(String.format("User %s logged in with REST-API, session id: %s", username, userSession.getId()));
        } catch (LoginException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected void setSessionInfo(HttpServletRequest request, UserSession userSession) {
        userSessionService.setSessionAddress(userSession.getId(), request.getRemoteAddr());

        String serverInfo = "REST API (" +
                globalConfig.getWebHostName() + ":" +
                globalConfig.getWebPort() + "/" +
                globalConfig.getWebContextName() + ") ";
        userSessionService.setSessionClientInfo(userSession.getId(), serverInfo + request.getHeader("User-Agent"));
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.POST)
    public void logoutByPost(@RequestBody String requestBody, @RequestHeader(value = "Content-Type") MimeType contentType,
                             HttpServletResponse response) throws IOException, JSONException {

        String sessionUUID;
        if (contentType.match(JSONConverter.MIME_TYPE_JSON)) {
            try {
                JSONObject json = new JSONObject(requestBody);
                sessionUUID = json.getString("session");
            } catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else if (contentType.match(FORM_TYPE)) {
            String[] fields = requestBody.split("=");
            if (fields.length < 2) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            sessionUUID = URLEncodeUtils.decodeUtf8(fields[1]);
        } else {
            throw new IllegalStateException("Unsupported content type: " + contentType);
        }

        doLogout(sessionUUID, response);
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.GET)
    public void logoutByGet(@RequestParam(value = "session") String sessionUUID,
                            HttpServletResponse response) throws IOException, JSONException {
        doLogout(sessionUUID, response);
    }

    protected void doLogout(String sessionUUID, HttpServletResponse response) throws IOException, JSONException {
        try {
            if (authentication.begin(sessionUUID)) {
                AuthenticationService authenticationService = AppBeans.get(AuthenticationService.NAME);
                authenticationService.logout();
            }
        } catch (Throwable e) {
            log.error("Error processing logout request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
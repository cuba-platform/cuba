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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
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
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class LoginServiceController {

    @Inject
    protected PasswordEncryption passwordEncryption;

    @Inject
    protected Authentication authentication;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected UserSessionService userSessionService;

    private static Logger log = LoggerFactory.getLogger(LoginServiceController.class);
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
                String name = URLDecoder.decode(fields[0], StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(fields[1], StandardCharsets.UTF_8.name());
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
                : new Locale(localeStr);
    }

    protected void doLogin(String username, String password, String localeStr,
                           HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        Locale locale = localeFromString(localeStr);

        LoginService loginService = AppBeans.get(LoginService.NAME);
        try {
            if (loginService.isBruteForceProtectionEnabled()) {
                if (loginService.loginAttemptsLeft(username, request.getRemoteAddr()) <= 0) {
                    log.info("Blocked user login attempt: login={}, ip={}", username, request.getRemoteAddr());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }

            UserSession userSession = loginService.login(username, passwordEncryption.getPlainHash(password), locale);

            if (!userSession.isSpecificPermitted(Authentication.PERMISSION_NAME)) {
                log.info(String.format("User %s is not allowed to use REST-API", username));
                AppContext.setSecurityContext(new SecurityContext(userSession));
                try {
                    loginService.logout();
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
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            writer.write(userSession.getId().toString());
            writer.close();

            log.debug(String.format("User %s logged in with REST-API, session id: %s", username, userSession.getId()));
        } catch (LoginException e) {
            if (loginService.isBruteForceProtectionEnabled()) {
                loginService.registerUnsuccessfulLogin(username, request.getRemoteAddr());
            }
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
            sessionUUID = URLDecoder.decode(fields[1], StandardCharsets.UTF_8.name());
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
                LoginService loginService = AppBeans.get(LoginService.NAME);
                loginService.logout();
            }
        } catch (Throwable e) {
            log.error("Error processing logout request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
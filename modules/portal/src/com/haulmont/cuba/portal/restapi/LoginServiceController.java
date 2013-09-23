/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author chevelev
 * @version $Id$
 */
@Controller
public class LoginServiceController {

    @Inject
    private PasswordEncryption passwordEncryption;

    private static Log log = LogFactory.getLog(LoginServiceController.class);
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
                            HttpServletResponse response) throws IOException, JSONException {

        response.addHeader("Access-Control-Allow-Origin", "*");

        String username;
        String password;
        Locale locale;
        if (contentType.match(JSONConvertor.MIME_TYPE_JSON)) {
            try {
                JSONObject json = new JSONObject(requestBody);
                username = json.getString("username");
                password = json.getString("password");
                locale = new Locale(json.getString("locale"));
            } catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else if (contentType.match(FORM_TYPE)) {
            String[] pairs = requestBody.split("\\&");
            Map<String, String> name2value = new HashMap<>();
            for (String pair : pairs) {
                String[] fields = pair.split("=");
                String name = URLDecoder.decode(fields[0], "UTF-8");
                String value = URLDecoder.decode(fields[1], "UTF-8");
                name2value.put(name, value);
            }
            username = name2value.get("username");
            password = name2value.get("password");
            locale = new Locale(name2value.get("locale"));
        } else {
            throw new IllegalStateException("Unsupported content type: " + contentType);
        }

        try {
            LoginService loginService = AppBeans.get(LoginService.NAME);
            UserSession userSession = loginService.login(username, passwordEncryption.getPlainHash(password), locale);
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            writer.write(userSession.getId().toString());
            writer.close();

            log.debug(String.format("User %s logged in with REST-API, session id: %s", username, userSession.getId()));
        } catch (LoginException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.GET)
    public void loginByGet(@RequestParam(value = "u") String username,
                           @RequestParam(value = "p") String password,
                           @RequestParam(value = "l", required = false) String localeStr,
                           HttpServletResponse response) throws IOException, JSONException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        Locale locale = StringUtils.isBlank(localeStr) ? new Locale("en") : new Locale(localeStr);
        try {
            LoginService loginService = AppBeans.get(LoginService.NAME);

            UserSession userSession = loginService.login(username, passwordEncryption.getPlainHash(password), locale);
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            writer.write(userSession.getId().toString());
            writer.close();

            log.debug(String.format("User %s logged in with REST-API, session id: %s", username, userSession.getId()));
        } catch (LoginException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.POST)
    public void logoutByPost(@RequestBody String requestBody, @RequestHeader(value = "Content-Type") MimeType contentType,
                             HttpServletResponse response) throws IOException, JSONException {

        String sessionUUID;
        if (contentType.match(JSONConvertor.MIME_TYPE_JSON)) {
            try {
                JSONObject json = new JSONObject(requestBody);
                sessionUUID = json.getString("session");
            } catch (JSONException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else if (contentType.match(FORM_TYPE)) {
            String[] fields = requestBody.split("=");
            sessionUUID = URLDecoder.decode(fields[1], "UTF-8");
        } else {
            throw new IllegalStateException("Unsupported content type: " + contentType);
        }
        try {
            Authentication authentication = Authentication.me(sessionUUID);
            if (authentication != null) {
                LoginService loginService = AppBeans.get(LoginService.NAME);
                loginService.logout();
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (NoUserSessionException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/api/logout", method = RequestMethod.GET)
    public void logoutByGet(@RequestParam(value = "session") String sessionUUID,
                            HttpServletResponse response) throws IOException, JSONException {

        try {
            Authentication authentication = Authentication.me(sessionUUID);
            if (authentication != null) {
                LoginService loginService = AppBeans.get(LoginService.NAME);
                loginService.logout();
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (NoUserSessionException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
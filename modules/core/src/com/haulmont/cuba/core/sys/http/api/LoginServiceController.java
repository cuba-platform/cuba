/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.http.api;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.util.WebUtils;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Author: Alexander Chevelev
 * Date: 19.04.2011
 * Time: 22:03:25
 */
@Controller
public class LoginServiceController {
    private static Log log = LogFactory.getLog(LoginServiceController.class);
    private static MimeType FORM_TYPE;

    static {
        try {
            FORM_TYPE = new MimeType("application/x-www-form-urlencoded;charset=UTF-8");
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }
    //todo wire
    private ConversionFabric conversionFabric = new ConversionFabric();

    public LoginServiceController() {
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestBody String requestBody,
                      @RequestHeader(value = "Content-Type") MimeType contentType,
                      HttpServletResponse response) throws JSONException, IOException, JSONException {

        String username;
        String password;
        Locale locale;
        if (contentType.match(JSONConvertor.MIME_TYPE_JSON)) {
            JSONObject json = new JSONObject(requestBody);
            username = json.getString("username");
            password = json.getString("passwordHash");
            locale = new Locale(json.getString("locale"));
        } else if (contentType.match(FORM_TYPE)) {
            String[] pairs = requestBody.split("\\&");
            Map<String, String> name2value = new HashMap<String, String>();
            for (String pair : pairs) {
                String[] fields = pair.split("=");
                String name = URLDecoder.decode(fields[0], "UTF-8");
                String value = URLDecoder.decode(fields[1], "UTF-8");
                name2value.put(name, value);
            }
            username = name2value.get("username");
            password = name2value.get("passwordHash");
            locale = new Locale(name2value.get("locale"));
        } else {
            throw new IllegalStateException("Unsupported content type: " + contentType);
        }

        try {
            LoginService svc = (LoginService) Locator.lookup("cuba_LoginService");
            UserSession userSession = svc.login(username, password, locale);
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            writer.write(userSession.getId().toString());
            writer.close();
        } catch (LoginException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

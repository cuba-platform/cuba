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

package com.haulmont.cuba.core.config;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Component("cuba_ConfigStorageCommon")
public class ConfigStorageCommon {

    @Inject
    protected Configuration configuration;

    @Inject
    protected LoginService loginService;

    protected final Log log = LogFactory.getLog(ConfigStorageCommon.class);

    public String printAppProperties(String prefix) {
        List<String> list = new ArrayList<String>();
        for (String name : AppContext.getPropertyNames()) {
            if (prefix == null || name.startsWith(prefix)) {
                list.add(name + "=" + AppContext.getProperty(name));
            }
        }
        Collections.sort(list);
        return new StrBuilder().appendWithSeparators(list, "\n").toString();
    }

    public String getAppProperty(String name) {
        if (StringUtils.isBlank(name))
            return "Enter a property name";

        return name + "=" + AppContext.getProperty(name);
    }

    public String setAppProperty(String name, String value) {
        if (StringUtils.isBlank(name))
            return "Enter a property name";
        if (StringUtils.isBlank(value))
            return "Enter a property value";

        AppContext.setProperty(name, value);
        return "Property " + name + " set to " + value;
    }

    /**
     * Method returns a result of config method invocation
     * @param classFQN fully qualified configuration interface name
     * @param methodName config getter method name
     * @param userLogin parameter is used for authentication if there is no security context bound to the current thread
     *                  and configuration method source is DATABASE
     * @param userPassword see userLogin parameter description
     * @return configuration method invocation result
     */
    public String getConfigValue(String classFQN, String methodName, String userLogin, String userPassword) {
        Class<?> aClass;
        try {
            aClass = Class.forName(classFQN);
        } catch (ClassNotFoundException e) {
            return "Class " + classFQN + " not found.\nPlease ensure that you entered a fully qualified class name and " +
                    "that you class is in a proper application module (core, web or portal).";
        }

        if (Config.class.isAssignableFrom(aClass)) {
            Config config = configuration.getConfig((Class<? extends Config>)aClass);
            Method method;
            boolean logoutRequired = false;
            try {
                method = aClass.getMethod(methodName);

                //if there is no security context bound to the current thread and the source of the config method is
                //DATABASE, then login attempt with 'userLogin' and 'userPassword' will be made
                if (AppContext.getSecurityContext() == null) {
                    SourceType sourceType;
                    Source methodSourceAnnotation = method.getAnnotation(Source.class);
                    if (methodSourceAnnotation != null) {
                        sourceType = methodSourceAnnotation.type();
                    } else {
                        Source classSourceAnnotation = aClass.getAnnotation(Source.class);
                        sourceType = classSourceAnnotation.type();
                    }

                    if (sourceType != null && sourceType == SourceType.DATABASE) {
                        if (Strings.isNullOrEmpty(userLogin)) {
                            return "No security context bound to the current thread. Please specify the user name.";
                        } else {
                            try {
                                Map<String, Locale> availableLocales = configuration.getConfig(GlobalConfig.class).getAvailableLocales();
                                Locale defaultLocale = availableLocales.values().iterator().next();
                                UserSession session = loginService.loginTrusted(userLogin, userPassword, defaultLocale);
                                AppContext.setSecurityContext(new SecurityContext(session));
                                logoutRequired = true;
                            } catch (LoginException e) {
                                log.error(ExceptionUtils.getStackTrace(e));
                                return "Login error: " + e.getMessage();
                            }
                        }
                    }
                }

                Object result = method.invoke(config);
                return result == null ? null : result.toString();
            } catch (NoSuchMethodException e) {
                return "Method " + methodName + "() not found in class " + classFQN;
            } catch (InvocationTargetException | IllegalAccessException e) {
                return ExceptionUtils.getStackTrace(e);
            } finally {
                if (logoutRequired) {
                    loginService.logout();
                    AppContext.setSecurityContext(null);
                }
            }
        } else {
            return "Class " + classFQN + " is not an implementation of Config interface";
        }
    }
}
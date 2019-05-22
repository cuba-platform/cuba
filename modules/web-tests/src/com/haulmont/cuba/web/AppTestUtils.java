/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.sys.AppCookies;
import com.vaadin.server.VaadinRequest;
import org.springframework.context.ApplicationContext;

public final class AppTestUtils {

    private AppTestUtils() {
    }

    public static void setThemeConstants(App app, ThemeConstants themeConstants) {
        app.themeConstants = themeConstants;
    }

    public static void setCookies(App app, AppCookies appCookies) {
        app.cookies = appCookies;
    }

    public static void setConnection(App app, Connection connection) {
        app.connection = connection;
    }

    public static void setEvents(App app, Events events) {
        app.events = events;
    }

    public static void setApplicationContext(AppUI ui, ApplicationContext context) {
        ui.setApplicationContext(context);
    }

    public static void initUi(AppUI ui, VaadinRequest request) {
        ui.init(request);
    }
}
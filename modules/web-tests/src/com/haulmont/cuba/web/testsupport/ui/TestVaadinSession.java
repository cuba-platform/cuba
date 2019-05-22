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

package com.haulmont.cuba.web.testsupport.ui;

import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;

import java.util.Locale;

public class TestVaadinSession extends VaadinSession {

    protected WebBrowser webBrowser;

    public TestVaadinSession(WebBrowser webBrowser, Locale locale) {
        super(new VaadinServletService(){});
        this.webBrowser = webBrowser;

        setLocale(locale);
    }

    @Override
    public boolean hasLock() {
        return true;
    }

    @Override
    public WebBrowser getBrowser() {
        return webBrowser;
    }
}
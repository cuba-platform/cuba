/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class UIScope implements Scope {

    public static final String NAME = "ui";

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null || !session.hasLock()) {
            throw new IllegalStateException("Unable to use UIScope from non-Vaadin thread");
        }

        AppUI ui = AppUI.getCurrent();
        if (ui == null) {
            throw new IllegalStateException("Unable to use UIScope - there is no UI instance");
        }

        switch (name) {
            case Screens.NAME:
                Screens screens = ui.getScreens();
                if (screens == null) {
                    screens = (Screens) objectFactory.getObject();
                }
                return screens;

            case Dialogs.NAME:
                Dialogs dialogs = ui.getDialogs();
                if (dialogs == null) {
                    dialogs = (Dialogs) objectFactory.getObject();
                }
                return dialogs;

            case Notifications.NAME:
                Notifications notifications = ui.getNotifications();
                if (notifications == null) {
                    notifications = (Notifications) objectFactory.getObject();
                }
                return notifications;

            case Fragments.NAME:
                Fragments fragments = ui.getFragments();
                if (fragments == null) {
                    fragments = (Fragments) objectFactory.getObject();
                }
                return fragments;

            case WebBrowserTools.NAME:
                WebBrowserTools webBrowserTools = ui.getWebBrowserTools();
                if (webBrowserTools == null) {
                    webBrowserTools = (WebBrowserTools) objectFactory.getObject();
                }
                return webBrowserTools;

            default:
                throw new UnsupportedOperationException("Unknown UI scoped bean " + name);
        }
    }

    @Override
    public Object remove(String name) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null || !session.hasLock()) {
            throw new IllegalStateException("Unable to use UIScope from non-Vaadin thread");
        }

        throw new UnsupportedOperationException("Remove is not supported for UI scope");
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
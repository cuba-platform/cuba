/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaUIProvider extends DefaultUIProvider {
    @Override
    public String getPageTitle(UICreateEvent event) {
        if (AppUI.class.isAssignableFrom(event.getUIClass())) {
            // vaadin7 problem with dynamic page title
            if (App.isBound()) {
                Messages messages = AppBeans.get(Messages.class);
                String messagesPack = AppConfig.getMessagesPack();
                App app = App.getInstance();
                if (app.getConnection().isConnected()) {
                    return messages.getMessage(messagesPack, "application.caption", app.getLocale());
                }
                else
                    return messages.getMessage(messagesPack, "loginWindow.caption", app.getLocale());
            }
            return super.getPageTitle(event);
        } else
            return super.getPageTitle(event);
    }
}
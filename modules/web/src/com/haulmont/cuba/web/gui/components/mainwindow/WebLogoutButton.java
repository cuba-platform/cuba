/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.LogoutButton;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.ui.Button;
import org.apache.commons.lang.StringUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebLogoutButton extends WebAbstractComponent<CubaButton> implements LogoutButton {

    public static final String LOGOUT_BUTTON_STYLENAME = "cuba-logout-button";
    protected String icon;

    public WebLogoutButton() {
        component = new CubaButton();
        component.addStyleName(LOGOUT_BUTTON_STYLENAME);
        component.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logout();
            }
        });
    }

    protected void logout() {
        Window window = ComponentsHelper.getWindow(this);
        if (window == null) {
            throw new IllegalStateException("Unable to find Frame for logout button");
        }

        window.saveSettings();

        final WebWindowManager wm = (WebWindowManager) window.getWindowManager();
        wm.checkModificationsAndCloseAll(new Runnable() {
            @Override
            public void run() {
                Connection connection = wm.getApp().getConnection();
                connection.logout();
            }
        });
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(LOGOUT_BUTTON_STYLENAME);
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (!StringUtils.isEmpty(icon)) {
            component.setIcon(WebComponentsHelper.getIcon(icon));
            component.addStyleName(WebButton.ICON_STYLE);
        } else {
            component.setIcon(null);
            component.removeStyleName(WebButton.ICON_STYLE);
        }
    }
}
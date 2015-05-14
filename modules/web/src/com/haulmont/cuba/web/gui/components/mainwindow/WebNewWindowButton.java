/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.mainwindow.NewWindowButton;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebNewWindowButton extends WebAbstractComponent<CubaButton> implements NewWindowButton {

    public static final String NEWWINDOW_BUTTON_STYLENAME = "cuba-newwindow-button";

    protected String icon;

    public WebNewWindowButton() {
        component = new CubaButton();
        component.addStyleName(NEWWINDOW_BUTTON_STYLENAME);

        URL pageUrl;
        try {
            pageUrl = Page.getCurrent().getLocation().toURL();
        } catch (MalformedURLException ignored) {
            LogFactory.getLog(getClass()).warn("Couldn't get URL of current Page");
            return;
        }

        ExternalResource currentPage = new ExternalResource(pageUrl);
        final BrowserWindowOpener opener = new BrowserWindowOpener(currentPage);
        opener.setWindowName("_blank");

        opener.extend(component);
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(NEWWINDOW_BUTTON_STYLENAME);
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
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
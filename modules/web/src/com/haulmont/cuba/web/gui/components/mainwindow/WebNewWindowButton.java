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

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.mainwindow.NewWindowButton;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class WebNewWindowButton extends WebAbstractComponent<CubaButton> implements NewWindowButton {

    public static final String NEWWINDOW_BUTTON_STYLENAME = "c-newwindow-button";

    public WebNewWindowButton() {
        component = new CubaButton();
        component.addStyleName(NEWWINDOW_BUTTON_STYLENAME);
        component.setDescription(null);

        URL pageUrl;
        try {
            pageUrl = Page.getCurrent().getLocation().toURL();
        } catch (MalformedURLException ignored) {
            LoggerFactory.getLogger(WebNewWindowButton.class).warn("Couldn't get URL of current Page");
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
}
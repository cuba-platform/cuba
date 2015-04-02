/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Link;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import org.apache.commons.lang.StringUtils;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLink extends WebAbstractComponent<com.vaadin.ui.Link> implements Link {

    public static final String ICON_STYLE = "icon";

    protected String icon;

    public WebLink() {
        component = new com.vaadin.ui.Link();
    }

    @Override
    public void setUrl(String url) {
        component.setResource(new ExternalResource(url));
    }

    @Override
    public String getUrl() {
        Resource resource = component.getResource();
        if (resource instanceof ExternalResource)
            return ((ExternalResource) resource).getURL();

        return null;
    }

    @Override
    public void setTarget(String target) {
        component.setTargetName(target);
    }

    @Override
    public String getTarget() {
        return component.getTargetName();
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
            component.addStyleName(ICON_STYLE);
        } else {
            component.setIcon(null);
            component.removeStyleName(ICON_STYLE);
        }
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
}
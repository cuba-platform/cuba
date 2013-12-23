/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Link;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLink extends WebAbstractComponent<com.vaadin.ui.Link> implements Link {

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
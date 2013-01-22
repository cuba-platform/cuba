/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.ValueProvider;
import com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost;
import com.haulmont.cuba.web.toolkit.ui.JavaScriptHost;
import com.vaadin.ui.ClientWidget;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@ClientWidget(VScriptHost.class)
@SuppressWarnings({"unused"})
public class WebScriptHost extends WebAbstractComponent<JavaScriptHost> {

    public WebScriptHost() {
        this.component = new JavaScriptHost();

        this.setWidth("0");
        this.setHeight("0");

        this.setVisible(true);
        this.setExpandable(false);
    }

    public ValueProvider getComponentParams() {
        return component.getValueProvider();
    }

    public void evaluateScript(String script) {
        this.component.evaluateScript(script);
    }

    public void viewDocument(String documentUrl) {
        this.component.viewDocument(documentUrl);
    }

    public void getResource(String resourceUrl) {
        this.component.getResource(resourceUrl);
    }
}

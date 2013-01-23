/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.toolkit.gwt.client.utils;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ValueMap;

/**
 * Component for evaluate custom JavaScript from server
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class VScriptHost extends SimplePanel implements Paintable {
    public static final String COMMAND_PARAM_KEY = "command";

    // System commands
    public static final String SCRIPT_COMMAND = "script";
    public static final String LOCALE_COMMAND = "locale";

    // File commands
    public static final String VIEW_COMMAND = "view";
    public static final String GET_COMMAND = "get";

    public static final String SCRIPT_PARAM_KEY = "script";
    public static final String URL_PARAM_KEY = "url";
    public static final String LOCALE_PARAM_KEY = "messages";

    public VScriptHost() {
        getElement().getStyle().setDisplay(Style.Display.NONE);
        getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        String paintableId = uidl.getId();

        this.getElement().setId("scriptHost_" + paintableId);

        String command = uidl.getStringAttribute(COMMAND_PARAM_KEY);
        if (SCRIPT_COMMAND.equals(command)) {
            String script = uidl.getStringAttribute(SCRIPT_PARAM_KEY);
            if (script != null)
                evaluateScript(script);
        } else if (VIEW_COMMAND.equals(command)) {
            // open new window
            String url = uidl.getStringAttribute(URL_PARAM_KEY);
            if (url != null)
                viewDocument(url);
        } else if (GET_COMMAND.equals(command)) {
            // download file
            String url = uidl.getStringAttribute(URL_PARAM_KEY);
            if (url != null)
                getResource(url);
        } else if (LOCALE_COMMAND.equals(command)) {
            // update locale and system messages in ApplicationConfiguration
            ValueMap localeMessages = uidl.getMapAttribute(LOCALE_PARAM_KEY);
            client.getConfiguration().updateSystemMessages(localeMessages);
        }
    }

    private native void evaluateScript(String script)/*-{
        eval(script);
    }-*/;

    private native void viewDocument(String documentUrl)/*-{
        window.open(documentUrl, '');
    }-*/;

    private native void getResource(String resourceUrl)/*-{
        var timedAction = function() {
            document.location.href = resourceUrl;
        };
        setTimeout(timedAction, 50);
    }-*/;
}

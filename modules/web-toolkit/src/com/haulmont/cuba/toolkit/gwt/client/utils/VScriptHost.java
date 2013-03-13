/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.toolkit.gwt.client.utils;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.terminal.gwt.client.*;

/**
 * Component for evaluate custom JavaScript from server
 *
 * @author artamonov
 * @version $Id$
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

    public static final String HISTORY_BACK_ACTION = "historyBackAction";
    public static final String SERVER_CALL_ACTION = "serverCall";

    private boolean historyHandlerInitialized = false;
    private boolean serverCallHandlerInitialized = false;

    private ApplicationConnection client;
    private String paintableId;

    public VScriptHost() {
        getElement().getStyle().setDisplay(Style.Display.NONE);
        getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;
        this.paintableId = uidl.getId();

        if (client.getConfiguration().isHandleHistoryBack() && !historyHandlerInitialized) {
            initHistoryHandler();

            historyHandlerInitialized = true;
        }

        if (!serverCallHandlerInitialized) {
            initJsApi();

            serverCallHandlerInitialized = true;
        }

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

    public void handleHistoryBackAction() {
        if (historyHandlerInitialized) {
            client.updateVariable(paintableId, HISTORY_BACK_ACTION, "performed", true);
        }
    }

    public void makeServerCall(String[] params) {
        if (params != null && params.length > 0)
            client.updateVariable(paintableId, SERVER_CALL_ACTION, params, true);
        else
            VConsole.log("JsAPI: Null or empty params in server call");
    }

    private native void evaluateScript(String script)/*-{
        eval(script);
    }-*/;

    private native void viewDocument(String documentUrl)/*-{
        window.open(documentUrl, '');
    }-*/;

    private native void getResource(String resourceUrl)/*-{
        var timedAction = function () {
            document.location.href = resourceUrl;
        };
        setTimeout(timedAction, 50);
    }-*/;

    private native void initHistoryHandler()
    /*-{
        var vScriptHost = this;
        (function (window) {
            var History = window.History;

            var location = window.location.href;

            var defaultState = 0;

            if (location.indexOf('?a') >= 0) {
                History.pushState({state: 1, rand: 1}, window.document.title, '?b');
                defaultState = 1;
            } else {
                History.pushState({state: 2, rand: 2}, window.document.title, '?a');
                defaultState = 2;
            }

            History.Adapter.bind(window, 'statechange', function () {
                var State = History.getState();

                if (!State.data || State.data.state != defaultState) {
                    // one step forward
                    History.go(1);
                    // call handler
                    vScriptHost.@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::handleHistoryBackAction()();
                }
            });
        })($wnd);
    }-*/;

    private native void initJsApi() /*-{
        var vScriptHost = this;
        $wnd.cubaJsApi = {
            makeServerCall: function (params) {
                if (params)
                    vScriptHost.@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::makeServerCall([Ljava/lang/String;)(params);
            }
        }
    }-*/;
}
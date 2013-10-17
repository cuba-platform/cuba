/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.toolkit.gwt.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.SimplePanel;
import com.haulmont.cuba.toolkit.gwt.client.ResourcesLoader;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VNotification;

/**
 * Component for evaluate custom JavaScript from server
 *
 * @author artamonov
 * @version $Id$
 */
public class VScriptHost extends SimplePanel implements Paintable {

    private static final boolean DEBUG = false;

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

    private JavaScriptObject windowUnloadHandler = null;

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
            if (!BrowserInfo.get().isIE7()) {
                injectAndInitHistory(client);
            }

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

    private void injectAndInitHistory(ApplicationConnection client) {
        windowUnloadHandler = getOnBeforeUnloadHandler();

        ResourcesLoader.injectJs(null, client.getAppUri(), "/js/json2.js");
        ResourcesLoader.injectJs(null, client.getAppUri(), "/js/jquery.history.js");

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                HistoryInjectAPI.onReady(new Runnable() {
                    @Override
                    public void run() {
                        initHistorySupport();

                        // we don't use onbeforeunload from history.js
                        setOnBeforeUnloadHandler(windowUnloadHandler);
                    }
                });
            }
        });
    }

    public native JavaScriptObject getOnBeforeUnloadHandler() /*-{
        return $wnd.onbeforeunload;
    }-*/;

    public native JavaScriptObject setOnBeforeUnloadHandler(JavaScriptObject jso) /*-{
        $wnd.onbeforeunload = jso;
    }-*/;

    public void handleHistoryBackAction() {
        // handle notifications
        if (VNotification.getLastNotification() != null) {
            VNotification.getLastNotification().fade();
		} else if (historyHandlerInitialized) {
            client.updateVariable(paintableId, HISTORY_BACK_ACTION, "performed", true);
		}
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        disableHistoryHandler();
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

    public static boolean isIE() {
        return BrowserInfo.get().isIE();
    }

    public static void log(String info) {
        if (DEBUG)
            VConsole.log(info);
    }

    private native void disableHistoryHandler()/*-{
        $wnd.historyBackListener = undefined;
    }-*/;

    protected String getUrl() {
        return Document.get().getURL();
    }

    private native void initHistorySupport()
    /*-{
        var vScriptHost = this;

        function logInfo(info) {
            @com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::log(Ljava/lang/String;)(info);
        }

        function initHandlers(wnd, History) {
            var timestamps = [];

            logInfo('>>> Init historyBackListener');
            wnd.historyBackListener = function() {
                if (!vScriptHost.@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::isAttached()()) {
                    logInfo('>>> Detached history handler');
                    return;
                }

                var State = History.getState();

                if (State.data.timestamp in timestamps) {
                    logInfo('>>> Skip fake history step');

                    delete timestamps[State.data.timestamp];
                } else {
                    var title = wnd.document.title;
                    logInfo('>>> Current title:  ' + title);

                    var goForward = function () {
                        var time = new Date().getTime();
                        timestamps[time] = time;

                        var currentLocation = vScriptHost.@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::getUrl()();

                        logInfo('>>> Current location: \'' + currentLocation + '\'');

                        // one step forward
                        var state = '?a';
                        if (currentLocation.indexOf('?a') >= 0)
                            state = '?b';

                        var data = {state: time, timestamp: time, rand: 2};
                        timestamps.push(time);

                        logInfo('>>> Push history step ' + state);
                        History.pushState(data, title, state);

                        logInfo('>>> Call server-side');
                        // call handler
                        vScriptHost.@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::handleHistoryBackAction()();
                    };

                    if (!@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::isIE()())
                        goForward();
                    else
                        setTimeout(goForward, 200);
                }
            };

            if (!wnd.callHistoryBackListener) {
                logInfo('>>> Init callHistoryBackListener');

                wnd.callHistoryBackListener = function () {
                    if (wnd.historyBackListener)
                        wnd.historyBackListener();
                };

                logInfo('>>> Bind callHistoryBackListener');
                History.Adapter.bind(wnd, 'statechange', wnd.callHistoryBackListener);
            }
        }

        function prepareHistory(wnd, History) {
            var currentLocation = vScriptHost.@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::getUrl()();

            logInfo('>>> Current location: ' + currentLocation);

            var title = wnd.document.title;
            logInfo('>>> Current title: ' + title);

            var state = '?a';
            if (currentLocation.indexOf('?a') >= 0)
                state = '?b';

            var time = new Date().getTime();
            var data = {state: time, timestamp: time, rand: 2};

            // base state for hash change
            if (@com.haulmont.cuba.toolkit.gwt.client.utils.VScriptHost::isIE()()) {
                logInfo('>>> Push base state: ' + state);
                History.pushState(data, title, '?c' + state);
            }

            logInfo('>>> Deffer history step init');
            // deffered change state
            setTimeout(function() {
                logInfo('>>> Init special state ' + state);
                History.pushState(data, title, state);

                logInfo('>>> Deffer history init handlers');
                setTimeout(function(){
                    logInfo('>>> Init history handlers');
                    initHandlers(wnd, History);
                }, 200);
            }, 200);
        }

        prepareHistory($wnd, $wnd.historyProvider.get());
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
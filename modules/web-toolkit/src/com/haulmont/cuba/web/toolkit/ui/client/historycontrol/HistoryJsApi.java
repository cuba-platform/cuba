/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.historycontrol;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.haulmont.cuba.web.toolkit.ui.client.logging.ClientLogger;
import com.haulmont.cuba.web.toolkit.ui.client.logging.ClientLoggerFactory;
import com.vaadin.client.BrowserInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class HistoryJsApi {

    protected ClientLogger logger = ClientLoggerFactory.getLogger("HistoryJsApi");

    protected Set<Long> fakeHistorySteps = new HashSet<Long>();

    public HistoryJsApi() {
        initOnReady();
    }

    private void init() {
        JavaScriptObject windowUnloadHandler = getOnBeforeUnloadHandler();

        logger.log(">>> Init history");
        initHistory();

        logger.log(">>> Init start state");
        initStartState();

        // we don't use onbeforeunload from history.js
        setOnBeforeUnloadHandler(windowUnloadHandler);
    }

    protected abstract void onHistoryBackPerformed();

    protected abstract boolean isEnabled();

    public void disable() {
        removeHistoryBackListener();
    }

    protected void initStartState() {
        String url = Document.get().getURL();
        logger.log(">>> Current location: '" + url + "'");

        final String title = Document.get().getTitle();
        logger.log(">>> Current title: " + title);

        String urlTail = "?a";
        if (url.contains("?a"))
            urlTail = "?b";

        if (BrowserInfo.get().isIE()) {
            logger.log(">>> Push base state for IE");

            pushState(new Date().getTime(), title, urlTail + "c");
        }

        final String finalUrlTail = urlTail;
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                logger.log(">>> Init start step");

                pushState(new Date().getTime(), title, finalUrlTail);

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        initHandler();
                    }
                });
            }
        });
    }

    protected void initHandler() {
        logger.log(">>> Init handler");

        assignHistoryBackListener(new Runnable() {
            @Override
            public void run() {
                handleHistoryStep();
            }
        });
    }

    protected void handleHistoryStep() {
        if (!isEnabled()) {
            logger.log(">>> Disabled history handler");
            disable();
            return;
        }

        Long stateTime = getStateTimeStamp();

        if (logger.enabled) {
            if (stateTime != null) {
                logger.log(">>> Check step: " + stateTime.toString());
            } else {
                logger.log(">>> Check step: undefined");
            }
        }

        if (stateTime != null && fakeHistorySteps.contains(stateTime.longValue())) {
            logger.log(">>> Skip fake history step");

            fakeHistorySteps.remove(stateTime);
        } else {
            if (!BrowserInfo.get().isIE()) {
                goForward();
            } else {
                goForwardDelayed();
            }
        }
    }

    protected void goForward() {
        String title = Document.get().getTitle();
        logger.log(">>> Current title: " + title);

        Long time = new Date().getTime();
        fakeHistorySteps.add(time);

        String url = Document.get().getURL();
        logger.log(">>> Current location: '" + url + "'");

        String urlTail = "?a";
        if (url.contains("?a"))
            urlTail = "?b";

        logger.log(">>> Push history step: " + time + " " + urlTail);
        pushState(time, title, urlTail);

        logger.log(">>> Call history back handler");
        onHistoryBackPerformed();
    }

    protected void goForwardDelayed() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                goForward();
            }
        });
    }

    // wait for jQuery and jQuery.history then init
    protected native void initOnReady() /*-{
        var historyControl = this;

        if (!$wnd.jQuery || !$wnd.importHistoryJsLibrary) {
            var id = $wnd.setInterval(function () {
                if ($wnd.jQuery && $wnd.importHistoryJsLibrary) {
                    $wnd.clearInterval(id);
                    historyControl.@com.haulmont.cuba.web.toolkit.ui.client.historycontrol.HistoryJsApi::init()();
                }
            }, 100);
        } else {
            historyControl.@com.haulmont.cuba.web.toolkit.ui.client.historycontrol.HistoryJsApi::init()();
        }
    }-*/;

    public native JavaScriptObject getOnBeforeUnloadHandler() /*-{
        return $wnd.onbeforeunload;
    }-*/;

    public native void setOnBeforeUnloadHandler(JavaScriptObject jso) /*-{
        $wnd.onbeforeunload = jso;
    }-*/;

    protected native void assignHistoryBackListener(Runnable historyBackListener) /*-{
        $wnd.historyBackListener = function() {
            historyBackListener.@java.lang.Runnable::run()();
        };
    }-*/;

    protected native void removeHistoryBackListener() /*-{
        $wnd.historyBackListener = undefined;
    }-*/;

    protected native void initHistory()/*-{
        $wnd.importHistoryJsLibrary();

        this.History = $wnd.History;
        if (!$wnd.callHistoryBackListener) {
            $wnd.callHistoryBackListener = function () {
                if ($wnd.historyBackListener)
                    $wnd.historyBackListener();
            };
            this.History.Adapter.bind($wnd, 'statechange', $wnd.callHistoryBackListener);
        }
    }-*/;

    protected native Long getStateTimeStamp() /*-{
        return this.History.getState().data.timestamp;
    }-*/;

    protected native void pushState(Long stateId, String title, String urlTail) /*-{
        var data = {state: stateId, timestamp: stateId, rand: 2};
        this.History.pushState(data, title, urlTail);
    }-*/;
}
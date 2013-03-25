/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.toolkit.gwt.client.utils;

import com.vaadin.terminal.gwt.client.VConsole;

/**
 * Check dynamically injected jquery.history.js and run handler if ready
 *
 * @author artamonov
 * @version $Id$
 */
public class HistoryInjectAPI {
    public static native void onReady(Runnable r) /*-{
        if (!$wnd.historyProvider) {
            var id = $wnd.setInterval(function () {
                if ($wnd.historyProvider) {
                    $wnd.clearInterval(id);
                    @com.haulmont.cuba.toolkit.gwt.client.utils.HistoryInjectAPI::execRunnable(Ljava/lang/Runnable;)(r);
                }
            }, 100);
        } else {
            @com.haulmont.cuba.toolkit.gwt.client.utils.HistoryInjectAPI::execRunnable(Ljava/lang/Runnable;)(r);
        }
    }-*/;

    private static void execRunnable(Runnable r) {
        if (r != null) {
            try {
                r.run();
            } catch (Throwable e) {
                VConsole.log(e.toString());
            }
        }
    }
}
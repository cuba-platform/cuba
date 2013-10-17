/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 18.10.2010 19:22:04
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.swfupload;

import com.vaadin.terminal.gwt.client.VConsole;

public class SwfUploadAPI {
    public static native void onReady(Runnable r) /*-{
        if (!$wnd.swfUploadHelper) {
            var id = $wnd.setInterval(function () {
                if ($wnd.swfUploadHelper) {
                    $wnd.clearInterval(id);
                    @com.haulmont.cuba.toolkit.gwt.client.swfupload.SwfUploadAPI::execRunnable(Ljava/lang/Runnable;)(r);
                }
            }, 100);
        } else {
            @com.haulmont.cuba.toolkit.gwt.client.swfupload.SwfUploadAPI::execRunnable(Ljava/lang/Runnable;)(r);
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
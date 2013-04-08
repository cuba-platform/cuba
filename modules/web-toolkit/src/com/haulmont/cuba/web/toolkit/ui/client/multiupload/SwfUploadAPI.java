/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.vaadin.client.VConsole;

/**
 * @author artamonov
 * @version $Id$
 */
public class SwfUploadAPI {
    public static native void onReady(Runnable r) /*-{
        if (!$wnd.swfUploadHelper) {
            var id = $wnd.setInterval(function () {
                if ($wnd.swfUploadHelper) {
                    $wnd.clearInterval(id);
                    @com.haulmont.cuba.web.toolkit.ui.client.multiupload.SwfUploadAPI::execRunnable(Ljava/lang/Runnable;)(r);
                }
            }, 100);
        } else {
            @com.haulmont.cuba.web.toolkit.ui.client.multiupload.SwfUploadAPI::execRunnable(Ljava/lang/Runnable;)(r);
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
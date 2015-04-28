/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import java.util.logging.Level;
import java.util.logging.Logger;

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
                java.util.logging.Logger logger = Logger.getLogger("SwfUploadAPI");
                logger.log(Level.WARNING, e.getMessage() == null ? "" : e.getMessage(), e);
            }
        }
    }
}
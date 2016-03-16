/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 */
public class SwfUploadAPI {
    public static native void onReady(Runnable r) /*-{
        if (!$wnd.swfUploadHelper) {
            var id = $wnd.setInterval($entry(function () {
                if ($wnd.swfUploadHelper) {
                    $wnd.clearInterval(id);
                    @com.haulmont.cuba.web.toolkit.ui.client.multiupload.SwfUploadAPI::execRunnable(Ljava/lang/Runnable;)(r);
                }
            }), 100);
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
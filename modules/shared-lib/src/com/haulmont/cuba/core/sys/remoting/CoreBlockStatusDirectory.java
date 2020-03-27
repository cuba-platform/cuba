/*
 * Copyright (c) 2008-2020 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * INTERNAL.
 * <p>
 * This class holds information about middleware block(s) that started or tried to start in this application server
 * (using the same shared classloader).
 * Information can be used by web client for troubleshooting purposes.
 *
 * It must be loaded to a classloader shared between the client tier and middleware.
 */
public class CoreBlockStatusDirectory {
    /**
     * Map: web context name --> block status.
     */
    private static Map<String, StatusInfo> blocks = new ConcurrentHashMap<>();

    public static Map<String, StatusInfo> getBlocks() {
        return Collections.unmodifiableMap(blocks);
    }

    public static void registerStartSuccess(String webContextName) {
        blocks.put(webContextName, StatusInfo.success(webContextName));
    }

    public static void registerStartFail(String webContextName, Throwable exception) {
        blocks.put(webContextName, StatusInfo.fail(webContextName, exception));
    }

    public static class StatusInfo {
        private String webContextName;
        private boolean success;

        /**
         * Exception that prevented middleware block to start successfully.
         */
        private Throwable exception;

        private StatusInfo() {
        }

        public static StatusInfo success(String webContextName) {
            StatusInfo res = new StatusInfo();
            res.webContextName = webContextName;
            res.success = true;
            return res;
        }

        public static StatusInfo fail(String webContextName, Throwable exception) {
            StatusInfo res = new StatusInfo();
            res.webContextName = webContextName;
            res.success = false;
            res.exception = exception;
            return res;
        }

        public String getWebContextName() {
            return webContextName;
        }

        public boolean isSuccess() {
            return success;
        }

        public Throwable getException() {
            return exception;
        }
    }
}

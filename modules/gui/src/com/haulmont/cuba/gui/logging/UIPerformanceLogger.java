/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.logging;

/**
 * Logger class for UI performance stats
 * Contains constants for screen life cycle
 *
 * @author artamonov
 * @version $Id$
 */
public interface UIPerformanceLogger {

    enum LifeCycle {
        LOAD("load"),
        XML("xml"),
        INIT("init"),
        READY("ready"),
        SET_ITEM("setItem"),
        UI_PERMISSIONS("uiPermissions"),
        INJECTION("inject"),
        COMPANION("companion");

        private String name;

        LifeCycle(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.executors.impl;

/**
 * @author artamonov
 * @version $Id$
 */
public class ActiveBackgroundTasksLimitException extends RuntimeException {

    public ActiveBackgroundTasksLimitException(String message) {
        super(message);
    }
}